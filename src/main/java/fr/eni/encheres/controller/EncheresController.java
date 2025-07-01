package fr.eni.encheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import fr.eni.encheres.bo.Article;

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
	
	@GetMapping("/connexion")
	public String connexion() {
		System.out.println("Clic vers Index");
		return "connexion";
	}
	
	@GetMapping("/inscription")
	public String inscription() {
		System.out.println("Clic vers Index");
		return "inscription";
	}


	@GetMapping("/encheres/detail")
	public String afficherDetailEnchere(@RequestAttribute(name="id") long idArticle, Model model) {
		Article article = encheresService.
		return"enchere-en-cours";
	}
	
	@PostMapping("/encheres")
		public String detailEnchere() {
		return"encheres";
		
	}
	
}
