package fr.eni.encheres.bo;

import java.util.List;

public class Categorie {
	
	private long idCategorie;
	private String libelle;
	
	private List<Article> articles;


	public Categorie() {
	}

	
	public Categorie(String libelle, List<Article> articles) {
		this.libelle = libelle;
		this.articles = articles;
	}


	public Categorie(long idCategorie, String libelle, List<Article> articles) {
		this(libelle, articles);
		this.idCategorie = idCategorie;
		
	}

	public long getIdCategorie() {
		return idCategorie;
	}

	public void setIdCategorie(long idCategorie) {
		this.idCategorie = idCategorie;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Categorie [idCategorie=");
		builder.append(idCategorie);
		builder.append(", libelle=");
		builder.append(libelle);
		builder.append(", articles=");
		builder.append(articles);
		builder.append("]");
		return builder.toString();
	}
	
	

}
