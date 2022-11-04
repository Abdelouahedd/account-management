package org.ae.account.management.exception;

public class UsernameAlreadyUsedException extends RuntimeException{
  public UsernameAlreadyUsedException() {
    super("Login name already used!");
  }
}
