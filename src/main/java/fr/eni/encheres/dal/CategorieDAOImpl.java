package fr.eni.encheres.dal;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.encheres.bo.Categorie;

/**
 * Implémentation du DAO pour la gestion des catégories.
 * Utilise NamedParameterJdbcTemplate pour exécuter des requêtes SQL nommées.
 */
@Repository
public class CategorieDAOImpl implements CategorieDAO{
	
	private static final String FIND_ALL ="SELECT * FROM CATEGORIE";
	private static final String FIND_BY_ID ="SELECT*FROM CATEGORIE WHERE idCategorie= :idCategorie";
	private static final String INSERT="INSERT INTO CATEGORIE (libelle) VALUES (:libelle)";
	private NamedParameterJdbcTemplate jdbcTemplate;

	/**
     * Constructeur avec injection du NamedParameterJdbcTemplate.
     * 
     * @param jdbcTemplate Template JDBC à utiliser.
     */
	public CategorieDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
     * Récupère une catégorie en base à partir de son identifiant.
     * 
     * @param id Identifiant de la catégorie.
     * @return Catégorie correspondante.
     */
	@Override
	public Categorie consulterCategorieParId(long id) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idCategorie", id);
		return this.jdbcTemplate.queryForObject(FIND_BY_ID, map, new BeanPropertyRowMapper<>(Categorie.class));
	}

	/**
     * Insère une nouvelle catégorie dans la base de données.
     * 
     * @param categorie La catégorie à insérer.
     */
	public void insertCategorieArticle(Categorie categorie) {
		MapSqlParameterSource map = new MapSqlParameterSource();
	
		map.addValue("libelle", categorie);
		this.jdbcTemplate.update(INSERT, map);
	}
	
	/**
     * Récupère la liste de toutes les catégories disponibles.
     * 
     * @return Liste des catégories.
     */
	@Override
	public List<Categorie> consulterCategories() {
		return this.jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Categorie.class));
	}

}
