package org.projeti.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.util.Base64;

public class EmailValidation {

    private static final String API_KEY = "a314b288-554a-441b-afd7-b2b5686dc872"; // Replace with actual API key
    private static final String API_URL = "https://api.mailgun.net/v3/YOUR_DOMAIN/messages"; // Replace with correct Mailgun API URL

    public void sendEmail(String recipientEmail, String subject, String message) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Set authentication header
            String auth = "api:" + API_KEY;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Email data
            String data = "from=you@yourdomain.com&to=" + recipientEmail +
                    "&subject=" + subject + "&text=" + message;

            // Send request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = data.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Check response
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Email sent successfully.");
            } else {
                System.out.println("Error sending email: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
