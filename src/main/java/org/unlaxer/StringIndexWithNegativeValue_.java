package org.unlaxer;

import org.unlaxer.base.IntegerValue;

public class StringIndexWithNegativeValue_ extends IntegerValue<StringIndexWithNegativeValue_>{

  public StringIndexWithNegativeValue_(int value) {
    super(value);
  }
  
  public StringIndexWithNegativeValue_(IntegerValue<?> value) {
    super(value);
  }
  
//  public StringIndex toStringIndex() {
//    return new StringIndex(value());
//  }
  
  @Override
  public StringIndexWithNegativeValue_ create(int i) {
    return new StringIndexWithNegativeValue_(i);
  }

  @Override
  public StringIndexWithNegativeValue_ create(IntegerValue<?> i) {
    return new StringIndexWithNegativeValue_(i);
  }

  
}