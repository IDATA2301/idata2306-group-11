package com.roamroute.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
/**
 * Service for sending emails via the Resend email provider using WebClient for reactive HTTP requests.
 * Provides high-level helpers for sending password reset emails and a generic send method used across the app.
 */
public class EmailService {

    private final WebClient webClient;
    private final String resendApiKey;
    private final String resendFromEmail;

    public EmailService(
        @Value("${resend.api.key}") String resendApiKey,
        @Value("${resend.from-email}") String resendFromEmail
    ) {
        this.webClient = WebClient.builder().baseUrl("https://api.resend.com").build();
        this.resendApiKey = resendApiKey;
        this.resendFromEmail = resendFromEmail;
    }

    /**
     * Send a password reset email to the user
     */
    public Mono<String> sendPasswordResetEmail(String to, String resetToken, String resetUrl) {
        String emailBody = String.format(
            "Click the link below to reset your password:\n\n" +
            "%s\n\n" +
            "This link will expire in 1 hour.",
            resetUrl
        );

        return sendEmail(to, "Reset your RoamRoute password", emailBody);
    }

    /**
     * Generic method to send email via Resend
     */
    public Mono<String> sendEmail(String to, String subject, String body) {
        ResendEmailRequest request = new ResendEmailRequest(
            resendFromEmail,
            List.of(to),
            subject,
            body
        );

        return webClient
            .post()
            .uri("/emails")
            .header("Authorization", "Bearer " + resendApiKey)
            .header("Content-Type", "application/json")
            .bodyValue(request)
            .retrieve()
            .onStatus(
                status -> status.isError(),
                response -> response.bodyToMono(String.class)
                    .defaultIfEmpty("")
                    .map(bodyText -> new IllegalStateException(
                        "Resend request failed with status " + response.statusCode() +
                        (bodyText.isBlank() ? "" : ": " + bodyText)
                    ))
            )
            .bodyToMono(String.class)
            .doOnSuccess(response -> System.out.println("Email sent successfully: " + response))
            .doOnError(error -> System.err.println("Email sending failed: " + error.getMessage()));
    }

    /**
     * Inner class for Resend API request payload
     */
    public static class ResendEmailRequest {
        public String from;
        public List<String> to;
        public String subject;
        public String text;

        public ResendEmailRequest(String from, List<String> to, String subject, String text) {
            this.from = from;
            this.to = to;
            this.subject = subject;
            this.text = text;
        }
    }
}
