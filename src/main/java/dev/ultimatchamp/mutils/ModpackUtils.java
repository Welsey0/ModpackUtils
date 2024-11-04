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

    // Ram Alert
    public static int getAllocatedRam() {
        return (int) (Runtime.getRuntime().maxMemory() / (1024 * 1024)); // bytes to MiB
    }
}
