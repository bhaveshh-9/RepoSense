import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';


interface RepositoryAnalysis {
  repositoryName: string;
  description: string;
  owner: string;
  language: string;
  stars: number;
  forks: number;
  openIssues: number;
  totalContributors: number;
  totalCommits: number;
  healthScore: number;
  healthStatus: string;
  repositoryUrl: string;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  repositoryUrl = '';
  analysis: RepositoryAnalysis | null = null;

  loading = false;
  errorMessage = '';

  private apiUrl = 'http://localhost:8080/api/analyze';

  constructor(private http: HttpClient) {}

  analyzeRepository() {
    if (!this.repositoryUrl.trim()) {
      this.errorMessage = 'Please enter a GitHub repository URL.';
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.analysis = null;

    this.http.post<RepositoryAnalysis>(this.apiUrl, {
      repositoryUrl: this.repositoryUrl.trim()
    }).subscribe({
      next: (response) => {
        this.analysis = response;
        this.loading = false;
      },
      error: (error) => {
        this.loading = false;

        if (error.status === 404) {
          this.errorMessage = 'Repository not found. Check the GitHub URL.';
        } else if (error.status === 429) {
          this.errorMessage = 'GitHub API rate limit reached. Try again later.';
        } else if (error.status === 400) {
          this.errorMessage = error.error?.message || 'Invalid repository URL.';
        } else {
          this.errorMessage =
            error.error?.message ||
            'Unable to analyze repository. Please try again.';
        }
      }
    });
  }

  get healthColor(): string {
    if (!this.analysis) return '#6366f1';

    if (this.analysis.healthScore >= 80) return '#22c55e';
    if (this.analysis.healthScore >= 60) return '#84cc16';
    if (this.analysis.healthScore >= 40) return '#f59e0b';

    return '#ef4444';
  }

  reset() {
    this.repositoryUrl = '';
    this.analysis = null;
    this.errorMessage = '';
  }
}
