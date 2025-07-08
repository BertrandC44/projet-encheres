package fr.eni.encheres.bll;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

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
	public String consulterMdpParPseudo(String pseudo) {
		return this.utilisateurDAO.consulterMdp(pseudo);
	}

	@Override
	public int consulterCredit(Utilisateur utilisateur) {
		return utilisateur.getCredit();
	}

//	@Override
//	public void debiter(int montantEnchere, Utilisateur utilisateur) {
//		int solde = utilisateur.getCredit();
//		if (solde > montantEnchere) {
//			solde -= montantEnchere;
//			utilisateur.setCredit(solde);
//		}
//	}
	
	@Override
	public void crediter(int credit, Utilisateur utilisateur) {
		int solde = utilisateur.getCredit();
		solde += credit;
		utilisateur.setCredit(solde);
	}

	@Override
	public void creerUtilisateur(Utilisateur utilisateur) throws BusinessException {
		BusinessException be = new BusinessException();
		
		boolean isValid = isEmailValide(utilisateur.getEmail(), be);
		isValid &= isPseudoValide(utilisateur.getPseudo(), be);
		
		if(isValid) {
			utilisateurDAO.creerUtilisateur(utilisateur);


		} else {


			throw be;
		}
		
	}
	
	
	@Override
	public void modifierUtilisateur(Utilisateur utilisateur, Utilisateur utilisateurEnsession) throws BusinessException {
		BusinessException be = new BusinessException();
		
		boolean isValid = isEmailModifierValide(utilisateur.getEmail(), utilisateur, utilisateurEnsession, be);
		isValid &= isPseudoModifierValide(utilisateur.getPseudo(), utilisateur, utilisateurEnsession,  be);
		
		if (isValid) {
			utilisateurDAO.modifierUtilisateur(utilisateur);
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
	public void supprimerMonProfil(Utilisateur utilisateur) {
		if (utilisateur != null) {
			utilisateurDAO.supprimerUtilisateur(utilisateur);
		}
		
	}

	@Override
	public boolean isEmailModifierValide(String email, Utilisateur utilisateur, Utilisateur utilisateurEnSession, BusinessException be) {
		Utilisateur Emailutilisateur = utilisateurDAO.utilisateurparEmail(email);
	    if (Emailutilisateur == null || Emailutilisateur.getIdUtilisateur() == utilisateurEnSession.getIdUtilisateur()) {
	    System.out.println("1");
	    	return true; 
	    }be.add("L'email \"" + email + "\" a déjà un compte associé");
	      System.out.println("2");
	    return false;
	    
	}
	
	
	@Override
	public boolean isEmailValide(String email, BusinessException be) {
		if(utilisateurDAO.isEmailValide(email)) {
			return true;
		}be.add("L'email \"" + email + "\" a déjà un compte associé");
		return false;
	}
	
	@Override
	public boolean isPseudoModifierValide(String pseudo, Utilisateur utilisateur, Utilisateur utilisateurEnSession, BusinessException be) {
		Utilisateur Pseudoutilisateur = utilisateurDAO.utilisateurParPseudo(pseudo);
	    if (Pseudoutilisateur == null || Pseudoutilisateur.getIdUtilisateur() == utilisateurEnSession.getIdUtilisateur()) {
	      System.out.println("1");
	    	return true;
	    }be.add("Le pseudo \"" + pseudo + "\" a déjà un compte associé");
	    System.out.println("2");
	    return false;
	}
	
	@Override
	public boolean isPseudoValide(String pseudo, BusinessException be) {
		if(utilisateurDAO.isPseudoValide(pseudo)) {
			return true;
		}be.add("Le pseudo \"" + pseudo + "\" existe déjà");
		return false;
	}

	@Override
	public boolean isCompteExist(String pseudo, BusinessException be) {
		if(utilisateurDAO.isExist(pseudo)) {
			return true;
		}be.add("Identifiant incorrect");
		return false;
	}

	@Override
	public boolean isAdmin(long idUtilisateur, BusinessException be) {
		if (utilisateurDAO.isAdmin(idUtilisateur)) {
			return true;
		}be.add("Action non autorisée, vous n'êtes pas administrateur du site");
		return false;
	}

//	@Override
//	public boolean isMdpValide(String mdp, BusinessException be) {
//		if (this.consulterMdpParPseudo(mdp).equals(mdp)) {
//			return true;
//		}be.add("Mot de passe incorrect");
//		return false;
//	}

	

	


	


	
	
	
	





}
