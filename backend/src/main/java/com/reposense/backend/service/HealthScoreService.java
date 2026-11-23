
package com.reposense.backend.service;

import org.springframework.stereotype.Service;

@Service
public class HealthScoreService {

    public int calculateScore(
            int stars,
            int forks,
            int issues,
            int contributors,
            int commits) {

        int score = 0;

        // Community popularity: 20 points
        score += Math.min(stars / 10, 20);

        // Collaboration: 20 points
        score += Math.min(contributors * 2, 20);

        // Development activity: 30 points
        score += Math.min(commits / 3, 30);

        // Maintenance: 20 points
        if (issues == 0) {
            score += 20;
        } else {
            score += Math.max(0, 20 - (issues / 5));
        }

        // Fork activity: 10 points
        score += Math.min(forks / 5, 10);

        return Math.min(score, 100);
    }

    public String getHealthStatus(int score) {
        if (score >= 80) return "Healthy";
        if (score >= 60) return "Good";
        if (score >= 40) return "Needs Attention";
        return "Poor";
    }
}
