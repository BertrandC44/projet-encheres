package fr.eni.encheres.controller;

import java.util.List;

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
import fr.eni.encheres.bll.contexte.ContexteService;
import fr.eni.encheres.bll.contexte.ContexteServiceImpl;
import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.exception.BusinessException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@SessionAttributes({"utilisateurEnSession"})
public class EncheresController {


    private EncheresService encheresService;
    private UtilisateurService utilisateurService;
    private ContexteService contexteService;

    public EncheresController(EncheresService encheresService, UtilisateurService utilisateurService, ContexteService contexteService) {
        this.encheresService = encheresService;
        this.utilisateurService = utilisateurService;
        this.contexteService = contexteService;
    }

    @GetMapping("/")
    public String index() {
        System.out.println("Clic vers Index");
        return "encheres";
    }

    @GetMapping("/encheres")
    public String encheres(Model model) {
        List<Article> articles = encheresService.consulterArticlePseudo();
        model.addAttribute("articles", articles);
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

    @GetMapping("/encheres/encherir")
    public String encherir(@RequestParam(name="idArticle") long idArticle, Model model) {
    	System.out.println("l'id de l'article est :" + idArticle);
        Article article = encheresService.consulterArticleParId(idArticle);
       
        int montantMax = encheresService.montantMax(idArticle);
        int enchereMin = montantMax+1;
        String utilisateurMontantMax = encheresService.utilisateurMontantMax(idArticle);
        String categorieArticle = encheresService.categorieArticle(idArticle);
        if (article != null) {
            model.addAttribute("article", article);
            model.addAttribute("montantMax", montantMax);
            model.addAttribute("utilisateurMontantMax", utilisateurMontantMax);
            model.addAttribute("enchereMin", enchereMin);
            model.addAttribute("categorieArticle", categorieArticle);
        }
        return "encherir";
    }

    @PostMapping("/encheres/encherir")
    public String encherirPost(@RequestParam(name="montantEnchere") int montantEnchere,
    						   @ModelAttribute("utilisateurEnSession") Utilisateur utilisateurEnSession, 
                               @RequestParam(name="idArticle") long idArticle, 
                               Model model) {
    	
        model.addAttribute("montantEnchere", montantEnchere);
        model.addAttribute("utilisateurEnSession", utilisateurEnSession);
        model.addAttribute("idArticle", idArticle);

        System.out.println("id utilisateur= " + utilisateurEnSession.getIdUtilisateur());

        return "redirect:/encheres";
    
    }

    @GetMapping("/encheres/vente")
    public String vente(Model model) {
        List<Categorie> categories = encheresService.consulterCategories();
        Article article = new Article();
        article.setCategorie(new Categorie());
        model.addAttribute("article", new Article());
        model.addAttribute("categorie", categories);
        return "vente";
    }

    @PostMapping("/encheres/vente")
    public String ventePost(@ModelAttribute Article article, @ModelAttribute("utilisateurEnSession") Utilisateur utilisateurEnSession,
                            @RequestParam("action") String action,
                            Model model) {

        List<Categorie> categories = encheresService.consulterCategories();
        model.addAttribute("categorie", categories);

        if ("categorieChoisie".equals(action)) {
            if (article.getCategorie() != null && article.getCategorie().getIdCategorie() > 0) {
                Categorie selectedCategorie = encheresService.consulterCategorieParId(article.getCategorie().getIdCategorie());
                article.setCategorie(selectedCategorie);
            }
            model.addAttribute("article", article);
            return "vente";
        }

        if ("validerFormulaire".equals(action)) {
        	if(article.getCategorie()== null || article.getCategorie().getIdCategorie()==0) {
        		model.addAttribute("error", "veuillez selectionner une catégorie");
        		  model.addAttribute("article", article);
        		  return "vente";
        	}
        	
        		if (utilisateurEnSession == null || utilisateurEnSession.getIdUtilisateur()==0) {
        			return "redirect:/connexion";
        		}
        	article.setUtilisateur(utilisateurEnSession);
        	article.setEtatVente(1);
        	
            this.encheresService.creerVente(article);
            return "redirect:/encheres";
        }
        model.addAttribute("article", article);
        return "vente";
    }


    @GetMapping("/encheres/detail")
    public String afficherDetailEnchere(@RequestParam(name="id") long idArticle, Model model) {
        Article article = encheresService.consulterArticleParId(idArticle);
        model.addAttribute("article", article);
        return "enchere-en-cours";
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

    @ModelAttribute("categorieEnSession")
    public List<Categorie> chargerCategoriesEnSession() {
        return this.encheresService.consulterCategories();
    }

}

