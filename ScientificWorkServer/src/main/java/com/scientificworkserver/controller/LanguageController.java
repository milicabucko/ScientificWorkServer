package com.scientificworkserver.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scientificworkserver.model.Language;
import com.scientificworkserver.service.LanguageService;

@RestController
@RequestMapping("/api/languages")
public class LanguageController {

	private final LanguageService languageService;

	public LanguageController(LanguageService languageService) {
		this.languageService = languageService;
	}

	@GetMapping
	public ResponseEntity<List<Language>> getAllLanguages() {
		return new ResponseEntity<List<Language>>(languageService.findAllLanguages(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Language> getOneLanguage(@PathVariable("id") Long id) {
		return new ResponseEntity<Language>(languageService.findOneLanguage(id), HttpStatus.OK);
	}

}
