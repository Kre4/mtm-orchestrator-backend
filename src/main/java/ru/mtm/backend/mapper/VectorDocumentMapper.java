package ru.mtm.backend.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.ai.document.Document;
import ru.mtm.backend.dto.KnowledgePassage;
import ru.mtm.backend.entity.KnowledgeArticle;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class VectorDocumentMapper {

    public static Document toDocument(KnowledgeArticle article) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("documentId", article.documentId());
        metadata.put("title", article.title());
        metadata.put("type", article.type());
        metadata.put("path", article.path());
        if (!article.tags().isBlank()) {
            metadata.put("tags", article.tags());
        }
        return Document.builder()
                .id(article.vectorId())
                .text(article.text())
                .metadata(metadata)
                .build();
    }

    public static KnowledgePassage toPassage(Document document) {
        return new KnowledgePassage(
                metadata(document, "documentId"),
                metadata(document, "title"),
                metadata(document, "type"),
                metadata(document, "path"),
                document.getScore(),
                document.getText() == null ? "" : document.getText());
    }

    private static String metadata(Document document, String key) {
        Object value = document.getMetadata().get(key);
        return value == null ? "" : value.toString();
    }
}
