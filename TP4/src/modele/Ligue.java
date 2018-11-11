package modele;

//import java.util.LinkedList;
//import java.util.List;

import org.bson.Document;

public class Ligue {
	
	// Attributes
	private String nomLigue;
	//private List<Equipe> listEquipes;
	private int nbJoueurMaxParEquipe;
	private int nbEquipes;

	/**
	 * constructeur par défaut
	 */
	public Ligue() {
	}
	
	/**
	 * constructeur de confort à 2 arguments
	 * @param nomLigue
	 * @param nbJoueurMaxParEquipe
	 */
	public Ligue(String nomLigue, int nbJoueurMaxParEquipe) {
		this.nomLigue = nomLigue;
		//this.listEquipes = new LinkedList<Equipe>(); //new LinkedList<Equipe>()
		this.setNbJoueurMaxParEquipe(nbJoueurMaxParEquipe);
		this.nbEquipes = 0;
	}
	
	/**
	 * Constructeur prenant les informations du document d
	 * @param d
	 */
	public Ligue(Document d)
    {
    	this.nomLigue = d.getString("nomLigue");
    	this.nbJoueurMaxParEquipe = d.getInteger("nbJoueurMaxParEquipe");
    	this.nbEquipes = d.getInteger("nbEquipes");
    }
	
	/**
	 * Constructeur de confort à 3 arguments
	 * @param nomLigue
	 * @param listEquipes
	 * @param nbJoueurMaxParEquipe
	 */
	/*public Ligue(String nomLigue, List<Equipe> listEquipes, int nbJoueurMaxParEquipe) {
		super();
		this.nomLigue = nomLigue;
		this.listEquipes = listEquipes;
		this.setNbJoueurMaxParEquipe(nbJoueurMaxParEquipe);
	}*/

	/**
	 * Les getters et setters
	 */
	public String getNomLigue() {
		return nomLigue;
	}

	public void setNomLigue(String nomLigue) {
		this.nomLigue = nomLigue;
	}

	/*public List<Equipe> getListEquipes() {
		return listEquipes;
	}

	public void setListEquipes(List<Equipe> listEquipes) {
		this.listEquipes = listEquipes;
	}*/

	public int getNbJoueurMaxParEquipe() {
		return nbJoueurMaxParEquipe;
	}

	public void setNbJoueurMaxParEquipe(int nbJoueurMaxParEquipe) {
		this.nbJoueurMaxParEquipe = nbJoueurMaxParEquipe;
	}
	
	public int getNbEquipes() {
		return nbEquipes;
	}

	public void setNbEquipes(int nbEquipes) {
		this.nbEquipes = nbEquipes;
	}

	/**
	 * Ajout une equipe à la liste d'equipe d'une ligue
	 */
	public void ajouterEquipe() {
		this.nbEquipes++;
	}
	/*public void ajouteEquipe(Equipe equipe) {
		listEquipes.add(equipe);
	}*/

	/**
	 * Supprime une equipe de la liste d'equipe d'une ligue
	 */
	public void supprimerEquipe() {
		this.nbEquipes--;
	}
	/*public void supprimerEquipe(Equipe equipe) {
		listEquipes.remove(equipe);
	}*/

	@Override
	public String toString() {
		return "Ligue [nomLigue= " + nomLigue + ", nbJoueurMaxParEquipe= "
				+ nbJoueurMaxParEquipe + "]";
	}
	
	 public Document toDocument()
	    {
	    	return new Document().append("nomLigue", nomLigue)
	    			             .append("nbJoueurMaxParEquipe", nbJoueurMaxParEquipe)
	    			             .append("nbEquipes", nbEquipes);
	    }
}