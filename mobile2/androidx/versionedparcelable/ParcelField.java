package androidx.versionedparcelable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD})
public @interface ParcelField {
  int value();
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/androidx/versionedparcelable/ParcelField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */