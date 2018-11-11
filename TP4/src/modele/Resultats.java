package modele;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import CentreSportif.Connexion;

public class Resultats {

	private Connexion cx;

	/**
	 * Creation d'une instance. Des annones SQL pour chaque requête sont
	 * précompilés.
	 */
	public Resultats(Connexion cx) throws SQLException {
		this.cx = cx;
	}

	/**
	 * Retourner la connexion associée.
	 */
	public Connexion getConnexion() {
		return cx;
	}

	/**
	 * Vérifie si un resultat existe.
	 * 
	 * @throws SQLException
	 */
	public boolean existe(String nomEquipeA, String nomEquipeB) throws SQLException {
		stmtExiste.setString(1, nomEquipeA);
		stmtExiste.setString(2, nomEquipeB);
		ResultSet rset = stmtExiste.executeQuery();
		boolean equipeExiste = rset.next();
		rset.close();
		return equipeExiste;
	}

	/**
	 * Lecture d'un resultat.
	 * 
	 * @throws SQLException
	 */
	public Resultat getResultat(String nomEquipeA, String nomEquipeB) throws SQLException {
		stmtExiste.setString(1, nomEquipeA);
		stmtExiste.setString(2, nomEquipeB);
		ResultSet rset = stmtExiste.executeQuery();
		if (rset.next()) {
			Resultat tupleResultat = new Resultat();
			tupleResultat.setNomEquipeA(nomEquipeA);
			tupleResultat.setNomEquipeB(nomEquipeB);
			rset.close();
			return tupleResultat;
		} else
			return null;
	}

	/**
	 * Ajout d'un nouveau resultat dans la base de données.
	 * 
	 * @throws SQLException
	 */
	public void ajouter(String nomEquipeA, String nomEquipeB, int scoreEquipeA, int scoreEquipeB) throws SQLException {
		stmtInsert.setString(1, nomEquipeA);
		stmtInsert.setString(2, nomEquipeB);
		stmtInsert.setInt(3, scoreEquipeA);
		stmtInsert.setInt(4, scoreEquipeB);
		stmtInsert.executeUpdate();
	}

	/**
	 * Modifier le resultat pour un match.
	 * 
	 * @throws SQLException
	 */

	public int modifier(String nomEquipeA, String nomEquipeB, int scoreEquipeA, int scoreEquipeB) throws SQLException {
		stmtUpdate.setString(3, nomEquipeA);
		stmtUpdate.setString(4, nomEquipeB);
		stmtUpdate.setInt(1, scoreEquipeA);
		stmtUpdate.setInt(2, scoreEquipeB);
		return stmtUpdate.executeUpdate();
	}

	/**
	 * Supprimer Resultat dans la base de données
	 * 
	 * @throws SQLException
	 */
	public int supprimer(String nomEquipeA, String nomEquipeB) throws SQLException {
		stmtDelete.setString(1, nomEquipeA);
		stmtDelete.setString(2, nomEquipeB);
		stmtDelete.setString(3, nomEquipeA);
		stmtDelete.setString(4, nomEquipeB);
		return stmtDelete.executeUpdate();
	}

	/**
	 * Obtenir nombre match gagné d'une équipe.
	 * 
	 * @throws SQLException
	 */
	public int ObtenirNbMGagne(String nomEquipe) throws SQLException {
		stmtNbMGagne.setString(1, nomEquipe);
		stmtNbMGagne.setString(2, nomEquipe);
		ResultSet rset = stmtNbMGagne.executeQuery();
		rset.next();
		int nb = rset.getInt("nb");
		rset.close();
		return nb;
	}

	/**
	 * Obtenir nombre match perdu d'une équipe.
	 * 
	 * @throws SQLException
	 */
	public int ObtenirNbMPerdu(String nomEquipe) throws SQLException {
		stmtNbMPerdu.setString(1, nomEquipe);
		stmtNbMPerdu.setString(2, nomEquipe);
		ResultSet rset = stmtNbMPerdu.executeQuery();
		rset.next();
		int nb = rset.getInt("nb");
		rset.close();
		return nb;
	}

	/**
	 * Obtenir nombre match nul d'une équipe.
	 * 
	 * @throws SQLException
	 */
	public int ObtenirNbMNul(String nomEquipe) throws SQLException {
		stmtNbMNul.setString(1, nomEquipe);
		stmtNbMNul.setString(2, nomEquipe);
		ResultSet rset = stmtNbMNul.executeQuery();
		rset.next();
		int nb = rset.getInt("nb");
		rset.close();
		return nb;
	}

	/**
	 * Afficher les resultats pour un match.
	 */
	public void afficher() throws SQLException {
		ResultSet rset = stmtDispResultat.executeQuery();
		rset.close();
	}
	
	/**
	 * Lecture des resultats de l'équipe
	 * 
	 * @throws SQLException
	 */
	public ArrayList<Resultat> lectureResultats(String nomEquipe) throws SQLException {
		stmtDispResultatsEquipe.setString(1, nomEquipe);
		stmtDispResultatsEquipe.setString(2, nomEquipe);
	
		ResultSet rset = stmtDispResultatsEquipe.executeQuery();
		ArrayList<Resultat> listResultats = new ArrayList<Resultat>();
	
		while (rset.next()) {
			Resultat tupleResultat = new Resultat();
			tupleResultat.setNomEquipeA(rset.getString("nomEquipeA"));
			tupleResultat.setNomEquipeB(rset.getString("nomEquipeB"));
			tupleResultat.setScoreEquipeA(rset.getInt("scoreEquipeA"));
			tupleResultat.setScoreEquipeB(rset.getInt("scoreEquipeB"));
			listResultats.add(tupleResultat);
		}
		rset.close();
		return listResultats;
	}

}
