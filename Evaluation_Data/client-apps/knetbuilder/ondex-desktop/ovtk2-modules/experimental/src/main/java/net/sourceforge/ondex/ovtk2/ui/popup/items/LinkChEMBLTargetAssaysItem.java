//package net.sourceforge.ondex.ovtk2.ui.popup.items;
//
//import java.awt.Color;
//import java.awt.Component;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//import javax.swing.JOptionPane;
//
//import net.sourceforge.ondex.core.ConceptAccession;
//import net.sourceforge.ondex.core.ConceptClass;
//import net.sourceforge.ondex.core.DataSource;
//import net.sourceforge.ondex.core.ONDEXConcept;
//import net.sourceforge.ondex.core.ONDEXGraph;
//import net.sourceforge.ondex.ovtk2.config.Config;
//import net.sourceforge.ondex.ovtk2.ui.popup.EntityMenuItem;
//import net.sourceforge.ondex.ovtk2.util.LayoutNeighbours;
//import net.sourceforge.ondex.ovtk2.util.chemical.BioactivitiesDocumentFilter;
//import net.sourceforge.ondex.parser.chemblactivity.Parser;
//import net.sourceforge.ondex.parser.chemblactivity.Parser.EXMODE;
//
//import org.w3c.dom.Document;
//
///**
// * Ondex queries the ChEMBL webservice with the compound(s) accession numbers
// * for assays, and then creates concepts from the results and adds them to the
// * graph
// * 
// * @author taubertj
// * 
// */
//public class LinkChEMBLTargetAssaysItem extends EntityMenuItem<ONDEXConcept> {
//
//	@Override
//	public boolean accepts() {
//
//		// get meta data
//		ONDEXGraph graph = viewer.getONDEXJUNGGraph();
//		ConceptClass ccTarget = graph.getMetaData().getConceptClass("Target");
//		DataSource dsTARGET = graph.getMetaData().getDataSource("CHEMBLTARGET");
//
//		// look at all selected targets
//		for (ONDEXConcept c : entities) {
//			if (c.getOfType().equals(ccTarget)) {
//				for (ConceptAccession ca : c.getConceptAccessions()) {
//					// at least one accession with source CHEMBL
//					if (ca.getElementOf().equals(dsTARGET)) {
//						return true;
//					}
//				}
//			}
//		}
//
//		return false;
//	}
//
//	@Override
//	protected void doAction() {
//
//		// get meta data
//		ONDEXGraph graph = viewer.getONDEXJUNGGraph();
//		ONDEXConcept center = null;
//		ConceptClass ccTarget = graph.getMetaData().getConceptClass("Target");
//		DataSource dsTARGET = graph.getMetaData().getDataSource("CHEMBLTARGET");
//
//		// parse all accessions contained in graph
//		Map<String, Set<ONDEXConcept>> accessions = new HashMap<String, Set<ONDEXConcept>>();
//		for (ONDEXConcept c : entities) {
//			if (c.getOfType().equals(ccTarget)) {
//				for (ConceptAccession ca : c.getConceptAccessions()) {
//					if (ca.getElementOf().equals(dsTARGET)) {
//						if (!accessions.containsKey(ca.getAccession()))
//							accessions.put(ca.getAccession(),
//									new HashSet<ONDEXConcept>());
//						accessions.get(ca.getAccession()).add(c);
//						center = c;
//					}
//				}
//			}
//		}
//
//		Parser activities = new Parser();
//		activities.setONDEXGraph(graph);
//		activities.initMetaData();
//		try {
//			EXMODE mode = EXMODE.TargetToAssays;
//
//			Map<String, Document> docs = activities.retrieveXML(accessions,
//					mode);
//
//			BioactivitiesDocumentFilter filter = new BioactivitiesDocumentFilter(docs, mode);
//
//			// ask user for filter
//			int option = JOptionPane.showConfirmDialog((Component) viewer,
//					filter, "Filter on properties",
//					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
//			if (option == JOptionPane.OK_OPTION) {
//
//				docs = filter.getFiltered();
//				
//				Set<ONDEXConcept> created = activities.parseActivities(docs,
//						accessions, mode);
//				System.out
//						.println("Added " + created.size() + " new concepts.");
//
//				// make new concepts visible
//				viewer.getONDEXJUNGGraph().setVisibility(created, true);
//				for (ONDEXConcept c : created) {
//					// set something like default attributes
//					viewer.getNodeColors().updateColor(c,
//							Config.getColorForConceptClass(c.getOfType()));
//					viewer.getNodeDrawPaint().updateColor(c, Color.BLACK);
//					viewer.getNodeShapes().updateShape(c);
//
//					// make all relations visible
//					viewer.getONDEXJUNGGraph().setVisibility(
//							graph.getRelationsOfConcept(c), true);
//				}
//
//				// layout nodes on big circle
//				LayoutNeighbours.layoutNodes(viewer.getVisualizationViewer(),
//						center, created);
//
//				if (viewer.getMetaGraph() != null)
//					viewer.getMetaGraph().updateMetaData();
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	@Override
//	public MENUCATEGORY getCategory() {
//		return MENUCATEGORY.LINK;
//	}
//
//	@Override
//	protected String getMenuPropertyName() {
//		return "Viewer.VertexMenu.LinkChEMBLTargetAssays";
//	}
//
//	@Override
//	protected String getUndoPropertyName() {
//		return "";
//	}
//
//}
