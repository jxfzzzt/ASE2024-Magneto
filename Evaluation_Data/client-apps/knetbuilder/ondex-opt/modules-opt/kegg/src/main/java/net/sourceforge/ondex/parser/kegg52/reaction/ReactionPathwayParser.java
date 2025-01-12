/*
 * Created on 19-May-2005
 *
 */
package net.sourceforge.ondex.parser.kegg52.reaction;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import com.sleepycat.persist.EntityCursor;

import net.sourceforge.ondex.event.type.DataFileErrorEvent;
import net.sourceforge.ondex.event.type.InconsistencyEvent;
import net.sourceforge.ondex.parser.kegg52.MetaData;
import net.sourceforge.ondex.parser.kegg52.Parser;
import net.sourceforge.ondex.parser.kegg52.data.Entry;
import net.sourceforge.ondex.parser.kegg52.data.Pathway;
import net.sourceforge.ondex.parser.kegg52.data.Reaction;
import net.sourceforge.ondex.parser.kegg52.sink.Concept;
import net.sourceforge.ondex.parser.kegg52.sink.ConceptAcc;
import net.sourceforge.ondex.parser.kegg52.sink.ConceptName;
import net.sourceforge.ondex.parser.kegg52.sink.ConceptWriter;
import net.sourceforge.ondex.parser.kegg52.sink.Relation;
import net.sourceforge.ondex.parser.kegg52.util.DPLPersistantSet;
import net.sourceforge.ondex.parser.kegg52.util.Util;


/**
 * @author taubertj
 */
public class ReactionPathwayParser {

    /**
     * Creates a formulaic represtentation of the reaction
     *
     * @param reaction
     * @return a formulaic represtentation of the reaction
     */
    public static String constructFormula(Reaction reaction) {
        StringBuilder reactionName = new StringBuilder();
        Iterator<Entry> subs = reaction.getSubstrates().iterator();
        int left = reaction.getSubstrates().size();
        while (subs.hasNext()) {
            reactionName.append((subs.next().getName()));
            if (left > 1) reactionName.append(" + ");
            left--;
        }

        if (reaction.getType().equalsIgnoreCase("reversible")) {
            reactionName.append(" <=> ");
        } else if (reaction.getType().equalsIgnoreCase("irreversible")) {
            reactionName.append(" => ");
        } else {
            reactionName.append(" => ");
        }

        Iterator<Entry> prods = reaction.getSubstrates().iterator();
        left = reaction.getSubstrates().size();
        while (prods.hasNext()) {
            reactionName.append((prods.next().getName()));
            if (left > 1) reactionName.append(" + ");
            left--;
        }

        return reactionName.toString();
    }

    public static void parseAndWrite(DPLPersistantSet<Pathway> pathways,
                                     DPLPersistantSet<Relation> relationsCache) {

        final Pattern spaceSplit = Pattern.compile(" ");

        ConceptWriter cw = Parser.getConceptWriter();
        Util util = Parser.getUtil();

        Set<String> writtenReactions = new HashSet<String>();

        EntityCursor<Pathway> cursor = pathways.getCursor();
        Iterator<Pathway> itPath = cursor.iterator();
        while (itPath.hasNext()) {
            Pathway pathway = itPath.next();
            Iterator<Reaction> itReact = pathway.getReactions().values().iterator();
            while (itReact.hasNext()) {

                //reaction as concept
                Reaction reaction = itReact.next();
                Concept concept_reaction = new Concept(reaction.getName().toUpperCase(), MetaData.CV_KEGG, MetaData.CC_REACTION);
                concept_reaction.setDescription(reaction.getType());

                ConceptName cn = new ConceptName("DRN:" + reaction.getName(), constructFormula(reaction));
                cn.setPreferred(false);
                concept_reaction.getConceptNames().add(cn);

                ConceptAcc ca = new ConceptAcc(reaction.getName(), reaction.getName(), MetaData.CV_KEGG);
                ca.setAmbiguous(false);
                concept_reaction.getConceptAccs().add(ca);

                concept_reaction.addContext(pathway.getId());

                if (!writtenReactions.contains(concept_reaction.getId())) {
                    writtenReactions.add(concept_reaction.getId());
                    util.writeConcept(concept_reaction);
                }

                //m_isp relation between pathway and reaction
                Relation m_isp = new Relation(concept_reaction.getId(), pathway.getId(), MetaData.RT_MEMBER_PART_OF);
                m_isp.setFrom_element_of(MetaData.CV_KEGG);
                m_isp.setTo_element_of(MetaData.CV_KEGG);
                if (relationsCache.contains(m_isp.pk)) {
                    m_isp = relationsCache.get(m_isp.pk);
                }
                m_isp.addContext(pathway.getId());
                relationsCache.add(m_isp);

                //cs_by relation for all substrates
                Iterator<Entry> subIt = reaction.getSubstrates().iterator();
                while (subIt.hasNext()) {
                    Entry substrate = subIt.next();
                    Relation cs_by = new Relation(substrate.getName(), reaction.getName(), MetaData.RT_CONSUMED_BY);
                    cs_by.setFrom_element_of(MetaData.CV_KEGG);
                    cs_by.setTo_element_of(MetaData.CV_KEGG);
                    if (relationsCache.contains(cs_by.pk)) {
                        cs_by = relationsCache.get(cs_by.pk);
                    }
                    cs_by.addContext(pathway.getId());
                    relationsCache.add(cs_by);
                }

                //pd_by relation for all products
                Iterator<Entry> reactIt = reaction.getProducts().iterator();
                while (reactIt.hasNext()) {
                    Entry product = reactIt.next();
                    Relation pd_by = new Relation(product.getName(), reaction.getName(), MetaData.RT_PRODUCED_BY);
                    pd_by.setFrom_element_of(MetaData.CV_KEGG);
                    pd_by.setTo_element_of(MetaData.CV_KEGG);
                    if (relationsCache.contains(pd_by.pk)) {
                        pd_by = relationsCache.get(pd_by.pk);
                    }
                    pd_by.addContext(pathway.getId());
                    relationsCache.add(pd_by);
                }
            }
        }
        pathways.closeCursor(cursor);

        cursor = pathways.getCursor();
        itPath = cursor.iterator();
        while (itPath.hasNext()) {
            Pathway pathway = itPath.next();

            //go through all entries
            Iterator<Entry> itEntries = pathway.getEntries().values().iterator();
            while (itEntries.hasNext()) {
                Entry entry = itEntries.next();

                //ca_by relation between an entry and a reaction
                if (entry.getReaction() != null) {
                    String[] results = spaceSplit.split(entry.getReaction().toUpperCase());
                    for (String result : results) {
                        if (result.trim().replaceAll("[^0-9]", "").length() != 5) {
                            Parser.propagateEventOccurred(new InconsistencyEvent("Reaction " + result + " is not a valid 5 digit reaction code", "ReactionPathwayParser"));
                            continue;
                        }

                        String[] concepts = spaceSplit.split(entry.getName().toUpperCase());
                        for (String concept : concepts) {
                            String conceptID = concept + "_EN";
                            if (cw.conceptParserIDIsWritten(conceptID)) {
                                Relation ca_by = new Relation(result, conceptID, MetaData.RT_CATALYSED_BY);
                                ca_by.setFrom_element_of(MetaData.CV_KEGG);
                                ca_by.setTo_element_of(MetaData.CV_KEGG);
                                if (relationsCache.contains(ca_by.pk)) {
                                    ca_by = relationsCache.get(ca_by.pk);
                                }
                                ca_by.addContext(pathway.getId());
                                relationsCache.add(ca_by);
                            } else {
                                DataFileErrorEvent dfi =
                                        new DataFileErrorEvent("Missing concept: " + conceptID, "");
                                Parser.propagateEventOccurred(dfi);
                                System.err.println("Missing concept: " + conceptID + " For Reaction");
                            }
                        }
                    }
                }
            }
        }
        pathways.closeCursor(cursor);

        util.writeRelations(relationsCache);

	}

}
