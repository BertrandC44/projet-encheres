package fr.eni.encheres.bll.contexte;

import fr.eni.encheres.bo.Utilisateur;

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
