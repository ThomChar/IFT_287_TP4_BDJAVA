package modele;

import org.bson.Document;

public class Ligue {
	
	// Attributes
	private String nomLigue;
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
	 * Les getters et setters
	 */
	public String getNomLigue() {
		return nomLigue;
	}

	public void setNomLigue(String nomLigue) {
		this.nomLigue = nomLigue;
	}

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
	/**
	 * Supprime une equipe de la liste d'equipe d'une ligue
	 */
	public void supprimerEquipe() {
		this.nbEquipes--;
	}

	@Override
	public String toString() {
		return "Ligue '"+ nomLigue + "' (nbJoueurMaxParEquipe= "
				+ nbJoueurMaxParEquipe + ")";
	}

	public Document toDocument()
    {
    	return new Document().append("nomLigue", nomLigue)
    			             .append("nbJoueurMaxParEquipe", nbJoueurMaxParEquipe)
    			             .append("nbEquipes", nbEquipes);
    }
}