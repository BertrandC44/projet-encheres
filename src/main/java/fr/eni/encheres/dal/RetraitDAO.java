package fr.eni.encheres.dal;

import java.util.List;

import fr.eni.encheres.bo.Retrait;


public interface RetraitDAO {
	
	void creer(Retrait retrait, long idArticle);
	
	List<Retrait> consulterRetrait(long idArticle);
}
