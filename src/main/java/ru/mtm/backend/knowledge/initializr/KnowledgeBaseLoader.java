package ru.mtm.backend.knowledge.initializr;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.mtm.backend.config.KnowledgeProperties;
import ru.mtm.backend.entity.KnowledgeArticle;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class KnowledgeBaseLoader {

	private final ObjectMapper objectMapper;

	private final ResourceLoader resourceLoader;

	private final KnowledgeProperties properties;

	public KnowledgeBaseLoader(ObjectMapper objectMapper, ResourceLoader resourceLoader, KnowledgeProperties properties) {
		this.objectMapper = objectMapper;
		this.resourceLoader = resourceLoader;
		this.properties = properties;
	}

	public KnowledgeCorpus load() {
		Resource manifestResource = resource("corpus_manifest.json");
		CorpusManifest manifest = readManifest(manifestResource);
		if (manifest.version() == null || manifest.version().isBlank()) {
			throw new IllegalStateException("Knowledge corpus manifest has no version: " + manifestResource);
		}
		if (manifest.documents() == null || manifest.documents().isEmpty()) {
			throw new IllegalStateException("Knowledge corpus manifest has no documents: " + manifestResource);
		}
		List<KnowledgeArticle> articles = new ArrayList<>();
		for (CorpusDocument document : manifest.documents()) {
			if (!document.indexable()) {
				continue;
			}
			articles.add(readArticle(document));
		}
		if (articles.isEmpty()) {
			throw new IllegalStateException("Knowledge corpus has no indexable documents: " + manifestResource);
		}
		return new KnowledgeCorpus(manifest.version(), List.copyOf(manifest.documents()), List.copyOf(articles));
	}

	private CorpusManifest readManifest(Resource manifestResource) {
		try (InputStream input = manifestResource.getInputStream()) {
			return objectMapper.readValue(input, CorpusManifest.class);
		}
		catch (IOException | JacksonException exception) {
			throw new IllegalStateException("Failed to read knowledge corpus manifest: " + manifestResource, exception);
		}
	}

	private KnowledgeArticle readArticle(CorpusDocument document) {
		Resource articleResource = resource(document.path());
		try {
			String raw = articleResource.getContentAsString(StandardCharsets.UTF_8);
			ParsedMarkdown parsed = ParsedMarkdown.parse(raw);
			String title = parsed.title().isBlank() ? document.documentId() : parsed.title();
			return new KnowledgeArticle(
					document.documentId(),
					title,
					document.type(),
					document.path(),
					String.join(", ", parsed.tags()),
					parsed.body());
		}
		catch (IOException exception) {
			throw new IllegalStateException("Failed to read knowledge corpus article: " + articleResource, exception);
		}
	}

	private Resource resource(String relativePath) {
		String location = properties.corpusLocation();
		if (!location.endsWith("/")) {
			location = location + "/";
		}
		Resource resource = resourceLoader.getResource(location + relativePath);
		if (!resource.exists()) {
			throw new IllegalStateException("Knowledge corpus resource not found: " + location + relativePath);
		}
		return resource;
	}

	public record KnowledgeCorpus(String version, List<CorpusDocument> documents, List<KnowledgeArticle> articles) {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	private record CorpusManifest(String version, List<CorpusDocument> documents) {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record CorpusDocument(
			@JsonProperty("document_id") String documentId,
			String path,
			String type,
			boolean indexable
	) {
	}

	private record ParsedMarkdown(String title, List<String> tags, String body) {

		static ParsedMarkdown parse(String raw) {
			String normalized = raw.replace("\r\n", "\n");
			if (!normalized.startsWith("---\n")) {
				return new ParsedMarkdown("", List.of(), normalized.trim());
			}
			int closing = normalized.indexOf("\n---\n", 4);
			if (closing < 0) {
				throw new IllegalStateException("Knowledge base article is missing the closing front matter marker");
			}
			String frontMatter = normalized.substring(4, closing);
			String body = normalized.substring(closing + "\n---\n".length()).trim();
			String title = "";
			List<String> tags = new ArrayList<>();
			boolean inTags = false;
			for (String line : frontMatter.split("\n")) {
				if (line.startsWith("title:")) {
					title = unquote(line.substring("title:".length()).trim());
					inTags = false;
					continue;
				}
				if (line.startsWith("tags:")) {
					inTags = true;
					continue;
				}
				if (inTags && line.startsWith("- ")) {
					tags.add(line.substring(2).trim());
					continue;
				}
				if (inTags && !line.isBlank() && !line.startsWith(" ") && !line.startsWith("\t")) {
					inTags = false;
				}
			}
			return new ParsedMarkdown(title, List.copyOf(tags), body);
		}

		private static String unquote(String value) {
			if (value.length() >= 2) {
				char first = value.charAt(0);
				char last = value.charAt(value.length() - 1);
				if ((first == '\'' && last == '\'') || (first == '"' && last == '"')) {
					return value.substring(1, value.length() - 1);
				}
			}
			return value;
		}

	}

}
