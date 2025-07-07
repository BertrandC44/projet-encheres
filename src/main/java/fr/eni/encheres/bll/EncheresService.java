package fr.eni.encheres.bll;

import java.util.List;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;

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
	
	void encherir(int montantEnchere, long idUtilisateur, long idArticle);
	
	int montantMax(long idArticle);
	
	String utilisateurMontantMax(long idArticle);
	
	String categorieArticle(long idArticle);
	
	List<Article> consulterArticlePseudo();
	
	List<Article> consulterArticleEncheresEnCours(long idUtilisateur);
	
	List<Article> consulterArticleMesEncheresEnCours(long idUtilisateur);
	
	List<Article> consulterArticleMesEncheresRemportees(long idUtilisateur);
	
	List<Article> consulterArticleMesVentesEnCours(long idUtilisateur);
	
	List<Article> consulterArticleMesVentesFutures(long idUtilisateur);
	
	List<Article> consulterArticleMesVentesTerminees(long idUtilisateur);
	
	
	
	

}
