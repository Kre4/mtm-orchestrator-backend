package ru.mtm.backend.knowledge.initializr;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.mtm.backend.entity.KnowledgeArticle;
import ru.mtm.backend.mapper.VectorDocumentMapper;

@Component
@ConditionalOnProperty(prefix = "mtm.knowledge", name = "index-on-startup", havingValue = "true", matchIfMissing = true)
public class KnowledgeIndexer implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(KnowledgeIndexer.class);

	private final KnowledgeBaseLoader loader;

	private final KnowledgeCorpusState corpusState;

	private final VectorStore vectorStore;

	public KnowledgeIndexer(KnowledgeBaseLoader loader, KnowledgeCorpusState corpusState, VectorStore vectorStore) {
		this.loader = loader;
		this.corpusState = corpusState;
		this.vectorStore = vectorStore;
	}

	@Override
	public void run(ApplicationArguments args) {
		KnowledgeBaseLoader.KnowledgeCorpus corpus = loader.load();
		if (corpusState.indexedVersion().filter(corpus.version()::equals).isPresent()) {
			log.info("Knowledge corpus {} is already indexed", corpus.version());
			return;
		}

		List<String> excludedIds = corpus.documents().stream()
				.filter(document -> !document.indexable())
				.map(document -> KnowledgeArticle.vectorId(document.documentId()))
				.toList();
		if (!excludedIds.isEmpty()) {
			vectorStore.delete(excludedIds);
		}

		List<Document> documents = corpus.articles().stream()
				.map(VectorDocumentMapper::toDocument)
				.toList();
		vectorStore.add(documents);
		corpusState.save(corpus.version());
		log.info("Indexed knowledge corpus {} with {} articles", corpus.version(), documents.size());
	}

}
