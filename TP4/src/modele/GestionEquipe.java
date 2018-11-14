package modele;


import java.util.List;

import CentreSportif.IFT287Exception;

public class GestionEquipe {

	private Equipes equipes;
	private Ligues ligues;
	private Participants participants;
	private Resultats resultats;

	/**
	 * Creation d'une instance
	 * @param equipes
	 * @param participants
	 * @param ligues
	 * @throws IFT287Exception
	 */
	public GestionEquipe(Equipes equipes, Participants participants, Ligues ligues, Resultats resultats)
			throws IFT287Exception {
		if (equipes.getConnexion() != ligues.getConnexion() || equipes.getConnexion() != participants.getConnexion()
				|| equipes.getConnexion() != resultats.getConnexion())
		    		throw new IFT287Exception("Les différents gestionnaires (equipes, partcipants, ligues, resultats) n'utilisent pas la même connexion au serveur");
			this.equipes = equipes;
			this.participants = participants;
			this.ligues = ligues;
			this.resultats = resultats;
	}

	/**
	 * Ajout d'une nouvelle equipe.
	 * @param nomEquipe
	 * @param matriculeCap
	 * @param nomLigue
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void ajouter(String nomEquipe, String matriculeCap, String nomLigue)
			throws IFT287Exception, Exception {
		try {
			// Vérifications
			if(equipes.existe(nomEquipe))
				throw new IFT287Exception("L'équipe '" + nomEquipe + "' existe déjà.");
			if (!participants.existe(matriculeCap))
				throw new IFT287Exception("Le participant ayant le matricule '" + matriculeCap + "' n'existe pas.");
			if (equipes.testDejaCapitaine(matriculeCap))
				throw new IFT287Exception("Ce participant est deja capitaine pour une autre équipe.");
			if (!ligues.existe(nomLigue))
				throw new IFT287Exception("La ligue " + nomLigue + " n'existe pas.");
						
			Participant capitaine = participants.getParticipant(matriculeCap);
			if(capitaine.getNomEquipe() != null && capitaine.getStatut().equals("ACCEPTE"))
				throw new IFT287Exception("Le participant ayant le matricule '" + matriculeCap + "' est déjà dans une équipe.");

			// Ajout de l'équipe
			equipes.creer(nomEquipe, matriculeCap, nomLigue);
			ligues.ajouterEquipe(nomLigue);
			
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Supprime Equipe de la base de données.
	 * @param nomEquipe
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void supprime(String nomEquipe) throws IFT287Exception, Exception {
		try {
			// Validations
			Equipe e = equipes.getEquipe(nomEquipe);
			if (e == null)
				throw new IFT287Exception("L'équipe '" + nomEquipe + "' n'existe pas.");
			if (!e.isActive())
				throw new IFT287Exception("L'équipe '" + nomEquipe + "' a encore des participants actifs");
			
			// Suppression de l'equipe.
			if (equipes.supprimer(nomEquipe))
				throw new IFT287Exception("Equipe '" + nomEquipe + "' n'existe pas.");
			ligues.supprimerEquipe(e.getNomLigue());
			
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Change le capitaine de l'equipe.
	 * @param nomEquipe
	 * @param matriculeCap
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void changerCapitaine(String nomEquipe, String matriculeCap)throws IFT287Exception, Exception {
		try {
			// Validations
			if (!equipes.existe(nomEquipe))
				throw new IFT287Exception("L'équipe '" + nomEquipe + "' n'existe pas.");
			if (!participants.existe(matriculeCap))
				throw new IFT287Exception("Le participant ayant le matricule '" + matriculeCap + "' n'existe pas.");
			
			Participant capitaine = participants.getParticipant(matriculeCap);
			
			if (!(capitaine.getNomEquipe().equals(nomEquipe))
					|| !capitaine.getStatut().equals("ACCEPTE"))
				throw new IFT287Exception("Le particpant ayant le matricule '" + matriculeCap + "' ne peut pas devenir captaine de l'équipe '"+ nomEquipe + "', car il n'est pas dans l'equipe.");
			
			// changement du capitaine
			equipes.changerCapitaine(nomEquipe, matriculeCap);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Affichage d'une equipe, de ses participants et de ses resultats
	 * @param nomEquipe
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void affichageEquipe(String nomEquipe) throws IFT287Exception, Exception {
		try {
			// Validation
			Equipe e = equipes.getEquipe(nomEquipe);
			if (e == null)
				throw new IFT287Exception("L'équipe '"+nomEquipe+"' n'existe pas.");
			
			// affichages
			e.toString();
			
			List<Participant> joueurs = participants.lectureParticipants(nomEquipe);
			List<Resultat> scores = resultats.lectureResultats(nomEquipe);

			System.out.println(e.toString());
			System.out.println("Joueurs : ");
			for(Participant p : joueurs) {
				p.toString();
			}
			System.out.println("Résultats : ");
			for(Resultat r : scores) {
				r.toString();
			}

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Affichage de l'ensemble des equipes
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void affichageEquipes() throws IFT287Exception, Exception {
		try {
			
			List<Equipe> eqs = equipes.lectureEquipes();
			System.out.println("\nToutes les équipes ("+ eqs.size() +") : ");
			
			for (Equipe e : eqs) {
				e.toString();
			}
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Affichage de l'ensemble des equipes d'une ligue ainsi que le nombre de matchs gagnés, perdus et nulls.
	 * @throws IFT287Exception, Exception
	 */
	public void afficherEquipesLigue(String nomLigue) throws IFT287Exception, Exception {
		try {
			// Validation
			Ligue l = ligues.getLigue(nomLigue);
			if (l == null)
				throw new IFT287Exception("La ligue '" + nomLigue + "' est inexistante.");
			
			// Affichages
			System.out.println("\nLigue " + nomLigue + "(nombre max de joueurs=" + ligues.getLigue(nomLigue).getNbJoueurMaxParEquipe() + ") :");
			for (Equipe eq : equipes.lectureEquipesLigue(nomLigue)) {
				System.out.println("nomEquipe=" + eq.getNomEquipe() + ", matriculeCap=" + eq.getMatriculeCap()
						+ ", nombreDeMatchsGagnés=" + resultats.ObtenirNbMGagne(eq.getNomEquipe())
						+ ", nombreDeMatchsPerdus=" + resultats.ObtenirNbMPerdu(eq.getNomEquipe())
						+ ", nombreDeMatchsNulls=" + resultats.ObtenirNbMNul(eq.getNomEquipe())
						);
			}
		} catch (Exception e) {
			throw e;
		}
	}
}
