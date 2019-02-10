package com.scientificworkserver.controller;

import java.util.List;

import com.scientificworkserver.model.ScientificWork;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.scientificworkserver.service.ScientificWorkService;

@RestController
@RequestMapping("/api/works")
@CrossOrigin(origins = "http://localhost:4200")
public class ScientificWorkController {

	private final ScientificWorkService scientificWorkService;

	public ScientificWorkController(ScientificWorkService scientificWorkService) {
		this.scientificWorkService = scientificWorkService;
	}

	@PostMapping("/create")
	public ResponseEntity<ScientificWork> createScientificWork(@RequestBody ScientificWork scientificWork) {
		scientificWork = scientificWorkService.createScientificWork(scientificWork);
		return new ResponseEntity<ScientificWork>(scientificWork, HttpStatus.OK);
	}

	@PostMapping("/delete")
	public ResponseEntity<Void> deleteScientificWork(@RequestBody ScientificWork scientificWork) {
		scientificWorkService.removeScientificWork(scientificWork.getId());
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<ScientificWork> updateScientificWork(@RequestBody ScientificWork scientificWork) {
		scientificWork = scientificWorkService.updateScientificWork(scientificWork);
		return new ResponseEntity<ScientificWork>(scientificWork, HttpStatus.OK);
	}

	@GetMapping
	public List<ScientificWork> getAllScientificWork() {
		return scientificWorkService.findAllScientificWork();
	}

	@GetMapping("/{id}")
	public ScientificWork getOneScientificWork(@PathVariable("id") Long id) {
		return scientificWorkService.findOneScientificWork(id);
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = "multipart/form-data")
	public ResponseEntity<ScientificWork> uploadScientificWork(@RequestParam("file") MultipartFile file) {
		ScientificWork scientificWork = scientificWorkService.uploadScientificWork(file);
		return new ResponseEntity<ScientificWork>(scientificWork, HttpStatus.OK);
	}

	@GetMapping("/download/{scientificWorkId}")
	public ResponseEntity<ByteArrayResource> getFile(@PathVariable("scientificWorkId") Long scientificWorkId) throws Exception {
		return scientificWorkService.downloadScientificWork(scientificWorkId);
	}

}
