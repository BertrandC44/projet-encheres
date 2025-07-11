package fr.eni.encheres.bll;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

	 String sauvegarderImage(String uploadDirectory, MultipartFile image) throws IOException;
}
