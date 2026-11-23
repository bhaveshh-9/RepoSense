
package com.reposense.backend.client;

import java.util.List;
import java.util.Map;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class GitHubApiClient {

    private final RestTemplate restTemplate = new RestTemplate();

    // private HttpHeaders createHeaders() {
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    //     headers.set("User-Agent", "RepoSense-App");
    //     return headers;
    // }

    public Map<String, Object> getRepository(String owner, String repo) {
        String url = "https://api.github.com/repos/" + owner + "/" + repo;

        return restTemplate.getForObject(url, Map.class);
    }

    public List<Map<String, Object>> getContributors(String owner, String repo) {
        String url = "https://api.github.com/repos/" + owner + "/" + repo + "/contributors?per_page=100";

        List<Map<String, Object>> result = restTemplate.getForObject(url, List.class);

        return result != null ? result : List.of();
    }

    public List<Map<String, Object>> getCommits(String owner, String repo) {
    String url = "https://api.github.com/repos/" + owner + "/" + repo + "/commits?per_page=100";

    try {
        List<Map<String, Object>> result = restTemplate.getForObject(url, List.class);
        return result != null ? result : List.of();
    } catch (HttpClientErrorException.Conflict ex) {
        return List.of();
    }
}
}
