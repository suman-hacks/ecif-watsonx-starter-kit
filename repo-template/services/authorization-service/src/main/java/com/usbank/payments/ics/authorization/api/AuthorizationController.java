package com.usbank.payments.ics.authorization.api;

import com.usbank.payments.ics.authorization.application.AuthorizationApplicationService;
import com.usbank.payments.ics.authorization.domain.AuthorizationDecision;
import com.usbank.payments.shared.contracts.AuthorizationRequest;
import com.usbank.payments.shared.contracts.AuthorizationResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/authorizations")
public class AuthorizationController {

    private final AuthorizationApplicationService service;

    public AuthorizationController(AuthorizationApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public AuthorizationResponse authorize(@Valid @RequestBody AuthorizationRequest request) {
        AuthorizationDecision decision = service.authorize(request);
        return AuthorizationResponse.from(decision);
    }
}
