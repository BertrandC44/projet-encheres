package fr.eni.encheres.bll.contexte;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dal.UtilisateurDAO;


@Service
@Primary
public class ContexteServiceImpl implements ContexteService {
	
	private UtilisateurDAO utilisateurDao;
	
	public ContexteServiceImpl(UtilisateurDAO utilisateurDao) {
		this.utilisateurDao = utilisateurDao;
	}

	/**
     * Charge un utilisateur en fonction de son pseudo.
     * 
     * @param pseudo le nom d'utilisateur
     * @return l'utilisateur correspondant, ou {@code null} si aucun n'est trouvé
     */
	@Override
	public Utilisateur charger(String pseudo) {
		return this.utilisateurDao.utilisateurParPseudo(pseudo);
	}

}
