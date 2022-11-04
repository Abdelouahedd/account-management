package org.ae.account.management.repository;

import org.ae.account.management.domain.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByActivationKey(String activationKey);
  Optional<User> findByResetKey(String restKey);
  Optional<User> findByEmailIgnoreCase(String email);
  Optional<User> findByLogin(String login);
  List<User> findByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);
}
