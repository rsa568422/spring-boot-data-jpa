package com.bolsadeideas.springboot.app.models.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.bolsadeideas.springboot.app.models.service.IUploadFileService;

@Service
public class UploadFileService implements IUploadFileService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final static String UPLOADS_FOLDER = "uploads";

	@Override
	public void init() throws IOException {
		this.deleteAll();
		Files.createDirectory(Paths.get(UPLOADS_FOLDER));
	}

	@Override
	public Resource load(String filename) throws MalformedURLException {
		Path pathPhoto = this.getPath(filename).toAbsolutePath();

		this.log.info("pathPhoto: ".concat(pathPhoto.toString()));

		Resource resource = null;
		resource = new UrlResource(pathPhoto.toUri());

		if (!resource.exists() || !resource.isReadable()) {
			throw new RuntimeException("Error: no se puede cargar la imagen ".concat(pathPhoto.toString()));
		}

		return resource;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		String fileName = String.format("%s_%s", UUID.randomUUID().toString().toUpperCase(),
				file.getOriginalFilename().toLowerCase().trim().replace(" ", "_"));

		Path rootPath = this.getPath(fileName);

		this.log.info("rootPath: ".concat(rootPath.toString()));

		Files.copy(file.getInputStream(), rootPath);

		return fileName;
	}

	@Override
	public boolean delete(String filename) {
		File file = this.getPath(filename).toFile();

		return file.exists() && file.canRead() && file.delete();
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());
	}

	private Path getPath(String filename) {
		return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
	}

}
