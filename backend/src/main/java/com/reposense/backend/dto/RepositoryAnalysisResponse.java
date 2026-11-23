package com.reposense.backend.dto;

public record RepositoryAnalysisResponse(
        String repositoryName,
        String description,
        String owner,
        String language,
        int stars,
        int forks,
        int openIssues,
        int totalContributors,
        int totalCommits,
        int healthScore,
        String healthStatus,
        String repositoryUrl
) {
}