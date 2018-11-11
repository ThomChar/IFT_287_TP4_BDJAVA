package modele;

import CentreSportif.IFT287Exception;

public class GestionLigue {

  	private Ligues ligue;
  	private Equipes equipe;
  	private Participants participant;

    /**
     * Creation d'une instance
     */
    public GestionLigue(Ligues ligue, Equipes equipe, Participants participant) throws IFT287Exception
    {
        if (participant.getConnexion() != ligue.getConnexion() || equipe.getConnexion() != ligue.getConnexion())
            throw new IFT287Exception("Les instances de ligue, particpant et equipe n'utilisent pas la même connexion au serveur");
        this.ligue = ligue;
        this.equipe = equipe;
        this.participant = participant;
    }

    /**
     * Ajout d'une nouvelle ligue vide dans la base de données. S'il existe déjà , une
     * exception est levée.
     * 
     * @throws IFT287Exception, Exception
     */		
    public void ajouterLigueEmpty(String nomLigue, int nbJoueurMaxParEquipe) throws IFT287Exception, Exception
    {
        try
        {
            // Vérifie si la ligue existe déjà
            if (ligue.existe(nomLigue))
                throw new IFT287Exception("Ligue "+nomLigue+" existe déjà ");

            // Ajout d'une ligue vide dans la table des ligues
            ligue.creer(nomLigue, nbJoueurMaxParEquipe);
            
        }
        catch (Exception e)
        {
            throw e;
        }
    }
    
    
    /**
     * Modifier le nombre de joueur max par equipe pour une ligue dans la base de données. 
     * 
     *  @throws IFT287Exception, Exception
     */		
    public void modifierNombreJoueurMax(String nomLigue, int nbJoueurMaxParEquipe) throws IFT287Exception, Exception
    {
        try
        {
            // Vérifie si la ligue existe déjà
            if (ligue.existe(nomLigue))
                throw new IFT287Exception("Ligue "+nomLigue+" existe déjà : ");
            
            // Ajout de la ligue dans la table des ligues
            ligue.modifierNbJoueursMaxParEquipe(nomLigue, nbJoueurMaxParEquipe);;
        }
        catch (Exception e)
        {
            throw e;
        }
    }
        
    
    /**
     * Supprime Ligue de la base de données.
     * 
     *  @throws SQLException, IFT287Exception, Exception
     */
    public void supprime(String nomLigue) throws IFT287Exception, Exception
    {
        try
        {
            // Validation
            Ligue tupleLigue = ligue.getLigue(nomLigue);
            if (tupleLigue == null)
                throw new IFT287Exception("Ligue inexistant: " + nomLigue);
            if (participant.nombreMembresLigue(nomLigue) > 0)
                throw new IFT287Exception("Ligue " + nomLigue + "a encore des participants actifs");
            
            // Suppression des equipes de la ligue.
            @SuppressWarnings("unused")
			//int nbEquipe = equipe.supprimerEquipesLigue(nomLigue);
            // Suppression de la ligue.
            boolean nbLique = ligue.supprimer(nomLigue);
            if (nbLique == false)
                throw new IFT287Exception("Ligue " + nomLigue + " inexistante");
        }
        catch (Exception e)
        {
            throw e;
        }
    }
}
