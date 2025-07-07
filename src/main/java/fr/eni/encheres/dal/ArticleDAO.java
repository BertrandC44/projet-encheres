package fr.eni.encheres.dal;

import java.util.List;

import fr.eni.encheres.bo.Article;

public interface ArticleDAO {

	List<Article> consulterArticles();
	
	Article consulterArticleParId(long id);
	
	void creerVente(Article article);
	
	void annulerVente(Article article);

	List<Article> consulterArticlePseudo();

}
