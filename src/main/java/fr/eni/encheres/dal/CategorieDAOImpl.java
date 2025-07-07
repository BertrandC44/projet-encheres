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
	private static final String INSERT="INSERT INTO CATEGORIE (libelle) VALUES (:libelle)";
	private NamedParameterJdbcTemplate jdbcTemplate;

	public CategorieDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Categorie consulterCategorieParId(long id) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idCategorie", id);
		return this.jdbcTemplate.queryForObject(FIND_BY_ID, map, new BeanPropertyRowMapper<>(Categorie.class));
	}

	public void insertCategorieArticle(Categorie categorie) {
		MapSqlParameterSource map = new MapSqlParameterSource();
	
		map.addValue("libelle", categorie);
		this.jdbcTemplate.update(INSERT, map);
	}
	
	@Override
	public List<Categorie> consulterCategories() {
		return this.jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Categorie.class));
	}

}
