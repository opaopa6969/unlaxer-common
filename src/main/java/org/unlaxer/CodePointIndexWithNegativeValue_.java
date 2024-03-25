package org.unlaxer;

import org.unlaxer.base.IntegerValue;

public class CodePointIndexWithNegativeValue_ extends IntegerValue<CodePointIndexWithNegativeValue_>{

  public CodePointIndexWithNegativeValue_(int value) {
    super(value);
  }
  
  public CodePointIndexWithNegativeValue_(IntegerValue<?> value) {
    super(value);
  }
  
//  /**
//   * @return CodePointIndex
//   * @throws if code less than 0 then throw IllegalArgumentException
//   */
//  public CodePointIndex toCodePointIndex() {
//    return new CodePointIndex(value());
//  }
  
  @Override
  public CodePointIndexWithNegativeValue_ create(int i) {
    return new CodePointIndexWithNegativeValue_(i);
  }

  @Override
  public CodePointIndexWithNegativeValue_ create(IntegerValue<?> i) {
    return new CodePointIndexWithNegativeValue_(i);
  }

}