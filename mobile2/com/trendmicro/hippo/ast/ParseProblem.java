package com.trendmicro.hippo.ast;

public class ParseProblem {
  private int length;
  
  private String message;
  
  private int offset;
  
  private String sourceName;
  
  private Type type;
  
  public ParseProblem(Type paramType, String paramString1, String paramString2, int paramInt1, int paramInt2) {
    setType(paramType);
    setMessage(paramString1);
    setSourceName(paramString2);
    setFileOffset(paramInt1);
    setLength(paramInt2);
  }
  
  public int getFileOffset() {
    return this.offset;
  }
  
  public int getLength() {
    return this.length;
  }
  
  public String getMessage() {
    return this.message;
  }
  
  public String getSourceName() {
    return this.sourceName;
  }
  
  public Type getType() {
    return this.type;
  }
  
  public void setFileOffset(int paramInt) {
    this.offset = paramInt;
  }
  
  public void setLength(int paramInt) {
    this.length = paramInt;
  }
  
  public void setMessage(String paramString) {
    this.message = paramString;
  }
  
  public void setSourceName(String paramString) {
    this.sourceName = paramString;
  }
  
  public void setType(Type paramType) {
    this.type = paramType;
  }
  
  public String toString() {
    String str;
    StringBuilder stringBuilder = new StringBuilder(200);
    stringBuilder.append(this.sourceName);
    stringBuilder.append(":");
    stringBuilder.append("offset=");
    stringBuilder.append(this.offset);
    stringBuilder.append(",");
    stringBuilder.append("length=");
    stringBuilder.append(this.length);
    stringBuilder.append(",");
    if (this.type == Type.Error) {
      str = "error: ";
    } else {
      str = "warning: ";
    } 
    stringBuilder.append(str);
    stringBuilder.append(this.message);
    return stringBuilder.toString();
  }
  
  public enum Type {
    Error, Warning;
    
    static {
      Type type = new Type("Warning", 1);
      Warning = type;
      $VALUES = new Type[] { Error, type };
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/ParseProblem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */