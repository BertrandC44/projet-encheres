package fr.eni.encheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;




import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.eni.encheres.bll.EncheresService;
import fr.eni.encheres.bo.Article;

@SessionAttributes({"membreEnSession"})
@Controller
public class EncheresController {
	
	private EncheresService encheresService;

	public EncheresController(EncheresService encheresService) {
		this.encheresService = encheresService;
	}


	@GetMapping("/")
	public String index() {
		System.out.println("Clic vers Index");
		return "encheres";
	}


	@GetMapping("/encheres/connexion")
	public String connexion() {
		System.out.println("Clic vers Connexion");
		return "connexion";
	}
	
	@GetMapping("/encheres/inscription")
	public String inscription() {
		System.out.println("Clic vers Inscription");
		return "inscription";
	}
	
	@GetMapping("/enchereid/detail")
	public String afficherEnchereId(@RequestParam(name = "id") long i, Model model) {
		Article unArticle = encheresService.consulterArticleParId(i);
		model.addAttribute("articleid", unArticle);
		String acteurFilm = "";

		model.addAttribute("acteur", acteurFilm);
		
		return "encheresid";
	}



	@GetMapping("/encheres")
	public String encheres() {
		System.out.println("afficher les enchères");
		return "encheres";
		
	}
	
	@GetMapping("/encheres/vente")
	public String vente(Model model) {
		Article nouvelArticle = new Article();
		model.addAttribute("article", nouvelArticle);
		System.out.println("afficher les ventes");
		return "vente";
	}

	@PostMapping("/encheres/vente")
	public String ventePost(@ModelAttribute Article article) {
		this.encheresService.creerVente(article);
		
		return "redirect:/encheres";
	}
	

	@GetMapping("/encheres/detail")
	public String afficherDetailEnchere(@RequestParam(name="id") long idArticle, Model model) {
		Article article = encheresService.consulterArticleParId(idArticle);
		model.addAttribute("article", article);
		return"enchere-en-cours";
	}
	
	@PostMapping("/encheres")
		public String detailEnchere() {
		return"encheres";
		
	}
	


}
