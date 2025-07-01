package fr.eni.encheres.bll;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dal.ArticleDAO;
import fr.eni.encheres.dal.UtilisateurDAO;

@Service
public class UtilisateurServiceImpl implements UtilisateurService{
	
	private UtilisateurDAO utilisateurDAO;

	

	@Override
	public List<Utilisateur> consulterUtilisateurs() {
		List<Utilisateur> lstUtilisateurs = utilisateurDAO.consulterUtilisateurs();
//		if (lstUtilisateurs != null) {
//			for (Utilisateur utilisateur : lstUtilisateurs) {
//				utilisateur.setEmail(null);
//			}
//		}
		return lstUtilisateurs;
	}

	@Override
	public Utilisateur consulterUtilisateursParId(long id) {
		Utilisateur utilisateur = utilisateurDAO.utilisateurparId(id);
		return utilisateur;
	}

	@Override
	public int consulterCredit(Utilisateur utilisateur) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Utilisateur debiter(int debit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void creerUtilisateur(Utilisateur utilisateur) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void supprimerUtilisateur(Utilisateur utilisateur) {
		// TODO Auto-generated method stub
		
	}

}
