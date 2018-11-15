package modele;

import java.util.ArrayList;

import CentreSportif.IFT287Exception;

public class GestionResultat {

	private Resultats resultats;
	private Equipes equipes;

	public GestionResultat(Resultats resultats, Equipes equipes) throws IFT287Exception {
		if (equipes.getConnexion() != resultats.getConnexion())
			throw new IFT287Exception(
					"Les différents gestionnaires (equipes et résultats) n'utilisent pas la même connexion au serveur");

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
				throw new IFT287Exception("L'équipe " + nomEquipeA + " n'existe pas.");
			if (eqB == null)
				throw new IFT287Exception("L'équipe " + nomEquipeB + " n'existe pas.");
			if (nomEquipeA.equals(nomEquipeB))
				throw new IFT287Exception("Les deux équipes saisies sont identiques.");

			Resultat tupleResultat = resultats.getResultat(nomEquipeA, nomEquipeB);
			
			if (tupleResultat != null)
				throw new IFT287Exception("Resultat deja existant: " + nomEquipeA + " contre " + nomEquipeB);
		
			// Creation du resultat
			resultats.ajouter(nomEquipeA, nomEquipeB, scoreEquipeA, scoreEquipeB);
			equipes.ajouterResultat(nomEquipeA);
			equipes.ajouterResultat(nomEquipeB);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Supprimer résultat
	 * 
	 * @param nomEquipeA
	 * @param nomEquipeB
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void supprimerResultat(String nomEquipeA, String nomEquipeB) throws IFT287Exception, Exception {
		try {
			// Verifier si resultat existe
			if (!resultats.supprimer(nomEquipeA, nomEquipeB))
				throw new IFT287Exception("Resultat entre " + nomEquipeA + " et " + nomEquipeB + " n'existe pas");
			equipes.supprimer(nomEquipeA);
			equipes.supprimer(nomEquipeB);
			
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
			
			// modifications
			resultats.modifier(nomEquipeA, nomEquipeB, scoreEquipeA, scoreEquipeB);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Affichage de l'ensemble des résultats d'une equipe.
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
	 * Affichage de l'ensemble des résultats d'une equipe.
	 * @param nomEquipe
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void affichageResultats() throws IFT287Exception, Exception {
		try {
			ArrayList<Resultat> listAllResultats = resultats.lectureResultats();
			
			System.out.println("\nLes résultats :");
			for (Resultat res : listAllResultats) {
				System.out.println(res.toString());
			}

		} catch (Exception e) {
			throw e;
		}
	}

}