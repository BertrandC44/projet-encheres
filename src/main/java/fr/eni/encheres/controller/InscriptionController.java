package fr.eni.encheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class InscriptionController {
	
	@GetMapping("/inscription")
	public String afficherInscription() {
		System.out.println("page inscription");
		return "inscription";
	}
	

}
