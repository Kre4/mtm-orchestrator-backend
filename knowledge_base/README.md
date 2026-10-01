# MTM AI Knowledge Base

Knowledge Base используется для справочных ответов MTM AI и состоит из двух частей:

- `gtd/` — материалы по методологии Getting Things Done;
- `application/` — справка по функциям приложения MTM.

Рабочие пользовательские данные в Knowledge Base не хранятся.

## Структура

Индексируемый корпус лежит в classpath и попадает в собранное приложение:

```text
src/main/resources/knowledge/
├── corpus_manifest.json
├── gtd/
└── application/
```

Реестр, открытые вопросы и оценка retrieval остаются вне индекса:

```text
knowledge_base/
├── README.md
├── source_registry.md
├── open_questions.md
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

Список документов, разрешённых к индексации, находится в `src/main/resources/knowledge/corpus_manifest.json`.

Поле `version` этого манифеста записывается в таблицу `knowledge_corpus_state`. Таблицу создаёт миграция Flyway `V1__create_knowledge_corpus_state.sql`. Если при старте там уже лежит та же версия, эмбеддинги не пересчитываются. После правки статей версию нужно увеличить.

Документы преобразуются в embeddings и сохраняются в PostgreSQL с pgvector.

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
