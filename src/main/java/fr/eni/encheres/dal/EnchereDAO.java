package fr.eni.encheres.dal;

import java.util.List;

import fr.eni.encheres.bo.Enchere;


public interface EnchereDAO {
	
	List<Enchere> consulterEncheres();	
	
	Enchere consulterEnchereParId(long idArticle);
	
	String categorieArticle(long idArticle);
		
	int montantEnchereMax(long idArticle);
	
	String utilisateurMontantMax(long idArticle);
	
	long idUtilisateurMontantMax(long idArticle);
	
	long idUtilisateurARecrediter(long idArticle);
	
	void encherir(int montantEnchere, long idUtilisateur, long idArticle);
	
	int recrediter(long idArticle);
		
	int nbEnchere(long idArticle);
	
	long idUtilisateurVendeur(long idArticle);
	

	

}
