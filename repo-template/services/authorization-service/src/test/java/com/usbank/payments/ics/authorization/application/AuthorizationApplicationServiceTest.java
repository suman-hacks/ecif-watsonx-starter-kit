package com.usbank.payments.ics.authorization.application;

import com.usbank.payments.ics.authorization.domain.AuthorizationDecision;
import com.usbank.payments.ics.authorization.domain.AuthorizationPolicyEvaluator;
import com.usbank.payments.ics.authorization.infrastructure.AuditPublisher;
import com.usbank.payments.ics.authorization.infrastructure.LegacyAuthorizationGateway;
import com.usbank.payments.shared.contracts.AuthorizationRequest;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class AuthorizationApplicationServiceTest {

    @Test
    void publishesAuditOnApproval() {
        AuthorizationPolicyEvaluator evaluator = mock(AuthorizationPolicyEvaluator.class);
        LegacyAuthorizationGateway gateway = mock(LegacyAuthorizationGateway.class);
        AuditPublisher publisher = mock(AuditPublisher.class);
        AuthorizationApplicationService service = new AuthorizationApplicationService(evaluator, gateway, publisher);

        AuthorizationRequest request = new AuthorizationRequest("411111******1111", 1000L, "USD");
        AuthorizationDecision pending = new AuthorizationDecision("PENDING_CORE", true, "ok");
        AuthorizationDecision approved = new AuthorizationDecision("00", true, "approved");

        when(evaluator.evaluate(request)).thenReturn(pending);
        when(gateway.authorize(pending)).thenReturn(approved);

        service.authorize(request);

        verify(publisher).publish(approved);
    }
}
