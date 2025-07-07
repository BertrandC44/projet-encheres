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
	private static final String FIND_BY_PSEUDO = "SELECT * FROM UTILISATEUR WHERE pseudo=:pseudo";
	private static final String CREATE_UTILISATEUR = "INSERT INTO UTILISATEUR(pseudo,nom,prenom,email,telephone,rue,codePostal,ville,motDePasse,credit,administrateur) VALUES (:pseudo,:nom,:prenom,:email,:telephone,:rue,:codePostal,:ville,:motDePasse,100,0)";
	private static final String UPDATE_UTILISATEUR = "UPDATE UTILISATEUR SET pseudo = :pseudo, nom = :nom , prenom = :prenom, email = :email, telephone = :telephone, rue = :rue, codePostal = :codePostal, ville = :ville, motDePasse = :motDePasse WHERE idUtilisateur = :idUtilisateur";
	private static final String DELETE_BY_ID = "DELETE FROM UTILISATEUR WHERE idUtilisateur=:idUtilisateur";
	private static final String FIND_CREDIT = "SELECT credit FROM UTILISATEUR WHERE idUtilisateur=:idUtilisateur";
	private static final String UPDATE_CREDIT = "UPDATE UTILISATEUR SET credit =:credit WHERE idUtilisateur =:idUtilisateur ";
	private static final String COUNT_EMAIL = "SELECT COUNT(email) FROM UTILISATEUR WHERE email =:email";
	private static final String COUNT_PSEUDO = "SELECT COUNT(pseudo) FROM UTILISATEUR WHERE pseudo =:pseudo";
	private static final String COUNT_ID = "SELECT COUNT(*) FROM UTILISATEUR WHERE idUtilisateur =:idUtilisateur";
	private static final String IS_ADMIN = "SELECT administrateur FROM UTILISATEUR WHERE idUtilisateur =:idUtilisateur";
	private static final String FIND_MDP_BY_PSEUDO = "SELECT motDePasse FROM UTILISATEUR WHERE pseudo=:pseudo";
	
	
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
	public Utilisateur utilisateurParPseudo(String pseudo) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("pseudo", pseudo);

		return namedParameterJdbcTemplate.queryForObject(FIND_BY_PSEUDO, map, new BeanPropertyRowMapper<>(Utilisateur.class));
	}
	
	@Override
	public void modifierUtilisateur(Utilisateur utilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idUtilisateur", utilisateur.getIdUtilisateur());
		map.addValue("pseudo", utilisateur.getPseudo());
		map.addValue("nom", utilisateur.getNom());
		map.addValue("prenom", utilisateur.getPrenom());
		map.addValue("email", utilisateur.getEmail());
		map.addValue("telephone", utilisateur.getTelephone());
		map.addValue("rue", utilisateur.getRue());
		map.addValue("codePostal", utilisateur.getCodePostal());
		map.addValue("ville", utilisateur.getVille());
		map.addValue("motDePasse", utilisateur.getMotDePasse());
		this.namedParameterJdbcTemplate.update(UPDATE_UTILISATEUR, map);
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
		
		return namedParameterJdbcTemplate.queryForObject(FIND_CREDIT, map, Integer.class);

	}
	



	@Override
	public void majCredit(int credit, long idUtilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("credit", credit);
		map.addValue("idUtilisateur", idUtilisateur);
		
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
	public boolean isPseudoValide(String pseudo) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("pseudo", pseudo);
		
		Integer nbPseudo = namedParameterJdbcTemplate.queryForObject(COUNT_PSEUDO, map, Integer.class);
		return nbPseudo == 0 ;
	}

	@Override
	public boolean isExist(String pseudo) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("pseudo", pseudo);
		
		Integer nbCompte = namedParameterJdbcTemplate.queryForObject(COUNT_PSEUDO, map, Integer.class);
		return nbCompte ==1;
	}

	@Override
	public boolean isAdmin(long idUtilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idUtilisateur", idUtilisateur);
		
		Integer isAdmin = namedParameterJdbcTemplate.queryForObject(IS_ADMIN, map, Integer.class);
		
		return isAdmin == 1;
	}

	@Override
	public String consulterMdp(String pseudo) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("pseudo", pseudo);
		
		return namedParameterJdbcTemplate.queryForObject(FIND_MDP_BY_PSEUDO, map, String.class);
	}



	
	/*class UtilisateurRowMapper implements RowMapper<Utilisateur>{

		@SuppressWarnings("unchecked")
		@Override
		public Utilisateur mapRow(ResultSet rs, int rowNum) throws SQLException {
			Utilisateur u = new Utilisateur();
			u.setPseudo(rs.getString("pseudo"));
			u.setNom(rs.getString("nom"));
			u.setPrenom(rs.getString("prenom"));
			u.setEmail(rs.getString("email"));
			
			Article article = new Article();
			article.setIdArticle(rs.getLong("idArticle"));
			
			u.setArticles((List<Article>) article);
			return u;
		}
		
	}*/
	
	


}
