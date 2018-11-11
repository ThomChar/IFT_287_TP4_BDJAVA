package modele;

import java.util.List;
import javax.persistence.TypedQuery;
import CentreSportif.Connexion;

public class Resultats {

	private TypedQuery<Resultat> stmtExiste;
	private TypedQuery<Long> stmtNbMGagne;
	private TypedQuery<Long> stmtNbMPerdu;
	private TypedQuery<Long> stmtNbMNul;
	private TypedQuery<Resultat> stmtListTousResultats;
	private TypedQuery<Resultat> stmtListTousResultatsEquipe;
	private Connexion cx;

	/**
	 * Creation d'une instance.
	 * @param cx
	 */
	public Resultats(Connexion cx) {
		this.cx = cx;
		stmtExiste = cx.getConnection().createQuery(
				"select r from Resultat r where (equipeA.nomEquipe = :nomEquipeA and equipeB.nomEquipe = :nomEquipeB) or (equipeA.nomEquipe = :nomEquipeB and equipeB.nomEquipe = :nomEquipeA)",
				Resultat.class);

		stmtListTousResultats = cx.getConnection().createQuery("select r from Resultat r", Resultat.class);
		stmtNbMGagne = cx.getConnection().createQuery(
				"select count(r) AS nb from Resultat r where (equipeA.nomEquipe = :nomEquipeA and scoreEquipeA > scoreEquipeB) or (equipeB.nomEquipe = :nomEquipeB and scoreEquipeA < scoreEquipeB)",
				Long.class);
		stmtNbMPerdu = cx.getConnection().createQuery(
				"select count(r) AS nb from Resultat r where (equipeA.nomEquipe = :nomEquipeA and scoreEquipeA < scoreEquipeB) or (equipeB.nomEquipe = :nomEquipeB and scoreEquipeA > scoreEquipeB)",
				Long.class);
		stmtNbMNul = cx.getConnection().createQuery(
				"select count(r) AS nb from Resultat r where (equipeA.nomEquipe = :nomEquipeA or equipeB.nomEquipe = :nomEquipeB) and scoreEquipeA = scoreEquipeB",
				Long.class);
		stmtListTousResultatsEquipe = cx.getConnection().createQuery(
				"select r from Resultat r where equipeA.nomEquipe = :nomEquipeA or equipeB.nomEquipe = :nomEquipeB",
				Resultat.class);

	}

	/**
	 * Retourner la connexion associ�e.
	 * @returncx
	 */
	public Connexion getConnexion() {
		return cx;
	}

	/**
	 * V�rifier si un r�sultat existe
	 * @param nomEquipeA
	 * @param nomEquipeB
	 * @return vrai ou faux
	 */
	public boolean existe(String nomEquipeA, String nomEquipeB) {
		stmtExiste.setParameter("nomEquipeA", nomEquipeA);
		stmtExiste.setParameter("nomEquipeB", nomEquipeB);
		return !stmtExiste.getResultList().isEmpty();
	}

	/**
	 * Lecture d'un resultat.
	 * @param nomEquipeA
	 * @param nomEquipeB
	 * @return un r�sultat
	 */
	public Resultat getResultat(String nomEquipeA, String nomEquipeB) {
		stmtExiste.setParameter("nomEquipeA", nomEquipeA);
		stmtExiste.setParameter("nomEquipeB", nomEquipeB);
		List<Resultat> resultats = stmtExiste.getResultList();
		if (!resultats.isEmpty()) {
			return resultats.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Ajout d'un nouveau r�sultat
	 * @param resultat
	 * @return le r�sultat cr��
	 */
	public Resultat creer(Resultat resultat) {
        cx.getConnection().persist(resultat);
        return resultat;
	}

	/**
	 * Supprimer un r�sultat
	 * @param resultat
	 * @return vrai ou faux
	 */
    public boolean supprimer(Resultat resultat)
    {
        if(resultat != null)
        {
            cx.getConnection().remove(resultat);
            return true;
        }
        return false;
    }

    /**
     * Obtenir nombre matchs gagn�s d'une �quipe.
     * @param nomEquipe
     * @return nombre de matchs gagn�s
     */
	public long ObtenirNbMGagne(String nomEquipe) {
		stmtNbMGagne.setParameter("nomEquipeA", nomEquipe);
		stmtNbMGagne.setParameter("nomEquipeB", nomEquipe);
		return stmtNbMGagne.getSingleResult();
	}

	/**
	 * Obtenir le nombre de matchs perdus d'une �quipe
	 * @param nomEquipe
	 * @return le nombre de matchs perdus
	 */
	public long ObtenirNbMPerdu(String nomEquipe) {
		stmtNbMPerdu.setParameter("nomEquipeA", nomEquipe);
		stmtNbMPerdu.setParameter("nomEquipeB", nomEquipe);
		return stmtNbMPerdu.getSingleResult();
	}

	/**
	 * Obtenir nombre matchs nuls d'une �quipe.
	 * @param nomEquipe
	 * @return le nombre matchs nuls d'une �quipe.
	 */
	public long ObtenirNbMNul(String nomEquipe) {
		stmtNbMNul.setParameter("nomEquipeA", nomEquipe);
		stmtNbMNul.setParameter("nomEquipeB", nomEquipe);
		return stmtNbMNul.getSingleResult();
	}

	/**
	 * Retourne l'ensemble des resultats d'une equipe
	 * @param nomEquipe
	 * @return liste de r�sultats
	 */
    public List<Resultat> calculerListeResultatsEquipe(String nomEquipe)
    {
    	stmtListTousResultatsEquipe.setParameter("nomEquipeA", nomEquipe);
    	stmtListTousResultatsEquipe.setParameter("nomEquipeB", nomEquipe);
        return stmtListTousResultatsEquipe.getResultList();
    }    

    /**
     * Retourner la liste de tous les r�sultats
     * @return liste de r�sultats
     */
    public List<Resultat> calculerListeResultats()
    {
        return stmtListTousResultats.getResultList();
    }
}
