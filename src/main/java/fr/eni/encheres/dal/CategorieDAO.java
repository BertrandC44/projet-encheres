package fr.eni.encheres.dal;

import java.util.List;

import fr.eni.encheres.bo.Categorie;

public interface CategorieDAO {
	
	Categorie consulterCategorieParId(long id);

	List<Categorie> consulterCategories();
	
	
}
