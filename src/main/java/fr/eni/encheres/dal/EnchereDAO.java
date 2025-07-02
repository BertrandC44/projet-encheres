package fr.eni.encheres.dal;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.encheres.bo.Enchere;


public interface EnchereDAO {
	
	List<Enchere> consulterEncheres();	
	
	void encherir(LocalDate dateEnchere, int montantEnchere, int idUtilisateur, long idArticle);

}
