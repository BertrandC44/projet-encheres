package fr.eni.encheres.dal;

import java.util.List;

import fr.eni.encheres.bo.Utilisateur;

public interface UtilisateurDAO {
	
	public List<Utilisateur> consulterUtilisateurs();
	
	public Utilisateur utilisateurparId(long id);

	public Utilisateur utilisateurparEmail(String email);
	
	public void creerUtilisateur(Utilisateur utilisateur);
	
	public void supprimerUtilisateur(Utilisateur utilisateur);
	
	public void crediter(int credit);
	
	public void debiter(int debit);

}
