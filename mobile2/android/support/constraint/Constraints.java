package android.support.constraint;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

public class Constraints extends ViewGroup {
  public static final String TAG = "Constraints";
  
  ConstraintSet myConstraintSet;
  
  public Constraints(Context paramContext) {
    super(paramContext);
    setVisibility(8);
  }
  
  public Constraints(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramAttributeSet);
    setVisibility(8);
  }
  
  public Constraints(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramAttributeSet);
    setVisibility(8);
  }
  
  private void init(AttributeSet paramAttributeSet) {
    Log.v("Constraints", " ################# init");
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams(-2, -2);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (ViewGroup.LayoutParams)new ConstraintLayout.LayoutParams(paramLayoutParams);
  }
  
  public ConstraintSet getConstraintSet() {
    if (this.myConstraintSet == null)
      this.myConstraintSet = new ConstraintSet(); 
    this.myConstraintSet.clone(this);
    return this.myConstraintSet;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {}
  
  public static class LayoutParams extends ConstraintLayout.LayoutParams {
    public float alpha = 1.0F;
    
    public boolean applyElevation = false;
    
    public float elevation = 0.0F;
    
    public float rotation = 0.0F;
    
    public float rotationX = 0.0F;
    
    public float rotationY = 0.0F;
    
    public float scaleX = 1.0F;
    
    public float scaleY = 1.0F;
    
    public float transformPivotX = 0.0F;
    
    public float transformPivotY = 0.0F;
    
    public float translationX = 0.0F;
    
    public float translationY = 0.0F;
    
    public float translationZ = 0.0F;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.ConstraintSet);
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.ConstraintSet_android_alpha) {
          this.alpha = typedArray.getFloat(j, this.alpha);
        } else if (j == R.styleable.ConstraintSet_android_elevation) {
          this.elevation = typedArray.getFloat(j, this.elevation);
          this.applyElevation = true;
        } else if (j == R.styleable.ConstraintSet_android_rotationX) {
          this.rotationX = typedArray.getFloat(j, this.rotationX);
        } else if (j == R.styleable.ConstraintSet_android_rotationY) {
          this.rotationY = typedArray.getFloat(j, this.rotationY);
        } else if (j == R.styleable.ConstraintSet_android_rotation) {
          this.rotation = typedArray.getFloat(j, this.rotation);
        } else if (j == R.styleable.ConstraintSet_android_scaleX) {
          this.scaleX = typedArray.getFloat(j, this.scaleX);
        } else if (j == R.styleable.ConstraintSet_android_scaleY) {
          this.scaleY = typedArray.getFloat(j, this.scaleY);
        } else if (j == R.styleable.ConstraintSet_android_transformPivotX) {
          this.transformPivotX = typedArray.getFloat(j, this.transformPivotX);
        } else if (j == R.styleable.ConstraintSet_android_transformPivotY) {
          this.transformPivotY = typedArray.getFloat(j, this.transformPivotY);
        } else if (j == R.styleable.ConstraintSet_android_translationX) {
          this.translationX = typedArray.getFloat(j, this.translationX);
        } else if (j == R.styleable.ConstraintSet_android_translationY) {
          this.translationY = typedArray.getFloat(j, this.translationY);
        } else if (j == R.styleable.ConstraintSet_android_translationZ) {
          this.translationX = typedArray.getFloat(j, this.translationZ);
        } 
      } 
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/constraint/Constraints.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */