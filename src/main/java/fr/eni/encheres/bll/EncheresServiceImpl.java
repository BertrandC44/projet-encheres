package fr.eni.encheres.bll;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.dal.ArticleDAO;

@Service
public class EncheresServiceImpl implements EncheresService{
	
	private ArticleDAO articleDAO;
	
	

	public EncheresServiceImpl(ArticleDAO articleDAO) {
		super();
		this.articleDAO = articleDAO;
	}

	@Override
	public List<Enchere> consulterEncheres() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Categorie> consulterCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Categorie consulterCategorieParId(long idCategorie) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
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
