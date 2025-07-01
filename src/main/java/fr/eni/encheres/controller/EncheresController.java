package fr.eni.encheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EncheresController {

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


	@GetMapping("/encheres")
	public String encheres() {
		System.out.println("afficher les enchères");
		return "encheres";
		
	}
	
	@GetMapping("/echeres/vente")
	public String vente() {
		System.out.println("afficher les ventes");
		return "vente";
	}

	@PostMapping()
	public String ventePost(@RequestParam("description") String description,@RequestParam("article") String article, Model model) {
		String imageUrl = null;
		
		
		
		return null;
	}
	
}
