package com.trendmicro.hippo.optimizer;

import com.trendmicro.hippo.Kit;
import com.trendmicro.hippo.Node;
import com.trendmicro.hippo.ast.FunctionNode;
import com.trendmicro.hippo.ast.ScriptNode;

public final class OptFunctionNode {
  private int directTargetIndex = -1;
  
  public final FunctionNode fnode;
  
  boolean itsContainsCalls0;
  
  boolean itsContainsCalls1;
  
  private boolean itsParameterNumberContext;
  
  private boolean[] numberVarFlags;
  
  OptFunctionNode(FunctionNode paramFunctionNode) {
    this.fnode = paramFunctionNode;
    paramFunctionNode.setCompilerData(this);
  }
  
  public static OptFunctionNode get(ScriptNode paramScriptNode) {
    return (OptFunctionNode)paramScriptNode.getCompilerData();
  }
  
  public static OptFunctionNode get(ScriptNode paramScriptNode, int paramInt) {
    return (OptFunctionNode)paramScriptNode.getFunctionNode(paramInt).getCompilerData();
  }
  
  public int getDirectTargetIndex() {
    return this.directTargetIndex;
  }
  
  public boolean getParameterNumberContext() {
    return this.itsParameterNumberContext;
  }
  
  public int getVarCount() {
    return this.fnode.getParamAndVarCount();
  }
  
  public int getVarIndex(Node paramNode) {
    int i = paramNode.getIntProp(7, -1);
    int j = i;
    if (i == -1) {
      Node node;
      j = paramNode.getType();
      if (j == 55) {
        node = paramNode;
      } else if (j == 56 || j == 157) {
        node = paramNode.getFirstChild();
      } else {
        throw Kit.codeBug();
      } 
      j = this.fnode.getIndexForNameNode(node);
      if (j >= 0) {
        paramNode.putIntProp(7, j);
      } else {
        throw Kit.codeBug();
      } 
    } 
    return j;
  }
  
  public boolean isNumberVar(int paramInt) {
    paramInt -= this.fnode.getParamCount();
    if (paramInt >= 0) {
      boolean[] arrayOfBoolean = this.numberVarFlags;
      if (arrayOfBoolean != null)
        return arrayOfBoolean[paramInt]; 
    } 
    return false;
  }
  
  public boolean isParameter(int paramInt) {
    boolean bool;
    if (paramInt < this.fnode.getParamCount()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isTargetOfDirectCall() {
    boolean bool;
    if (this.directTargetIndex >= 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  void setDirectTargetIndex(int paramInt) {
    if (paramInt < 0 || this.directTargetIndex >= 0)
      Kit.codeBug(); 
    this.directTargetIndex = paramInt;
  }
  
  void setIsNumberVar(int paramInt) {
    paramInt -= this.fnode.getParamCount();
    if (paramInt < 0)
      Kit.codeBug(); 
    if (this.numberVarFlags == null)
      this.numberVarFlags = new boolean[this.fnode.getParamAndVarCount() - this.fnode.getParamCount()]; 
    this.numberVarFlags[paramInt] = true;
  }
  
  void setParameterNumberContext(boolean paramBoolean) {
    this.itsParameterNumberContext = paramBoolean;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/optimizer/OptFunctionNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */