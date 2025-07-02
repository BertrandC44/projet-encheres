package fr.eni.encheres.dal;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.encheres.bo.Enchere;


@Repository
public class EnchereDAOImpl implements EnchereDAO{
	
	private static final String FIND_ALL = "SELECT * FROM ENCHERE";
	private static final String UPDATE_ENCHERE = "UPDATE ENCHERE SET montantEnchere=:montantEnchere WHERE idArticle=idArticle";
	
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * Constructeur 
	 * @param namedParameterJdbcTemplate
	 */
	public EnchereDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		super();
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public List<Enchere> consulterEncheres() {
		return namedParameterJdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Enchere.class));
	}

	@Override
	public void encherir(long idArticle, int credit) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		map.addValue("montantEnchere", credit);
		this.namedParameterJdbcTemplate.update(UPDATE_ENCHERE, map);
		
	}

}
