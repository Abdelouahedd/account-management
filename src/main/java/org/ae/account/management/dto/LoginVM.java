package org.ae.account.management.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record LoginVM(@NotNull @Size(min = 1, max = 50) String username,
                      @NotNull @Size(min = 4, max = 100) String password,
                      boolean rememberMe) {
}
