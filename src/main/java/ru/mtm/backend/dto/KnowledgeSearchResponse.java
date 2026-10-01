package ru.mtm.backend.dto;

import java.util.List;

public record KnowledgeSearchResponse(String query, List<KnowledgePassage> results) {
}
