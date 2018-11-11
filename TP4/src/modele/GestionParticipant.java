package modele;

import java.sql.SQLException;
import java.util.List;

import CentreSportif.IFT287Exception;
import CentreSportif.Connexion;

public class GestionParticipant {

	private Participants participants;
	private Equipes equipes;
	private Ligues ligues;
	private Connexion cx;

	/**
	 * Creation d'une instance
	 * @param participants
	 * @param equipes
	 * @param ligues
	 * @throws IFT287Exception
	 */
	public GestionParticipant(Participants participants, Equipes equipes, Ligues ligues) throws IFT287Exception {
		this.cx = participants.getConnexion();
		
		this.cx = equipes.getConnexion();
		if (equipes.getConnexion() != ligues.getConnexion() || participants.getConnexion() != equipes.getConnexion())
	    		throw new IFT287Exception("Les diff�rents gestionnaires (equipes, partcipants, ligues) n'utilisent pas la m�me connexion au serveur");

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
			cx.demarreTransaction();
			
			// Validation
			if (participants.existe(matricule))
				throw new IFT287Exception("Le particpant existe d�j�pour le matricule : " + matricule);
			
			// Ajout du participant dans la table des participants
			Participant p = new Participant(matricule, prenom, nom, motDePasse);
			participants.creer(p);
			
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
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
			cx.demarreTransaction();
			
			// V�rification
			if (!participants.existe(matricule))
				throw new IFT287Exception("Le particpant n'existe pas pour le matricule�: " + matricule);
		
			// modifications
			Participant p = participants.getParticipant(matricule);
			p.setNom(nom);
			p.setPrenom(prenom);

			cx.commit();
		} catch (Exception e) {
			cx.rollback();
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
			cx.demarreTransaction();
			
			// V�rifications
			if (!participants.existe(matricule))
				throw new IFT287Exception("Le particpant n'existe pas pour le matricule�: " + matricule);
			if (!motDePasse.equals(secondMotDePasse))
				throw new IFT287Exception("Le mot de passe n'a pas �t� modifi�. Les deux mots de passes ne sont pas identiques !");
			
			// modification
			Participant p = participants.getParticipant(matricule);
			p.setMotDePasse(motDePasse);

			cx.commit();
		} catch (Exception e) {
			cx.rollback();
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
			cx.demarreTransaction();

			// V�rifications
			if (!participants.existe(matricule))
				throw new IFT287Exception("Participant "+matricule+" n'existe pas");
			if (!equipes.existe(nomEquipe))
				throw new IFT287Exception("L'equipe selectionn�e " + nomEquipe + " est introuvable");
			
			Participant p = participants.getParticipant(matricule);
			Equipe e = equipes.getEquipe(nomEquipe);
			
			// v�rifier que le joueur n'est pas d�j� dans une �quipe
			if(p.getEquipe() != null)
			{
				if (p.getStatut().equals("EN ATTENTE")
						&& !p.getEquipe().getNomEquipe().equals(nomEquipe))
					throw new IFT287Exception("Le Participant selectionn� postule d�j� pour une autre �quipe : " + p.getEquipe().getNomEquipe());
				if (p.getStatut().equals("ACCEPTE")
						&& !p.getEquipe().getNomEquipe().equals(nomEquipe))
					throw new IFT287Exception("Le Participant selectionn� est d�j� dans une autre �quipe : " + p.getEquipe().getNomEquipe());
				if (p.getStatut().equals("ACCEPTE")
						&& p.getEquipe().getNomEquipe().equals(nomEquipe))
					throw new IFT287Exception("Le Participant selectionn� est d�j� dans cette equipe");
				if (participants.getParticipant(matricule).getStatut().equals("EN ATTENTE")
						&& p.getEquipe().getNomEquipe().equals(nomEquipe))
					throw new IFT287Exception("Le Participant postule d�j� pour cette equipe");
			}
			
			// postuler
			p.setStatut("EN ATTENTE");
			p.setEquipe(e);

			cx.commit();
		} catch (Exception e) {
			cx.rollback();
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
			cx.demarreTransaction();
			
			// V�rifications
			if (!participants.existe(matricule))
				throw new IFT287Exception("Le participant ayant le matricule '"+matricule+"' n'existe pas");
			if (!equipes.existe(nomEquipe))
				throw new IFT287Exception("L'equipe selectionn�e '" + nomEquipe + "' est introuvable");
			
			Participant p = participants.getParticipant(matricule);
			Equipe e = equipes.getEquipe(nomEquipe);
			Ligue l = ligues.getLigue(e.getLigue().getNomLigue());
			
			if(e.getListParticipants().size() >= l.getNbJoueurMaxParEquipe())
				throw new IFT287Exception("Impossible d'ajouter un nouveau joueur dans l'�quipe : " + nomEquipe + ", puisque nombre de joueurs max d�pass�.");
			if(p.getEquipe() != null)
			{
				if (p.getStatut().equals("EN ATTENTE")
						&& !p.getEquipe().getNomEquipe().equals(nomEquipe))
					throw new IFT287Exception("Le Participant selectionn� postule pour une autre �quipe : " + p.getEquipe().getNomEquipe());
				if (p.getStatut().equals("ACCEPTE")
						&& !p.getEquipe().getNomEquipe().equals(nomEquipe))
					throw new IFT287Exception("Le Participant selectionn� est d�j� dans une autre �quipe : "+ p.getEquipe().getNomEquipe());
				if (p.getStatut().equals("ACCEPTE")
						&& p.getEquipe().getNomEquipe().equals(nomEquipe))
					throw new IFT287Exception("Le Participant selectionn� est d�j� dans l'equipe");
				if (p.getStatut().equals("REFUSE")
						&& p.getEquipe().getNomEquipe().equals(nomEquipe))
					throw new IFT287Exception("le joueur a �t� refus� ut�rieurement, pour l'accepter, il faut qu'il repostule � nouveau.");
			}
			if(!p.getStatut().equals("EN ATTENTE"))
				throw new IFT287Exception("Le joueur ayant le matricule " + matricule + " n'a pas postul� pour une �quipe.");
			
			// accepter joueur
			p.setStatut("ACCEPTE");
			e.ajouterJoueur(p);

			cx.commit();
		} catch (Exception e) {
			cx.rollback();
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
			cx.demarreTransaction();
			
			// V�rifications
			if (!participants.existe(matricule))
				throw new IFT287Exception("Particpant n'existe pas�: " + matricule);
			if (!equipes.existe(nomEquipe))
				throw new IFT287Exception("L'equipe selectionn�e " + nomEquipe + " est introuvable");
			
			Participant p = participants.getParticipant(matricule);
			
			if (p.getStatut().equals("EN ATTENTE")
					&& !p.getEquipe().getNomEquipe().equals(nomEquipe))
				throw new IFT287Exception("Le Participant selectionn� est en attente pour une autre �quipe : "+ p.getEquipe().getNomEquipe());
			if (p.getStatut().equals("REFUSE")
					&& p.getEquipe().getNomEquipe().equals(nomEquipe))
				throw new IFT287Exception("Le Participant selectionn� a deja refuse pour cette equipe");
			if(!p.getStatut().equals("EN ATTENTE"))
				throw new IFT287Exception("Le joueur ayant le matricule " + matricule + " n'a pas fait de demande d'adh�sion.");

			p.setStatut("REFUSE");

			cx.commit();
		} catch (Exception e) {
			cx.rollback();
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
			cx.demarreTransaction();
			
			// V�rifications
			if (!participants.existe(matricule))
				throw new IFT287Exception("Participant n'existe pas�: " + matricule);
			if (!equipes.existe(nomEquipe))
				throw new IFT287Exception("L'equipe selectionn�e " + nomEquipe + " est introuvable");
			
			Participant p = participants.getParticipant(matricule);
			Equipe e = equipes.getEquipe(nomEquipe);
			
			if (p.getStatut().equals("EN ATTENTE")
					&& !p.getEquipe().getNomEquipe().equals(nomEquipe))
				throw new IFT287Exception("Le Participant selectionn� postule d�j� pour une autre �quipe : "
						+ p.getEquipe().getNomEquipe());
			if (p.getStatut().equals("EN ATTENTE")
					&& p.getEquipe().getNomEquipe().equals(nomEquipe))
				throw new IFT287Exception("Le Participant selectionn� postule pour cette equipe, il ne peut pas �tre supprim� mais peut �tre refus�");
			if (p.getStatut().equals("ACCEPTE")
					&& !p.getEquipe().getNomEquipe().equals(nomEquipe))
				throw new IFT287Exception("Le Participant selectionn� est dans une autre equipe");
			
			// on admet qu'une �quipe peut �tre provisoirement sans capitaine
			if(e.getCapitaine().getMatricule().equals(matricule)) {
				e.setCapitaine(null);
			}
			
			//suppression du joueur de l'�quipe
			e.supprimerJoueur(p);
			p.setStatut("SUPPRIME");

			cx.commit();
		} catch (Exception e) {
			cx.rollback();
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
			cx.demarreTransaction();
			
			// Validation
			Participant p = participants.getParticipant(matricule);
			if (p == null)
				throw new IFT287Exception("Particpant inexistant: " + matricule);
			if (p.isActive())
				throw new IFT287Exception("Particpant " + matricule + " est dans equipe "
						+ p.getEquipe().getNomEquipe() + "et ne peut pas �tre supprimer");

			// Suppression du participant.
			boolean operation = participants.supprimer(p);
			if (operation == false)
				throw new IFT287Exception("Particpant " + matricule + " inexistant");

			cx.commit();
		} catch (Exception e) {
			cx.rollback();
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
	public List<Participant> lectureParticipants(String nomEquipe) throws IFT287Exception, Exception {
		try {
			cx.demarreTransaction();
			// Validation
			Equipe tupleEquipe = equipes.getEquipe(nomEquipe);
			if (tupleEquipe == null)
				throw new IFT287Exception("Equipe inexistant: " + nomEquipe);
			if (!tupleEquipe.isActive())
				throw new IFT287Exception("Equipe " + nomEquipe + "a encore des participants actifs");
			
			// lecture des participants
			List<Participant> parts = participants.lectureParticipants(nomEquipe);
			
			cx.commit();
			return parts;
			
		} catch (Exception e) {
			cx.rollback();
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
			cx.demarreTransaction();
			// Validation
			Equipe tupleEquipe = equipes.getEquipe(nomEquipe);
			if (tupleEquipe == null)
				throw new IFT287Exception("Equipe inexistant: " + nomEquipe);
			if (!tupleEquipe.isActive())
				throw new IFT287Exception("Equipe " + nomEquipe + "a encore des participants actifs");
			
			// affichage
			List<Participant> listeParticipant = participants.lectureParticipants(nomEquipe);
	
			for(Participant p : listeParticipant)
	        {
	            System.out.println(p.toString());
	        }
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}

	/**
	 * Affichage de l'ensemble des participants de la table.
	 * 
	 *  @throws SQLException, IFT287Exception, Exception
	 */
	public void affichageParticipants() throws IFT287Exception, Exception {
		cx.demarreTransaction();
		List<Participant> listeParticipant = participants.lectureParticipants();
		
		for(Participant p : listeParticipant)
        {
            System.out.println(p.toString());
        }
		cx.commit();
	}

}
