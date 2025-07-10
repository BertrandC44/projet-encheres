package fr.eni.encheres.bll;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService {
	
	public ImageServiceImpl() {
	}
 
	@Override
	public String sauvegarderImage(String uploadDirectory, MultipartFile image) throws IOException {
		String uniqueFileName = UUID.randomUUID().toString() + ".jpg";
 
		Path uploadPath = Path.of(uploadDirectory);
		Path filePath = uploadPath.resolve(uniqueFileName);
 
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
 
		Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
 
		return uniqueFileName;
	}
 


}
