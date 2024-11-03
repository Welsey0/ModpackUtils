package dev.ultimatchamp.mutils;

import com.google.gson.JsonParser;
import dev.ultimatchamp.mutils.config.ModpackUtilsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ModpackUtils {
    public static final Logger LOGGER = LoggerFactory.getLogger("mutils");

    // Update Notifier
    private static final HttpClient client =
            HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

    public static String getLatestVersion() {
        try {
            String url = getURLForPlatform();
            String response = sendGetRequest(url);

            if (ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.MODRINTH) {
                return parseModrinthResponse(response);
            } else {
                return parseOtherPlatformResponse(response);
            }
        } catch (Exception e) {
            LOGGER.error("[ModpackUtils] {}", e.toString());
        }
        return null;
    }

    private static String getURLForPlatform() {
        if (ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.MODRINTH) {
            return "https://api.modrinth.com/v2/project/" + ModpackUtilsConfig.instance().modpackId + "/version";
        }
        return ModpackUtilsConfig.instance().versionAPI;
    }

    private static String sendGetRequest(String url) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(new URI(url))
                        .timeout(Duration.ofSeconds(10))
                        .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new IOException("[ModpackUtils] Failed to fetch the modpack info: " + response.statusCode());
        }
    }

    private static String parseModrinthResponse(String response) {
        var jsonArray = JsonParser.parseString(response).getAsJsonArray();
        return jsonArray.isEmpty() ? null : jsonArray.get(0).getAsJsonObject().get("version_number").getAsString();
    }

    private static String parseOtherPlatformResponse(String response) {
        return response.lines()
                .filter(line -> line.startsWith("version = \"") || line.startsWith("version_number = \""))
                .map(line -> line.substring(line.indexOf('"') + 1, line.length() - 1))
                .findFirst()
                .orElse(null);
    }

    public static boolean updateAvailable() {
        return !ModpackUtilsConfig.instance().localVersion.equals(getLatestVersion());
    }

    // MainMenuCredits Integration
    public static String getMmcText() {
        if (ModpackUtilsConfig.instance().mainMenuCreditsIntegeration == ModpackUtilsConfig.MmcStyle.NORMAL) {
            return """
                    {
                      "main_menu": {
                        "bottom_right": [
                          {
                            "text": "%s",
                            "clickEvent": {
                              "action": "open_url",
                              "value": "%s"
                            }
                          }
                        ]
                      }
                    }
                    """
                    .formatted(
                            ModpackUtilsConfig.instance().modpackName + " " + ModpackUtilsConfig.instance().localVersion,
                            ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.MODRINTH ? "https://modrinth.com/modpack/" + ModpackUtilsConfig.instance().modpackId + "/version/" + ModpackUtilsConfig.instance().localVersion : ModpackUtilsConfig.instance().modpackHome
                    );
        } else if (ModpackUtilsConfig.instance().mainMenuCreditsIntegeration == ModpackUtilsConfig.MmcStyle.FANCY) {
            return """
                    {
                      "main_menu": {
                        "bottom_right": [
                          {
                            "text": "%s",
                            "color": "green",
                            "clickEvent": {
                              "action": "open_url",
                              "value": "%s"
                            }
                          },
                          {
                            "text": "%s",
                            "color": "#FF00FF",
                            "clickEvent": {
                              "action": "open_url",
                              "value": "%s"
                            }
                          }
                        ]
                      }
                    }
                    """
                    .formatted(
                            ModpackUtilsConfig.instance().localVersion,
                            ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.MODRINTH ? "https://modrinth.com/modpack/" + ModpackUtilsConfig.instance().modpackId + "/version/" + ModpackUtilsConfig.instance().localVersion : ModpackUtilsConfig.instance().changelogLink,
                            ModpackUtilsConfig.instance().modpackName,
                            ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.MODRINTH ? "https://modrinth.com/modpack/" + ModpackUtilsConfig.instance().modpackId : ModpackUtilsConfig.instance().modpackHome
                    );
        }
        return null;
    }
}
