package com.usbank.payments.shared.contracts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthorizationRequest(
        @NotBlank String maskedPan,
        @NotNull Long amountMinor,
        @NotBlank String currencyCode) {
}
