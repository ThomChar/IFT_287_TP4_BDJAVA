package modele;

import java.util.ArrayList;

import CentreSportif.IFT287Exception;

public class GestionResultat {

	private Resultats resultats;
	private Equipes equipes;

	public GestionResultat(Resultats resultats, Equipes equipes) throws IFT287Exception {
		if (equipes.getConnexion() != resultats.getConnexion())
			throw new IFT287Exception(
					"Les diff�rents gestionnaires (equipes et r�sultats) n'utilisent pas la m�me connexion au serveur");

		this.resultats = resultats;
		this.equipes = equipes;

	}

	/**
	 * Inscrit un resultat enntre deux equipes
	 * 
	 * @param nomEquipeA
	 * @param nomEquipeB
	 * @param scoreEquipeA
	 * @param scoreEquipeB
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void InscrireResulat(String nomEquipeA, String nomEquipeB, int scoreEquipeA, int scoreEquipeB)
			throws IFT287Exception, Exception {
		try {

			Equipe eqA = equipes.getEquipe(nomEquipeA);
			Equipe eqB = equipes.getEquipe(nomEquipeB);

			// Validations
			if (eqA == null)
				throw new IFT287Exception("L'�quipe " + nomEquipeA + " n'existe pas.");
			if (eqB == null)
				throw new IFT287Exception("L'�quipe " + nomEquipeB + " n'existe pas.");
			if (nomEquipeA.equals(nomEquipeB))
				throw new IFT287Exception("Les deux �quipes saisies sont identiques.");

			// Verifier si resultat equipeA contre EquipeB existe
			Resultat tupleResultat = resultats.getResultat(nomEquipeA, nomEquipeB);
			// Si pas de match retour n'est autoris�
			Resultat tupleResultat2 = resultats.getResultat(nomEquipeB, nomEquipeA);

			if (tupleResultat != null)
				throw new IFT287Exception("Resultat deja existant: " + nomEquipeA + " contre " + nomEquipeB);
			if (tupleResultat2 != null)
				throw new IFT287Exception("Resultat deja existant: " + nomEquipeB + " contre " + nomEquipeA);

			// Creation du resultat
			resultats.ajouter(nomEquipeA, nomEquipeB, scoreEquipeA, scoreEquipeB);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Supprimer r�sultat
	 * 
	 * @param nomEquipeA
	 * @param nomEquipeB
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void supprimerResultat(String nomEquipeA, String nomEquipeB) throws IFT287Exception, Exception {
		try {

			// Verifier si resultat existe
			if (resultats.supprimer(nomEquipeA, nomEquipeB) == false)
				throw new IFT287Exception("Resultat entre " + nomEquipeA + " et " + nomEquipeB + " n'existe pas");

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Modifier resultat. Le resultat doit exister
	 * 
	 * @param nomEquipeA
	 * @param nomEquipeB
	 * @param scoreEquipeA
	 * @param scoreEquipeB
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void modifierResultat(String nomEquipeA, String nomEquipeB, int scoreEquipeA, int scoreEquipeB)
			throws IFT287Exception, Exception {
		try {

			// Verifier si resultat existe
			if (!resultats.existe(nomEquipeA, nomEquipeB))
				throw new IFT287Exception("Resultat entre " + nomEquipeA + " et " + nomEquipeB + " n'existe pas");

			resultats.modifier(nomEquipeA, nomEquipeB, scoreEquipeA, scoreEquipeB);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Affichage de l'ensemble des r�sultats d'une equipe.
	 * @param nomEquipe
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void affichageResultatsEquipe(String nomEquipe) throws IFT287Exception, Exception {

		// Verifier si equipe existe
		if (!equipes.existe(nomEquipe))
			throw new IFT287Exception("L'equipe " + nomEquipe + " n'existe pas");

		try {
			ArrayList<Resultat> listAllResultats = resultats.lectureResultats(nomEquipe);
			for (Resultat res : listAllResultats) {
				res.toString();
			}

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Affichage de l'ensemble des r�sultats de la table.
	 * 
	 * @throws IFT287Exception,
	 *             Exception
	 */
	public void affichageResultats() throws IFT287Exception, Exception {
		try {
			ArrayList<Resultat> listAllResultats = resultats.lectureResultats();
			for (Resultat res : listAllResultats) {
				res.toString();
			}

		} catch (Exception e) {
			throw e;
		}
	}

}