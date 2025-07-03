package fr.eni.encheres.bll;

import java.util.List;

import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.exception.BusinessException;

public interface UtilisateurService {

	List<Utilisateur> consulterUtilisateurs();
	
	Utilisateur consulterUtilisateursParId(long id);
	
	public Utilisateur consulterUtilisateurParPseudo(String pseudo) ;
	
	int consulterCredit(Utilisateur utilisateur);
	
	void debiter(int debit,Utilisateur utilisateur);
	
	void credit(int credit,Utilisateur utilisateur);
	
	void creerUtilisateur(Utilisateur utilisateur) throws BusinessException;
	
	void supprimerUtilisateur(Utilisateur utilisateur);

	boolean isEmailValide(String email, BusinessException be);
	
	boolean isCompteExist(long idUtilisateur, BusinessException be);
	
	boolean isAdmin(long idUtilisateur, BusinessException be);
	
}
