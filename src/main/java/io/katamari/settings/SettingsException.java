package io.katamari.settings;

public class SettingsException extends RuntimeException {

  public SettingsException(String message, Exception e) {
    super(message, e);
  }

  public SettingsException(Exception e) {
    super(e);
  }

}
