package ru.mtm.backend.entity;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record KnowledgeArticle(
		String documentId,
		String title,
		String type,
		String path,
		String tags,
		String text
) {

	public static String vectorId(String documentId) {
		return UUID.nameUUIDFromBytes(documentId.getBytes(StandardCharsets.UTF_8)).toString();
	}

	public String vectorId() {
		return vectorId(documentId);
	}

}
