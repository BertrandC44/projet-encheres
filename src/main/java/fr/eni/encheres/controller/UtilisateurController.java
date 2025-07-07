package fr.eni.encheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import fr.eni.encheres.bll.UtilisateurService;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.exception.BusinessException;
import jakarta.validation.Valid;

@Controller
@SessionAttributes({"utilisateurEnSession","categorieEnSession"})
public class UtilisateurController {

    private UtilisateurService utilisateurService;

	public UtilisateurController(UtilisateurService utilisateurService) {
		this.utilisateurService = utilisateurService;
	}
    
	

	    @GetMapping("/encheres/inscription")
	    public String afficherInscription(Model model) {
	        Utilisateur utilisateur = new Utilisateur();
	        model.addAttribute("utilisateur", utilisateur);
	        return "inscription";
	    }

	    @PostMapping("/encheres/inscription")
	    public String creerUtilisateur(@Valid @ModelAttribute Utilisateur utilisateur, BindingResult bindingResult,@ModelAttribute("utilisateurEnSession") Utilisateur utilisateurEnSession,Model model) {
	        if(utilisateur.getConfMdp().equals(utilisateur.getMotDePasse())) {
	            if (bindingResult.hasErrors()) {
	                return "inscription";
	            } else {
	                try {
	                    utilisateurService.creerUtilisateur(utilisateur);
	                    utilisateurEnSession.setIdUtilisateur(utilisateur.getIdUtilisateur());
	                    utilisateurEnSession.setPseudo(utilisateur.getPseudo());
	                    utilisateurEnSession.setNom(utilisateur.getNom());
	                    utilisateurEnSession.setPrenom(utilisateur.getPrenom());
	                    utilisateurEnSession.setEmail(utilisateur.getEmail());
	                    utilisateurEnSession.setTelephone(utilisateur.getTelephone());
	                    utilisateurEnSession.setRue(utilisateur.getRue());
	                    utilisateurEnSession.setCodePostal(utilisateur.getCodePostal());
	                    utilisateurEnSession.setVille(utilisateur.getVille());
	                    utilisateurEnSession.setMotDePasse(utilisateur.getMotDePasse());

	                    return "redirect:/encheres";

	                } catch (BusinessException e) {
	                    e.getErrors().forEach(m->{
	                        ObjectError error = new ObjectError("globalError", m);
	                        bindingResult.addError(error);
	                    });
	                    return "inscription";
	                }
	            }
	        }
	        bindingResult.rejectValue("confMdp","password.mismatch" ,"La confirmation est différente du mot de passe saisi");
	        model.addAttribute("utilisateur",utilisateur);
	        return "inscription";
	    }

	    @GetMapping("/encheres/deconnexion")
	    public String finSession(SessionStatus sessionStatus) {
	        sessionStatus.setComplete();
	        return "redirect:/encheres";
	    }
	    
	    @GetMapping("encheres/profil")
	    public String afficherProfil(@RequestParam(name="pseudo") String pseudo, Model model) {
	        Utilisateur utilisateur = utilisateurService.consulterUtilisateurParPseudo(pseudo);
	        if (utilisateur != null) {
	            model.addAttribute("utilisateur", utilisateur);
	        }
	        return "profil";
	    }

	    @PostMapping("encheres/profil")
	    public String afficherModifierProfil(@RequestParam(name="pseudo") String pseudo, Model model) {
	        Utilisateur utilisateur = utilisateurService.consulterUtilisateurParPseudo(pseudo);
	        if (utilisateur != null) {
	            model.addAttribute("utilisateur", utilisateur);
	        }
	        return "modifier-profil";
	    }

	    @PostMapping("encheres/profil/modifier")
	    public String modifierProfil(@Valid @ModelAttribute Utilisateur utilisateur, BindingResult bindingResult) {
	        try {
	            utilisateurService.modifierUtilisateur(utilisateur);
	            return "redirect:/encheres";
	        } catch (BusinessException e) {
	            e.getErrors().forEach(m->{
	                ObjectError error = new ObjectError("globalError", m);
	                bindingResult.addError(error);
	            });
	            return "modifier-profil";
	        }
	    }

	    @GetMapping("/encheres/profil/sup")
	    public String supprimerUtilisateur(@ModelAttribute Utilisateur utilisateur) {
	        utilisateurService.supprimerMonProfil(utilisateur);
	        return "redirect:/encheres/deconnexion";
	    }

	    @GetMapping("/encheres/connexion")
	    public String connexion() {
	        System.out.println("Clic vers Connexion");
	        return "connexion";
	    }
	    
	    @PostMapping("/encheres/connexion")
	    public String connexion(@RequestParam(name = "pseudo") String pseudo,
	                            @RequestParam(name="motDePasse") String mdp,
	                            @Valid @ModelAttribute("utilisateurEnSession") Utilisateur utilisateurEnSession,
	                            BindingResult bindingResult, BusinessException be) {

	        if(utilisateurService.isCompteExist(pseudo, be ))  {
	            if(utilisateurService.consulterMdpParPseudo(pseudo).equals(mdp)) {
	                Utilisateur utilisateur = this.utilisateurService.consulterUtilisateurParPseudo(pseudo);
	                if(utilisateur != null) {
	                    utilisateurEnSession.setIdUtilisateur(utilisateur.getIdUtilisateur());
	                    utilisateurEnSession.setPseudo(utilisateur.getPseudo());
	                    utilisateurEnSession.setNom(utilisateur.getNom());
	                    utilisateurEnSession.setPrenom(utilisateur.getPrenom());
	                    utilisateurEnSession.setEmail(utilisateur.getEmail());
	                    utilisateurEnSession.setTelephone(utilisateur.getTelephone());
	                    utilisateurEnSession.setRue(utilisateur.getRue());
	                    utilisateurEnSession.setCodePostal(utilisateur.getCodePostal());
	                    utilisateurEnSession.setVille(utilisateur.getVille());
	                    utilisateurEnSession.setMotDePasse(utilisateur.getMotDePasse());

	                    return "redirect:/encheres";
	                } else {
	                    utilisateurEnSession.setIdUtilisateur(0);
	                    utilisateurEnSession.setPseudo(null);
	                    utilisateurEnSession.setNom(null);
	                    utilisateurEnSession.setPrenom(null);
	                    utilisateurEnSession.setEmail(null);
	                    utilisateurEnSession.setTelephone(null);
	                    utilisateurEnSession.setRue(null);
	                    utilisateurEnSession.setCodePostal(null);
	                    utilisateurEnSession.setVille(null);
	                    utilisateurEnSession.setMotDePasse(null);

	                    return "connexion";    
	                }
	            }            
	        }
	        bindingResult.rejectValue("pseudo","pseudo.mismatch" ,"L'identifiant et/ou mot de passe incorrect");
	        return "connexion";    
	    }
	    
	    

	    @ModelAttribute("utilisateurEnSession")
	    public Utilisateur addUtilisateurEnSession() {
	        System.out.println("Utilisateur en session");
	        return new Utilisateur();
	    }
	
}
