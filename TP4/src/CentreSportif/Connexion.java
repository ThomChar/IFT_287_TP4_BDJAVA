package CentreSportif;

import javax.persistence.*;
import java.util.*;

/**
 * Gestionnaire d'une connexion avec une BD Objet via ObjectDB.
 * 
 * <pre>
 * 
 * Vincent Ducharme
 * Universit� de Sherbrooke
 * Version 1.0 - 18 juin 2016
 * IFT287 - Exploitation de BD relationnelles et OO
 * 
 * Ce programme permet d'ouvrir une connexion avec une BD via ObjectDB.
 * 
 * Pr�-condition
 *   La base de donn�e ObjectDB doit �tre accessible.
 * 
 * Post-condition
 *   La connexion est ouverte.
 * </pre>
 */
public class Connexion
{
    private EntityManager em;
    private EntityManagerFactory emf;

    /**
     * Ouverture d'une connexion
     * 
     * @param serveur : Le type de serveur SQL � utiliser (Valeur : local, dinf).
     * @param bd : nom de la base de donn�es
     * @param user : userid sur le serveur SQL
     * @param pass : mot de passe sur le serveur SQL
     */
    public Connexion(String serveur, String bd, String user, String pass) throws IFT287Exception
    {
        if (serveur.equals("local"))
        {
            emf = Persistence.createEntityManagerFactory(bd);
        }
        else if (serveur.equals("dinf"))
        {
        	Map<String, String> properties = new HashMap<String, String>();
        	  properties.put("javax.persistence.jdbc.user", user);
        	  properties.put("javax.persistence.jdbc.password", pass);
        	emf = Persistence.createEntityManagerFactory("objectdb://bd-info2.dinf.usherbrooke.ca:6136/"+user+"/" + bd, properties);
        }
        else
        {
            throw new IFT287Exception("Serveur inconnu");
        }
    	
    	em = emf.createEntityManager();
        
        System.out.println("Ouverture de la connexion :\n"
                + "Connect� sur la BD ObjectDB "
                + bd + " avec l'utilisateur " + user);
    }

    /**
     * fermeture d'une connexion
     */
    public void fermer()
    {
        em.close();
        emf.close();
        System.out.println("Connexion ferm�e");
    }
    
    public void demarreTransaction()
    {
    	em.getTransaction().begin();
    }

    /**
     * commit
     */
    public void commit()
    {
        em.getTransaction().commit();
    }

    /**
     * rollback
     */
    public void rollback()
    {
        em.getTransaction().rollback();
    }

    /**
     * retourne la Connection ObjectDB
     */
    public EntityManager getConnection()
    {
        return em;
    }
}// Classe Connexion
