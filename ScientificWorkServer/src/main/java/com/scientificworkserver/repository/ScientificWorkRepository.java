package com.scientificworkserver.repository;

import java.util.List;

import com.scientificworkserver.model.ScientificWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ScientificWorkRepository extends JpaRepository<ScientificWork, Long>{
	
	ScientificWork findScientificWorkById(Long id);
	
	ScientificWork findScientificWorkByFilename(String filename);

	@Query("Select eb from ScientificWork eb where eb.user.id = ?1")
	List<ScientificWork> findScientificWorkByUser(Long id);
	
}
