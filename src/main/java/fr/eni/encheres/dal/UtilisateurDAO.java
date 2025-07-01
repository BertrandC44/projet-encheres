package fr.eni.encheres.dal;

import java.util.List;

import fr.eni.encheres.bo.Utilisateur;

public interface UtilisateurDAO {
	
	public List<Utilisateur> findAll();
	
	public Utilisateur findById(long id);
	
	public void createUtilisateur(Utilisateur utilisateur);
	
	public void supprimerUtilisateur(Utilisateur utilisateur);
	
	public void crediter(int credit);
	
	public void debiter(int debit);

}
