package org.ae.account.management.exception;

public class LoginAlreadyUsedException extends RuntimeException {
  public LoginAlreadyUsedException() {
    super("Login name already used!");
  }
}
