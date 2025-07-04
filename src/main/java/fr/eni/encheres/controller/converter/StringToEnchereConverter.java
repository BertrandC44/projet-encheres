package fr.eni.encheres.controller.converter;

import org.springframework.stereotype.Component;

import org.springframework.core.convert.converter.Converter;
import fr.eni.encheres.bll.EncheresService;
import fr.eni.encheres.bo.Article;

@Component
public class StringToEnchereConverter implements Converter<String, Article> {
	
	private EncheresService encheresService;

	public StringToEnchereConverter(EncheresService encheresService) {

		this.encheresService = encheresService;
	}

	@Override
	public Article convert(String idArticle) {
		// TODO Auto-generated method stub
		return this.encheresService.consulterArticleParId(Long.parseLong(idArticle));
	}

}
