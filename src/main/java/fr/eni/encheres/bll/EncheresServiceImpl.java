package fr.eni.encheres.bll;

import java.time.LocalDate;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.bo.Retrait;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dal.ArticleDAO;
import fr.eni.encheres.dal.CategorieDAO;
import fr.eni.encheres.dal.EnchereDAO;
import fr.eni.encheres.dal.RetraitDAO;
import fr.eni.encheres.dal.UtilisateurDAO;
import fr.eni.encheres.exception.BusinessException;


@Service

public class EncheresServiceImpl implements EncheresService{	

	private EnchereDAO enchereDAO;
	private CategorieDAO categorieDAO;
	private ArticleDAO articleDAO;
	private UtilisateurDAO utilisateurDAO;
	private RetraitDAO retraitDAO;
	private LocalDate today = LocalDate.now();

	public EncheresServiceImpl(EnchereDAO enchereDAO, CategorieDAO categorieDAO, ArticleDAO articleDAO,
			UtilisateurDAO utilisateurDAO, RetraitDAO retraitDAO) {
		this.enchereDAO = enchereDAO;
		this.categorieDAO = categorieDAO;
		this.articleDAO = articleDAO;
		this.utilisateurDAO = utilisateurDAO;
		this.retraitDAO = retraitDAO;
	}
	
	 
	/**
	 * méthode pour assigner l'image en fonction de l'id de la catégorie
	 * @param c : catégorie
	 */
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
	
	
	/**
	 *Retourne la liste des enchères
	 */
	@Override
	public List<Enchere> consulterEncheres() {
	    return enchereDAO.consulterEncheres();
	}
	
	/**
	 *retourne la liste des enchères depuis idArticle
	 */
	@Override
	public Enchere consulterEnchereParId(long idArticle) {
	    return enchereDAO.consulterEnchereParId(idArticle);
	}
	
	/**
	 * retourne liste des catégories
	 */
	@Override
	public List<Categorie> consulterCategories() {
	    List<Categorie> categories = categorieDAO.consulterCategories();
	    for (Categorie c : categories) {
	        assignerImageCategorie(c);
	    }
	    return categories;
	}

	/**
	 * retourne liste des catégories depuis idCategorie
	 */
	@Override
	public Categorie consulterCategorieParId(long idCategorie) {
	    return categorieDAO.consulterCategorieParId(idCategorie);
	}

	
	/**
	 *Retourne la liste des articles en vente
	 */
	@Override
	public List<Article> consulterArticles() {
	    List<Article> articles = articleDAO.consulterArticles();
	    for (Article article : articles) {
	        assignerImageCategorie(article.getCategorie());
	    }
	    return articles;
	}

	
	/**
	 *Retourne la liste des articles en vente depuis le pseudo utilisateur
	 */
	@Override
	public List<Article> consulterArticlePseudo() {
	    List<Article> articles = articleDAO.consulterArticlePseudo();
	    for (Article article : articles) {
	        assignerImageCategorie(article.getCategorie());
	    }
	    return articles;
	}
	
	
	/**
	 * Retourne l'article correpsondant à idArticle
	 */
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

	
	/**
	 *créé un article en base de donnée
	 */
	@Override
	public void creerVente(Article article) {
		Categorie categorie= article.getCategorie();
		
		articleDAO.creerVente(article);
		Retrait retrait = article.getRetrait();	
		retrait.setArticle(article);
		categorieDAO.consulterCategorieParId(categorie.getIdCategorie());
		retraitDAO.creer(retrait, article.getIdArticle());
	}
	

	/**
	 *supprimer un article en vente
	 */
	@Override
	public void annulerVente(Article article) {
		// TODO Auto-generated method stub
		
	}

	/**
	 *Débite l'utilisateur qui a enchérit
	 */
	@Override
	public int debiter(int montantEnchere, Utilisateur utilisateur) {
		int solde = utilisateur.getCredit();
		if (solde > montantEnchere) {
			solde -= montantEnchere;
			utilisateur.setCredit(solde);
		}
		return solde;
	}

	/**
	 *valide l'enchère de l'utilisateur s'il n'est pas le vendeur, que l'enchère est ouverte, qu'il enchérit au-dessus,
	 *qu'il a suffisamment de crédit et qu'il n'est pas déjà le meilleur enchérisseur
	 */
	@Override
	public void encherir(int montantEnchere, long idUtilisateur, long idArticle) throws BusinessException{
		BusinessException be = new BusinessException();
		
		boolean isValid = isNotSameEncherisseur(idArticle, idUtilisateur, be);
		isValid &=isNotEnoughCredit(montantEnchere, idUtilisateur, be);
		isValid &=isEnchereOpen(idArticle, be);
		isValid &=isEnchereClosed2(idArticle, be);
		isValid &=isNotSameEncherisseurVendeur(idArticle, idUtilisateur, be);
		isValid &=enchereIsNotEnough(montantEnchere, idArticle, be);

		if (isValid) {
			Utilisateur utilisateurMax = utilisateurDAO.utilisateurparId(idUtilisateur);
			int newCredit = debiter(montantEnchere, utilisateurMax);
			enchereDAO.encherir(montantEnchere, idUtilisateur, idArticle);
			utilisateurDAO.majCredit(newCredit, idUtilisateur);
      
				if (enchereDAO.nbEnchere(idArticle)!=0) {
					Utilisateur utilisateurSecond = utilisateurDAO.utilisateurparId(enchereDAO.idUtilisateurARecrediter(idArticle));
					int credit = utilisateurSecond.getCredit() + enchereDAO.recrediter(idArticle);
					utilisateurDAO.majCredit(credit, utilisateurSecond.getIdUtilisateur());
					}
			} else {
				throw be;
				}

	}
	
	/**
	 * vérifie que l'enchérisseur n'est pas déjà le meilleur enchérisseur sur l'article
	 * @param idArticle
	 * @param idUtilisateur
	 * @param be
	 * @return
	 */
	private boolean isNotSameEncherisseur (long idArticle, long idUtilisateur, BusinessException be) {
		if(this.enchereDAO.idUtilisateurMontantMax(idArticle)==idUtilisateur) {
			be.add("Vous êtes pour le moment le meilleur enchérisseur");
			System.out.println("Vous êtes pour le moment le meilleur enchérisseur");
			return false;
		}
		return true;
	}
	
	/**
	 * vérifie que l'enchérisseur a suffisamment de crédit
	 * @param montantEnchere
	 * @param idUtilisateur
	 * @param be
	 * @return
	 */
	private boolean isNotEnoughCredit (int montantEnchere, long idUtilisateur, BusinessException be) {
		if (montantEnchere>=this.utilisateurDAO.utilisateurparId(idUtilisateur).getCredit()) {
			be.add("Vous n'avez pas assez de crédit pour enchérir !");
			return false;
		}
		return true;
	} 
	 
	/**
	 * vérifie que l'enchère soit ouverte
	 * @param idArticle
	 * @param be
	 * @return
	 */
	private boolean isEnchereOpen (long idArticle, BusinessException be) {
		LocalDate debutEnchereDate = this.articleDAO.consulterArticleParId(idArticle).getDateDebutEncheres();
		if (today.isBefore(debutEnchereDate)) {
			be.add("Cet article n'est pas encore mis en enchère.");
			return false;
		}
		return true;
	}
	
	/**
	 * vérifie que l'enchère ne soit pas terminée
	 * @param idArticle
	 * @param be
	 * @return
	 */
	private boolean isEnchereClosed2 (long idArticle, BusinessException be) {
		LocalDate finEnchereDate = this.articleDAO.consulterArticleParId(idArticle).getDateFinEncheres();
		if (today.isAfter(finEnchereDate)) {
			be.add("Les enchères sur cet article sont terminées.");
			return false;
		}
		return true;
	}
	

	/**
	 *
	 */
	@Override
	public boolean isEnchereClosed (long idArticle) {
		LocalDate finEnchereDate = this.articleDAO.consulterArticleParId(idArticle).getDateFinEncheres();
		if (today.isAfter(finEnchereDate)) {
			return false;
		}
		return true;
	}

	private boolean isNotSameEncherisseurVendeur (long idArticle, long idUtilisateur, BusinessException be) {
		if(this.enchereDAO.idUtilisateurVendeur(idArticle)==idUtilisateur) {
			be.add("Vous ne pouvez pas encherir sur votre article...");
			return false;
		}
		return true;
	}

	
	private boolean enchereIsNotEnough (int montantEnchere, long idArticle, BusinessException be) {
		if(this.enchereDAO.montantEnchereMax(idArticle)>=montantEnchere) {
			be.add("Vous n'avez pas assez enchéri pour cette article");
			return false;
		}
		return true;
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

	@Override
	public List<Article> consulterArticleParIdCategorie(long idCategorie) {
		List<Article> articles = articleDAO.consulterArticleParCategorie(idCategorie);
	    for (Article a : articles) {
	        assignerImageCategorie(a.getCategorie());
	    }
	    return articles;
	
	}

	@Override
	public List<Article> consulterArticleParMotCle(String motCle) {
		List<Article> articles = articleDAO.consulterArticleParMotCle(motCle);
	    for (Article a : articles) {
	        assignerImageCategorie(a.getCategorie());
	    }
	    return articles;
	}

	@Override
	public int nbEnchere(long idArticle) {
		return enchereDAO.nbEnchere(idArticle);
	}

	@Override
	public long idUtilisateurVendeur(long idArticle) {
		return enchereDAO.idUtilisateurVendeur(idArticle);
	}

	@Override
	public void majEtatVente(long idArticle) {
		enchereDAO.majEtatVente(idArticle);
		
	}

}
