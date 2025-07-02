package fr.eni.encheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import fr.eni.encheres.bll.EncheresService;
import fr.eni.encheres.bll.UtilisateurService;
import fr.eni.encheres.bll.UtilisateurServiceImpl;
import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Utilisateur;
import jakarta.validation.Valid;


@Controller
@SessionAttributes({"membreEnSession"})
public class EncheresController {

    private final UtilisateurServiceImpl utilisateurServiceImpl;
	
	private EncheresService encheresService;
	private UtilisateurService utilisateurService;

	public EncheresController(EncheresService encheresService, UtilisateurService utilisateurService, UtilisateurServiceImpl utilisateurServiceImpl) {
		this.encheresService = encheresService;
		this.utilisateurServiceImpl = utilisateurServiceImpl;
	}


	@GetMapping("/")
	public String index() {
		System.out.println("Clic vers Index");
		return "encheres";
	}

	
	@GetMapping("/encheres")
	public String encheres() {
		System.out.println("afficher les enchères");
		return "encheres";
		
	}


	@GetMapping("/encheres/connexion")
	public String connexion() {
		System.out.println("Clic vers Connexion");
		return "connexion";
	}
	
	@GetMapping("/encheres/inscription")
	public String afficherInscription(Model model) {
		Utilisateur utilisateur = new Utilisateur();
		model.addAttribute("utilisateur", utilisateur);
		return "inscription";
	}
	
	@PostMapping("/enchères")
	public String creerUtilisateur(@Valid @ModelAttribute Utilisateur utilisateur, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "inscription";
			
		} else {
			try {
				utilisateurService.creerUtilisateur(utilisateur);
				return "redirect:/encheres";
		
			} catch (BusinessException e) {
				e.getMessages().forEach(m->{
					ObjectError error = new ObjectError("globalError", m);
					bindingResult.addError(error);
				});
			return "inscription";
			}
			
		}
	}
	
	

	@GetMapping("/encheres/deconnexion")
	public String finSession(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		return "redirect:/encheres";
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
		public String retourDetailEnchere() {
		return"encheres";
		
	}
	


}
