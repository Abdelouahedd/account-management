package org.ae.account.management.exception;

import org.ae.account.management.util.ErrorConstants;

public class EmailAlreadyUsedException extends RuntimeException{
  public EmailAlreadyUsedException() {
    super("Email is already in use!");
  }
}
