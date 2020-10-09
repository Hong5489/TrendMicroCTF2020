package android.support.transition;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.graphics.PathParser;
import android.util.AttributeSet;
import org.xmlpull.v1.XmlPullParser;

public class PatternPathMotion extends PathMotion {
  private Path mOriginalPatternPath;
  
  private final Path mPatternPath = new Path();
  
  private final Matrix mTempMatrix = new Matrix();
  
  public PatternPathMotion() {
    this.mPatternPath.lineTo(1.0F, 0.0F);
    this.mOriginalPatternPath = this.mPatternPath;
  }
  
  public PatternPathMotion(Context paramContext, AttributeSet paramAttributeSet) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, Styleable.PATTERN_PATH_MOTION);
    try {
      String str = TypedArrayUtils.getNamedString(typedArray, (XmlPullParser)paramAttributeSet, "patternPathData", 0);
      if (str != null) {
        setPatternPath(PathParser.createPathFromPathData(str));
        return;
      } 
      RuntimeException runtimeException = new RuntimeException();
      this("pathData must be supplied for patternPathMotion");
      throw runtimeException;
    } finally {
      typedArray.recycle();
    } 
  }
  
  public PatternPathMotion(Path paramPath) {
    setPatternPath(paramPath);
  }
  
  private static float distance(float paramFloat1, float paramFloat2) {
    return (float)Math.sqrt((paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2));
  }
  
  public Path getPath(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    paramFloat3 -= paramFloat1;
    float f = paramFloat4 - paramFloat2;
    paramFloat4 = distance(paramFloat3, f);
    double d = Math.atan2(f, paramFloat3);
    this.mTempMatrix.setScale(paramFloat4, paramFloat4);
    this.mTempMatrix.postRotate((float)Math.toDegrees(d));
    this.mTempMatrix.postTranslate(paramFloat1, paramFloat2);
    Path path = new Path();
    this.mPatternPath.transform(this.mTempMatrix, path);
    return path;
  }
  
  public Path getPatternPath() {
    return this.mOriginalPatternPath;
  }
  
  public void setPatternPath(Path paramPath) {
    PathMeasure pathMeasure = new PathMeasure(paramPath, false);
    float f1 = pathMeasure.getLength();
    float[] arrayOfFloat = new float[2];
    pathMeasure.getPosTan(f1, arrayOfFloat, null);
    float f2 = arrayOfFloat[0];
    f1 = arrayOfFloat[1];
    pathMeasure.getPosTan(0.0F, arrayOfFloat, null);
    float f3 = arrayOfFloat[0];
    float f4 = arrayOfFloat[1];
    if (f3 != f2 || f4 != f1) {
      this.mTempMatrix.setTranslate(-f3, -f4);
      f2 -= f3;
      f1 -= f4;
      f4 = 1.0F / distance(f2, f1);
      this.mTempMatrix.postScale(f4, f4);
      double d = Math.atan2(f1, f2);
      this.mTempMatrix.postRotate((float)Math.toDegrees(-d));
      paramPath.transform(this.mTempMatrix, this.mPatternPath);
      this.mOriginalPatternPath = paramPath;
      return;
    } 
    throw new IllegalArgumentException("pattern must not end at the starting point");
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/transition/PatternPathMotion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */