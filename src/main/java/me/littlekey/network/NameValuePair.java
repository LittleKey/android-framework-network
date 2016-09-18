package me.littlekey.network;

import java.io.Serializable;

/**
 * Created by nengxiangzhou on 15/6/19.
 */
public class NameValuePair implements Serializable {
  private final String mName;
  private final String mValue;

  /**
   * Default Constructor taking a name and a value. The value may be null.
   *
   * @param name The name.
   * @param value The value.
   */
  public NameValuePair(final String name, final String value) {
    super();
    if (name == null) {
      throw new IllegalArgumentException("Name may not be null");
    }
    this.mName = name;
    this.mValue = value;
  }

  public NameValuePair(String... pairString) {
    this(pairString[0], pairString[1]);
  }

  public String getName() {
    return this.mName;
  }

  public String getValue() {
    return this.mValue;
  }

  @Override
  public String toString() {
    // don't call complex default formatting for a simple toString

    if (this.mValue == null) {
      return mName;
    } else {
      int len = this.mName.length() + 1 + this.mValue.length();
      StringBuilder buffer = new StringBuilder(len);
      buffer.append(this.mName);
      buffer.append("=");
      buffer.append(this.mValue);
      return buffer.toString();
    }
  }
}
