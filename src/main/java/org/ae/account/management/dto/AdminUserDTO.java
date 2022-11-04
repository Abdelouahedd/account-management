package org.ae.account.management.dto;

import org.ae.account.management.domain.Authority;
import org.ae.account.management.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public record AdminUserDTO(
  Long id,
  @NotBlank
  @Size(min = 1, max = 50)
  String login,
  @Size(max = 50)
  String firstName,
  @Size(max = 50)
  String lastName,
  @Email
  @Size(min = 5, max = 254)
  String email,
  boolean activated,
  @Size(min = 2, max = 10)
  String langKey,
  String createdBy,
  Instant createdDate,
  String lastModifiedBy,
  Instant lastModifiedDate,
  Set<String> authorities
) {
  public AdminUserDTO(User user) {
    this(user.getId(),
      user.getLogin(),
      user.getFirstName(),
      user.getLastName(),
      user.getEmail(),
      user.isActivated(),
      user.getLangKey(),
      user.getCreatedBy(),
      user.getCreatedDate(),
      user.getLastModifiedBy(),
      user.getLastModifiedDate(),
      user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
  }
}
