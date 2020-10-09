package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.design.R;
import android.support.design.animation.AnimationUtils;
import android.support.design.animation.AnimatorSetCompat;
import android.support.design.animation.ImageMatrixProperty;
import android.support.design.animation.MatrixEvaluator;
import android.support.design.animation.MotionSpec;
import android.support.design.ripple.RippleUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.util.Property;
import android.view.View;
import android.view.ViewTreeObserver;
import java.util.ArrayList;
import java.util.Iterator;

class FloatingActionButtonImpl {
  static final int ANIM_STATE_HIDING = 1;
  
  static final int ANIM_STATE_NONE = 0;
  
  static final int ANIM_STATE_SHOWING = 2;
  
  static final long ELEVATION_ANIM_DELAY = 100L;
  
  static final long ELEVATION_ANIM_DURATION = 100L;
  
  static final TimeInterpolator ELEVATION_ANIM_INTERPOLATOR = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
  
  static final int[] EMPTY_STATE_SET;
  
  static final int[] ENABLED_STATE_SET;
  
  static final int[] FOCUSED_ENABLED_STATE_SET;
  
  private static final float HIDE_ICON_SCALE = 0.0F;
  
  private static final float HIDE_OPACITY = 0.0F;
  
  private static final float HIDE_SCALE = 0.0F;
  
  static final int[] HOVERED_ENABLED_STATE_SET;
  
  static final int[] HOVERED_FOCUSED_ENABLED_STATE_SET;
  
  static final int[] PRESSED_ENABLED_STATE_SET = new int[] { 16842919, 16842910 };
  
  private static final float SHOW_ICON_SCALE = 1.0F;
  
  private static final float SHOW_OPACITY = 1.0F;
  
  private static final float SHOW_SCALE = 1.0F;
  
  int animState = 0;
  
  CircularBorderDrawable borderDrawable;
  
  Drawable contentBackground;
  
  Animator currentAnimator;
  
  private MotionSpec defaultHideMotionSpec;
  
  private MotionSpec defaultShowMotionSpec;
  
  float elevation;
  
  private ArrayList<Animator.AnimatorListener> hideListeners;
  
  MotionSpec hideMotionSpec;
  
  float hoveredFocusedTranslationZ;
  
  float imageMatrixScale = 1.0F;
  
  int maxImageSize;
  
  private ViewTreeObserver.OnPreDrawListener preDrawListener;
  
  float pressedTranslationZ;
  
  Drawable rippleDrawable;
  
  private float rotation;
  
  ShadowDrawableWrapper shadowDrawable;
  
  final ShadowViewDelegate shadowViewDelegate;
  
  Drawable shapeDrawable;
  
  private ArrayList<Animator.AnimatorListener> showListeners;
  
  MotionSpec showMotionSpec;
  
  private final StateListAnimator stateListAnimator;
  
  private final Matrix tmpMatrix = new Matrix();
  
  private final Rect tmpRect = new Rect();
  
  private final RectF tmpRectF1 = new RectF();
  
  private final RectF tmpRectF2 = new RectF();
  
  final VisibilityAwareImageButton view;
  
  static {
    HOVERED_FOCUSED_ENABLED_STATE_SET = new int[] { 16843623, 16842908, 16842910 };
    FOCUSED_ENABLED_STATE_SET = new int[] { 16842908, 16842910 };
    HOVERED_ENABLED_STATE_SET = new int[] { 16843623, 16842910 };
    ENABLED_STATE_SET = new int[] { 16842910 };
    EMPTY_STATE_SET = new int[0];
  }
  
  FloatingActionButtonImpl(VisibilityAwareImageButton paramVisibilityAwareImageButton, ShadowViewDelegate paramShadowViewDelegate) {
    this.view = paramVisibilityAwareImageButton;
    this.shadowViewDelegate = paramShadowViewDelegate;
    StateListAnimator stateListAnimator = new StateListAnimator();
    this.stateListAnimator = stateListAnimator;
    stateListAnimator.addState(PRESSED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToPressedTranslationZAnimation()));
    this.stateListAnimator.addState(HOVERED_FOCUSED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation()));
    this.stateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation()));
    this.stateListAnimator.addState(HOVERED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation()));
    this.stateListAnimator.addState(ENABLED_STATE_SET, createElevationAnimator(new ResetElevationAnimation()));
    this.stateListAnimator.addState(EMPTY_STATE_SET, createElevationAnimator(new DisabledElevationAnimation()));
    this.rotation = this.view.getRotation();
  }
  
  private void calculateImageMatrixFromScale(float paramFloat, Matrix paramMatrix) {
    paramMatrix.reset();
    Drawable drawable = this.view.getDrawable();
    if (drawable != null && this.maxImageSize != 0) {
      RectF rectF1 = this.tmpRectF1;
      RectF rectF2 = this.tmpRectF2;
      rectF1.set(0.0F, 0.0F, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
      int i = this.maxImageSize;
      rectF2.set(0.0F, 0.0F, i, i);
      paramMatrix.setRectToRect(rectF1, rectF2, Matrix.ScaleToFit.CENTER);
      i = this.maxImageSize;
      paramMatrix.postScale(paramFloat, paramFloat, i / 2.0F, i / 2.0F);
    } 
  }
  
  private AnimatorSet createAnimator(MotionSpec paramMotionSpec, float paramFloat1, float paramFloat2, float paramFloat3) {
    ArrayList<ObjectAnimator> arrayList = new ArrayList();
    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this.view, View.ALPHA, new float[] { paramFloat1 });
    paramMotionSpec.getTiming("opacity").apply((Animator)objectAnimator);
    arrayList.add(objectAnimator);
    objectAnimator = ObjectAnimator.ofFloat(this.view, View.SCALE_X, new float[] { paramFloat2 });
    paramMotionSpec.getTiming("scale").apply((Animator)objectAnimator);
    arrayList.add(objectAnimator);
    objectAnimator = ObjectAnimator.ofFloat(this.view, View.SCALE_Y, new float[] { paramFloat2 });
    paramMotionSpec.getTiming("scale").apply((Animator)objectAnimator);
    arrayList.add(objectAnimator);
    calculateImageMatrixFromScale(paramFloat3, this.tmpMatrix);
    objectAnimator = ObjectAnimator.ofObject(this.view, (Property)new ImageMatrixProperty(), (TypeEvaluator)new MatrixEvaluator(), (Object[])new Matrix[] { new Matrix(this.tmpMatrix) });
    paramMotionSpec.getTiming("iconScale").apply((Animator)objectAnimator);
    arrayList.add(objectAnimator);
    AnimatorSet animatorSet = new AnimatorSet();
    AnimatorSetCompat.playTogether(animatorSet, arrayList);
    return animatorSet;
  }
  
  private ValueAnimator createElevationAnimator(ShadowAnimatorImpl paramShadowAnimatorImpl) {
    ValueAnimator valueAnimator = new ValueAnimator();
    valueAnimator.setInterpolator(ELEVATION_ANIM_INTERPOLATOR);
    valueAnimator.setDuration(100L);
    valueAnimator.addListener((Animator.AnimatorListener)paramShadowAnimatorImpl);
    valueAnimator.addUpdateListener(paramShadowAnimatorImpl);
    valueAnimator.setFloatValues(new float[] { 0.0F, 1.0F });
    return valueAnimator;
  }
  
  private void ensurePreDrawListener() {
    if (this.preDrawListener == null)
      this.preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
          public boolean onPreDraw() {
            FloatingActionButtonImpl.this.onPreDraw();
            return true;
          }
        }; 
  }
  
  private MotionSpec getDefaultHideMotionSpec() {
    if (this.defaultHideMotionSpec == null)
      this.defaultHideMotionSpec = MotionSpec.createFromResource(this.view.getContext(), R.animator.design_fab_hide_motion_spec); 
    return this.defaultHideMotionSpec;
  }
  
  private MotionSpec getDefaultShowMotionSpec() {
    if (this.defaultShowMotionSpec == null)
      this.defaultShowMotionSpec = MotionSpec.createFromResource(this.view.getContext(), R.animator.design_fab_show_motion_spec); 
    return this.defaultShowMotionSpec;
  }
  
  private boolean shouldAnimateVisibilityChange() {
    boolean bool;
    if (ViewCompat.isLaidOut((View)this.view) && !this.view.isInEditMode()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void updateFromViewRotation() {
    if (Build.VERSION.SDK_INT == 19)
      if (this.rotation % 90.0F != 0.0F) {
        if (this.view.getLayerType() != 1)
          this.view.setLayerType(1, null); 
      } else if (this.view.getLayerType() != 0) {
        this.view.setLayerType(0, null);
      }  
    ShadowDrawableWrapper shadowDrawableWrapper = this.shadowDrawable;
    if (shadowDrawableWrapper != null)
      shadowDrawableWrapper.setRotation(-this.rotation); 
    CircularBorderDrawable circularBorderDrawable = this.borderDrawable;
    if (circularBorderDrawable != null)
      circularBorderDrawable.setRotation(-this.rotation); 
  }
  
  public void addOnHideAnimationListener(Animator.AnimatorListener paramAnimatorListener) {
    if (this.hideListeners == null)
      this.hideListeners = new ArrayList<>(); 
    this.hideListeners.add(paramAnimatorListener);
  }
  
  void addOnShowAnimationListener(Animator.AnimatorListener paramAnimatorListener) {
    if (this.showListeners == null)
      this.showListeners = new ArrayList<>(); 
    this.showListeners.add(paramAnimatorListener);
  }
  
  CircularBorderDrawable createBorderDrawable(int paramInt, ColorStateList paramColorStateList) {
    Context context = this.view.getContext();
    CircularBorderDrawable circularBorderDrawable = newCircularDrawable();
    circularBorderDrawable.setGradientColors(ContextCompat.getColor(context, R.color.design_fab_stroke_top_outer_color), ContextCompat.getColor(context, R.color.design_fab_stroke_top_inner_color), ContextCompat.getColor(context, R.color.design_fab_stroke_end_inner_color), ContextCompat.getColor(context, R.color.design_fab_stroke_end_outer_color));
    circularBorderDrawable.setBorderWidth(paramInt);
    circularBorderDrawable.setBorderTint(paramColorStateList);
    return circularBorderDrawable;
  }
  
  GradientDrawable createShapeDrawable() {
    GradientDrawable gradientDrawable = newGradientDrawableForShape();
    gradientDrawable.setShape(1);
    gradientDrawable.setColor(-1);
    return gradientDrawable;
  }
  
  final Drawable getContentBackground() {
    return this.contentBackground;
  }
  
  float getElevation() {
    return this.elevation;
  }
  
  final MotionSpec getHideMotionSpec() {
    return this.hideMotionSpec;
  }
  
  float getHoveredFocusedTranslationZ() {
    return this.hoveredFocusedTranslationZ;
  }
  
  void getPadding(Rect paramRect) {
    this.shadowDrawable.getPadding(paramRect);
  }
  
  float getPressedTranslationZ() {
    return this.pressedTranslationZ;
  }
  
  final MotionSpec getShowMotionSpec() {
    return this.showMotionSpec;
  }
  
  void hide(final InternalVisibilityChangedListener listener, final boolean fromUser) {
    Iterator<Animator.AnimatorListener> iterator;
    if (isOrWillBeHidden())
      return; 
    Animator animator = this.currentAnimator;
    if (animator != null)
      animator.cancel(); 
    if (shouldAnimateVisibilityChange()) {
      MotionSpec motionSpec = this.hideMotionSpec;
      if (motionSpec == null)
        motionSpec = getDefaultHideMotionSpec(); 
      AnimatorSet animatorSet = createAnimator(motionSpec, 0.0F, 0.0F, 0.0F);
      animatorSet.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            private boolean cancelled;
            
            public void onAnimationCancel(Animator param1Animator) {
              this.cancelled = true;
            }
            
            public void onAnimationEnd(Animator param1Animator) {
              FloatingActionButtonImpl.this.animState = 0;
              FloatingActionButtonImpl.this.currentAnimator = null;
              if (!this.cancelled) {
                byte b;
                VisibilityAwareImageButton visibilityAwareImageButton = FloatingActionButtonImpl.this.view;
                if (fromUser) {
                  b = 8;
                } else {
                  b = 4;
                } 
                visibilityAwareImageButton.internalSetVisibility(b, fromUser);
                FloatingActionButtonImpl.InternalVisibilityChangedListener internalVisibilityChangedListener = listener;
                if (internalVisibilityChangedListener != null)
                  internalVisibilityChangedListener.onHidden(); 
              } 
            }
            
            public void onAnimationStart(Animator param1Animator) {
              FloatingActionButtonImpl.this.view.internalSetVisibility(0, fromUser);
              FloatingActionButtonImpl.this.animState = 1;
              FloatingActionButtonImpl.this.currentAnimator = param1Animator;
              this.cancelled = false;
            }
          });
      ArrayList<Animator.AnimatorListener> arrayList = this.hideListeners;
      if (arrayList != null) {
        iterator = arrayList.iterator();
        while (iterator.hasNext())
          animatorSet.addListener(iterator.next()); 
      } 
      animatorSet.start();
    } else {
      byte b;
      VisibilityAwareImageButton visibilityAwareImageButton = this.view;
      if (fromUser) {
        b = 8;
      } else {
        b = 4;
      } 
      visibilityAwareImageButton.internalSetVisibility(b, fromUser);
      if (iterator != null)
        iterator.onHidden(); 
    } 
  }
  
  boolean isOrWillBeHidden() {
    int i = this.view.getVisibility();
    boolean bool1 = false;
    boolean bool2 = false;
    if (i == 0) {
      if (this.animState == 1)
        bool2 = true; 
      return bool2;
    } 
    bool2 = bool1;
    if (this.animState != 2)
      bool2 = true; 
    return bool2;
  }
  
  boolean isOrWillBeShown() {
    int i = this.view.getVisibility();
    boolean bool1 = false;
    boolean bool2 = false;
    if (i != 0) {
      if (this.animState == 2)
        bool2 = true; 
      return bool2;
    } 
    bool2 = bool1;
    if (this.animState != 1)
      bool2 = true; 
    return bool2;
  }
  
  void jumpDrawableToCurrentState() {
    this.stateListAnimator.jumpToCurrentState();
  }
  
  CircularBorderDrawable newCircularDrawable() {
    return new CircularBorderDrawable();
  }
  
  GradientDrawable newGradientDrawableForShape() {
    return new GradientDrawable();
  }
  
  void onAttachedToWindow() {
    if (requirePreDrawListener()) {
      ensurePreDrawListener();
      this.view.getViewTreeObserver().addOnPreDrawListener(this.preDrawListener);
    } 
  }
  
  void onCompatShadowChanged() {}
  
  void onDetachedFromWindow() {
    if (this.preDrawListener != null) {
      this.view.getViewTreeObserver().removeOnPreDrawListener(this.preDrawListener);
      this.preDrawListener = null;
    } 
  }
  
  void onDrawableStateChanged(int[] paramArrayOfint) {
    this.stateListAnimator.setState(paramArrayOfint);
  }
  
  void onElevationsChanged(float paramFloat1, float paramFloat2, float paramFloat3) {
    ShadowDrawableWrapper shadowDrawableWrapper = this.shadowDrawable;
    if (shadowDrawableWrapper != null) {
      shadowDrawableWrapper.setShadowSize(paramFloat1, this.pressedTranslationZ + paramFloat1);
      updatePadding();
    } 
  }
  
  void onPaddingUpdated(Rect paramRect) {}
  
  void onPreDraw() {
    float f = this.view.getRotation();
    if (this.rotation != f) {
      this.rotation = f;
      updateFromViewRotation();
    } 
  }
  
  public void removeOnHideAnimationListener(Animator.AnimatorListener paramAnimatorListener) {
    ArrayList<Animator.AnimatorListener> arrayList = this.hideListeners;
    if (arrayList == null)
      return; 
    arrayList.remove(paramAnimatorListener);
  }
  
  void removeOnShowAnimationListener(Animator.AnimatorListener paramAnimatorListener) {
    ArrayList<Animator.AnimatorListener> arrayList = this.showListeners;
    if (arrayList == null)
      return; 
    arrayList.remove(paramAnimatorListener);
  }
  
  boolean requirePreDrawListener() {
    return true;
  }
  
  void setBackgroundDrawable(ColorStateList paramColorStateList1, PorterDuff.Mode paramMode, ColorStateList paramColorStateList2, int paramInt) {
    Drawable[] arrayOfDrawable;
    Drawable drawable3 = DrawableCompat.wrap((Drawable)createShapeDrawable());
    this.shapeDrawable = drawable3;
    DrawableCompat.setTintList(drawable3, paramColorStateList1);
    if (paramMode != null)
      DrawableCompat.setTintMode(this.shapeDrawable, paramMode); 
    Drawable drawable2 = DrawableCompat.wrap((Drawable)createShapeDrawable());
    this.rippleDrawable = drawable2;
    DrawableCompat.setTintList(drawable2, RippleUtils.convertToRippleDrawableColor(paramColorStateList2));
    if (paramInt > 0) {
      CircularBorderDrawable circularBorderDrawable = createBorderDrawable(paramInt, paramColorStateList1);
      this.borderDrawable = circularBorderDrawable;
      arrayOfDrawable = new Drawable[] { circularBorderDrawable, this.shapeDrawable, this.rippleDrawable };
    } else {
      this.borderDrawable = null;
      arrayOfDrawable = new Drawable[] { this.shapeDrawable, this.rippleDrawable };
    } 
    this.contentBackground = (Drawable)new LayerDrawable(arrayOfDrawable);
    Context context = this.view.getContext();
    Drawable drawable1 = this.contentBackground;
    float f1 = this.shadowViewDelegate.getRadius();
    float f2 = this.elevation;
    ShadowDrawableWrapper shadowDrawableWrapper = new ShadowDrawableWrapper(context, drawable1, f1, f2, f2 + this.pressedTranslationZ);
    this.shadowDrawable = shadowDrawableWrapper;
    shadowDrawableWrapper.setAddPaddingForCorners(false);
    this.shadowViewDelegate.setBackgroundDrawable((Drawable)this.shadowDrawable);
  }
  
  void setBackgroundTintList(ColorStateList paramColorStateList) {
    Drawable drawable = this.shapeDrawable;
    if (drawable != null)
      DrawableCompat.setTintList(drawable, paramColorStateList); 
    drawable = this.borderDrawable;
    if (drawable != null)
      drawable.setBorderTint(paramColorStateList); 
  }
  
  void setBackgroundTintMode(PorterDuff.Mode paramMode) {
    Drawable drawable = this.shapeDrawable;
    if (drawable != null)
      DrawableCompat.setTintMode(drawable, paramMode); 
  }
  
  final void setElevation(float paramFloat) {
    if (this.elevation != paramFloat) {
      this.elevation = paramFloat;
      onElevationsChanged(paramFloat, this.hoveredFocusedTranslationZ, this.pressedTranslationZ);
    } 
  }
  
  final void setHideMotionSpec(MotionSpec paramMotionSpec) {
    this.hideMotionSpec = paramMotionSpec;
  }
  
  final void setHoveredFocusedTranslationZ(float paramFloat) {
    if (this.hoveredFocusedTranslationZ != paramFloat) {
      this.hoveredFocusedTranslationZ = paramFloat;
      onElevationsChanged(this.elevation, paramFloat, this.pressedTranslationZ);
    } 
  }
  
  final void setImageMatrixScale(float paramFloat) {
    this.imageMatrixScale = paramFloat;
    Matrix matrix = this.tmpMatrix;
    calculateImageMatrixFromScale(paramFloat, matrix);
    this.view.setImageMatrix(matrix);
  }
  
  final void setMaxImageSize(int paramInt) {
    if (this.maxImageSize != paramInt) {
      this.maxImageSize = paramInt;
      updateImageMatrixScale();
    } 
  }
  
  final void setPressedTranslationZ(float paramFloat) {
    if (this.pressedTranslationZ != paramFloat) {
      this.pressedTranslationZ = paramFloat;
      onElevationsChanged(this.elevation, this.hoveredFocusedTranslationZ, paramFloat);
    } 
  }
  
  void setRippleColor(ColorStateList paramColorStateList) {
    Drawable drawable = this.rippleDrawable;
    if (drawable != null)
      DrawableCompat.setTintList(drawable, RippleUtils.convertToRippleDrawableColor(paramColorStateList)); 
  }
  
  final void setShowMotionSpec(MotionSpec paramMotionSpec) {
    this.showMotionSpec = paramMotionSpec;
  }
  
  void show(final InternalVisibilityChangedListener listener, final boolean fromUser) {
    Iterator<Animator.AnimatorListener> iterator;
    if (isOrWillBeShown())
      return; 
    Animator animator = this.currentAnimator;
    if (animator != null)
      animator.cancel(); 
    if (shouldAnimateVisibilityChange()) {
      if (this.view.getVisibility() != 0) {
        this.view.setAlpha(0.0F);
        this.view.setScaleY(0.0F);
        this.view.setScaleX(0.0F);
        setImageMatrixScale(0.0F);
      } 
      MotionSpec motionSpec = this.showMotionSpec;
      if (motionSpec == null)
        motionSpec = getDefaultShowMotionSpec(); 
      AnimatorSet animatorSet = createAnimator(motionSpec, 1.0F, 1.0F, 1.0F);
      animatorSet.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator param1Animator) {
              FloatingActionButtonImpl.this.animState = 0;
              FloatingActionButtonImpl.this.currentAnimator = null;
              FloatingActionButtonImpl.InternalVisibilityChangedListener internalVisibilityChangedListener = listener;
              if (internalVisibilityChangedListener != null)
                internalVisibilityChangedListener.onShown(); 
            }
            
            public void onAnimationStart(Animator param1Animator) {
              FloatingActionButtonImpl.this.view.internalSetVisibility(0, fromUser);
              FloatingActionButtonImpl.this.animState = 2;
              FloatingActionButtonImpl.this.currentAnimator = param1Animator;
            }
          });
      ArrayList<Animator.AnimatorListener> arrayList = this.showListeners;
      if (arrayList != null) {
        iterator = arrayList.iterator();
        while (iterator.hasNext())
          animatorSet.addListener(iterator.next()); 
      } 
      animatorSet.start();
    } else {
      this.view.internalSetVisibility(0, fromUser);
      this.view.setAlpha(1.0F);
      this.view.setScaleY(1.0F);
      this.view.setScaleX(1.0F);
      setImageMatrixScale(1.0F);
      if (iterator != null)
        iterator.onShown(); 
    } 
  }
  
  final void updateImageMatrixScale() {
    setImageMatrixScale(this.imageMatrixScale);
  }
  
  final void updatePadding() {
    Rect rect = this.tmpRect;
    getPadding(rect);
    onPaddingUpdated(rect);
    this.shadowViewDelegate.setShadowPadding(rect.left, rect.top, rect.right, rect.bottom);
  }
  
  private class DisabledElevationAnimation extends ShadowAnimatorImpl {
    protected float getTargetShadowSize() {
      return 0.0F;
    }
  }
  
  private class ElevateToHoveredFocusedTranslationZAnimation extends ShadowAnimatorImpl {
    protected float getTargetShadowSize() {
      return FloatingActionButtonImpl.this.elevation + FloatingActionButtonImpl.this.hoveredFocusedTranslationZ;
    }
  }
  
  private class ElevateToPressedTranslationZAnimation extends ShadowAnimatorImpl {
    protected float getTargetShadowSize() {
      return FloatingActionButtonImpl.this.elevation + FloatingActionButtonImpl.this.pressedTranslationZ;
    }
  }
  
  static interface InternalVisibilityChangedListener {
    void onHidden();
    
    void onShown();
  }
  
  private class ResetElevationAnimation extends ShadowAnimatorImpl {
    protected float getTargetShadowSize() {
      return FloatingActionButtonImpl.this.elevation;
    }
  }
  
  private abstract class ShadowAnimatorImpl extends AnimatorListenerAdapter implements ValueAnimator.AnimatorUpdateListener {
    private float shadowSizeEnd;
    
    private float shadowSizeStart;
    
    private boolean validValues;
    
    private ShadowAnimatorImpl() {}
    
    protected abstract float getTargetShadowSize();
    
    public void onAnimationEnd(Animator param1Animator) {
      FloatingActionButtonImpl.this.shadowDrawable.setShadowSize(this.shadowSizeEnd);
      this.validValues = false;
    }
    
    public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
      if (!this.validValues) {
        this.shadowSizeStart = FloatingActionButtonImpl.this.shadowDrawable.getShadowSize();
        this.shadowSizeEnd = getTargetShadowSize();
        this.validValues = true;
      } 
      ShadowDrawableWrapper shadowDrawableWrapper = FloatingActionButtonImpl.this.shadowDrawable;
      float f = this.shadowSizeStart;
      shadowDrawableWrapper.setShadowSize(f + (this.shadowSizeEnd - f) * param1ValueAnimator.getAnimatedFraction());
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/widget/FloatingActionButtonImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */