package com.reposense.backend.controller;

import com.reposense.backend.dto.RepositoryRequest;
import com.reposense.backend.dto.RepositoryAnalysisResponse;
import com.reposense.backend.service.RepositoryAnalysisService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class RepositoryController {

    private final RepositoryAnalysisService analysisService;

    public RepositoryController(RepositoryAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping("/analyze")
    public RepositoryAnalysisResponse analyzeRepository(
            @RequestBody RepositoryRequest request) {

        return analysisService.analyze(request.repositoryUrl());
    }
}