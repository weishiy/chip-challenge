package nz.ac.wgtn.swen225.lc.fuzz;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GitLabIntegration {

    public static void reportIssueWithAssign(String title, String description, String assignID) {
        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(
                            """
                                    {
                                       "assignee_id": "${assignID}",
                                       "title": "${title}",
                                       "description": "${description}",
                                       "labels": "#detectedByFuzzer"
                                    }""".replace("${assignID}", assignID).
                                    replace("${title}", title)
                                    .replace("${description}", description)))

                    // 18975 is our chips-challenge
                    .uri(new URI("https://gitlab.ecs.vuw.ac.nz/api/v4/projects/18975/issues"))
                    .header("Content-Type", "application/json")
                    .header("PRIVATE-TOKEN", "9Fm_FF51Sf9NcZUZLUde")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() > 300) {
                throw new IllegalStateException(response.statusCode() + " " + response.body());
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new IllegalStateException("Failed to communicate to gitlab", e);
        }
    }

    public static void reportIssueNoAssign(String title, String description) {
        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(
                            """
                                    {
                                       "title": "${title}",
                                       "description": "${description}",
                                       "labels": "#detectedByFuzzer"
                                    }""".replace("${title}", title)
                                    .replace("${description}", description)))

                    // 18975 is our chips-challenge
                    .uri(new URI("https://gitlab.ecs.vuw.ac.nz/api/v4/projects/18975/issues"))
                    .header("Content-Type", "application/json")
                    .header("PRIVATE-TOKEN", "9Fm_FF51Sf9NcZUZLUde")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() > 300) {
                throw new IllegalStateException(response.statusCode() + " " + response.body());
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new IllegalStateException("Failed to communicate to gitlab", e);
        }
    }
}
