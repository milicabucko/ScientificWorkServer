package com.scientificworkserver.serviceimpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.scientificworkserver.model.ScientificWork;
import com.scientificworkserver.model.User;
import com.scientificworkserver.service.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.scientificworkserver.repository.ScientificWorkRepository;
import com.scientificworkserver.service.ScientificWorkService;
import com.scientificworkserver.service.IndexingService;

@Service
public class ScientificWorkServiceImpl implements ScientificWorkService {

	private final ScientificWorkRepository scientificWorkRepository;
	private final UserService userService;
	private final IndexingService indexingService;

	public ScientificWorkServiceImpl(ScientificWorkRepository scientificWorkRepository, UserService userService, IndexingService indexingService) {
		this.scientificWorkRepository = scientificWorkRepository;
		this.userService = userService;
		this.indexingService = indexingService;
	}

	@Override
	public ScientificWork createScientificWork(ScientificWork scientificWork) {
		ScientificWork savedScientificWork = scientificWorkRepository.save(scientificWork);
		if (scientificWork.getFilename() != null) {
			ScientificWork sWork = indexingService.getHandler(savedScientificWork.getFilename())
					.getScientificWork(new File(savedScientificWork.getFilename()));
			sWork.setTitle(savedScientificWork.getTitle());
			sWork.setKeywords(savedScientificWork.getKeywords());
			sWork.setAuthor(savedScientificWork.getAuthor());
			sWork.setLanguage(savedScientificWork.getLanguage());
			sWork.setMagazine(savedScientificWork.getMagazine());
			sWork.setCategory(savedScientificWork.getCategory());
			sWork.setId(savedScientificWork.getId());
			sWork.convertToLat();
			indexingService.addIndexScientificWork(sWork);
		}
		return savedScientificWork;
	}

	@Override
	public void removeScientificWork(Long id) {
		scientificWorkRepository.delete(id);
	}

	@Override
	public List<ScientificWork> findAllScientificWork() {
		return scientificWorkRepository.findAll();
	}


	@Override
	public ScientificWork findOneScientificWork(Long id) {
		return scientificWorkRepository.getOne(id);
	}

	@Override
	public ScientificWork uploadScientificWork(MultipartFile file) {

		User user = UserService.aktivanUser;

		ScientificWork scientificWork = new ScientificWork();
		scientificWork.setUser(user);
		scientificWork.setFilename(file.getOriginalFilename());
		// scientificWork = scientificWorkRepository.save(scientificWork);
		// Long id = scientificWork.getId();
		scientificWork = indexingService.addScientificWork(scientificWork, file);

		String baseDirectory = "D:/home";
		try {
			Files.createDirectories(Paths.get(baseDirectory, "scientificWorks"));
			Files.write(Paths.get(baseDirectory, "scientificWorks", file.getOriginalFilename()), file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		scientificWork.setFilename(file.getOriginalFilename());
		return scientificWork;
	}

	@Override
	public ResponseEntity<ByteArrayResource> downloadScientificWork(Long scientificWorkId) throws Exception {
		ScientificWork scientificWork = scientificWorkRepository.findOne(scientificWorkId);

		if (scientificWork == null) {
			System.out.println("Document doesn't exist");
			return null;
		}

		String baseDirectory = "D:/home/scientificWorks/";
		Path scientificWorkPath = Paths.get(baseDirectory, scientificWork.getFilename());
		
		byte[] scientificWorkContent = Files.readAllBytes(scientificWorkPath);
	    ByteArrayResource resource = new ByteArrayResource(scientificWorkContent);

	    return ResponseEntity.ok()
	            .contentLength(scientificWorkContent.length)
	            .contentType(MediaType.parseMediaType(scientificWork.getMime()))
	            .body(resource);
	}

	@Override
	public ScientificWork findOneScientificWorkByFilename(String filename) {
		return scientificWorkRepository.findScientificWorkByFilename(filename);
	}

	@Override
	public ScientificWork updateScientificWork(ScientificWork scientificWork) {
		ScientificWork e = scientificWorkRepository.findOne(scientificWork.getId());
		e.setAuthor(scientificWork.getAuthor());
		e.setKeywords(scientificWork.getKeywords());
		e.setLanguage(scientificWork.getLanguage());
		e.setTitle(scientificWork.getTitle());
		e.setPublicationYear(scientificWork.getPublicationYear());
		e.setMagazine(scientificWork.getMagazine());
		e.setCategory(scientificWork.getCategory());
		e.setOpenAccess(scientificWork.getOpenAccess());
		return scientificWorkRepository.save(e);
	}

}
