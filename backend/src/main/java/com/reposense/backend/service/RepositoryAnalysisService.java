
package com.reposense.backend.service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.reposense.backend.client.GitHubApiClient;
import com.reposense.backend.dto.RepositoryAnalysisResponse;

@Service
public class RepositoryAnalysisService {

    private final GitHubApiClient gitHubApiClient;
    private final HealthScoreService healthScoreService;

    public RepositoryAnalysisService(
            GitHubApiClient gitHubApiClient,
            HealthScoreService healthScoreService) {

        this.gitHubApiClient = gitHubApiClient;
        this.healthScoreService = healthScoreService;
    }

    public RepositoryAnalysisResponse analyze(String repositoryUrl) {

        // Validate repository URL
        if (repositoryUrl == null || repositoryUrl.isBlank()) {
            throw new IllegalArgumentException(
                    "Repository URL is required"
            );
        }

        Pattern pattern = Pattern.compile(
                "^https?://github\\.com/([A-Za-z0-9_.-]+)/([A-Za-z0-9_.-]+)/?$"
        );

        Matcher matcher = pattern.matcher(repositoryUrl.trim());

        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    "Please enter a valid GitHub repository URL"
            );
        }

        // Extract owner and repository name
        String owner = matcher.group(1);
        String repo = matcher.group(2).replace(".git", "");

        // Fetch repository details
        Map<String, Object> repository =
                gitHubApiClient.getRepository(owner, repo);

        // Fetch contributors
        List<Map<String, Object>> contributors =
                gitHubApiClient.getContributors(owner, repo);

        // Fetch commits
        List<Map<String, Object>> commits =
                gitHubApiClient.getCommits(owner, repo);

        // Extract repository information
        String name = (String) repository.get("name");
        String description = (String) repository.get("description");
        String language = (String) repository.get("language");

        int stars = getInt(repository, "stargazers_count");
        int forks = getInt(repository, "forks_count");
        int issues = getInt(repository, "open_issues_count");

        int contributorCount = contributors.size();
        int commitCount = commits.size();

        // Calculate health score
        int score = healthScoreService.calculateScore(
                stars,
                forks,
                issues,
                contributorCount,
                commitCount
        );

        String status = healthScoreService.getHealthStatus(score);

        // Create analysis response
        return new RepositoryAnalysisResponse(
                name,
                description,
                owner,
                language,
                stars,
                forks,
                issues,
                contributorCount,
                commitCount,
                score,
                status,
                "https://github.com/" + owner + "/" + repo
        );
    }

    // Safely extract integer values from GitHub response
    private int getInt(Map<String, Object> data, String key) {

        Object value = data.get(key);

        if (value instanceof Number number) {
            return number.intValue();
        }

        return 0;
    }
}
