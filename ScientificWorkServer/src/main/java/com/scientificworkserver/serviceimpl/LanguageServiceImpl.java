package com.scientificworkserver.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.scientificworkserver.model.Language;
import com.scientificworkserver.repository.LanguageRepository;
import com.scientificworkserver.service.LanguageService;

@Service
public class LanguageServiceImpl implements LanguageService {
	
	private final LanguageRepository languageRepository;
	
	public LanguageServiceImpl(LanguageRepository languageRepository) {
		this.languageRepository = languageRepository;
	}
	
	@Override
	public List<Language> findAllLanguages() {
		return languageRepository.findAll();
	}

	@Override
	public Language findOneLanguage(Long id) {
		return languageRepository.getOne(id);
	}

}
