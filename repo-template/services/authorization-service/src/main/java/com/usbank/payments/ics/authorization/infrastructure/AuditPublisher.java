package com.usbank.payments.ics.authorization.infrastructure;

import com.usbank.payments.ics.authorization.domain.AuthorizationDecision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AuditPublisher {
    private static final Logger log = LoggerFactory.getLogger(AuditPublisher.class);

    public void publish(AuthorizationDecision decision) {
        log.info("Publishing authorization audit event for code={}", decision.decisionCode());
    }
}
