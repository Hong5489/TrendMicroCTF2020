package com.trendmicro.hippo.optimizer;

import com.trendmicro.hippo.Kit;
import com.trendmicro.hippo.Node;
import com.trendmicro.hippo.NodeTransformer;
import com.trendmicro.hippo.ObjArray;
import com.trendmicro.hippo.ast.ScriptNode;
import java.util.Map;

class OptTransformer extends NodeTransformer {
  private ObjArray directCallTargets;
  
  private Map<String, OptFunctionNode> possibleDirectCalls;
  
  OptTransformer(Map<String, OptFunctionNode> paramMap, ObjArray paramObjArray) {
    this.possibleDirectCalls = paramMap;
    this.directCallTargets = paramObjArray;
  }
  
  private void detectDirectCall(Node paramNode, ScriptNode paramScriptNode) {
    if (paramScriptNode.getType() == 110) {
      Node node1 = paramNode.getFirstChild();
      int i = 0;
      Node node2 = node1.getNext();
      while (node2 != null) {
        node2 = node2.getNext();
        i++;
      } 
      if (i == 0)
        (OptFunctionNode.get(paramScriptNode)).itsContainsCalls0 = true; 
      if (this.possibleDirectCalls != null) {
        String str;
        paramScriptNode = null;
        if (node1.getType() == 39) {
          str = node1.getString();
        } else if (node1.getType() == 33) {
          str = node1.getFirstChild().getNext().getString();
        } else if (node1.getType() == 34) {
          throw Kit.codeBug();
        } 
        if (str != null) {
          OptFunctionNode optFunctionNode = this.possibleDirectCalls.get(str);
          if (optFunctionNode != null && i == optFunctionNode.fnode.getParamCount() && !optFunctionNode.fnode.requiresActivation() && i <= 32) {
            paramNode.putProp(9, optFunctionNode);
            if (!optFunctionNode.isTargetOfDirectCall()) {
              i = this.directCallTargets.size();
              this.directCallTargets.add(optFunctionNode);
              optFunctionNode.setDirectTargetIndex(i);
            } 
          } 
        } 
      } 
    } 
  }
  
  protected void visitCall(Node paramNode, ScriptNode paramScriptNode) {
    detectDirectCall(paramNode, paramScriptNode);
    super.visitCall(paramNode, paramScriptNode);
  }
  
  protected void visitNew(Node paramNode, ScriptNode paramScriptNode) {
    detectDirectCall(paramNode, paramScriptNode);
    super.visitNew(paramNode, paramScriptNode);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/optimizer/OptTransformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */