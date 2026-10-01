package ru.mtm.backend.dto;

public record KnowledgePassage(
		String documentId,
		String title,
		String type,
		String path,
		Double score,
		String text
) {
}
