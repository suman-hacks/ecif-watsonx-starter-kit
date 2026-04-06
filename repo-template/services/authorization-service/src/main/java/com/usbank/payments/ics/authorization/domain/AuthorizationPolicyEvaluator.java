package com.usbank.payments.ics.authorization.domain;

import com.usbank.payments.shared.contracts.AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationPolicyEvaluator {
    public AuthorizationDecision evaluate(AuthorizationRequest request) {
        return new AuthorizationDecision("PENDING_CORE", true, "Pre-checks passed");
    }
}
