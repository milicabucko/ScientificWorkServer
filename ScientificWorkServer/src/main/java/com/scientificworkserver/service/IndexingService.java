package com.scientificworkserver.service;

import com.scientificworkserver.model.ScientificWork;
import org.springframework.web.multipart.MultipartFile;

import com.scientificworkserver.lucene.indexing.handler.DocumentHandler;

public interface IndexingService {
	
	ScientificWork addScientificWork(ScientificWork scientificWork, MultipartFile file);
	
	ScientificWork addIndexScientificWork(ScientificWork scientificWork);
	
	void deleteScientificWork(String filename);
	
	DocumentHandler getHandler(String filename);

}
