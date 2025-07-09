package fr.eni.encheres.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import fr.eni.encheres.bll.EncheresService;
import fr.eni.encheres.bll.UtilisateurService;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.bo.Retrait;
import fr.eni.encheres.bo.Utilisateur;

import fr.eni.encheres.dal.ArticleDAO;
import fr.eni.encheres.dal.CategorieDAO;

import fr.eni.encheres.exception.BusinessException;

@Controller
@SessionAttributes({ "utilisateurEnSession", "categorieEnSession" })
public class EncheresController {


	private EncheresService encheresService;
	private UtilisateurService utilisateurService;

	public EncheresController(EncheresService encheresService, UtilisateurService utilisateurService) {
		this.encheresService = encheresService;
		this.utilisateurService = utilisateurService;

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
	public String filtresEncheres(Model model, @ModelAttribute("utilisateurEnSession") Utilisateur utilisateurEnSession,
			@RequestParam(name = "achats", required = false) List<String> achats,
			@RequestParam(name = "ventes", required = false) List<String> ventes,
			@RequestParam(name = "categorie", required = false) String categorie,
			@RequestParam(name = "motCle", required = false) String motCle) {

		List<Categorie> categories = encheresService.consulterCategories();
		List<Article> articles = new ArrayList<Article>();
		
		if (achats == null && ventes == null && categorie.equals("0") && motCle.equals("")) {
					return "redirect:/encheres";	
		}

		if (utilisateurEnSession.getIdUtilisateur() != 0) {
			if (achats != null) {
				for (String a : achats) {
					if ("eO".equals(a)) {
						articles.addAll(encheresService
								.consulterArticleEncheresEnCours(utilisateurEnSession.getIdUtilisateur()));
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
						articles.addAll(encheresService
								.consulterArticleMesVentesEnCours(utilisateurEnSession.getIdUtilisateur()));
					}
					if ("vNd".equals(v)) {
						articles.addAll(encheresService
								.consulterArticleMesVentesFutures(utilisateurEnSession.getIdUtilisateur()));
					}
					if ("vT".equals(v)) {
						articles.addAll(encheresService
								.consulterArticleMesVentesTerminees(utilisateurEnSession.getIdUtilisateur()));
					}
				}
			}

		}

		if (categorie != null && categorie != "0") {
			articles.addAll(encheresService.consulterArticleParIdCategorie(Long.parseLong(categorie)));

		}

		if (motCle != null && motCle != "") {
			articles.addAll(encheresService.consulterArticleParMotCle(motCle));

		}

		model.addAttribute("articles", articles);
		model.addAttribute("categorie", categories);
		return "encheres";

	}

	@GetMapping("/encheres/encherir")
	public String encherir(@RequestParam(name = "idArticle") long idArticle, Model model,
			@ModelAttribute("utilisateurEnSession") Utilisateur utilisateurEnSession) {
		
		  Enchere enchere = new Enchere();
		   model.addAttribute("enchere", enchere);

		Article article = encheresService.consulterArticleParId(idArticle);
		model.addAttribute("article", article);
		
		int montantMax = encheresService.montantMax(idArticle);
		int enchereMin = montantMax + 1;
		String utilisateurMontantMax = encheresService.utilisateurMontantMax(idArticle);
		String categorieArticle = encheresService.categorieArticle(idArticle);
		
		model.addAttribute("article", article);
		model.addAttribute("montantMax", montantMax);
		model.addAttribute("utilisateurMontantMax", utilisateurMontantMax);
		model.addAttribute("enchereMin", enchereMin);
		model.addAttribute("categorieArticle", categorieArticle);

		if (utilisateurEnSession.getIdUtilisateur() != 0) {
			if (article == null) {
				return "redirect:/encheres";
				}
				
				LocalDate dateFin = article.getDateFinEncheres();
				if (dateFin != null && dateFin.isBefore(LocalDate.now())) {
					return "acquisition";
					}

			return "encherir";

		}
		return "connexion";
	}

	
	
	

	@PostMapping("/encheres/encherir")
	public String encherirPost(@RequestParam(name = "montantEnchere") int montantEnchere,
			@ModelAttribute("utilisateurEnSession") Utilisateur utilisateurEnSession,
			@ModelAttribute("enchere") Enchere enchere, BindingResult bindingResult,
			@RequestParam(name = "idArticle") long idArticle, Model model) {
		
		Article article = encheresService.consulterArticleParId(idArticle);
	    model.addAttribute("article", article);
	    
	    int montantMax = encheresService.montantMax(idArticle);
		model.addAttribute("montantMax", montantMax);
		
		String utilisateurMontantMax = encheresService.utilisateurMontantMax(idArticle) ;
		model.addAttribute("utilisateurMontantMax", utilisateurMontantMax);
		
		String categorieArticle = encheresService.categorieArticle(idArticle);
		model.addAttribute("categorieArticle", categorieArticle);
		
		model.addAttribute("enchere",enchere);
		model.addAttribute("montantEnchere", montantEnchere);
//		model.addAttribute("idArticle", idArticle);

		if (bindingResult.hasErrors()) {
			
			return "redirect:/encheres/encherir?idArticle=" + idArticle;
		} else {
			try {

				encheresService.encherir(montantEnchere, utilisateurEnSession.getIdUtilisateur(), idArticle);
			} catch (BusinessException e) {
				e.getErrors().forEach(message->{
				    bindingResult.addError(new ObjectError("globalError", message));
					});
				}
				    return "encherir";
				}
	}


	@GetMapping("/encheres/vente")
	public String vente(@ModelAttribute("utilisateurEnSession") Utilisateur utilisateurEnSession,  Model model) {
		List<Categorie> categories = encheresService.consulterCategories();
		Article article = new Article();
		article.setCategorie(new Categorie());
		
		model.addAttribute("utilisateur", utilisateurEnSession);
		model.addAttribute("article", new Article());
		model.addAttribute("categorie", categories);

		return "vente";
	}

    @GetMapping("/encheres/detail")
    public String afficherDetailEnchere(@RequestParam(name="id") long idArticle, Model model) {
        Article article = encheresService.consulterArticleParId(idArticle);
        model.addAttribute("article", article);
        return "enchere-en-cours";
    }

	@PostMapping("/encheres/vente")
	public String creerArticle(@ModelAttribute Article article,
			@ModelAttribute("utilisateurEnSession") Utilisateur utilisateurEnSession,
			@RequestParam("action") String action, Model model, @RequestParam(name = "rue") String rue,
			@RequestParam(name = "codePostal") String codePostal, @RequestParam(name = "ville") String ville) {

		List<Categorie> categories = encheresService.consulterCategories();
		model.addAttribute("categorie", categories);

		Retrait retrait = new Retrait();
		retrait.setRue(rue);
		retrait.setVille(ville);
		retrait.setCodePostal(codePostal);
		article.setRetrait(retrait);
		
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

				return "connexion";
			} else {

				article.setUtilisateur(utilisateurEnSession);
				article.setEtatVente(1);

				this.encheresService.creerVente(article);

				return "redirect:/encheres";
			}

		} else

			model.addAttribute("article", article);

		return "vente";

	}


	@ModelAttribute("utilisateurEnSession")
	public Utilisateur addUtilisateurEnSession() {
		System.out.println("Utilisateur en session");
		return new Utilisateur();
	}



	@ModelAttribute("categorieEnSession")
	public List<Categorie> chargerCategoriesEnSession() {
		return this.encheresService.consulterCategories();	}



	@GetMapping("/encheres/acquisition")
	public String acquerir(@RequestParam(name = "idArticle") long idArticle, @ModelAttribute("utilisateurEnSession") Utilisateur utilisateurEnSession, Model model) {

		Article article = encheresService.consulterArticleParId(idArticle);
		Utilisateur vendeur = utilisateurService.consulterUtilisateursParId(article.getUtilisateur().getIdUtilisateur());
		System.out.println(article);
	
		if (encheresService.isEnchereClosed(idArticle)) {
			if (utilisateurEnSession.getIdUtilisateur() != 0) {
				if (article != null) {
					model.addAttribute("article", article);
					model.addAttribute("vendeur", vendeur);
					model.addAttribute("montantMax", encheresService.montantMax(idArticle));
					model.addAttribute("utilisateurMontantMax", encheresService.utilisateurMontantMax(idArticle));
					if (utilisateurEnSession.getIdUtilisateur() == encheresService.idUtilisateurMontantMax(idArticle)) {
						model.addAttribute("titreAcquereur", "Vous avez remporté l'enchère");
					} else {
						model.addAttribute("titre", "L'enchère a été remporté");
					}
					
				}
				return "acquisition";
	
			}
		}
		return "connexion";
	}
	
}
