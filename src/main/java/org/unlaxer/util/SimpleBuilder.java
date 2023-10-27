package org.unlaxer.util;

import java.util.stream.Stream;

import org.unlaxer.Source;
import org.unlaxer.StringSource;

public class SimpleBuilder implements CharSequence{

  int index;
  StringBuilder builder;

  int tabSpace = 2;
  
  String lf= new String(new char[] {10});
  String crlf= new String(new char[] {13,10});
  String cr = new String(new char[] {13});
  
  public SimpleBuilder() {
    this(0);
  }

  public SimpleBuilder(int index) {
    this(index, new StringBuilder());
  }

  public SimpleBuilder(int index, StringBuilder builder) {
    super();
    this.index = index;
    this.builder = builder;
  }

  public SimpleBuilder incTab() {
    ++index;
    return this;
  }

  public SimpleBuilder decTab() {
    --index;
    return this;

  }
  
  public SimpleBuilder append(CharSequence word) {
    builder.append(word);
    return this;
  }


  public SimpleBuilder append(String word) {
    builder.append(word);
    return this;
  }

  public SimpleBuilder withTab(String word) {
    tab();
    builder.append(word);
    return this;
  }


  public SimpleBuilder line(String word) {
    tab();
    append(word + lf);
    return this;
  }

  public SimpleBuilder lines(String lines) {
    String[] split = lines.split("\n");
    for (String line : split) {
      tab();
      append(line + lf);
    }
    return this;
  }
  
  public SimpleBuilder append(Source word) {
    builder.append(word);
    return this;
  }

  public SimpleBuilder withTab(Source word) {
    tab();
    builder.append(word);
    return this;
  }


  public SimpleBuilder line(Source word) {
    tab();
    append(word + lf);
    return this;
  }

  public SimpleBuilder lines(Source lines) {
    String[] split = lines.split("\n");
    for (String line : split) {
      tab();
      append(line + lf);
    }
    return this;
  }


  public SimpleBuilder lines(Stream<String> linesStream) {
    linesStream.forEach(this::line);
    return this;
  }

  static byte tabBytes = " ".getBytes()[0];

  private SimpleBuilder tab() {
    byte[] tabs = new byte[index];
    for (int i = 0; i < index * tabSpace; i++) {
      tabs[i] = tabBytes;
    }
    builder.append(new String(tabs));
    return this;
  }


  public SimpleBuilder w(String word) {
    word = word == null ? "" : word;
    append("\"" + word.replaceAll("\"", "\\\"") + "\"");
    return this;
  }

  public SimpleBuilder p(String word) {
    word = word == null ? "" : word;
    append("(" + word + ")");
    return this;
  }
  
  public SimpleBuilder w(Source word) {
    word = word == null ? new StringSource("") : word;
    append("\"" + word.replaceAll("\"", "\\\"") + "\"");
    return this;
  }

  public SimpleBuilder p(Source word) {
    word = word == null ? new StringSource("") : word;
    append("(" + word + ")");
    return this;
  }


  @Override
  public String toString() {
    return builder.toString();
  }
  
  public Source toSource() {
    return new StringSource(builder.toString());
  }

  public SimpleBuilder n() {
    append(lf);
    return this;
  }

  @Override
  public int length() {
    return builder.length();
  }

  @Override
  public char charAt(int index) {
    return builder.charAt(index);
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    return builder.subSequence(start,end);
  }
}