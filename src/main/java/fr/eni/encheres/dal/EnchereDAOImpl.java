package fr.eni.encheres.dal;

import java.time.LocalDate;
import java.util.List;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.encheres.bo.Article;
import fr.eni.encheres.bo.Enchere;


@Repository
public class EnchereDAOImpl implements EnchereDAO {
	
	private static final String FIND_ALL = "SELECT * FROM ENCHERE";
	private static final String FIND_BY_ID = "SELECT * FROM ENCHERE WHERE idArticle = :idArticle";
	private static final String FIND_ID_VENDEUR = "SELECT idUtilisateur FROM ARTICLE WHERE idArticle = :idArticle";
	private static final String FIND_CREDIT_VENDEUR = "SELECT U.credit FROM UTILISATEUR U JOIN ARTICLE A ON A.idUtilisateur = U.idUtilisateur WHERE A.idUtilisateur = :idUtilisateur";

	private static final String FIND_MONTANT_MAX = "SELECT MAX(montantEnchere) from ENCHERE WHERE idArticle=:idArticle";
	private static final String FIND_MONTANT_SECOND = "SELECT montantEnchere FROM ENCHERE WHERE idArticle = :idArticle ORDER BY montantEnchere DESC OFFSET 1 ROW FETCH NEXT 1 ROWS ONLY";
	private static final String FIND_UTILISATEUR_MAX = "SELECT U.pseudo	FROM ENCHERE E JOIN UTILISATEUR U ON E.idUtilisateur = U.idUtilisateur WHERE E.idArticle = :idArticle AND E.montantEnchere = (SELECT MAX(montantEnchere) FROM ENCHERE WHERE idArticle = :idArticle)";
	private static final String FIND_IDUTILISATEUR_MAX = "SELECT U.idUtilisateur FROM ENCHERE E JOIN UTILISATEUR U ON E.idUtilisateur = U.idUtilisateur WHERE E.idArticle = :idArticle AND E.montantEnchere = (SELECT MAX(montantEnchere) FROM ENCHERE WHERE idArticle = :idArticle)";
	private static final String FIND_IDUTILISATEUR_SECOND = "SELECT idUtilisateur FROM ENCHERE WHERE idArticle = :idArticle ORDER BY montantEnchere DESC OFFSET 1 ROW FETCH NEXT 1 ROWS ONLY";
	private static final String COUNT_ENCHERE = "SELECT count(idEnchere) FROM ENCHERE WHERE idArticle = :idArticle";

	private static final String FIND_CATEGORIE = "SELECT C.libelle FROM CATEGORIE C JOIN ARTICLE A  ON A.idCategorie = C.idCategorie WHERE A.idArticle = :idArticle";
	
	private static final String INSERT_ENCHERE = "INSERT INTO ENCHERE (dateEnchere, montantEnchere, idUtilisateur, idArticle) VALUES (:dateEnchere, :montantEnchere, :idUtilisateur, :idArticle)";
	
	private static final String UPDATE_ETAT_VENTE = "UPDATE ARTICLE SET etatVente = 2 WHERE idArticle = :idArticle";
	
	private static final String DELETE_ENCHERE = "DELETE FROM ENCHERE WHERE idArticle = :idArticle";
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public EnchereDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	/**
     * Retourne toutes les enchères enregistrées.
     * 
     * @return une liste d'enchères
     */
	@Override
	public List<Enchere> consulterEncheres() {
		return namedParameterJdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Enchere.class));
	}
	
	/**
     * Récupère une enchère liée à un article spécifique.
     * 
     * @param idArticle identifiant de l'article
     * @return l'enchère correspondante
     */
	@Override
	public Enchere consulterEnchereParId(long idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, map, new BeanPropertyRowMapper<>(Enchere.class));
	}
	
	/**
     * Retourne le libellé de la catégorie de l’article.
     * 
     * @param idArticle identifiant de l'article
     * @return libellé de la catégorie
     */
	@Override
	public String categorieArticle(long idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		return this.namedParameterJdbcTemplate.queryForObject(FIND_CATEGORIE, map, String.class);
	}

	
	/**
     * Récupère le montant le plus élevé pour un article.
     * 
     * @param idArticle identifiant de l'article
     * @return montant maximum ou 0 si aucune enchère
     */
	@Override
	public int montantEnchereMax(long idArticle) {
		MapSqlParameterSource map= new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		Integer montant = this.namedParameterJdbcTemplate.queryForObject(FIND_MONTANT_MAX, map, Integer.class);
		//pour gérer s'il n'y a pas d'enchère (création de nouvel article)
		return montant != null ? montant : 0;
	}

	/**
     * Récupère le pseudo de l'utilisateur ayant fait l'enchère la plus haute.
     * 
     * @param idArticle identifiant de l'article
     * @return pseudo ou message par défaut si aucun enchérisseur
     */
	@Override
	public String utilisateurMontantMax(long idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		//try catch pour gérer s'il n'y a pas encore d'enchérisseur (création de nouvel article)
		try {
				return namedParameterJdbcTemplate.queryForObject(FIND_UTILISATEUR_MAX, map, String.class);
			}catch (EmptyResultDataAccessException e) {
		return "pas d'encherisseur";
		}

	}


	/**
     * Compte le nombre total d'enchères sur un article.
     * 
     * @param idArticle identifiant de l'article
     * @return nombre d'enchères
     */
	public int nbEnchere(long idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		return this.namedParameterJdbcTemplate.queryForObject(COUNT_ENCHERE, map, Integer.class);
	}

	/**
     * Retourne l'ID de l'utilisateur ayant mis l'enchère la plus haute.
     * 
     * @param idArticle identifiant de l'article
     * @return ID utilisateur
     */
	@Override
	public long idUtilisateurMontantMax(long idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);

		try {
		    return namedParameterJdbcTemplate.queryForObject(FIND_IDUTILISATEUR_MAX, map, Integer.class);
		} catch (EmptyResultDataAccessException e) {
		    return 0;  
		}
	}
 
	
	 /**
     * Retourne l'ID de l'utilisateur ayant mis la deuxième meilleure enchère.
     * 
     * @param idArticle identifiant de l'article
     * @return ID utilisateur
     */
	@Override
	public long idUtilisateurARecrediter(long idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		try {
		    return namedParameterJdbcTemplate.queryForObject(FIND_IDUTILISATEUR_SECOND, map, Integer.class);
		} catch (EmptyResultDataAccessException e) {
		    return 0;  
		}
	}

	/**
     * Ajoute une enchère dans la base de données.
     * 
     * @param montantEnchere montant proposé
     * @param idUtilisateur ID de l'utilisateur enchérisseur
     * @param idArticle ID de l'article concerné
     */
	@Override
	public void encherir(int montantEnchere, long idUtilisateur, long idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		LocalDate dateEnchere = LocalDate.now();
		map.addValue("dateEnchere", dateEnchere);
		map.addValue("montantEnchere", montantEnchere);
		map.addValue("idUtilisateur", idUtilisateur);
		map.addValue("idArticle", idArticle);
		System.out.println("INSERT avec idUtilisateur = " + idUtilisateur);
		this.namedParameterJdbcTemplate.update(INSERT_ENCHERE, map);

	}

	/**
     * Retourne le montant de la deuxième meilleure enchère.
     * 
     * @param idArticle identifiant de l'article
     * @return montant de l'enchère à recréditer
     */
	@Override
	public int recrediter(long idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		return this.namedParameterJdbcTemplate.queryForObject(FIND_MONTANT_SECOND, map, Integer.class);
	}


	@Override
	public long idUtilisateurVendeur(long idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		return this.namedParameterJdbcTemplate.queryForObject(FIND_ID_VENDEUR, map, Integer.class);
	}
	
	
	@Override
	public int creditUtilisateurVendeur(long idUtilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idUtilisateur", idUtilisateur);
		return this.namedParameterJdbcTemplate.queryForObject(FIND_CREDIT_VENDEUR, map, Integer.class);
	}
	
	/**
     * Met à jour l'état de vente d'un article (passage à l'état "vendu").
     * 
     * @param idArticle identifiant de l'article
     */
	@Override
	public void majEtatVente(long idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		this.namedParameterJdbcTemplate.update(UPDATE_ETAT_VENTE, map);
	}

	
    /**
     * Supprime Toutes les encheres de la base selon l'idArticle.
     * 
     * @param Enchères à supprimer.
     */
	@Override
	public void deleteEnchere(long idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		this.namedParameterJdbcTemplate.update(DELETE_ENCHERE, map);
		
	}
  
}

