package modele;

import org.bson.Document;

//import java.util.LinkedList;
//import java.util.List;

public class Equipe {

	//Attributes
	private String nomEquipe;
	
	//private List<Participant> listParticipants;
	private int nbParticipants;
	
	//private Participant capitaine;
	private String matriculeCap;
	
	//private Ligue ligue;
	private String nomLigue;
	
	//private List<Resultat> listResultats;
	private int nbResultats;
	
	/**
	 * Constructeur par défaut
	 */
	public Equipe() {
	}
	
	/**
	 * Constructeur prenant les informations du document d
	 * @param d
	 */
	public Equipe(Document d)
    {
    	this.nomLigue = d.getString("nomLigue");
    	this.nomEquipe = d.getString("nomEquipe");
    	this.matriculeCap = d.getString("matriculeCap");
    	this.nbParticipants = d.getInteger("nbParticipants");
    	this.nbResultats = d.getInteger("nbResultats");
    }
	
	/**
	 * Constructeur de confort à 3 éléments
	 * @param ligue
	 * @param nomEquipe
	 * @param matriculeCap
	 */
	public Equipe(String nomLigue, String nomEquipe, String matriculeCap) {
		this.nomEquipe = nomEquipe;
		this.nbParticipants = 0;
		this.matriculeCap = matriculeCap;
		this.nomLigue = nomLigue;
		this.nbResultats = 0;
	}

	/**
	 * Les getteurs et setteurs
	 */
	public String getNomEquipe() {
		return nomEquipe;
	}

	public void setNomEquipe(String nomEquipe) {
		this.nomEquipe = nomEquipe;
	}
	
	public int getNbParticipants() {
		return nbParticipants;
	}

	public void setNbParticipants(int nbParticipants) {
		this.nbParticipants = nbParticipants;
	}

	public String getMatriculeCap() {
		return matriculeCap;
	}

	public void setMatriculeCap(String matriculeCap) {
		this.matriculeCap = matriculeCap;
	}

	public String getNomLigue() {
		return nomLigue;
	}

	public void setNomLigue(String nomLigue) {
		this.nomLigue = nomLigue;
	}

	public int getNbResultats() {
		return nbResultats;
	}

	public void setNbResultats(int nbResultats) {
		this.nbResultats = nbResultats;
	}
	
	/**
	 * Ajoute un participant à la liste de participants d'une equipe
	 */
	public void ajouterJoueur() {
		this.nbParticipants++;
	}
	
	/**
	 * Supprime un participant de la liste de participants d'une equipe
	 */
	public void supprimerJoueur() {
		this.nbParticipants--;
	}
	
	/**
	 * Ajoute un resultat à la liste de resultats d'une equipe
	 */
	public void ajouterResultat() {
		this.nbResultats++;
	}
	
	/**
	 * Supprime un resultat de la liste de resultats d'une equipe
	 */
	public void supprimerResultat() {
		this.nbResultats--;
	}
	
	public boolean isActive() {
		boolean testIsActive = true;
		if(this.nbParticipants == 0) {
			testIsActive = false;
		}
		return testIsActive;
	}
	
	 @Override
	public String toString() {
		return "Equipe "+ nomEquipe + " (matriculeCap=" + matriculeCap + ", nomLigue=" + nomLigue +")";
	}
	 
	public Document toDocument()
	{
	    return new Document().append("nomLigue", nomLigue)
	    			         .append("nomEquipe", nomEquipe)
	    			         .append("matriculeCap", matriculeCap)
	    			         .append("nbParticipants", nbParticipants)
	    			         .append("nbResultats", nbResultats);
	}
}
