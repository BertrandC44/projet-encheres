package fr.eni.encheres.bo;

import java.time.LocalDate;

public class Enchere {

	private LocalDate dateEnchere;
	private int montantEnchere;

	private Utilisateur utilisateur;
	private Article article;

	public Enchere() {

	}

	public Enchere(LocalDate dateEnchere, int montantEnchere, Utilisateur utilisateur, Article article) {
		this.dateEnchere = dateEnchere;
		this.montantEnchere = montantEnchere;
		this.utilisateur = utilisateur;
		this.article = article;
	}

	public LocalDate getDateEnchere() {
		return dateEnchere;
	}

	public void setDateEnchere(LocalDate dateEnchere) {
		this.dateEnchere = dateEnchere;
	}

	public int getMontantEnchere() {
		return montantEnchere;
	}

	public void setMontantEnchere(int montantEnchere) {
		this.montantEnchere = montantEnchere;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Enchere [dateEnchere=");
		builder.append(dateEnchere);
		builder.append(", montantEnchere=");
		builder.append(montantEnchere);
		builder.append(", utilisateur=");
		builder.append(utilisateur);
		builder.append(", article=");
		builder.append(article);
		builder.append("]");
		return builder.toString();
	}

}
