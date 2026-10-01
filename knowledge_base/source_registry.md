# Реестр источников MTM AI Knowledge Base

## GTD

| ID | Использование | Источник | URL |
|---|---|---|---|
| SRC-GTD-WHAT | основы GTD, Capture, Clarify | Getting Things Done — What is GTD? | https://gettingthingsdone.com/what-is-gtd/ |
| SRC-GTD-DEFINITIONS | Project, Next Action, Waiting For, Someday/Maybe | Some Key GTD Definitions | https://gettingthingsdone.com/2010/10/some-key-gtd-definitions/ |
| SRC-GTD-TWO-MINUTE | правило двух минут | When to use GTD's Two-Minute Rule | https://gettingthingsdone.com/2011/06/when-to-use-gtds-two-minute-rule/ |
| SRC-GTD-ENGAGE | контекст, время, энергия, приоритет | Choosing What to Do | https://gettingthingsdone.com/2023/01/choosing-what-to-do/ |
| SRC-GTD-WEEKLY | Weekly Review | The GTD Weekly Review | https://gettingthingsdone.com/2009/05/the-gtd-weekly-review/ |

## Внутренние документы MTM AI

| ID | Документ | Что подтверждает |
|---|---|---|
| SRC-MTM-PASSPORT | Паспорт системы «MTM AI» | назначение системы, роли, intents, разрешённые/запрещённые действия, границы MVP, структура KB, модель данных, метрики |
| SRC-MTM-POLICY | Политика действий MTM AI, версия 1.0 | выполнение действий, уточнения, подтверждения, fallback, human review, поиск задач, безопасность |
| SRC-MTM-ACCEPTANCE | Приемочные испытания | функциональные и эксплуатационные требования |
| SRC-MTM-STAGE2 | Этап 2 — Описание | актуальный scope интерфейса и задачи второго этапа |

## Приоритет при расхождениях

Для актуального интерфейса используется более новое решение из `SRC-MTM-STAGE2`.

Пример: ранняя Policy использует `Inbox` для задачи без даты, а актуальный scope второго этапа задаёт размещение задачи без даты в разделе «Когда-нибудь».
