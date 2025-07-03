package fr.eni.encheres.dal;

import java.util.List;

import fr.eni.encheres.bo.Utilisateur;

public interface UtilisateurDAO {
	
	List<Utilisateur> consulterUtilisateurs();
	
	Utilisateur utilisateurParPseudo(String pseudo);
	
	Utilisateur utilisateurparId(long id);

	Utilisateur utilisateurparEmail(String email);
	
	void creerUtilisateur(Utilisateur utilisateur);
	
	void supprimerUtilisateur(Utilisateur utilisateur);
	
	int consulterCredit(Utilisateur utilisateur);
	
	void majCredit(int credit);
	
	boolean isEmailValide(String email);
	
	boolean isPseudoValide(String pseudo);
	
	boolean isAdmin(long idUtilisateur);

	boolean isExist(String pseudo);




}
