package modele;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;
import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import CentreSportif.Connexion;

public class Resultats {

	private Connexion cx;
	private MongoCollection<Document> resultatsCollection;

	/**
	 * Creation d'une instance. Des annones SQL pour chaque requête sont
	 * précompilés.
	 */
	public Resultats(Connexion cx) {
		this.cx = cx;
		this.resultatsCollection = cx.getDatabase().getCollection("Resultats");
	}

	/**
	 * Retourner la connexion associée.
	 */
	public Connexion getConnexion() {
		return cx;
	}

	/**
	 * Vérifie si un resultat existe.
	 */
	public boolean existe(String nomEquipeA, String nomEquipeB) {
		return resultatsCollection.find(or(and(eq("nomEquipeA", nomEquipeB), eq("nomEquipeB", nomEquipeA)),
				and(eq("nomEquipeA", nomEquipeA), eq("nomEquipeB", nomEquipeB)))).first() != null;
	}

	/**
	 * Lecture d'une resultat.
	 */
	public Resultat getResultat(String nomEquipeA, String nomEquipeB) {
		Document r = resultatsCollection.find(or(and(eq("nomEquipeA", nomEquipeB), eq("nomEquipeB", nomEquipeA)),
				and(eq("nomEquipeA", nomEquipeA), eq("nomEquipeB", nomEquipeB)))).first();
		if (r != null) {
			return new Resultat(r);
		}
		return null;
	}

	/**
	 * Ajout d'un nouveau resultat dans la base de données.
	 * 
	 */
	public void ajouter(String nomEquipeA, String nomEquipeB, int scoreEquipeA, int scoreEquipeB) {
		Resultat r = new Resultat(nomEquipeA, nomEquipeB, scoreEquipeA, scoreEquipeB);

		// Ajout d'un Resultat.
		resultatsCollection.insertOne(r.toDocument());
	}

	/**
	 * Modifier le resultat pour un match.
	 */

	public void modifier(String nomEquipeA, String nomEquipeB, int scoreEquipeA, int scoreEquipeB) {
		if (resultatsCollection.find(and(eq("nomEquipeA", nomEquipeB), eq("nomEquipeB", nomEquipeA))) == null) {
			// Switch le nom des equipes
			String nomSwitch = nomEquipeA;
			nomEquipeA = nomEquipeB;
			nomEquipeB = nomSwitch;
			// Switch le score des equipes
			int scoreSwitch = scoreEquipeA;
			scoreEquipeA = scoreEquipeB;
			scoreEquipeB = scoreSwitch;

		}
		resultatsCollection.updateOne(and(eq("nomEquipeA", nomEquipeB), eq("nomEquipeB", nomEquipeA)),
				combine(set("scoreEquipeA", scoreEquipeA), set("scoreEquipeB", scoreEquipeB)));
	}

	/**
	 * Supprimer Resultat dans la base de données
	 */
	public boolean supprimer(String nomEquipeA, String nomEquipeB) {
		return resultatsCollection.deleteOne(or(and(eq("nomEquipeA", nomEquipeB), eq("nomEquipeB", nomEquipeA)),
				and(eq("nomEquipeA", nomEquipeA), eq("nomEquipeB", nomEquipeB)))).getDeletedCount() > 0;
	}

	/**
	 * Obtenir nombre match gagné d'une équipe.
	 */
	public long ObtenirNbMGagne(String nomEquipe) {
		long nbMGagne = 0;
		ArrayList<Resultat> listResultats = this.lectureResultats(nomEquipe);
		for(Resultat res : listResultats) {
			if((res.getNomEquipeA().equals(nomEquipe) && (res.getScoreEquipeA() > res.getScoreEquipeB())) ||
					(res.getNomEquipeB().equals(nomEquipe) && (res.getScoreEquipeB() > res.getScoreEquipeA()))) {
				nbMGagne++;
			}
		}
		
		return nbMGagne;
	}

	/**
	 * Obtenir nombre match perdu d'une équipe.
	 */
	public long ObtenirNbMPerdu(String nomEquipe) {
		long nbMPerdu = 0;
		ArrayList<Resultat> listResultats = this.lectureResultats(nomEquipe);
		for(Resultat res : listResultats) {
			if((res.getNomEquipeA().equals(nomEquipe) && (res.getScoreEquipeA() < res.getScoreEquipeB())) ||
					(res.getNomEquipeB().equals(nomEquipe) && (res.getScoreEquipeB() < res.getScoreEquipeA()))) {
				nbMPerdu++;
			}
		}
		
		return nbMPerdu;
	}

	/**
	 * Obtenir nombre match nul d'une équipe.
	 */
	public long ObtenirNbMNul(String nomEquipe) {
		long nbMNul = 0;
		ArrayList<Resultat> listResultats = this.lectureResultats(nomEquipe);
		for(Resultat res : listResultats) {
			if((res.getNomEquipeA().equals(nomEquipe) && (res.getScoreEquipeA() == res.getScoreEquipeB())) ||
					(res.getNomEquipeB().equals(nomEquipe) && (res.getScoreEquipeB() == res.getScoreEquipeA()))) {
				nbMNul++;
			}
		}
		
		return nbMNul;
	}

	/**
	 * Lecture des resultats de l'équipe
	 */
	public ArrayList<Resultat> lectureResultats(String nomEquipe) {

		ArrayList<Resultat> listResultats = new ArrayList<Resultat>();

		MongoCursor<Document> res = resultatsCollection.find(or(eq("nomEquipeA", nomEquipe), eq("nomEquipeB", nomEquipe))).iterator();
		while (res.hasNext()) {
			listResultats.add(new Resultat(res.next()));
		}

		return listResultats;
	}

	/**
	 * Lecture des resultats de l'équipe
	 */
	public ArrayList<Resultat> lectureResultats() {

		ArrayList<Resultat> listResultats = new ArrayList<Resultat>();

		MongoCursor<Document> res = resultatsCollection.find().iterator();
		while (res.hasNext()) {
			listResultats.add(new Resultat(res.next()));
		}

		return listResultats;
	}
}
