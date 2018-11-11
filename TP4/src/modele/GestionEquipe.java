package modele;

import java.util.ArrayList;
import CentreSportif.IFT287Exception;

public class GestionEquipe {

	private Equipes equipe;
	private Ligues ligue;
	private Participants participant;
	private Resultats resultat;

	/**
	 * Creation d'une instance
	 */
	public GestionEquipe(Equipes equipe, Participants participant, Ligues ligue, Resultats resultat)
			throws IFT287Exception {
		if (equipe.getConnexion() == ligue.getConnexion() && participant.getConnexion() == equipe.getConnexion()
				&& equipe.getConnexion() == resultat.getConnexion()
				&& ligue.getConnexion() == resultat.getConnexion()) {
			this.equipe = equipe;
			this.participant = participant;
			this.ligue = ligue;
			this.resultat = resultat;
		} else {
			throw new IFT287Exception(
					"Les instances de participant et de resultat n'utilisent pas la même connexion au serveur");
		}
	}

	/**
	 * Ajout d'une nouvelle equipe dans la base de données. Si elle existe déjà , une
	 * exception est levée.
	 * @throws IFT287Exception, Exception
	 */
	public void ajouter(String nomEquipe, String matriculeCap, String nomLigue)
			throws IFT287Exception, Exception {
		try {
			// Vérifie si l equipe existe déjà
		
			/*if (equipe.testDejaCapitaine(matriculeCap))
				throw new IFT287Exception("Ce participant est deja capitaine : ");*/
			if (!ligue.existe(nomLigue))
				throw new IFT287Exception("Ligue " + nomLigue + " n'existe pas : ");
			if (!participant.existe(matriculeCap))
				throw new IFT287Exception("Ligue " + nomLigue + " n'existe pas : ");

			// Ajout de l equipe dans la table des equipes
			equipe.creer(nomEquipe, matriculeCap, nomLigue);
			
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Supprime Equipe de la base de données.
	 * 
	 *  @throws IFT287Exception, Exception
	 */
	public void supprime(String nomEquipe) throws IFT287Exception, Exception {
		try {
			// Validation
			Equipe tupleEquipe = equipe.getEquipe(nomEquipe);
			if (tupleEquipe == null)
				throw new IFT287Exception("Equipe inexistant: " + nomEquipe);
			if (!tupleEquipe.isActive())
				throw new IFT287Exception("Equipe " + nomEquipe + "a encore des participants actifs");

			// Suppression de l'equipe.
			boolean test = equipe.supprimer(nomEquipe);
			if (test == false)
				throw new IFT287Exception("Equipe " + nomEquipe + " inexistante");
			
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Change le capitaine de l'equipe.
	 * 
	 * @throws IFT287Exception, Exception
	 */
	public void changerCapitaine(String nomEquipe, String matriculeCap)throws IFT287Exception, Exception {
		try {
			// Vérifie si l equipe existe déjà
			if (!equipe.existe(nomEquipe))
				throw new IFT287Exception("Equipe " + nomEquipe + " n'existe pas : ");
			if (!participant.existe(matriculeCap))
				throw new IFT287Exception("Participant " + matriculeCap + " n'existe pas : ");
			if (!(participant.getParticipant(matriculeCap).getNomEquipe().equals(nomEquipe) && participant.getParticipant(matriculeCap).getStatut().equals("ACCEPTE")))
				throw new IFT287Exception("Ce Particpant " + matriculeCap + " ne peut pas devenir captaine de " +nomEquipe+" car il n'est pas dans l'equipe");
			if ((participant.getParticipant(matriculeCap).getNomEquipe().equals(nomEquipe) && participant.getParticipant(matriculeCap).getStatut().equals("ACCEPTE")))
				throw new IFT287Exception("Ce Particpant " + matriculeCap + " ne peut pas devenir captaine de " +nomEquipe+" car il n'est pas dans l'equipe");

			equipe.changerCapitaine(nomEquipe, matriculeCap);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Affichage d'une equipe, de ses participants et de ses resultats
	 * 
	 *  @throws IFT287Exception, Exception
	 */
	/*public void affichageEquipe(String nomEquipe) throws IFT287Exception, Exception {
		// Validation
		try {
			Equipe tupleEquipe = equipe.getEquipe(nomEquipe);
			if (tupleEquipe == null)
				throw new IFT287Exception("Equipe inexistante: " + nomEquipe);

			tupleEquipe.setListParticipants(participant.lectureParticipants(nomEquipe));
			tupleEquipe.setListResultats(resultat.lectureResultats(nomEquipe));
			System.out.println(tupleEquipe.toString());

		} catch (Exception e) {
			throw e;
		}
	}*/

	/**
	 * Lecture des equipes d'une ligue
	 * 
	 * @throws IFT287Exception, Exception
	 */
	/*public ArrayList<Equipe> lectureEquipesLigue(String nomLigue) throws IFT287Exception, Exception {
		// Validation
		try {
			Ligue tupleLigue = ligue.getLigue(nomLigue);
			if (tupleLigue == null)
				throw new IFT287Exception("Ligue inexistant: " + nomLigue);

			ArrayList<Equipe> listEquipes = equipe.lectureEquipesLigue(nomLigue);
			return listEquipes;
			
		} catch (Exception e) {
			throw e;
		}
	}*/

	/**
	 * Affichage de l'ensemble des equipes de la table.
	 * 
	 * @throws IFT287Exception, Exception
	 */
	/*public void affichageEquipes() throws IFT287Exception, Exception {
		try {
			
			System.out.println("Equipe [");
			for (Equipe eq : equipe.lectureEquipes()) {
				System.out.println("nomEquipe=" + eq.getNomEquipe() + ", matriculeCap=" + eq.getMatriculeCap()
						+ ", nomLigue=" + eq.getNomLigue());
			}
			System.out.println("]");
			
		} catch (Exception e) {
			throw e;
		}
	}*/
	
	/**
	 * Affichage de l'ensemble des equipes d'une ligue ainsi que le nombre de matchs gagnés, perdus et nulls.
	 * 
	 * @throws IFT287Exception, Exception
	 */
	/*public void afficherEquipesLigue(String nomLigue) throws IFT287Exception, Exception {
		try {
			// Validation
			Ligue tupleLigue = ligue.getLigue(nomLigue);
			if (tupleLigue == null)
				throw new IFT287Exception("Ligue inexistante: " + nomLigue);
			
			// Affichage
			System.out.println("\nLigue " + nomLigue + "(nombre max de joueurs=" + ligue.getLigue(nomLigue).getNbJoueurMaxParEquipe() + ") :");
			for (Equipe eq : equipe.lectureEquipesLigue(nomLigue)) {
				System.out.println("nomEquipe=" + eq.getNomEquipe() + ", matriculeCap=" + eq.getMatriculeCap()
						+ ", nombreDeMatchsGagnés=" + resultat.ObtenirNbMGagne(eq.getNomEquipe())
						+ ", nombreDeMatchsPerdus=" + resultat.ObtenirNbMPerdu(eq.getNomEquipe())
						+ ", nombreDeMatchsNulls=" + resultat.ObtenirNbMNul(eq.getNomEquipe())
						);
			}
		} catch (Exception e) {
			throw e;
		}
	}*/

}
