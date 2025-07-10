package fr.eni.encheres.bll;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dal.UtilisateurDAO;
import fr.eni.encheres.exception.BusinessException;

/**
 * Implémentation du service de gestion des utilisateurs.
 * Fournit les opérations métiers liées à la création, modification, suppression,
 * consultation et validation des comptes utilisateurs.
 */
@Service
public class UtilisateurServiceImpl implements UtilisateurService{
	
	private UtilisateurDAO utilisateurDAO;
	
	/**
     * Constructeur du service avec injection de DAO utilisateur.
     * 
     * @param utilisateurDAO DAO pour l'accès aux données utilisateur.
     */
	public UtilisateurServiceImpl(UtilisateurDAO utilisateurDAO) {
		this.utilisateurDAO = utilisateurDAO;
	}

	/**
     * Retourne la liste de tous les utilisateurs.
     * 
     * @return Liste des utilisateurs.
     */
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

	/**
     * Retourne un utilisateur selon son ID.
     * 
     * @param id Identifiant de l'utilisateur.
     * @return L'utilisateur correspondant.
     */
	@Override
	public Utilisateur consulterUtilisateursParId(long id) {
		Utilisateur utilisateur = utilisateurDAO.utilisateurparId(id);
		return utilisateur;
	}
	
	/**
     * Retourne un utilisateur selon son pseudonyme.
     * 
     * @param pseudo Pseudonyme de l'utilisateur.
     * @return Utilisateur correspondant.
     */
	@Override
	public Utilisateur consulterUtilisateurParPseudo(String pseudo) {
		return this.utilisateurDAO.utilisateurParPseudo(pseudo);
	}
	
	/**
     * Récupère le mot de passe associé à un pseudo.
     * 
     * @param pseudo Le pseudonyme concerné.
     * @return Mot de passe en clair (à sécuriser !).
     */
	@Override
	public String consulterMdpParPseudo(String pseudo) {
		return this.utilisateurDAO.consulterMdp(pseudo);
	}

	/**
     * Retourne le crédit disponible d'un utilisateur.
     * 
     * @param utilisateur Utilisateur concerné.
     * @return Montant du crédit.
     */
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
	
	/**
     * Ajoute du crédit à un utilisateur.
     * 
     * @param credit Montant à créditer.
     * @param utilisateur Utilisateur concerné.
     */
	@Override
	public void crediter(int credit, Utilisateur utilisateur) {
		int solde = utilisateur.getCredit();
		solde += credit;
		utilisateur.setCredit(solde);
	}

	 /**
     * Crée un nouveau compte utilisateur en validant l'unicité du pseudo et de l'email.
     * 
     * @param utilisateur Utilisateur à créer.
     * @throws BusinessException si validation échoue.
     */
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
	
	
	 /**
     * Modifie un utilisateur en validant les champs modifiés.
     * 
     * @param utilisateur Utilisateur modifié.
     * @param utilisateurEnsession Utilisateur en session (pour validation).
     * @throws BusinessException en cas de conflit.
     */
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

	/**
     * Supprime un utilisateur si ce dernier est administrateur.
     * 
     * @param utilisateur Utilisateur à supprimer.
     */
	@Override
	public void supprimerUtilisateur(Utilisateur utilisateur) {
		BusinessException be = new BusinessException();
		
		if(isAdmin(utilisateur.getIdUtilisateur(), be)) {
			utilisateurDAO.supprimerUtilisateur(utilisateur);
		}
		
		
	}
	
	/**
     * Permet à un utilisateur de supprimer son propre profil.
     * 
     * @param utilisateur Utilisateur à supprimer.
     */
	@Override
	public void supprimerMonProfil(Utilisateur utilisateur) {
		if (utilisateur != null) {
			utilisateurDAO.supprimerUtilisateur(utilisateur);
		}
		
	}

	/**
     * Valide l'email modifié en vérifiant son unicité.
     * 
     * @param email Email proposé.
     * @param utilisateur Utilisateur cible.
     * @param utilisateurEnSession Utilisateur connecté.
     * @param be Exception à enrichir.
     * @return true si email valide, false sinon.
     */
	@Override
	public boolean isEmailModifierValide(String email, Utilisateur utilisateur, Utilisateur utilisateurEnSession, BusinessException be) {
		Utilisateur Emailutilisateur = utilisateurDAO.utilisateurparEmail(email);
	    if (Emailutilisateur == null || Emailutilisateur.getIdUtilisateur() == utilisateurEnSession.getIdUtilisateur()) {
	    	return true; 
	    }be.add("L'email est déjà utilisé");
	    return false;
	    
	}
	
	/**
     * Vérifie si un email est déjà utilisé.
     * 
     * @param email Email à vérifier.
     * @param be Exception métier.
     * @return true si email libre, false sinon.
     */
	@Override
	public boolean isEmailValide(String email, BusinessException be) {
		if(utilisateurDAO.isEmailValide(email)) {
			return true;
		}be.add("L'email \"" + email + "\" a déjà un compte associé");
		return false;
	}
	
	/**
     * Valide le pseudonyme modifié.
     * 
     * @param pseudo Pseudo proposé.
     * @param utilisateur Utilisateur cible.
     * @param utilisateurEnSession Utilisateur connecté.
     * @param be Exception à enrichir.
     * @return true si pseudo libre, false sinon.
     */
	@Override
	public boolean isPseudoModifierValide(String pseudo, Utilisateur utilisateur, Utilisateur utilisateurEnSession, BusinessException be) {
		Utilisateur Pseudoutilisateur = utilisateurDAO.utilisateurParPseudo(pseudo);
	    if (Pseudoutilisateur == null || Pseudoutilisateur.getIdUtilisateur() == utilisateurEnSession.getIdUtilisateur()) {
	    	return true;
	    }be.add("Le pseudo est déjà utilisé");
	    return false;
	}
	
	/**
     * Vérifie si un pseudo est disponible.
     * 
     * @param pseudo Pseudo à valider.
     * @param be Exception à enrichir.
     * @return true si pseudo libre, false sinon.
     */
	@Override
	public boolean isPseudoValide(String pseudo, BusinessException be) {
		if(utilisateurDAO.isPseudoValide(pseudo)) {
			return true;
		}be.add("Le pseudo \"" + pseudo + "\" existe déjà");
		return false;
	}

	/**
     * Vérifie si le compte existe pour un pseudo donné.
     * 
     * @param pseudo Pseudo à tester.
     * @param be Exception métier.
     * @return true si compte existant, false sinon.
     */
	@Override
	public boolean isCompteExist(String pseudo, BusinessException be) {
		if(utilisateurDAO.isExist(pseudo)) {
			return true;
		}be.add("Identifiant incorrect");
		return false;
	}

	/**
     * Vérifie si l'utilisateur est administrateur du site.
     * 
     * @param idUtilisateur ID de l'utilisateur.
     * @param be Exception métier.
     * @return true si admin, false sinon.
     */
	@Override
	public boolean isAdmin(long idUtilisateur, BusinessException be) {
		if (utilisateurDAO.isAdmin(idUtilisateur)) {
			return true;
		}be.add("Action non autorisée, vous n'êtes pas administrateur du site");
		return false;
	}


	

	


	


	
	
	
	





}
