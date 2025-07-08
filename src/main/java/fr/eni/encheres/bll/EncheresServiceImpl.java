package fr.eni.encheres.bll;

import java.time.LocalDate;
import java.util.List;


import org.springframework.dao.DataAccessException;
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
		boolean isValid = isNotSameEncherisseur(idArticle, idUtilisateur, be);
		isValid &=isNotEnoughCredit(montantEnchere, idUtilisateur, be);
		isValid &=isEnchereOpen(idArticle, be);
		isValid &=isEnchereClose(idArticle, be);
		if (isValid) {
			Utilisateur utilisateur = utilisateurDAO.utilisateurparId(idUtilisateur);
			int solde = debiter(montantEnchere, utilisateur);
			enchereDAO.encherir(montantEnchere, idUtilisateur, idArticle);
			utilisateurDAO.majCredit(solde, idUtilisateur);
		} else {
			throw be;
		}
	}
	
	private boolean isNotSameEncherisseur (long idArticle, long idUtilisateur, BusinessException be) {
		if(this.enchereDAO.idUtilisateurMontantMax(idArticle)==idUtilisateur) {
			be.add("Vous êtes pour le moment le meilleur enchérisseur");
			return false;
		}
		return true;
	}
	
	private boolean isNotEnoughCredit (int montantEnchere, long idUtilisateur, BusinessException be) {
		if (this.utilisateurDAO.utilisateurparId(idUtilisateur).getCredit()>=montantEnchere) {
			be.add("Vous n'avez pas assez de crédit pour enchérir !");
			return false;
		}
		return true;
	} 
	
	private boolean isEnchereOpen (long idArticle, BusinessException be) {
		LocalDate today = LocalDate.now();
		LocalDate debutEnchereDate = this.articleDAO.consulterArticleParId(idArticle).getDateFinEncheres();
		if (today.isAfter(debutEnchereDate)) {
			be.add("Cet article n'est pas encore en mis en enchère.");
			return false;
		}
		return true;
	}
	
	private boolean isEnchereClose (long idArticle, BusinessException be) {
		LocalDate today = LocalDate.now();
		LocalDate finEnchereDate = this.articleDAO.consulterArticleParId(idArticle).getDateFinEncheres();
		if (today.isBefore(finEnchereDate)) {
			be.add("Les enchères sur cet article sont terminées.");
			return false;
		}
		return true;
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
