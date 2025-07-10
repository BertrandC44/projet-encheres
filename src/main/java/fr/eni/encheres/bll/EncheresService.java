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

	public int nbEnchere(long idArticle);
	
	long idUtilisateurVendeur(long idArticle);
	
	void majEtatVente(long idArticle);



}
