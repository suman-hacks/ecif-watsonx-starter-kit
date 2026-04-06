package com.usbank.payments.shared.contracts;

import com.usbank.payments.ics.authorization.domain.AuthorizationDecision;

public record AuthorizationResponse(String decisionCode, boolean approved, String reason) {
    public static AuthorizationResponse from(AuthorizationDecision decision) {
        return new AuthorizationResponse(decision.decisionCode(), decision.approved(), decision.reason());
    }
}
