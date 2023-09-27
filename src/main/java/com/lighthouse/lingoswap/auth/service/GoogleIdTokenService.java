package com.lighthouse.lingoswap.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.lighthouse.lingoswap.auth.exception.InvalidIdTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class GoogleIdTokenService {

    private final GoogleIdTokenVerifier verifier;

    public String parseIdToken(final String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            GoogleIdToken.Payload payload = Objects.requireNonNull(idToken.getPayload());
            return Objects.requireNonNull(payload.getEmail());
        } catch (GeneralSecurityException | IllegalArgumentException | NullPointerException | IOException ex) {
            throw new InvalidIdTokenException(ex);
        }
    }
}