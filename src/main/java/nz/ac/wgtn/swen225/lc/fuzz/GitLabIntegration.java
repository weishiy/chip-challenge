package nz.ac.wgtn.swen225.lc.fuzz;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * This class enables the fuzz tests to automatically create issues in gitlab
 *
 * @author Oliver Berry
 * Student ID: 300474410
 *
 */

public class GitLabIntegration {

    /**
     * creates an issue in the gitlab and assigns it to a user
     *
     * @param title the title for the issue
     * @param description the description of the issue
     * @param assignID the user id for the issue to be assigned to
     */

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

    /**
     * creates an issue in the gitlab, but doesn't assign it to a user.
     * Used in cases where the fuzzer fails to assign an error to a user,
     * because it is still valuable to know that an error was caused and
     * what the error was.
     *
     * @param title the title for the issue
     * @param description the description of the issue
     */

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
