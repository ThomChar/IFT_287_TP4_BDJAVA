package modele;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.inc;
import static com.mongodb.client.model.Updates.set;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
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
	 * Vérifie si une Equipe existe.
	 */
	public boolean existe(String nomEquipe){
		return equipesCollection.find(eq("nomEquipe", nomEquipe)).first() != null;
	}
	
	/**
	 * Verifie si un participant est deja capitain d'une equipe.
	 */
	/*public boolean testDejaCapitaine(String matricule) throws SQLException {
		stmtExisteCapitaine.setString(1, matricule);
		ResultSet rset = stmtExisteCapitaine.executeQuery();
		boolean capiatineExiste = rset.next();
		rset.close();
		return capiatineExiste;
	}*/

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
	 * Ajout d'une nouvelle equipe non vide.
	 * 
	 */
	public void creer(String nomEquipe, String matriculeCap, String nomLigue) {
		/* Ajout de l'equipe. */
		Equipe e = new Equipe(nomEquipe, matriculeCap, nomLigue);

		// Ajout d'une ligue.
		equipesCollection.insertOne(e.toDocument());
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
	/*public int supprimerEquipesLigue(String nomLigue) throws SQLException {
		stmtDeleteEquipesLigue.setString(1, nomLigue);
		return stmtDeleteEquipesLigue.executeUpdate();
	}*/
	

	/**
	 * Lecture des equipes d'une ligue
	 * 
	 * @throws SQLException
	 */
	/*public ArrayList<Equipe> lectureEquipesLigue(String nomLigue) throws SQLException {
		stmtDispEquipesLigue.setString(1, nomLigue);
		ResultSet rset = stmtDispEquipesLigue.executeQuery();

		ArrayList<Equipe> listEquipes = new ArrayList<Equipe>();

		while (rset.next()) {
			Equipe tupleEquipe = new Equipe();
			tupleEquipe.setNomEquipe(rset.getString("nomEquipe"));
			tupleEquipe.setMatriculeCap(rset.getString("matriculeCapitaine"));
			tupleEquipe.setNomLigue(rset.getString("nomLigue"));
			listEquipes.add(tupleEquipe);
		}
		rset.close();
		return listEquipes;
	}*/
	
	/**
	 * Lecture des equipes de la table
	 * 
	 * @throws SQLException
	 */
	/*public ArrayList<Equipe> lectureEquipes() throws SQLException {
		ResultSet rset = stmtDispEquipesParLigue.executeQuery();

		ArrayList<Equipe> listEquipes = new ArrayList<Equipe>();

		while (rset.next()) {
			Equipe tupleEquipe = new Equipe();
			tupleEquipe.setNomEquipe(rset.getString("nomEquipe"));
			tupleEquipe.setMatriculeCap(rset.getString("matriculeCapitaine"));
			tupleEquipe.setNomLigue(rset.getString("nomLigue"));
			//rset.close();
			listEquipes.add(tupleEquipe);
		}
		rset.close();
		return listEquipes;
	}*/

}
