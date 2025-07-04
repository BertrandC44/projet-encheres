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

@Service
public class EncheresServiceImpl implements EncheresService{
	
	private ArticleDAO articleDAO;
	private CategorieDAO categorieDAO;
	
	

	public EncheresServiceImpl(ArticleDAO articleDAO, CategorieDAO categorieDAO) {
		
		this.articleDAO = articleDAO;
		this.categorieDAO = categorieDAO;
	}

	//méthode globale pour les images
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
		// TODO Auto-generated method stub
		return null;
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
	Categorie categorie = categorieDAO.consulterCategorieParId(idCategorie);
	return categorie;
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
	public void encherir(long idArticle, int credit) {
		// TODO Auto-generated method stub
		
}

}
