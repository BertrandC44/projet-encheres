package fr.eni.encheres.bll;

import java.util.List;

import fr.eni.encheres.bo.Utilisateur;

public interface UtilisateurService {

List<Utilisateur> consulterUtilisateurs();
	
	Utilisateur consulterUtilisateursParId(long id);
	
	Utilisateur crediter(int credit);
	
	Utilisateur debiter(int debit);
	
	void creerUtilisateur(Utilisateur utilisateur);
	
	void supprimerUtilisateur(Utilisateur utilisateur);
	
}
