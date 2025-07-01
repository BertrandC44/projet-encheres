package fr.eni.encheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EncheresController {

	@GetMapping("/")
	public String index() {
		System.out.println("Clic vers Index");
		return "encheres";
	}
	
	@GetMapping("/encheres")
	public String indexBis() {
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

}
