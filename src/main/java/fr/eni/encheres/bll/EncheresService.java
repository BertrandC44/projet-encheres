package fr.eni.encheres.bll;

import java.util.List;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.bo.Retrait;
import fr.eni.encheres.bo.Utilisateur;

public interface EncheresService {
	
	List<Enchere> consulterEncheres();
	
	List<Categorie> consulterCategories();
	
	Categorie consulterCategorieParId(long id);
	
	List<Utilisateur> consulterUtilisateurs();
	
	Utilisateur consulterUtilisateursParId(long id);
	
	Utilisateur crediter(int credit);
	
	Utilisateur debiter(int debit);
	
	List<Article> consulterArticles();
	
	Article consulterArticleParId(long id);
	
	Article rechercheParMotCle(String motCle);
	
	void creerEnchere(Enchere enchere);
	
	void annulerEnchere(Enchere enchere);
	
	void creerUtilisateur(Utilisateur utilisateur);
	
	void supprimerUtilisateur(Utilisateur utilisateur);
	

}
