package com.trendmicro.hippo.ast;

import com.trendmicro.hippo.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ScriptNode extends Scope {
  private List<FunctionNode> EMPTY_LIST = Collections.emptyList();
  
  private Object compilerData;
  
  private String encodedSource;
  
  private int encodedSourceEnd = -1;
  
  private int encodedSourceStart = -1;
  
  private int endLineno = -1;
  
  private List<FunctionNode> functions;
  
  private boolean inStrictMode;
  
  private boolean[] isConsts;
  
  private int paramCount = 0;
  
  private List<RegExpLiteral> regexps;
  
  private String sourceName;
  
  private List<Symbol> symbols = new ArrayList<>(4);
  
  private int tempNumber = 0;
  
  private String[] variableNames;
  
  public ScriptNode() {}
  
  public ScriptNode(int paramInt) {
    super(paramInt);
  }
  
  public int addFunction(FunctionNode paramFunctionNode) {
    if (paramFunctionNode == null)
      codeBug(); 
    if (this.functions == null)
      this.functions = new ArrayList<>(); 
    this.functions.add(paramFunctionNode);
    return this.functions.size() - 1;
  }
  
  public void addRegExp(RegExpLiteral paramRegExpLiteral) {
    if (paramRegExpLiteral == null)
      codeBug(); 
    if (this.regexps == null)
      this.regexps = new ArrayList<>(); 
    this.regexps.add(paramRegExpLiteral);
    paramRegExpLiteral.putIntProp(4, this.regexps.size() - 1);
  }
  
  void addSymbol(Symbol paramSymbol) {
    if (this.variableNames != null)
      codeBug(); 
    if (paramSymbol.getDeclType() == 88)
      this.paramCount++; 
    this.symbols.add(paramSymbol);
  }
  
  public void flattenSymbolTable(boolean paramBoolean) {
    if (!paramBoolean) {
      ArrayList<Symbol> arrayList = new ArrayList();
      if (this.symbolTable != null)
        for (byte b1 = 0; b1 < this.symbols.size(); b1++) {
          Symbol symbol = this.symbols.get(b1);
          if (symbol.getContainingTable() == this)
            arrayList.add(symbol); 
        }  
      this.symbols = arrayList;
    } 
    this.variableNames = new String[this.symbols.size()];
    this.isConsts = new boolean[this.symbols.size()];
    for (byte b = 0; b < this.symbols.size(); b++) {
      Symbol symbol = this.symbols.get(b);
      this.variableNames[b] = symbol.getName();
      boolean[] arrayOfBoolean = this.isConsts;
      if (symbol.getDeclType() == 155) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      } 
      arrayOfBoolean[b] = paramBoolean;
      symbol.setIndex(b);
    } 
  }
  
  public int getBaseLineno() {
    return this.lineno;
  }
  
  public Object getCompilerData() {
    return this.compilerData;
  }
  
  public String getEncodedSource() {
    return this.encodedSource;
  }
  
  public int getEncodedSourceEnd() {
    return this.encodedSourceEnd;
  }
  
  public int getEncodedSourceStart() {
    return this.encodedSourceStart;
  }
  
  public int getEndLineno() {
    return this.endLineno;
  }
  
  public int getFunctionCount() {
    int i;
    List<FunctionNode> list = this.functions;
    if (list == null) {
      i = 0;
    } else {
      i = list.size();
    } 
    return i;
  }
  
  public FunctionNode getFunctionNode(int paramInt) {
    return this.functions.get(paramInt);
  }
  
  public List<FunctionNode> getFunctions() {
    List<FunctionNode> list1 = this.functions;
    List<FunctionNode> list2 = list1;
    if (list1 == null)
      list2 = this.EMPTY_LIST; 
    return list2;
  }
  
  public int getIndexForNameNode(Node paramNode) {
    Symbol symbol;
    int i;
    if (this.variableNames == null)
      codeBug(); 
    Scope scope = paramNode.getScope();
    if (scope == null) {
      paramNode = null;
    } else {
      symbol = scope.getSymbol(((Name)paramNode).getIdentifier());
    } 
    if (symbol == null) {
      i = -1;
    } else {
      i = symbol.getIndex();
    } 
    return i;
  }
  
  public String getNextTempName() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("$");
    int i = this.tempNumber;
    this.tempNumber = i + 1;
    stringBuilder.append(i);
    return stringBuilder.toString();
  }
  
  public boolean[] getParamAndVarConst() {
    if (this.variableNames == null)
      codeBug(); 
    return this.isConsts;
  }
  
  public int getParamAndVarCount() {
    if (this.variableNames == null)
      codeBug(); 
    return this.symbols.size();
  }
  
  public String[] getParamAndVarNames() {
    if (this.variableNames == null)
      codeBug(); 
    return this.variableNames;
  }
  
  public int getParamCount() {
    return this.paramCount;
  }
  
  public String getParamOrVarName(int paramInt) {
    if (this.variableNames == null)
      codeBug(); 
    return this.variableNames[paramInt];
  }
  
  public int getRegexpCount() {
    int i;
    List<RegExpLiteral> list = this.regexps;
    if (list == null) {
      i = 0;
    } else {
      i = list.size();
    } 
    return i;
  }
  
  public String getRegexpFlags(int paramInt) {
    return ((RegExpLiteral)this.regexps.get(paramInt)).getFlags();
  }
  
  public String getRegexpString(int paramInt) {
    return ((RegExpLiteral)this.regexps.get(paramInt)).getValue();
  }
  
  public String getSourceName() {
    return this.sourceName;
  }
  
  public List<Symbol> getSymbols() {
    return this.symbols;
  }
  
  public boolean isInStrictMode() {
    return this.inStrictMode;
  }
  
  public void setBaseLineno(int paramInt) {
    if (paramInt < 0 || this.lineno >= 0)
      codeBug(); 
    this.lineno = paramInt;
  }
  
  public void setCompilerData(Object paramObject) {
    assertNotNull(paramObject);
    if (this.compilerData == null) {
      this.compilerData = paramObject;
      return;
    } 
    throw new IllegalStateException();
  }
  
  public void setEncodedSource(String paramString) {
    this.encodedSource = paramString;
  }
  
  public void setEncodedSourceBounds(int paramInt1, int paramInt2) {
    this.encodedSourceStart = paramInt1;
    this.encodedSourceEnd = paramInt2;
  }
  
  public void setEncodedSourceEnd(int paramInt) {
    this.encodedSourceEnd = paramInt;
  }
  
  public void setEncodedSourceStart(int paramInt) {
    this.encodedSourceStart = paramInt;
  }
  
  public void setEndLineno(int paramInt) {
    if (paramInt < 0 || this.endLineno >= 0)
      codeBug(); 
    this.endLineno = paramInt;
  }
  
  public void setInStrictMode(boolean paramBoolean) {
    this.inStrictMode = paramBoolean;
  }
  
  public void setSourceName(String paramString) {
    this.sourceName = paramString;
  }
  
  public void setSymbols(List<Symbol> paramList) {
    this.symbols = paramList;
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      Iterator<AstNode> iterator = iterator();
      while (iterator.hasNext())
        ((AstNode)iterator.next()).visit(paramNodeVisitor); 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/ScriptNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */