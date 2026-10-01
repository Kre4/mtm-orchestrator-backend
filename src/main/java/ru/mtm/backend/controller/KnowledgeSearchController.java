package ru.mtm.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.mtm.backend.dto.KnowledgeSearchRequest;
import ru.mtm.backend.dto.KnowledgeSearchResponse;
import ru.mtm.backend.knowledge.KnowledgeSearchService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/knowledge")
public class KnowledgeSearchController {

    private final KnowledgeSearchService searchService;

    @PostMapping("/search")
    public KnowledgeSearchResponse search(@Valid @RequestBody KnowledgeSearchRequest request) {
        String query = request.query().trim();
        return searchService.search(query, request.topK());
    }

}
