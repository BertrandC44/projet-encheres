package fr.eni.encheres.dal;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.encheres.bo.Utilisateur;

@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO{

	private static final String FIND_ALL = "SELECT * FROM UTILISATEURS";
	private static final String FIND_BY_ID = "SELECT * FROM UTILISATEURS WHERE idUtilisateur=:idUtilisateur";
	private static final String FIND_BY_EMAIL = "SELECT * FROM UTILISATEURS WHERE email=:email";
	private static final String CREATE_UTILISATEUR = "INSERT INTO UTILISATEUR(pseudo,nom,prenom,email,telephone,rue,codePostal,ville,motDePasse,credit,administrateur) VALUES (:pseudo,:nom,:prenom,:email,:telephone,:rue,:codePostal,:ville,:motDePasse,200,:administrateur)";
	
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public UtilisateurDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public List<Utilisateur> findAll() {
		return namedParameterJdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Utilisateur.class));
	}

	@Override
	public Utilisateur findById(long idUtilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idUtilisateur", idUtilisateur);
		
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, map, new BeanPropertyRowMapper<>(Utilisateur.class));
	}
	
	@Override
	public Utilisateur findByEmail(String email) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("email", email);
		
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, map, new BeanPropertyRowMapper<>(Utilisateur.class));
	}

	@Override
	public void createUtilisateur(Utilisateur utilisateur) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void supprimerUtilisateur(Utilisateur utilisateur) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void crediter(int credit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void debiter(int debit) {
		// TODO Auto-generated method stub
		
	}

}
