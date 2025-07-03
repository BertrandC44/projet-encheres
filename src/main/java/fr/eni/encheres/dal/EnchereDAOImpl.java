package fr.eni.encheres.dal;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.encheres.bo.Enchere;


@Repository
public class EnchereDAOImpl implements EnchereDAO {
	
	private static final String FIND_ALL = "SELECT * FROM ENCHERE";
	private static final String FIND_BY_ID = "SELECT * FROM ENCHERE WHERE idArticle = :idArticle";
	private static final String INSERT_ENCHERE = "INSERT INTO ENCHERE (dateEnchere, montantEnchere, idUtilisateur, idArticle) VALUES (:dateEnchere, :montantEnchere, :idUtilisateur, :idArticle)";
	
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public EnchereDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public List<Enchere> consulterEncheres() {
		return namedParameterJdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Enchere.class));
	}
	
	@Override
	public Enchere consulterEnchereParId(long idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, map, new BeanPropertyRowMapper<>(Enchere.class));
	}

	@Override
	public void encherir(LocalDate dateEnchere, int montantEnchere, int idUtilisateur, long idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("dateEnchere", dateEnchere);
		map.addValue("montantEnchere", montantEnchere);
		map.addValue("idUtilisateur", idUtilisateur);
		map.addValue("idArticle", idArticle);
		this.namedParameterJdbcTemplate.update(INSERT_ENCHERE, map);
	}



}
