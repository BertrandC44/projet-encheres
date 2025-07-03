package fr.eni.encheres.dal;

import java.util.List;

import fr.eni.encheres.bo.Utilisateur;

public interface UtilisateurDAO {
	
	public List<Utilisateur> consulterUtilisateurs();
	
	public Utilisateur utilisateurParPseudo(String pseudo);
	
	public Utilisateur utilisateurparId(long id);

	public Utilisateur utilisateurparEmail(String email);
	
	public void creerUtilisateur(Utilisateur utilisateur);
	
	public void modifierUtilisateur(Utilisateur utilisateur);
	
	public void supprimerUtilisateur(Utilisateur utilisateur);
	
	public void supprimerMonProfil(Utilisateur utilisateur);
	
	public int consulterCredit(Utilisateur utilisateur);
	
	public void majCredit(int credit);
	
	public boolean isEmailValide(String email);
	
	boolean isExist(long idUtilisateur);
	
	boolean isAdmin(long idUtilisateur);


}
