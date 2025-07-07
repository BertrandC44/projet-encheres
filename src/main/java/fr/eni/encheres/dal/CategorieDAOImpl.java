package fr.eni.encheres.dal;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.encheres.bo.Categorie;

@Repository
public class CategorieDAOImpl implements CategorieDAO{
	
	private static final String FIND_ALL ="SELECT*FROM CATEGORIE";
	private static final String FIND_BY_ID ="SELECT*FROM CATEGORIE WHERE idCategorie= :idCategorie";
	
	private NamedParameterJdbcTemplate jdbcTemplate;

	public CategorieDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Categorie consulterCategorieParId(long id) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idCategorie", id);
		return this.jdbcTemplate.queryForObject(FIND_BY_ID, map, new BeanPropertyRowMapper<>(Categorie.class));
	}

	@Override
	public List<Categorie> consulterCategories() {
		return this.jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Categorie.class));
	}

}
