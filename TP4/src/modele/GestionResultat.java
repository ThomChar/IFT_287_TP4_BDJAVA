package modele;

import java.util.List;

import CentreSportif.Connexion;
import CentreSportif.IFT287Exception;

public class GestionResultat {

	private Resultats resultats;
	private Equipes equipes;
	private Connexion cx;

	/**
	 * Creation d'une instance
	 * @param resultats
	 * @param equipes
	 * @throws IFT287Exception
	 */
	public GestionResultat(Resultats resultats, Equipes equipes) throws IFT287Exception {
		this.cx = resultats.getConnexion();
		if (equipes.getConnexion() != resultats.getConnexion())
    		throw new IFT287Exception("Les diff�rents gestionnaires (equipes et r�sultats) n'utilisent pas la m�me connexion au serveur");

		this.resultats = resultats;
		this.equipes = equipes;
	}

	/**
	 * Inscrit un resultat enntre deux equipes
	 * @param nomEquipeA
	 * @param nomEquipeB
	 * @param scoreEquipeA
	 * @param scoreEquipeB
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void InscrireResulat(String nomEquipeA, String nomEquipeB, int scoreEquipeA, int scoreEquipeB) throws IFT287Exception, Exception {
		try {
			cx.demarreTransaction();
			
			Equipe eqA = equipes.getEquipe(nomEquipeA);
			Equipe eqB = equipes.getEquipe(nomEquipeB);
			
			// Validations
			if(eqA == null)
				throw new IFT287Exception("L'�quipe " + nomEquipeA + " n'existe pas.");
			if(eqB == null)
				throw new IFT287Exception("L'�quipe " + nomEquipeB + " n'existe pas.");
			if(nomEquipeA.equals(nomEquipeB))
				throw new IFT287Exception("Les deux �quipes saisies sont identiques.");
			
			Resultat tupleResultat = resultats.getResultat(nomEquipeA,nomEquipeB);
			
			if (tupleResultat != null)
				throw new IFT287Exception("Resultat deja existant: " + nomEquipeA + " contre " + nomEquipeB );

			// ajout du r�sulat
			Resultat tupleNewResultat = new Resultat(eqA, eqB, scoreEquipeA, scoreEquipeB);
			resultats.creer(tupleNewResultat);
			eqA.ajouterResultat(tupleNewResultat);
			eqB.ajouterResultat(tupleNewResultat);

			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}

	/**
	 * Modifier resultat. Le resultat doit exister
	 * @param nomEquipeA
	 * @param nomEquipeB
	 * @param scoreEquipeA
	 * @param scoreEquipeB
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void modifierResultat(String nomEquipeA, String nomEquipeB, int scoreEquipeA, int scoreEquipeB) throws IFT287Exception, Exception {
		try {
			cx.demarreTransaction();
			
			// V�rification
			if (!resultats.existe(nomEquipeA, nomEquipeB))
				throw new IFT287Exception("Resultat entre " + nomEquipeA + " et " + nomEquipeB + " n'existe pas");
			
			Resultat r = resultats.getResultat(nomEquipeA, nomEquipeB);
			
			// modifications
			r.setScoreEquipeA(scoreEquipeA);
			r.setScoreEquipeB(scoreEquipeB);
		
			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}
	
	/**
	 * Supprimer r�sultat
	 * @param nomEquipeA
	 * @param nomEquipeB
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void supprimerResultat(String nomEquipeA, String nomEquipeB) throws IFT287Exception, Exception {
		try {
			cx.demarreTransaction();
			
			Resultat r = resultats.getResultat(nomEquipeA, nomEquipeB);
			
			// V�rification
			if (resultats.supprimer(r) == false)
				throw new IFT287Exception("Resultat entre " + nomEquipeA + " et " + nomEquipeB + " n'existe pas");
			
			Equipe eqA = equipes.getEquipe(nomEquipeA);
			Equipe eqB = equipes.getEquipe(nomEquipeB);
			
			// supprimer le r�sultat de la liste
			eqA.supprimerResultat(r);
			eqB.supprimerResultat(r);
			resultats.supprimer(r);

			cx.commit();
		} catch (Exception e) {
			cx.rollback();
			throw e;
		}
	}
	
	/**
	 * Affichage de l'ensemble des r�sultats
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void affichageResultats() throws IFT287Exception, Exception {
		cx.demarreTransaction();
		List<Resultat> list = resultats.calculerListeResultats();

		for (Resultat r : list) {
			System.out.println(r.toString());
		}
		cx.commit();
	}
	
	/**
	 * Affichage de l'ensemble des r�sultats d'une equipe.
	 * @param nomEquipe
	 * @throws IFT287Exception
	 * @throws Exception
	 */
	public void affichageResultatsEquipe(String nomEquipe) throws IFT287Exception, Exception {
		cx.demarreTransaction();
		List<Resultat> list = resultats.calculerListeResultatsEquipe(nomEquipe);

		for (Resultat r : list) {
			System.out.println(r.toString());
		}
		cx.commit();
	}
}