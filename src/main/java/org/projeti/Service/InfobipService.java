package org.projeti.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class InfobipService {

    private static final String API_URL = "https://api.infobip.com/sms/1/text/single";
    private static final String API_KEY = "21fc371dae33e34ce9a4dbbc0b9e453d-323dd203-08e1-4e61-9167-dfe1cea65c2f"; // Replace with your API key

    // Method to send SMS
    public void sendSms(String toPhoneNumber, String message) {
        try {
            // Create JSON payload for sending SMS
            JSONObject json = new JSONObject();
            json.put("from", "Infobip");  // Use the default sender (or use 'Infobip' as the sender name)
            json.put("to", toPhoneNumber);  // Ensure phone number is in international format (e.g., +15551234567)
            json.put("text", message);  // Message content

            // Create HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(API_URL))
                    .header("Authorization", "App " + API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString(), StandardCharsets.UTF_8))
                    .build();

            // Send request and get response
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the response status
            if (response.statusCode() == 200) {
                System.out.println("SMS sent successfully.");
            } else {
                // If response code isn't 200, print the response body for debugging
                System.out.println("Error sending SMS. Status Code: " + response.statusCode());
                System.out.println("Response body: " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Exception occurred while sending SMS: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
