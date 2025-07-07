package fr.eni.encheres.bll;

import java.util.List;

import org.springframework.dao.DataAccessException;
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
	private UtilisateurDAO utilisateurDAO;
	

	public EncheresServiceImpl(EnchereDAO enchereDAO, CategorieDAO categorieDAO, ArticleDAO articleDAO, UtilisateurDAO utilisateurDAO) {
	    this.enchereDAO = enchereDAO;
	    this.categorieDAO = categorieDAO;
	    this.articleDAO = articleDAO;
	    this.utilisateurDAO = utilisateurDAO;
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
	public int debiter(int montantEnchere, Utilisateur utilisateur) {
		int solde = utilisateur.getCredit();
		if (solde > montantEnchere) {
			solde -= montantEnchere;
			utilisateur.setCredit(solde);
		}
		return solde;
	}

	@Override
	public void encherir(int montantEnchere, long idUtilisateur, long idArticle) throws BusinessException{
		BusinessException be = new BusinessException();
		Utilisateur utilisateur = utilisateurDAO.utilisateurparId(idUtilisateur);
		try {
			if (idUtilisateurMontantMax(idArticle)!=idUtilisateur) {
				if (utilisateur.getCredit()>=montantEnchere) {
					int solde = debiter(montantEnchere, utilisateur);
					enchereDAO.encherir(montantEnchere, idUtilisateur, idArticle);
					utilisateurDAO.majCredit(solde, idUtilisateur);
				} be.add("Vous n'avez pas assez de crédit pour enchérir !");
			}be.add("Vous êtes pour le moment le meilleur enchérisseur");
		} catch (DataAccessException e) {

			throw be;
		}
	}
	
//	@Override
//	public void encherir(int montantEnchere, long idUtilisateur, long idArticle) throws BusinessException {
//	    BusinessException be = new BusinessException();
//	    Utilisateur utilisateur = utilisateurDAO.utilisateurparId(idUtilisateur);
//
//	    if (idUtilisateurMontantMax(idArticle) == idUtilisateur) {
//	        be.add("Vous êtes déjà le meilleur enchérisseur.");
//	    }
//
//	    if (utilisateur.getCredit() < montantEnchere) {
//	        be.add("Vous n'avez pas assez de crédit pour enchérir !");
//	    }
//
//
//	    if (be.hasError()) {
//	        throw be;
//	    }
//
//	    try {
//	        int nouveauSolde = debiter(montantEnchere, utilisateur);
//	        enchereDAO.encherir(montantEnchere, idUtilisateur, idArticle);
//	        utilisateurDAO.majCredit(nouveauSolde, idUtilisateur);
//	    } catch (DataAccessException e) {
//	        // Erreur technique → tu peux logger ici aussi
//	        throw new BusinessException("Erreur lors du traitement. Veuillez recommencer.");
//	    }
//	}


	@Override
	public int montantMax(long idArticle) {
		return enchereDAO.montantEnchereMax(idArticle);
	}

	@Override
	public String utilisateurMontantMax(long idArticle) {
		return enchereDAO.utilisateurMontantMax(idArticle);
	}
	
	@Override
	public long idUtilisateurMontantMax(long idArticle) {
		return enchereDAO.idUtilisateurMontantMax(idArticle);
	}

	@Override
	public String categorieArticle(long idArticle) {
		return enchereDAO.categorieArticle(idArticle);
	}

}
