package fr.eni.encheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.eni.tp.filmotheque.bo.Film;

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
	
	@GetMapping("/Enchereid/detail")
	public String afficherUnFilmId(@RequestParam(name = "id") long i, Model model) {
		Film unFilm = filmService.consulterFilmParId(i);
		model.addAttribute("filmid", unFilm);
		String acteurFilm = "";

		model.addAttribute("acteur", acteurFilm);

		System.out.println(unFilm);
		return "filmsid";
	}

}
