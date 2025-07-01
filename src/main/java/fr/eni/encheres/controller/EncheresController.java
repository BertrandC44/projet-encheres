package fr.eni.encheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.eni.encheres.bll.EncheresService;
import fr.eni.encheres.bo.Article;

@Controller
public class EncheresController {
	
	private EncheresService encheresService;

	@GetMapping("/")
	public String index() {
		System.out.println("Clic vers Index");
		return "encheres";
	}
	
	@GetMapping("/connexion")
	public String connexion() {
		System.out.println("Clic vers Connexion");
		return "connexion";
	}
	
	@GetMapping("/inscription")
	public String inscription() {
		System.out.println("Clic vers Inscription");
		return "inscription";
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
	
}
