package ru.jordosi.freelance_tracker.exception;

public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String message) {
    super(message != null ? message : "Unknown error");
  }
}
