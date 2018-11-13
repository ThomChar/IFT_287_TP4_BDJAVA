package modele;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.inc;
import static com.mongodb.client.model.Updates.set;

import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import CentreSportif.Connexion;

public class Equipes {

	private Connexion cx;
	private MongoCollection<Document> equipesCollection;

	/**
	 * Creation d'une instance.
	 */
	public Equipes(Connexion cx) {
		this.cx = cx;
		equipesCollection = cx.getDatabase().getCollection("Equipes");
	}

	/**
	 * Retourner la connexion associée.
	 */
	public Connexion getConnexion() {
		return cx;
	}
	
	/**
	 * Ajout d'une nouvelle equipe non vide.
	 * 
	 */
	public void creer(String nomEquipe, String matriculeCap, String nomLigue) {

		Equipe e = new Equipe(nomEquipe, matriculeCap, nomLigue);

		// Ajout d'une ligue.
		equipesCollection.insertOne(e.toDocument());
	}

	/**
	 * Vérifie si une Equipe existe.
	 */
	public boolean existe(String nomEquipe){
		return equipesCollection.find(eq("nomEquipe", nomEquipe)).first() != null;
	}
	
	/**
	 * Verifie si un participant est deja capitain d'une equipe.
	 */
	public boolean testDejaCapitaine(String matricule) {
		return equipesCollection.find(eq("matriculeCap", matricule)).first() != null;
	}

	/**
	 * Lecture d'une Equipe.
	 * 
	 */
	public Equipe getEquipe(String nomEquipe) {
		Document e = equipesCollection.find(eq("nomEquipe", nomEquipe)).first();
		if (e != null) {
			return new Equipe(e);
		}
		return null;
	}
	
	/**
	 * Suppression d'une equipe.
	 */
	public boolean supprimer(String nomEquipe) {
		return equipesCollection.deleteOne(eq("nomEquipe", nomEquipe)).getDeletedCount() > 0;
	}
	
	/**
	 * Change le capitaine de l'equipe d'une equipe
	 */
	public void changerCapitaine(String nomEquipe, String matriculeCap) {
		 equipesCollection.updateOne(eq("nomEquipe", nomEquipe), set("matriculeCap", matriculeCap)); 
	}
	
	public void ajouterJoueur(String nomEquipe) {
		equipesCollection.updateOne(eq("nomEquipe", nomEquipe), inc("nbParticipants", 1));
	}

	public void supprimerJoueur(String nomEquipe) {
		equipesCollection.updateOne(eq("nomEquipe", nomEquipe), inc("nbParticipants", -1));
	}

	public void ajouterResultat(String nomEquipe) {
		equipesCollection.updateOne(eq("nomEquipe", nomEquipe), inc("nbResultats", 1));
	}

	public void supprimerResultat(String nomEquipe) {
		equipesCollection.updateOne(eq("nomEquipe", nomEquipe), inc("nbResultats", -1));
	}
	
	/**
	 * Suppression des �quipes d'une ligue.
	 */
	public boolean supprimerEquipesLigue(String nomLigue) {
		return equipesCollection.deleteMany(eq("nomLigue", nomLigue)).getDeletedCount() > 0;
	}
	
	/**
	 * Lecture des equipes d'une ligue
	 * 
	 * @throws SQLException
	 */
	public ArrayList<Equipe> lectureEquipesLigue(String nomLigue) {
		ArrayList<Equipe> listEquipes = new ArrayList<Equipe>();
		
		MongoCursor<Document> eqs = equipesCollection.find(eq("nomLigue", nomLigue)).iterator();
		if (eqs.hasNext()) {
			listEquipes.add(new Equipe(eqs.next()));
		}
		
		return listEquipes;
	}
	
	/**
	 * Lecture des equipes de la table
	 * 
	 * @throws SQLException
	 */
	public ArrayList<Equipe> lectureEquipes() {
		ArrayList<Equipe> listEquipes = new ArrayList<Equipe>();
		
		MongoCursor<Document> eqs = equipesCollection.find().iterator();
		if (eqs.hasNext()) {
			listEquipes.add(new Equipe(eqs.next()));
		}
		
		return listEquipes;
	}
}
