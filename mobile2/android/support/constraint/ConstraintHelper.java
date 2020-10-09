package android.support.constraint;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.Helper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.util.Arrays;

public abstract class ConstraintHelper extends View {
  protected int mCount;
  
  protected Helper mHelperWidget;
  
  protected int[] mIds = new int[32];
  
  private String mReferenceIds;
  
  protected boolean mUseViewMeasure = false;
  
  protected Context myContext;
  
  public ConstraintHelper(Context paramContext) {
    super(paramContext);
    this.myContext = paramContext;
    init((AttributeSet)null);
  }
  
  public ConstraintHelper(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    this.myContext = paramContext;
    init(paramAttributeSet);
  }
  
  public ConstraintHelper(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    this.myContext = paramContext;
    init(paramAttributeSet);
  }
  
  private void addID(String paramString) {
    if (paramString == null)
      return; 
    if (this.myContext == null)
      return; 
    paramString = paramString.trim();
    int i = 0;
    try {
      int k = R.id.class.getField(paramString).getInt(null);
      i = k;
    } catch (Exception exception) {}
    int j = i;
    if (i == 0)
      j = this.myContext.getResources().getIdentifier(paramString, "id", this.myContext.getPackageName()); 
    i = j;
    if (j == 0) {
      i = j;
      if (isInEditMode()) {
        i = j;
        if (getParent() instanceof ConstraintLayout) {
          Object object = ((ConstraintLayout)getParent()).getDesignInformation(0, paramString);
          i = j;
          if (object != null) {
            i = j;
            if (object instanceof Integer)
              i = ((Integer)object).intValue(); 
          } 
        } 
      } 
    } 
    if (i != 0) {
      setTag(i, (Object)null);
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Could not find id of \"");
      stringBuilder.append(paramString);
      stringBuilder.append("\"");
      Log.w("ConstraintHelper", stringBuilder.toString());
    } 
  }
  
  private void setIds(String paramString) {
    if (paramString == null)
      return; 
    for (int i = 0;; i = j + 1) {
      int j = paramString.indexOf(',', i);
      if (j == -1) {
        addID(paramString.substring(i));
        return;
      } 
      addID(paramString.substring(i, j));
    } 
  }
  
  public int[] getReferencedIds() {
    return Arrays.copyOf(this.mIds, this.mCount);
  }
  
  protected void init(AttributeSet paramAttributeSet) {
    if (paramAttributeSet != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.ConstraintLayout_Layout);
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.ConstraintLayout_Layout_constraint_referenced_ids) {
          String str = typedArray.getString(j);
          this.mReferenceIds = str;
          setIds(str);
        } 
      } 
    } 
  }
  
  public void onDraw(Canvas paramCanvas) {}
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    if (this.mUseViewMeasure) {
      super.onMeasure(paramInt1, paramInt2);
    } else {
      setMeasuredDimension(0, 0);
    } 
  }
  
  public void setReferencedIds(int[] paramArrayOfint) {
    this.mCount = 0;
    for (byte b = 0; b < paramArrayOfint.length; b++)
      setTag(paramArrayOfint[b], (Object)null); 
  }
  
  public void setTag(int paramInt, Object paramObject) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mCount : I
    //   4: istore_3
    //   5: aload_0
    //   6: getfield mIds : [I
    //   9: astore_2
    //   10: iload_3
    //   11: iconst_1
    //   12: iadd
    //   13: aload_2
    //   14: arraylength
    //   15: if_icmple -> 30
    //   18: aload_0
    //   19: aload_2
    //   20: aload_2
    //   21: arraylength
    //   22: iconst_2
    //   23: imul
    //   24: invokestatic copyOf : ([II)[I
    //   27: putfield mIds : [I
    //   30: aload_0
    //   31: getfield mIds : [I
    //   34: astore_2
    //   35: aload_0
    //   36: getfield mCount : I
    //   39: istore_3
    //   40: aload_2
    //   41: iload_3
    //   42: iload_1
    //   43: iastore
    //   44: aload_0
    //   45: iload_3
    //   46: iconst_1
    //   47: iadd
    //   48: putfield mCount : I
    //   51: return
  }
  
  public void updatePostLayout(ConstraintLayout paramConstraintLayout) {}
  
  public void updatePostMeasure(ConstraintLayout paramConstraintLayout) {}
  
  public void updatePreLayout(ConstraintLayout paramConstraintLayout) {
    if (isInEditMode())
      setIds(this.mReferenceIds); 
    Helper helper = this.mHelperWidget;
    if (helper == null)
      return; 
    helper.removeAllIds();
    for (byte b = 0; b < this.mCount; b++) {
      View view = paramConstraintLayout.getViewById(this.mIds[b]);
      if (view != null)
        this.mHelperWidget.add(paramConstraintLayout.getViewWidget(view)); 
    } 
  }
  
  public void validateParams() {
    if (this.mHelperWidget == null)
      return; 
    ViewGroup.LayoutParams layoutParams = getLayoutParams();
    if (layoutParams instanceof ConstraintLayout.LayoutParams)
      ((ConstraintLayout.LayoutParams)layoutParams).widget = (ConstraintWidget)this.mHelperWidget; 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/constraint/ConstraintHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */