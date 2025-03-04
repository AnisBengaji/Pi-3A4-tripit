package org.projeti.utils;


import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApiUtil {
    private static final String API_KEY = "c0a6aa6bae204fde954162110250403";
    private static final String BASE_URL = "http://api.weatherapi.com/v1/forecast.json";

    public static WeatherInfo getWeather(String location, String date) {
        try {
            String urlString = BASE_URL + "?key=" + API_KEY + "&q=" + location + "&dt=" + date;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject json = new JSONObject(response.toString());
                JSONObject forecast = json.getJSONObject("forecast");
                JSONObject forecastDay = forecast.getJSONArray("forecastday").getJSONObject(0);
                JSONObject day = forecastDay.getJSONObject("day");
                double minTemp = day.getDouble("mintemp_c");
                double maxTemp = day.getDouble("maxtemp_c");
                String condition = day.getJSONObject("condition").getString("text");
                return new WeatherInfo(minTemp, maxTemp, condition);
            } else {
                System.out.println("GET request failed. Response code: " + responseCode);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class WeatherInfo {
        private double minTemp;
        private double maxTemp;
        private String condition;

        public WeatherInfo(double minTemp, double maxTemp, String condition) {
            this.minTemp = minTemp;
            this.maxTemp = maxTemp;
            this.condition = condition;
        }

        public double getMinTemp() {
            return minTemp;
        }

        public double getMaxTemp() {
            return maxTemp;
        }

        public String getCondition() {
            return condition;
        }
    }
}

