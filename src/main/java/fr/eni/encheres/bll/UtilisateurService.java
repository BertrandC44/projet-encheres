package fr.eni.encheres.bll;

import java.util.List;

import fr.eni.encheres.bo.Utilisateur;

public interface UtilisateurService {

	List<Utilisateur> consulterUtilisateurs();
	
	Utilisateur consulterUtilisateursParId(long id);
	
	int consulterCredit(Utilisateur utilisateur);
	
	Utilisateur debiter(int debit);
	
	void creerUtilisateur(Utilisateur utilisateur);
	
	void supprimerUtilisateur(Utilisateur utilisateur);
	
}
