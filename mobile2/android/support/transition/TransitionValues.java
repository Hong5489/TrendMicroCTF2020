package android.support.transition;

import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransitionValues {
  final ArrayList<Transition> mTargetedTransitions = new ArrayList<>();
  
  public final Map<String, Object> values = new HashMap<>();
  
  public View view;
  
  public boolean equals(Object paramObject) {
    return (paramObject instanceof TransitionValues && this.view == ((TransitionValues)paramObject).view && this.values.equals(((TransitionValues)paramObject).values));
  }
  
  public int hashCode() {
    return this.view.hashCode() * 31 + this.values.hashCode();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("TransitionValues@");
    stringBuilder.append(Integer.toHexString(hashCode()));
    stringBuilder.append(":\n");
    String str2 = stringBuilder.toString();
    stringBuilder = new StringBuilder();
    stringBuilder.append(str2);
    stringBuilder.append("    view = ");
    stringBuilder.append(this.view);
    stringBuilder.append("\n");
    str2 = stringBuilder.toString();
    stringBuilder = new StringBuilder();
    stringBuilder.append(str2);
    stringBuilder.append("    values:");
    String str1 = stringBuilder.toString();
    for (String str : this.values.keySet()) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str1);
      stringBuilder1.append("    ");
      stringBuilder1.append(str);
      stringBuilder1.append(": ");
      stringBuilder1.append(this.values.get(str));
      stringBuilder1.append("\n");
      str1 = stringBuilder1.toString();
    } 
    return str1;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/transition/TransitionValues.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */