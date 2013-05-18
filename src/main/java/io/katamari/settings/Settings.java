package io.katamari.settings;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import io.katamari.settings.types.Booleans;
import io.katamari.settings.types.ByteSizeValue;
import io.katamari.settings.types.SizeValue;
import io.katamari.settings.types.TimeValue;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import static io.katamari.settings.types.ByteSizeValue.parseBytesSizeValue;
import static io.katamari.settings.types.SizeValue.parseSizeValue;
import static io.katamari.settings.types.TimeValue.parseTimeValue;

public class Settings {

  private Map<String, String> settings = Maps.newHashMap();

  private Settings(Object yaml) {
    if (yaml instanceof Map) {
      parseMap("", (Map) yaml);
    }
  }

  private void parseMap(String prefix, Map<Object, Object> yaml) {
    for (Map.Entry<Object, Object> entry : yaml.entrySet()) {
      String key = entry.getKey().toString();

      if (entry.getValue() instanceof String ||
          entry.getValue() instanceof Boolean ||
          entry.getValue() instanceof Number) {
        settings.put(createPrefix(prefix) + key, entry.getValue().toString());
      } else if (entry.getValue() instanceof Map) {
        parseMap(createPrefix(key), (Map<Object, Object>) entry.getValue());
      }
      // Maybe add list support here as well
    }
  }

  private String createPrefix(String prefix) {
    if (prefix.length() == 0) {
      return "";
    }

    return prefix.endsWith(".") ? prefix : prefix + ".";
  }

  public static Settings load(InputStream is) {
    Yaml yaml = new Yaml();
    return new Settings(yaml.load(is));
  }

  public static Settings load(File file) {
    try {
      return load(new FileInputStream(file));
    } catch (FileNotFoundException e) {
      throw new SettingsException(e);
    }
  }

  public static Settings load(String filePath) {
    return load(new File(filePath));
  }


  public String getAsString(String key, String defaultValue) {
    String setting = getAsString(key);
    return setting == null ? defaultValue : setting;
  }

  public String getAsString(String key) {
    if (settings.containsKey(key)) {
      String setting = settings.get(key);
      if (!Strings.isNullOrEmpty(setting))
        return setting;
    }

    return null;
  }

  public Float getAsFloat(String setting, Float defaultValue) {
    String sValue = getAsString(setting);
    if (sValue == null) {
      return defaultValue;
    }
    try {
      return Float.parseFloat(sValue);
    } catch (NumberFormatException e) {
      throw new SettingsException("Failed to parse float setting [" + setting + "] with value [" + sValue + "]", e);
    }
  }

  public Double getAsDouble(String setting, Double defaultValue) {
    String sValue = getAsString(setting);
    if (sValue == null) {
      return defaultValue;
    }
    try {
      return Double.parseDouble(sValue);
    } catch (NumberFormatException e) {
      throw new SettingsException("Failed to parse double setting [" + setting + "] with value [" + sValue + "]", e);
    }
  }

  public Integer getAsInt(String setting, Integer defaultValue) {
    String sValue = getAsString(setting);
    if (sValue == null) {
      return defaultValue;
    }
    try {
      return Integer.parseInt(sValue);
    } catch (NumberFormatException e) {
      throw new SettingsException("Failed to parse int setting [" + setting + "] with value [" + sValue + "]", e);
    }
  }

  public Long getAsLong(String setting, Long defaultValue) {
    String sValue = getAsString(setting);
    if (sValue == null) {
      return defaultValue;
    }
    try {
      return Long.parseLong(sValue);
    } catch (NumberFormatException e) {
      throw new SettingsException("Failed to parse long setting [" + setting + "] with value [" + sValue + "]", e);
    }
  }

  public Boolean getAsBoolean(String setting, Boolean defaultValue) {
    return Booleans.parseBoolean(getAsString(setting), defaultValue);
  }

  public TimeValue getAsTime(String setting, TimeValue defaultValue) {
    return parseTimeValue(getAsString(setting), defaultValue);
  }

  public ByteSizeValue getAsBytesSize(String setting, ByteSizeValue defaultValue) throws SettingsException {
    return parseBytesSizeValue(getAsString(setting), defaultValue);
  }

  public SizeValue getAsSize(String setting, SizeValue defaultValue) throws SettingsException {
    return parseSizeValue(getAsString(setting), defaultValue);
  }

  public Settings componentSettings(String prefix) {
    if (!prefix.endsWith(".")) {
      prefix += ".";
    }

    Map<String, String> componentsSettings = Maps.newHashMap();
    for (Map.Entry<String, String> entry : settings.entrySet()) {
      if (entry.getKey().startsWith(prefix)) {
        componentsSettings.put(entry.getKey().substring(prefix.length()), entry.getValue());
      }
    }

    return new Settings(componentsSettings);
  }

  public String toString() {
    return settings.toString();
  }

  public static SettingsBuilder builder() {
    return new SettingsBuilder();
  }


  public static class SettingsBuilder {

    private Map<String, String> settings = Maps.newHashMap();

    public SettingsBuilder() {}

    public SettingsBuilder put(String key, String value) {
      settings.put(key, value);
      return this;
    }

    public Settings build() {
      return new Settings(settings);
    }

  }

}
