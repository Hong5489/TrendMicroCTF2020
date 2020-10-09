package android.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface Size {
  long max() default 9223372036854775807L;
  
  long min() default -9223372036854775808L;
  
  long multiple() default 1L;
  
  long value() default -1L;
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/annotation/Size.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */