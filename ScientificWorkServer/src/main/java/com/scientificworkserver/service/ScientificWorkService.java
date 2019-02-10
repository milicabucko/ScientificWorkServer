package com.scientificworkserver.service;

import java.util.List;

import com.scientificworkserver.model.ScientificWork;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ScientificWorkService {
	
	ScientificWork createScientificWork(ScientificWork scientificWork);
	
	void removeScientificWork(Long id);
	
	List<ScientificWork> findAllScientificWork();
	
	ScientificWork findOneScientificWork(Long id);
	
	ScientificWork uploadScientificWork(MultipartFile file);
	
	ResponseEntity<ByteArrayResource> downloadScientificWork(Long scientificWorkId) throws Exception;
	
	ScientificWork findOneScientificWorkByFilename(String filename);
	
	ScientificWork updateScientificWork(ScientificWork scientificWork);

}
