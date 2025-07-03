package fr.eni.encheres.bll;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dal.UtilisateurDAO;
import fr.eni.encheres.exception.BusinessException;

@Service
public class UtilisateurServiceImpl implements UtilisateurService{
	
	private UtilisateurDAO utilisateurDAO;

	public UtilisateurServiceImpl(UtilisateurDAO utilisateurDAO) {
		this.utilisateurDAO = utilisateurDAO;
	}


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
	public Utilisateur consulterUtilisateurParPseudo(String pseudo) {
		return this.utilisateurDAO.utilisateurParPseudo(pseudo);
	}

	@Override
	public int consulterCredit(Utilisateur utilisateur) {
		return utilisateur.getCredit();
	}

	@Override
	public void debiter(int debit, Utilisateur utilisateur) {
		int solde = utilisateur.getCredit();
		if (solde > debit) {
			solde -= debit;
			utilisateur.setCredit(solde);
		}
	}
	
	@Override
	public void credit(int credit, Utilisateur utilisateur) {
		int solde = utilisateur.getCredit();
		solde += credit;
		utilisateur.setCredit(solde);
	}

	@Override
	public void creerUtilisateur(Utilisateur utilisateur) throws BusinessException {
		BusinessException be = new BusinessException();
		
		boolean isValid = isEmailValide(utilisateur.getEmail(), be);
		
		if(isValid) {
			utilisateurDAO.creerUtilisateur(utilisateur);


		} else {


			throw be;
		}
		
	}

	@Override
	public void supprimerUtilisateur(Utilisateur utilisateur) {
		BusinessException be = new BusinessException();
		
		if(isAdmin(utilisateur.getIdUtilisateur(), be)) {
			utilisateurDAO.supprimerUtilisateur(utilisateur);
		}
		
		
	}

	@Override
	public boolean isEmailValide(String email, BusinessException be) {
		if(utilisateurDAO.isEmailValide(email)) {
			return true;
		}be.add("Vous avez déjà un compte créé");
		return false;
	}

	@Override
	public boolean isCompteExist(long idUtilisateur, BusinessException be) {
		if(utilisateurDAO.isExist(idUtilisateur)) {
			return true;
		}be.add("Ce compte n'existe pas");
		return false;
	}

	@Override
	public boolean isAdmin(long idUtilisateur, BusinessException be) {
		if (utilisateurDAO.isAdmin(idUtilisateur)) {
			return true;
		}be.add("Action non autorisée, vous n'êtes pas administrateur du site");
		return false;
	}



	
	
	
	





}
