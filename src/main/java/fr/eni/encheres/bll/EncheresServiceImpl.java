package fr.eni.encheres.bll;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import fr.eni.encheres.Application;
import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dal.ArticleDAO;
import fr.eni.encheres.dal.CategorieDAO;

import fr.eni.encheres.dal.CategorieDAO;
import fr.eni.encheres.dal.EnchereDAO;
import fr.eni.encheres.dal.UtilisateurDAO;
import fr.eni.encheres.exception.BusinessException;


@Service
public class EncheresServiceImpl implements EncheresService{

    private final Application application;

    private final UtilisateurServiceImpl utilisateurServiceImpl;
	
	private EnchereDAO enchereDAO;
	private CategorieDAO categorieDAO;
	private ArticleDAO articleDAO;
	private UtilisateurDAO utilisateurDAO;
	

	public EncheresServiceImpl(EnchereDAO enchereDAO, CategorieDAO categorieDAO, ArticleDAO articleDAO, UtilisateurDAO utilisateurDAO, UtilisateurServiceImpl utilisateurServiceImpl, Application application) {
	    this.enchereDAO = enchereDAO;
	    this.categorieDAO = categorieDAO;
	    this.articleDAO = articleDAO;
	    this.utilisateurDAO = utilisateurDAO;
	    this.utilisateurServiceImpl = utilisateurServiceImpl;
	    this.application = application;
	}

	// méthode pour assigner l'image en fonction de l'id de la catégorie
	private void assignerImageCategorie(Categorie c) {
	    if (c != null) {
	        switch ((int) c.getIdCategorie()) {
	            case 1 -> c.setImage("clavier.jpg");
	            case 2 -> c.setImage("chaise-en-bois.jpg");
	            case 3 -> c.setImage("tondeuse.png");
	            case 4 -> c.setImage("tennis.png");
	            default -> c.setImage("default.jpg");
	        }
	    }
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
	    List<Categorie> categories = categorieDAO.consulterCategories();
	    for (Categorie c : categories) {
	        assignerImageCategorie(c);
	    }
	    return categories;
	}

	@Override
	public Categorie consulterCategorieParId(long idCategorie) {
	    return categorieDAO.consulterCategorieParId(idCategorie);
	}

	@Override
	public List<Article> consulterArticles() {
	    List<Article> articles = articleDAO.consulterArticles();
	    for (Article article : articles) {
	        assignerImageCategorie(article.getCategorie());
	    }
	    return articles;
	}

	@Override
	public List<Article> consulterArticlePseudo() {
	    List<Article> articles = articleDAO.consulterArticlePseudo();
	    for (Article article : articles) {
	        assignerImageCategorie(article.getCategorie());
	    }
	    return articles;
	}
	
	@Override
	public Article consulterArticleParId(long idArticle) {
		//pour éviter de renvoyer une erruer s'il n'y a pas d'id 
	
		try {
	    Article article = articleDAO.consulterArticleParId(idArticle);
	    if(article != null) {
	    assignerImageCategorie(article.getCategorie());
	    }
	    return article;
	}catch (EmptyResultDataAccessException e) {
		return null;
	}
}

	@Override
	public Article rechercheParMotCle(String motCle) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void creerVente(Article article) {
		Categorie categorie= article.getCategorie();
		articleDAO.creerVente(article);
		
		categorieDAO.consulterCategorieParId(categorie.getIdCategorie());
		
	
		
	}

	

	@Override
	public void annulerVente(Article article) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void encherir(int montantEnchere, long idUtilisateur, long idArticle ) {
		BusinessException be = new BusinessException();

		enchereDAO.encherir(idUtilisateur, idArticle, montantEnchere);
	}

	@Override
	public int montantMax(long idArticle) {
		return enchereDAO.montantEnchereMax(idArticle);
	}

	@Override
	public String utilisateurMontantMax(long idArticle) {
		return enchereDAO.utilisateurMontantMax(idArticle);
	}

	@Override
	public String categorieArticle(long idArticle) {
		
		return enchereDAO.categorieArticle(idArticle);
	}

}
