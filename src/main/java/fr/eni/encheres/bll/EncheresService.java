package fr.eni.encheres.bll;

import java.time.LocalDate;
import java.util.List;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;

import fr.eni.encheres.bo.Utilisateur;

public interface EncheresService {
	
	List<Enchere> consulterEncheres();
	
	Enchere consulterEnchereParId(long idArticle);
	
	List<Categorie> consulterCategories();
	
	Categorie consulterCategorieParId(long id);
	
	List<Article> consulterArticles();
	
	Article consulterArticleParId(long id);
	
	Article rechercheParMotCle(String motCle);

	void creerVente(Article article);

	void annulerVente(Article article);
	
	void encherir(long idUtilisateur, long idArticle, int montantEnchere);
	
	int montantMax(long idArticle);
	
	
	

}
