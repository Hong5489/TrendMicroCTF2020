package com.trendmicro.classfile;

final class FieldOrMethodRef {
  private String className;
  
  private int hashCode = -1;
  
  private String name;
  
  private String type;
  
  FieldOrMethodRef(String paramString1, String paramString2, String paramString3) {
    this.className = paramString1;
    this.name = paramString2;
    this.type = paramString3;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof FieldOrMethodRef;
    boolean bool1 = false;
    if (!bool)
      return false; 
    paramObject = paramObject;
    if (this.className.equals(((FieldOrMethodRef)paramObject).className) && this.name.equals(((FieldOrMethodRef)paramObject).name) && this.type.equals(((FieldOrMethodRef)paramObject).type))
      bool1 = true; 
    return bool1;
  }
  
  public String getClassName() {
    return this.className;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getType() {
    return this.type;
  }
  
  public int hashCode() {
    if (this.hashCode == -1)
      this.hashCode = this.className.hashCode() ^ this.name.hashCode() ^ this.type.hashCode(); 
    return this.hashCode;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/classfile/FieldOrMethodRef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */