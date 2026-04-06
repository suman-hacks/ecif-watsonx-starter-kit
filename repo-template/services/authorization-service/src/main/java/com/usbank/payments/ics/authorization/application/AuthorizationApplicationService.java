package com.usbank.payments.ics.authorization.application;

import com.usbank.payments.ics.authorization.domain.AuthorizationDecision;
import com.usbank.payments.ics.authorization.domain.AuthorizationPolicyEvaluator;
import com.usbank.payments.ics.authorization.infrastructure.AuditPublisher;
import com.usbank.payments.ics.authorization.infrastructure.LegacyAuthorizationGateway;
import com.usbank.payments.shared.contracts.AuthorizationRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationApplicationService {

    private final AuthorizationPolicyEvaluator evaluator;
    private final LegacyAuthorizationGateway gateway;
    private final AuditPublisher auditPublisher;

    public AuthorizationApplicationService(
            AuthorizationPolicyEvaluator evaluator,
            LegacyAuthorizationGateway gateway,
            AuditPublisher auditPublisher) {
        this.evaluator = evaluator;
        this.gateway = gateway;
        this.auditPublisher = auditPublisher;
    }

    public AuthorizationDecision authorize(AuthorizationRequest request) {
        AuthorizationDecision decision = evaluator.evaluate(request);
        AuthorizationDecision finalDecision = gateway.authorize(decision);
        if (finalDecision.isApproved()) {
            auditPublisher.publish(finalDecision);
        }
        return finalDecision;
    }
}
