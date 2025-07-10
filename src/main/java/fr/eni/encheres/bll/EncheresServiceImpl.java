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

/**
 * Implémentation du service pour la gestion des enchères.
 * Fournit les opérations liées aux articles, enchères, utilisateurs, catégories et retraits.
 * Valide également les règles métier lors des enchères.
 */
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
     * Retourne la liste de toutes les enchères disponibles.
     * 
     * @return Liste des enchères.
     */

	@Override
	public List<Enchere> consulterEncheres() {
	    return enchereDAO.consulterEncheres();
	}
	


    /**
     * Retourne l'enchère liée à un article donné.
     * 
     * @param idArticle L'identifiant de l'article.
     * @return L'enchère correspondante ou null si non trouvée.
     */
	@Override
	public Enchere consulterEnchereParId(long idArticle) {
	    return enchereDAO.consulterEnchereParId(idArticle);
	}
	

    /**
     * Retourne la liste de toutes les catégories avec leurs images associées.
     * 
     * @return Liste des catégories.
     */
	@Override
	public List<Categorie> consulterCategories() {
	    List<Categorie> categories = categorieDAO.consulterCategories();
	    return categories;
	}


    /**
     * Retourne une catégorie par son identifiant.
     * 
     * @param idCategorie Identifiant de la catégorie.
     * @return La catégorie correspondante.
     */
	@Override
	public Categorie consulterCategorieParId(long idCategorie) {
	    return categorieDAO.consulterCategorieParId(idCategorie);
	}


    /**
     * Retourne la liste de tous les articles disponibles.
     * 
     * @return Liste des articles.
     */
	@Override
	public List<Article> consulterArticles() {
	    List<Article> articles = articleDAO.consulterArticles();
	    return articles;
	}


    /**
     * Retourne la liste des articles avec leur pseudonyme vendeur.
     * 
     * @return Liste des articles.
     */
	@Override
	public List<Article> consulterArticlePseudo() {
	    List<Article> articles = articleDAO.consulterArticlePseudo();
	    for (Article article : articles) {
	    	 article.getImage();
	    }
	    return articles;
	}
	
    /**
     * Retourne un article par son identifiant.
     * 
     * @param idArticle Identifiant de l'article.
     * @return Article correspondant ou null si non trouvé.
     */
	@Override
	public Article consulterArticleParId(long idArticle) {
		//pour éviter de renvoyer une erreur s'il n'y a pas d'id 
	
		try {
	    Article article = articleDAO.consulterArticleParId(idArticle);
	    if(article != null) {
	    article.getImage();
	    }
	    return article;
	}catch (EmptyResultDataAccessException e) {
		return null;
	}
	}


    /**
     * Crée une nouvelle vente d'article et enregistre les informations de retrait.
     * 
     * @param article L'article à vendre.
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
     * Annule une vente. (Méthode à implémenter)
     * 
     * @param article L'article dont la vente doit être annulée.
     */
	@Override
	public void annulerVente(Article article) {
		// TODO Auto-generated method stub
		
	}


   /**
     * Débite le crédit d'un utilisateur après une enchère.
     * 
     * @param montantEnchere Montant de l'enchère.
     * @param utilisateur Utilisateur à débiter.
     * @return Nouveau solde.
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
     * 
     * @param montantEnchere Montant proposé.
     * @param idUtilisateur ID de l'enchérisseur.
     * @param idArticle ID de l'article.
     * @throws BusinessException si une règle métier est violée.
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
     * Vérifie si l'enchère est terminée pour un article donné.
     * 
     * @param idArticle ID de l'article.
     * @return true si encore ouverte, false sinon.
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

     * Vérifie si l'utilisateur a suffisamment de crédit pour enchérir.
     * 
     * @param montantEnchere Montant proposé.
     * @param idUtilisateur ID de l'enchérisseur.
     * @param be Exception métier à enrichir.
     * @return true si le crédit est suffisant, false sinon.
     */
	private boolean isNotEnoughCredit (int montantEnchere, long idUtilisateur, BusinessException be) {
		if (montantEnchere>=this.utilisateurDAO.utilisateurparId(idUtilisateur).getCredit()) {
			be.add("Vous n'avez pas assez de crédit pour enchérir !");
			return false;
		}
		return true;
	} 
	 

	/**

     * Vérifie si l'enchère est ouverte pour l'article donné.
     * 
     * @param idArticle ID de l'article.
     * @param be Exception métier à enrichir.
     * @return true si l'enchère est ouverte, false sinon.
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
     * Vérifie si l'enchère est terminée pour l'article donné.
     * 
     * @param idArticle ID de l'article.
     * @param be Exception métier à enrichir.
     * @return true si l'enchère n'est pas terminée, false sinon.
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
     * Vérifie de manière simplifiée si l'enchère est encore active.
     * 
     * @param idArticle ID de l'article.
     * @return true si l'enchère est encore en cours, false sinon.
     */



	@Override
	public boolean isEnchereClosed (long idArticle) {
		LocalDate finEnchereDate = this.articleDAO.consulterArticleParId(idArticle).getDateFinEncheres();
		if (today.isAfter(finEnchereDate)) {
			return false;
		}
		return true;
	}


	/**
     * Vérifie que l'utilisateur ne tente pas d'enchérir sur son propre article.
     * 
     * @param idArticle ID de l'article.
     * @param idUtilisateur ID de l'enchérisseur.
     * @param be Exception métier à enrichir.
     * @return true si l'utilisateur est différent du vendeur, false sinon.
     */

	private boolean isNotSameEncherisseurVendeur (long idArticle, long idUtilisateur, BusinessException be) {
		if(this.enchereDAO.idUtilisateurVendeur(idArticle)==idUtilisateur) {
			be.add("Vous ne pouvez pas encherir sur votre article...");
			return false;
		}
		return true;
	}

	 /**
     * Vérifie que le montant proposé est supérieur au montant maximal actuel.
     * 
     * @param montantEnchere Montant proposé.
     * @param idArticle ID de l'article.
     * @param be Exception métier à enrichir.
     * @return true si le montant est suffisant, false sinon.
     */
	private boolean enchereIsNotEnough (int montantEnchere, long idArticle, BusinessException be) {
		if(this.enchereDAO.montantEnchereMax(idArticle)>=montantEnchere) {
			be.add("Vous n'avez pas assez enchéri pour cette article");
			return false;
		}
		return true;
	}

	/**
     * Récupère le montant maximal d'enchère pour un article.
     * 
     * @param idArticle ID de l'article.
     * @return Montant de l'enchère maximale.
     */
	@Override
	public int montantMax(long idArticle) {
		return enchereDAO.montantEnchereMax(idArticle);
	}
	
	/**
     * Récupère le pseudonyme de l'utilisateur ayant placé l'enchère maximale.
     * 
     * @param idArticle ID de l'article.
     * @return Pseudo de l'utilisateur.
     */
	@Override
	public String utilisateurMontantMax(long idArticle) {
		return enchereDAO.utilisateurMontantMax(idArticle);
	}
	
	 /**
     * Récupère l'ID de l'utilisateur ayant placé l'enchère maximale.
     * 
     * @param idArticle ID de l'article.
     * @return ID de l'utilisateur.
     */
	@Override
	public long idUtilisateurMontantMax(long idArticle) {
		return enchereDAO.idUtilisateurMontantMax(idArticle);
	}

	/**
     * Récupère la catégorie d'un article sous forme de chaîne de caractères.
     * 
     * @param idArticle ID de l'article.
     * @return Nom de la catégorie.
     */
	@Override
	public String categorieArticle(long idArticle) {
		return enchereDAO.categorieArticle(idArticle);
	}

	/**
     * Liste les articles mis en vente par un utilisateur avec enchères en cours.
     * 
     * @param idUtilisateur ID de l'utilisateur.
     * @return Liste des articles.
     */
	@Override
	public List<Article> consulterArticleEncheresEnCours(long idUtilisateur) {
		List<Article> articles = articleDAO.consulterArticleEncheresEnCours(idUtilisateur);
	    return articles;
	}

	/**
     * Liste les articles sur lesquels l'utilisateur a enchéri et dont les enchères sont en cours.
     * 
     * @param idUtilisateur ID de l'utilisateur.
     * @return Liste des articles.
     */
	@Override
	public List<Article> consulterArticleMesEncheresEnCours(long idUtilisateur) {
		List<Article> articles = articleDAO.consulterArticleMesEncheresEnCours(idUtilisateur);
	    return articles;
	}

	/**
     * Liste les articles remportés par l'utilisateur.
     * 
     * @param idUtilisateur ID de l'utilisateur.
     * @return Liste des articles gagnés.
     */
	@Override
	public List<Article> consulterArticleMesEncheresRemportees(long idUtilisateur) {
		List<Article> articles = articleDAO.consulterArticleMesEncheresRemportees(idUtilisateur);
	    return articles;
	}

	 /**
     * Liste les ventes en cours de l'utilisateur.
     * 
     * @param idUtilisateur ID du vendeur.
     * @return Liste des articles vendus en cours.
     */
	@Override
	public List<Article> consulterArticleMesVentesEnCours(long idUtilisateur) {
		List<Article> articles = articleDAO.consulterArticleMesVentesEnCours(idUtilisateur);
	    return articles;
	}

	 /**
     * Liste les ventes futures de l'utilisateur.
     * 
     * @param idUtilisateur ID du vendeur.
     * @return Liste des articles à venir.
     */
	@Override
	public List<Article> consulterArticleMesVentesFutures(long idUtilisateur) {
		List<Article> articles = articleDAO.consulterArticleMesVentesFutures(idUtilisateur);
	    return articles;
	}

	/**
     * Liste les ventes terminées de l'utilisateur.
     * 
     * @param idUtilisateur ID du vendeur.
     * @return Liste des articles vendus.
     */
	@Override
	public List<Article> consulterArticleMesVentesTerminees(long idUtilisateur) {
		List<Article> articles = articleDAO.consulterArticleMesVentesTerminees(idUtilisateur);
	    return articles;
	}

	 /**
     * Liste les articles appartenant à une catégorie donnée.
     * 
     * @param idCategorie ID de la catégorie.
     * @return Liste des articles.
     */
	@Override
	public List<Article> consulterArticleParIdCategorie(long idCategorie) {
		List<Article> articles = articleDAO.consulterArticleParCategorie(idCategorie);
	    return articles;
	
	}

	/**
     * Liste les articles correspondant à un mot-clé.
     * 
     * @param motCle Mot-clé à rechercher.
     * @return Liste des articles correspondants.
     */
	@Override
	public List<Article> consulterArticleParMotCle(String motCle) {
		List<Article> articles = articleDAO.consulterArticleParMotCle(motCle);
	    return articles;
	}

	/**
     * Récupère le nombre d'enchères pour un article.
     * 
     * @param idArticle ID de l'article.
     * @return Nombre d'enchères.
     */
	@Override
	public int nbEnchere(long idArticle) {
		return enchereDAO.nbEnchere(idArticle);
	}

	/**
     * Récupère l'identifiant du vendeur d'un article.
     * 
     * @param idArticle ID de l'article.
     * @return ID du vendeur.
     */
	@Override
	public long idUtilisateurVendeur(long idArticle) {
		return enchereDAO.idUtilisateurVendeur(idArticle);
	}

	/**
     * Met à jour l'état de vente d'un article.
     * 
     * @param idArticle ID de l'article.
     */
	@Override
	public void majEtatVente(long idArticle) {
		enchereDAO.majEtatVente(idArticle);
		
	}

}
