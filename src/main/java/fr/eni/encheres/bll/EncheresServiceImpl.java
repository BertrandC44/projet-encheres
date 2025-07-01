package fr.eni.encheres.bll;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.bo.Utilisateur;

@Service
public class EncheresServiceImpl implements EncheresService{

	@Override
	public List<Enchere> consulterEncheres() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enchere consulterEnchereParId(long idEnchere) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Categorie> consulterCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Categorie consulterCategorieParId(long idCategorie) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Utilisateur> consulterUtilisateurs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Utilisateur consulterUtilisateursParId(long idUtilisateur) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Utilisateur crediter(int credit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Utilisateur debiter(int debit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Article> consulterArticles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Article consulterArticleParId(long idArticle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Article rechercheParMotCle(String motCle) {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public void annulerEnchere(Enchere enchere) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void creerUtilisateur(Utilisateur utilisateur) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void supprimerUtilisateur(Utilisateur utilisateur) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void creerArticle(Article article) {
		// TODO Auto-generated method stub
		
	}

}
