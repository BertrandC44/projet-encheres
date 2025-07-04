package fr.eni.encheres.bo;

import java.util.ArrayList;
import java.util.List;

public class Categorie {
	
	private long idCategorie;
	private String libelle;
	private String image;
	
	private List<Article> articles = new ArrayList<Article>();


	public Categorie() {
	}

	
	public Categorie(String libelle) {
		this.libelle = libelle;
	
	}


	public Categorie(long idCategorie, String libelle, String image, List<Article> articles) {
		super();
		this.idCategorie = idCategorie;
		this.libelle = libelle;
		this.image = image;
		this.articles = articles;
	}


	public Categorie(long idCategorie, String libelle) {
		this(libelle);
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

	
	
	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
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
