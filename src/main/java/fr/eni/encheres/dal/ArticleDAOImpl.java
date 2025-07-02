package fr.eni.encheres.dal;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.encheres.bo.Article;

@Repository
public class ArticleDAOImpl implements ArticleDAO{
	private static final String FIND_ALL = "SELECT * FROM ARTICLE"; 
	private static final String FIND_BY_ID = "SELECT * FROM ARTICLE WHERE idArticle = :idArticle";
	private static final String CREATE_ARTICLE = "INSERT INTO ARTICLE (nomArticle, description, dateDebutEncheres, dateFinEncheres, miseAPrix, prixVente, etatVente, idCategorie, idUtilisateur) VALUES "
			+ "(:nomArticle, :decription, :dateDebutEncheres, :dateFinEncheres, :miseAPrix, :prixVente, :etatVente, :idCategorie, :idUtilisateur)";
	private static final String DELETE_ARTICLE = "DELETE FROM ARTICLE WHERE idArticle = :idArticle";
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	
	public ArticleDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	
	@Override
	public List<Article> consulterArticles() {
		return this.jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Article.class));
	}

	@Override
	public Article consulterArticleParId(long id) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", id);
		return this.jdbcTemplate.queryForObject(FIND_BY_ID, map, new BeanPropertyRowMapper<>(Article.class));
	}

	@Override
	public void creerVente(Article article) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("nomArticle", article.getNomArticle());
		map.addValue("description", article.getDescription());
		map.addValue("dateDebutEncheres", article.getDateDebutEncheres());
		map.addValue("dateFinEnchere", article.getDateFinEncheres());
		map.addValue("miseAPrix", article.getMiseAPrix());
		map.addValue("prixVente", article.getPrixVente());
		map.addValue("etatVente", article.getEtatVente());
		map.addValue("idCategorie", article.getCategorie().getIdCategorie());
		map.addValue("idUtilisateur", article.getUtilisateur().getIdUtilisateur());
		this.jdbcTemplate.update(CREATE_ARTICLE, map);
		
	}

	@Override
	public void annulerVente(Article article) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", article.getIdArticle());
		this.jdbcTemplate.update(DELETE_ARTICLE, map);
		
	}

}
