package dev.ultimatchamp.mutils;

import com.google.gson.JsonParser;
import dev.ultimatchamp.mutils.config.ModpackUtilsConfig;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
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
            var response = sendGetRequest(getURLForPlatform());

            return switch (ModpackUtilsConfig.instance().platform) {
                case MODRINTH -> parseModrinthResponse(response);
                case CURSEFORGE -> parseCurseForgeResponse(response);
                case CUSTOM -> parseOtherPlatformResponse(response);
            };
        } catch (Exception e) {
            LOGGER.error("[ModpackUtils] Failed to fetch the modpack info!", e);
        }

        return null;
    }

    private static String getURLForPlatform() {
        return switch (ModpackUtilsConfig.instance().platform) {
            case MODRINTH -> "https://api.modrinth.com/v2/project/" + ModpackUtilsConfig.instance().modpackId + "/version";
            case CURSEFORGE -> "https://api.curseforge.com/v1/mods/" + ModpackUtilsConfig.instance().modpackId + "/files";
            case CUSTOM ->  ModpackUtilsConfig.instance().versionAPI;
        };
    }

    private static String sendGetRequest(String url) throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10));

        if (ModpackUtilsConfig.instance().platform == ModpackUtilsConfig.Platforms.CURSEFORGE)
            requestBuilder.header("x-api-key", getCurseForgeApiKey());

        var request = requestBuilder.build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new IOException("[ModpackUtils] Failed to fetch the modpack info: " + response.statusCode());
        }
    }

    private static String parseModrinthResponse(String response) {
        var jsonArray = JsonParser.parseString(response).getAsJsonArray();

        for (var i = 0; i < jsonArray.size(); i++) {
            var jsonObject = jsonArray.get(i).getAsJsonObject();

            if (ModpackUtilsConfig.instance().versionType.contains(jsonObject.get("version_type").getAsString())) {
                if (ModpackUtilsConfig.instance().loader.equals(jsonObject.get("loaders").getAsJsonArray().get(0).getAsString())) {
                    var hasNameFilter = true;
                    if (!ModpackUtilsConfig.instance().nameFilters.isEmpty()) {
                        for (var filter : ModpackUtilsConfig.instance().nameFilters) {
                            if (!jsonObject.get("name").getAsString().contains(filter)) {
                                hasNameFilter = false;
                            }
                        }
                    }

                    var hasVersionFilter = true;
                    if (!ModpackUtilsConfig.instance().versionFilters.isEmpty()) {
                        for (var filter : ModpackUtilsConfig.instance().versionFilters) {
                            if (!jsonObject.get("version_number").getAsString().contains(filter)) {
                                hasVersionFilter = false;
                            }
                        }
                    }

                    if (hasNameFilter && hasVersionFilter) {
                        if (ModpackUtilsConfig.instance().checkMcVersion) {
                            if (Minecraft.getInstance().getLaunchedVersion().equals(jsonObject.get("game_versions").getAsJsonArray().get(0).getAsString())) {
                                return jsonObject.get("version_number").getAsString();
                            }
                        } else {
                            return jsonObject.get("version_number").getAsString();
                        }
                    }
                }
            }
        }

        return null;
    }

    private static String getCurseForgeApiKey() {
        /*
         * This API key should only be used by ModpackUtils, you are not allowed to use it in your own project or fork,
         * if you want to use the CurseForge API in your own project, you should apply for your own API key.
         */
        return ""; // CurseForge, please give me an API key ;(
    }

    private static String parseCurseForgeResponse(String response) {
        var jsonObject = JsonParser.parseString(response).getAsJsonObject();
        var dataArray = jsonObject.getAsJsonArray("data");

        for (var i = 0; i < dataArray.size(); i++) {
            var versionObject = dataArray.get(i).getAsJsonObject();
            var versionType = "release";

            if (versionObject.get("releaseType").getAsString().equals("2")) {
                versionType = "beta";
            } else if (versionObject.get("releaseType").getAsString().equals("3")) {
                versionType = "alpha";
            }

            if (versionObject.get("isAvailable").getAsBoolean()) {
                if (ModpackUtilsConfig.instance().versionType.contains(versionType)) {
                    if (ModpackUtilsConfig.instance().loader.equalsIgnoreCase(versionObject.getAsJsonArray("gameVersions").get(0).getAsString())) {
                        var hasNameFilter = true;
                        if (!ModpackUtilsConfig.instance().nameFilters.isEmpty()) {
                            for (var filter : ModpackUtilsConfig.instance().nameFilters) {
                                if (!versionObject.get("displayName").getAsString().contains(filter)) {
                                    hasNameFilter = false;
                                }
                            }
                        }

                        var hasVersionFilter = true;
                        if (!ModpackUtilsConfig.instance().versionFilters.isEmpty()) {
                            for (var filter : ModpackUtilsConfig.instance().versionFilters) {
                                if (!versionObject.get("displayName").getAsString().contains(filter)) {
                                    hasVersionFilter = false;
                                }
                            }
                        }

                        if (hasNameFilter && hasVersionFilter) {
                            if (ModpackUtilsConfig.instance().checkMcVersion) {
                                if (Minecraft.getInstance().getLaunchedVersion().equals(versionObject.getAsJsonArray("gameVersions").get(1).getAsString())) {
                                    return versionObject.get("displayName").getAsString();
                                }
                            } else {
                                return jsonObject.get("displayName").getAsString();
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    public static String getLatestFileId() {
        try {
            var jsonObject = JsonParser.parseString(sendGetRequest(getURLForPlatform())).getAsJsonObject();
            var dataArray = jsonObject.getAsJsonArray("data");

            for (var i = 0; i < dataArray.size(); i++) {
                var versionObject = dataArray.get(i).getAsJsonObject();

                if (versionObject.get("isAvailable").getAsBoolean()) {
                    if (ModpackUtils.getLatestVersion().equals(versionObject.get("displayName").getAsString())) {
                        return versionObject.get("id").getAsString();
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("[ModpackUtils] Failed to fetch the modpack info!", e);
        }

        return null;
    }

    public static String getLocalFileId() {
        try {
            var jsonObject = JsonParser.parseString(sendGetRequest(getURLForPlatform())).getAsJsonObject();
            var dataArray = jsonObject.getAsJsonArray("data");

            for (var i = 0; i < dataArray.size(); i++) {
                var versionObject = dataArray.get(i).getAsJsonObject();

                if (versionObject.get("isAvailable").getAsBoolean()) {
                    if (ModpackUtilsConfig.instance().localVersion.equals(versionObject.get("displayName").getAsString())) {
                        return versionObject.get("id").getAsString();
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("[ModpackUtils] Failed to fetch the modpack info!", e);
        }

        return null;
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
