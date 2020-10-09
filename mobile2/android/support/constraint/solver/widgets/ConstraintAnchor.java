package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import android.support.constraint.solver.SolverVariable;
import java.util.ArrayList;
import java.util.HashSet;

public class ConstraintAnchor {
  private static final boolean ALLOW_BINARY = false;
  
  public static final int AUTO_CONSTRAINT_CREATOR = 2;
  
  public static final int SCOUT_CREATOR = 1;
  
  private static final int UNSET_GONE_MARGIN = -1;
  
  public static final int USER_CREATOR = 0;
  
  private int mConnectionCreator = 0;
  
  private ConnectionType mConnectionType = ConnectionType.RELAXED;
  
  int mGoneMargin = -1;
  
  public int mMargin = 0;
  
  final ConstraintWidget mOwner;
  
  private ResolutionAnchor mResolutionAnchor = new ResolutionAnchor(this);
  
  SolverVariable mSolverVariable;
  
  private Strength mStrength = Strength.NONE;
  
  ConstraintAnchor mTarget;
  
  final Type mType;
  
  public ConstraintAnchor(ConstraintWidget paramConstraintWidget, Type paramType) {
    this.mOwner = paramConstraintWidget;
    this.mType = paramType;
  }
  
  private boolean isConnectionToMe(ConstraintWidget paramConstraintWidget, HashSet<ConstraintWidget> paramHashSet) {
    if (paramHashSet.contains(paramConstraintWidget))
      return false; 
    paramHashSet.add(paramConstraintWidget);
    if (paramConstraintWidget == getOwner())
      return true; 
    ArrayList<ConstraintAnchor> arrayList = paramConstraintWidget.getAnchors();
    byte b = 0;
    int i = arrayList.size();
    while (b < i) {
      ConstraintAnchor constraintAnchor = arrayList.get(b);
      if (constraintAnchor.isSimilarDimensionConnection(this) && constraintAnchor.isConnected() && isConnectionToMe(constraintAnchor.getTarget().getOwner(), paramHashSet))
        return true; 
      b++;
    } 
    return false;
  }
  
  public boolean connect(ConstraintAnchor paramConstraintAnchor, int paramInt) {
    return connect(paramConstraintAnchor, paramInt, -1, Strength.STRONG, 0, false);
  }
  
  public boolean connect(ConstraintAnchor paramConstraintAnchor, int paramInt1, int paramInt2) {
    return connect(paramConstraintAnchor, paramInt1, -1, Strength.STRONG, paramInt2, false);
  }
  
  public boolean connect(ConstraintAnchor paramConstraintAnchor, int paramInt1, int paramInt2, Strength paramStrength, int paramInt3, boolean paramBoolean) {
    if (paramConstraintAnchor == null) {
      this.mTarget = null;
      this.mMargin = 0;
      this.mGoneMargin = -1;
      this.mStrength = Strength.NONE;
      this.mConnectionCreator = 2;
      return true;
    } 
    if (!paramBoolean && !isValidConnection(paramConstraintAnchor))
      return false; 
    this.mTarget = paramConstraintAnchor;
    if (paramInt1 > 0) {
      this.mMargin = paramInt1;
    } else {
      this.mMargin = 0;
    } 
    this.mGoneMargin = paramInt2;
    this.mStrength = paramStrength;
    this.mConnectionCreator = paramInt3;
    return true;
  }
  
  public boolean connect(ConstraintAnchor paramConstraintAnchor, int paramInt1, Strength paramStrength, int paramInt2) {
    return connect(paramConstraintAnchor, paramInt1, -1, paramStrength, paramInt2, false);
  }
  
  public int getConnectionCreator() {
    return this.mConnectionCreator;
  }
  
  public ConnectionType getConnectionType() {
    return this.mConnectionType;
  }
  
  public int getMargin() {
    if (this.mOwner.getVisibility() == 8)
      return 0; 
    if (this.mGoneMargin > -1) {
      ConstraintAnchor constraintAnchor = this.mTarget;
      if (constraintAnchor != null && constraintAnchor.mOwner.getVisibility() == 8)
        return this.mGoneMargin; 
    } 
    return this.mMargin;
  }
  
  public final ConstraintAnchor getOpposite() {
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case null:
        return this.mOwner.mTop;
      case null:
        return this.mOwner.mBottom;
      case null:
        return this.mOwner.mLeft;
      case null:
        return this.mOwner.mRight;
      case null:
      case null:
      case null:
      case null:
      case null:
        break;
    } 
    return null;
  }
  
  public ConstraintWidget getOwner() {
    return this.mOwner;
  }
  
  public int getPriorityLevel() {
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case null:
        return 0;
      case null:
        return 0;
      case null:
        return 0;
      case null:
        return 1;
      case null:
        return 2;
      case null:
        return 2;
      case null:
        return 2;
      case null:
        return 2;
      case null:
        break;
    } 
    return 2;
  }
  
  public ResolutionAnchor getResolutionNode() {
    return this.mResolutionAnchor;
  }
  
  public int getSnapPriorityLevel() {
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case null:
        return 0;
      case null:
        return 1;
      case null:
        return 0;
      case null:
        return 2;
      case null:
        return 0;
      case null:
        return 0;
      case null:
        return 1;
      case null:
        return 1;
      case null:
        break;
    } 
    return 3;
  }
  
  public SolverVariable getSolverVariable() {
    return this.mSolverVariable;
  }
  
  public Strength getStrength() {
    return this.mStrength;
  }
  
  public ConstraintAnchor getTarget() {
    return this.mTarget;
  }
  
  public Type getType() {
    return this.mType;
  }
  
  public boolean isConnected() {
    boolean bool;
    if (this.mTarget != null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isConnectionAllowed(ConstraintWidget paramConstraintWidget) {
    if (isConnectionToMe(paramConstraintWidget, new HashSet<>()))
      return false; 
    ConstraintWidget constraintWidget = getOwner().getParent();
    return (constraintWidget == paramConstraintWidget) ? true : ((paramConstraintWidget.getParent() == constraintWidget));
  }
  
  public boolean isConnectionAllowed(ConstraintWidget paramConstraintWidget, ConstraintAnchor paramConstraintAnchor) {
    return isConnectionAllowed(paramConstraintWidget);
  }
  
  public boolean isSideAnchor() {
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case null:
      case null:
      case null:
      case null:
        return true;
      case null:
      case null:
      case null:
      case null:
      case null:
        break;
    } 
    return false;
  }
  
  public boolean isSimilarDimensionConnection(ConstraintAnchor paramConstraintAnchor) {
    Type type1 = paramConstraintAnchor.getType();
    Type type2 = this.mType;
    boolean bool1 = true;
    boolean bool2 = true;
    boolean bool3 = true;
    if (type1 == type2)
      return true; 
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case null:
        return false;
      case null:
      case null:
      case null:
      case null:
        bool2 = bool3;
        if (type1 != Type.TOP) {
          bool2 = bool3;
          if (type1 != Type.BOTTOM) {
            bool2 = bool3;
            if (type1 != Type.CENTER_Y)
              if (type1 == Type.BASELINE) {
                bool2 = bool3;
              } else {
                bool2 = false;
              }  
          } 
        } 
        return bool2;
      case null:
      case null:
      case null:
        bool2 = bool1;
        if (type1 != Type.LEFT) {
          bool2 = bool1;
          if (type1 != Type.RIGHT)
            if (type1 == Type.CENTER_X) {
              bool2 = bool1;
            } else {
              bool2 = false;
            }  
        } 
        return bool2;
      case null:
        break;
    } 
    if (type1 == Type.BASELINE)
      bool2 = false; 
    return bool2;
  }
  
  public boolean isSnapCompatibleWith(ConstraintAnchor paramConstraintAnchor) {
    int i;
    if (this.mType == Type.CENTER)
      return false; 
    if (this.mType == paramConstraintAnchor.getType())
      return true; 
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case null:
        i = null.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[paramConstraintAnchor.getType().ordinal()];
        return (i != 4) ? (!(i != 5)) : true;
      case null:
        i = null.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[paramConstraintAnchor.getType().ordinal()];
        return (i != 2) ? (!(i != 3)) : true;
      case null:
        i = null.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[paramConstraintAnchor.getType().ordinal()];
        return (i != 4) ? (!(i != 8)) : true;
      case null:
        i = null.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[paramConstraintAnchor.getType().ordinal()];
        return (i != 5) ? (!(i != 8)) : true;
      case null:
        i = null.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[paramConstraintAnchor.getType().ordinal()];
        return (i != 2) ? (!(i != 7)) : true;
      case null:
        i = null.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type[paramConstraintAnchor.getType().ordinal()];
        return (i != 3) ? (!(i != 7)) : true;
      case null:
      case null:
      case null:
        break;
    } 
    return false;
  }
  
  public boolean isValidConnection(ConstraintAnchor paramConstraintAnchor) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: iconst_0
    //   3: istore_3
    //   4: iconst_0
    //   5: istore #4
    //   7: aload_1
    //   8: ifnonnull -> 13
    //   11: iconst_0
    //   12: ireturn
    //   13: aload_1
    //   14: invokevirtual getType : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   17: astore #5
    //   19: aload_0
    //   20: getfield mType : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   23: astore #6
    //   25: aload #5
    //   27: aload #6
    //   29: if_acmpne -> 64
    //   32: aload #6
    //   34: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BASELINE : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   37: if_acmpne -> 62
    //   40: aload_1
    //   41: invokevirtual getOwner : ()Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   44: invokevirtual hasBaseline : ()Z
    //   47: ifeq -> 60
    //   50: aload_0
    //   51: invokevirtual getOwner : ()Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   54: invokevirtual hasBaseline : ()Z
    //   57: ifne -> 62
    //   60: iconst_0
    //   61: ireturn
    //   62: iconst_1
    //   63: ireturn
    //   64: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintAnchor$Type : [I
    //   67: aload_0
    //   68: getfield mType : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   71: invokevirtual ordinal : ()I
    //   74: iaload
    //   75: tableswitch default -> 124, 1 -> 272, 2 -> 207, 3 -> 207, 4 -> 141, 5 -> 141, 6 -> 139, 7 -> 139, 8 -> 139, 9 -> 139
    //   124: new java/lang/AssertionError
    //   127: dup
    //   128: aload_0
    //   129: getfield mType : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   132: invokevirtual name : ()Ljava/lang/String;
    //   135: invokespecial <init> : (Ljava/lang/Object;)V
    //   138: athrow
    //   139: iconst_0
    //   140: ireturn
    //   141: aload #5
    //   143: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   146: if_acmpeq -> 166
    //   149: aload #5
    //   151: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   154: if_acmpne -> 160
    //   157: goto -> 166
    //   160: iconst_0
    //   161: istore #7
    //   163: goto -> 169
    //   166: iconst_1
    //   167: istore #7
    //   169: iload #7
    //   171: istore_3
    //   172: aload_1
    //   173: invokevirtual getOwner : ()Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   176: instanceof android/support/constraint/solver/widgets/Guideline
    //   179: ifeq -> 205
    //   182: iload #7
    //   184: ifne -> 199
    //   187: iload #4
    //   189: istore #7
    //   191: aload #5
    //   193: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_Y : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   196: if_acmpne -> 202
    //   199: iconst_1
    //   200: istore #7
    //   202: iload #7
    //   204: istore_3
    //   205: iload_3
    //   206: ireturn
    //   207: aload #5
    //   209: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   212: if_acmpeq -> 232
    //   215: aload #5
    //   217: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   220: if_acmpne -> 226
    //   223: goto -> 232
    //   226: iconst_0
    //   227: istore #7
    //   229: goto -> 235
    //   232: iconst_1
    //   233: istore #7
    //   235: iload #7
    //   237: istore_3
    //   238: aload_1
    //   239: invokevirtual getOwner : ()Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   242: instanceof android/support/constraint/solver/widgets/Guideline
    //   245: ifeq -> 270
    //   248: iload #7
    //   250: ifne -> 264
    //   253: iload_2
    //   254: istore #7
    //   256: aload #5
    //   258: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_X : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   261: if_acmpne -> 267
    //   264: iconst_1
    //   265: istore #7
    //   267: iload #7
    //   269: istore_3
    //   270: iload_3
    //   271: ireturn
    //   272: iload_3
    //   273: istore #7
    //   275: aload #5
    //   277: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BASELINE : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   280: if_acmpeq -> 308
    //   283: iload_3
    //   284: istore #7
    //   286: aload #5
    //   288: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_X : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   291: if_acmpeq -> 308
    //   294: iload_3
    //   295: istore #7
    //   297: aload #5
    //   299: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_Y : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   302: if_acmpeq -> 308
    //   305: iconst_1
    //   306: istore #7
    //   308: iload #7
    //   310: ireturn
  }
  
  public boolean isVerticalAnchor() {
    switch (this.mType) {
      default:
        throw new AssertionError(this.mType.name());
      case null:
      case null:
      case null:
      case null:
      case null:
        return true;
      case null:
      case null:
      case null:
      case null:
        break;
    } 
    return false;
  }
  
  public void reset() {
    this.mTarget = null;
    this.mMargin = 0;
    this.mGoneMargin = -1;
    this.mStrength = Strength.STRONG;
    this.mConnectionCreator = 0;
    this.mConnectionType = ConnectionType.RELAXED;
    this.mResolutionAnchor.reset();
  }
  
  public void resetSolverVariable(Cache paramCache) {
    SolverVariable solverVariable = this.mSolverVariable;
    if (solverVariable == null) {
      this.mSolverVariable = new SolverVariable(SolverVariable.Type.UNRESTRICTED, null);
    } else {
      solverVariable.reset();
    } 
  }
  
  public void setConnectionCreator(int paramInt) {
    this.mConnectionCreator = paramInt;
  }
  
  public void setConnectionType(ConnectionType paramConnectionType) {
    this.mConnectionType = paramConnectionType;
  }
  
  public void setGoneMargin(int paramInt) {
    if (isConnected())
      this.mGoneMargin = paramInt; 
  }
  
  public void setMargin(int paramInt) {
    if (isConnected())
      this.mMargin = paramInt; 
  }
  
  public void setStrength(Strength paramStrength) {
    if (isConnected())
      this.mStrength = paramStrength; 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.mOwner.getDebugName());
    stringBuilder.append(":");
    stringBuilder.append(this.mType.toString());
    return stringBuilder.toString();
  }
  
  public enum ConnectionType {
    RELAXED, STRICT;
    
    static {
      ConnectionType connectionType = new ConnectionType("STRICT", 1);
      STRICT = connectionType;
      $VALUES = new ConnectionType[] { RELAXED, connectionType };
    }
  }
  
  public enum Strength {
    NONE, STRONG, WEAK;
    
    static {
      Strength strength = new Strength("WEAK", 2);
      WEAK = strength;
      $VALUES = new Strength[] { NONE, STRONG, strength };
    }
  }
  
  public enum Type {
    NONE, RIGHT, TOP, BASELINE, BOTTOM, CENTER, CENTER_X, CENTER_Y, LEFT;
    
    static {
      RIGHT = new Type("RIGHT", 3);
      BOTTOM = new Type("BOTTOM", 4);
      BASELINE = new Type("BASELINE", 5);
      CENTER = new Type("CENTER", 6);
      CENTER_X = new Type("CENTER_X", 7);
      Type type = new Type("CENTER_Y", 8);
      CENTER_Y = type;
      $VALUES = new Type[] { NONE, LEFT, TOP, RIGHT, BOTTOM, BASELINE, CENTER, CENTER_X, type };
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/constraint/solver/widgets/ConstraintAnchor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */