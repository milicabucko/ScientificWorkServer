package com.scientificworkserver.service;

import java.util.List;

import com.scientificworkserver.model.Language;

public interface LanguageService {
	
	List<Language> findAllLanguages();
	
	Language findOneLanguage(Long id);
	
}
