package modele;

import java.util.ArrayList;

import CentreSportif.IFT287Exception;

public class GestionParticipant {

	private Participants participants;
	private Equipes equipes;
	private Ligues ligues;

	/**
	 * Creation d'une instance
	 * @param equipes
	 * @param participants
	 * @param ligues
	 * @throws IFT287Exception
	 */
	public GestionParticipant(Participants participants, Equipes equipes, Ligues ligues) throws IFT287Exception {
		if (participants.getConnexion() != equipes.getConnexion() && participants.getConnexion() != ligues.getConnexion())
			throw new IFT287Exception(
					"Les instances de participant, ligue et de equipe n'utilisent pas la m�me connexion au serveur");

		this.participants = participants;
		this.equipes = equipes;
		this.ligues = ligues;
	}

	/**
	 * Ajout d'un nouveau particpant
	 * @param matricule
	 * @param prenom
	 * @param nom
	 * @param motDePasse
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void ajouter(String matricule, String prenom, String nom, String motDePasse) throws IFT287Exception, Exception {
		try {
			// V�rification
			if (participants.existe(matricule))
				throw new IFT287Exception("Particpant existe d�j�: " + matricule);
			
			// Ajout du participant
			participants.ajouter(matricule, prenom, nom, motDePasse);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Modifier le nom et le pr�nom d'un participant
	 * @param matricule
	 * @param prenom
	 * @param nom
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void modifierNomPrenom(String matricule, String prenom, String nom) throws IFT287Exception, Exception {
		try {
			// V�rification
			if (!participants.existe(matricule))
				throw new IFT287Exception("Le particpant n'existe pas pour le matricule�: " + matricule+".");
			
			// modifications
			participants.modifierNomPrenom(matricule, prenom, nom);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Modifier le mot de passe d'un participant
	 * @param matricule
	 * @param motDePasse
	 * @param secondMotDePasse
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void modifierMotDePasse(String matricule, String motDePasse, String secondMotDePasse) throws IFT287Exception, Exception {
		try {
			// V�rifications
			if (!participants.existe(matricule))
				throw new IFT287Exception("Le particpant n'existe pas pour le matricule�: " + matricule);
			if (!motDePasse.equals(secondMotDePasse))
				throw new IFT287Exception("Le mot de passe n'a pas �t� modifi�. Les deux mots de passes ne sont pas identiques !");
						
			// modification
			participants.modifierMotDePasse(matricule, motDePasse);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Ajouter un participant dans une �quipe (Le joueur postule, mais � besoin d'une approbation)
	 * @param nomEquipe
	 * @param matricule
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void postulerAUneEquipe(String nomEquipe, String matricule) throws IFT287Exception, Exception {
		try {
			// V�rifications
			if (!participants.existe(matricule))
				throw new IFT287Exception("Participant "+matricule+" n'existe pas");
			if (!equipes.existe(nomEquipe))
				throw new IFT287Exception("L'equipe selectionn�e " + nomEquipe + " est introuvable");
			
			Participant p = participants.getParticipant(matricule);
			
			// v�rifier que le joueur n'est pas d�j� dans une �quipe
			if(p.getNomEquipe() != null)
			{
				if (p.getStatut().equals("EN ATTENTE")
						&& !p.getNomEquipe().equals(nomEquipe))
					throw new IFT287Exception("Le Participant selectionn� postule d�j� pour une autre �quipe : " + p.getNomEquipe());
				if (p.getStatut().equals("ACCEPTE")
						&& !p.getNomEquipe().equals(nomEquipe))
					throw new IFT287Exception("Le Participant selectionn� est d�j� dans une autre �quipe : " + p.getNomEquipe());
				if (p.getStatut().equals("ACCEPTE")
						&& p.getNomEquipe().equals(nomEquipe))
					throw new IFT287Exception("Le Participant selectionn� est d�j� dans cette equipe");
				if (participants.getParticipant(matricule).getStatut().equals("EN ATTENTE")
						&& p.getNomEquipe().equals(nomEquipe))
					throw new IFT287Exception("Le Participant postule d�j� pour cette equipe");
			}

			participants.ajouteParEquipe(nomEquipe, matricule);
			
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Accepter un participant dans une �quipe
	 * @param nomEquipe
	 * @param matricule
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void accepteParEquipe(String nomEquipe, String matricule) throws IFT287Exception, Exception {
		try {
			// V�rifications
			if (!participants.existe(matricule))
				throw new IFT287Exception("Le participant ayant le matricule '"+matricule+"' n'existe pas");
			if (!equipes.existe(nomEquipe))
				throw new IFT287Exception("L'equipe selectionn�e '" + nomEquipe + "' est introuvable");
			
			Participant p = participants.getParticipant(matricule);
			Equipe e = equipes.getEquipe(nomEquipe);
			Ligue l = ligues.getLigue(e.getNomLigue());
			
			if(e.getNbParticipants() >= l.getNbJoueurMaxParEquipe())
				throw new IFT287Exception("Impossible d'ajouter un nouveau joueur dans l'�quipe : " + nomEquipe + ", puisque nombre de joueurs max d�pass�.");
			if(p.getNomEquipe() != null)
			{
				if (p.getStatut().equals("EN ATTENTE")
						&& !p.getNomEquipe().equals(nomEquipe))
					throw new IFT287Exception("Le Participant selectionn� postule pour une autre �quipe : " + p.getNomEquipe());
				if (p.getStatut().equals("ACCEPTE")
						&& !p.getNomEquipe().equals(nomEquipe))
					throw new IFT287Exception("Le Participant selectionn� est d�j� dans une autre �quipe : "+ p.getNomEquipe());
				if (p.getStatut().equals("ACCEPTE")
						&& p.getNomEquipe().equals(nomEquipe))
					throw new IFT287Exception("Le Participant selectionn� est d�j� dans l'equipe");
				if (p.getStatut().equals("REFUSE")
						&& p.getNomEquipe().equals(nomEquipe))
					throw new IFT287Exception("le joueur a �t� refus� ut�rieurement, pour l'accepter, il faut qu'il repostule � nouveau.");
			}
			if(!p.getStatut().equals("EN ATTENTE"))
				throw new IFT287Exception("Le joueur ayant le matricule " + matricule + " n'a pas postul� pour une �quipe.");
						
			participants.accepteParEquipe(matricule);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Refuser un participant dans une �quipe
	 * @param nomEquipe
	 * @param matricule
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void refuseParEquipe(String nomEquipe, String matricule) throws IFT287Exception, Exception {
		try {
			// V�rifications
			if (!participants.existe(matricule))
				throw new IFT287Exception("Particpant n'existe pas�: " + matricule);
			if (!equipes.existe(nomEquipe))
				throw new IFT287Exception("L'equipe selectionn�e " + nomEquipe + " est introuvable");
			
			Participant p = participants.getParticipant(matricule);
			
			if (p.getStatut().equals("EN ATTENTE")
					&& !p.getNomEquipe().equals(nomEquipe))
				throw new IFT287Exception("Le Participant selectionn� est en attente pour une autre �quipe : "+ p.getNomEquipe());
			if (p.getStatut().equals("REFUSE")
					&& p.getNomEquipe().equals(nomEquipe))
				throw new IFT287Exception("Le Participant selectionn� a deja refuse pour cette equipe");
			if(!p.getStatut().equals("EN ATTENTE"))
				throw new IFT287Exception("Le joueur ayant le matricule " + matricule + " n'a pas fait de demande d'adh�sion.");

			participants.refuseParEquipe(matricule);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Supprimer un participant d'une �quipe (le retirer de la liste des participants de l'�quipe)
	 * @param nomEquipe
	 * @param matricule
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void supprimeParEquipe(String nomEquipe, String matricule) throws IFT287Exception, Exception {
		try {
			// V�rifie si le participant existe
			if (!participants.existe(matricule))
				throw new IFT287Exception("Participant n'existe pas�: " + matricule);
			if (!equipes.existe(nomEquipe))
				throw new IFT287Exception("L'equipe selectionn�e " + nomEquipe + " est introuvable");
			if (participants.getParticipant(matricule).getStatut().equals("EN ATTENTE")
					&& !participants.getParticipant(matricule).getNomEquipe().equals(nomEquipe))
				throw new IFT287Exception("Le Participant selectionn� postule d�j� pour une autre �quipe : "
						+ participants.getParticipant(matricule).getNomEquipe());
			if (participants.getParticipant(matricule).getStatut().equals("EN ATTENTE")
					&& participants.getParticipant(matricule).getNomEquipe().equals(nomEquipe))
				throw new IFT287Exception(
						"Le Participant selectionn� postule pour cette equipe, il ne peut pas �tre supprim� mais peut �tre refus�");
			if (participants.getParticipant(matricule).getStatut().equals("ACCEPTE")
					&& !participants.getParticipant(matricule).getNomEquipe().equals(nomEquipe))
				throw new IFT287Exception("Le Participant selectionn� est dans une autre equipe");
			
			Equipe tupleEquipe = equipes.getEquipe(nomEquipe);
			if(tupleEquipe.getMatriculeCap().equals(matricule)) {
				equipes.changerCapitaine(nomEquipe, null);
			}
			participants.supprimeParEquipe(matricule);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Supprimer un participant
	 * @param matricule
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void supprimer(String matricule) throws IFT287Exception, Exception {
		try {
			// Validation
			Participant p = participants.getParticipant(matricule);
			if (p == null)
				throw new IFT287Exception("Particpant inexistant: " + matricule);
			if (p.isActive())
				throw new IFT287Exception("Particpant " + matricule + " est dans equipe "
						+ p.getNomEquipe() + "et ne peut pas �tre supprimer");

			// Suppression du participant.
			if (participants.supprimer(matricule))
				throw new IFT287Exception("Particpant " + matricule + " inexistant");

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Lecture des participants d'une �quipe
	 * @param nomEquipe
	 * @return liste de participants
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public ArrayList<Participant> lectureParticipants(String nomEquipe)
			throws IFT287Exception, Exception {
		// Validation
		try {
			// Validation
			Equipe tupleEquipe = equipes.getEquipe(nomEquipe);
			if (tupleEquipe == null)
				throw new IFT287Exception("Equipe inexistant: " + nomEquipe);
			if (!tupleEquipe.isActive())
				throw new IFT287Exception("Equipe " + nomEquipe + "a encore des participants actifs");

			ArrayList<Participant> listeParticipant = participants.lectureParticipants(nomEquipe);

			return listeParticipant;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Affichage des participants d'une �quipe
	 * @param nomEquipe
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void affichageParticipants(String nomEquipe) throws IFT287Exception, Exception {
		try {
			// Validation
			Equipe tupleEquipe = equipes.getEquipe(nomEquipe);
			if (tupleEquipe == null)
				throw new IFT287Exception("Equipe inexistant: " + nomEquipe);
			if (!tupleEquipe.isActive())
				throw new IFT287Exception("Equipe " + nomEquipe + "a encore des participants actifs");

			@SuppressWarnings("unused")
			ArrayList<Participant> listeParticipant = participants.lectureParticipants(nomEquipe);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Affichage de l'ensemble des participants de la table.
	 * 
	 *  @throws IFT287Exception, Exception
	 */
	public void affichageParticipants() throws IFT287Exception, Exception {
		try {
			@SuppressWarnings("unused")
			ArrayList<Participant> listeParticipant = participants.lectureParticipants();

		} catch (Exception e) {
			throw e;
		}
	}

}
