package fr.eni.encheres.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.incrementer.SybaseAnywhereMaxValueIncrementer;
import org.springframework.stereotype.Repository;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Retrait;
import fr.eni.encheres.bo.Utilisateur;

@Repository
public class ArticleDAOImpl implements ArticleDAO {

    private static final String FIND_ALL = "SELECT * FROM ARTICLE"; 

    private static final String FIND_BY_ID = "SELECT * FROM ARTICLE INNER JOIN UTILISATEUR ON ARTICLE.idUtilisateur=utilisateur.idUtilisateur WHERE idArticle = :idArticle";

    private static final String CREATE_ARTICLE = "INSERT INTO ARTICLE (nomArticle, description, dateDebutEncheres, dateFinEncheres, miseAPrix, prixVente, etatVente, idCategorie, idUtilisateur) VALUES "
            + "(:nomArticle, :description, :dateDebutEncheres, :dateFinEncheres, :miseAPrix, :prixVente, :etatVente, :idCategorie, :idUtilisateur)";

    private static final String DELETE_ARTICLE = "DELETE FROM ARTICLE WHERE idArticle = :idArticle";

    private static final String FIND_BY_ID_USER = "SELECT a.*, u.pseudo, r.rue, r.ville, r.codePostal FROM ARTICLE a JOIN UTILISATEUR u ON a.idUtilisateur = u.idUtilisateur JOIN RETRAIT r ON r.idArticle = a.idArticle WHERE u.idUtilisateur=:idUtilisateur";
    private static final String RETRAIT_UTILISATEUR = "SELECT a.*, u.pseudo, r.rue, r.ville, r.codePostal FROM ARTICLE a JOIN UTILISATEUR u ON a.idUtilisateur = u.idUtilisateur JOIN RETRAIT r ON r.idArticle = a.idArticle";

    private static final String FIND_ENCHERES_EN_COURS = "SELECT * FROM ARTICLE a JOIN UTILISATEUR u ON a.idUtilisateur = u.idUtilisateur JOIN RETRAIT r ON r.idArticle = a.idArticle WHERE dateDebutEncheres<=GETDATE() AND dateFinEncheres>GETDATE() AND a.idUtilisateur<>:idUtilisateur";
    
    private static final String FIND_MES_ENCHERES_EN_COURS= "  SELECT * FROM ARTICLE a JOIN UTILISATEUR u ON a.idUtilisateur = u.idUtilisateur JOIN RETRAIT r ON r.idArticle = a.idArticle JOIN ENCHERE e ON a.idArticle = e.idArticle\r\n"
    		+ "  WHERE dateDebutEncheres<=GETDATE() AND dateFinEncheres>GETDATE() and e.idUtilisateur =:idUtilisateur";
    
    private static final String FIND_MES_ENCHERES_REMPORTEES = " SELECT TOP 1 * FROM ARTICLE a JOIN UTILISATEUR u ON a.idUtilisateur = u.idUtilisateur JOIN RETRAIT r ON r.idArticle = a.idArticle JOIN ENCHERE e ON a.idArticle = e.idArticle\r\n"
    		+ "  WHERE dateFinEncheres<=GETDATE() and e.idUtilisateur =:idUtilisateur";
    
    private static final String FIND_MES_VENTES_EN_COURS = " SELECT * FROM ARTICLE a JOIN UTILISATEUR u ON a.idUtilisateur = u.idUtilisateur JOIN RETRAIT r ON r.idArticle = a.idArticle WHERE dateDebutEncheres<=GETDATE() AND dateFinEncheres>GETDATE() AND a.idUtilisateur=:idUtilisateur";
    
    
    private static final String FIND_MES_VENTES_A_VENIR = "SELECT * FROM ARTICLE a JOIN UTILISATEUR u ON a.idUtilisateur = u.idUtilisateur JOIN RETRAIT r ON r.idArticle = a.idArticle WHERE dateDebutEncheres>GETDATE() AND a.idUtilisateur=:idUtilisateur";
    
    private static final String FIND_MES_VENTES_TERMINEES = "SELECT * FROM ARTICLE a JOIN UTILISATEUR u ON a.idUtilisateur = u.idUtilisateur JOIN RETRAIT r ON r.idArticle = a.idArticle WHERE dateFinEncheres<=GETDATE() AND a.idUtilisateur=:idUtilisateur";
    
    
    
    private NamedParameterJdbcTemplate jdbcTemplate;

    public ArticleDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Article> consulterArticles() {
        return this.jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Article.class));
    }

    @Override
    public List<Article> consulterArticlePseudo() {
        return jdbcTemplate.query(RETRAIT_UTILISATEUR, new ArticleRowMapper());
    }
    
    


    @Override
    public Article consulterArticleParId(long id) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("idArticle", id);
        return this.jdbcTemplate.queryForObject(FIND_BY_ID, map, new ArticleRowMapper());
    }

    @Override
    public void creerVente(Article article) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("nomArticle", article.getNomArticle());
        map.addValue("description", article.getDescription()); // corrigé ici
        map.addValue("dateDebutEncheres", article.getDateDebutEncheres());
        map.addValue("dateFinEncheres", article.getDateFinEncheres());
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
    

	@Override
	public List<Article> consulterArticleEncheresEnCours(long idUtilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idUtilisateur", idUtilisateur);
		return jdbcTemplate.query(FIND_ENCHERES_EN_COURS, map, new ArticleRowMapper());
	}

	@Override
	public List<Article> consulterArticleMesEncheresEnCours(long idUtilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idUtilisateur", idUtilisateur);
		
		return jdbcTemplate.query(FIND_MES_ENCHERES_EN_COURS, map, new ArticleRowMapper());
	}

	@Override
	public List<Article> consulterArticleMesEncheresRemportees(long idUtilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idUtilisateur", idUtilisateur);
		
		return jdbcTemplate.query(FIND_MES_ENCHERES_REMPORTEES, map, new ArticleRowMapper());
	}

	@Override
	public List<Article> consulterArticleMesVentesEnCours(long idUtilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idUtilisateur", idUtilisateur);
		return jdbcTemplate.query(FIND_MES_VENTES_EN_COURS, map, new ArticleRowMapper());
	}

	@Override
	public List<Article> consulterArticleMesVentesFutures(long idUtilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idUtilisateur", idUtilisateur);
		return jdbcTemplate.query(FIND_MES_VENTES_A_VENIR, map, new ArticleRowMapper());
	}

	@Override
	public List<Article> consulterArticleMesVentesTerminees(long idUtilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idUtilisateur", idUtilisateur);
		return jdbcTemplate.query(FIND_MES_VENTES_TERMINEES, map, new ArticleRowMapper());
	}

    // Garde une seule classe ArticleRowMapper corrigée
    class ArticleRowMapper implements RowMapper<Article> {

        @Override
        public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
            Article a = new Article();
            a.setNomArticle(rs.getString("nomArticle"));
            a.setDescription(rs.getString("description"));
            a.setDateDebutEncheres(rs.getDate("dateDebutEncheres").toLocalDate());
            a.setDateFinEncheres(rs.getDate("dateFinEncheres").toLocalDate());
            a.setMiseAPrix(rs.getInt("miseAPrix"));
            a.setPrixVente(rs.getInt("prixVente"));
            a.setEtatVente(rs.getInt("etatVente"));

            Categorie categorie = new Categorie();
            categorie.setIdCategorie(rs.getInt("idCategorie"));
            a.setCategorie(categorie);

            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setIdUtilisateur(rs.getInt("idUtilisateur"));
            utilisateur.setPseudo(rs.getString("pseudo"));
            a.setUtilisateur(utilisateur);

            Retrait retrait = new Retrait();
            retrait.setRue(rs.getString("rue"));
            retrait.setVille(rs.getString("ville"));
            retrait.setCodePostal(rs.getString("codePostal"));
            a.setRetrait(retrait);

            // Supprimé la deuxième création de Utilisateur qui écrasait la première

            return a;
        }
    }

}