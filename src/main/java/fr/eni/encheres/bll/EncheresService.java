package fr.eni.encheres.bll;

import java.util.List;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;

import fr.eni.encheres.bo.Utilisateur;

public interface EncheresService {
	
	List<Enchere> consulterEncheres();
	
	List<Categorie> consulterCategories();
	
	Categorie consulterCategorieParId(long id);
	
	List<Article> consulterArticles();
	
	Article consulterArticleParId(long id);
	
	Article rechercheParMotCle(String motCle);

	void creerVente(Article article);

	void annulerVente(Article article);
	
	void encherir(long idArticle,int credit);
	
	

}
