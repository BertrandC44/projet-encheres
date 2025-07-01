package fr.eni.encheres.dal;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.encheres.bo.Enchere;

@Repository
public class EnchereDAOImpl implements EnchereDAO{
	
	private static final String FIND_ALL = "SELECT * FROM ENCHERE";
	private static final String FIND_CREDIT = "SELECT * FROM UTILISATEUR";
	
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public List<Enchere> consulterEncheres() {
		return namedParameterJdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Enchere.class));
	}

	@Override
	public void encherir(long idArticle, int credit) {
		
		
	}

}
