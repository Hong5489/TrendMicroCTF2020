package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.StateSet;
import java.util.ArrayList;

public final class StateListAnimator {
  private final Animator.AnimatorListener animationListener = (Animator.AnimatorListener)new AnimatorListenerAdapter() {
      public void onAnimationEnd(Animator param1Animator) {
        if (StateListAnimator.this.runningAnimator == param1Animator)
          StateListAnimator.this.runningAnimator = null; 
      }
    };
  
  private Tuple lastMatch = null;
  
  ValueAnimator runningAnimator = null;
  
  private final ArrayList<Tuple> tuples = new ArrayList<>();
  
  private void cancel() {
    ValueAnimator valueAnimator = this.runningAnimator;
    if (valueAnimator != null) {
      valueAnimator.cancel();
      this.runningAnimator = null;
    } 
  }
  
  private void start(Tuple paramTuple) {
    ValueAnimator valueAnimator = paramTuple.animator;
    this.runningAnimator = valueAnimator;
    valueAnimator.start();
  }
  
  public void addState(int[] paramArrayOfint, ValueAnimator paramValueAnimator) {
    Tuple tuple = new Tuple(paramArrayOfint, paramValueAnimator);
    paramValueAnimator.addListener(this.animationListener);
    this.tuples.add(tuple);
  }
  
  public void jumpToCurrentState() {
    ValueAnimator valueAnimator = this.runningAnimator;
    if (valueAnimator != null) {
      valueAnimator.end();
      this.runningAnimator = null;
    } 
  }
  
  public void setState(int[] paramArrayOfint) {
    Tuple tuple3;
    Tuple tuple2 = null;
    int i = this.tuples.size();
    byte b = 0;
    while (true) {
      tuple3 = tuple2;
      if (b < i) {
        tuple3 = this.tuples.get(b);
        if (StateSet.stateSetMatches(tuple3.specs, paramArrayOfint))
          break; 
        b++;
        continue;
      } 
      break;
    } 
    Tuple tuple1 = this.lastMatch;
    if (tuple3 == tuple1)
      return; 
    if (tuple1 != null)
      cancel(); 
    this.lastMatch = tuple3;
    if (tuple3 != null)
      start(tuple3); 
  }
  
  static class Tuple {
    final ValueAnimator animator;
    
    final int[] specs;
    
    Tuple(int[] param1ArrayOfint, ValueAnimator param1ValueAnimator) {
      this.specs = param1ArrayOfint;
      this.animator = param1ValueAnimator;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/widget/StateListAnimator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */