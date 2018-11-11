package modele;

import java.util.List;
import CentreSportif.Connexion;
import CentreSportif.IFT287Exception;

public class GestionEquipe {

	private Equipes equipes;
	private Ligues ligues;
	private Participants participants;
	private Resultats resultats;
	private Connexion cx;

	/**
	 * Creation d'une instance
	 * @param equipes
	 * @param participants
	 * @param ligues
	 * @param resultats
	 * @throws IFT287Exception
	 */
	public GestionEquipe(Equipes equipes, Participants participants, Ligues ligues, Resultats resultats) throws IFT287Exception {
		this.cx = equipes.getConnexion();
		if (equipes.getConnexion() != ligues.getConnexion() || participants.getConnexion() != equipes.getConnexion()
			|| equipes.getConnexion() != resultats.getConnexion()
			|| ligues.getConnexion() != resultats.getConnexion())
	    		throw new IFT287Exception("Les diff�rents gestionnaires (equipes, partcipants, ligues, resultats) n'utilisent pas la m�me connexion au serveur");
		this.equipes = equipes;
		this.participants = participants;
		this.ligues = ligues;
		this.resultats = resultats;

	}

	/**
	 * Ajout d'une nouvelle equipe dans la base de donn�es.
	 * @param nomEquipe
	 * @param matriculeCap
	 * @param nomLigue
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void ajouter(String nomEquipe, String matriculeCap, String nomLigue) throws IFT287Exception, Exception {
		try {
			cx.demarreTransaction();
			
			// v�rifications
			if (equipes.existe(nomEquipe))
				throw new IFT287Exception("L'�quipe existe d�j� pour le nom " + nomEquipe + ".");
			if (!participants.existe(matriculeCap))
				throw new IFT287Exception("Le participant ayant le matricule '" + matriculeCap + "' n'existe pas.");
			if (equipes.testDejaCapitaine(matriculeCap))
				throw new IFT287Exception("Ce participant est deja capitaine pour une autre �quipe.");
			if (!ligues.existe(nomLigue))
				throw new IFT287Exception("La ligue " + nomLigue + " n'existe pas.");
						
			Participant capitaine = participants.getParticipant(matriculeCap);
			if(capitaine.getEquipe() != null && capitaine.getStatut().equals("ACCEPTE"))
				throw new IFT287Exception("Le participant ayant le matricule '" + matriculeCap + "' est d�j� dans une �quipe.");
			
			Ligue ligue = ligues.getLigue(nomLigue);
			Equipe tupleEquipe = new Equipe(ligue, nomEquipe, capitaine);
			
			// ajout de l'�quipe
			tupleEquipe.ajouterJoueur(capitaine);
			capitaine.setEquipe(tupleEquipe);
			capitaine.setStatut("ACCEPTE");
			equipes.creer(tupleEquipe);

			cx.commit();

		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}

	/**
	 * Supprime Equipe de la base de donn�es.
	 * @param nomEquipe
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void supprimer(String nomEquipe) throws IFT287Exception, Exception {
		try {
			cx.demarreTransaction();
			
			// Validation
			Equipe tupleEquipe = equipes.getEquipe(nomEquipe);
			if (tupleEquipe == null)
				throw new IFT287Exception("L'�quipe " + nomEquipe + " n'existe pas.");
			if (!tupleEquipe.isActive())
				throw new IFT287Exception("L'�quipe " + nomEquipe + " a encore des participants actifs");

			// Suppression de l'equipe.
			if (equipes.supprimer(tupleEquipe))
				throw new IFT287Exception("Equipe " + nomEquipe + " inexistante");

			cx.commit();
		} catch (Exception e) {
			cx.rollback();
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
	public void changerCapitaine(String nomEquipe, String matriculeCap) throws IFT287Exception, Exception {
		try {
			cx.demarreTransaction();
			
			// Validation
			if (!equipes.existe(nomEquipe))
				throw new IFT287Exception("L'�quipe " + nomEquipe + " n'existe pas�: ");
			if (!participants.existe(matriculeCap))
				throw new IFT287Exception("Le participant ayant le matricule '" + matriculeCap + "' n'existe pas.");
			
			Equipe tupleEquipe = equipes.getEquipe(nomEquipe);
			Participant capitaine = participants.getParticipant(matriculeCap);
			
			if (!(capitaine.getEquipe().getNomEquipe().equals(nomEquipe)
					&& capitaine.getStatut().equals("ACCEPTE")))
				throw new IFT287Exception("Le particpant ayant le matricule '" + matriculeCap + "' ne peut pas devenir captaine de l'�quipe '"+ nomEquipe + "', car il n'est pas dans l'equipe.");
			
			// affecter le nouveau capitaine
			tupleEquipe.setCapitaine(capitaine);

			cx.commit();
		} catch (Exception e) {
			cx.rollback();
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
			cx.demarreTransaction();
			// Validation
			Equipe tupleEquipe = equipes.getEquipe(nomEquipe);
			if (tupleEquipe == null)
				throw new IFT287Exception("Equipe inexistante: " + nomEquipe);
			
			// afficher l'�quipe
			System.out.println(tupleEquipe.toString());
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}

	/**
	 * Lecture des equipes d'une ligue
	 * @param nomLigue
	 * @return une liste d'�quipes correspondant au nom de la ligue pass� en param�tre
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public List<Equipe> lectureEquipesLigue(String nomLigue) throws IFT287Exception, Exception {
		try {
			cx.demarreTransaction();
			
			// Validation
			Ligue tupleLigue = ligues.getLigue(nomLigue);
			if (tupleLigue == null)
				throw new IFT287Exception("Ligue inexistant: " + nomLigue);
			
			// obtenir la liste des �quipes pour la ligue
			List<Equipe> listEquipes = equipes.calculerListeEquipesLigue(nomLigue);

			cx.commit();
			return listEquipes;
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}

	/**
	 * Affichage de l'ensemble des equipes
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void affichageEquipes() throws IFT287Exception, Exception {
		cx.demarreTransaction();
		// afficher toutes les �quipes
		List<Equipe> list = equipes.calculerListeEquipes();

		for (Equipe eq : list) {
			System.out.println(eq.toString());
		}
		cx.commit();
	}

	/**
	 * Affichage de l'ensemble des equipes d'une ligue ainsi que le nombre de matchs
	 * gagn�s, perdus et nulls.
	 * @param nomLigue
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void afficherEquipesLigue(String nomLigue) throws IFT287Exception, Exception {
		try {
			cx.demarreTransaction();
			// Validation
			Ligue tupleLigue = ligues.getLigue(nomLigue);
			if (tupleLigue == null)
				throw new IFT287Exception("La ligue " + nomLigue + " est inexistante.");
	
			// Affichage
			System.out.println("\nLigue " + nomLigue + "(nombre max de joueurs="
					+ ligues.getLigue(nomLigue).getNbJoueurMaxParEquipe() + ") :");
			for (Equipe eq : equipes.calculerListeEquipesLigue(nomLigue)) {
				System.out
						.println("nomEquipe=" + eq.getNomEquipe() + ", matriculeCap=" + eq.getCapitaine().getMatricule()
								+ ", nombreDeMatchsGagn�s=" + resultats.ObtenirNbMGagne(eq.getNomEquipe())
								+ ", nombreDeMatchsPerdus=" + resultats.ObtenirNbMPerdu(eq.getNomEquipe())
								+ ", nombreDeMatchsNuls=" + resultats.ObtenirNbMNul(eq.getNomEquipe()));
			}
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}
}