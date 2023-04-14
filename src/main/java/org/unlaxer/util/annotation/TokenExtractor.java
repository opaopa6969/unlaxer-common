package org.unlaxer.util.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * annotate method for token extractor in Parser
 **/
public @interface TokenExtractor{
	
	boolean specifiedTokenIsThisParser() default true;
	
	boolean isExtactedList() default false;
}
