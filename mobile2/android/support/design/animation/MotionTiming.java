package android.support.design.animation;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;

public class MotionTiming {
  private long delay = 0L;
  
  private long duration = 300L;
  
  private TimeInterpolator interpolator = null;
  
  private int repeatCount = 0;
  
  private int repeatMode = 1;
  
  public MotionTiming(long paramLong1, long paramLong2) {
    this.delay = paramLong1;
    this.duration = paramLong2;
  }
  
  public MotionTiming(long paramLong1, long paramLong2, TimeInterpolator paramTimeInterpolator) {
    this.delay = paramLong1;
    this.duration = paramLong2;
    this.interpolator = paramTimeInterpolator;
  }
  
  static MotionTiming createFromAnimator(ValueAnimator paramValueAnimator) {
    MotionTiming motionTiming = new MotionTiming(paramValueAnimator.getStartDelay(), paramValueAnimator.getDuration(), getInterpolatorCompat(paramValueAnimator));
    motionTiming.repeatCount = paramValueAnimator.getRepeatCount();
    motionTiming.repeatMode = paramValueAnimator.getRepeatMode();
    return motionTiming;
  }
  
  private static TimeInterpolator getInterpolatorCompat(ValueAnimator paramValueAnimator) {
    TimeInterpolator timeInterpolator = paramValueAnimator.getInterpolator();
    return (timeInterpolator instanceof android.view.animation.AccelerateDecelerateInterpolator || timeInterpolator == null) ? AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR : ((timeInterpolator instanceof android.view.animation.AccelerateInterpolator) ? AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR : ((timeInterpolator instanceof android.view.animation.DecelerateInterpolator) ? AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR : timeInterpolator));
  }
  
  public void apply(Animator paramAnimator) {
    paramAnimator.setStartDelay(getDelay());
    paramAnimator.setDuration(getDuration());
    paramAnimator.setInterpolator(getInterpolator());
    if (paramAnimator instanceof ValueAnimator) {
      ((ValueAnimator)paramAnimator).setRepeatCount(getRepeatCount());
      ((ValueAnimator)paramAnimator).setRepeatMode(getRepeatMode());
    } 
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (paramObject == null || getClass() != paramObject.getClass())
      return false; 
    paramObject = paramObject;
    return (getDelay() != paramObject.getDelay()) ? false : ((getDuration() != paramObject.getDuration()) ? false : ((getRepeatCount() != paramObject.getRepeatCount()) ? false : ((getRepeatMode() != paramObject.getRepeatMode()) ? false : getInterpolator().getClass().equals(paramObject.getInterpolator().getClass()))));
  }
  
  public long getDelay() {
    return this.delay;
  }
  
  public long getDuration() {
    return this.duration;
  }
  
  public TimeInterpolator getInterpolator() {
    TimeInterpolator timeInterpolator = this.interpolator;
    if (timeInterpolator == null)
      timeInterpolator = AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR; 
    return timeInterpolator;
  }
  
  public int getRepeatCount() {
    return this.repeatCount;
  }
  
  public int getRepeatMode() {
    return this.repeatMode;
  }
  
  public int hashCode() {
    return ((((int)(getDelay() ^ getDelay() >>> 32L) * 31 + (int)(getDuration() ^ getDuration() >>> 32L)) * 31 + getInterpolator().getClass().hashCode()) * 31 + getRepeatCount()) * 31 + getRepeatMode();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('\n');
    stringBuilder.append(getClass().getName());
    stringBuilder.append('{');
    stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    stringBuilder.append(" delay: ");
    stringBuilder.append(getDelay());
    stringBuilder.append(" duration: ");
    stringBuilder.append(getDuration());
    stringBuilder.append(" interpolator: ");
    stringBuilder.append(getInterpolator().getClass());
    stringBuilder.append(" repeatCount: ");
    stringBuilder.append(getRepeatCount());
    stringBuilder.append(" repeatMode: ");
    stringBuilder.append(getRepeatMode());
    stringBuilder.append("}\n");
    return stringBuilder.toString();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/animation/MotionTiming.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */