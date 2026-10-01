package ru.mtm.backend.knowledge.initializr;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class KnowledgeCorpusState {

	private final JdbcTemplate jdbcTemplate;

	public KnowledgeCorpusState(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Optional<String> indexedVersion() {
		List<String> versions = jdbcTemplate.query(
				"SELECT version FROM knowledge_corpus_state WHERE id = 1",
				(rs, rowNum) -> rs.getString(1));
		return versions.stream().findFirst();
	}

	public void save(String version) {
		jdbcTemplate.update("""
				INSERT INTO knowledge_corpus_state (id, version, indexed_at)
				VALUES (1, ?, CURRENT_TIMESTAMP)
				ON CONFLICT (id) DO UPDATE
				SET version = EXCLUDED.version, indexed_at = CURRENT_TIMESTAMP
				""", version);
	}

}
