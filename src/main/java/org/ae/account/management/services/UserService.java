package org.ae.account.management.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ae.account.management.domain.Authority;
import org.ae.account.management.domain.User;
import org.ae.account.management.dto.AdminUserDTO;
import org.ae.account.management.exception.EmailAlreadyUsedException;
import org.ae.account.management.exception.InvalidPasswordException;
import org.ae.account.management.exception.UsernameAlreadyUsedException;
import org.ae.account.management.repository.AuthorityRepository;
import org.ae.account.management.repository.UserRepository;
import org.ae.account.management.util.AuthoritiesConstants;
import org.ae.account.management.util.Constants;
import org.ae.account.management.util.RandomUtil;
import org.ae.account.management.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthorityRepository authorityRepository;

  public Optional<User> activateRegistration(String key) {
    log.debug("Activating user for activation key {}", key);
    return userRepository
      .findByActivationKey(key)
      .map(user -> {
        // activate given user for the registration key.
        user.setActivated(true);
        user.setActivationKey(null);
        userRepository.save(user);
        log.debug("Activated user: {}", user);
        return user;
      });
  }

  public Optional<User> completePasswordReset(String newPassword, String key) {
    log.debug("Reset user password for reset key {}", key);
    return userRepository
      .findByResetKey(key)
      .filter(user -> user.getResetDate().isAfter(Instant.now().minus(1, ChronoUnit.DAYS)))
      .map(user -> {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetKey(null);
        user.setResetDate(null);
        return user;
      });
  }

  public Optional<User> requestPasswordReset(String mail) {
    return userRepository
      .findByEmailIgnoreCase(mail)
      .filter(User::isActivated)
      .map(user -> {
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        return user;
      });
  }

  public User registerUser(AdminUserDTO userDTO, String password) {
    userRepository
      .findByLogin(userDTO.getLogin().toLowerCase())
      .ifPresent(existingUser -> {
        boolean removed = removeNonActivatedUser(existingUser);
        if (!removed) {
          throw new UsernameAlreadyUsedException();
        }
      });
    userRepository
      .findByEmailIgnoreCase(userDTO.getEmail())
      .ifPresent(existingUser -> {
        boolean removed = removeNonActivatedUser(existingUser);
        if (!removed) {
          throw new EmailAlreadyUsedException();
        }
      });
    User newUser = new User();
    String encryptedPassword = passwordEncoder.encode(password);
    newUser.setLogin(userDTO.getLogin().toLowerCase());
    // new user gets initially a generated password
    newUser.setPassword(encryptedPassword);
    newUser.setFirstName(userDTO.getFirstName());
    newUser.setLastName(userDTO.getLastName());
    if (userDTO.getEmail() != null) {
      newUser.setEmail(userDTO.getEmail().toLowerCase());
    }
    newUser.setLangKey(userDTO.getLangKey());
    // new user is not active
    newUser.setActivated(false);
    // new user gets registration key
    newUser.setActivationKey(RandomUtil.generateActivationKey());
    Set<Authority> authorities = new HashSet<>();
    authorityRepository.findByName(AuthoritiesConstants.USER).ifPresent(authorities::add);
    newUser.setAuthorities(authorities);
    userRepository.save(newUser);
    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  private boolean removeNonActivatedUser(User existingUser) {
    if (existingUser.isActivated()) {
      return false;
    }
    userRepository.delete(existingUser);
    userRepository.flush();
    return true;
  }

  public User createUser(AdminUserDTO userDTO) {
    User user = new User();
    user.setLogin(userDTO.getLogin().toLowerCase());
    user.setFirstName(userDTO.getFirstName());
    user.setLastName(userDTO.getLastName());
    if (userDTO.getEmail() != null) {
      user.setEmail(userDTO.getEmail().toLowerCase());
    }
    if (userDTO.getLangKey() == null) {
      user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
    } else {
      user.setLangKey(userDTO.getLangKey());
    }
    String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
    user.setPassword(encryptedPassword);
    user.setResetKey(RandomUtil.generateResetKey());
    user.setResetDate(Instant.now());
    user.setActivated(true);
    if (userDTO.getAuthorities() != null) {
      Set<Authority> authorities = userDTO
        .getAuthorities()
        .stream()
        .map(authorityRepository::findByName)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toSet());
      user.setAuthorities(authorities);
    }
    userRepository.save(user);
    log.debug("Created Information for User: {}", user);
    return user;
  }

  /**
   * Update all information for a specific user, and return the modified user.
   *
   * @param userDTO user to update.
   * @return updated user.
   */
  public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
    return Optional
      .of(userRepository.findById(userDTO.getId()))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .map(user -> {
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
          user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setActivated(userDTO.isActivated());
        user.setLangKey(userDTO.getLangKey());
        Set<Authority> managedAuthorities = user.getAuthorities();
        managedAuthorities.clear();
        userDTO
          .getAuthorities()
          .stream()
          .map(authorityRepository::findByName)
          .filter(Optional::isPresent)
          .map(Optional::get)
          .forEach(managedAuthorities::add);
        userRepository.save(user);
        log.debug("Changed Information for User: {}", user);
        return user;
      })
      .map(AdminUserDTO::new);
  }

  public void deleteUser(String login) {
    userRepository
      .findByLogin(login)
      .ifPresent(user -> {
        userRepository.delete(user);
        log.debug("Deleted User: {}", user);
      });
  }

  /**
   * Update basic information (first name, last name, email, language) for the current user.
   *
   * @param firstName first name of user.
   * @param lastName  last name of user.
   * @param email     email id of user.
   * @param langKey   language key.
   */
  public void updateUser(String firstName, String lastName, String email, String langKey) {
    SecurityUtils
      .getCurrentUserLogin()
      .flatMap(userRepository::findByLogin)
      .ifPresent(user -> {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        if (email != null) {
          user.setEmail(email.toLowerCase());
        }
        user.setLangKey(langKey);
        userRepository.save(user);
        log.debug("Changed Information for User: {}", user);
      });
  }

  @Transactional
  public void changePassword(String currentClearTextPassword, String newPassword) {
    SecurityUtils
      .getCurrentUserLogin()
      .flatMap(userRepository::findByLogin)
      .ifPresent(user -> {
        String currentEncryptedPassword = user.getPassword();
        if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
          throw new InvalidPasswordException();
        }
        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encryptedPassword);
        userRepository.save(user);
        log.debug("Changed password for User: {}", user);
      });
  }

  @Transactional(readOnly = true)
  public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
    return userRepository.findAll(pageable).map(AdminUserDTO::new);
  }


  @Transactional(readOnly = true)
  public Optional<User> getUserWithAuthoritiesByLogin(String login) {
    return userRepository.findByLogin(login);
  }

  @Transactional(readOnly = true)
  public Optional<User> getUserWithAuthorities() {
    return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findByLogin);
  }

  /**
   * Not activated users should be automatically deleted after 3 days.
   * <p>
   * This is scheduled to get fired everyday, at 01:00 (am).
   */
  @Scheduled(cron = "0 0 1 * * ?")
  public void removeNotActivatedUsers() {
    userRepository
      .findByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
      .forEach(user -> {
        log.debug("Deleting not activated user {}", user.getLogin());
        userRepository.delete(user);
      });
  }

  /**
   * Gets a list of all the authorities.
   *
   * @return a list of all the authorities.
   */
  @Transactional(readOnly = true)
  public List<String> getAuthorities() {
    return authorityRepository.findAll().stream().map(Authority::getName).toList();
  }


}
