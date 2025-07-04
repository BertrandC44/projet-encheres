package fr.eni.encheres.bll;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.dal.ArticleDAO;

import fr.eni.encheres.dal.CategorieDAO;
import fr.eni.encheres.dal.EnchereDAO;
import fr.eni.encheres.dal.UtilisateurDAO;


@Service
public class EncheresServiceImpl implements EncheresService{
	
	private ArticleDAO articleDAO;
	
	

	public EncheresServiceImpl(ArticleDAO articleDAO) {
		super();
		this.articleDAO = articleDAO;
	}

	
	private EnchereDAO enchereDAO;
	private CategorieDAO categorieDAO;
	private ArticleDAO articleDAO;
	private UtilisateurDAO utilisateurDAO;

	public EncheresServiceImpl(EnchereDAO enchereDAO, CategorieDAO categorieDAO, ArticleDAO articleDAO,	UtilisateurDAO utilisateurDAO) {
		this.enchereDAO = enchereDAO;
		this.categorieDAO = categorieDAO;
		this.articleDAO = articleDAO;
		this.utilisateurDAO = utilisateurDAO;
	}

	@Override
	public List<Enchere> consulterEncheres() {
		return enchereDAO.consulterEncheres();
	}
	
	@Override
	public Enchere consulterEnchereParId(long idArticle) {
		return enchereDAO.consulterEnchereParId(idArticle);
	}

	@Override
	public List<Categorie> consulterCategories() {	
		return categorieDAO.consulterCategories();
	}

	@Override
	public Categorie consulterCategorieParId(long idCategorie) {
		return categorieDAO.consulterCategorieParId(idCategorie);
	}


	@Override
	public List<Article> consulterArticles() {
		return articleDAO.consulterArticles();
	}

	@Override
	public List<Article> consulterArticlePseudo() {
		return articleDAO.consulterArticlePseudo();
	}
	
	@Override
	public Article consulterArticleParId(long idArticle) {
		return articleDAO.consulterArticleParId(idArticle);
	}

	@Override
	public Article rechercheParMotCle(String motCle) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void creerVente(Article article) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void annulerVente(Article article) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void encherir(long idUtilisateur, long idArticle, int montantEnchere) {
		enchereDAO.encherir(idUtilisateur, idArticle, montantEnchere);

	}

	@Override
	public int montantMax(long idArticle) {
		// TODO Auto-generated method stub

		return enchereDAO.montantEnchereMax(idArticle);
	}



}
