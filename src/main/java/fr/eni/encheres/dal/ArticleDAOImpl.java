package fr.eni.encheres.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Retrait;
import fr.eni.encheres.bo.Utilisateur;

/**
 * Implémentation du DAO pour la gestion des articles.
 * Gère les opérations de création, consultation, suppression et recherches spécifiques.
 */
@Repository
public class ArticleDAOImpl implements ArticleDAO {


	private static final String FIND_ALL = "SELECT * FROM ARTICLE";
	private static final String CREATE_ARTICLE = "INSERT INTO ARTICLE (nomArticle, description, dateDebutEncheres, dateFinEncheres, miseAPrix, prixVente, etatVente, idCategorie, idUtilisateur, imageArticle) VALUES "
			+ "(:nomArticle, :description, :dateDebutEncheres, :dateFinEncheres, :miseAPrix, :prixVente, :etatVente, :idCategorie, :idUtilisateur, :imageArticle)";
	private static final String FIND_BY_ID = "SELECT * FROM ARTICLE a JOIN RETRAIT r ON a.idArticle = r.idArticle JOIN UTILISATEUR u ON a.idUtilisateur=u.idUtilisateur WHERE a.idArticle = :idArticle";
	private static final String DELETE_ARTICLE = "DELETE FROM ARTICLE WHERE idArticle = :idArticle";
	// private static final String RETRAIT_UTILISATEUR = "SELECT a.*, u.pseudo,
	// r.rue, r.ville, r.codePostal FROM ARTICLE a JOIN UTILISATEUR u ON
	// a.idUtilisateur = u.idUtilisateur LEFT JOIN RETRAIT r ON r.idArticle =
	// a.idArticle";
	private static final String FIND_BY_ID_USER = "SELECT a.*, u.pseudo, r.rue, r.ville, r.codePostal FROM ARTICLE a JOIN RETRAIT r ON r.idArticle = a.idArticle JOIN UTILISATEUR u ON a.idUtilisateur = u.idUtilisateur WHERE u.idUtilisateur=:idUtilisateur";
	private static final String RETRAIT_UTILISATEUR = "SELECT a.*, u.pseudo, r.rue, r.ville, r.codePostal FROM ARTICLE a JOIN UTILISATEUR u ON a.idUtilisateur = u.idUtilisateur JOIN RETRAIT r ON r.idArticle = a.idArticle";
	private static final String FIND_ENCHERES_EN_COURS = "SELECT * FROM ARTICLE a JOIN RETRAIT r ON r.idArticle = a.idArticle JOIN UTILISATEUR u ON a.idUtilisateur = u.idUtilisateur WHERE dateDebutEncheres<=GETDATE() AND dateFinEncheres>GETDATE() AND a.idUtilisateur<>:idUtilisateur";
	private static final String FIND_MES_ENCHERES_EN_COURS = "  SELECT * FROM ARTICLE a JOIN UTILISATEUR u ON a.idUtilisateur = u.idUtilisateur JOIN RETRAIT r ON r.idArticle = a.idArticle JOIN ENCHERE e ON a.idArticle = e.idArticle\r\n"
			+ "  WHERE dateDebutEncheres<=GETDATE() AND dateFinEncheres>GETDATE() and e.idUtilisateur =:idUtilisateur";
	private static final String FIND_MES_ENCHERES_REMPORTEES = " SELECT TOP 1 * FROM ARTICLE a JOIN RETRAIT r ON r.idArticle = a.idArticle JOIN ENCHERE e ON a.idArticle = e.idArticle JOIN UTILISATEUR u ON a.idUtilisateur = u.idUtilisateur \r\n"
			+ "  WHERE dateFinEncheres<=GETDATE() and e.idUtilisateur =:idUtilisateur";
	private static final String FIND_MES_VENTES_EN_COURS = " SELECT * FROM ARTICLE a JOIN RETRAIT r ON r.idArticle = a.idArticle JOIN UTILISATEUR u ON a.idUtilisateur = u.idUtilisateur WHERE dateDebutEncheres<=GETDATE() AND dateFinEncheres>GETDATE() AND a.idUtilisateur=:idUtilisateur";
	private static final String FIND_MES_VENTES_A_VENIR = "SELECT * FROM ARTICLE a JOIN RETRAIT r ON r.idArticle = a.idArticle JOIN UTILISATEUR u ON a.idUtilisateur = u.idUtilisateur WHERE dateDebutEncheres>GETDATE() AND a.idUtilisateur=:idUtilisateur";
	private static final String FIND_MES_VENTES_TERMINEES = "SELECT * FROM ARTICLE a JOIN RETRAIT r ON r.idArticle = a.idArticle  JOIN UTILISATEUR u ON a.idUtilisateur = u.idUtilisateur WHERE dateFinEncheres<=GETDATE() AND a.idUtilisateur=:idUtilisateur";
	private static final String FIND_BY_IDCATEGORIE = "SELECT * FROM ARTICLE a JOIN RETRAIT r ON r.idArticle = a.idArticle JOIN UTILISATEUR u ON a.idUtilisateur = u.idUtilisateur WHERE idCategorie=:idCategorie";
	private static final String FIND_BY_MOT_CLE = "SELECT * FROM ARTICLE a JOIN RETRAIT r ON r.idArticle = a.idArticle JOIN UTILISATEUR u ON a.idUtilisateur = u.idUtilisateur WHERE nomArticle LIKE :motCle";		

	//SELECT * FROM ARTICLE a JOIN RETRAIT r ON r.idArticle = a.idArticle WHERE a.idArticle =6

	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	/**
     * Constructeur avec injection du NamedParameterJdbcTemplate.
     * 
     * @param jdbcTemplate Template JDBC avec nommage de paramètres.
     */
	public ArticleDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
     * Retourne tous les articles sans détail utilisateur ou retrait.
     * 
     * @return Liste des articles.
     */
	@Override
	public List<Article> consulterArticles() {
		return this.jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Article.class));
	}

	/**
     * Retourne les articles avec informations de retrait et utilisateur.
     * 
     * @return Liste des articles enrichis.
     */
	@Override
	public List<Article> consulterArticlePseudo() {
		return jdbcTemplate.query(RETRAIT_UTILISATEUR, new ArticleRowMapper());
	}

	/**
     * Retourne un article par son ID, avec utilisateur inclus.
     * 
     * @param id Identifiant de l'article.
     * @return Article détaillé.
     */
	@Override
	public Article consulterArticleParId(long id) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", id);
		return this.jdbcTemplate.queryForObject(FIND_BY_ID, map, new ArticleTelRowMapper());
	}

	/**
     * Crée un nouvel article en base, et récupère sa clé auto-générée.
     * 
     * @param article Article à insérer.
     */
    @Override
    public void creerVente(Article article) {
    	KeyHolder keyHolder = new GeneratedKeyHolder();
 
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
        map.addValue("imageArticle", article.getImage());

        this.jdbcTemplate.update(CREATE_ARTICLE, map,keyHolder);
       
		

		if (keyHolder != null && keyHolder.getKey() != null) {
			// Mise à jour de l'identifiant du cours auto-généré par la base
			article.setIdArticle(keyHolder.getKey().longValue());
		}

	}

    /**
     * Supprime un article de la base.
     * 
     * @param article Article à supprimer.
     */
	@Override
	public void annulerVente(Article article) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", article.getIdArticle());
		this.jdbcTemplate.update(DELETE_ARTICLE, map);
	}

	 /**
     * Retourne la liste des articles dont les enchères sont en cours, sauf pour le vendeur.
     * 
     * @param idUtilisateur ID du vendeur à exclure.
     * @return Liste des articles ouverts aux enchères.
     */
	@Override
	public List<Article> consulterArticleEncheresEnCours(long idUtilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idUtilisateur", idUtilisateur);
		return jdbcTemplate.query(FIND_ENCHERES_EN_COURS, map, new ArticleRowMapper());
	}

	/**
     * Liste les articles sur lesquels l'utilisateur a enchéri et qui sont encore en cours.
     * 
     * @param idUtilisateur ID de l'acheteur.
     * @return Liste d'articles.
     */
    @Override
	public List<Article> consulterArticleMesEncheresEnCours(long idUtilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idUtilisateur", idUtilisateur);

		return jdbcTemplate.query(FIND_MES_ENCHERES_EN_COURS, map, new ArticleRowMapper());
	}
    /**
     * Liste les articles remportés par l'utilisateur.
     * 
     * @param idUtilisateur ID de l'acheteur.
     * @return Articles remportés.
     */
	@Override
	public List<Article> consulterArticleMesEncheresRemportees(long idUtilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idUtilisateur", idUtilisateur);

		return jdbcTemplate.query(FIND_MES_ENCHERES_REMPORTEES, map, new ArticleRowMapper());
	}

	/**
     * Liste les articles que l'utilisateur a mis en vente et dont les enchères sont en cours.
     * 
     * @param idUtilisateur ID du vendeur.
     * @return Liste des ventes en cours.
     */
	@Override
	public List<Article> consulterArticleMesVentesEnCours(long idUtilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idUtilisateur", idUtilisateur);
		return jdbcTemplate.query(FIND_MES_VENTES_EN_COURS, map, new ArticleRowMapper());
	}

	/**
     * Liste les ventes à venir pour l'utilisateur.
     * 
     * @param idUtilisateur ID du vendeur.
     * @return Liste des ventes futures.
     */
	@Override
	public List<Article> consulterArticleMesVentesFutures(long idUtilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idUtilisateur", idUtilisateur);
		return jdbcTemplate.query(FIND_MES_VENTES_A_VENIR, map, new ArticleRowMapper());
	}

	/**
     * Liste les ventes terminées de l'utilisateur.
     * 
     * @param idUtilisateur ID du vendeur.
     * @return Liste des ventes terminées.
     */
	@Override
	public List<Article> consulterArticleMesVentesTerminees(long idUtilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idUtilisateur", idUtilisateur);
		return jdbcTemplate.query(FIND_MES_VENTES_TERMINEES, map, new ArticleRowMapper());
	}
	
	/**
     * Liste les articles par catégorie.
     * 
     * @param idCategorie ID de la catégorie.
     * @return Liste des articles correspondants.
     */
	@Override
	public List<Article> consulterArticleParCategorie(long idCategorie) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idCategorie", idCategorie);
		return jdbcTemplate.query(FIND_BY_IDCATEGORIE, map, new ArticleRowMapper());
	}
	
	/**
     * Recherche d’articles par mot-clé sur le nom.
     * 
     * @param motCle Mot-clé recherché.
     * @return Liste filtrée des articles.
     */
	@Override
	public List<Article> consulterArticleParMotCle(String motCle) {
		String contient = "%" + motCle + "%";
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("motCle", contient);
		
		return jdbcTemplate.query(FIND_BY_MOT_CLE, map, new ArticleRowMapper());
	}
	
	
	/**
     * Mapper personnalisé pour transformer les lignes de résultats en objets Article.
     */
    // Garde une seule classe ArticleRowMapper corrigée
    class ArticleRowMapper implements RowMapper<Article> {

        @Override
        public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
            Article a = new Article();
            a.setIdArticle(rs.getLong("idArticle"));
            a.setNomArticle(rs.getString("nomArticle"));
            a.setDescription(rs.getString("description"));
            a.setDateDebutEncheres(rs.getDate("dateDebutEncheres").toLocalDate());
            a.setDateFinEncheres(rs.getDate("dateFinEncheres").toLocalDate());
            a.setMiseAPrix(rs.getInt("miseAPrix"));
            a.setPrixVente(rs.getInt("prixVente"));
            a.setEtatVente(rs.getInt("etatVente"));
            a.setImage(rs.getString("imageArticle"));

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
    
    // Garde une seule classe ArticleRowMapper corrigée
    class ArticleTelRowMapper implements RowMapper<Article> {
    	
    	@Override
    	public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
    		Article a = new Article();
    		a.setIdArticle(rs.getLong("idArticle"));
    		a.setNomArticle(rs.getString("nomArticle"));
    		a.setDescription(rs.getString("description"));
    		a.setDateDebutEncheres(rs.getDate("dateDebutEncheres").toLocalDate());
    		a.setDateFinEncheres(rs.getDate("dateFinEncheres").toLocalDate());
    		a.setMiseAPrix(rs.getInt("miseAPrix"));
    		a.setPrixVente(rs.getInt("prixVente"));
    		a.setEtatVente(rs.getInt("etatVente"));
    		a.setImage(rs.getString("imageArticle"));
    		
    		Categorie categorie = new Categorie();
    		categorie.setIdCategorie(rs.getInt("idCategorie"));
    		a.setCategorie(categorie);
    		
    		Utilisateur utilisateur = new Utilisateur();
    		utilisateur.setIdUtilisateur(rs.getInt("idUtilisateur"));
    		utilisateur.setPseudo(rs.getString("pseudo"));
    		utilisateur.setTelephone(rs.getString("telephone"));
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