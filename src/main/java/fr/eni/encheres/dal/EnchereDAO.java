package fr.eni.encheres.dal;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;


public interface EnchereDAO {
	
	List<Enchere> consulterEncheres();	
	
	int montantEnchereMax(long idArticle);
	
	String utilisateurMontantMax(long idArticle);
	
	long idUtilisateurMontantMax(long idArticle);
	
	long idUtilisateurARecrediter(long idArticle);
	
	void encherir(int montantEnchere, long idUtilisateur, long idArticle);
	
	int recrediter(long idArticle);
	
	Enchere consulterEnchereParId(long idArticle);
	
	String categorieArticle(long idArticle);
	
	public int nbEnchere(long idArticle);
	
	long idUtilisateurVendeur(long idArticle);
	

}
