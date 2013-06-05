package io.katamari.env;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import io.katamari.utils.Crypto;

import java.util.Map;

public class Session {

  private Map<String, String> fields = null;

  public void clear() {
    if (fields != null) {
      fields.clear();
      fields = null;
    }
  }

  public void put(String name, String value) {
    if (fields == null) fields = Maps.newHashMapWithExpectedSize(2);
    fields.put(name, value);
  }

  public void remove(String name) {
    if (fields != null && fields.containsKey(name)) {
      fields.remove(name);
    }
  }

  public boolean contains(String name) {
    if (fields != null) {
      return fields.containsKey(name);
    }
    return false;
  }

  public String get(String key) {
    return contains(key) ? fields.get(key) : null;
  }

  public boolean isEmpty() {
    return fields == null || fields.size() == 0;
  }

  public String serialize(String secret) {
    if (isEmpty()) return "";
    String serializedSession = Joiner.on("&").withKeyValueSeparator("=").join(fields);
    String sign = Crypto.sign(serializedSession, secret.getBytes());
    return sign + serializedSession;
  }

  public void deserialize(String secret, String value) {
    if (!Strings.isNullOrEmpty(value)) {
      String sentHash = value.substring(0, 40);
      String computedHash = Crypto.sign(value.substring(40), secret.getBytes());
      if (sentHash.equals(computedHash)) {
        fields = Maps.newHashMap(Splitter.on("&").omitEmptyStrings().trimResults().withKeyValueSeparator("=").split(value.substring(40)));
      }
    }
  }
}
