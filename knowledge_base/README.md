# MTM AI Knowledge Base

Knowledge Base используется для справочных ответов MTM AI и состоит из двух частей:

- `gtd/` — материалы по методологии Getting Things Done;
- `application/` — справка по функциям приложения MTM.

Рабочие пользовательские данные в Knowledge Base не хранятся.

## Структура

```text
knowledge_base/
├── README.md
├── source_registry.md
├── corpus_manifest.json
├── open_questions.md
├── gtd/
├── application/
└── evaluation/
    ├── README.md
    └── retrieval_test_set.csv
```

## Формат документов

Один `.md`-файл посвящён одной основной теме.

В начале статьи используется YAML front matter:

```yaml
---
id: gtd_next_action
title: "Следующее действие (Next Action)"
type: gtd
source_title: "Some Key GTD Definitions"
source_url: "https://..."
tags:
  - gtd
  - next_action
language: ru
---
```

Для внутренних материалов MTM указываются `source_ids`, соответствующие записям в `source_registry.md`.

## Индексация

Список документов, разрешённых к индексации, находится в `corpus_manifest.json`.

Документы могут быть разбиты на chunks, преобразованы в embeddings и сохранены в PostgreSQL с pgvector.

## Требования к содержанию

Материал должен:

- отвечать на конкретную группу пользовательских вопросов;
- иметь проверяемый источник;
- не дублировать соседние статьи;
- не содержать неподтверждённых функций;
- быть написан кратко и понятно;
- сохранять различие между методологией GTD и возможностями приложения MTM.

## Оценка retrieval

Тестовый набор находится в `evaluation/retrieval_test_set.csv`.

Основная метрика:

**Recall@3 ≥ 85%**

Успех для запроса: хотя бы один корректный документ из `relevant_documents` попал в Top-3 результатов поиска.

Строки со статусом `NEEDS_PROJECT_CONFIRMATION` и `NEEDS_SOURCE_REVIEW` не включаются в расчёт до закрытия соответствующего вопроса.
