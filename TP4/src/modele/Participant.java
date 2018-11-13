package modele;

import org.bson.Document;

public class Participant {

	//Attributes
	private String matricule;
	private String prenom;
	private String nom;
	private String motDePasse;
	private String statut;
	//private Equipe equipe;
	private String nomEquipe;
	
	/**
	 * constructeur par défaut
	 */
	public Participant() {
	}
	
	/**
	 * Constructeur prenant les informations du document d
	 * @param d
	 */
	public Participant(Document d)
    {
    	this.matricule = d.getString("matricule");
    	this.prenom = d.getString("prenom");
    	this.nom = d.getString("nom");
    	this.motDePasse = d.getString("motDePasse");
    	this.statut = d.getString("statut");
    	this.nomEquipe = d.getString("nomEquipe");
    }
	
	/**
	 * constructeur à 4 arguments
	 * @param matricule
	 * @param prenom
	 * @param nom
	 * @param motDePasse
	 */
	public Participant(String matricule, String prenom, String nom, String motDePasse) {
		this.matricule = matricule;
		this.prenom = prenom;
		this.nom = nom;
		this.motDePasse = motDePasse;
		//this.equipe = null;
		this.setNomEquipe(null);
		this.statut = null;
	}
	
	/**
	 * constructeur à 5 éléments
	 * @param matricule
	 * @param prenom
	 * @param nom
	 * @param motDePasse
	 * @param equipe
	 */
	public Participant(String matricule, String prenom, String nom, String motDePasse, String nomEquipe) {
		this.matricule = matricule;
		this.prenom = prenom;
		this.nom = nom;
		this.motDePasse = motDePasse;
		this.setNomEquipe(nomEquipe);
		this.statut = null;
	}
	
	/**
	 * Les getters et setters
	 */
	public String getMatricule() {
		return matricule;
	}
	
	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getMotDePasse() {
		return motDePasse;
	}
	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}
	
	/*public Equipe getEquipe() {
		return equipe;
	}

	public void setEquipe(Equipe equipe) {
		this.equipe = equipe;
	}*/
	
	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}
	

	public String getNomEquipe() {
		return nomEquipe;
	}

	public void setNomEquipe(String nomEquipe) {
		this.nomEquipe = nomEquipe;
	}
	
	/**
	 * Savoir si un joueur joue dans une équipe
	 * @return retourne vrai si le joueur joue dans une équipe, sinon faux
	 */
	public boolean isActive() {
		boolean testIsActive = true;
		if(this.getStatut() == null || this.getStatut().equals("SUPPRIME") || this.getStatut().equals("REFUSE")) {
			testIsActive = false;
		}
		return testIsActive;
	}
	
	@Override
	public String toString() {
		return prenom + " " + nom + "("+ matricule + ")" + " [mot de passe ="
				+ motDePasse + " statut=" + statut + "]";
	}
	
	public Document toDocument()
	{
	    return new Document().append("matricule", matricule)
	    			         .append("prenom", prenom)
	    			         .append("nom", nom)
	    			         .append("motDePasse", motDePasse)
	    			         .append("statut", statut)
	    					 .append("nomEquipe", nomEquipe);
	}
}
