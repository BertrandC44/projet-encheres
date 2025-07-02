package fr.eni.encheres.dal;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fr.eni.encheres.bo.Utilisateur;

@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO{


	private static final String FIND_ALL = "SELECT * FROM UTILISATEUR";
	private static final String FIND_BY_ID = "SELECT * FROM UTILISATEUR WHERE idUtilisateur=:idUtilisateur";
	private static final String CREATE_UTILISATEUR = "INSERT INTO UTILISATEUR(pseudo,nom,prenom,email,telephone,rue,codePostal,ville,motDePasse,credit,administrateur) VALUES (:pseudo,:nom,:prenom,:email,:telephone,:rue,:codePostal,:ville,:motDePasse,100,0)";
	private static final String DELETE_BY_ID = "DELETE FROM UTILISATEUR WHERE idUtilisateur=:idUtilisateur";
	private static final String FIND_CREDIT = "SELECT credit FROM UTILISATEUR WHERE idUtilisateur=:idUtilisateur";
	private static final String UPDATE_CREDIT = "UPDATE UTILISATEUR SET credit =:credit WHERE idUtilisateur =:idUtilisateur ";
	private static final String COUNT_EMAIL = "SELECT COUNT(email) FROM UTILISATEUR WHERE email =:email";
	private static final String COUNT_ID = "SELECT COUNT(*) FROM UTILISATEUR WHERE idUtilisateur =:idUtilisateur";
	private static final String IS_ADMIN = "SELECT administrateur FROM UTILISATEUR WHERE idUtilisateur =:idUtilisateur";
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public UtilisateurDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public List<Utilisateur> consulterUtilisateurs() {
		return namedParameterJdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Utilisateur.class));
	}

	@Override
	public Utilisateur utilisateurparId(long idUtilisateur) {

		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idUtilisateur", idUtilisateur);
		
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, map, new BeanPropertyRowMapper<>(Utilisateur.class));
	}
	
	@Override
	public Utilisateur utilisateurparEmail(String email) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("email", email);
		
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, map, new BeanPropertyRowMapper<>(Utilisateur.class));
	}

	@Override
	public void creerUtilisateur(Utilisateur utilisateur) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("pseudo", utilisateur.getPseudo());
		map.addValue("nom", utilisateur.getNom());
		map.addValue("prenom", utilisateur.getPrenom());
		map.addValue("email", utilisateur.getEmail());
		map.addValue("telephone", utilisateur.getTelephone());
		map.addValue("rue", utilisateur.getRue());
		map.addValue("codePostal", utilisateur.getCodePostal());
		map.addValue("ville", utilisateur.getVille());
		map.addValue("motDePasse", utilisateur.getMotDePasse());
		map.addValue("credit", utilisateur.getCredit());
		map.addValue("administrateur", utilisateur.isAdmin());
		
		namedParameterJdbcTemplate.update(CREATE_UTILISATEUR, map, keyHolder);
		
		if (keyHolder != null && keyHolder.getKey() != null) {
			// Mise à jour de l'identifiant du cours auto-généré par la base
			utilisateur.setIdUtilisateur(keyHolder.getKey().longValue());
		}
		
	}

	@Override
	public void supprimerUtilisateur(Utilisateur utilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idUtilisateur", utilisateur.getIdUtilisateur());
		
		namedParameterJdbcTemplate.update(DELETE_BY_ID, map);
	}

	@Override
	public int consulterCredit(Utilisateur utilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idUtilisateur",utilisateur.getIdUtilisateur());
		
		return namedParameterJdbcTemplate.queryForObject(FIND_CREDIT, map, new BeanPropertyRowMapper<>(Integer.class));
	}
	

	@Override
	public void majCredit(int credit) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("credit", credit);
		
		namedParameterJdbcTemplate.update(UPDATE_CREDIT, map);
		
	}

	@Override
	public boolean isEmailValide(String email) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("email", email);
		
		Integer nbEmail = namedParameterJdbcTemplate.queryForObject(COUNT_EMAIL, map, Integer.class);
		return nbEmail ==0 ;
	}

	@Override
	public boolean isExist(long idUtilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idUtilisateur", idUtilisateur);
		
		Integer nbCompte = namedParameterJdbcTemplate.queryForObject(COUNT_ID, map, Integer.class);
		return nbCompte ==1;
	}

	@Override
	public boolean isAdmin(long idUtilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idUtilisateur", idUtilisateur);
		
		Integer isAdmin = namedParameterJdbcTemplate.queryForObject(IS_ADMIN, map, Integer.class);
		
		return isAdmin == 1;
	}
	
	
	


}
