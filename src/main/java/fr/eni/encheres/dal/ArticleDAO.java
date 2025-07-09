package fr.eni.encheres.dal;

import java.util.List;

import fr.eni.encheres.bo.Article;

public interface ArticleDAO {

	List<Article> consulterArticles();
	
	Article consulterArticleParId(long id);
	
	void creerVente(Article article);
	
	void annulerVente(Article article);

	List<Article> consulterArticlePseudo();
	
	List<Article> consulterArticleEncheresEnCours(long idUtilisateur);
	
	List<Article> consulterArticleMesEncheresEnCours(long idUtilisateur);
	
	List<Article> consulterArticleMesEncheresRemportees(long idUtilisateur);
	
	List<Article> consulterArticleMesVentesEnCours(long idUtilisateur);
	
	List<Article> consulterArticleMesVentesFutures(long idUtilisateur);
	
	List<Article> consulterArticleMesVentesTerminees(long idUtilisateur);
	
	List<Article> consulterArticleParCategorie(long idCategorie);
	
	List<Article> consulterArticleParMotCle(String motCle);

}
