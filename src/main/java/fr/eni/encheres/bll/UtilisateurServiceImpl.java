package fr.eni.encheres.bll;

import java.util.List;

import org.springframework.dao.DataAccessException;
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
		
		boolean isValid = isPseudoValide(utilisateur.getPseudo(), be);
		isValid &= isEmailValide(utilisateur.getEmail(), be);
		isValid &= isConfMdpValide(utilisateur, be);
		
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
	    	return true; 
	    }be.add("L'email est déjà utilisé");
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
	    	return true;
	    }be.add("Le pseudo est déjà utilisé");
	    return false;
	}
	
	@Override
	public boolean isPseudoValide(String pseudo, BusinessException be) {
		if(!utilisateurDAO.isPseudoValide(pseudo)) {
			be.add("Le pseudo \"" + pseudo + "\" existe déjà");
			return false;
		}
		return true;
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
	
	@Override
	public boolean isConfMdpValide(Utilisateur utilisateur, BusinessException be) {
		if (!utilisateur.getConfMdp().equals(utilisateur.getMotDePasse())) {
			be.add("La confirmation est différente du mot de passe saisi");
            return false;
        }
		return true;
	}

//	@Override
//	public boolean isMdpValide(String mdp, BusinessException be) {
//		if (this.consulterMdpParPseudo(mdp).equals(mdp)) {
//			return true;
//		}be.add("Mot de passe incorrect");
//		return false;
//	}

	

	


	


	
	
	
	





}
