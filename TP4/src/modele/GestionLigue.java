package modele;

import CentreSportif.IFT287Exception;

public class GestionLigue {

  	private Ligues ligues;
  	private Equipes equipes;
  	private Participants participants;

    /**
     * Creation d'une instance
     */
    public GestionLigue(Ligues ligue, Equipes equipe, Participants participant) throws IFT287Exception
    {
        if (participant.getConnexion() != ligue.getConnexion() || equipe.getConnexion() != ligue.getConnexion())
            throw new IFT287Exception("Les instances de ligue, particpant et equipe n'utilisent pas la m�me connexion au serveur");
        this.ligues = ligue;
        this.equipes = equipe;
        this.participants = participant;
    }

    /**
     * Ajout d'une nouvelle ligue. S'il existe d�j�, une exception est lev�e.
     * @param nomLigue
     * @param nbJoueurMaxParEquipe
     * @throws IFT287Exception
     * @throws Exception
     */	
    public void ajouterLigue(String nomLigue, int nbJoueurMaxParEquipe) throws IFT287Exception, Exception
    {
        try
        {
            // V�rifie si la ligue existe d�j�
            if (ligues.existe(nomLigue))
            	throw new IFT287Exception("La ligue "+nomLigue+" existe d�j�.");

            // Ajout d'une ligue
            ligues.creer(nomLigue, nbJoueurMaxParEquipe);
            
        }
        catch (Exception e)
        {
            throw e;
        }
    }
    
    
    /**
     * Modifier le nombre de joueur max par equipe pour une ligue dans la base de donn�es.
     * @param nomLigue
     * @param nbJoueurMaxParEquipe
     * @throws IFT287Exception
     * @throws Exception
     */
    public void modifierNombreJoueurMax(String nomLigue, int nbJoueurMaxParEquipe) throws IFT287Exception, Exception
    {
        try
        {
            // V�rifie si la ligue existe d�j�
            if (ligues.existe(nomLigue))
                throw new IFT287Exception("Ligue "+nomLigue+" existe d�j�: ");
            
            // Modification de la ligue
            ligues.modifierNbJoueursMaxParEquipe(nomLigue, nbJoueurMaxParEquipe);;
        }
        catch (Exception e)
        {
            throw e;
        }
    }
        
    
    /**
     * Supprime Ligue
     * @param nomLigue
     * @throws IFT287Exception
     * @throws Exception
     */
    public void supprimer(String nomLigue) throws IFT287Exception, Exception
    {
        try
        {
            // Validations
            Ligue tupleLigue = ligues.getLigue(nomLigue);
            if (tupleLigue == null)
                throw new IFT287Exception("La ligue '"+nomLigue+"' est inexistante.");
            if (participants.nombreMembresLigue(nomLigue) > 0)
                throw new IFT287Exception("La ligue '" + nomLigue + "' a encore des participants actifs");
            
            // Suppression des equipes de la ligue.
            if(equipes.supprimerEquipesLigue(nomLigue))
            	throw new IFT287Exception("La ligue '" + nomLigue + "' inexistante");
            
            // Suppression de la ligue.
            if (ligues.supprimer(nomLigue))
                throw new IFT287Exception("La ligue '" + nomLigue + "' inexistante");
        }
        catch (Exception e)
        {
            throw e;
        }
    }
}
