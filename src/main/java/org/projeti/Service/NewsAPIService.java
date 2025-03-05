package org.projeti.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class NewsAPIService {
    private static final String API_KEY = "faa104d60e5a4431b10e2e2c70c8c83d";  // Replace with your actual API key
    private static final String API_URL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=" + API_KEY;

    public static JSONArray fetchNews() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {  // HTTP OK
                Scanner scanner = new Scanner(url.openStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                return jsonResponse.getJSONArray("articles");
            } else {
                System.out.println("Failed to fetch news: " + responseCode);
                return new JSONArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }
}
