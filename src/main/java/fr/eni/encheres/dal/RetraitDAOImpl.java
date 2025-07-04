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


@Repository
public class RetraitDAOImpl implements RetraitDAO {

		private static final String FIND_BY_ID = "SELECT * FROM ARTICLE WHERE idArticle= :idArticle";
 
		private final String INSERT = "INSERT INTO RETRAIT(idArticle, rue, codePostal, ville) VALUES (:idArticle, :rue, :codePostal, :ville)";
		
		private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

		public RetraitDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {

			this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		}

		@Override
		public List<Retrait> consulterRetrait(long idArticle) {
			MapSqlParameterSource map = new MapSqlParameterSource();
			map.addValue("idArticle", idArticle);

			return namedParameterJdbcTemplate.query(FIND_BY_ID, map, new RetraitRowMapper());
		}
		
		
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
		
			@Override
			public void creer(Retrait retrait, long idArticle) {
				MapSqlParameterSource map = new MapSqlParameterSource();			
				map.addValue("idArticle", retrait.getArticle().getIdArticle());
				map.addValue("rue", retrait.getRue());
				map.addValue("codePostal", retrait.getCodePostal());
				map.addValue("ville", retrait.getVille());
				
				namedParameterJdbcTemplate.update(INSERT, map);

			}

		
}

