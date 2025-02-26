package org.projeti.Service;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

public class StripePaymentService {
    public static String createCheckoutSession(double amount) throws Exception {
        // Récupérer la clé API depuis les variables d'environnement
        String stripeApiKey = System.getenv("STRIPE_API_KEY");
        if (stripeApiKey == null || stripeApiKey.isEmpty()) {
            throw new RuntimeException("La clé API Stripe n'est pas configurée.");
        }

        // Initialiser Stripe avec la clé API
        Stripe.apiKey = stripeApiKey;

        // Créer une session de paiement
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/success")
                .setCancelUrl("http://localhost:8080/cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("eur")
                                                .setUnitAmount((long) (amount * 100)) // Stripe travaille en centimes
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Réservation Événement")
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        // Créer la session de paiement
        Session session = Session.create(params);
        return session.getUrl(); // Retourne l'URL de la session de paiement
    }
}