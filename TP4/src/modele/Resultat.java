package modele;

import org.bson.Document;

public class Resultat {
	
	//Attributes
	//private Equipe equipeA;
	//private Equipe equipeB;
	private String nomEquipeA;
	private String nomEquipeB;
	private int scoreEquipeA;
	private int scoreEquipeB;
		
	/**
	 * Constructeur par défaut	
	 */
	public Resultat() {
	}
	
	/**
	 * Constructeur à 4 arguments
	 * @param equipeA
	 * @param equipeB
	 * @param scoreEquipeA
	 * @param scoreEquipeB
	 */
	
	/**
	 * Constructeur prenant les informations du document d
	 * @param d
	 */
	public Resultat(Document d)
    {
    	this.nomEquipeA = d.getString("nomEquipeA");
    	this.nomEquipeB = d.getString("nomEquipeB");
    	this.scoreEquipeA = d.getInteger("scoreEquipeA");
    	this.scoreEquipeB = d.getInteger("scoreEquipeB");
    }
	
	public Resultat(String nomEquipeA, String nomEquipeB, int scoreEquipeA, int scoreEquipeB) {
		this.nomEquipeA = nomEquipeA;
		this.nomEquipeB = nomEquipeB;
		this.scoreEquipeA = scoreEquipeA;
		this.scoreEquipeB = scoreEquipeB;
	}
	
	/**
	 * Les getteurs et setters
	 */
	/*public Equipe getEquipeA() {
		return equipeA;
	}
	
	public void setEquipeA(Equipe equipeA) {
		this.equipeA = equipeA;
	}
	public Equipe getEquipeB() {
		return equipeB;
	}
	public void setEquipeB(Equipe equipeB) {
		this.equipeB = equipeB;
	}*/
	
	public String getNomEquipeA() {
		return nomEquipeA;
	}

	public void setNomEquipeA(String nomEquipeA) {
		this.nomEquipeA = nomEquipeA;
	}

	public String getNomEquipeB() {
		return nomEquipeB;
	}

	public void setNomEquipeB(String nomEquipeB) {
		this.nomEquipeB = nomEquipeB;
	}
	
	public int getScoreEquipeA() {
		return scoreEquipeA;
	}
	public void setScoreEquipeA(int scoreEquipeA) {
		this.scoreEquipeA = scoreEquipeA;
	}
	public int getScoreEquipeB() {
		return scoreEquipeB;
	}
	public void setScoreEquipeB(int scoreEquipeB) {
		this.scoreEquipeB = scoreEquipeB;
	}

	@Override
	public String toString() {
		return nomEquipeA + " - " + nomEquipeB + " : " + scoreEquipeA + " - " + scoreEquipeB;
	}
	
	public Document toDocument()
	{
	   return new Document().append("nomEquipeA", nomEquipeA)
	   			         	.append("nomEquipeB", nomEquipeB)
	   			         	.append("scoreEquipeA", scoreEquipeA)
	   			         	.append("scoreEquipeB", scoreEquipeB);
	}
}
