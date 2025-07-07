package fr.eni.encheres.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import fr.eni.encheres.bll.EncheresService;
import fr.eni.encheres.bll.UtilisateurService;

import fr.eni.encheres.bll.contexte.ContexteService;
import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dal.CategorieDAO;
import fr.eni.encheres.exception.BusinessException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@SessionAttributes({ "utilisateurEnSession", "categorieEnSession" })
public class EncheresController {

	private final UtilisateurController utilisateurController;

	private EncheresService encheresService;
	private UtilisateurService utilisateurService;

	public EncheresController(EncheresService encheresService, UtilisateurService utilisateurService,
			UtilisateurController utilisateurController) {
		this.encheresService = encheresService;
		this.utilisateurService = utilisateurService;
		this.utilisateurController = utilisateurController;
	}

	@GetMapping("/")
	public String index() {
		System.out.println("Clic vers Index");
		return "encheres";
	}

	@GetMapping("/encheres")
	public String encheres(Model model) {
		List<Article> articles = encheresService.consulterArticlePseudo();
		List<Categorie> categories = encheresService.consulterCategories();
		model.addAttribute("articles", articles);
		model.addAttribute("categorie", categories);
		return "encheres";
	}

	@PostMapping("/encheres")
	public String encheres(Model model, @ModelAttribute("utilisateurEnSession") Utilisateur utilisateurEnSession,
			@RequestParam(name = "achats", required = false) List<String> achats,
			@RequestParam(name = "ventes", required = false) List<String> ventes,
			@RequestParam(name = "categorie", required = false) String categorie) {

		List<Article> articles = new ArrayList<Article>();

		if (achats != null) {
			for (String a : achats) {
				System.out.println("Filtre achat : " + a);
				if ("eO".equals(a)) {
					articles.addAll(
							encheresService.consulterArticleEncheresEnCours(utilisateurEnSession.getIdUtilisateur()));
				}
				if ("eEc".equals(a)) {
					articles.addAll(encheresService
							.consulterArticleMesEncheresEnCours(utilisateurEnSession.getIdUtilisateur()));
				}
				if ("eE".equals(a)) {
					articles.addAll(encheresService
							.consulterArticleMesEncheresRemportees(utilisateurEnSession.getIdUtilisateur()));
				}
			}
		}

		if (ventes != null) {
			for (String v : ventes) {

				if ("vEc".equals(v)) {
					articles.addAll(
							encheresService.consulterArticleMesVentesEnCours(utilisateurEnSession.getIdUtilisateur()));
				}
				if ("vNd".equals(v)) {
					articles.addAll(
							encheresService.consulterArticleMesVentesFutures(utilisateurEnSession.getIdUtilisateur()));
				}
				if ("vT".equals(v)) {
					articles.addAll(encheresService
							.consulterArticleMesVentesTerminees(utilisateurEnSession.getIdUtilisateur()));
				}
			}
		}

		model.addAttribute("articles", articles);
		return "/encheres";

	}

	@GetMapping("/encheres/encherir")
	public String encherir(@RequestParam(name = "idArticle") long idArticle, Model model) {
		System.out.println("l'id de l'article est :" + idArticle);
		Article article = encheresService.consulterArticleParId(idArticle);

		int montantMax = encheresService.montantMax(idArticle);
		int enchereMin = montantMax + 1;
		String utilisateurMontantMax = encheresService.utilisateurMontantMax(idArticle);
		String categorieArticle = encheresService.categorieArticle(idArticle);
		if (article != null) {
			model.addAttribute("article", article);
			model.addAttribute("montantMax", montantMax);
			model.addAttribute("utilisateurMontantMax", utilisateurMontantMax);
			model.addAttribute("enchereMin", enchereMin);
			model.addAttribute("categorieArticle", categorieArticle);
		}
		return "encherir";
	}

	@PostMapping("/encheres/encherir")

	public String encherirPost(@Valid @RequestParam(name = "montantEnchere") int montantEnchere,
			@ModelAttribute("utilisateurEnSession") Utilisateur utilisateurEnSession,
			@RequestParam(name = "idArticle") long idArticle, Model model, BindingResult bindingResult) {
		model.addAttribute("montantEnchere", montantEnchere);
		model.addAttribute("utilisateurEnSession", utilisateurEnSession);
		model.addAttribute("idArticle", idArticle);

		Utilisateur utilisateur = utilisateurService
				.consulterUtilisateursParId(utilisateurEnSession.getIdUtilisateur());
		if (bindingResult.hasErrors()) {
			return "encheres/encherir";
		} else {
			System.out.println("id utilisateur= " + utilisateur.getIdUtilisateur());
			System.out.println("Solde utilisateur= " + utilisateur.getCredit());
			System.out.println("id article= " + idArticle);
			try {
				encheresService.encherir(montantEnchere, utilisateur.getIdUtilisateur(), idArticle);
			} catch (BusinessException e) {
				e.printStackTrace();
			}

			return "redirect:/encheres";
		}

	}

	@GetMapping("/encheres/vente")
	public String vente(Model model) {
		List<Categorie> categories = encheresService.consulterCategories();
		Article article = new Article();
		article.setCategorie(new Categorie());
		model.addAttribute("article", new Article());
		model.addAttribute("categorie", categories);
		return "vente";
	}

	@PostMapping("/encheres/vente")
	public String creerArticle(@ModelAttribute Article article,
			@ModelAttribute("utilisateurEnSession") Utilisateur utilisateurEnSession,
			@RequestParam("action") String action, Model model) {

		List<Categorie> categories = encheresService.consulterCategories();
		model.addAttribute("categorie", categories);

		if ("categorieChoisie".equals(action)) {
			if (article.getCategorie() != null && article.getCategorie().getIdCategorie() > 0) {

				Categorie selectedCategorie = encheresService
						.consulterCategorieParId(article.getCategorie().getIdCategorie());
				article.setCategorie(selectedCategorie);
			} else {

				model.addAttribute("article", article);
				return "vente";
			}

		}

		if ("validerFormulaire".equals(action)) {
			if (article.getCategorie() == null || article.getCategorie().getIdCategorie() == 0) {

				model.addAttribute("error", "veuillez selectionner une catégorie");
				model.addAttribute("article", article);
				return "vente";
			}

			if (utilisateurEnSession == null || utilisateurEnSession.getIdUtilisateur() == 0) {

				return "redirect:/connexion";
			} else {

				article.setUtilisateur(utilisateurEnSession);
				article.setEtatVente(1);

				this.encheresService.creerVente(article);
				article.setCategorie(article.getCategorie());
				article.setDateDebutEncheres(article.getDateDebutEncheres());
				article.setDateFinEncheres(article.getDateFinEncheres());
				article.setDescription(article.getDescription());
				article.setMiseAPrix(article.getMiseAPrix());
				article.setNomArticle(article.getNomArticle());
				article.setRetrait(article.getRetrait());

				return "redirect:/encheres";
			}

		} else

			model.addAttribute("article", article);

		return "vente";

	}

	@GetMapping("/encheres/detail")
	public String afficherDetailEnchere(@RequestParam(name = "id") long idArticle, Model model) {
		Article article = encheresService.consulterArticleParId(idArticle);
		model.addAttribute("article", article);
		return "enchere-en-cours";
	}

	@ModelAttribute("categorieEnSession")
	public List<Categorie> chargerCategoriesEnSession() {
		return this.encheresService.consulterCategories();
	}

}
