package org.ae.account.management.repository;

import org.ae.account.management.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
  Optional<Authority> findByName(String name);
}
