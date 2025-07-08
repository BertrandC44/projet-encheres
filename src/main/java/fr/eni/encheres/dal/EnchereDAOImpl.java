package fr.eni.encheres.dal;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.validator.cfg.context.ReturnValueConstraintMappingContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import fr.eni.encheres.bo.Enchere;


@Repository
public class EnchereDAOImpl implements EnchereDAO {
	
	private static final String FIND_ALL = "SELECT * FROM ENCHERE";
	private static final String FIND_BY_ID = "SELECT * FROM ENCHERE WHERE idArticle = :idArticle";
	private static final String FIND_ID_VENDEUR = "SELECT idUtilisateur FROM ARTICLE WHERE idArticle = :idArticle";

	private static final String FIND_MONTANT_MAX = "SELECT MAX(montantEnchere) from ENCHERE WHERE idArticle=:idArticle";
	private static final String FIND_MONTANT_SECOND = "SELECT montantEnchere FROM ENCHERE WHERE idArticle = :idArticle ORDER BY montantEnchere DESC OFFSET 1 ROW FETCH NEXT 1 ROWS ONLY";
	private static final String FIND_UTILISATEUR_MAX = "SELECT U.pseudo	FROM ENCHERE E JOIN UTILISATEUR U ON E.idUtilisateur = U.idUtilisateur WHERE E.idArticle = :idArticle AND E.montantEnchere = (SELECT MAX(montantEnchere) FROM ENCHERE WHERE idArticle = :idArticle)";
	private static final String FIND_IDUTILISATEUR_MAX = "SELECT U.idUtilisateur FROM ENCHERE E JOIN UTILISATEUR U ON E.idUtilisateur = U.idUtilisateur WHERE E.idArticle = :idArticle AND E.montantEnchere = (SELECT MAX(montantEnchere) FROM ENCHERE WHERE idArticle = :idArticle)";
	private static final String FIND_IDUTILISATEUR_SECOND = "SELECT idUtilisateur FROM ENCHERE WHERE idArticle = :idArticle ORDER BY montantEnchere DESC OFFSET 1 ROW FETCH NEXT 1 ROWS ONLY";
	private static final String COUNT_ENCHERE = "SELECT count(idEnchere) FROM ENCHERE WHERE idArticle = :idArticle";

	private static final String FIND_CATEGORIE = "SELECT C.libelle FROM CATEGORIE C JOIN ARTICLE A  ON A.idCategorie = C.idCategorie WHERE A.idArticle = :idArticle";
	
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

	
	@Override
	public int montantEnchereMax(long idArticle) {
		MapSqlParameterSource map= new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		Integer montant = this.namedParameterJdbcTemplate.queryForObject(FIND_MONTANT_MAX, map, Integer.class);
		//pour gérer s'il n'y a pas d'enchère (création de nouvel article)
		return montant != null ? montant : 0;
	}
 
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

	
	public int nbEnchere(long idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		return this.namedParameterJdbcTemplate.queryForObject(COUNT_ENCHERE, map, Integer.class);
	}
	

	@Override
	public long idUtilisateurMontantMax(long idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		return (long)this.namedParameterJdbcTemplate.queryForObject(FIND_IDUTILISATEUR_MAX, map, Integer.class);
	}
	
	@Override
	public long idUtilisateurARecrediter(long idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		return (long)this.namedParameterJdbcTemplate.queryForObject(FIND_IDUTILISATEUR_SECOND, map, Integer.class);
	}

	@Override
	public String categorieArticle(long idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		return this.namedParameterJdbcTemplate.queryForObject(FIND_CATEGORIE, map, String.class);
	}

	@Override
	public int recrediter(long idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		return this.namedParameterJdbcTemplate.queryForObject(FIND_MONTANT_SECOND, map, Integer.class);
	}
	
	public long idUtilisateurVendeur(long idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		return this.namedParameterJdbcTemplate.queryForObject(FIND_ID_VENDEUR, map, Integer.class);
	}

}

