package org.ae.account.management.exception;

public class InvalidPasswordException extends RuntimeException {
  public InvalidPasswordException() {
    super("Incorrect password");
  }

}
