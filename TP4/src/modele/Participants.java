package modele;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Filters.and;

import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import CentreSportif.Connexion;

public class Participants {
	
	private Connexion cx;
	private MongoCollection<Document> participantsCollection;

    /**
	 * Creation d'une instance.
	 */
	public Participants(Connexion cx) {
		this.cx = cx;
		participantsCollection = cx.getDatabase().getCollection("Participants");
	}

	/**
	 * Retourner la connexion associee.
	 * @return Connexion
	 */
	public Connexion getConnexion() {
		return cx;
	}
	
	/**
	 * Ajouter un participant
	 * @param matricule
	 * @param prenom
	 * @param nom
	 * @param motDePasse
	 * @param nomEquipe
	 * @param statut
	 */
    public void ajouter(String matricule, String prenom, String nom, String motDePasse) {
    	
    	Participant p = new Participant(matricule, prenom, nom, motDePasse);
    	participantsCollection.insertOne(p.toDocument());
    }

    /**
     * Vérifie si un participant existe.
     * @param matricule
     * @return vrai ou faux
     */

    public boolean existe(String matricule)
    {
    	return participantsCollection.find(eq("matricule", matricule)).first() != null;
    }

    /**
     * Lecture d'un participant.
     * @param matricule
     * @return un participant
     */
    public Participant getParticipant(String matricule)
    {
    	Document p = participantsCollection.find(eq("matricule", matricule)).first();
    	if(p != null)
    	{
    		return new Participant(p);
    	}
        return null;
    }

    /**
     * Modifier nom et prenom d'un participant
     * @param matricule
     * @param prenom
     * @param nom
     */
    public void modifierNomPrenom(String matricule, String prenom, String nom){
    	participantsCollection.updateOne(eq("matricule", matricule), set("prenom", prenom));
    	participantsCollection.updateOne(eq("matricule", matricule), set("nom", nom)); 
    }
    
    /**
     * Modifier motDePasse d'un participant
     * @param matricule
     * @param motDePasse
     */
    public void modifierMotDePasse(String matricule, String motDePasse) {
    	participantsCollection.updateOne(eq("matricule", matricule), set("motDePasse", motDePasse)); 
    }
    
    /**
     * Participant postule à une équipe
     * @param nomEquipe
     * @param matricule
     */
    public void ajouteParEquipe(String nomEquipe, String matricule)
    {
    	participantsCollection.updateOne(eq("matricule", matricule), set("nomEquipe", nomEquipe));
    	participantsCollection.updateOne(eq("matricule", matricule), set("statut", "EN ATTENTE")); 
    }
    
    /**
     * Accepter le participant dans une equipe.
     * @param matricule
     */
    public void accepteParEquipe(String matricule)
    {
    	participantsCollection.updateOne(eq("matricule", matricule), set("statut", "ACCEPTE")); 
    }

    /**
     * Refuser Participant disponible dans une equipe
     * @param matricule
     */
    public void refuseParEquipe(String matricule)
    {
    	participantsCollection.updateOne(eq("matricule", matricule), set("statut", "REFUSE")); 
    }
    
    /**
     * Supprimer Participant dans une equipe
     * @param matricule
     */
    public void supprimeParEquipe(String matricule)
    {
    	participantsCollection.updateOne(eq("matricule", matricule), set("statut", "SUPPRIME")); 
    }

    /**
     * Suppression d'un participant du complexe.
     * @param p
     * @return vrai ou faux
     */
    public boolean supprimer(String matricule) {
    	return participantsCollection.deleteOne(eq("matricule", matricule)).getDeletedCount() > 0;
    }
    
    /**
     * Lecture de tous les participants.
     * @return liste de participants
     */
    public ArrayList<Participant> lectureParticipants()
    {
    	ArrayList<Participant> listParticipants = new ArrayList<Participant>();
		
		MongoCursor<Document> parts = participantsCollection.find(eq("statut", "ACCEPTE")).sort(ascending("nom")).iterator();
		while (parts.hasNext()) {
			listParticipants.add(new Participant(parts.next()));
		}
		
		return listParticipants;
    }
    
    /**
     * Lecture des participants d'une équipes
     * @param nomEquipe
     * @return liste de participants
     */
	public ArrayList<Participant> lectureParticipants(String nomEquipe) {
		ArrayList<Participant> listParticipants = new ArrayList<Participant>();
		
		MongoCursor<Document> parts = participantsCollection.find(and(eq("nomEquipe", nomEquipe),eq("statut", "ACCEPTE"))).sort(ascending("nom")).iterator();
		while (parts.hasNext()) {
			listParticipants.add(new Participant(parts.next()));
		}
		
		return listParticipants;	
	}
	
	/**
	 * Compter nombres de particpants dans une équipe
	 * @param nomEquipe
	 * @return nombre de membres dans une équipe
	 */
	public long nombreMembresEquipe(String nomEquipe)
	{
		return participantsCollection.countDocuments(and(eq("nomEquipe", nomEquipe), eq("statut", "ACCEPTE")));
	}
	
	/**
	 * Compter nombres de particpants dans une ligue
	 * @param nomLigue
	 * @return nombre de membres dans une ligue
	 */
	public long nombreMembresLigue(String nomLigue) 
	{
		return participantsCollection.countDocuments(and(eq("nomLigue", nomLigue), eq("statut", "ACCEPTE")));
	}
 }
