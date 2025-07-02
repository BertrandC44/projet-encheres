package fr.eni.encheres.dal;

import java.util.List;

import fr.eni.encheres.bo.Utilisateur;

public interface UtilisateurDAO {
	
	public List<Utilisateur> consulterUtilisateurs();
	
	public Utilisateur utilisateurParId(long id);
	
	public void creerUtilisateur(Utilisateur utilisateur);
	
	public void supprimerUtilisateur(Utilisateur utilisateur);
	
	public int consulterCredit(Utilisateur utilisateur);
	
	public void majCredit(int credit);

}
