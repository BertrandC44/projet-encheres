package fr.eni.encheres.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Retrait;

/**
 * Implémentation du DAO pour les opérations liées aux retraits d'articles.
 * Utilise NamedParameterJdbcTemplate pour interagir avec la base de données.
 */
@Repository
public class RetraitDAOImpl implements RetraitDAO {

		private static final String FIND_BY_ID = "SELECT * FROM ARTICLE WHERE idArticle= :idArticle";
 
		private final String INSERT = "INSERT INTO RETRAIT(idArticle, rue, codePostal, ville) VALUES (:idArticle, :rue, :codePostal, :ville)";
		
		private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

		
	    /**
	     * Constructeur avec injection du NamedParameterJdbcTemplate.
	     * 
	     * @param namedParameterJdbcTemplate template JDBC à utiliser.
	     */
		public RetraitDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {

			this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		}

		 /**
	     * Récupère la liste des retraits liés à un article donné.
	     * 
	     * @param idArticle identifiant de l'article.
	     * @return liste des retraits associés.
	     */
		@Override
		public List<Retrait> consulterRetrait(long idArticle) {
			MapSqlParameterSource map = new MapSqlParameterSource();
			map.addValue("idArticle", idArticle);

			return namedParameterJdbcTemplate.query(FIND_BY_ID, map, new RetraitRowMapper());
		}
		
		 /**
	     * Mapper personnalisé pour transformer une ligne SQL en objet Retrait.
	     */
		class RetraitRowMapper implements RowMapper<Retrait>{
			@Override
			public Retrait mapRow(ResultSet rs, int rowNum) throws SQLException {
				Retrait r = new Retrait();
				r.setRue(rs.getString("rue"));
				r.setCodePostal(rs.getString("codePostal"));
				r.setVille(rs.getString("ville"));
		
				// Association pour l'article
				Article article = new Article();
				article.setIdArticle(rs.getLong("idArticle"));
				
				r.setArticle(article);
				
				return r;
			}
		}
		
		/**
	     * Crée une entrée de retrait pour un article dans la base de données.
	     * 
	     * @param retrait objet Retrait contenant les informations.
	     * @param idArticle identifiant de l'article concerné.
	     */
			@Override
			public void creer(Retrait retrait, long idArticle) {
				MapSqlParameterSource map = new MapSqlParameterSource();			
				map.addValue("idArticle", idArticle);
				map.addValue("rue", retrait.getRue());
				map.addValue("codePostal", retrait.getCodePostal());
				map.addValue("ville", retrait.getVille());
				
				namedParameterJdbcTemplate.update(INSERT, map);

			}

		
}

