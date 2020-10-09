package android.support.design.animation;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class MotionSpec {
  private static final String TAG = "MotionSpec";
  
  private final SimpleArrayMap<String, MotionTiming> timings = new SimpleArrayMap();
  
  private static void addTimingFromAnimator(MotionSpec paramMotionSpec, Animator paramAnimator) {
    ObjectAnimator objectAnimator;
    if (paramAnimator instanceof ObjectAnimator) {
      objectAnimator = (ObjectAnimator)paramAnimator;
      paramMotionSpec.setTiming(objectAnimator.getPropertyName(), MotionTiming.createFromAnimator((ValueAnimator)objectAnimator));
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Animator must be an ObjectAnimator: ");
    stringBuilder.append(objectAnimator);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public static MotionSpec createFromAttribute(Context paramContext, TypedArray paramTypedArray, int paramInt) {
    if (paramTypedArray.hasValue(paramInt)) {
      paramInt = paramTypedArray.getResourceId(paramInt, 0);
      if (paramInt != 0)
        return createFromResource(paramContext, paramInt); 
    } 
    return null;
  }
  
  public static MotionSpec createFromResource(Context paramContext, int paramInt) {
    try {
      Animator animator = AnimatorInflater.loadAnimator(paramContext, paramInt);
      if (animator instanceof AnimatorSet)
        return createSpecFromAnimators(((AnimatorSet)animator).getChildAnimations()); 
      if (animator != null) {
        ArrayList<Animator> arrayList = new ArrayList();
        this();
        arrayList.add(animator);
        return createSpecFromAnimators(arrayList);
      } 
      return null;
    } catch (Exception exception) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Can't load animation resource ID #0x");
      stringBuilder.append(Integer.toHexString(paramInt));
      Log.w("MotionSpec", stringBuilder.toString(), exception);
      return null;
    } 
  }
  
  private static MotionSpec createSpecFromAnimators(List<Animator> paramList) {
    MotionSpec motionSpec = new MotionSpec();
    byte b = 0;
    int i = paramList.size();
    while (b < i) {
      addTimingFromAnimator(motionSpec, paramList.get(b));
      b++;
    } 
    return motionSpec;
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (paramObject == null || getClass() != paramObject.getClass())
      return false; 
    paramObject = paramObject;
    return this.timings.equals(((MotionSpec)paramObject).timings);
  }
  
  public MotionTiming getTiming(String paramString) {
    if (hasTiming(paramString))
      return (MotionTiming)this.timings.get(paramString); 
    throw new IllegalArgumentException();
  }
  
  public long getTotalDuration() {
    long l = 0L;
    byte b = 0;
    int i = this.timings.size();
    while (b < i) {
      MotionTiming motionTiming = (MotionTiming)this.timings.valueAt(b);
      l = Math.max(l, motionTiming.getDelay() + motionTiming.getDuration());
      b++;
    } 
    return l;
  }
  
  public boolean hasTiming(String paramString) {
    boolean bool;
    if (this.timings.get(paramString) != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int hashCode() {
    return this.timings.hashCode();
  }
  
  public void setTiming(String paramString, MotionTiming paramMotionTiming) {
    this.timings.put(paramString, paramMotionTiming);
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('\n');
    stringBuilder.append(getClass().getName());
    stringBuilder.append('{');
    stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    stringBuilder.append(" timings: ");
    stringBuilder.append(this.timings);
    stringBuilder.append("}\n");
    return stringBuilder.toString();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/animation/MotionSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */