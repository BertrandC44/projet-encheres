package fr.eni.encheres.bo;

public class Retrait {
	
	private String rue;
	private String codePostal;
	private String ville;
	
	private Article article;


	public Retrait() {
	}

	public Retrait(String rue, String codePostal, String ville, Article article) {
		this.rue = rue;
		this.codePostal = codePostal;
		this.ville = ville;
		this.article = article;
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

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Retrait [rue=");
		builder.append(rue);
		builder.append(", codePostal=");
		builder.append(codePostal);
		builder.append(", ville=");
		builder.append(ville);
		builder.append(", article=");
		builder.append(article);
		builder.append("]");
		return builder.toString();
	}
	
	

}
