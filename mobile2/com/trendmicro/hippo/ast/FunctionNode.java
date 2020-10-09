package com.trendmicro.hippo.ast;

import com.trendmicro.hippo.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FunctionNode extends ScriptNode {
  public static final int ARROW_FUNCTION = 4;
  
  public static final int FUNCTION_EXPRESSION = 2;
  
  public static final int FUNCTION_EXPRESSION_STATEMENT = 3;
  
  public static final int FUNCTION_STATEMENT = 1;
  
  private static final List<AstNode> NO_PARAMS = Collections.unmodifiableList(new ArrayList<>());
  
  private AstNode body;
  
  private Form functionForm = Form.FUNCTION;
  
  private Name functionName;
  
  private int functionType;
  
  private List<Node> generatorResumePoints;
  
  private boolean isExpressionClosure;
  
  private boolean isGenerator;
  
  private Map<Node, int[]> liveLocals;
  
  private int lp = -1;
  
  private AstNode memberExprNode;
  
  private boolean needsActivation;
  
  private List<AstNode> params;
  
  private int rp = -1;
  
  public FunctionNode() {}
  
  public FunctionNode(int paramInt) {
    super(paramInt);
  }
  
  public FunctionNode(int paramInt, Name paramName) {
    super(paramInt);
    setFunctionName(paramName);
  }
  
  public int addFunction(FunctionNode paramFunctionNode) {
    int i = super.addFunction(paramFunctionNode);
    if (getFunctionCount() > 0)
      this.needsActivation = true; 
    return i;
  }
  
  public void addLiveLocals(Node paramNode, int[] paramArrayOfint) {
    if (this.liveLocals == null)
      this.liveLocals = (Map)new HashMap<>(); 
    this.liveLocals.put(paramNode, paramArrayOfint);
  }
  
  public void addParam(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    if (this.params == null)
      this.params = new ArrayList<>(); 
    this.params.add(paramAstNode);
    paramAstNode.setParent(this);
  }
  
  public void addResumptionPoint(Node paramNode) {
    if (this.generatorResumePoints == null)
      this.generatorResumePoints = new ArrayList<>(); 
    this.generatorResumePoints.add(paramNode);
  }
  
  public AstNode getBody() {
    return this.body;
  }
  
  public Name getFunctionName() {
    return this.functionName;
  }
  
  public int getFunctionType() {
    return this.functionType;
  }
  
  public Map<Node, int[]> getLiveLocals() {
    return this.liveLocals;
  }
  
  public int getLp() {
    return this.lp;
  }
  
  public AstNode getMemberExprNode() {
    return this.memberExprNode;
  }
  
  public String getName() {
    String str;
    Name name = this.functionName;
    if (name != null) {
      str = name.getIdentifier();
    } else {
      str = "";
    } 
    return str;
  }
  
  public List<AstNode> getParams() {
    List<AstNode> list = this.params;
    if (list == null)
      list = NO_PARAMS; 
    return list;
  }
  
  public List<Node> getResumptionPoints() {
    return this.generatorResumePoints;
  }
  
  public int getRp() {
    return this.rp;
  }
  
  public boolean isExpressionClosure() {
    return this.isExpressionClosure;
  }
  
  public boolean isGenerator() {
    return this.isGenerator;
  }
  
  public boolean isGetterMethod() {
    boolean bool;
    if (this.functionForm == Form.GETTER) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isMethod() {
    return (this.functionForm == Form.GETTER || this.functionForm == Form.SETTER || this.functionForm == Form.METHOD);
  }
  
  public boolean isNormalMethod() {
    boolean bool;
    if (this.functionForm == Form.METHOD) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isParam(AstNode paramAstNode) {
    boolean bool;
    List<AstNode> list = this.params;
    if (list == null) {
      bool = false;
    } else {
      bool = list.contains(paramAstNode);
    } 
    return bool;
  }
  
  public boolean isSetterMethod() {
    boolean bool;
    if (this.functionForm == Form.SETTER) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean requiresActivation() {
    return this.needsActivation;
  }
  
  public void setBody(AstNode paramAstNode) {
    assertNotNull(paramAstNode);
    this.body = paramAstNode;
    if (Boolean.TRUE.equals(paramAstNode.getProp(25)))
      setIsExpressionClosure(true); 
    int i = paramAstNode.getPosition() + paramAstNode.getLength();
    paramAstNode.setParent(this);
    setLength(i - this.position);
    setEncodedSourceBounds(this.position, i);
  }
  
  public void setFunctionIsGetterMethod() {
    this.functionForm = Form.GETTER;
  }
  
  public void setFunctionIsNormalMethod() {
    this.functionForm = Form.METHOD;
  }
  
  public void setFunctionIsSetterMethod() {
    this.functionForm = Form.SETTER;
  }
  
  public void setFunctionName(Name paramName) {
    this.functionName = paramName;
    if (paramName != null)
      paramName.setParent(this); 
  }
  
  public void setFunctionType(int paramInt) {
    this.functionType = paramInt;
  }
  
  public void setIsExpressionClosure(boolean paramBoolean) {
    this.isExpressionClosure = paramBoolean;
  }
  
  public void setIsGenerator() {
    this.isGenerator = true;
  }
  
  public void setLp(int paramInt) {
    this.lp = paramInt;
  }
  
  public void setMemberExprNode(AstNode paramAstNode) {
    this.memberExprNode = paramAstNode;
    if (paramAstNode != null)
      paramAstNode.setParent(this); 
  }
  
  public void setParams(List<AstNode> paramList) {
    if (paramList == null) {
      this.params = null;
    } else {
      List<AstNode> list = this.params;
      if (list != null)
        list.clear(); 
      Iterator<AstNode> iterator = paramList.iterator();
      while (iterator.hasNext())
        addParam(iterator.next()); 
    } 
  }
  
  public void setParens(int paramInt1, int paramInt2) {
    this.lp = paramInt1;
    this.rp = paramInt2;
  }
  
  public void setRequiresActivation() {
    this.needsActivation = true;
  }
  
  public void setRp(int paramInt) {
    this.rp = paramInt;
  }
  
  public String toSource(int paramInt) {
    boolean bool;
    StringBuilder stringBuilder = new StringBuilder();
    if (this.functionType == 4) {
      bool = true;
    } else {
      bool = false;
    } 
    if (!isMethod()) {
      stringBuilder.append(makeIndent(paramInt));
      if (!bool)
        stringBuilder.append("function"); 
    } 
    if (this.functionName != null) {
      stringBuilder.append(" ");
      stringBuilder.append(this.functionName.toSource(0));
    } 
    List<AstNode> list = this.params;
    if (list == null) {
      stringBuilder.append("() ");
    } else if (bool && this.lp == -1) {
      printList(list, stringBuilder);
      stringBuilder.append(" ");
    } else {
      stringBuilder.append("(");
      printList(this.params, stringBuilder);
      stringBuilder.append(") ");
    } 
    if (bool)
      stringBuilder.append("=> "); 
    if (this.isExpressionClosure) {
      AstNode astNode = getBody();
      if (astNode.getLastChild() instanceof ReturnStatement) {
        stringBuilder.append(((ReturnStatement)astNode.getLastChild()).getReturnValue().toSource(0));
        if (this.functionType == 1)
          stringBuilder.append(";"); 
      } else {
        stringBuilder.append(" ");
        stringBuilder.append(astNode.toSource(0));
      } 
    } else {
      stringBuilder.append(getBody().toSource(paramInt).trim());
    } 
    if (this.functionType == 1 || isMethod())
      stringBuilder.append("\n"); 
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      Name name = this.functionName;
      if (name != null)
        name.visit(paramNodeVisitor); 
      Iterator<AstNode> iterator = getParams().iterator();
      while (iterator.hasNext())
        ((AstNode)iterator.next()).visit(paramNodeVisitor); 
      getBody().visit(paramNodeVisitor);
      if (!this.isExpressionClosure) {
        AstNode astNode = this.memberExprNode;
        if (astNode != null)
          astNode.visit(paramNodeVisitor); 
      } 
    } 
  }
  
  public enum Form {
    FUNCTION, GETTER, METHOD, SETTER;
    
    static {
      Form form = new Form("METHOD", 3);
      METHOD = form;
      $VALUES = new Form[] { FUNCTION, GETTER, SETTER, form };
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/FunctionNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */