package fr.eni.encheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eni.encheres.bll.EncheresService;
import fr.eni.encheres.bo.Enchere;

@SessionAttributes({"membreEnSession"})
@Controller
public class EncheresController {
	
	private EncheresService encheresService;

	/**
	 * @param encheresService
	 */
	public EncheresController(EncheresService encheresService) {
		this.encheresService = encheresService;
	}


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
	
	@GetMapping("/enchereid/detail")
	public String afficherEnchereId(@RequestParam(name = "id") long i, Model model) {
		Enchere uneEnchere = encheresService.consulterArticleParId(i);
		model.addAttribute("articleid", uneEnchere);
		String acteurFilm = "";

		model.addAttribute("acteur", acteurFilm);

		
		return "encheresid";
	}

	@GetMapping("/encheres/detail")
	public String detailEnchere() {
		return"enchere-en-cours";
	}
}
