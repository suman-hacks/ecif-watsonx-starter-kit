package com.usbank.payments.ics.authorization.domain;

public record AuthorizationDecision(String decisionCode, boolean approved, String reason) {
    public boolean isApproved() {
        return approved;
    }
}
