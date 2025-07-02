package fr.eni.encheres.bo;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class Utilisateur {

	private long idUtilisateur;
	@NotBlank
	@Pattern(regexp = "^[A-Za-z0-9_]+$")
	private String pseudo;
	@NotBlank
	private String nom;
	@NotBlank
	private String prenom;
	@NotBlank
	@Pattern(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$\" />")
	private String email;
	@NotBlank
	private String telephone;
	@NotBlank
	private String rue;
	@NotBlank
	private String codePostal;
	@NotBlank
	private String ville;
	@NotBlank
	private String motDePasse;
	private int credit;
	private boolean admin;
	
	private List<Article> articles = new ArrayList<Article>();
	private List<Enchere> encheres = new ArrayList<Enchere>();
	
	
	
	public Utilisateur() {
		super();
	}

	public Utilisateur(String pseudo, String nom, String prenom, String email, String telephone, String rue,
			String codePostal, String ville, String motDePasse, int credit, boolean admin) {
		this.pseudo = pseudo;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.telephone = telephone;
		this.rue = rue;
		this.codePostal = codePostal;
		this.ville = ville;
		this.motDePasse = motDePasse;
		this.credit = credit;
		this.admin = admin;

	}

	public Utilisateur(long idUtilisateur, String pseudo, String nom, String prenom, String email, String telephone,
			String rue, String codePostal, String ville, String motDePasse, int credit, boolean admin) {
		this(pseudo, nom, prenom, email, telephone, rue, codePostal, ville, motDePasse, credit, admin);
		this.idUtilisateur = idUtilisateur;
	}
	
	
	public long getIdUtilisateur() {
		return idUtilisateur;
	}
	public void setIdUtilisateur(long idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}
	public String getPseudo() {
		return pseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getRue() {
		return rue;
	}
	public void setRue(String rue) {
		this.rue = rue;
	}
	public String getCodePostal() {
		return codePostal;
	}
	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	public String getMotDePasse() {
		return motDePasse;
	}
	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}
	public int getCredit() {
		return credit;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public List<Article> getArticles() {
		return articles;
	}
	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}
	public List<Enchere> getEncheres() {
		return encheres;
	}
	public void setEncheres(List<Enchere> encheres) {
		this.encheres = encheres;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Utilisateur [idUtilisateur=");
		builder.append(idUtilisateur);
		builder.append(", pseudo=");
		builder.append(pseudo);
		builder.append(", nom=");
		builder.append(nom);
		builder.append(", prenom=");
		builder.append(prenom);
		builder.append(", email=");
		builder.append(email);
		builder.append(", telephone=");
		builder.append(telephone);
		builder.append(", rue=");
		builder.append(rue);
		builder.append(", codePostal=");
		builder.append(codePostal);
		builder.append(", ville=");
		builder.append(ville);
		builder.append(", motDePasse=");
		builder.append(motDePasse);
		builder.append(", credit=");
		builder.append(credit);
		builder.append(", admin=");
		builder.append(admin);
		builder.append(", articles=");
		builder.append(articles);
		builder.append(", encheres=");
		builder.append(encheres);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
