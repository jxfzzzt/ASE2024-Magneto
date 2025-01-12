package net.sourceforge.ondex.parser.aracyc.objects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sourceforge.ondex.core.ONDEXConcept;
/**
 * Represents an abstract sink object and provides basis functionality
 * @author peschr
 *
 */
public abstract class AbstractNode {
	private String uniqueId;
	private String commonName;
	private Set<Dblink> dbLink = new HashSet<Dblink>();
	private String comment;
	
	private HashSet<String> synonym = new HashSet<String>();
	private HashMap<String,Publication> publications = new HashMap<String,Publication>();
	public void addPublication(Publication pub){
		publications.put(pub.getUniqueId(),pub);
	}
	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public String getUniqueId() {
		return uniqueId;
	}
	public Set<Dblink> getDbLink() {
		return dbLink;
	}
	public void setDbLink(Dblink dbLink) {
		this.dbLink.add(dbLink);
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	private ONDEXConcept concept;
	public void setConcept(ONDEXConcept concept) {
		this.concept = concept;
	}
	public ONDEXConcept getConcept() {
		return concept;
	}
	
	public Iterator<Publication> getPublications() {
		return publications.values().iterator();
	}
	public String toString(){
		return this.getUniqueId() +"@"+ this.hashCode();
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public HashSet<String> getSynonym() {
		return synonym;
	}
	/**
	 * Adds a synonym if the synonym is not already assigned
	 * @param synonym
	 */
	public void addSynonym(String synonym) {
		if(!synonym.equals(this.commonName))
			this.synonym.add(synonym);
	}
}
