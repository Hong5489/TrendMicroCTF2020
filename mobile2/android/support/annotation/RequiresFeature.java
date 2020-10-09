package android.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface RequiresFeature {
  String enforcement();
  
  String name();
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/annotation/RequiresFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */