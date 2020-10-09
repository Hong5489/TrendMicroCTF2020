package android.support.v4.view;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public final class GestureDetectorCompat {
  private final GestureDetectorCompatImpl mImpl;
  
  public GestureDetectorCompat(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener) {
    this(paramContext, paramOnGestureListener, null);
  }
  
  public GestureDetectorCompat(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener, Handler paramHandler) {
    if (Build.VERSION.SDK_INT > 17) {
      this.mImpl = new GestureDetectorCompatImplJellybeanMr2(paramContext, paramOnGestureListener, paramHandler);
    } else {
      this.mImpl = new GestureDetectorCompatImplBase(paramContext, paramOnGestureListener, paramHandler);
    } 
  }
  
  public boolean isLongpressEnabled() {
    return this.mImpl.isLongpressEnabled();
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    return this.mImpl.onTouchEvent(paramMotionEvent);
  }
  
  public void setIsLongpressEnabled(boolean paramBoolean) {
    this.mImpl.setIsLongpressEnabled(paramBoolean);
  }
  
  public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener) {
    this.mImpl.setOnDoubleTapListener(paramOnDoubleTapListener);
  }
  
  static interface GestureDetectorCompatImpl {
    boolean isLongpressEnabled();
    
    boolean onTouchEvent(MotionEvent param1MotionEvent);
    
    void setIsLongpressEnabled(boolean param1Boolean);
    
    void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener param1OnDoubleTapListener);
  }
  
  static class GestureDetectorCompatImplBase implements GestureDetectorCompatImpl {
    private static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
    
    private static final int LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
    
    private static final int LONG_PRESS = 2;
    
    private static final int SHOW_PRESS = 1;
    
    private static final int TAP = 3;
    
    private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
    
    private boolean mAlwaysInBiggerTapRegion;
    
    private boolean mAlwaysInTapRegion;
    
    MotionEvent mCurrentDownEvent;
    
    boolean mDeferConfirmSingleTap;
    
    GestureDetector.OnDoubleTapListener mDoubleTapListener;
    
    private int mDoubleTapSlopSquare;
    
    private float mDownFocusX;
    
    private float mDownFocusY;
    
    private final Handler mHandler;
    
    private boolean mInLongPress;
    
    private boolean mIsDoubleTapping;
    
    private boolean mIsLongpressEnabled;
    
    private float mLastFocusX;
    
    private float mLastFocusY;
    
    final GestureDetector.OnGestureListener mListener;
    
    private int mMaximumFlingVelocity;
    
    private int mMinimumFlingVelocity;
    
    private MotionEvent mPreviousUpEvent;
    
    boolean mStillDown;
    
    private int mTouchSlopSquare;
    
    private VelocityTracker mVelocityTracker;
    
    static {
    
    }
    
    GestureDetectorCompatImplBase(Context param1Context, GestureDetector.OnGestureListener param1OnGestureListener, Handler param1Handler) {
      if (param1Handler != null) {
        this.mHandler = new GestureHandler(param1Handler);
      } else {
        this.mHandler = new GestureHandler();
      } 
      this.mListener = param1OnGestureListener;
      if (param1OnGestureListener instanceof GestureDetector.OnDoubleTapListener)
        setOnDoubleTapListener((GestureDetector.OnDoubleTapListener)param1OnGestureListener); 
      init(param1Context);
    }
    
    private void cancel() {
      this.mHandler.removeMessages(1);
      this.mHandler.removeMessages(2);
      this.mHandler.removeMessages(3);
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
      this.mIsDoubleTapping = false;
      this.mStillDown = false;
      this.mAlwaysInTapRegion = false;
      this.mAlwaysInBiggerTapRegion = false;
      this.mDeferConfirmSingleTap = false;
      if (this.mInLongPress)
        this.mInLongPress = false; 
    }
    
    private void cancelTaps() {
      this.mHandler.removeMessages(1);
      this.mHandler.removeMessages(2);
      this.mHandler.removeMessages(3);
      this.mIsDoubleTapping = false;
      this.mAlwaysInTapRegion = false;
      this.mAlwaysInBiggerTapRegion = false;
      this.mDeferConfirmSingleTap = false;
      if (this.mInLongPress)
        this.mInLongPress = false; 
    }
    
    private void init(Context param1Context) {
      if (param1Context != null) {
        if (this.mListener != null) {
          this.mIsLongpressEnabled = true;
          ViewConfiguration viewConfiguration = ViewConfiguration.get(param1Context);
          int i = viewConfiguration.getScaledTouchSlop();
          int j = viewConfiguration.getScaledDoubleTapSlop();
          this.mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
          this.mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
          this.mTouchSlopSquare = i * i;
          this.mDoubleTapSlopSquare = j * j;
          return;
        } 
        throw new IllegalArgumentException("OnGestureListener must not be null");
      } 
      throw new IllegalArgumentException("Context must not be null");
    }
    
    private boolean isConsideredDoubleTap(MotionEvent param1MotionEvent1, MotionEvent param1MotionEvent2, MotionEvent param1MotionEvent3) {
      boolean bool = this.mAlwaysInBiggerTapRegion;
      boolean bool1 = false;
      if (!bool)
        return false; 
      if (param1MotionEvent3.getEventTime() - param1MotionEvent2.getEventTime() > DOUBLE_TAP_TIMEOUT)
        return false; 
      int i = (int)param1MotionEvent1.getX() - (int)param1MotionEvent3.getX();
      int j = (int)param1MotionEvent1.getY() - (int)param1MotionEvent3.getY();
      if (i * i + j * j < this.mDoubleTapSlopSquare)
        bool1 = true; 
      return bool1;
    }
    
    void dispatchLongPress() {
      this.mHandler.removeMessages(3);
      this.mDeferConfirmSingleTap = false;
      this.mInLongPress = true;
      this.mListener.onLongPress(this.mCurrentDownEvent);
    }
    
    public boolean isLongpressEnabled() {
      return this.mIsLongpressEnabled;
    }
    
    public boolean onTouchEvent(MotionEvent param1MotionEvent) {
      // Byte code:
      //   0: aload_1
      //   1: invokevirtual getAction : ()I
      //   4: istore_2
      //   5: aload_0
      //   6: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   9: ifnonnull -> 19
      //   12: aload_0
      //   13: invokestatic obtain : ()Landroid/view/VelocityTracker;
      //   16: putfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   19: aload_0
      //   20: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   23: aload_1
      //   24: invokevirtual addMovement : (Landroid/view/MotionEvent;)V
      //   27: iload_2
      //   28: sipush #255
      //   31: iand
      //   32: bipush #6
      //   34: if_icmpne -> 42
      //   37: iconst_1
      //   38: istore_3
      //   39: goto -> 44
      //   42: iconst_0
      //   43: istore_3
      //   44: iload_3
      //   45: ifeq -> 57
      //   48: aload_1
      //   49: invokevirtual getActionIndex : ()I
      //   52: istore #4
      //   54: goto -> 60
      //   57: iconst_m1
      //   58: istore #4
      //   60: fconst_0
      //   61: fstore #5
      //   63: fconst_0
      //   64: fstore #6
      //   66: aload_1
      //   67: invokevirtual getPointerCount : ()I
      //   70: istore #7
      //   72: iconst_0
      //   73: istore #8
      //   75: iload #8
      //   77: iload #7
      //   79: if_icmpge -> 120
      //   82: iload #4
      //   84: iload #8
      //   86: if_icmpne -> 92
      //   89: goto -> 114
      //   92: fload #5
      //   94: aload_1
      //   95: iload #8
      //   97: invokevirtual getX : (I)F
      //   100: fadd
      //   101: fstore #5
      //   103: fload #6
      //   105: aload_1
      //   106: iload #8
      //   108: invokevirtual getY : (I)F
      //   111: fadd
      //   112: fstore #6
      //   114: iinc #8, 1
      //   117: goto -> 75
      //   120: iload_3
      //   121: ifeq -> 133
      //   124: iload #7
      //   126: iconst_1
      //   127: isub
      //   128: istore #4
      //   130: goto -> 137
      //   133: iload #7
      //   135: istore #4
      //   137: fload #5
      //   139: iload #4
      //   141: i2f
      //   142: fdiv
      //   143: fstore #5
      //   145: fload #6
      //   147: iload #4
      //   149: i2f
      //   150: fdiv
      //   151: fstore #6
      //   153: iconst_0
      //   154: istore #9
      //   156: iconst_0
      //   157: istore #4
      //   159: iconst_0
      //   160: istore #10
      //   162: iconst_0
      //   163: istore #11
      //   165: iload_2
      //   166: sipush #255
      //   169: iand
      //   170: istore #8
      //   172: iload #8
      //   174: ifeq -> 920
      //   177: iload #8
      //   179: iconst_1
      //   180: if_icmpeq -> 638
      //   183: iload #8
      //   185: iconst_2
      //   186: if_icmpeq -> 397
      //   189: iload #8
      //   191: iconst_3
      //   192: if_icmpeq -> 390
      //   195: iload #8
      //   197: iconst_5
      //   198: if_icmpeq -> 359
      //   201: iload #8
      //   203: bipush #6
      //   205: if_icmpeq -> 211
      //   208: goto -> 596
      //   211: aload_0
      //   212: fload #5
      //   214: putfield mLastFocusX : F
      //   217: aload_0
      //   218: fload #5
      //   220: putfield mDownFocusX : F
      //   223: aload_0
      //   224: fload #6
      //   226: putfield mLastFocusY : F
      //   229: aload_0
      //   230: fload #6
      //   232: putfield mDownFocusY : F
      //   235: aload_0
      //   236: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   239: sipush #1000
      //   242: aload_0
      //   243: getfield mMaximumFlingVelocity : I
      //   246: i2f
      //   247: invokevirtual computeCurrentVelocity : (IF)V
      //   250: aload_1
      //   251: invokevirtual getActionIndex : ()I
      //   254: istore #8
      //   256: aload_1
      //   257: iload #8
      //   259: invokevirtual getPointerId : (I)I
      //   262: istore #4
      //   264: aload_0
      //   265: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   268: iload #4
      //   270: invokevirtual getXVelocity : (I)F
      //   273: fstore #5
      //   275: aload_0
      //   276: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   279: iload #4
      //   281: invokevirtual getYVelocity : (I)F
      //   284: fstore #6
      //   286: iconst_0
      //   287: istore_2
      //   288: iload_2
      //   289: iload #7
      //   291: if_icmpge -> 356
      //   294: iload_2
      //   295: iload #8
      //   297: if_icmpne -> 303
      //   300: goto -> 350
      //   303: aload_1
      //   304: iload_2
      //   305: invokevirtual getPointerId : (I)I
      //   308: istore #12
      //   310: aload_0
      //   311: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   314: iload #12
      //   316: invokevirtual getXVelocity : (I)F
      //   319: fload #5
      //   321: fmul
      //   322: aload_0
      //   323: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   326: iload #12
      //   328: invokevirtual getYVelocity : (I)F
      //   331: fload #6
      //   333: fmul
      //   334: fadd
      //   335: fconst_0
      //   336: fcmpg
      //   337: ifge -> 350
      //   340: aload_0
      //   341: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   344: invokevirtual clear : ()V
      //   347: goto -> 356
      //   350: iinc #2, 1
      //   353: goto -> 288
      //   356: goto -> 596
      //   359: aload_0
      //   360: fload #5
      //   362: putfield mLastFocusX : F
      //   365: aload_0
      //   366: fload #5
      //   368: putfield mDownFocusX : F
      //   371: aload_0
      //   372: fload #6
      //   374: putfield mLastFocusY : F
      //   377: aload_0
      //   378: fload #6
      //   380: putfield mDownFocusY : F
      //   383: aload_0
      //   384: invokespecial cancelTaps : ()V
      //   387: goto -> 596
      //   390: aload_0
      //   391: invokespecial cancel : ()V
      //   394: goto -> 596
      //   397: aload_0
      //   398: getfield mInLongPress : Z
      //   401: ifeq -> 407
      //   404: goto -> 596
      //   407: aload_0
      //   408: getfield mLastFocusX : F
      //   411: fload #5
      //   413: fsub
      //   414: fstore #13
      //   416: aload_0
      //   417: getfield mLastFocusY : F
      //   420: fload #6
      //   422: fsub
      //   423: fstore #14
      //   425: aload_0
      //   426: getfield mIsDoubleTapping : Z
      //   429: ifeq -> 449
      //   432: iconst_0
      //   433: aload_0
      //   434: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   437: aload_1
      //   438: invokeinterface onDoubleTapEvent : (Landroid/view/MotionEvent;)Z
      //   443: ior
      //   444: istore #11
      //   446: goto -> 1195
      //   449: aload_0
      //   450: getfield mAlwaysInTapRegion : Z
      //   453: ifeq -> 573
      //   456: fload #5
      //   458: aload_0
      //   459: getfield mDownFocusX : F
      //   462: fsub
      //   463: f2i
      //   464: istore_3
      //   465: fload #6
      //   467: aload_0
      //   468: getfield mDownFocusY : F
      //   471: fsub
      //   472: f2i
      //   473: istore #4
      //   475: iload_3
      //   476: iload_3
      //   477: imul
      //   478: iload #4
      //   480: iload #4
      //   482: imul
      //   483: iadd
      //   484: istore_3
      //   485: iload_3
      //   486: aload_0
      //   487: getfield mTouchSlopSquare : I
      //   490: if_icmple -> 557
      //   493: aload_0
      //   494: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   497: aload_0
      //   498: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   501: aload_1
      //   502: fload #13
      //   504: fload #14
      //   506: invokeinterface onScroll : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;FF)Z
      //   511: istore #11
      //   513: aload_0
      //   514: fload #5
      //   516: putfield mLastFocusX : F
      //   519: aload_0
      //   520: fload #6
      //   522: putfield mLastFocusY : F
      //   525: aload_0
      //   526: iconst_0
      //   527: putfield mAlwaysInTapRegion : Z
      //   530: aload_0
      //   531: getfield mHandler : Landroid/os/Handler;
      //   534: iconst_3
      //   535: invokevirtual removeMessages : (I)V
      //   538: aload_0
      //   539: getfield mHandler : Landroid/os/Handler;
      //   542: iconst_1
      //   543: invokevirtual removeMessages : (I)V
      //   546: aload_0
      //   547: getfield mHandler : Landroid/os/Handler;
      //   550: iconst_2
      //   551: invokevirtual removeMessages : (I)V
      //   554: goto -> 557
      //   557: iload_3
      //   558: aload_0
      //   559: getfield mTouchSlopSquare : I
      //   562: if_icmple -> 570
      //   565: aload_0
      //   566: iconst_0
      //   567: putfield mAlwaysInBiggerTapRegion : Z
      //   570: goto -> 1195
      //   573: fload #13
      //   575: invokestatic abs : (F)F
      //   578: fconst_1
      //   579: fcmpl
      //   580: ifge -> 603
      //   583: fload #14
      //   585: invokestatic abs : (F)F
      //   588: fconst_1
      //   589: fcmpl
      //   590: iflt -> 596
      //   593: goto -> 603
      //   596: iload #10
      //   598: istore #11
      //   600: goto -> 1195
      //   603: aload_0
      //   604: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   607: aload_0
      //   608: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   611: aload_1
      //   612: fload #13
      //   614: fload #14
      //   616: invokeinterface onScroll : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;FF)Z
      //   621: istore #11
      //   623: aload_0
      //   624: fload #5
      //   626: putfield mLastFocusX : F
      //   629: aload_0
      //   630: fload #6
      //   632: putfield mLastFocusY : F
      //   635: goto -> 1195
      //   638: aload_0
      //   639: iconst_0
      //   640: putfield mStillDown : Z
      //   643: aload_1
      //   644: invokestatic obtain : (Landroid/view/MotionEvent;)Landroid/view/MotionEvent;
      //   647: astore #15
      //   649: aload_0
      //   650: getfield mIsDoubleTapping : Z
      //   653: ifeq -> 673
      //   656: iconst_0
      //   657: aload_0
      //   658: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   661: aload_1
      //   662: invokeinterface onDoubleTapEvent : (Landroid/view/MotionEvent;)Z
      //   667: ior
      //   668: istore #11
      //   670: goto -> 854
      //   673: aload_0
      //   674: getfield mInLongPress : Z
      //   677: ifeq -> 700
      //   680: aload_0
      //   681: getfield mHandler : Landroid/os/Handler;
      //   684: iconst_3
      //   685: invokevirtual removeMessages : (I)V
      //   688: aload_0
      //   689: iconst_0
      //   690: putfield mInLongPress : Z
      //   693: iload #9
      //   695: istore #11
      //   697: goto -> 854
      //   700: aload_0
      //   701: getfield mAlwaysInTapRegion : Z
      //   704: ifeq -> 761
      //   707: aload_0
      //   708: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   711: aload_1
      //   712: invokeinterface onSingleTapUp : (Landroid/view/MotionEvent;)Z
      //   717: istore #9
      //   719: iload #9
      //   721: istore #11
      //   723: aload_0
      //   724: getfield mDeferConfirmSingleTap : Z
      //   727: ifeq -> 854
      //   730: aload_0
      //   731: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   734: astore #16
      //   736: iload #9
      //   738: istore #11
      //   740: aload #16
      //   742: ifnull -> 854
      //   745: aload #16
      //   747: aload_1
      //   748: invokeinterface onSingleTapConfirmed : (Landroid/view/MotionEvent;)Z
      //   753: pop
      //   754: iload #9
      //   756: istore #11
      //   758: goto -> 854
      //   761: aload_0
      //   762: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   765: astore #16
      //   767: aload_1
      //   768: iconst_0
      //   769: invokevirtual getPointerId : (I)I
      //   772: istore_3
      //   773: aload #16
      //   775: sipush #1000
      //   778: aload_0
      //   779: getfield mMaximumFlingVelocity : I
      //   782: i2f
      //   783: invokevirtual computeCurrentVelocity : (IF)V
      //   786: aload #16
      //   788: iload_3
      //   789: invokevirtual getYVelocity : (I)F
      //   792: fstore #6
      //   794: aload #16
      //   796: iload_3
      //   797: invokevirtual getXVelocity : (I)F
      //   800: fstore #5
      //   802: fload #6
      //   804: invokestatic abs : (F)F
      //   807: aload_0
      //   808: getfield mMinimumFlingVelocity : I
      //   811: i2f
      //   812: fcmpl
      //   813: ifgt -> 834
      //   816: iload #9
      //   818: istore #11
      //   820: fload #5
      //   822: invokestatic abs : (F)F
      //   825: aload_0
      //   826: getfield mMinimumFlingVelocity : I
      //   829: i2f
      //   830: fcmpl
      //   831: ifle -> 854
      //   834: aload_0
      //   835: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   838: aload_0
      //   839: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   842: aload_1
      //   843: fload #5
      //   845: fload #6
      //   847: invokeinterface onFling : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;FF)Z
      //   852: istore #11
      //   854: aload_0
      //   855: getfield mPreviousUpEvent : Landroid/view/MotionEvent;
      //   858: astore_1
      //   859: aload_1
      //   860: ifnull -> 867
      //   863: aload_1
      //   864: invokevirtual recycle : ()V
      //   867: aload_0
      //   868: aload #15
      //   870: putfield mPreviousUpEvent : Landroid/view/MotionEvent;
      //   873: aload_0
      //   874: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   877: astore_1
      //   878: aload_1
      //   879: ifnull -> 891
      //   882: aload_1
      //   883: invokevirtual recycle : ()V
      //   886: aload_0
      //   887: aconst_null
      //   888: putfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   891: aload_0
      //   892: iconst_0
      //   893: putfield mIsDoubleTapping : Z
      //   896: aload_0
      //   897: iconst_0
      //   898: putfield mDeferConfirmSingleTap : Z
      //   901: aload_0
      //   902: getfield mHandler : Landroid/os/Handler;
      //   905: iconst_1
      //   906: invokevirtual removeMessages : (I)V
      //   909: aload_0
      //   910: getfield mHandler : Landroid/os/Handler;
      //   913: iconst_2
      //   914: invokevirtual removeMessages : (I)V
      //   917: goto -> 1195
      //   920: iload #4
      //   922: istore_3
      //   923: aload_0
      //   924: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   927: ifnull -> 1043
      //   930: aload_0
      //   931: getfield mHandler : Landroid/os/Handler;
      //   934: iconst_3
      //   935: invokevirtual hasMessages : (I)Z
      //   938: istore #11
      //   940: iload #11
      //   942: ifeq -> 953
      //   945: aload_0
      //   946: getfield mHandler : Landroid/os/Handler;
      //   949: iconst_3
      //   950: invokevirtual removeMessages : (I)V
      //   953: aload_0
      //   954: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   957: astore #15
      //   959: aload #15
      //   961: ifnull -> 1027
      //   964: aload_0
      //   965: getfield mPreviousUpEvent : Landroid/view/MotionEvent;
      //   968: astore #16
      //   970: aload #16
      //   972: ifnull -> 1027
      //   975: iload #11
      //   977: ifeq -> 1027
      //   980: aload_0
      //   981: aload #15
      //   983: aload #16
      //   985: aload_1
      //   986: invokespecial isConsideredDoubleTap : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;Landroid/view/MotionEvent;)Z
      //   989: ifeq -> 1027
      //   992: aload_0
      //   993: iconst_1
      //   994: putfield mIsDoubleTapping : Z
      //   997: aload_0
      //   998: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   1001: aload_0
      //   1002: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1005: invokeinterface onDoubleTap : (Landroid/view/MotionEvent;)Z
      //   1010: iconst_0
      //   1011: ior
      //   1012: aload_0
      //   1013: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   1016: aload_1
      //   1017: invokeinterface onDoubleTapEvent : (Landroid/view/MotionEvent;)Z
      //   1022: ior
      //   1023: istore_3
      //   1024: goto -> 1043
      //   1027: aload_0
      //   1028: getfield mHandler : Landroid/os/Handler;
      //   1031: iconst_3
      //   1032: getstatic android/support/v4/view/GestureDetectorCompat$GestureDetectorCompatImplBase.DOUBLE_TAP_TIMEOUT : I
      //   1035: i2l
      //   1036: invokevirtual sendEmptyMessageDelayed : (IJ)Z
      //   1039: pop
      //   1040: iload #4
      //   1042: istore_3
      //   1043: aload_0
      //   1044: fload #5
      //   1046: putfield mLastFocusX : F
      //   1049: aload_0
      //   1050: fload #5
      //   1052: putfield mDownFocusX : F
      //   1055: aload_0
      //   1056: fload #6
      //   1058: putfield mLastFocusY : F
      //   1061: aload_0
      //   1062: fload #6
      //   1064: putfield mDownFocusY : F
      //   1067: aload_0
      //   1068: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1071: astore #15
      //   1073: aload #15
      //   1075: ifnull -> 1083
      //   1078: aload #15
      //   1080: invokevirtual recycle : ()V
      //   1083: aload_0
      //   1084: aload_1
      //   1085: invokestatic obtain : (Landroid/view/MotionEvent;)Landroid/view/MotionEvent;
      //   1088: putfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1091: aload_0
      //   1092: iconst_1
      //   1093: putfield mAlwaysInTapRegion : Z
      //   1096: aload_0
      //   1097: iconst_1
      //   1098: putfield mAlwaysInBiggerTapRegion : Z
      //   1101: aload_0
      //   1102: iconst_1
      //   1103: putfield mStillDown : Z
      //   1106: aload_0
      //   1107: iconst_0
      //   1108: putfield mInLongPress : Z
      //   1111: aload_0
      //   1112: iconst_0
      //   1113: putfield mDeferConfirmSingleTap : Z
      //   1116: aload_0
      //   1117: getfield mIsLongpressEnabled : Z
      //   1120: ifeq -> 1160
      //   1123: aload_0
      //   1124: getfield mHandler : Landroid/os/Handler;
      //   1127: iconst_2
      //   1128: invokevirtual removeMessages : (I)V
      //   1131: aload_0
      //   1132: getfield mHandler : Landroid/os/Handler;
      //   1135: iconst_2
      //   1136: aload_0
      //   1137: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1140: invokevirtual getDownTime : ()J
      //   1143: getstatic android/support/v4/view/GestureDetectorCompat$GestureDetectorCompatImplBase.TAP_TIMEOUT : I
      //   1146: i2l
      //   1147: ladd
      //   1148: getstatic android/support/v4/view/GestureDetectorCompat$GestureDetectorCompatImplBase.LONGPRESS_TIMEOUT : I
      //   1151: i2l
      //   1152: ladd
      //   1153: invokevirtual sendEmptyMessageAtTime : (IJ)Z
      //   1156: pop
      //   1157: goto -> 1160
      //   1160: aload_0
      //   1161: getfield mHandler : Landroid/os/Handler;
      //   1164: iconst_1
      //   1165: aload_0
      //   1166: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1169: invokevirtual getDownTime : ()J
      //   1172: getstatic android/support/v4/view/GestureDetectorCompat$GestureDetectorCompatImplBase.TAP_TIMEOUT : I
      //   1175: i2l
      //   1176: ladd
      //   1177: invokevirtual sendEmptyMessageAtTime : (IJ)Z
      //   1180: pop
      //   1181: iload_3
      //   1182: aload_0
      //   1183: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   1186: aload_1
      //   1187: invokeinterface onDown : (Landroid/view/MotionEvent;)Z
      //   1192: ior
      //   1193: istore #11
      //   1195: iload #11
      //   1197: ireturn
    }
    
    public void setIsLongpressEnabled(boolean param1Boolean) {
      this.mIsLongpressEnabled = param1Boolean;
    }
    
    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener param1OnDoubleTapListener) {
      this.mDoubleTapListener = param1OnDoubleTapListener;
    }
    
    private class GestureHandler extends Handler {
      GestureHandler() {}
      
      GestureHandler(Handler param2Handler) {
        super(param2Handler.getLooper());
      }
      
      public void handleMessage(Message param2Message) {
        int i = param2Message.what;
        if (i != 1) {
          if (i != 2) {
            if (i == 3) {
              if (GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDoubleTapListener != null)
                if (!GestureDetectorCompat.GestureDetectorCompatImplBase.this.mStillDown) {
                  GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDoubleTapListener.onSingleTapConfirmed(GestureDetectorCompat.GestureDetectorCompatImplBase.this.mCurrentDownEvent);
                } else {
                  GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDeferConfirmSingleTap = true;
                }  
            } else {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("Unknown message ");
              stringBuilder.append(param2Message);
              throw new RuntimeException(stringBuilder.toString());
            } 
          } else {
            GestureDetectorCompat.GestureDetectorCompatImplBase.this.dispatchLongPress();
          } 
        } else {
          GestureDetectorCompat.GestureDetectorCompatImplBase.this.mListener.onShowPress(GestureDetectorCompat.GestureDetectorCompatImplBase.this.mCurrentDownEvent);
        } 
      }
    }
  }
  
  private class GestureHandler extends Handler {
    GestureHandler() {}
    
    GestureHandler(Handler param1Handler) {
      super(param1Handler.getLooper());
    }
    
    public void handleMessage(Message param1Message) {
      int i = param1Message.what;
      if (i != 1) {
        if (i != 2) {
          if (i == 3) {
            if (this.this$0.mDoubleTapListener != null)
              if (!this.this$0.mStillDown) {
                this.this$0.mDoubleTapListener.onSingleTapConfirmed(this.this$0.mCurrentDownEvent);
              } else {
                this.this$0.mDeferConfirmSingleTap = true;
              }  
          } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown message ");
            stringBuilder.append(param1Message);
            throw new RuntimeException(stringBuilder.toString());
          } 
        } else {
          this.this$0.dispatchLongPress();
        } 
      } else {
        this.this$0.mListener.onShowPress(this.this$0.mCurrentDownEvent);
      } 
    }
  }
  
  static class GestureDetectorCompatImplJellybeanMr2 implements GestureDetectorCompatImpl {
    private final GestureDetector mDetector;
    
    GestureDetectorCompatImplJellybeanMr2(Context param1Context, GestureDetector.OnGestureListener param1OnGestureListener, Handler param1Handler) {
      this.mDetector = new GestureDetector(param1Context, param1OnGestureListener, param1Handler);
    }
    
    public boolean isLongpressEnabled() {
      return this.mDetector.isLongpressEnabled();
    }
    
    public boolean onTouchEvent(MotionEvent param1MotionEvent) {
      return this.mDetector.onTouchEvent(param1MotionEvent);
    }
    
    public void setIsLongpressEnabled(boolean param1Boolean) {
      this.mDetector.setIsLongpressEnabled(param1Boolean);
    }
    
    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener param1OnDoubleTapListener) {
      this.mDetector.setOnDoubleTapListener(param1OnDoubleTapListener);
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v4/view/GestureDetectorCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */