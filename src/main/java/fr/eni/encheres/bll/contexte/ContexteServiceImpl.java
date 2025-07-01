package fr.eni.encheres.bll.contexte;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dal.UtilisateurDAO;


@Service
@Primary
public class ContexteServiceImpl implements ContexteService {
	
	private UtilisateurDAO utilisateurDao;
	
	
	
	/**
	 * @param utilisateurDao
	 */
	public ContexteServiceImpl(UtilisateurDAO utilisateurDao) {
		this.utilisateurDao = utilisateurDao;
	}



	@Override
	public Utilisateur charger(String email) {
		// TODO Auto-generated method stub
		return this.utilisateurDao.findByEmail(email);
	}

}
