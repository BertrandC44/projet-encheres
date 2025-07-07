package fr.eni.encheres.bll;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

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

 
	private EnchereDAO enchereDAO;
	private CategorieDAO categorieDAO;
	private ArticleDAO articleDAO;

	

	public EncheresServiceImpl(EnchereDAO enchereDAO, CategorieDAO categorieDAO, ArticleDAO articleDAO) {
	    this.enchereDAO = enchereDAO;
	    this.categorieDAO = categorieDAO;
	    this.articleDAO = articleDAO;

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
	    Article article = articleDAO.consulterArticleParId(idArticle);
	    assignerImageCategorie(article.getCategorie());
	    return article;
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

	@Override
	public List<Article> consulterArticleEncheresEnCours(long idUtilisateur) {
		List<Article> articles = articleDAO.consulterArticleEncheresEnCours(idUtilisateur);
	    for (Article a : articles) {
	        assignerImageCategorie(a.getCategorie());
	    }
	    return articles;
	}

	@Override
	public List<Article> consulterArticleMesEncheresEnCours(long idUtilisateur) {
		List<Article> articles = articleDAO.consulterArticleMesEncheresEnCours(idUtilisateur);
	    for (Article a : articles) {
	        assignerImageCategorie(a.getCategorie());
	    }
	    return articles;
	}

	@Override
	public List<Article> consulterArticleMesEncheresRemportees(long idUtilisateur) {
		List<Article> articles = articleDAO.consulterArticleMesEncheresRemportees(idUtilisateur);
	    for (Article a : articles) {
	        assignerImageCategorie(a.getCategorie());
	    }
	    return articles;
	}

	@Override
	public List<Article> consulterArticleMesVentesEnCours(long idUtilisateur) {
		List<Article> articles = articleDAO.consulterArticleMesVentesEnCours(idUtilisateur);
	    for (Article a : articles) {
	        assignerImageCategorie(a.getCategorie());
	    }
	    return articles;
	}

	@Override
	public List<Article> consulterArticleMesVentesFutures(long idUtilisateur) {
		List<Article> articles = articleDAO.consulterArticleMesVentesFutures(idUtilisateur);
	    for (Article a : articles) {
	        assignerImageCategorie(a.getCategorie());
	    }
	    return articles;
	}

	@Override
	public List<Article> consulterArticleMesVentesTerminees(long idUtilisateur) {
		List<Article> articles = articleDAO.consulterArticleMesVentesTerminees(idUtilisateur);
	    for (Article a : articles) {
	        assignerImageCategorie(a.getCategorie());
	    }
	    return articles;
	}

}
