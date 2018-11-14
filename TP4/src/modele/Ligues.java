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
		this.liguesCollection = cx.getDatabase().getCollection("Ligues");
	}

	/**
	 * Retourner la connexion associee.
	 */
	public Connexion getConnexion() {
		return cx;
	}

	/**
	 * Verifie si une ligue existe.
	 */
	public boolean existe(String nomLigue) {
		return liguesCollection.find(eq("nomLigue", nomLigue)).first() != null;
	}

	/**
	 * Lecture d'une ligue.
	 */
	public Ligue getLigue(String nomLigue) {
		Document l = liguesCollection.find(eq("nomLigue", nomLigue)).first();
		if (l != null) {
			return new Ligue(l);
		}
		return null;
	}

	/**
	 * Ajout d'une nouvelle ligue dans la base de donnees.
	 */
	public void creer(String nomLigue, int nbJoueurMaxParEquipe) {
		Ligue l = new Ligue(nomLigue, nbJoueurMaxParEquipe);

		// Ajout d'une ligue.
		liguesCollection.insertOne(l.toDocument());
	}

	/**
	 * Ajout d'une nouvelle ligue (vide).
	 * 
	 * @throws IFT287Exception
	 */
	/*
	 * public void creation(String nomLigue, int nbJoueurMaxParEquipe,
	 * ArrayList<Equipe> listEquipes) throws SQLException, IFT287Exception {
	 * stmtInsert.setString(1, nomLigue); stmtInsert.setInt(2,
	 * nbJoueurMaxParEquipe); stmtInsert.setArray(3, (Array) listEquipes);
	 * stmtInsert.executeUpdate(); }
	 */

	/**
	 * Modifier le nombre de Joueur max par equipe pour une ligue.
	 * 
	 * @throws IFT287Exception
	 */
	
	 public void modifierNbJoueursMaxParEquipe(String nomLigue, int nbJoueurMaxParEquipe) {
	  liguesCollection.updateOne(eq("nomLigue", nomLigue), set("nbJoueurMaxParEquipe", nbJoueurMaxParEquipe)); 
	 }
	 

	/**
	 * Modifier le contenu de la liste des equipe pour une ligue.
	 * 
	 * @throws IFT287Exception
	 */
	/*
	 * public void modifierListEquipes(String nomLigue, ArrayList<Equipe>
	 * listEquipes) throws SQLException, IFT287Exception {
	 * stmtUpdateListEquipes.setArray(1, (Array)listEquipes);
	 * stmtUpdateListEquipes.setString(2, nomLigue);
	 * stmtUpdateListEquipes.executeUpdate(); }
	 */

	/**
	 * Suppression d'une ligue. regarder si ligue n'est pas active avant de
	 * supprimer
	 * 
	 */
	public boolean supprimer(String nomLigue) {
		return liguesCollection.deleteOne(eq("nomLigue", nomLigue)).getDeletedCount() > 0;
	}

	public void ajouterEquipe(String nomLigue) {
		liguesCollection.updateOne(eq("nomLigue", nomLigue), inc("nbEquipes", 1));
	}

	public void supprimerEquipe(String nomLigue) {
		liguesCollection.updateOne(eq("nomLigue", nomLigue), inc("nbEquipes", -1));
	}

	/**
	 * Afficher toutes les equipes d'une ligue.
	 * 
	 * @throws IFT287Exception
	 */
	/*public void afficher(String nomLigue) throws SQLException, IFT287Exception {

		stmtUpdateListEquipes.setString(1, nomLigue);
		stmtUpdateListEquipes.executeUpdate();
	}*/
}
