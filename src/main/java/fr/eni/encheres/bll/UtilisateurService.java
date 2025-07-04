package fr.eni.encheres.bll;

import java.util.List;

import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.exception.BusinessException;

public interface UtilisateurService {

	List<Utilisateur> consulterUtilisateurs();
	
	Utilisateur consulterUtilisateursParId(long id);
	
	Utilisateur consulterUtilisateurParPseudo(String pseudo) ;
	
	String consulterMdpParPseudo(String pseudo);
	
	int consulterCredit(Utilisateur utilisateur);
	
	void debiter(int debit,Utilisateur utilisateur);
	
	void credit(int credit,Utilisateur utilisateur);
	
	void creerUtilisateur(Utilisateur utilisateur) throws BusinessException;
	
	void modifierUtilisateur(Utilisateur utilisateur) throws BusinessException;
	
	void supprimerUtilisateur(Utilisateur utilisateur);
	
	void supprimerMonProfil(Utilisateur utilisateur);

	boolean isEmailValide(String email, BusinessException be);

	boolean isPseudoValide(String pseudo, BusinessException be);
	
	boolean isAdmin(long idUtilisateur, BusinessException be);

	boolean isCompteExist(String pseudo, BusinessException be);
	
//	boolean isMdpValide (String mdp, BusinessException be);


	
}
