package com.usbank.payments.ics.authorization.infrastructure;

import com.usbank.payments.ics.authorization.domain.AuthorizationDecision;
import org.springframework.stereotype.Component;

@Component
public class LegacyAuthorizationGateway {
    public AuthorizationDecision authorize(AuthorizationDecision input) {
        return input;
    }
}
