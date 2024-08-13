package src;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Fetching {
    public static List<String> fetchServerTxt(String urlString) throws Exception {
        List<String> vpnConfigs = new ArrayList<>();
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (!inputLine.trim().isEmpty()) {
                    vpnConfigs.add(inputLine);
                }
            }
        }

        return vpnConfigs;
    }
}
