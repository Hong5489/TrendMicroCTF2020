package android.support.v4.graphics;

import android.graphics.Path;
import android.util.Log;
import java.util.ArrayList;

public class PathParser {
  private static final String LOGTAG = "PathParser";
  
  private static void addNode(ArrayList<PathDataNode> paramArrayList, char paramChar, float[] paramArrayOffloat) {
    paramArrayList.add(new PathDataNode(paramChar, paramArrayOffloat));
  }
  
  public static boolean canMorph(PathDataNode[] paramArrayOfPathDataNode1, PathDataNode[] paramArrayOfPathDataNode2) {
    if (paramArrayOfPathDataNode1 == null || paramArrayOfPathDataNode2 == null)
      return false; 
    if (paramArrayOfPathDataNode1.length != paramArrayOfPathDataNode2.length)
      return false; 
    for (byte b = 0; b < paramArrayOfPathDataNode1.length; b++) {
      if ((paramArrayOfPathDataNode1[b]).mType != (paramArrayOfPathDataNode2[b]).mType || (paramArrayOfPathDataNode1[b]).mParams.length != (paramArrayOfPathDataNode2[b]).mParams.length)
        return false; 
    } 
    return true;
  }
  
  static float[] copyOfRange(float[] paramArrayOffloat, int paramInt1, int paramInt2) {
    if (paramInt1 <= paramInt2) {
      int i = paramArrayOffloat.length;
      if (paramInt1 >= 0 && paramInt1 <= i) {
        paramInt2 -= paramInt1;
        i = Math.min(paramInt2, i - paramInt1);
        float[] arrayOfFloat = new float[paramInt2];
        System.arraycopy(paramArrayOffloat, paramInt1, arrayOfFloat, 0, i);
        return arrayOfFloat;
      } 
      throw new ArrayIndexOutOfBoundsException();
    } 
    throw new IllegalArgumentException();
  }
  
  public static PathDataNode[] createNodesFromPathData(String paramString) {
    if (paramString == null)
      return null; 
    int i = 0;
    int j = 1;
    ArrayList<PathDataNode> arrayList = new ArrayList();
    while (j < paramString.length()) {
      j = nextStart(paramString, j);
      String str = paramString.substring(i, j).trim();
      if (str.length() > 0) {
        float[] arrayOfFloat = getFloats(str);
        addNode(arrayList, str.charAt(0), arrayOfFloat);
      } 
      i = j;
      j++;
    } 
    if (j - i == 1 && i < paramString.length())
      addNode(arrayList, paramString.charAt(i), new float[0]); 
    return arrayList.<PathDataNode>toArray(new PathDataNode[arrayList.size()]);
  }
  
  public static Path createPathFromPathData(String paramString) {
    Path path = new Path();
    PathDataNode[] arrayOfPathDataNode = createNodesFromPathData(paramString);
    if (arrayOfPathDataNode != null)
      try {
        PathDataNode.nodesToPath(arrayOfPathDataNode, path);
        return path;
      } catch (RuntimeException runtimeException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Error in parsing ");
        stringBuilder.append(paramString);
        throw new RuntimeException(stringBuilder.toString(), runtimeException);
      }  
    return null;
  }
  
  public static PathDataNode[] deepCopyNodes(PathDataNode[] paramArrayOfPathDataNode) {
    if (paramArrayOfPathDataNode == null)
      return null; 
    PathDataNode[] arrayOfPathDataNode = new PathDataNode[paramArrayOfPathDataNode.length];
    for (byte b = 0; b < paramArrayOfPathDataNode.length; b++)
      arrayOfPathDataNode[b] = new PathDataNode(paramArrayOfPathDataNode[b]); 
    return arrayOfPathDataNode;
  }
  
  private static void extract(String paramString, int paramInt, ExtractFloatResult paramExtractFloatResult) {
    int i = paramInt;
    char c = Character.MIN_VALUE;
    paramExtractFloatResult.mEndWithNegOrDot = false;
    boolean bool1 = false;
    boolean bool2 = false;
    while (i < paramString.length()) {
      boolean bool = false;
      char c1 = paramString.charAt(i);
      if (c1 != ' ') {
        boolean bool3;
        boolean bool4;
        if (c1 != 'E' && c1 != 'e') {
          switch (c1) {
            default:
              c1 = c;
              bool3 = bool1;
              bool4 = bool;
              break;
            case '.':
              if (!bool1) {
                bool3 = true;
                c1 = c;
                bool4 = bool;
                break;
              } 
              c1 = '\001';
              paramExtractFloatResult.mEndWithNegOrDot = true;
              bool3 = bool1;
              bool4 = bool;
              break;
            case '-':
              c1 = c;
              bool3 = bool1;
              bool4 = bool;
              if (i != paramInt) {
                c1 = c;
                bool3 = bool1;
                bool4 = bool;
                if (!bool2) {
                  c1 = '\001';
                  paramExtractFloatResult.mEndWithNegOrDot = true;
                  bool3 = bool1;
                  bool4 = bool;
                } 
              } 
              break;
            case ',':
              c1 = '\001';
              bool4 = bool;
              bool3 = bool1;
              break;
          } 
        } else {
          bool4 = true;
          c1 = c;
          bool3 = bool1;
        } 
        if (c1 != '\000')
          break; 
        i++;
        c = c1;
        bool1 = bool3;
        bool2 = bool4;
      } 
    } 
    paramExtractFloatResult.mEndPosition = i;
  }
  
  private static float[] getFloats(String paramString) {
    if (paramString.charAt(0) == 'z' || paramString.charAt(0) == 'Z')
      return new float[0]; 
    try {
      null = new float[paramString.length()];
      int i = 0;
      int j = 1;
      ExtractFloatResult extractFloatResult = new ExtractFloatResult();
      this();
      int k = paramString.length();
      while (j < k) {
        extract(paramString, j, extractFloatResult);
        int m = extractFloatResult.mEndPosition;
        int n = i;
        if (j < m) {
          null[i] = Float.parseFloat(paramString.substring(j, m));
          n = i + 1;
        } 
        if (extractFloatResult.mEndWithNegOrDot) {
          j = m;
          i = n;
          continue;
        } 
        j = m + 1;
        i = n;
      } 
      return copyOfRange(null, 0, i);
    } catch (NumberFormatException numberFormatException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("error in parsing \"");
      stringBuilder.append(paramString);
      stringBuilder.append("\"");
      throw new RuntimeException(stringBuilder.toString(), numberFormatException);
    } 
  }
  
  private static int nextStart(String paramString, int paramInt) {
    while (paramInt < paramString.length()) {
      char c = paramString.charAt(paramInt);
      if (((c - 65) * (c - 90) <= 0 || (c - 97) * (c - 122) <= 0) && c != 'e' && c != 'E')
        return paramInt; 
      paramInt++;
    } 
    return paramInt;
  }
  
  public static void updateNodes(PathDataNode[] paramArrayOfPathDataNode1, PathDataNode[] paramArrayOfPathDataNode2) {
    for (byte b = 0; b < paramArrayOfPathDataNode2.length; b++) {
      (paramArrayOfPathDataNode1[b]).mType = (char)(paramArrayOfPathDataNode2[b]).mType;
      for (byte b1 = 0; b1 < (paramArrayOfPathDataNode2[b]).mParams.length; b1++)
        (paramArrayOfPathDataNode1[b]).mParams[b1] = (paramArrayOfPathDataNode2[b]).mParams[b1]; 
    } 
  }
  
  private static class ExtractFloatResult {
    int mEndPosition;
    
    boolean mEndWithNegOrDot;
  }
  
  public static class PathDataNode {
    public float[] mParams;
    
    public char mType;
    
    PathDataNode(char param1Char, float[] param1ArrayOffloat) {
      this.mType = (char)param1Char;
      this.mParams = param1ArrayOffloat;
    }
    
    PathDataNode(PathDataNode param1PathDataNode) {
      this.mType = (char)param1PathDataNode.mType;
      float[] arrayOfFloat = param1PathDataNode.mParams;
      this.mParams = PathParser.copyOfRange(arrayOfFloat, 0, arrayOfFloat.length);
    }
    
    private static void addCommand(Path param1Path, float[] param1ArrayOffloat1, char param1Char1, char param1Char2, float[] param1ArrayOffloat2) {
      byte b;
      float f1 = param1ArrayOffloat1[0];
      float f2 = param1ArrayOffloat1[1];
      float f3 = param1ArrayOffloat1[2];
      float f4 = param1ArrayOffloat1[3];
      float f5 = param1ArrayOffloat1[4];
      float f6 = param1ArrayOffloat1[5];
      switch (param1Char2) {
        default:
          b = 2;
          break;
        case 'Z':
        case 'z':
          param1Path.close();
          f1 = f5;
          f2 = f6;
          f3 = f5;
          f4 = f6;
          param1Path.moveTo(f1, f2);
          b = 2;
          break;
        case 'Q':
        case 'S':
        case 'q':
        case 's':
          b = 4;
          break;
        case 'L':
        case 'M':
        case 'T':
        case 'l':
        case 'm':
        case 't':
          b = 2;
          break;
        case 'H':
        case 'V':
        case 'h':
        case 'v':
          b = 1;
          break;
        case 'C':
        case 'c':
          b = 6;
          break;
        case 'A':
        case 'a':
          b = 7;
          break;
      } 
      char c1 = Character.MIN_VALUE;
      float f7 = f3;
      float f8 = f4;
      f4 = f6;
      f6 = f2;
      f3 = f5;
      char c2 = param1Char1;
      param1Char1 = c1;
      f5 = f1;
      while (true) {
        c1 = param1Char2;
        if (param1Char1 < param1ArrayOffloat2.length) {
          if (c1 != 'A') {
            if (c1 != 'C') {
              if (c1 != 'H') {
                if (c1 != 'Q') {
                  if (c1 != 'V') {
                    if (c1 != 'a') {
                      if (c1 != 'c') {
                        if (c1 != 'h') {
                          if (c1 != 'q') {
                            if (c1 != 'v') {
                              if (c1 != 'L') {
                                if (c1 != 'M') {
                                  if (c1 != 'S') {
                                    if (c1 != 'T') {
                                      if (c1 != 'l') {
                                        if (c1 != 'm') {
                                          if (c1 != 's') {
                                            if (c1 != 't') {
                                              f2 = f7;
                                              f1 = f8;
                                            } else {
                                              f1 = 0.0F;
                                              f2 = 0.0F;
                                              if (c2 == 'q' || c2 == 't' || c2 == 'Q' || c2 == 'T') {
                                                f1 = f5 - f7;
                                                f2 = f6 - f8;
                                              } 
                                              param1Path.rQuadTo(f1, f2, param1ArrayOffloat2[param1Char1 + 0], param1ArrayOffloat2[param1Char1 + 1]);
                                              f8 = f5 + param1ArrayOffloat2[param1Char1 + 0];
                                              f7 = f6 + param1ArrayOffloat2[param1Char1 + 1];
                                              f1 = f5 + f1;
                                              float f = f6 + f2;
                                              f6 = f7;
                                              f5 = f8;
                                              f2 = f1;
                                              f1 = f;
                                            } 
                                          } else {
                                            if (c2 == 'c' || c2 == 's' || c2 == 'C' || c2 == 'S') {
                                              f2 = f5 - f7;
                                              f1 = f6 - f8;
                                            } else {
                                              f2 = 0.0F;
                                              f1 = 0.0F;
                                            } 
                                            param1Path.rCubicTo(f2, f1, param1ArrayOffloat2[param1Char1 + 0], param1ArrayOffloat2[param1Char1 + 1], param1ArrayOffloat2[param1Char1 + 2], param1ArrayOffloat2[param1Char1 + 3]);
                                            f1 = param1ArrayOffloat2[param1Char1 + 0];
                                            f8 = param1ArrayOffloat2[param1Char1 + 1];
                                            f2 = f5 + param1ArrayOffloat2[param1Char1 + 2];
                                            f7 = param1ArrayOffloat2[param1Char1 + 3];
                                            f1 += f5;
                                            f8 = f6 + f8;
                                            f6 = f7 + f6;
                                            f5 = f2;
                                            f2 = f1;
                                            f1 = f8;
                                          } 
                                        } else {
                                          f5 += param1ArrayOffloat2[param1Char1 + 0];
                                          f6 += param1ArrayOffloat2[param1Char1 + 1];
                                          if (param1Char1 > '\000') {
                                            param1Path.rLineTo(param1ArrayOffloat2[param1Char1 + 0], param1ArrayOffloat2[param1Char1 + 1]);
                                            f2 = f7;
                                            f1 = f8;
                                          } else {
                                            param1Path.rMoveTo(param1ArrayOffloat2[param1Char1 + 0], param1ArrayOffloat2[param1Char1 + 1]);
                                            f3 = f5;
                                            f4 = f6;
                                            f2 = f7;
                                            f1 = f8;
                                          } 
                                        } 
                                      } else {
                                        param1Path.rLineTo(param1ArrayOffloat2[param1Char1 + 0], param1ArrayOffloat2[param1Char1 + 1]);
                                        f5 += param1ArrayOffloat2[param1Char1 + 0];
                                        f6 += param1ArrayOffloat2[param1Char1 + 1];
                                        f2 = f7;
                                        f1 = f8;
                                      } 
                                    } else {
                                      f1 = f5;
                                      f2 = f6;
                                      if (c2 == 'q' || c2 == 't' || c2 == 'Q' || c2 == 'T') {
                                        f1 = f5 * 2.0F - f7;
                                        f2 = f6 * 2.0F - f8;
                                      } 
                                      param1Path.quadTo(f1, f2, param1ArrayOffloat2[param1Char1 + 0], param1ArrayOffloat2[param1Char1 + 1]);
                                      f5 = param1ArrayOffloat2[param1Char1 + 0];
                                      f6 = param1ArrayOffloat2[param1Char1 + 1];
                                      f8 = f2;
                                      f2 = f1;
                                      f1 = f8;
                                    } 
                                  } else {
                                    if (c2 == 'c' || c2 == 's' || c2 == 'C' || c2 == 'S') {
                                      f5 = f5 * 2.0F - f7;
                                      f6 = f6 * 2.0F - f8;
                                    } 
                                    param1Path.cubicTo(f5, f6, param1ArrayOffloat2[param1Char1 + 0], param1ArrayOffloat2[param1Char1 + 1], param1ArrayOffloat2[param1Char1 + 2], param1ArrayOffloat2[param1Char1 + 3]);
                                    f2 = param1ArrayOffloat2[param1Char1 + 0];
                                    f1 = param1ArrayOffloat2[param1Char1 + 1];
                                    f5 = param1ArrayOffloat2[param1Char1 + 2];
                                    f6 = param1ArrayOffloat2[param1Char1 + 3];
                                  } 
                                } else {
                                  f5 = param1ArrayOffloat2[param1Char1 + 0];
                                  f6 = param1ArrayOffloat2[param1Char1 + 1];
                                  if (param1Char1 > '\000') {
                                    param1Path.lineTo(param1ArrayOffloat2[param1Char1 + 0], param1ArrayOffloat2[param1Char1 + 1]);
                                    f2 = f7;
                                    f1 = f8;
                                  } else {
                                    param1Path.moveTo(param1ArrayOffloat2[param1Char1 + 0], param1ArrayOffloat2[param1Char1 + 1]);
                                    f2 = f5;
                                    f1 = f6;
                                    f3 = f5;
                                    f4 = f6;
                                    f6 = f1;
                                    f5 = f2;
                                    f2 = f7;
                                    f1 = f8;
                                  } 
                                } 
                              } else {
                                param1Path.lineTo(param1ArrayOffloat2[param1Char1 + 0], param1ArrayOffloat2[param1Char1 + 1]);
                                f5 = param1ArrayOffloat2[param1Char1 + 0];
                                f6 = param1ArrayOffloat2[param1Char1 + 1];
                                f2 = f7;
                                f1 = f8;
                              } 
                            } else {
                              param1Path.rLineTo(0.0F, param1ArrayOffloat2[param1Char1 + 0]);
                              f6 += param1ArrayOffloat2[param1Char1 + 0];
                              f2 = f7;
                              f1 = f8;
                            } 
                          } else {
                            param1Path.rQuadTo(param1ArrayOffloat2[param1Char1 + 0], param1ArrayOffloat2[param1Char1 + 1], param1ArrayOffloat2[param1Char1 + 2], param1ArrayOffloat2[param1Char1 + 3]);
                            f1 = param1ArrayOffloat2[param1Char1 + 0];
                            f8 = param1ArrayOffloat2[param1Char1 + 1];
                            f2 = f5 + param1ArrayOffloat2[param1Char1 + 2];
                            f7 = param1ArrayOffloat2[param1Char1 + 3];
                            f1 += f5;
                            f8 = f6 + f8;
                            f6 = f7 + f6;
                            f5 = f2;
                            f2 = f1;
                            f1 = f8;
                          } 
                        } else {
                          param1Path.rLineTo(param1ArrayOffloat2[param1Char1 + 0], 0.0F);
                          f5 += param1ArrayOffloat2[param1Char1 + 0];
                          f2 = f7;
                          f1 = f8;
                        } 
                      } else {
                        param1Path.rCubicTo(param1ArrayOffloat2[param1Char1 + 0], param1ArrayOffloat2[param1Char1 + 1], param1ArrayOffloat2[param1Char1 + 2], param1ArrayOffloat2[param1Char1 + 3], param1ArrayOffloat2[param1Char1 + 4], param1ArrayOffloat2[param1Char1 + 5]);
                        f8 = param1ArrayOffloat2[param1Char1 + 2];
                        f1 = param1ArrayOffloat2[param1Char1 + 3];
                        f2 = f5 + param1ArrayOffloat2[param1Char1 + 4];
                        f7 = param1ArrayOffloat2[param1Char1 + 5];
                        f8 += f5;
                        f1 = f6 + f1;
                        f6 = f7 + f6;
                        f5 = f2;
                        f2 = f8;
                      } 
                    } else {
                      boolean bool1;
                      boolean bool2;
                      f2 = param1ArrayOffloat2[param1Char1 + 5];
                      f8 = param1ArrayOffloat2[param1Char1 + 6];
                      f1 = param1ArrayOffloat2[param1Char1 + 0];
                      f7 = param1ArrayOffloat2[param1Char1 + 1];
                      float f = param1ArrayOffloat2[param1Char1 + 2];
                      if (param1ArrayOffloat2[param1Char1 + 3] != 0.0F) {
                        bool1 = true;
                      } else {
                        bool1 = false;
                      } 
                      if (param1ArrayOffloat2[param1Char1 + 4] != 0.0F) {
                        bool2 = true;
                      } else {
                        bool2 = false;
                      } 
                      c2 = param1Char1;
                      drawArc(param1Path, f5, f6, f2 + f5, f8 + f6, f1, f7, f, bool1, bool2);
                      f5 += param1ArrayOffloat2[c2 + 5];
                      f6 += param1ArrayOffloat2[c2 + 6];
                      f2 = f5;
                      f1 = f6;
                    } 
                  } else {
                    c2 = param1Char1;
                    param1Path.lineTo(f5, param1ArrayOffloat2[c2 + 0]);
                    f6 = param1ArrayOffloat2[c2 + 0];
                    f2 = f7;
                    f1 = f8;
                  } 
                } else {
                  c2 = param1Char1;
                  param1Path.quadTo(param1ArrayOffloat2[c2 + 0], param1ArrayOffloat2[c2 + 1], param1ArrayOffloat2[c2 + 2], param1ArrayOffloat2[c2 + 3]);
                  f2 = param1ArrayOffloat2[c2 + 0];
                  f1 = param1ArrayOffloat2[c2 + 1];
                  f5 = param1ArrayOffloat2[c2 + 2];
                  f6 = param1ArrayOffloat2[c2 + 3];
                } 
              } else {
                c2 = param1Char1;
                param1Path.lineTo(param1ArrayOffloat2[c2 + 0], f6);
                f5 = param1ArrayOffloat2[c2 + 0];
                f2 = f7;
                f1 = f8;
              } 
            } else {
              c2 = param1Char1;
              param1Path.cubicTo(param1ArrayOffloat2[c2 + 0], param1ArrayOffloat2[c2 + 1], param1ArrayOffloat2[c2 + 2], param1ArrayOffloat2[c2 + 3], param1ArrayOffloat2[c2 + 4], param1ArrayOffloat2[c2 + 5]);
              f5 = param1ArrayOffloat2[c2 + 4];
              f6 = param1ArrayOffloat2[c2 + 5];
              f2 = param1ArrayOffloat2[c2 + 2];
              f1 = param1ArrayOffloat2[c2 + 3];
            } 
          } else {
            boolean bool1;
            boolean bool2;
            c2 = param1Char1;
            f1 = param1ArrayOffloat2[c2 + 5];
            float f = param1ArrayOffloat2[c2 + 6];
            f8 = param1ArrayOffloat2[c2 + 0];
            f2 = param1ArrayOffloat2[c2 + 1];
            f7 = param1ArrayOffloat2[c2 + 2];
            if (param1ArrayOffloat2[c2 + 3] != 0.0F) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            if (param1ArrayOffloat2[c2 + 4] != 0.0F) {
              bool2 = true;
            } else {
              bool2 = false;
            } 
            drawArc(param1Path, f5, f6, f1, f, f8, f2, f7, bool1, bool2);
            f2 = param1ArrayOffloat2[c2 + 5];
            f1 = param1ArrayOffloat2[c2 + 6];
            f5 = f2;
            f6 = f1;
          } 
          c2 = param1Char2;
          int i = param1Char1 + b;
          f7 = f2;
          f8 = f1;
          continue;
        } 
        param1ArrayOffloat1[0] = f5;
        param1ArrayOffloat1[1] = f6;
        param1ArrayOffloat1[2] = f7;
        param1ArrayOffloat1[3] = f8;
        param1ArrayOffloat1[4] = f3;
        param1ArrayOffloat1[5] = f4;
        return;
      } 
    }
    
    private static void arcToBezier(Path param1Path, double param1Double1, double param1Double2, double param1Double3, double param1Double4, double param1Double5, double param1Double6, double param1Double7, double param1Double8, double param1Double9) {
      double d1 = param1Double3;
      int i = (int)Math.ceil(Math.abs(param1Double9 * 4.0D / Math.PI));
      double d2 = Math.cos(param1Double7);
      double d3 = Math.sin(param1Double7);
      param1Double7 = Math.cos(param1Double8);
      double d4 = Math.sin(param1Double8);
      double d5 = -d1;
      d1 = -d1 * d3 * d4 + param1Double4 * d2 * param1Double7;
      double d6 = param1Double9 / i;
      byte b = 0;
      param1Double9 = param1Double5;
      double d7 = d5 * d2 * d4 - param1Double4 * d3 * param1Double7;
      double d8 = param1Double8;
      d5 = param1Double6;
      param1Double8 = d4;
      param1Double5 = d3;
      param1Double6 = d2;
      d2 = d6;
      while (true) {
        double d = param1Double3;
        if (b < i) {
          double d9 = d8 + d2;
          double d10 = Math.sin(d9);
          double d11 = Math.cos(d9);
          d6 = param1Double1 + d * param1Double6 * d11 - param1Double4 * param1Double5 * d10;
          d4 = param1Double2 + d * param1Double5 * d11 + param1Double4 * param1Double6 * d10;
          d3 = -d * param1Double6 * d10 - param1Double4 * param1Double5 * d11;
          d = -d * param1Double5 * d10 + param1Double4 * param1Double6 * d11;
          d11 = Math.tan((d9 - d8) / 2.0D);
          d8 = Math.sin(d9 - d8) * (Math.sqrt(d11 * 3.0D * d11 + 4.0D) - 1.0D) / 3.0D;
          param1Path.rLineTo(0.0F, 0.0F);
          param1Path.cubicTo((float)(param1Double9 + d8 * d7), (float)(d5 + d8 * d1), (float)(d6 - d8 * d3), (float)(d4 - d8 * d), (float)d6, (float)d4);
          d8 = d9;
          d5 = d4;
          d7 = d3;
          d1 = d;
          param1Double9 = d6;
          b++;
          continue;
        } 
        break;
      } 
    }
    
    private static void drawArc(Path param1Path, float param1Float1, float param1Float2, float param1Float3, float param1Float4, float param1Float5, float param1Float6, float param1Float7, boolean param1Boolean1, boolean param1Boolean2) {
      double d1 = Math.toRadians(param1Float7);
      double d2 = Math.cos(d1);
      double d3 = Math.sin(d1);
      double d4 = (param1Float1 * d2 + param1Float2 * d3) / param1Float5;
      double d5 = (-param1Float1 * d3 + param1Float2 * d2) / param1Float6;
      double d6 = (param1Float3 * d2 + param1Float4 * d3) / param1Float5;
      double d7 = (-param1Float3 * d3 + param1Float4 * d2) / param1Float6;
      double d8 = d4 - d6;
      double d9 = d5 - d7;
      double d10 = (d4 + d6) / 2.0D;
      double d11 = (d5 + d7) / 2.0D;
      double d12 = d8 * d8 + d9 * d9;
      if (d12 == 0.0D) {
        Log.w("PathParser", " Points are coincident");
        return;
      } 
      double d13 = 1.0D / d12 - 0.25D;
      if (d13 < 0.0D) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Points are too far apart ");
        stringBuilder.append(d12);
        Log.w("PathParser", stringBuilder.toString());
        float f = (float)(Math.sqrt(d12) / 1.99999D);
        drawArc(param1Path, param1Float1, param1Float2, param1Float3, param1Float4, param1Float5 * f, param1Float6 * f, param1Float7, param1Boolean1, param1Boolean2);
        return;
      } 
      d12 = Math.sqrt(d13);
      d8 = d12 * d8;
      d9 = d12 * d9;
      if (param1Boolean1 == param1Boolean2) {
        d10 -= d9;
        d11 += d8;
      } else {
        d10 += d9;
        d11 -= d8;
      } 
      d4 = Math.atan2(d5 - d11, d4 - d10);
      d6 = Math.atan2(d7 - d11, d6 - d10) - d4;
      if (d6 >= 0.0D) {
        param1Boolean1 = true;
      } else {
        param1Boolean1 = false;
      } 
      d7 = d6;
      if (param1Boolean2 != param1Boolean1)
        if (d6 > 0.0D) {
          d7 = d6 - 6.283185307179586D;
        } else {
          d7 = d6 + 6.283185307179586D;
        }  
      d10 *= param1Float5;
      d11 = param1Float6 * d11;
      arcToBezier(param1Path, d10 * d2 - d11 * d3, d10 * d3 + d11 * d2, param1Float5, param1Float6, param1Float1, param1Float2, d1, d4, d7);
    }
    
    public static void nodesToPath(PathDataNode[] param1ArrayOfPathDataNode, Path param1Path) {
      float[] arrayOfFloat = new float[6];
      char c1 = 'm';
      byte b = 0;
      char c2;
      for (c2 = c1; b < param1ArrayOfPathDataNode.length; c2 = c1) {
        addCommand(param1Path, arrayOfFloat, c2, (param1ArrayOfPathDataNode[b]).mType, (param1ArrayOfPathDataNode[b]).mParams);
        c1 = (param1ArrayOfPathDataNode[b]).mType;
        b++;
      } 
    }
    
    public void interpolatePathDataNode(PathDataNode param1PathDataNode1, PathDataNode param1PathDataNode2, float param1Float) {
      byte b = 0;
      while (true) {
        float[] arrayOfFloat = param1PathDataNode1.mParams;
        if (b < arrayOfFloat.length) {
          this.mParams[b] = arrayOfFloat[b] * (1.0F - param1Float) + param1PathDataNode2.mParams[b] * param1Float;
          b++;
          continue;
        } 
        break;
      } 
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v4/graphics/PathParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */