package net.sourceforge.ondex.parser.ecocyc.parse.transformers;

import java.util.HashSet;

import net.sourceforge.ondex.config.ValidatorRegistry;
import net.sourceforge.ondex.core.AttributeName;
import net.sourceforge.ondex.core.ConceptClass;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.core.RelationType;
import net.sourceforge.ondex.event.type.AttributeNameMissingEvent;
import net.sourceforge.ondex.event.type.ConceptClassMissingEvent;
import net.sourceforge.ondex.event.type.RelationTypeMissingEvent;
import net.sourceforge.ondex.parser.ecocyc.MetaData;
import net.sourceforge.ondex.parser.ecocyc.Parser;
import net.sourceforge.ondex.parser.ecocyc.objects.AbstractNode;
import net.sourceforge.ondex.parser.ecocyc.objects.Gene;
/**
 * Transforms a net.sourceforge.ondex.parser.ecocyc.sink.Gene to a Concept.
 * @author peschr
 */
public class GeneTransformer extends AbstractTransformer {

	ConceptClass ccGene = null;

	private RelationType rtSetIsPartOf = null;
	private AttributeName attTaxId = null;
	
	public GeneTransformer(Parser parser) {
		super(parser);
		try {

			ccGene = graph.getMetaData().getConceptClass(MetaData.CC_Gene);
			if (ccGene == null) {
				Parser.propagateEventOccurred(new ConceptClassMissingEvent(MetaData.CC_Gene, Parser.getCurrentMethodName()));
			}
			
			rtSetIsPartOf = graph.getMetaData().getRelationType(MetaData.RT_ENCODED_BY);
			if (rtSetIsPartOf == null) {
				Parser.propagateEventOccurred(new RelationTypeMissingEvent(MetaData.RT_ENCODED_BY, Parser.getCurrentMethodName()));
			}
			
			attTaxId = graph.getMetaData().getAttributeName(MetaData.ATR_TAXID);
			if (attTaxId == null) {
				Parser.propagateEventOccurred(new AttributeNameMissingEvent(MetaData.ATR_TAXID, Parser.getCurrentMethodName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void nodeToConcept(AbstractNode node) {
		Gene gene = (Gene) node;
		ONDEXConcept concept = graph.getFactory().createConcept(gene.getUniqueId(),
				dataSourceMetaC, ccGene, etIMPD);
		
		if (gene.getSpecies() != null) {
			String species = gene.getSpecies();
			if (Parser.speciesNames.containsKey(species))
				species = Parser.speciesNames.get(species);
			String taxId = (String) ValidatorRegistry.validators.get("taxonomy")
					.validate(species);
			if (taxId != null)
				concept.createAttribute(attTaxId, taxId, false);
			else
				System.err.println("No mapping for species: "
						+ gene.getSpecies());
		}  else 
			concept.createAttribute(attTaxId, MetaData.TAXID, false);
		gene.setConcept(concept);
		//super.addCommonDetailsToConcept(concept, node);
	}

	@Override
	public void pointerToRelation(AbstractNode node) {
		Gene gene = (Gene) node;
		HashSet<ONDEXConcept> context = new HashSet<ONDEXConcept>();
		if ( gene.getProduct() != null ){
			context = super.getNonRedundant(gene.getConcept(), gene.getProduct().getConcept());
			super.copyContext(node.getConcept(), context);
		}
		if (gene.getProduct() != null) {
			// Protein (encoded by)=> Gene
			super.copyContext(
			graph.getFactory().createRelation(gene.getProduct().getConcept(), gene
					.getConcept(), rtSetIsPartOf, etIMPD),context);
		}
	}
}
