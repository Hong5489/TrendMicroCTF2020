package com.trendmicro.classfile;

import com.trendmicro.hippo.ObjArray;
import com.trendmicro.hippo.UintMap;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class ClassFileWriter {
  public static final short ACC_ABSTRACT = 1024;
  
  public static final short ACC_FINAL = 16;
  
  public static final short ACC_NATIVE = 256;
  
  public static final short ACC_PRIVATE = 2;
  
  public static final short ACC_PROTECTED = 4;
  
  public static final short ACC_PUBLIC = 1;
  
  public static final short ACC_STATIC = 8;
  
  public static final short ACC_SUPER = 32;
  
  public static final short ACC_SYNCHRONIZED = 32;
  
  public static final short ACC_TRANSIENT = 128;
  
  public static final short ACC_VOLATILE = 64;
  
  private static final boolean DEBUGCODE = false;
  
  private static final boolean DEBUGLABELS = false;
  
  private static final boolean DEBUGSTACK = false;
  
  private static final int ExceptionTableSize = 4;
  
  private static final int FileHeaderConstant = -889275714;
  
  private static final boolean GenerateStackMap;
  
  private static final int LineNumberTableSize = 16;
  
  private static final int MIN_FIXUP_TABLE_SIZE = 40;
  
  private static final int MIN_LABEL_TABLE_SIZE = 32;
  
  private static final int MajorVersion;
  
  private static final int MinorVersion;
  
  private static final int SuperBlockStartsSize = 4;
  
  private String generatedClassName;
  
  private ObjArray itsBootstrapMethods;
  
  private int itsBootstrapMethodsLength = 0;
  
  private byte[] itsCodeBuffer = new byte[256];
  
  private int itsCodeBufferTop;
  
  private ConstantPool itsConstantPool;
  
  private ClassFileMethod itsCurrentMethod;
  
  private ExceptionTableEntry[] itsExceptionTable;
  
  private int itsExceptionTableTop;
  
  private ObjArray itsFields = new ObjArray();
  
  private long[] itsFixupTable;
  
  private int itsFixupTableTop;
  
  private short itsFlags;
  
  private ObjArray itsInterfaces = new ObjArray();
  
  private UintMap itsJumpFroms = null;
  
  private int[] itsLabelTable;
  
  private int itsLabelTableTop;
  
  private int[] itsLineNumberTable;
  
  private int itsLineNumberTableTop;
  
  private short itsMaxLocals;
  
  private short itsMaxStack;
  
  private ObjArray itsMethods = new ObjArray();
  
  private short itsSourceFileNameIndex;
  
  private short itsStackTop;
  
  private int[] itsSuperBlockStarts = null;
  
  private int itsSuperBlockStartsTop = 0;
  
  private short itsSuperClassIndex;
  
  private short itsThisClassIndex;
  
  private ObjArray itsVarDescriptors;
  
  private char[] tmpCharBuffer = new char[64];
  
  static {
    // Byte code:
    //   0: aconst_null
    //   1: astore_0
    //   2: aconst_null
    //   3: astore_1
    //   4: iconst_0
    //   5: istore_2
    //   6: iconst_0
    //   7: istore_3
    //   8: iconst_1
    //   9: istore #4
    //   11: iconst_1
    //   12: istore #5
    //   14: iconst_1
    //   15: istore #6
    //   17: iload_3
    //   18: istore #7
    //   20: iload_2
    //   21: istore #8
    //   23: ldc com/trendmicro/classfile/ClassFileWriter
    //   25: ldc 'ClassFileWriter.class'
    //   27: invokevirtual getResourceAsStream : (Ljava/lang/String;)Ljava/io/InputStream;
    //   30: astore #9
    //   32: aload #9
    //   34: astore #10
    //   36: aload #9
    //   38: ifnonnull -> 60
    //   41: aload #9
    //   43: astore_1
    //   44: iload_3
    //   45: istore #7
    //   47: aload #9
    //   49: astore_0
    //   50: iload_2
    //   51: istore #8
    //   53: ldc 'com/trendmicro/classfile/ClassFileWriter.class'
    //   55: invokestatic getSystemResourceAsStream : (Ljava/lang/String;)Ljava/io/InputStream;
    //   58: astore #10
    //   60: aload #10
    //   62: astore_1
    //   63: iload_3
    //   64: istore #7
    //   66: aload #10
    //   68: astore_0
    //   69: iload_2
    //   70: istore #8
    //   72: bipush #8
    //   74: newarray byte
    //   76: astore #9
    //   78: iconst_0
    //   79: istore #11
    //   81: iload #11
    //   83: bipush #8
    //   85: if_icmpge -> 180
    //   88: aload #10
    //   90: astore_1
    //   91: iload_3
    //   92: istore #7
    //   94: aload #10
    //   96: astore_0
    //   97: iload_2
    //   98: istore #8
    //   100: aload #10
    //   102: aload #9
    //   104: iload #11
    //   106: bipush #8
    //   108: iload #11
    //   110: isub
    //   111: invokevirtual read : ([BII)I
    //   114: istore #12
    //   116: iload #12
    //   118: iflt -> 131
    //   121: iload #11
    //   123: iload #12
    //   125: iadd
    //   126: istore #11
    //   128: goto -> 81
    //   131: aload #10
    //   133: astore_1
    //   134: iload_3
    //   135: istore #7
    //   137: aload #10
    //   139: astore_0
    //   140: iload_2
    //   141: istore #8
    //   143: new java/io/IOException
    //   146: astore #9
    //   148: aload #10
    //   150: astore_1
    //   151: iload_3
    //   152: istore #7
    //   154: aload #10
    //   156: astore_0
    //   157: iload_2
    //   158: istore #8
    //   160: aload #9
    //   162: invokespecial <init> : ()V
    //   165: aload #10
    //   167: astore_1
    //   168: iload_3
    //   169: istore #7
    //   171: aload #10
    //   173: astore_0
    //   174: iload_2
    //   175: istore #8
    //   177: aload #9
    //   179: athrow
    //   180: aload #9
    //   182: iconst_4
    //   183: baload
    //   184: bipush #8
    //   186: ishl
    //   187: aload #9
    //   189: iconst_5
    //   190: baload
    //   191: sipush #255
    //   194: iand
    //   195: ior
    //   196: istore #11
    //   198: aload #9
    //   200: bipush #6
    //   202: baload
    //   203: istore_3
    //   204: aload #9
    //   206: bipush #7
    //   208: baload
    //   209: istore_2
    //   210: iload_3
    //   211: bipush #8
    //   213: ishl
    //   214: iload_2
    //   215: sipush #255
    //   218: iand
    //   219: ior
    //   220: istore_3
    //   221: iload #11
    //   223: putstatic com/trendmicro/classfile/ClassFileWriter.MinorVersion : I
    //   226: iload_3
    //   227: putstatic com/trendmicro/classfile/ClassFileWriter.MajorVersion : I
    //   230: iload_3
    //   231: bipush #50
    //   233: if_icmplt -> 239
    //   236: goto -> 242
    //   239: iconst_0
    //   240: istore #6
    //   242: iload #6
    //   244: putstatic com/trendmicro/classfile/ClassFileWriter.GenerateStackMap : Z
    //   247: aload #10
    //   249: ifnull -> 359
    //   252: aload #10
    //   254: invokevirtual close : ()V
    //   257: goto -> 351
    //   260: astore #10
    //   262: iload #7
    //   264: putstatic com/trendmicro/classfile/ClassFileWriter.MinorVersion : I
    //   267: bipush #48
    //   269: putstatic com/trendmicro/classfile/ClassFileWriter.MajorVersion : I
    //   272: bipush #48
    //   274: bipush #50
    //   276: if_icmplt -> 286
    //   279: iload #4
    //   281: istore #6
    //   283: goto -> 289
    //   286: iconst_0
    //   287: istore #6
    //   289: iload #6
    //   291: putstatic com/trendmicro/classfile/ClassFileWriter.GenerateStackMap : Z
    //   294: aload_1
    //   295: ifnull -> 306
    //   298: aload_1
    //   299: invokevirtual close : ()V
    //   302: goto -> 306
    //   305: astore_1
    //   306: aload #10
    //   308: athrow
    //   309: astore #10
    //   311: iload #8
    //   313: putstatic com/trendmicro/classfile/ClassFileWriter.MinorVersion : I
    //   316: bipush #48
    //   318: putstatic com/trendmicro/classfile/ClassFileWriter.MajorVersion : I
    //   321: bipush #48
    //   323: bipush #50
    //   325: if_icmplt -> 335
    //   328: iload #5
    //   330: istore #6
    //   332: goto -> 338
    //   335: iconst_0
    //   336: istore #6
    //   338: iload #6
    //   340: putstatic com/trendmicro/classfile/ClassFileWriter.GenerateStackMap : Z
    //   343: aload_0
    //   344: ifnull -> 359
    //   347: aload_0
    //   348: invokevirtual close : ()V
    //   351: goto -> 359
    //   354: astore #10
    //   356: goto -> 351
    //   359: return
    // Exception table:
    //   from	to	target	type
    //   23	32	309	java/lang/Exception
    //   23	32	260	finally
    //   53	60	309	java/lang/Exception
    //   53	60	260	finally
    //   72	78	309	java/lang/Exception
    //   72	78	260	finally
    //   100	116	309	java/lang/Exception
    //   100	116	260	finally
    //   143	148	309	java/lang/Exception
    //   143	148	260	finally
    //   160	165	309	java/lang/Exception
    //   160	165	260	finally
    //   177	180	309	java/lang/Exception
    //   177	180	260	finally
    //   252	257	354	java/io/IOException
    //   298	302	305	java/io/IOException
    //   347	351	354	java/io/IOException
  }
  
  public ClassFileWriter(String paramString1, String paramString2, String paramString3) {
    this.generatedClassName = paramString1;
    ConstantPool constantPool = new ConstantPool(this);
    this.itsConstantPool = constantPool;
    this.itsThisClassIndex = constantPool.addClass(paramString1);
    this.itsSuperClassIndex = this.itsConstantPool.addClass(paramString2);
    if (paramString3 != null)
      this.itsSourceFileNameIndex = this.itsConstantPool.addUtf8(paramString3); 
    this.itsFlags = (short)33;
  }
  
  private void addLabelFixup(int paramInt1, int paramInt2) {
    if (paramInt1 < 0) {
      int i = paramInt1 & Integer.MAX_VALUE;
      if (i < this.itsLabelTableTop) {
        paramInt1 = this.itsFixupTableTop;
        long[] arrayOfLong = this.itsFixupTable;
        if (arrayOfLong == null || paramInt1 == arrayOfLong.length) {
          arrayOfLong = this.itsFixupTable;
          if (arrayOfLong == null) {
            this.itsFixupTable = new long[40];
          } else {
            long[] arrayOfLong1 = new long[arrayOfLong.length * 2];
            System.arraycopy(arrayOfLong, 0, arrayOfLong1, 0, paramInt1);
            this.itsFixupTable = arrayOfLong1;
          } 
        } 
        this.itsFixupTableTop = paramInt1 + 1;
        this.itsFixupTable[paramInt1] = i << 32L | paramInt2;
        return;
      } 
      throw new IllegalArgumentException("Bad label");
    } 
    throw new IllegalArgumentException("Bad label, no biscuit");
  }
  
  private int addReservedCodeSpace(int paramInt) {
    if (this.itsCurrentMethod != null) {
      int i = this.itsCodeBufferTop;
      int j = i + paramInt;
      byte[] arrayOfByte = this.itsCodeBuffer;
      if (j > arrayOfByte.length) {
        int k = arrayOfByte.length * 2;
        paramInt = k;
        if (j > k)
          paramInt = j; 
        arrayOfByte = new byte[paramInt];
        System.arraycopy(this.itsCodeBuffer, 0, arrayOfByte, 0, i);
        this.itsCodeBuffer = arrayOfByte;
      } 
      this.itsCodeBufferTop = j;
      return i;
    } 
    throw new IllegalArgumentException("No method to add to");
  }
  
  private void addSuperBlockStart(int paramInt) {
    if (GenerateStackMap) {
      int[] arrayOfInt1 = this.itsSuperBlockStarts;
      if (arrayOfInt1 == null) {
        this.itsSuperBlockStarts = new int[4];
      } else {
        int j = arrayOfInt1.length;
        int k = this.itsSuperBlockStartsTop;
        if (j == k) {
          int[] arrayOfInt = new int[k * 2];
          System.arraycopy(arrayOfInt1, 0, arrayOfInt, 0, k);
          this.itsSuperBlockStarts = arrayOfInt;
        } 
      } 
      int[] arrayOfInt2 = this.itsSuperBlockStarts;
      int i = this.itsSuperBlockStartsTop;
      this.itsSuperBlockStartsTop = i + 1;
      arrayOfInt2[i] = paramInt;
    } 
  }
  
  private void addToCodeBuffer(int paramInt) {
    int i = addReservedCodeSpace(1);
    this.itsCodeBuffer[i] = (byte)(byte)paramInt;
  }
  
  private void addToCodeInt16(int paramInt) {
    int i = addReservedCodeSpace(2);
    putInt16(paramInt, this.itsCodeBuffer, i);
  }
  
  private static char arrayTypeToName(int paramInt) {
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("bad operand");
      case 11:
        return 'J';
      case 10:
        return 'I';
      case 9:
        return 'S';
      case 8:
        return 'B';
      case 7:
        return 'D';
      case 6:
        return 'F';
      case 5:
        return 'C';
      case 4:
        break;
    } 
    return 'Z';
  }
  
  private static void badStack(int paramInt) {
    String str;
    if (paramInt < 0) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Stack underflow: ");
      stringBuilder.append(paramInt);
      str = stringBuilder.toString();
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Too big stack: ");
      stringBuilder.append(paramInt);
      str = stringBuilder.toString();
    } 
    throw new IllegalStateException(str);
  }
  
  private static String bytecodeStr(int paramInt) {
    return "";
  }
  
  private static String classDescriptorToInternalName(String paramString) {
    return paramString.substring(1, paramString.length() - 1);
  }
  
  public static String classNameToSignature(String paramString) {
    int i = paramString.length();
    int j = i + 1;
    char[] arrayOfChar = new char[j + 1];
    arrayOfChar[0] = (char)'L';
    arrayOfChar[j] = (char)';';
    paramString.getChars(0, i, arrayOfChar, 1);
    for (i = 1; i != j; i++) {
      if (arrayOfChar[i] == '.')
        arrayOfChar[i] = (char)'/'; 
    } 
    return new String(arrayOfChar, 0, j + 1);
  }
  
  private int[] createInitialLocals() {
    int[] arrayOfInt = new int[this.itsMaxLocals];
    int i = 0;
    if ((this.itsCurrentMethod.getFlags() & 0x8) == 0)
      if ("<init>".equals(this.itsCurrentMethod.getName())) {
        arrayOfInt[0] = 6;
        i = 0 + 1;
      } else {
        arrayOfInt[0] = TypeInfo.OBJECT(this.itsThisClassIndex);
        i = 0 + 1;
      }  
    String str = this.itsCurrentMethod.getType();
    int j = str.indexOf('(');
    int k = str.indexOf(')');
    if (j == 0 && k >= 0) {
      j++;
      StringBuilder stringBuilder = new StringBuilder();
      int m = i;
      while (j < k) {
        i = str.charAt(j);
        if (i != 70) {
          if (i != 76) {
            if (i != 83 && i != 73 && i != 74 && i != 90) {
              if (i != 91) {
                switch (i) {
                  default:
                    i = j;
                    break;
                  case 66:
                  case 67:
                  case 68:
                    stringBuilder.append(str.charAt(j));
                    i = j + 1;
                    break;
                } 
              } else {
                stringBuilder.append('[');
                j++;
                continue;
              } 
            } else {
            
            } 
          } else {
            i = str.indexOf(';', j) + 1;
            stringBuilder.append(str.substring(j, i));
          } 
          int n = TypeInfo.fromType(descriptorToInternalName(stringBuilder.toString()), this.itsConstantPool);
          j = m + 1;
          arrayOfInt[m] = n;
          if (TypeInfo.isTwoWords(n))
            j++; 
          stringBuilder.setLength(0);
          m = j;
          j = i;
        } 
      } 
      return arrayOfInt;
    } 
    throw new IllegalArgumentException("bad method type");
  }
  
  private static String descriptorToInternalName(String paramString) {
    char c = paramString.charAt(0);
    if (c != 'F')
      if (c != 'L') {
        if (c != 'S' && c != 'V' && c != 'I' && c != 'J' && c != 'Z' && c != '[') {
          StringBuilder stringBuilder;
          switch (c) {
            default:
              stringBuilder = new StringBuilder();
              stringBuilder.append("bad descriptor:");
              stringBuilder.append(paramString);
              throw new IllegalArgumentException(stringBuilder.toString());
            case 'B':
            case 'C':
            case 'D':
              break;
          } 
        } 
      } else {
        return classDescriptorToInternalName(paramString);
      }  
    return paramString;
  }
  
  private void finalizeSuperBlockStarts() {
    if (GenerateStackMap) {
      byte b;
      for (b = 0; b < this.itsExceptionTableTop; b++)
        addSuperBlockStart(getLabelPC((this.itsExceptionTable[b]).itsHandlerLabel)); 
      Arrays.sort(this.itsSuperBlockStarts, 0, this.itsSuperBlockStartsTop);
      int i = this.itsSuperBlockStarts[0];
      int j = 1;
      b = 1;
      while (b < this.itsSuperBlockStartsTop) {
        int[] arrayOfInt = this.itsSuperBlockStarts;
        int k = arrayOfInt[b];
        int m = i;
        int n = j;
        if (i != k) {
          if (j != b)
            arrayOfInt[j] = k; 
          n = j + 1;
          m = k;
        } 
        b++;
        i = m;
        j = n;
      } 
      this.itsSuperBlockStartsTop = j;
      if (this.itsSuperBlockStarts[j - 1] == this.itsCodeBufferTop)
        this.itsSuperBlockStartsTop = j - 1; 
    } 
  }
  
  private void fixLabelGotos() {
    byte[] arrayOfByte = this.itsCodeBuffer;
    byte b = 0;
    while (b < this.itsFixupTableTop) {
      long l = this.itsFixupTable[b];
      int i = (int)(l >> 32L);
      int j = (int)l;
      i = this.itsLabelTable[i];
      if (i != -1) {
        addSuperBlockStart(i);
        this.itsJumpFroms.put(i, j - 1);
        i -= j - 1;
        if ((short)i == i) {
          arrayOfByte[j] = (byte)(byte)(i >> 8);
          arrayOfByte[j + 1] = (byte)(byte)i;
          b++;
          continue;
        } 
        throw new ClassFileFormatException("Program too complex: too big jump offset");
      } 
      throw new RuntimeException("unlocated label");
    } 
    this.itsFixupTableTop = 0;
  }
  
  static String getSlashedForm(String paramString) {
    return paramString.replace('.', '/');
  }
  
  private int getWriteSize() {
    if (this.itsSourceFileNameIndex != 0)
      this.itsConstantPool.addUtf8("SourceFile"); 
    int i = 0 + 8 + this.itsConstantPool.getWriteSize() + 2 + 2 + 2 + 2 + this.itsInterfaces.size() * 2 + 2;
    int j;
    for (j = 0; j < this.itsFields.size(); j++)
      i += ((ClassFileField)this.itsFields.get(j)).getWriteSize(); 
    i += 2;
    for (j = 0; j < this.itsMethods.size(); j++)
      i += ((ClassFileMethod)this.itsMethods.get(j)).getWriteSize(); 
    i += 2;
    j = i;
    if (this.itsSourceFileNameIndex != 0)
      j = i + 2 + 4 + 2; 
    i = j;
    if (this.itsBootstrapMethods != null)
      i = j + 2 + 4 + 2 + this.itsBootstrapMethodsLength; 
    return i;
  }
  
  private static int opcodeCount(int paramInt) {
    if (paramInt != 254 && paramInt != 255) {
      StringBuilder stringBuilder;
      switch (paramInt) {
        default:
          switch (paramInt) {
            default:
              stringBuilder = new StringBuilder();
              stringBuilder.append("Bad opcode: ");
              stringBuilder.append(paramInt);
              throw new IllegalArgumentException(stringBuilder.toString());
            case 197:
              return 2;
            case 187:
            case 188:
            case 189:
            case 192:
            case 193:
            case 198:
            case 199:
            case 200:
            case 201:
              return 1;
            case 190:
            case 191:
            case 194:
            case 195:
            case 196:
            case 202:
              break;
          } 
          break;
        case 170:
        case 171:
          return -1;
        case 132:
        case 16:
        case 17:
        case 18:
        case 19:
        case 20:
        case 21:
        case 22:
        case 23:
        case 24:
        case 25:
        case 54:
        case 55:
        case 56:
        case 57:
        case 58:
        case 153:
        case 154:
        case 155:
        case 156:
        case 157:
        case 158:
        case 159:
        case 160:
        case 161:
        case 162:
        case 163:
        case 164:
        case 165:
        case 166:
        case 167:
        case 168:
        case 169:
        case 178:
        case 179:
        case 180:
        case 181:
        case 182:
        case 183:
        case 184:
        case 185:
        
        case 0:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
        case 9:
        case 10:
        case 11:
        case 12:
        case 13:
        case 14:
        case 15:
        case 26:
        case 27:
        case 28:
        case 29:
        case 30:
        case 31:
        case 32:
        case 33:
        case 34:
        case 35:
        case 36:
        case 37:
        case 38:
        case 39:
        case 40:
        case 41:
        case 42:
        case 43:
        case 44:
        case 45:
        case 46:
        case 47:
        case 48:
        case 49:
        case 50:
        case 51:
        case 52:
        case 53:
        case 59:
        case 60:
        case 61:
        case 62:
        case 63:
        case 64:
        case 65:
        case 66:
        case 67:
        case 68:
        case 69:
        case 70:
        case 71:
        case 72:
        case 73:
        case 74:
        case 75:
        case 76:
        case 77:
        case 78:
        case 79:
        case 80:
        case 81:
        case 82:
        case 83:
        case 84:
        case 85:
        case 86:
        case 87:
        case 88:
        case 89:
        case 90:
        case 91:
        case 92:
        case 93:
        case 94:
        case 95:
        case 96:
        case 97:
        case 98:
        case 99:
        case 100:
        case 101:
        case 102:
        case 103:
        case 104:
        case 105:
        case 106:
        case 107:
        case 108:
        case 109:
        case 110:
        case 111:
        case 112:
        case 113:
        case 114:
        case 115:
        case 116:
        case 117:
        case 118:
        case 119:
        case 120:
        case 121:
        case 122:
        case 123:
        case 124:
        case 125:
        case 126:
        case 127:
        case 128:
        case 129:
        case 130:
        case 131:
        case 133:
        case 134:
        case 135:
        case 136:
        case 137:
        case 138:
        case 139:
        case 140:
        case 141:
        case 142:
        case 143:
        case 144:
        case 145:
        case 146:
        case 147:
        case 148:
        case 149:
        case 150:
        case 151:
        case 152:
        case 172:
        case 173:
        case 174:
        case 175:
        case 176:
        case 177:
          break;
      } 
    } 
    return 0;
  }
  
  private static int opcodeLength(int paramInt, boolean paramBoolean) {
    if (paramInt != 254 && paramInt != 255) {
      StringBuilder stringBuilder;
      byte b1 = 5;
      byte b2 = 2;
      switch (paramInt) {
        default:
          switch (paramInt) {
            default:
              stringBuilder = new StringBuilder();
              stringBuilder.append("Bad opcode: ");
              stringBuilder.append(paramInt);
              throw new IllegalArgumentException(stringBuilder.toString());
            case 197:
              return 4;
            case 185:
            case 186:
            case 200:
            case 201:
              return 5;
            case 178:
            case 179:
            case 180:
            case 181:
            case 182:
            case 183:
            case 184:
            case 187:
            case 189:
            case 192:
            case 193:
            case 198:
            case 199:
              return 3;
            case 188:
              return 2;
            case 172:
            case 173:
            case 174:
            case 175:
            case 176:
            case 177:
            case 190:
            case 191:
            case 194:
            case 195:
            case 196:
            case 202:
              break;
          } 
          break;
        case 132:
          if (paramBoolean) {
            paramInt = b1;
          } else {
            paramInt = 3;
          } 
          return paramInt;
        case 21:
        case 22:
        case 23:
        case 24:
        case 25:
        case 54:
        case 55:
        case 56:
        case 57:
        case 58:
        case 169:
          paramInt = b2;
          if (paramBoolean)
            paramInt = 3; 
          return paramInt;
        case 17:
        case 19:
        case 20:
        case 153:
        case 154:
        case 155:
        case 156:
        case 157:
        case 158:
        case 159:
        case 160:
        case 161:
        case 162:
        case 163:
        case 164:
        case 165:
        case 166:
        case 167:
        case 168:
        case 16:
        case 18:
        
        case 0:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
        case 9:
        case 10:
        case 11:
        case 12:
        case 13:
        case 14:
        case 15:
        case 26:
        case 27:
        case 28:
        case 29:
        case 30:
        case 31:
        case 32:
        case 33:
        case 34:
        case 35:
        case 36:
        case 37:
        case 38:
        case 39:
        case 40:
        case 41:
        case 42:
        case 43:
        case 44:
        case 45:
        case 46:
        case 47:
        case 48:
        case 49:
        case 50:
        case 51:
        case 52:
        case 53:
        case 59:
        case 60:
        case 61:
        case 62:
        case 63:
        case 64:
        case 65:
        case 66:
        case 67:
        case 68:
        case 69:
        case 70:
        case 71:
        case 72:
        case 73:
        case 74:
        case 75:
        case 76:
        case 77:
        case 78:
        case 79:
        case 80:
        case 81:
        case 82:
        case 83:
        case 84:
        case 85:
        case 86:
        case 87:
        case 88:
        case 89:
        case 90:
        case 91:
        case 92:
        case 93:
        case 94:
        case 95:
        case 96:
        case 97:
        case 98:
        case 99:
        case 100:
        case 101:
        case 102:
        case 103:
        case 104:
        case 105:
        case 106:
        case 107:
        case 108:
        case 109:
        case 110:
        case 111:
        case 112:
        case 113:
        case 114:
        case 115:
        case 116:
        case 117:
        case 118:
        case 119:
        case 120:
        case 121:
        case 122:
        case 123:
        case 124:
        case 125:
        case 126:
        case 127:
        case 128:
        case 129:
        case 130:
        case 131:
        case 133:
        case 134:
        case 135:
        case 136:
        case 137:
        case 138:
        case 139:
        case 140:
        case 141:
        case 142:
        case 143:
        case 144:
        case 145:
        case 146:
        case 147:
        case 148:
        case 149:
        case 150:
        case 151:
        case 152:
          break;
      } 
    } 
    return 1;
  }
  
  static int putInt16(int paramInt1, byte[] paramArrayOfbyte, int paramInt2) {
    paramArrayOfbyte[paramInt2 + 0] = (byte)(byte)(paramInt1 >>> 8);
    paramArrayOfbyte[paramInt2 + 1] = (byte)(byte)paramInt1;
    return paramInt2 + 2;
  }
  
  static int putInt32(int paramInt1, byte[] paramArrayOfbyte, int paramInt2) {
    paramArrayOfbyte[paramInt2 + 0] = (byte)(byte)(paramInt1 >>> 24);
    paramArrayOfbyte[paramInt2 + 1] = (byte)(byte)(paramInt1 >>> 16);
    paramArrayOfbyte[paramInt2 + 2] = (byte)(byte)(paramInt1 >>> 8);
    paramArrayOfbyte[paramInt2 + 3] = (byte)(byte)paramInt1;
    return paramInt2 + 4;
  }
  
  static int putInt64(long paramLong, byte[] paramArrayOfbyte, int paramInt) {
    paramInt = putInt32((int)(paramLong >>> 32L), paramArrayOfbyte, paramInt);
    return putInt32((int)paramLong, paramArrayOfbyte, paramInt);
  }
  
  private static int sizeOfParameters(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual length : ()I
    //   4: istore_1
    //   5: aload_0
    //   6: bipush #41
    //   8: invokevirtual lastIndexOf : (I)I
    //   11: istore_2
    //   12: iconst_3
    //   13: iload_1
    //   14: if_icmpgt -> 562
    //   17: aload_0
    //   18: iconst_0
    //   19: invokevirtual charAt : (I)C
    //   22: bipush #40
    //   24: if_icmpne -> 562
    //   27: iconst_1
    //   28: iload_2
    //   29: if_icmpgt -> 562
    //   32: iload_2
    //   33: iconst_1
    //   34: iadd
    //   35: iload_1
    //   36: if_icmpge -> 562
    //   39: iconst_1
    //   40: istore_3
    //   41: iconst_1
    //   42: istore #4
    //   44: iconst_0
    //   45: istore_1
    //   46: iconst_0
    //   47: istore #5
    //   49: iload_3
    //   50: istore #6
    //   52: iload_1
    //   53: istore #7
    //   55: iload #5
    //   57: istore #8
    //   59: iload #4
    //   61: iload_2
    //   62: if_icmpeq -> 397
    //   65: aload_0
    //   66: iload #4
    //   68: invokevirtual charAt : (I)C
    //   71: istore #7
    //   73: iload_1
    //   74: istore #6
    //   76: iload #7
    //   78: bipush #70
    //   80: if_icmpeq -> 383
    //   83: iload #4
    //   85: istore #6
    //   87: iload #7
    //   89: bipush #76
    //   91: if_icmpeq -> 321
    //   94: iload_1
    //   95: istore #6
    //   97: iload #7
    //   99: bipush #83
    //   101: if_icmpeq -> 383
    //   104: iload_1
    //   105: istore #6
    //   107: iload #7
    //   109: bipush #73
    //   111: if_icmpeq -> 383
    //   114: iload #7
    //   116: bipush #74
    //   118: if_icmpeq -> 313
    //   121: iload_1
    //   122: istore #6
    //   124: iload #7
    //   126: bipush #90
    //   128: if_icmpeq -> 383
    //   131: iload #7
    //   133: bipush #91
    //   135: if_icmpeq -> 181
    //   138: iload_1
    //   139: istore #6
    //   141: iload #7
    //   143: tableswitch default -> 168, 66 -> 383, 67 -> 383, 68 -> 313
    //   168: iconst_0
    //   169: istore #6
    //   171: iload_1
    //   172: istore #7
    //   174: iload #5
    //   176: istore #8
    //   178: goto -> 397
    //   181: iinc #4, 1
    //   184: aload_0
    //   185: iload #4
    //   187: invokevirtual charAt : (I)C
    //   190: istore #7
    //   192: iload #7
    //   194: bipush #91
    //   196: if_icmpne -> 213
    //   199: iinc #4, 1
    //   202: aload_0
    //   203: iload #4
    //   205: invokevirtual charAt : (I)C
    //   208: istore #7
    //   210: goto -> 192
    //   213: iload #7
    //   215: bipush #70
    //   217: if_icmpeq -> 301
    //   220: iload #4
    //   222: istore #6
    //   224: iload #7
    //   226: bipush #76
    //   228: if_icmpeq -> 321
    //   231: iload #7
    //   233: bipush #83
    //   235: if_icmpeq -> 301
    //   238: iload #7
    //   240: bipush #90
    //   242: if_icmpeq -> 301
    //   245: iload #7
    //   247: bipush #73
    //   249: if_icmpeq -> 301
    //   252: iload #7
    //   254: bipush #74
    //   256: if_icmpeq -> 301
    //   259: iload #7
    //   261: tableswitch default -> 288, 66 -> 301, 67 -> 301, 68 -> 301
    //   288: iconst_0
    //   289: istore #6
    //   291: iload_1
    //   292: istore #7
    //   294: iload #5
    //   296: istore #8
    //   298: goto -> 397
    //   301: iinc #1, -1
    //   304: iinc #5, 1
    //   307: iinc #4, 1
    //   310: goto -> 49
    //   313: iload_1
    //   314: iconst_1
    //   315: isub
    //   316: istore #6
    //   318: goto -> 383
    //   321: iinc #1, -1
    //   324: iinc #5, 1
    //   327: iload #6
    //   329: iconst_1
    //   330: iadd
    //   331: istore #4
    //   333: aload_0
    //   334: bipush #59
    //   336: iload #4
    //   338: invokevirtual indexOf : (II)I
    //   341: istore #6
    //   343: iload #4
    //   345: iconst_1
    //   346: iadd
    //   347: iload #6
    //   349: if_icmpgt -> 370
    //   352: iload #6
    //   354: iload_2
    //   355: if_icmplt -> 361
    //   358: goto -> 370
    //   361: iload #6
    //   363: iconst_1
    //   364: iadd
    //   365: istore #4
    //   367: goto -> 49
    //   370: iconst_0
    //   371: istore #6
    //   373: iload_1
    //   374: istore #7
    //   376: iload #5
    //   378: istore #8
    //   380: goto -> 397
    //   383: iload #6
    //   385: iconst_1
    //   386: isub
    //   387: istore_1
    //   388: iinc #5, 1
    //   391: iinc #4, 1
    //   394: goto -> 49
    //   397: iload #6
    //   399: ifeq -> 562
    //   402: aload_0
    //   403: iload_2
    //   404: iconst_1
    //   405: iadd
    //   406: invokevirtual charAt : (I)C
    //   409: istore #4
    //   411: iload #7
    //   413: istore_1
    //   414: iload #4
    //   416: bipush #70
    //   418: if_icmpeq -> 538
    //   421: iload #7
    //   423: istore_1
    //   424: iload #4
    //   426: bipush #76
    //   428: if_icmpeq -> 538
    //   431: iload #7
    //   433: istore_1
    //   434: iload #4
    //   436: bipush #83
    //   438: if_icmpeq -> 538
    //   441: iload #6
    //   443: istore #5
    //   445: iload #7
    //   447: istore_1
    //   448: iload #4
    //   450: bipush #86
    //   452: if_icmpeq -> 545
    //   455: iload #7
    //   457: istore_1
    //   458: iload #4
    //   460: bipush #73
    //   462: if_icmpeq -> 538
    //   465: iload #4
    //   467: bipush #74
    //   469: if_icmpeq -> 533
    //   472: iload #7
    //   474: istore_1
    //   475: iload #4
    //   477: bipush #90
    //   479: if_icmpeq -> 538
    //   482: iload #7
    //   484: istore_1
    //   485: iload #4
    //   487: bipush #91
    //   489: if_icmpeq -> 538
    //   492: iload #7
    //   494: istore_1
    //   495: iload #4
    //   497: tableswitch default -> 524, 66 -> 538, 67 -> 538, 68 -> 533
    //   524: iconst_0
    //   525: istore #5
    //   527: iload #7
    //   529: istore_1
    //   530: goto -> 545
    //   533: iload #7
    //   535: iconst_1
    //   536: iadd
    //   537: istore_1
    //   538: iinc #1, 1
    //   541: iload #6
    //   543: istore #5
    //   545: iload #5
    //   547: ifeq -> 562
    //   550: iload #8
    //   552: bipush #16
    //   554: ishl
    //   555: ldc_w 65535
    //   558: iload_1
    //   559: iand
    //   560: ior
    //   561: ireturn
    //   562: new java/lang/StringBuilder
    //   565: dup
    //   566: invokespecial <init> : ()V
    //   569: astore #9
    //   571: aload #9
    //   573: ldc_w 'Bad parameter signature: '
    //   576: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   579: pop
    //   580: aload #9
    //   582: aload_0
    //   583: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   586: pop
    //   587: new java/lang/IllegalArgumentException
    //   590: dup
    //   591: aload #9
    //   593: invokevirtual toString : ()Ljava/lang/String;
    //   596: invokespecial <init> : (Ljava/lang/String;)V
    //   599: athrow
  }
  
  private static int stackChange(int paramInt) {
    if (paramInt != 254 && paramInt != 255) {
      StringBuilder stringBuilder;
      switch (paramInt) {
        default:
          stringBuilder = new StringBuilder();
          stringBuilder.append("Bad opcode: ");
          stringBuilder.append(paramInt);
          throw new IllegalArgumentException(stringBuilder.toString());
        case 80:
        case 82:
          return -4;
        case 79:
        case 81:
        case 83:
        case 84:
        case 85:
        case 86:
        case 148:
        case 151:
        case 152:
          return -3;
        case 55:
        case 57:
        case 63:
        case 64:
        case 65:
        case 66:
        case 71:
        case 72:
        case 73:
        case 74:
        case 88:
        case 97:
        case 99:
        case 101:
        case 103:
        case 105:
        case 107:
        case 109:
        case 111:
        case 113:
        case 115:
        case 127:
        case 129:
        case 131:
        case 159:
        case 160:
        case 161:
        case 162:
        case 163:
        case 164:
        case 165:
        case 166:
        case 173:
        case 175:
          return -2;
        case 46:
        case 48:
        case 50:
        case 51:
        case 52:
        case 53:
        case 54:
        case 56:
        case 58:
        case 59:
        case 60:
        case 61:
        case 62:
        case 67:
        case 68:
        case 69:
        case 70:
        case 75:
        case 76:
        case 77:
        case 78:
        case 87:
        case 96:
        case 98:
        case 100:
        case 102:
        case 104:
        case 106:
        case 108:
        case 110:
        case 112:
        case 114:
        case 120:
        case 121:
        case 122:
        case 123:
        case 124:
        case 125:
        case 126:
        case 128:
        case 130:
        case 136:
        case 137:
        case 142:
        case 144:
        case 149:
        case 150:
        case 153:
        case 154:
        case 155:
        case 156:
        case 157:
        case 158:
        case 170:
        case 171:
        case 172:
        case 174:
        case 176:
        case 180:
        case 181:
        case 182:
        case 183:
        case 185:
        case 191:
        case 194:
        case 195:
        case 198:
        case 199:
          return -1;
        case 9:
        case 10:
        case 14:
        case 15:
        case 20:
        case 22:
        case 24:
        case 30:
        case 31:
        case 32:
        case 33:
        case 38:
        case 39:
        case 40:
        case 41:
        case 92:
        case 93:
        case 94:
          return 2;
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
        case 11:
        case 12:
        case 13:
        case 16:
        case 17:
        case 18:
        case 19:
        case 21:
        case 23:
        case 25:
        case 26:
        case 27:
        case 28:
        case 29:
        case 34:
        case 35:
        case 36:
        case 37:
        case 42:
        case 43:
        case 44:
        case 45:
        case 89:
        case 90:
        case 91:
        case 133:
        case 135:
        case 140:
        case 141:
        case 168:
        case 187:
        case 197:
        case 201:
          return 1;
        case 0:
        case 47:
        case 49:
        case 95:
        case 116:
        case 117:
        case 118:
        case 119:
        case 132:
        case 134:
        case 138:
        case 139:
        case 143:
        case 145:
        case 146:
        case 147:
        case 167:
        case 169:
        case 177:
        case 178:
        case 179:
        case 184:
        case 186:
        case 188:
        case 189:
        case 190:
        case 192:
        case 193:
        case 196:
        case 200:
        case 202:
          break;
      } 
    } 
    return 0;
  }
  
  private void xop(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt3 != 0) {
      if (paramInt3 != 1) {
        if (paramInt3 != 2) {
          if (paramInt3 != 3) {
            add(paramInt2, paramInt3);
          } else {
            add(paramInt1 + 3);
          } 
        } else {
          add(paramInt1 + 2);
        } 
      } else {
        add(paramInt1 + 1);
      } 
    } else {
      add(paramInt1);
    } 
  }
  
  public int acquireLabel() {
    int i = this.itsLabelTableTop;
    int[] arrayOfInt = this.itsLabelTable;
    if (arrayOfInt == null || i == arrayOfInt.length) {
      arrayOfInt = this.itsLabelTable;
      if (arrayOfInt == null) {
        this.itsLabelTable = new int[32];
      } else {
        int[] arrayOfInt1 = new int[arrayOfInt.length * 2];
        System.arraycopy(arrayOfInt, 0, arrayOfInt1, 0, i);
        this.itsLabelTable = arrayOfInt1;
      } 
    } 
    this.itsLabelTableTop = i + 1;
    this.itsLabelTable[i] = -1;
    return Integer.MIN_VALUE | i;
  }
  
  public void add(int paramInt) {
    if (opcodeCount(paramInt) == 0) {
      int i = this.itsStackTop + stackChange(paramInt);
      if (i < 0 || 32767 < i)
        badStack(i); 
      addToCodeBuffer(paramInt);
      this.itsStackTop = (short)(short)i;
      if (i > this.itsMaxStack)
        this.itsMaxStack = (short)(short)i; 
      if (paramInt == 191)
        addSuperBlockStart(this.itsCodeBufferTop); 
      return;
    } 
    throw new IllegalArgumentException("Unexpected operands");
  }
  
  public void add(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: getfield itsStackTop : S
    //   4: iload_1
    //   5: invokestatic stackChange : (I)I
    //   8: iadd
    //   9: istore_3
    //   10: iload_3
    //   11: iflt -> 21
    //   14: sipush #32767
    //   17: iload_3
    //   18: if_icmpge -> 25
    //   21: iload_3
    //   22: invokestatic badStack : (I)V
    //   25: iload_1
    //   26: sipush #180
    //   29: if_icmpeq -> 643
    //   32: iload_1
    //   33: sipush #181
    //   36: if_icmpeq -> 643
    //   39: iload_1
    //   40: sipush #188
    //   43: if_icmpeq -> 608
    //   46: iload_1
    //   47: sipush #198
    //   50: if_icmpeq -> 468
    //   53: iload_1
    //   54: sipush #199
    //   57: if_icmpeq -> 468
    //   60: iload_1
    //   61: tableswitch default -> 116, 16 -> 437, 17 -> 407, 18 -> 322, 19 -> 322, 20 -> 322, 21 -> 260, 22 -> 260, 23 -> 260, 24 -> 260, 25 -> 260
    //   116: iload_1
    //   117: tableswitch default -> 152, 54 -> 260, 55 -> 260, 56 -> 260, 57 -> 260, 58 -> 260
    //   152: iload_1
    //   153: tableswitch default -> 236, 153 -> 468, 154 -> 468, 155 -> 468, 156 -> 468, 157 -> 468, 158 -> 468, 159 -> 468, 160 -> 468, 161 -> 468, 162 -> 468, 163 -> 468, 164 -> 468, 165 -> 468, 166 -> 468, 167 -> 247, 168 -> 468, 169 -> 260
    //   236: new java/lang/IllegalArgumentException
    //   239: dup
    //   240: ldc_w 'Unexpected opcode for 1 operand'
    //   243: invokespecial <init> : (Ljava/lang/String;)V
    //   246: athrow
    //   247: aload_0
    //   248: aload_0
    //   249: getfield itsCodeBufferTop : I
    //   252: iconst_3
    //   253: iadd
    //   254: invokespecial addSuperBlockStart : (I)V
    //   257: goto -> 468
    //   260: iload_2
    //   261: iflt -> 311
    //   264: ldc_w 65536
    //   267: iload_2
    //   268: if_icmple -> 311
    //   271: iload_2
    //   272: sipush #256
    //   275: if_icmplt -> 298
    //   278: aload_0
    //   279: sipush #196
    //   282: invokespecial addToCodeBuffer : (I)V
    //   285: aload_0
    //   286: iload_1
    //   287: invokespecial addToCodeBuffer : (I)V
    //   290: aload_0
    //   291: iload_2
    //   292: invokespecial addToCodeInt16 : (I)V
    //   295: goto -> 664
    //   298: aload_0
    //   299: iload_1
    //   300: invokespecial addToCodeBuffer : (I)V
    //   303: aload_0
    //   304: iload_2
    //   305: invokespecial addToCodeBuffer : (I)V
    //   308: goto -> 664
    //   311: new com/trendmicro/classfile/ClassFileWriter$ClassFileFormatException
    //   314: dup
    //   315: ldc_w 'out of range variable'
    //   318: invokespecial <init> : (Ljava/lang/String;)V
    //   321: athrow
    //   322: iload_2
    //   323: iflt -> 396
    //   326: iload_2
    //   327: ldc_w 65536
    //   330: if_icmpge -> 396
    //   333: iload_2
    //   334: sipush #256
    //   337: if_icmpge -> 368
    //   340: iload_1
    //   341: bipush #19
    //   343: if_icmpeq -> 368
    //   346: iload_1
    //   347: bipush #20
    //   349: if_icmpne -> 355
    //   352: goto -> 368
    //   355: aload_0
    //   356: iload_1
    //   357: invokespecial addToCodeBuffer : (I)V
    //   360: aload_0
    //   361: iload_2
    //   362: invokespecial addToCodeBuffer : (I)V
    //   365: goto -> 664
    //   368: iload_1
    //   369: bipush #18
    //   371: if_icmpne -> 383
    //   374: aload_0
    //   375: bipush #19
    //   377: invokespecial addToCodeBuffer : (I)V
    //   380: goto -> 388
    //   383: aload_0
    //   384: iload_1
    //   385: invokespecial addToCodeBuffer : (I)V
    //   388: aload_0
    //   389: iload_2
    //   390: invokespecial addToCodeInt16 : (I)V
    //   393: goto -> 664
    //   396: new com/trendmicro/classfile/ClassFileWriter$ClassFileFormatException
    //   399: dup
    //   400: ldc_w 'out of range index'
    //   403: invokespecial <init> : (Ljava/lang/String;)V
    //   406: athrow
    //   407: iload_2
    //   408: i2s
    //   409: iload_2
    //   410: if_icmpne -> 426
    //   413: aload_0
    //   414: iload_1
    //   415: invokespecial addToCodeBuffer : (I)V
    //   418: aload_0
    //   419: iload_2
    //   420: invokespecial addToCodeInt16 : (I)V
    //   423: goto -> 664
    //   426: new java/lang/IllegalArgumentException
    //   429: dup
    //   430: ldc_w 'out of range short'
    //   433: invokespecial <init> : (Ljava/lang/String;)V
    //   436: athrow
    //   437: iload_2
    //   438: i2b
    //   439: iload_2
    //   440: if_icmpne -> 457
    //   443: aload_0
    //   444: iload_1
    //   445: invokespecial addToCodeBuffer : (I)V
    //   448: aload_0
    //   449: iload_2
    //   450: i2b
    //   451: invokespecial addToCodeBuffer : (I)V
    //   454: goto -> 664
    //   457: new java/lang/IllegalArgumentException
    //   460: dup
    //   461: ldc_w 'out of range byte'
    //   464: invokespecial <init> : (Ljava/lang/String;)V
    //   467: athrow
    //   468: iload_2
    //   469: ldc_w -2147483648
    //   472: iand
    //   473: ldc_w -2147483648
    //   476: if_icmpeq -> 504
    //   479: iload_2
    //   480: iflt -> 493
    //   483: iload_2
    //   484: ldc_w 65535
    //   487: if_icmpgt -> 493
    //   490: goto -> 504
    //   493: new java/lang/IllegalArgumentException
    //   496: dup
    //   497: ldc_w 'Bad label for branch'
    //   500: invokespecial <init> : (Ljava/lang/String;)V
    //   503: athrow
    //   504: aload_0
    //   505: getfield itsCodeBufferTop : I
    //   508: istore #4
    //   510: aload_0
    //   511: iload_1
    //   512: invokespecial addToCodeBuffer : (I)V
    //   515: iload_2
    //   516: ldc_w -2147483648
    //   519: iand
    //   520: ldc_w -2147483648
    //   523: if_icmpeq -> 554
    //   526: aload_0
    //   527: iload_2
    //   528: invokespecial addToCodeInt16 : (I)V
    //   531: iload_2
    //   532: iload #4
    //   534: iadd
    //   535: istore_1
    //   536: aload_0
    //   537: iload_1
    //   538: invokespecial addSuperBlockStart : (I)V
    //   541: aload_0
    //   542: getfield itsJumpFroms : Lcom/trendmicro/hippo/UintMap;
    //   545: iload_1
    //   546: iload #4
    //   548: invokevirtual put : (II)V
    //   551: goto -> 605
    //   554: aload_0
    //   555: iload_2
    //   556: invokevirtual getLabelPC : (I)I
    //   559: istore_1
    //   560: iload_1
    //   561: iconst_m1
    //   562: if_icmpeq -> 591
    //   565: aload_0
    //   566: iload_1
    //   567: iload #4
    //   569: isub
    //   570: invokespecial addToCodeInt16 : (I)V
    //   573: aload_0
    //   574: iload_1
    //   575: invokespecial addSuperBlockStart : (I)V
    //   578: aload_0
    //   579: getfield itsJumpFroms : Lcom/trendmicro/hippo/UintMap;
    //   582: iload_1
    //   583: iload #4
    //   585: invokevirtual put : (II)V
    //   588: goto -> 605
    //   591: aload_0
    //   592: iload_2
    //   593: iload #4
    //   595: iconst_1
    //   596: iadd
    //   597: invokespecial addLabelFixup : (II)V
    //   600: aload_0
    //   601: iconst_0
    //   602: invokespecial addToCodeInt16 : (I)V
    //   605: goto -> 664
    //   608: iload_2
    //   609: iflt -> 632
    //   612: iload_2
    //   613: sipush #256
    //   616: if_icmpge -> 632
    //   619: aload_0
    //   620: iload_1
    //   621: invokespecial addToCodeBuffer : (I)V
    //   624: aload_0
    //   625: iload_2
    //   626: invokespecial addToCodeBuffer : (I)V
    //   629: goto -> 664
    //   632: new java/lang/IllegalArgumentException
    //   635: dup
    //   636: ldc_w 'out of range index'
    //   639: invokespecial <init> : (Ljava/lang/String;)V
    //   642: athrow
    //   643: iload_2
    //   644: iflt -> 687
    //   647: iload_2
    //   648: ldc_w 65536
    //   651: if_icmpge -> 687
    //   654: aload_0
    //   655: iload_1
    //   656: invokespecial addToCodeBuffer : (I)V
    //   659: aload_0
    //   660: iload_2
    //   661: invokespecial addToCodeInt16 : (I)V
    //   664: aload_0
    //   665: iload_3
    //   666: i2s
    //   667: i2s
    //   668: putfield itsStackTop : S
    //   671: iload_3
    //   672: aload_0
    //   673: getfield itsMaxStack : S
    //   676: if_icmple -> 686
    //   679: aload_0
    //   680: iload_3
    //   681: i2s
    //   682: i2s
    //   683: putfield itsMaxStack : S
    //   686: return
    //   687: new java/lang/IllegalArgumentException
    //   690: dup
    //   691: ldc_w 'out of range field'
    //   694: invokespecial <init> : (Ljava/lang/String;)V
    //   697: athrow
  }
  
  public void add(int paramInt1, int paramInt2, int paramInt3) {
    int i = this.itsStackTop + stackChange(paramInt1);
    if (i < 0 || 32767 < i)
      badStack(i); 
    if (paramInt1 == 132) {
      if (paramInt2 >= 0 && 65536 > paramInt2) {
        if (paramInt3 >= 0 && 65536 > paramInt3) {
          if (paramInt2 > 255 || paramInt3 < -128 || paramInt3 > 127) {
            addToCodeBuffer(196);
            addToCodeBuffer(132);
            addToCodeInt16(paramInt2);
            addToCodeInt16(paramInt3);
          } else {
            addToCodeBuffer(132);
            addToCodeBuffer(paramInt2);
            addToCodeBuffer(paramInt3);
          } 
        } else {
          throw new ClassFileFormatException("out of range increment");
        } 
      } else {
        throw new ClassFileFormatException("out of range variable");
      } 
    } else if (paramInt1 == 197) {
      if (paramInt2 >= 0 && paramInt2 < 65536) {
        if (paramInt3 >= 0 && paramInt3 < 256) {
          addToCodeBuffer(197);
          addToCodeInt16(paramInt2);
          addToCodeBuffer(paramInt3);
        } else {
          throw new IllegalArgumentException("out of range dimensions");
        } 
      } else {
        throw new IllegalArgumentException("out of range index");
      } 
    } else {
      throw new IllegalArgumentException("Unexpected opcode for 2 operands");
    } 
    this.itsStackTop = (short)(short)i;
    if (i > this.itsMaxStack)
      this.itsMaxStack = (short)(short)i; 
  }
  
  public void add(int paramInt, String paramString) {
    int i = this.itsStackTop + stackChange(paramInt);
    if (i < 0 || 32767 < i)
      badStack(i); 
    if (paramInt == 187 || paramInt == 189 || paramInt == 192 || paramInt == 193) {
      short s = this.itsConstantPool.addClass(paramString);
      addToCodeBuffer(paramInt);
      addToCodeInt16(s);
      this.itsStackTop = (short)(short)i;
      if (i > this.itsMaxStack)
        this.itsMaxStack = (short)(short)i; 
      return;
    } 
    throw new IllegalArgumentException("bad opcode for class reference");
  }
  
  public void add(int paramInt, String paramString1, String paramString2, String paramString3) {
    int j;
    int i = this.itsStackTop + stackChange(paramInt);
    char c = paramString3.charAt(0);
    if (c == 'J' || c == 'D') {
      c = '\002';
    } else {
      c = '\001';
    } 
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("bad opcode for field reference");
      case 179:
      case 181:
        j = i - c;
        break;
      case 178:
      case 180:
        j = i + j;
        break;
    } 
    if (j < 0 || 32767 < j)
      badStack(j); 
    i = this.itsConstantPool.addFieldRef(paramString1, paramString2, paramString3);
    addToCodeBuffer(paramInt);
    addToCodeInt16(i);
    this.itsStackTop = (short)(short)j;
    if (j > this.itsMaxStack)
      this.itsMaxStack = (short)(short)j; 
  }
  
  public void addALoad(int paramInt) {
    xop(42, 25, paramInt);
  }
  
  public void addAStore(int paramInt) {
    xop(75, 58, paramInt);
  }
  
  public void addDLoad(int paramInt) {
    xop(38, 24, paramInt);
  }
  
  public void addDStore(int paramInt) {
    xop(71, 57, paramInt);
  }
  
  public void addExceptionHandler(int paramInt1, int paramInt2, int paramInt3, String paramString) {
    if ((paramInt1 & Integer.MIN_VALUE) == Integer.MIN_VALUE) {
      if ((paramInt2 & Integer.MIN_VALUE) == Integer.MIN_VALUE) {
        if ((paramInt3 & Integer.MIN_VALUE) == Integer.MIN_VALUE) {
          short s;
          if (paramString == null) {
            boolean bool = false;
            s = bool;
          } else {
            short s1 = this.itsConstantPool.addClass(paramString);
            s = s1;
          } 
          ExceptionTableEntry exceptionTableEntry = new ExceptionTableEntry(paramInt1, paramInt2, paramInt3, s);
          paramInt1 = this.itsExceptionTableTop;
          if (paramInt1 == 0) {
            this.itsExceptionTable = new ExceptionTableEntry[4];
          } else {
            ExceptionTableEntry[] arrayOfExceptionTableEntry = this.itsExceptionTable;
            if (paramInt1 == arrayOfExceptionTableEntry.length) {
              ExceptionTableEntry[] arrayOfExceptionTableEntry1 = new ExceptionTableEntry[paramInt1 * 2];
              System.arraycopy(arrayOfExceptionTableEntry, 0, arrayOfExceptionTableEntry1, 0, paramInt1);
              this.itsExceptionTable = arrayOfExceptionTableEntry1;
            } 
          } 
          this.itsExceptionTable[paramInt1] = exceptionTableEntry;
          this.itsExceptionTableTop = paramInt1 + 1;
          return;
        } 
        throw new IllegalArgumentException("Bad handlerLabel");
      } 
      throw new IllegalArgumentException("Bad endLabel");
    } 
    throw new IllegalArgumentException("Bad startLabel");
  }
  
  public void addFLoad(int paramInt) {
    xop(34, 23, paramInt);
  }
  
  public void addFStore(int paramInt) {
    xop(67, 56, paramInt);
  }
  
  public void addField(String paramString1, String paramString2, short paramShort) {
    short s1 = this.itsConstantPool.addUtf8(paramString1);
    short s2 = this.itsConstantPool.addUtf8(paramString2);
    this.itsFields.add(new ClassFileField(s1, s2, paramShort));
  }
  
  public void addField(String paramString1, String paramString2, short paramShort, double paramDouble) {
    ClassFileField classFileField = new ClassFileField(this.itsConstantPool.addUtf8(paramString1), this.itsConstantPool.addUtf8(paramString2), paramShort);
    classFileField.setAttributes(this.itsConstantPool.addUtf8("ConstantValue"), (short)0, (short)2, this.itsConstantPool.addConstant(paramDouble));
    this.itsFields.add(classFileField);
  }
  
  public void addField(String paramString1, String paramString2, short paramShort, int paramInt) {
    ClassFileField classFileField = new ClassFileField(this.itsConstantPool.addUtf8(paramString1), this.itsConstantPool.addUtf8(paramString2), paramShort);
    classFileField.setAttributes(this.itsConstantPool.addUtf8("ConstantValue"), (short)0, (short)0, this.itsConstantPool.addConstant(paramInt));
    this.itsFields.add(classFileField);
  }
  
  public void addField(String paramString1, String paramString2, short paramShort, long paramLong) {
    ClassFileField classFileField = new ClassFileField(this.itsConstantPool.addUtf8(paramString1), this.itsConstantPool.addUtf8(paramString2), paramShort);
    classFileField.setAttributes(this.itsConstantPool.addUtf8("ConstantValue"), (short)0, (short)2, this.itsConstantPool.addConstant(paramLong));
    this.itsFields.add(classFileField);
  }
  
  public void addILoad(int paramInt) {
    xop(26, 21, paramInt);
  }
  
  public void addIStore(int paramInt) {
    xop(59, 54, paramInt);
  }
  
  public void addInterface(String paramString) {
    short s = this.itsConstantPool.addClass(paramString);
    this.itsInterfaces.add(Short.valueOf(s));
  }
  
  public void addInvoke(int paramInt, String paramString1, String paramString2, String paramString3) {
    int i = sizeOfParameters(paramString3);
    short s = (short)i;
    int j = this.itsStackTop + s + stackChange(paramInt);
    if (j < 0 || 32767 < j)
      badStack(j); 
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("bad opcode for method reference");
      case 182:
      case 183:
      case 184:
      case 185:
        break;
    } 
    addToCodeBuffer(paramInt);
    if (paramInt == 185) {
      addToCodeInt16(this.itsConstantPool.addInterfaceMethodRef(paramString1, paramString2, paramString3));
      addToCodeBuffer((i >>> 16) + 1);
      addToCodeBuffer(0);
    } else {
      addToCodeInt16(this.itsConstantPool.addMethodRef(paramString1, paramString2, paramString3));
    } 
    this.itsStackTop = (short)(short)j;
    if (j > this.itsMaxStack)
      this.itsMaxStack = (short)(short)j; 
  }
  
  public void addInvokeDynamic(String paramString1, String paramString2, MHandle paramMHandle, Object... paramVarArgs) {
    if (MajorVersion >= 51) {
      short s = (short)sizeOfParameters(paramString2);
      int j = this.itsStackTop + s;
      if (j < 0 || 32767 < j)
        badStack(j); 
      BootstrapEntry bootstrapEntry = new BootstrapEntry(paramMHandle, paramVarArgs);
      if (this.itsBootstrapMethods == null)
        this.itsBootstrapMethods = new ObjArray(); 
      int k = this.itsBootstrapMethods.indexOf(bootstrapEntry);
      int i = k;
      if (k == -1) {
        i = this.itsBootstrapMethods.size();
        this.itsBootstrapMethods.add(bootstrapEntry);
        this.itsBootstrapMethodsLength += bootstrapEntry.code.length;
      } 
      i = this.itsConstantPool.addInvokeDynamic(paramString1, paramString2, i);
      addToCodeBuffer(186);
      addToCodeInt16(i);
      addToCodeInt16(0);
      this.itsStackTop = (short)(short)j;
      if (j > this.itsMaxStack)
        this.itsMaxStack = (short)(short)j; 
      return;
    } 
    throw new RuntimeException("Please build and run with JDK 1.7 for invokedynamic support");
  }
  
  public void addLLoad(int paramInt) {
    xop(30, 22, paramInt);
  }
  
  public void addLStore(int paramInt) {
    xop(63, 55, paramInt);
  }
  
  public void addLineNumberEntry(short paramShort) {
    if (this.itsCurrentMethod != null) {
      int i = this.itsLineNumberTableTop;
      if (i == 0) {
        this.itsLineNumberTable = new int[16];
      } else {
        int[] arrayOfInt = this.itsLineNumberTable;
        if (i == arrayOfInt.length) {
          int[] arrayOfInt1 = new int[i * 2];
          System.arraycopy(arrayOfInt, 0, arrayOfInt1, 0, i);
          this.itsLineNumberTable = arrayOfInt1;
        } 
      } 
      this.itsLineNumberTable[i] = (this.itsCodeBufferTop << 16) + paramShort;
      this.itsLineNumberTableTop = i + 1;
      return;
    } 
    throw new IllegalArgumentException("No method to stop");
  }
  
  public void addLoadConstant(double paramDouble) {
    add(20, this.itsConstantPool.addConstant(paramDouble));
  }
  
  public void addLoadConstant(float paramFloat) {
    add(18, this.itsConstantPool.addConstant(paramFloat));
  }
  
  public void addLoadConstant(int paramInt) {
    if (paramInt != 0) {
      if (paramInt != 1) {
        if (paramInt != 2) {
          if (paramInt != 3) {
            if (paramInt != 4) {
              if (paramInt != 5) {
                add(18, this.itsConstantPool.addConstant(paramInt));
              } else {
                add(8);
              } 
            } else {
              add(7);
            } 
          } else {
            add(6);
          } 
        } else {
          add(5);
        } 
      } else {
        add(4);
      } 
    } else {
      add(3);
    } 
  }
  
  public void addLoadConstant(long paramLong) {
    add(20, this.itsConstantPool.addConstant(paramLong));
  }
  
  public void addLoadConstant(String paramString) {
    add(18, this.itsConstantPool.addConstant(paramString));
  }
  
  public void addLoadThis() {
    add(42);
  }
  
  public void addPush(double paramDouble) {
    if (paramDouble == 0.0D) {
      add(14);
      if (1.0D / paramDouble < 0.0D)
        add(119); 
    } else {
      if (paramDouble == 1.0D || paramDouble == -1.0D) {
        add(15);
        if (paramDouble < 0.0D)
          add(119); 
        return;
      } 
      addLoadConstant(paramDouble);
    } 
  }
  
  public void addPush(int paramInt) {
    if ((byte)paramInt == paramInt) {
      if (paramInt == -1) {
        add(2);
      } else if (paramInt >= 0 && paramInt <= 5) {
        add((byte)(paramInt + 3));
      } else {
        add(16, (byte)paramInt);
      } 
    } else if ((short)paramInt == paramInt) {
      add(17, (short)paramInt);
    } else {
      addLoadConstant(paramInt);
    } 
  }
  
  public void addPush(long paramLong) {
    int i = (int)paramLong;
    if (i == paramLong) {
      addPush(i);
      add(133);
    } else {
      addLoadConstant(paramLong);
    } 
  }
  
  public void addPush(String paramString) {
    int i = paramString.length();
    int j = this.itsConstantPool.getUtfEncodingLimit(paramString, 0, i);
    if (j == i) {
      addLoadConstant(paramString);
      return;
    } 
    add(187, "java/lang/StringBuilder");
    add(89);
    addPush(i);
    addInvoke(183, "java/lang/StringBuilder", "<init>", "(I)V");
    int k = 0;
    while (true) {
      add(89);
      addLoadConstant(paramString.substring(k, j));
      addInvoke(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
      add(87);
      if (j == i) {
        addInvoke(182, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
        return;
      } 
      k = j;
      j = this.itsConstantPool.getUtfEncodingLimit(paramString, j, i);
    } 
  }
  
  public void addPush(boolean paramBoolean) {
    byte b;
    if (paramBoolean) {
      b = 4;
    } else {
      b = 3;
    } 
    add(b);
  }
  
  public int addTableSwitch(int paramInt1, int paramInt2) {
    if (paramInt1 <= paramInt2) {
      int i = this.itsStackTop + stackChange(170);
      if (i < 0 || 32767 < i)
        badStack(i); 
      int j = this.itsCodeBufferTop & 0x3;
      int k = addReservedCodeSpace(j + 1 + (paramInt2 - paramInt1 + 1 + 3) * 4);
      byte[] arrayOfByte = this.itsCodeBuffer;
      int m = k + 1;
      arrayOfByte[k] = (byte)-86;
      while (j != 0) {
        this.itsCodeBuffer[m] = (byte)0;
        j--;
        m++;
      } 
      paramInt1 = putInt32(paramInt1, this.itsCodeBuffer, m + 4);
      putInt32(paramInt2, this.itsCodeBuffer, paramInt1);
      this.itsStackTop = (short)(short)i;
      if (i > this.itsMaxStack)
        this.itsMaxStack = (short)(short)i; 
      return k;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Bad bounds: ");
    stringBuilder.append(paramInt1);
    stringBuilder.append(' ');
    stringBuilder.append(paramInt2);
    throw new ClassFileFormatException(stringBuilder.toString());
  }
  
  public void addVariableDescriptor(String paramString1, String paramString2, int paramInt1, int paramInt2) {
    short s1 = this.itsConstantPool.addUtf8(paramString1);
    short s2 = this.itsConstantPool.addUtf8(paramString2);
    if (this.itsVarDescriptors == null)
      this.itsVarDescriptors = new ObjArray(); 
    this.itsVarDescriptors.add(new int[] { s1, s2, paramInt1, paramInt2 });
  }
  
  public void adjustStackTop(int paramInt) {
    paramInt = this.itsStackTop + paramInt;
    if (paramInt < 0 || 32767 < paramInt)
      badStack(paramInt); 
    this.itsStackTop = (short)(short)paramInt;
    if (paramInt > this.itsMaxStack)
      this.itsMaxStack = (short)(short)paramInt; 
  }
  
  final char[] getCharBuffer(int paramInt) {
    char[] arrayOfChar = this.tmpCharBuffer;
    if (paramInt > arrayOfChar.length) {
      int i = arrayOfChar.length * 2;
      int j = i;
      if (paramInt > i)
        j = paramInt; 
      this.tmpCharBuffer = new char[j];
    } 
    return this.tmpCharBuffer;
  }
  
  public final String getClassName() {
    return this.generatedClassName;
  }
  
  public int getCurrentCodeOffset() {
    return this.itsCodeBufferTop;
  }
  
  public int getLabelPC(int paramInt) {
    if (paramInt < 0) {
      paramInt &= Integer.MAX_VALUE;
      if (paramInt < this.itsLabelTableTop)
        return this.itsLabelTable[paramInt]; 
      throw new IllegalArgumentException("Bad label");
    } 
    throw new IllegalArgumentException("Bad label, no biscuit");
  }
  
  public short getStackTop() {
    return this.itsStackTop;
  }
  
  public boolean isUnderStringSizeLimit(String paramString) {
    return this.itsConstantPool.isUnderUtfEncodingLimit(paramString);
  }
  
  public void markHandler(int paramInt) {
    this.itsStackTop = (short)1;
    markLabel(paramInt);
  }
  
  public void markLabel(int paramInt) {
    if (paramInt < 0) {
      paramInt &= Integer.MAX_VALUE;
      if (paramInt <= this.itsLabelTableTop) {
        int[] arrayOfInt = this.itsLabelTable;
        if (arrayOfInt[paramInt] == -1) {
          arrayOfInt[paramInt] = this.itsCodeBufferTop;
          return;
        } 
        throw new IllegalStateException("Can only mark label once");
      } 
      throw new IllegalArgumentException("Bad label");
    } 
    throw new IllegalArgumentException("Bad label, no biscuit");
  }
  
  public void markLabel(int paramInt, short paramShort) {
    markLabel(paramInt);
    this.itsStackTop = (short)paramShort;
  }
  
  public final void markTableSwitchCase(int paramInt1, int paramInt2) {
    addSuperBlockStart(this.itsCodeBufferTop);
    this.itsJumpFroms.put(this.itsCodeBufferTop, paramInt1);
    setTableSwitchJump(paramInt1, paramInt2, this.itsCodeBufferTop);
  }
  
  public final void markTableSwitchCase(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt3 >= 0 && paramInt3 <= this.itsMaxStack) {
      this.itsStackTop = (short)(short)paramInt3;
      addSuperBlockStart(this.itsCodeBufferTop);
      this.itsJumpFroms.put(this.itsCodeBufferTop, paramInt1);
      setTableSwitchJump(paramInt1, paramInt2, this.itsCodeBufferTop);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Bad stack index: ");
    stringBuilder.append(paramInt3);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public final void markTableSwitchDefault(int paramInt) {
    addSuperBlockStart(this.itsCodeBufferTop);
    this.itsJumpFroms.put(this.itsCodeBufferTop, paramInt);
    setTableSwitchJump(paramInt, -1, this.itsCodeBufferTop);
  }
  
  public void setFlags(short paramShort) {
    this.itsFlags = (short)paramShort;
  }
  
  public void setStackTop(short paramShort) {
    this.itsStackTop = (short)paramShort;
  }
  
  public void setTableSwitchJump(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt3 >= 0 && this.itsCodeBufferTop >= paramInt3) {
      if (paramInt2 >= -1) {
        int j;
        int i = paramInt1 & 0x3;
        if (paramInt2 < 0) {
          j = paramInt1 + 1 + i;
        } else {
          j = paramInt1 + 1 + i + (paramInt2 + 3) * 4;
        } 
        if (paramInt1 >= 0) {
          int k = this.itsCodeBufferTop;
          if (k - 16 - i - 1 >= paramInt1) {
            byte[] arrayOfByte = this.itsCodeBuffer;
            if ((arrayOfByte[paramInt1] & 0xFF) == 170) {
              if (j >= 0 && k >= j + 4) {
                putInt32(paramInt3 - paramInt1, arrayOfByte, j);
                return;
              } 
              StringBuilder stringBuilder4 = new StringBuilder();
              stringBuilder4.append("Too big case index: ");
              stringBuilder4.append(paramInt2);
              throw new ClassFileFormatException(stringBuilder4.toString());
            } 
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(paramInt1);
            stringBuilder3.append(" is not offset of tableswitch statement");
            throw new IllegalArgumentException(stringBuilder3.toString());
          } 
        } 
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(paramInt1);
        stringBuilder2.append(" is outside a possible range of tableswitch in already generated code");
        throw new IllegalArgumentException(stringBuilder2.toString());
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Bad case index: ");
      stringBuilder1.append(paramInt2);
      throw new IllegalArgumentException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Bad jump target: ");
    stringBuilder.append(paramInt3);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void startMethod(String paramString1, String paramString2, short paramShort) {
    this.itsCurrentMethod = new ClassFileMethod(paramString1, this.itsConstantPool.addUtf8(paramString1), paramString2, this.itsConstantPool.addUtf8(paramString2), paramShort);
    this.itsJumpFroms = new UintMap();
    this.itsMethods.add(this.itsCurrentMethod);
    addSuperBlockStart(0);
  }
  
  public void stopMethod(short paramShort) {
    if (this.itsCurrentMethod != null) {
      int i;
      fixLabelGotos();
      this.itsMaxLocals = (short)paramShort;
      StackMapTable stackMapTable = null;
      if (GenerateStackMap) {
        finalizeSuperBlockStarts();
        stackMapTable = new StackMapTable();
        stackMapTable.generate();
      } 
      int j = 0;
      if (this.itsLineNumberTable != null)
        j = this.itsLineNumberTableTop * 4 + 8; 
      paramShort = 0;
      ObjArray objArray = this.itsVarDescriptors;
      if (objArray != null)
        i = objArray.size() * 10 + 8; 
      int k = 0;
      int m = k;
      if (stackMapTable != null) {
        int n = stackMapTable.computeWriteSize();
        m = k;
        if (n > 0)
          m = n + 6; 
      } 
      k = this.itsCodeBufferTop + 14 + 2 + this.itsExceptionTableTop * 8 + 2 + j + i + m;
      if (k <= 65536) {
        byte[] arrayOfByte = new byte[k];
        j = putInt16(this.itsConstantPool.addUtf8("Code"), arrayOfByte, 0);
        int i1 = k - 6;
        j = putInt32(i1, arrayOfByte, j);
        j = putInt16(this.itsMaxStack, arrayOfByte, j);
        j = putInt16(this.itsMaxLocals, arrayOfByte, j);
        j = putInt32(this.itsCodeBufferTop, arrayOfByte, j);
        System.arraycopy(this.itsCodeBuffer, 0, arrayOfByte, j, this.itsCodeBufferTop);
        k = j + this.itsCodeBufferTop;
        j = this.itsExceptionTableTop;
        if (j > 0) {
          j = putInt16(j, arrayOfByte, k);
          k = 0;
          while (k < this.itsExceptionTableTop) {
            ExceptionTableEntry exceptionTableEntry = this.itsExceptionTable[k];
            short s1 = (short)getLabelPC(exceptionTableEntry.itsStartLabel);
            short s2 = (short)getLabelPC(exceptionTableEntry.itsEndLabel);
            short s3 = (short)getLabelPC(exceptionTableEntry.itsHandlerLabel);
            short s4 = exceptionTableEntry.itsCatchType;
            if (s1 != -1) {
              if (s2 != -1) {
                if (s3 != -1) {
                  j = putInt16(s4, arrayOfByte, putInt16(s3, arrayOfByte, putInt16(s2, arrayOfByte, putInt16(s1, arrayOfByte, j))));
                  k++;
                  continue;
                } 
                throw new IllegalStateException("handler label not defined");
              } 
              throw new IllegalStateException("end label not defined");
            } 
            throw new IllegalStateException("start label not defined");
          } 
          n = j;
        } else {
          n = putInt16(0, arrayOfByte, k);
        } 
        j = 0;
        if (this.itsLineNumberTable != null)
          j = 0 + 1; 
        k = j;
        if (this.itsVarDescriptors != null)
          k = j + 1; 
        j = k;
        if (m > 0)
          j = k + 1; 
        int n = putInt16(j, arrayOfByte, n);
        k = n;
        if (this.itsLineNumberTable != null) {
          k = putInt16(this.itsConstantPool.addUtf8("LineNumberTable"), arrayOfByte, n);
          k = putInt32(this.itsLineNumberTableTop * 4 + 2, arrayOfByte, k);
          n = putInt16(this.itsLineNumberTableTop, arrayOfByte, k);
          byte b = 0;
          while (true) {
            k = n;
            if (b < this.itsLineNumberTableTop) {
              n = putInt32(this.itsLineNumberTable[b], arrayOfByte, n);
              b++;
              continue;
            } 
            break;
          } 
        } 
        if (this.itsVarDescriptors != null) {
          k = putInt16(this.itsConstantPool.addUtf8("LocalVariableTable"), arrayOfByte, k);
          int i3 = this.itsVarDescriptors.size();
          int i2 = putInt16(i3, arrayOfByte, putInt32(i3 * 10 + 2, arrayOfByte, k));
          byte b = 0;
          n = i1;
          k = i;
          i = i2;
          while (b < i3) {
            int[] arrayOfInt = (int[])this.itsVarDescriptors.get(b);
            int i4 = arrayOfInt[0];
            i1 = arrayOfInt[1];
            i2 = arrayOfInt[2];
            i = putInt16(arrayOfInt[3], arrayOfByte, putInt16(i1, arrayOfByte, putInt16(i4, arrayOfByte, putInt16(this.itsCodeBufferTop - i2, arrayOfByte, putInt16(i2, arrayOfByte, i)))));
            b++;
          } 
        } else {
          i = k;
        } 
        if (m > 0)
          stackMapTable.write(arrayOfByte, putInt16(this.itsConstantPool.addUtf8("StackMapTable"), arrayOfByte, i)); 
        this.itsCurrentMethod.setCodeAttribute(arrayOfByte);
        this.itsExceptionTable = null;
        this.itsExceptionTableTop = 0;
        this.itsLineNumberTableTop = 0;
        this.itsCodeBufferTop = 0;
        this.itsCurrentMethod = null;
        this.itsMaxStack = (short)0;
        this.itsStackTop = (short)0;
        this.itsLabelTableTop = 0;
        this.itsFixupTableTop = 0;
        this.itsVarDescriptors = null;
        this.itsSuperBlockStarts = null;
        this.itsSuperBlockStartsTop = 0;
        this.itsJumpFroms = null;
        return;
      } 
      throw new ClassFileFormatException("generated bytecode for method exceeds 64K limit.");
    } 
    throw new IllegalStateException("No method to stop");
  }
  
  public byte[] toByteArray() {
    int i = 0;
    int j = 0;
    short s = 0;
    if (this.itsBootstrapMethods != null) {
      j = 0 + 1;
      i = this.itsConstantPool.addUtf8("BootstrapMethods");
    } 
    int k = j;
    if (this.itsSourceFileNameIndex != 0) {
      k = j + 1;
      s = this.itsConstantPool.addUtf8("SourceFile");
    } 
    int m = getWriteSize();
    byte[] arrayOfByte = new byte[m];
    j = putInt32(-889275714, arrayOfByte, 0);
    j = putInt16(MinorVersion, arrayOfByte, j);
    j = putInt16(MajorVersion, arrayOfByte, j);
    j = this.itsConstantPool.write(arrayOfByte, j);
    j = putInt16(this.itsFlags, arrayOfByte, j);
    j = putInt16(this.itsThisClassIndex, arrayOfByte, j);
    j = putInt16(this.itsSuperClassIndex, arrayOfByte, j);
    int n = putInt16(this.itsInterfaces.size(), arrayOfByte, j);
    for (j = 0; j < this.itsInterfaces.size(); j++)
      n = putInt16(((Short)this.itsInterfaces.get(j)).shortValue(), arrayOfByte, n); 
    n = putInt16(this.itsFields.size(), arrayOfByte, n);
    for (j = 0; j < this.itsFields.size(); j++)
      n = ((ClassFileField)this.itsFields.get(j)).write(arrayOfByte, n); 
    n = putInt16(this.itsMethods.size(), arrayOfByte, n);
    for (j = 0; j < this.itsMethods.size(); j++)
      n = ((ClassFileMethod)this.itsMethods.get(j)).write(arrayOfByte, n); 
    k = putInt16(k, arrayOfByte, n);
    j = k;
    if (this.itsBootstrapMethods != null) {
      j = putInt16(i, arrayOfByte, k);
      j = putInt32(this.itsBootstrapMethodsLength + 2, arrayOfByte, j);
      i = putInt16(this.itsBootstrapMethods.size(), arrayOfByte, j);
      k = 0;
      while (true) {
        j = i;
        if (k < this.itsBootstrapMethods.size()) {
          BootstrapEntry bootstrapEntry = (BootstrapEntry)this.itsBootstrapMethods.get(k);
          System.arraycopy(bootstrapEntry.code, 0, arrayOfByte, i, bootstrapEntry.code.length);
          i += bootstrapEntry.code.length;
          k++;
          continue;
        } 
        break;
      } 
    } 
    i = j;
    if (this.itsSourceFileNameIndex != 0) {
      j = putInt32(2, arrayOfByte, putInt16(s, arrayOfByte, j));
      i = putInt16(this.itsSourceFileNameIndex, arrayOfByte, j);
    } 
    if (i == m)
      return arrayOfByte; 
    throw new RuntimeException();
  }
  
  public void write(OutputStream paramOutputStream) throws IOException {
    paramOutputStream.write(toByteArray());
  }
  
  final class BootstrapEntry {
    final byte[] code;
    
    BootstrapEntry(ClassFileWriter.MHandle param1MHandle, Object... param1VarArgs) {
      this.code = new byte[param1VarArgs.length * 2 + 4];
      ClassFileWriter.putInt16(ClassFileWriter.this.itsConstantPool.addMethodHandle(param1MHandle), this.code, 0);
      ClassFileWriter.putInt16(param1VarArgs.length, this.code, 2);
      for (byte b = 0; b < param1VarArgs.length; b++)
        ClassFileWriter.putInt16(ClassFileWriter.this.itsConstantPool.addConstant(param1VarArgs[b]), this.code, b * 2 + 4); 
    }
    
    public boolean equals(Object param1Object) {
      boolean bool;
      if (param1Object instanceof BootstrapEntry && Arrays.equals(this.code, ((BootstrapEntry)param1Object).code)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public int hashCode() {
      return Arrays.hashCode(this.code);
    }
  }
  
  public static class ClassFileFormatException extends RuntimeException {
    private static final long serialVersionUID = 1263998431033790599L;
    
    ClassFileFormatException(String param1String) {
      super(param1String);
    }
  }
  
  public static final class MHandle {
    final String desc;
    
    final String name;
    
    final String owner;
    
    final byte tag;
    
    public MHandle(byte param1Byte, String param1String1, String param1String2, String param1String3) {
      this.tag = (byte)param1Byte;
      this.owner = param1String1;
      this.name = param1String2;
      this.desc = param1String3;
    }
    
    public boolean equals(Object param1Object) {
      boolean bool = true;
      if (param1Object == this)
        return true; 
      if (!(param1Object instanceof MHandle))
        return false; 
      param1Object = param1Object;
      if (this.tag != ((MHandle)param1Object).tag || !this.owner.equals(((MHandle)param1Object).owner) || !this.name.equals(((MHandle)param1Object).name) || !this.desc.equals(((MHandle)param1Object).desc))
        bool = false; 
      return bool;
    }
    
    public int hashCode() {
      return this.tag + this.owner.hashCode() * this.name.hashCode() * this.desc.hashCode();
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.owner);
      stringBuilder.append('.');
      stringBuilder.append(this.name);
      stringBuilder.append(this.desc);
      stringBuilder.append(" (");
      stringBuilder.append(this.tag);
      stringBuilder.append(')');
      return stringBuilder.toString();
    }
  }
  
  final class StackMapTable {
    static final boolean DEBUGSTACKMAP = false;
    
    private int[] locals = null;
    
    private int localsTop = 0;
    
    private byte[] rawStackMap = null;
    
    private int rawStackMapTop = 0;
    
    private int[] stack = null;
    
    private int stackTop = 0;
    
    private SuperBlock[] superBlockDeps;
    
    private SuperBlock[] superBlocks = null;
    
    private boolean wide = false;
    
    private SuperBlock[] workList = null;
    
    private int workListTop = 0;
    
    private void addToWorkList(SuperBlock param1SuperBlock) {
      if (!param1SuperBlock.isInQueue()) {
        param1SuperBlock.setInQueue(true);
        param1SuperBlock.setInitialized(true);
        int i = this.workListTop;
        SuperBlock[] arrayOfSuperBlock1 = this.workList;
        if (i == arrayOfSuperBlock1.length) {
          SuperBlock[] arrayOfSuperBlock = new SuperBlock[i * 2];
          System.arraycopy(arrayOfSuperBlock1, 0, arrayOfSuperBlock, 0, i);
          this.workList = arrayOfSuperBlock;
        } 
        SuperBlock[] arrayOfSuperBlock2 = this.workList;
        i = this.workListTop;
        this.workListTop = i + 1;
        arrayOfSuperBlock2[i] = param1SuperBlock;
      } 
    }
    
    private void clearStack() {
      this.stackTop = 0;
    }
    
    private void computeRawStackMap() {
      int[] arrayOfInt = this.superBlocks[0].getTrimmedLocals();
      int i = -1;
      byte b = 1;
      while (true) {
        SuperBlock[] arrayOfSuperBlock = this.superBlocks;
        if (b < arrayOfSuperBlock.length) {
          SuperBlock superBlock = arrayOfSuperBlock[b];
          int[] arrayOfInt1 = superBlock.getTrimmedLocals();
          int[] arrayOfInt2 = superBlock.getStack();
          int j = superBlock.getStart() - i - 1;
          if (arrayOfInt2.length == 0) {
            if (arrayOfInt.length > arrayOfInt1.length) {
              i = arrayOfInt1.length;
            } else {
              i = arrayOfInt.length;
            } 
            int k = Math.abs(arrayOfInt.length - arrayOfInt1.length);
            byte b1;
            for (b1 = 0; b1 < i && arrayOfInt[b1] == arrayOfInt1[b1]; b1++);
            if (b1 == arrayOfInt1.length && k == 0) {
              writeSameFrame(j);
            } else if (b1 == arrayOfInt1.length && k <= 3) {
              writeChopFrame(k, j);
            } else if (b1 == arrayOfInt.length && k <= 3) {
              writeAppendFrame(arrayOfInt1, k, j);
            } else {
              writeFullFrame(arrayOfInt1, arrayOfInt2, j);
            } 
          } else if (arrayOfInt2.length == 1) {
            if (Arrays.equals(arrayOfInt, arrayOfInt1)) {
              writeSameLocalsOneStackItemFrame(arrayOfInt2, j);
            } else {
              writeFullFrame(arrayOfInt1, arrayOfInt2, j);
            } 
          } else {
            writeFullFrame(arrayOfInt1, arrayOfInt2, j);
          } 
          arrayOfInt = arrayOfInt1;
          i = superBlock.getStart();
          b++;
          continue;
        } 
        break;
      } 
    }
    
    private int execute(int param1Int) {
      StringBuilder stringBuilder1;
      ConstantPool constantPool;
      String str1;
      String str3;
      StringBuilder stringBuilder2;
      FieldOrMethodRef fieldOrMethodRef;
      String str2;
      char c;
      long l1;
      long l2;
      int i = ClassFileWriter.this.itsCodeBuffer[param1Int] & 0xFF;
      int j = 0;
      int k = 2;
      boolean bool = true;
      switch (i) {
        default:
          stringBuilder1 = new StringBuilder();
          stringBuilder1.append("bad opcode: ");
          stringBuilder1.append(i);
          throw new IllegalArgumentException(stringBuilder1.toString());
        case 196:
          this.wide = true;
          param1Int = j;
          break;
        case 192:
          pop();
          push(TypeInfo.OBJECT(getOperand(param1Int + 1, 2)));
          param1Int = j;
          break;
        case 191:
          param1Int = pop();
          clearStack();
          push(param1Int);
          param1Int = j;
          break;
        case 189:
          param1Int = getOperand(param1Int + 1, 2);
          str3 = (String)ClassFileWriter.this.itsConstantPool.getConstantData(param1Int);
          pop();
          stringBuilder1 = new StringBuilder();
          stringBuilder1.append("[L");
          stringBuilder1.append(str3);
          stringBuilder1.append(';');
          push(TypeInfo.OBJECT(stringBuilder1.toString(), ClassFileWriter.this.itsConstantPool));
          param1Int = j;
          break;
        case 188:
          pop();
          c = ClassFileWriter.arrayTypeToName(ClassFileWriter.this.itsCodeBuffer[param1Int + 1]);
          constantPool = ClassFileWriter.this.itsConstantPool;
          stringBuilder2 = new StringBuilder();
          stringBuilder2.append("[");
          stringBuilder2.append(c);
          push(TypeInfo.OBJECT(constantPool.addClass(stringBuilder2.toString())));
          param1Int = j;
          break;
        case 187:
          push(TypeInfo.UNINITIALIZED_VARIABLE(param1Int));
          param1Int = j;
          break;
        case 186:
          param1Int = getOperand(param1Int + 1, 2);
          str1 = (String)ClassFileWriter.this.itsConstantPool.getConstantData(param1Int);
          k = ClassFileWriter.sizeOfParameters(str1);
          for (param1Int = 0; param1Int < k >>> 16; param1Int++)
            pop(); 
          str1 = ClassFileWriter.descriptorToInternalName(str1.substring(str1.indexOf(')') + 1));
          param1Int = j;
          if (!str1.equals("V")) {
            push(TypeInfo.fromType(str1, ClassFileWriter.this.itsConstantPool));
            param1Int = j;
          } 
          break;
        case 182:
        case 183:
        case 184:
        case 185:
          param1Int = getOperand(param1Int + 1, 2);
          fieldOrMethodRef = (FieldOrMethodRef)ClassFileWriter.this.itsConstantPool.getConstantData(param1Int);
          str1 = fieldOrMethodRef.getType();
          str2 = fieldOrMethodRef.getName();
          k = ClassFileWriter.sizeOfParameters(str1);
          for (param1Int = 0; param1Int < k >>> 16; param1Int++)
            pop(); 
          if (i != 184) {
            k = pop();
            param1Int = TypeInfo.getTag(k);
            if (param1Int == TypeInfo.UNINITIALIZED_VARIABLE(0) || param1Int == 6)
              if ("<init>".equals(str2)) {
                initializeTypeInfo(k, TypeInfo.OBJECT(ClassFileWriter.this.itsThisClassIndex));
              } else {
                throw new IllegalStateException("bad instance");
              }  
          } 
          str1 = ClassFileWriter.descriptorToInternalName(str1.substring(str1.indexOf(')') + 1));
          param1Int = j;
          if (!str1.equals("V")) {
            push(TypeInfo.fromType(str1, ClassFileWriter.this.itsConstantPool));
            param1Int = j;
          } 
          break;
        case 180:
          pop();
        case 178:
          param1Int = getOperand(param1Int + 1, 2);
          push(TypeInfo.fromType(ClassFileWriter.descriptorToInternalName(((FieldOrMethodRef)ClassFileWriter.this.itsConstantPool.getConstantData(param1Int)).getType()), ClassFileWriter.this.itsConstantPool));
          param1Int = j;
          break;
        case 172:
        case 173:
        case 174:
        case 175:
        case 176:
        case 177:
          clearStack();
          param1Int = j;
          break;
        case 170:
          k = param1Int + 1 + (param1Int & 0x3);
          j = getOperand(k + 4, 4);
          param1Int = (getOperand(k + 8, 4) - j + 4) * 4 + k - param1Int;
          pop();
          break;
        case 95:
          k = pop();
          param1Int = pop();
          push(k);
          push(param1Int);
          param1Int = j;
          break;
        case 94:
          l1 = pop2();
          l2 = pop2();
          push2(l1);
          push2(l2);
          push2(l1);
          param1Int = j;
          break;
        case 93:
          l1 = pop2();
          param1Int = pop();
          push2(l1);
          push(param1Int);
          push2(l1);
          param1Int = j;
          break;
        case 92:
          l1 = pop2();
          push2(l1);
          push2(l1);
          param1Int = j;
          break;
        case 91:
          param1Int = pop();
          l1 = pop2();
          push(param1Int);
          push2(l1);
          push(param1Int);
          param1Int = j;
          break;
        case 90:
          param1Int = pop();
          k = pop();
          push(param1Int);
          push(k);
          push(param1Int);
          param1Int = j;
          break;
        case 89:
          param1Int = pop();
          push(param1Int);
          push(param1Int);
          param1Int = j;
          break;
        case 88:
          pop2();
          param1Int = j;
          break;
        case 79:
        case 80:
        case 81:
        case 82:
        case 83:
        case 84:
        case 85:
        case 86:
          pop();
        case 159:
        case 160:
        case 161:
        case 162:
        case 163:
        case 164:
        case 165:
        case 166:
        case 181:
          pop();
        case 87:
        case 153:
        case 154:
        case 155:
        case 156:
        case 157:
        case 158:
        case 179:
        case 194:
        case 195:
        case 198:
        case 199:
          pop();
          param1Int = j;
          break;
        case 75:
        case 76:
        case 77:
        case 78:
          executeAStore(i - 75);
          param1Int = j;
          break;
        case 71:
        case 72:
        case 73:
        case 74:
          executeStore(i - 71, 3);
          param1Int = j;
          break;
        case 67:
        case 68:
        case 69:
        case 70:
          executeStore(i - 67, 2);
          param1Int = j;
          break;
        case 63:
        case 64:
        case 65:
        case 66:
          executeStore(i - 63, 4);
          param1Int = j;
          break;
        case 59:
        case 60:
        case 61:
        case 62:
          executeStore(i - 59, 1);
          param1Int = j;
          break;
        case 58:
          if (!this.wide)
            k = 1; 
          executeAStore(getOperand(param1Int + 1, k));
          param1Int = j;
          break;
        case 57:
          if (!this.wide)
            k = 1; 
          executeStore(getOperand(param1Int + 1, k), 3);
          param1Int = j;
          break;
        case 56:
          k = bool;
          if (this.wide)
            k = 2; 
          executeStore(getOperand(param1Int + 1, k), 2);
          param1Int = j;
          break;
        case 55:
          if (!this.wide)
            k = 1; 
          executeStore(getOperand(param1Int + 1, k), 4);
          param1Int = j;
          break;
        case 54:
          if (!this.wide)
            k = 1; 
          executeStore(getOperand(param1Int + 1, k), 1);
          param1Int = j;
          break;
        case 50:
          pop();
          param1Int = pop();
          str1 = (String)ClassFileWriter.this.itsConstantPool.getConstantData(param1Int >>> 8);
          if (str1.charAt(0) == '[') {
            str1 = ClassFileWriter.descriptorToInternalName(str1.substring(1));
            push(TypeInfo.OBJECT(ClassFileWriter.this.itsConstantPool.addClass(str1)));
            param1Int = j;
            break;
          } 
          throw new IllegalStateException("bad array type");
        case 49:
        case 99:
        case 103:
        case 107:
        case 111:
        case 115:
          pop();
        case 119:
        case 135:
        case 138:
        case 141:
          pop();
        case 48:
        case 98:
        case 102:
        case 106:
        case 110:
        case 114:
          pop();
        case 118:
        case 134:
        case 137:
        case 144:
          pop();
        case 47:
        case 97:
        case 101:
        case 105:
        case 109:
        case 113:
        case 121:
        case 123:
        case 125:
        case 127:
        case 129:
        case 131:
          pop();
        case 117:
        case 133:
        case 140:
        case 143:
          pop();
        case 46:
        case 51:
        case 52:
        case 53:
        case 96:
        case 100:
        case 104:
        case 108:
        case 112:
        case 120:
        case 122:
        case 124:
        case 126:
        case 128:
        case 130:
        case 148:
        case 149:
        case 150:
        case 151:
        case 152:
          pop();
        case 116:
        case 136:
        case 139:
        case 142:
        case 145:
        case 146:
        case 147:
        case 190:
        case 193:
          pop();
        case 42:
        case 43:
        case 44:
        case 45:
          executeALoad(i - 42);
          param1Int = j;
          break;
        case 25:
          if (!this.wide)
            k = 1; 
          executeALoad(getOperand(param1Int + 1, k));
          param1Int = j;
          break;
        case 18:
        case 19:
        case 20:
          if (i == 18) {
            param1Int = getOperand(param1Int + 1);
          } else {
            param1Int = getOperand(param1Int + 1, 2);
          } 
          param1Int = ClassFileWriter.this.itsConstantPool.getConstantType(param1Int);
          if (param1Int != 3) {
            if (param1Int != 4) {
              if (param1Int != 5) {
                if (param1Int != 6) {
                  if (param1Int == 8) {
                    push(TypeInfo.OBJECT("java/lang/String", ClassFileWriter.this.itsConstantPool));
                    param1Int = j;
                    break;
                  } 
                  StringBuilder stringBuilder = new StringBuilder();
                  stringBuilder.append("bad const type ");
                  stringBuilder.append(param1Int);
                  throw new IllegalArgumentException(stringBuilder.toString());
                } 
                push(3);
                param1Int = j;
                break;
              } 
              push(4);
              param1Int = j;
              break;
            } 
            push(2);
            param1Int = j;
            break;
          } 
          push(1);
          param1Int = j;
          break;
        case 14:
        case 15:
        case 24:
        case 38:
        case 39:
        case 40:
        case 41:
          push(3);
          param1Int = j;
          break;
        case 11:
        case 12:
        case 13:
        case 23:
        case 34:
        case 35:
        case 36:
        case 37:
          push(2);
          param1Int = j;
          break;
        case 9:
        case 10:
        case 22:
        case 30:
        case 31:
        case 32:
        case 33:
          push(4);
          param1Int = j;
          break;
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
        case 16:
        case 17:
        case 21:
        case 26:
        case 27:
        case 28:
        case 29:
          push(1);
          param1Int = j;
          break;
        case 1:
          push(5);
          param1Int = j;
          break;
        case 0:
        case 132:
        case 167:
        case 200:
          param1Int = j;
          break;
      } 
      k = param1Int;
      if (param1Int == 0)
        k = ClassFileWriter.opcodeLength(i, this.wide); 
      if (this.wide && i != 196)
        this.wide = false; 
      return k;
    }
    
    private void executeALoad(int param1Int) {
      int i = getLocal(param1Int);
      int j = TypeInfo.getTag(i);
      if (j == 7 || j == 6 || j == 8 || j == 5) {
        push(i);
        return;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("bad local variable type: ");
      stringBuilder.append(i);
      stringBuilder.append(" at index: ");
      stringBuilder.append(param1Int);
      throw new IllegalStateException(stringBuilder.toString());
    }
    
    private void executeAStore(int param1Int) {
      setLocal(param1Int, pop());
    }
    
    private void executeBlock(SuperBlock param1SuperBlock) {
      int i = 0;
      int j = param1SuperBlock.getStart();
      while (j < param1SuperBlock.getEnd()) {
        int k = ClassFileWriter.this.itsCodeBuffer[j] & 0xFF;
        int m = execute(j);
        if (isBranch(k)) {
          flowInto(getBranchTarget(j));
        } else if (k == 170) {
          int n = j + 1 + (j & 0x3);
          flowInto(getSuperBlockFromOffset(j + getOperand(n, 4)));
          int i1 = getOperand(n + 4, 4);
          int i2 = getOperand(n + 8, 4);
          for (i = 0; i < i2 - i1 + 1; i++)
            flowInto(getSuperBlockFromOffset(getOperand(i * 4 + n + 12, 4) + j)); 
        } 
        for (i = 0; i < ClassFileWriter.this.itsExceptionTableTop; i++) {
          ExceptionTableEntry exceptionTableEntry = ClassFileWriter.this.itsExceptionTable[i];
          short s1 = (short)ClassFileWriter.this.getLabelPC(exceptionTableEntry.itsStartLabel);
          short s2 = (short)ClassFileWriter.this.getLabelPC(exceptionTableEntry.itsEndLabel);
          if (j >= s1 && j < s2) {
            int n;
            SuperBlock superBlock = getSuperBlockFromOffset((short)ClassFileWriter.this.getLabelPC(exceptionTableEntry.itsHandlerLabel));
            if (exceptionTableEntry.itsCatchType == 0) {
              n = TypeInfo.OBJECT(ClassFileWriter.this.itsConstantPool.addClass("java/lang/Throwable"));
            } else {
              n = TypeInfo.OBJECT(exceptionTableEntry.itsCatchType);
            } 
            int[] arrayOfInt = this.locals;
            int i1 = this.localsTop;
            ConstantPool constantPool = ClassFileWriter.this.itsConstantPool;
            superBlock.merge(arrayOfInt, i1, new int[] { n }, 1, constantPool);
            addToWorkList(superBlock);
          } 
        } 
        j += m;
        i = k;
      } 
      if (!isSuperBlockEnd(i)) {
        j = param1SuperBlock.getIndex() + 1;
        SuperBlock[] arrayOfSuperBlock = this.superBlocks;
        if (j < arrayOfSuperBlock.length)
          flowInto(arrayOfSuperBlock[j]); 
      } 
    }
    
    private void executeStore(int param1Int1, int param1Int2) {
      pop();
      setLocal(param1Int1, param1Int2);
    }
    
    private void executeWorkList() {
      while (true) {
        int i = this.workListTop;
        if (i > 0) {
          SuperBlock[] arrayOfSuperBlock = this.workList;
          this.workListTop = --i;
          SuperBlock superBlock = arrayOfSuperBlock[i];
          superBlock.setInQueue(false);
          this.locals = superBlock.getLocals();
          int[] arrayOfInt = superBlock.getStack();
          this.stack = arrayOfInt;
          this.localsTop = this.locals.length;
          this.stackTop = arrayOfInt.length;
          executeBlock(superBlock);
          continue;
        } 
        break;
      } 
    }
    
    private void flowInto(SuperBlock param1SuperBlock) {
      if (param1SuperBlock.merge(this.locals, this.localsTop, this.stack, this.stackTop, ClassFileWriter.this.itsConstantPool))
        addToWorkList(param1SuperBlock); 
    }
    
    private SuperBlock getBranchTarget(int param1Int) {
      if ((ClassFileWriter.this.itsCodeBuffer[param1Int] & 0xFF) == 200) {
        param1Int = getOperand(param1Int + 1, 4) + param1Int;
      } else {
        param1Int = (short)getOperand(param1Int + 1, 2) + param1Int;
      } 
      return getSuperBlockFromOffset(param1Int);
    }
    
    private int getLocal(int param1Int) {
      return (param1Int < this.localsTop) ? this.locals[param1Int] : 0;
    }
    
    private int getOperand(int param1Int) {
      return getOperand(param1Int, 1);
    }
    
    private int getOperand(int param1Int1, int param1Int2) {
      int i = 0;
      if (param1Int2 <= 4) {
        for (byte b = 0; b < param1Int2; b++)
          i = i << 8 | ClassFileWriter.this.itsCodeBuffer[param1Int1 + b] & 0xFF; 
        return i;
      } 
      throw new IllegalArgumentException("bad operand size");
    }
    
    private SuperBlock[] getSuperBlockDependencies() {
      SuperBlock[] arrayOfSuperBlock = new SuperBlock[this.superBlocks.length];
      byte b;
      for (b = 0; b < ClassFileWriter.this.itsExceptionTableTop; b++) {
        ExceptionTableEntry exceptionTableEntry = ClassFileWriter.this.itsExceptionTable[b];
        short s = (short)ClassFileWriter.this.getLabelPC(exceptionTableEntry.itsStartLabel);
        SuperBlock superBlock1 = getSuperBlockFromOffset((short)ClassFileWriter.this.getLabelPC(exceptionTableEntry.itsHandlerLabel));
        SuperBlock superBlock2 = getSuperBlockFromOffset(s);
        arrayOfSuperBlock[superBlock1.getIndex()] = superBlock2;
      } 
      int[] arrayOfInt = ClassFileWriter.this.itsJumpFroms.getKeys();
      for (b = 0; b < arrayOfInt.length; b++) {
        int i = arrayOfInt[b];
        SuperBlock superBlock = getSuperBlockFromOffset(ClassFileWriter.this.itsJumpFroms.getInt(i, -1));
        arrayOfSuperBlock[getSuperBlockFromOffset(i).getIndex()] = superBlock;
      } 
      return arrayOfSuperBlock;
    }
    
    private SuperBlock getSuperBlockFromOffset(int param1Int) {
      byte b = 0;
      while (true) {
        SuperBlock[] arrayOfSuperBlock = this.superBlocks;
        if (b < arrayOfSuperBlock.length) {
          SuperBlock superBlock = arrayOfSuperBlock[b];
          if (superBlock != null) {
            if (param1Int >= superBlock.getStart() && param1Int < superBlock.getEnd())
              return superBlock; 
            b++;
            continue;
          } 
        } 
        break;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("bad offset: ");
      stringBuilder.append(param1Int);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    private int getWorstCaseWriteSize() {
      return (this.superBlocks.length - 1) * (ClassFileWriter.this.itsMaxLocals * 3 + 7 + ClassFileWriter.this.itsMaxStack * 3);
    }
    
    private void initializeTypeInfo(int param1Int1, int param1Int2) {
      initializeTypeInfo(param1Int1, param1Int2, this.locals, this.localsTop);
      initializeTypeInfo(param1Int1, param1Int2, this.stack, this.stackTop);
    }
    
    private void initializeTypeInfo(int param1Int1, int param1Int2, int[] param1ArrayOfint, int param1Int3) {
      for (byte b = 0; b < param1Int3; b++) {
        if (param1ArrayOfint[b] == param1Int1)
          param1ArrayOfint[b] = param1Int2; 
      } 
    }
    
    private boolean isBranch(int param1Int) {
      switch (param1Int) {
        default:
          switch (param1Int) {
            default:
              return false;
            case 198:
            case 199:
            case 200:
              break;
          } 
          break;
        case 153:
        case 154:
        case 155:
        case 156:
        case 157:
        case 158:
        case 159:
        case 160:
        case 161:
        case 162:
        case 163:
        case 164:
        case 165:
        case 166:
        case 167:
          break;
      } 
      return true;
    }
    
    private boolean isSuperBlockEnd(int param1Int) {
      if (param1Int != 167 && param1Int != 191 && param1Int != 200 && param1Int != 176 && param1Int != 177)
        switch (param1Int) {
          default:
            return false;
          case 170:
          case 171:
          case 172:
          case 173:
          case 174:
            break;
        }  
      return true;
    }
    
    private void killSuperBlock(SuperBlock param1SuperBlock) {
      int[] arrayOfInt3;
      int[] arrayOfInt1 = new int[0];
      int[] arrayOfInt2 = new int[1];
      arrayOfInt2[0] = TypeInfo.OBJECT("java/lang/Throwable", ClassFileWriter.this.itsConstantPool);
      int i = 0;
      while (true) {
        arrayOfInt3 = arrayOfInt1;
        if (i < ClassFileWriter.this.itsExceptionTableTop) {
          ExceptionTableEntry exceptionTableEntry = ClassFileWriter.this.itsExceptionTable[i];
          int k = ClassFileWriter.this.getLabelPC(exceptionTableEntry.itsStartLabel);
          int m = ClassFileWriter.this.getLabelPC(exceptionTableEntry.itsEndLabel);
          SuperBlock superBlock = getSuperBlockFromOffset(ClassFileWriter.this.getLabelPC(exceptionTableEntry.itsHandlerLabel));
          if ((param1SuperBlock.getStart() > k && param1SuperBlock.getStart() < m) || (k > param1SuperBlock.getStart() && k < param1SuperBlock.getEnd() && superBlock.isInitialized())) {
            arrayOfInt3 = superBlock.getLocals();
            break;
          } 
          i++;
          continue;
        } 
        break;
      } 
      for (i = 0; i < ClassFileWriter.this.itsExceptionTableTop; i = k + 1) {
        ExceptionTableEntry exceptionTableEntry = ClassFileWriter.this.itsExceptionTable[i];
        int k = i;
        if (ClassFileWriter.this.getLabelPC(exceptionTableEntry.itsStartLabel) == param1SuperBlock.getStart()) {
          for (k = i + 1; k < ClassFileWriter.this.itsExceptionTableTop; k++)
            ClassFileWriter.this.itsExceptionTable[k - 1] = ClassFileWriter.this.itsExceptionTable[k]; 
          ClassFileWriter.access$410(ClassFileWriter.this);
          k = i - 1;
        } 
      } 
      param1SuperBlock.merge(arrayOfInt3, arrayOfInt3.length, arrayOfInt2, arrayOfInt2.length, ClassFileWriter.this.itsConstantPool);
      int j = param1SuperBlock.getEnd() - 1;
      ClassFileWriter.this.itsCodeBuffer[j] = (byte)-65;
      for (i = param1SuperBlock.getStart(); i < j; i++)
        ClassFileWriter.this.itsCodeBuffer[i] = (byte)0; 
    }
    
    private int pop() {
      int[] arrayOfInt = this.stack;
      int i = this.stackTop - 1;
      this.stackTop = i;
      return arrayOfInt[i];
    }
    
    private long pop2() {
      long l = pop();
      return TypeInfo.isTwoWords((int)l) ? l : (l << 32L | (pop() & 0xFFFFFF));
    }
    
    private void push(int param1Int) {
      int i = this.stackTop;
      if (i == this.stack.length) {
        int[] arrayOfInt1 = new int[Math.max(i * 2, 4)];
        System.arraycopy(this.stack, 0, arrayOfInt1, 0, this.stackTop);
        this.stack = arrayOfInt1;
      } 
      int[] arrayOfInt = this.stack;
      i = this.stackTop;
      this.stackTop = i + 1;
      arrayOfInt[i] = param1Int;
    }
    
    private void push2(long param1Long) {
      push((int)(param1Long & 0xFFFFFFL));
      param1Long >>>= 32L;
      if (param1Long != 0L)
        push((int)(0xFFFFFFL & param1Long)); 
    }
    
    private void setLocal(int param1Int1, int param1Int2) {
      int i = this.localsTop;
      if (param1Int1 >= i) {
        int[] arrayOfInt = new int[param1Int1 + 1];
        System.arraycopy(this.locals, 0, arrayOfInt, 0, i);
        this.locals = arrayOfInt;
        this.localsTop = param1Int1 + 1;
      } 
      this.locals[param1Int1] = param1Int2;
    }
    
    private void verify() {
      int[] arrayOfInt = ClassFileWriter.this.createInitialLocals();
      SuperBlock superBlock = this.superBlocks[0];
      int i = arrayOfInt.length;
      ConstantPool constantPool = ClassFileWriter.this.itsConstantPool;
      superBlock.merge(arrayOfInt, i, new int[0], 0, constantPool);
      this.workList = new SuperBlock[] { this.superBlocks[0] };
      this.workListTop = 1;
      executeWorkList();
      i = 0;
      while (true) {
        SuperBlock[] arrayOfSuperBlock = this.superBlocks;
        if (i < arrayOfSuperBlock.length) {
          SuperBlock superBlock1 = arrayOfSuperBlock[i];
          if (!superBlock1.isInitialized())
            killSuperBlock(superBlock1); 
          i++;
          continue;
        } 
        executeWorkList();
        return;
      } 
    }
    
    private void writeAppendFrame(int[] param1ArrayOfint, int param1Int1, int param1Int2) {
      int i = param1ArrayOfint.length;
      byte[] arrayOfByte = this.rawStackMap;
      int j = this.rawStackMapTop;
      int k = j + 1;
      this.rawStackMapTop = k;
      arrayOfByte[j] = (byte)(byte)(param1Int1 + 251);
      this.rawStackMapTop = ClassFileWriter.putInt16(param1Int2, arrayOfByte, k);
      this.rawStackMapTop = writeTypes(param1ArrayOfint, i - param1Int1);
    }
    
    private void writeChopFrame(int param1Int1, int param1Int2) {
      byte[] arrayOfByte = this.rawStackMap;
      int i = this.rawStackMapTop;
      int j = i + 1;
      this.rawStackMapTop = j;
      arrayOfByte[i] = (byte)(byte)(251 - param1Int1);
      this.rawStackMapTop = ClassFileWriter.putInt16(param1Int2, arrayOfByte, j);
    }
    
    private void writeFullFrame(int[] param1ArrayOfint1, int[] param1ArrayOfint2, int param1Int) {
      byte[] arrayOfByte = this.rawStackMap;
      int i = this.rawStackMapTop;
      int j = i + 1;
      this.rawStackMapTop = j;
      arrayOfByte[i] = (byte)-1;
      param1Int = ClassFileWriter.putInt16(param1Int, arrayOfByte, j);
      this.rawStackMapTop = param1Int;
      this.rawStackMapTop = ClassFileWriter.putInt16(param1ArrayOfint1.length, this.rawStackMap, param1Int);
      param1Int = writeTypes(param1ArrayOfint1);
      this.rawStackMapTop = param1Int;
      this.rawStackMapTop = ClassFileWriter.putInt16(param1ArrayOfint2.length, this.rawStackMap, param1Int);
      this.rawStackMapTop = writeTypes(param1ArrayOfint2);
    }
    
    private void writeSameFrame(int param1Int) {
      if (param1Int <= 63) {
        byte[] arrayOfByte = this.rawStackMap;
        int i = this.rawStackMapTop;
        this.rawStackMapTop = i + 1;
        arrayOfByte[i] = (byte)(byte)param1Int;
      } else {
        byte[] arrayOfByte = this.rawStackMap;
        int i = this.rawStackMapTop;
        int j = i + 1;
        this.rawStackMapTop = j;
        arrayOfByte[i] = (byte)-5;
        this.rawStackMapTop = ClassFileWriter.putInt16(param1Int, arrayOfByte, j);
      } 
    }
    
    private void writeSameLocalsOneStackItemFrame(int[] param1ArrayOfint, int param1Int) {
      if (param1Int <= 63) {
        byte[] arrayOfByte = this.rawStackMap;
        int i = this.rawStackMapTop;
        this.rawStackMapTop = i + 1;
        arrayOfByte[i] = (byte)(byte)(param1Int + 64);
      } else {
        byte[] arrayOfByte = this.rawStackMap;
        int j = this.rawStackMapTop;
        int i = j + 1;
        this.rawStackMapTop = i;
        arrayOfByte[j] = (byte)-9;
        this.rawStackMapTop = ClassFileWriter.putInt16(param1Int, arrayOfByte, i);
      } 
      writeType(param1ArrayOfint[0]);
    }
    
    private int writeType(int param1Int) {
      int i = param1Int & 0xFF;
      byte[] arrayOfByte = this.rawStackMap;
      int j = this.rawStackMapTop;
      this.rawStackMapTop = j + 1;
      arrayOfByte[j] = (byte)(byte)i;
      if (i == 7 || i == 8)
        this.rawStackMapTop = ClassFileWriter.putInt16(param1Int >>> 8, this.rawStackMap, this.rawStackMapTop); 
      return this.rawStackMapTop;
    }
    
    private int writeTypes(int[] param1ArrayOfint) {
      return writeTypes(param1ArrayOfint, 0);
    }
    
    private int writeTypes(int[] param1ArrayOfint, int param1Int) {
      while (param1Int < param1ArrayOfint.length) {
        this.rawStackMapTop = writeType(param1ArrayOfint[param1Int]);
        param1Int++;
      } 
      return this.rawStackMapTop;
    }
    
    int computeWriteSize() {
      this.rawStackMap = new byte[getWorstCaseWriteSize()];
      computeRawStackMap();
      return this.rawStackMapTop + 2;
    }
    
    void generate() {
      this.superBlocks = new SuperBlock[ClassFileWriter.this.itsSuperBlockStartsTop];
      int[] arrayOfInt = ClassFileWriter.this.createInitialLocals();
      for (byte b = 0; b < ClassFileWriter.this.itsSuperBlockStartsTop; b++) {
        int j;
        int i = ClassFileWriter.this.itsSuperBlockStarts[b];
        if (b == ClassFileWriter.this.itsSuperBlockStartsTop - 1) {
          j = ClassFileWriter.this.itsCodeBufferTop;
        } else {
          j = ClassFileWriter.this.itsSuperBlockStarts[b + 1];
        } 
        this.superBlocks[b] = new SuperBlock(b, i, j, arrayOfInt);
      } 
      this.superBlockDeps = getSuperBlockDependencies();
      verify();
    }
    
    int write(byte[] param1ArrayOfbyte, int param1Int) {
      param1Int = ClassFileWriter.putInt32(this.rawStackMapTop + 2, param1ArrayOfbyte, param1Int);
      param1Int = ClassFileWriter.putInt16(this.superBlocks.length - 1, param1ArrayOfbyte, param1Int);
      System.arraycopy(this.rawStackMap, 0, param1ArrayOfbyte, param1Int, this.rawStackMapTop);
      return this.rawStackMapTop + param1Int;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/classfile/ClassFileWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */