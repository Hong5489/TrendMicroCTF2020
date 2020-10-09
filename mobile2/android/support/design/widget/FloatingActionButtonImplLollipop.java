package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.design.ripple.RippleUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import java.util.ArrayList;

class FloatingActionButtonImplLollipop extends FloatingActionButtonImpl {
  private InsetDrawable insetDrawable;
  
  FloatingActionButtonImplLollipop(VisibilityAwareImageButton paramVisibilityAwareImageButton, ShadowViewDelegate paramShadowViewDelegate) {
    super(paramVisibilityAwareImageButton, paramShadowViewDelegate);
  }
  
  private Animator createElevationAnimator(float paramFloat1, float paramFloat2) {
    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.play((Animator)ObjectAnimator.ofFloat(this.view, "elevation", new float[] { paramFloat1 }).setDuration(0L)).with((Animator)ObjectAnimator.ofFloat(this.view, View.TRANSLATION_Z, new float[] { paramFloat2 }).setDuration(100L));
    animatorSet.setInterpolator(ELEVATION_ANIM_INTERPOLATOR);
    return (Animator)animatorSet;
  }
  
  public float getElevation() {
    return this.view.getElevation();
  }
  
  void getPadding(Rect paramRect) {
    if (this.shadowViewDelegate.isCompatPaddingEnabled()) {
      float f1 = this.shadowViewDelegate.getRadius();
      float f2 = getElevation() + this.pressedTranslationZ;
      int i = (int)Math.ceil(ShadowDrawableWrapper.calculateHorizontalPadding(f2, f1, false));
      int j = (int)Math.ceil(ShadowDrawableWrapper.calculateVerticalPadding(f2, f1, false));
      paramRect.set(i, j, i, j);
    } else {
      paramRect.set(0, 0, 0, 0);
    } 
  }
  
  void jumpDrawableToCurrentState() {}
  
  CircularBorderDrawable newCircularDrawable() {
    return new CircularBorderDrawableLollipop();
  }
  
  GradientDrawable newGradientDrawableForShape() {
    return new AlwaysStatefulGradientDrawable();
  }
  
  void onCompatShadowChanged() {
    updatePadding();
  }
  
  void onDrawableStateChanged(int[] paramArrayOfint) {
    if (Build.VERSION.SDK_INT == 21)
      if (this.view.isEnabled()) {
        this.view.setElevation(this.elevation);
        if (this.view.isPressed()) {
          this.view.setTranslationZ(this.pressedTranslationZ);
        } else {
          if (this.view.isFocused() || this.view.isHovered()) {
            this.view.setTranslationZ(this.hoveredFocusedTranslationZ);
            return;
          } 
          this.view.setTranslationZ(0.0F);
        } 
      } else {
        this.view.setElevation(0.0F);
        this.view.setTranslationZ(0.0F);
      }  
  }
  
  void onElevationsChanged(float paramFloat1, float paramFloat2, float paramFloat3) {
    if (Build.VERSION.SDK_INT == 21) {
      this.view.refreshDrawableState();
    } else {
      StateListAnimator stateListAnimator = new StateListAnimator();
      stateListAnimator.addState(PRESSED_ENABLED_STATE_SET, createElevationAnimator(paramFloat1, paramFloat3));
      stateListAnimator.addState(HOVERED_FOCUSED_ENABLED_STATE_SET, createElevationAnimator(paramFloat1, paramFloat2));
      stateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, createElevationAnimator(paramFloat1, paramFloat2));
      stateListAnimator.addState(HOVERED_ENABLED_STATE_SET, createElevationAnimator(paramFloat1, paramFloat2));
      AnimatorSet animatorSet = new AnimatorSet();
      ArrayList<ObjectAnimator> arrayList = new ArrayList();
      arrayList.add(ObjectAnimator.ofFloat(this.view, "elevation", new float[] { paramFloat1 }).setDuration(0L));
      if (Build.VERSION.SDK_INT >= 22 && Build.VERSION.SDK_INT <= 24)
        arrayList.add(ObjectAnimator.ofFloat(this.view, View.TRANSLATION_Z, new float[] { this.view.getTranslationZ() }).setDuration(100L)); 
      arrayList.add(ObjectAnimator.ofFloat(this.view, View.TRANSLATION_Z, new float[] { 0.0F }).setDuration(100L));
      animatorSet.playSequentially(arrayList.<Animator>toArray(new Animator[0]));
      animatorSet.setInterpolator(ELEVATION_ANIM_INTERPOLATOR);
      stateListAnimator.addState(ENABLED_STATE_SET, (Animator)animatorSet);
      stateListAnimator.addState(EMPTY_STATE_SET, createElevationAnimator(0.0F, 0.0F));
      this.view.setStateListAnimator(stateListAnimator);
    } 
    if (this.shadowViewDelegate.isCompatPaddingEnabled())
      updatePadding(); 
  }
  
  void onPaddingUpdated(Rect paramRect) {
    if (this.shadowViewDelegate.isCompatPaddingEnabled()) {
      this.insetDrawable = new InsetDrawable(this.rippleDrawable, paramRect.left, paramRect.top, paramRect.right, paramRect.bottom);
      this.shadowViewDelegate.setBackgroundDrawable((Drawable)this.insetDrawable);
    } else {
      this.shadowViewDelegate.setBackgroundDrawable(this.rippleDrawable);
    } 
  }
  
  boolean requirePreDrawListener() {
    return false;
  }
  
  void setBackgroundDrawable(ColorStateList paramColorStateList1, PorterDuff.Mode paramMode, ColorStateList paramColorStateList2, int paramInt) {
    Drawable drawable;
    this.shapeDrawable = DrawableCompat.wrap((Drawable)createShapeDrawable());
    DrawableCompat.setTintList(this.shapeDrawable, paramColorStateList1);
    if (paramMode != null)
      DrawableCompat.setTintMode(this.shapeDrawable, paramMode); 
    if (paramInt > 0) {
      this.borderDrawable = createBorderDrawable(paramInt, paramColorStateList1);
      LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] { this.borderDrawable, this.shapeDrawable });
    } else {
      this.borderDrawable = null;
      drawable = this.shapeDrawable;
    } 
    this.rippleDrawable = (Drawable)new RippleDrawable(RippleUtils.convertToRippleDrawableColor(paramColorStateList2), drawable, null);
    this.contentBackground = this.rippleDrawable;
    this.shadowViewDelegate.setBackgroundDrawable(this.rippleDrawable);
  }
  
  void setRippleColor(ColorStateList paramColorStateList) {
    if (this.rippleDrawable instanceof RippleDrawable) {
      ((RippleDrawable)this.rippleDrawable).setColor(RippleUtils.convertToRippleDrawableColor(paramColorStateList));
    } else {
      super.setRippleColor(paramColorStateList);
    } 
  }
  
  static class AlwaysStatefulGradientDrawable extends GradientDrawable {
    public boolean isStateful() {
      return true;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/widget/FloatingActionButtonImplLollipop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */