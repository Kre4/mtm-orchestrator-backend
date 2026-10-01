# Retrieval evaluation

`retrieval_test_set.csv` содержит 142 пользовательских запросов с эталонными документами.

Статусы:
- `READY` — 135;
- `NEEDS_PROJECT_CONFIRMATION` — 6;
- `NEEDS_SOURCE_REVIEW` — 1.

В Recall@3 включаются только строки со статусом `READY`.

## Метрика

**Целевое значение Recall@3: ≥ 85%.**

Для каждого запроса система возвращает Top-3 документов.

Запрос считается успешным, если хотя бы один документ из `relevant_documents` присутствует в Top-3.

Формула:

`Recall@3 = successful_queries / all_ready_queries`

Текущий набор используется как `dev`. Для итогового замера рекомендуется сформировать отдельный holdout после фиксации Knowledge Base и параметров retrieval.
