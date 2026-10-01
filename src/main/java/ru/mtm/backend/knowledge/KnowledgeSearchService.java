package ru.mtm.backend.knowledge;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.mtm.backend.config.KnowledgeProperties;
import ru.mtm.backend.dto.KnowledgePassage;
import ru.mtm.backend.dto.KnowledgeSearchResponse;
import ru.mtm.backend.mapper.VectorDocumentMapper;

@Service
@RequiredArgsConstructor
public class KnowledgeSearchService {

	private final VectorStore vectorStore;

	private final KnowledgeProperties properties;

	public KnowledgeSearchResponse search(String query, Integer topK) {
		int limit = topK == null ? properties.topK() : topK;
		List<Document> documents = vectorStore.similaritySearch(SearchRequest.builder()
				.query(query)
				.topK(limit)
				.similarityThreshold(properties.similarityThreshold())
				.build());
		if (CollectionUtils.isEmpty(documents)) {
			return new KnowledgeSearchResponse(query, List.of());
		}
		List<KnowledgePassage> results = documents.stream()
				.map(VectorDocumentMapper::toPassage)
				.toList();
		return new KnowledgeSearchResponse(query, results);
	}

}
