package com.scientificworkserver.controller;

import java.util.ArrayList;
import java.util.List;

import com.scientificworkserver.model.ScientificWork;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.scientificworkserver.lucene.model.AdvancedQuery;
import com.scientificworkserver.lucene.model.RequiredHighlight;
import com.scientificworkserver.lucene.model.SearchType;
import com.scientificworkserver.lucene.model.SimpleQuery;
import com.scientificworkserver.lucene.search.QueryBuilder;
import com.scientificworkserver.lucene.search.ResultRetriever;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "http://localhost:4200")
public class SearchController {

	private final ResultRetriever resultRetriever;

	public SearchController(ResultRetriever resultRetriever) {
		this.resultRetriever = resultRetriever;
	}

	@PostMapping(value = "/term", consumes = "application/json")
	public ResponseEntity<List<ScientificWork>> searchTermQuery(@RequestBody SimpleQuery simpleQuery) throws Exception {
		org.elasticsearch.index.query.QueryBuilder query = QueryBuilder.buildQuery(SearchType.regular,
				simpleQuery.getField(), simpleQuery.getValue());
		List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
		rh.add(new RequiredHighlight(simpleQuery.getField(), simpleQuery.getValue()));
		List<ScientificWork> results = resultRetriever.getResults(query, rh);
		ArrayList<ScientificWork> resultsCopy = getScientificWorks(results);
		return new ResponseEntity<List<ScientificWork>>(resultsCopy, HttpStatus.OK);
	}

	@PostMapping(value = "/fuzzy", consumes = "application/json")
	public ResponseEntity<List<ScientificWork>> searchFuzzy(@RequestBody SimpleQuery simpleQuery) throws Exception {
		org.elasticsearch.index.query.QueryBuilder query = QueryBuilder.buildQuery(SearchType.fuzzy,
				simpleQuery.getField(), simpleQuery.getValue());
		List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
		rh.add(new RequiredHighlight(simpleQuery.getField(), simpleQuery.getValue()));
		List<ScientificWork> results = resultRetriever.getResults(query, rh);
		return new ResponseEntity<List<ScientificWork>>(results, HttpStatus.OK);
	}

	@PostMapping(value = "/phrase", consumes = "application/json")
	public ResponseEntity<List<ScientificWork>> searchPhrase(@RequestBody SimpleQuery simpleQuery) throws Exception {
		org.elasticsearch.index.query.QueryBuilder query = QueryBuilder.buildQuery(SearchType.phrase,
				simpleQuery.getField(), simpleQuery.getValue());
		List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
		rh.add(new RequiredHighlight(simpleQuery.getField(), simpleQuery.getValue()));
		List<ScientificWork> results = resultRetriever.getResults(query, rh);
		ArrayList<ScientificWork> resultsCopy = getScientificWorks(results);
		return new ResponseEntity<List<ScientificWork>>(resultsCopy, HttpStatus.OK);
	}

	@PostMapping(value = "/boolean", consumes = "application/json")
	public ResponseEntity<List<ScientificWork>> searchBoolean(@RequestBody AdvancedQuery advancedQuery) throws Exception {
		org.elasticsearch.index.query.QueryBuilder query1 = QueryBuilder.buildQuery(SearchType.regular,
				advancedQuery.getField1(), advancedQuery.getValue1());
		org.elasticsearch.index.query.QueryBuilder query2 = QueryBuilder.buildQuery(SearchType.regular,
				advancedQuery.getField2(), advancedQuery.getValue2());

		BoolQueryBuilder builder = QueryBuilders.boolQuery();
		if (advancedQuery.getOperation().equalsIgnoreCase("AND")) {
			builder.must(query1);
			builder.must(query2);
		} else if (advancedQuery.getOperation().equalsIgnoreCase("OR")) {
			builder.should(query1);
			builder.should(query2);
		} else if (advancedQuery.getOperation().equalsIgnoreCase("NOT")) {
			builder.must(query1);
			builder.mustNot(query2);
		}

		List<RequiredHighlight> rh = new ArrayList<RequiredHighlight>();
		rh.add(new RequiredHighlight(advancedQuery.getField1(), advancedQuery.getValue1()));
		rh.add(new RequiredHighlight(advancedQuery.getField2(), advancedQuery.getValue2()));
		List<ScientificWork> results = resultRetriever.getResults(builder, rh);
		ArrayList<ScientificWork> resultsCopy = getScientificWorks(results);
		return new ResponseEntity<List<ScientificWork>>(resultsCopy, HttpStatus.OK);
	}

	private ArrayList<ScientificWork> getScientificWorks(List<ScientificWork> results) {
		ArrayList<ScientificWork> resultsCopy = new ArrayList<>();
		resultsCopy.addAll(results);
		for (ScientificWork sw : results) {
			ArrayList<ScientificWork> sameDocuments = new ArrayList<>();
			for (ScientificWork swc : resultsCopy) {
				if (sw.getId().equals(swc.getId())) {
					sameDocuments.add(swc);
				}
			}
			if (sameDocuments.size() == 2) {
				ScientificWork sw1 = sameDocuments.get(0);
				ScientificWork sw2 = sameDocuments.get(1);
				if (sw1.getHighlight() != null) {
					resultsCopy.remove(sw2);
				}
				else if (sw2.getHighlight() != null) {
					resultsCopy.remove(sw1);
				}
				else {
					resultsCopy.remove(sw2);
				}
				sameDocuments.clear();
			}

		}
		return resultsCopy;
	}

}
