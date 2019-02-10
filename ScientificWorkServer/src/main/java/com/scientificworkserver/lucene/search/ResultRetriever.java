package com.scientificworkserver.lucene.search;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.scientificworkserver.model.ScientificWork;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import com.scientificworkserver.lucene.model.RequiredHighlight;
import com.scientificworkserver.service.ScientificWorkService;

@Service
public class ResultRetriever {

	private final ElasticsearchTemplate template;
	private final ScientificWorkService scientificWorkService;

	public ResultRetriever(ScientificWorkService scientificWorkService, ElasticsearchTemplate template) {
		this.scientificWorkService = scientificWorkService;
		this.template = template;
	}

	public List<ScientificWork> getResults(org.elasticsearch.index.query.QueryBuilder query,
                                           List<RequiredHighlight> requiredHighlights) {
		if (query == null) {
			return null;
		}

		List<ScientificWork> results = new ArrayList<ScientificWork>();

		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(query).build();

		List<ScientificWork> scientificWorks = template.queryForList(searchQuery, ScientificWork.class);

		for (ScientificWork scientificWork : scientificWorks) {
			String[] tempArray = scientificWork.getFilename().split(Pattern.quote("\\"));
			String temp = tempArray[tempArray.length - 1];
			ScientificWork resultScientificWork = scientificWorkService.findOneScientificWorkByFilename(temp);
			try {
				if (resultScientificWork != null) {
					shouldReturnHighlightedFieldsForGivenQueryAndFields(query, scientificWork.getId());
				}
			} catch (Exception e) {
			}

			if (resultScientificWork != null) {
				results.add(resultScientificWork);
			}
		}
		return results;
	}

	public void shouldReturnHighlightedFieldsForGivenQueryAndFields(org.elasticsearch.index.query.QueryBuilder query,
			Long id) {

		final List<HighlightBuilder.Field> message = new ArrayList<HighlightBuilder.Field>();
		message.add(new HighlightBuilder.Field("text"));
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(query)
				.withHighlightFields(message.toArray(new HighlightBuilder.Field[message.size()])).build();

		SearchResultMapper searchResultMapper = new SearchResultMapper() {
			public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
				List<ScientificWork> chunk = new ArrayList<ScientificWork>();
				for (SearchHit searchHit : response.getHits()) {
					String idStrings = searchHit.getId();
					String scientificWorkFilename = scientificWorkService.findOneScientificWork(id).getFilename();
					if (!idStrings.contains(scientificWorkFilename)) {
						continue;
					}
					if (response.getHits().getHits().length <= 0) {
						return null;
					}
					ScientificWork sWork = new ScientificWork();
					String highlight = "";
					for (Text highlightField : searchHit.getHighlightFields().get("text").fragments()) {
						highlight = highlight + " " + highlightField;
					}
					ScientificWork scientificWork = scientificWorkService.findOneScientificWork(id);
					highlight = highlight.replace("<em>", "<b class=\"highlight\">");
					highlight = highlight.replace("</em>", "</b>");
					scientificWork.setHighlight(highlight);

					sWork.setTitle(searchHit.getHighlightFields().get("text").fragments()[0].toString());
					chunk.add(sWork);

					if (chunk.size() > 0) {
						return new AggregatedPageImpl<T>((List<T>) chunk);
					}
					return null;
				}
				return null;
			}
		};
		Page<ScientificWork> sampleEntities = template.queryForPage(searchQuery, ScientificWork.class, searchResultMapper);
	}

}
