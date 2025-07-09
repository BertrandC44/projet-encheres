package fr.eni.encheres.bll;

import java.util.List;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;


import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.exception.BusinessException;


public interface EncheresService {
	
	List<Enchere> consulterEncheres();
	
	Enchere consulterEnchereParId(long idArticle);
	
	List<Categorie> consulterCategories();
	
	Categorie consulterCategorieParId(long id);
	
	List<Article> consulterArticles();
	
	Article consulterArticleParId(long id);

	void creerVente(Article article);

	void annulerVente(Article article);
	
	int debiter(int montantEnchere, Utilisateur utilisateur);
	
	void encherir(int montantEnchere, long idUtilisateur, long idArticle ) throws BusinessException;
<<<<<<< HEAD
	
=======

>>>>>>> bb94891f72ee3b8f4df5e682aaaa50292bc09f61
	int montantMax(long idArticle);
	
	long idUtilisateurMontantMax(long idArticle);
	
	String utilisateurMontantMax(long idArticle);
	
	String categorieArticle(long idArticle);
	
	List<Article> consulterArticlePseudo();
	
	List<Article> consulterArticleEncheresEnCours(long idUtilisateur);
	
	List<Article> consulterArticleMesEncheresEnCours(long idUtilisateur);
	
	List<Article> consulterArticleMesEncheresRemportees(long idUtilisateur);
	
	List<Article> consulterArticleMesVentesEnCours(long idUtilisateur);
	
	List<Article> consulterArticleMesVentesFutures(long idUtilisateur);
	
	List<Article> consulterArticleMesVentesTerminees(long idUtilisateur);
	
	List<Article> consulterArticleParIdCategorie(long idCategorie);
	
	List<Article> consulterArticleParMotCle(String motCle);
<<<<<<< HEAD
	
	public int nbEnchere(long idArticle);
	
	long idUtilisateurVendeur(long idArticle);
	
	boolean isEnchereClosed (long idArticle);
	
	
	
	
=======

>>>>>>> bb94891f72ee3b8f4df5e682aaaa50292bc09f61

}
