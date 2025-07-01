package fr.eni.encheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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


	@GetMapping("/encheres/detail")
	public String afficherDetailEnchere() {
		return"enchere-en-cours";
	}
	
	@PostMapping("/encheres")
		public String detailEnchere() {
		return"encheres";
		
	}
	
}
