CREATE TABLE IF NOT EXISTS knowledge_corpus_state (
    id SMALLINT PRIMARY KEY,
    version TEXT NOT NULL,
    indexed_at TIMESTAMPTZ NOT NULL
);
