package ru.mtm.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mtm.knowledge")
public record KnowledgeProperties(
		String corpusLocation,
		int topK,
		double similarityThreshold,
		boolean indexOnStartup
) {

	public KnowledgeProperties {
		if (corpusLocation == null || corpusLocation.isBlank()) {
			corpusLocation = "classpath:knowledge/";
		}
		if (topK <= 0) {
			topK = 3;
		}
		if (similarityThreshold < 0 || similarityThreshold > 1) {
			similarityThreshold = 0.5;
		}
	}

}
