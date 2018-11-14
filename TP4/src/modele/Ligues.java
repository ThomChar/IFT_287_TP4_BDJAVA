package modele;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.inc;
import static com.mongodb.client.model.Updates.set;

import org.bson.Document;
import com.mongodb.client.MongoCollection;
import CentreSportif.Connexion;

public class Ligues {

	private Connexion cx;
	private MongoCollection<Document> liguesCollection;

	/**
	 * Creation d'une instance.
	 */
	public Ligues(Connexion cx) {
		this.cx = cx;
		liguesCollection = cx.getDatabase().getCollection("Ligues");
	}

	/**
	 * Retourner la connexion associee.
	 * @return Connexion
	 */
	public Connexion getConnexion() {
		return cx;
	}

	/**
     * Verifie si une ligue existe.
     * @param nomLigue
     * @return vrai ou faux
     */
	public boolean existe(String nomLigue) {
		return liguesCollection.find(eq("nomLigue", nomLigue)).first() != null;
	}

	/**
	 * Lecture d'une ligue.
	 * @param nomLigue
	 * @return objet ligue
	 */
	public Ligue getLigue(String nomLigue) {
		Document l = liguesCollection.find(eq("nomLigue", nomLigue)).first();
		if (l != null) {
			return new Ligue(l);
		}
		return null;
	}

	/**
	 * Ajout d'une nouvelle ligue
	 * @param nomLigue
	 * @param nbJoueurMaxParEquipe
	 */
	public void creer(String nomLigue, int nbJoueurMaxParEquipe) {
		Ligue l = new Ligue(nomLigue, nbJoueurMaxParEquipe);

		// Ajout d'une ligue.
		liguesCollection.insertOne(l.toDocument());
	}

	/**
	 * Modifier le nombre de Joueur max par equipe pour une ligue.
	 * @param nomLigue
	 * @param nbJoueurMaxParEquipe
	 */
	 public void modifierNbJoueursMaxParEquipe(String nomLigue, int nbJoueurMaxParEquipe) {
		 liguesCollection.updateOne(eq("nomLigue", nomLigue), set("nbJoueurMaxParEquipe", nbJoueurMaxParEquipe)); 
	 }

	 /**
	  * Suppression d'une ligue
	  * @param nomLigue
	  * @return boolean
	  */
	public boolean supprimer(String nomLigue) {
		return liguesCollection.deleteOne(eq("nomLigue", nomLigue)).getDeletedCount() > 0;
	}

	/**
	 * Ajouter une équipe dans la ligue
	 * @param nomLigue
	 */
	public void ajouterEquipe(String nomLigue) {
		liguesCollection.updateOne(eq("nomLigue", nomLigue), inc("nbEquipes", 1));
	}
	
	/**
	 * Supprimer une équipe dans la ligue
	 * @param nomLigue
	 */
	public void supprimerEquipe(String nomLigue) {
		liguesCollection.updateOne(eq("nomLigue", nomLigue), inc("nbEquipes", -1));
	}
}
