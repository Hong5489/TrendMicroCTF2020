package com.trendmicro.hippo.ast;

import com.trendmicro.hippo.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Scope extends Jump {
  private List<Scope> childScopes;
  
  protected Scope parentScope;
  
  protected Map<String, Symbol> symbolTable;
  
  protected ScriptNode top;
  
  public Scope() {}
  
  public Scope(int paramInt) {
    this.position = paramInt;
  }
  
  public Scope(int paramInt1, int paramInt2) {
    this(paramInt1);
    this.length = paramInt2;
  }
  
  private Map<String, Symbol> ensureSymbolTable() {
    if (this.symbolTable == null)
      this.symbolTable = new LinkedHashMap<>(5); 
    return this.symbolTable;
  }
  
  public static void joinScopes(Scope paramScope1, Scope paramScope2) {
    Map<String, Symbol> map2 = paramScope1.ensureSymbolTable();
    Map<String, Symbol> map1 = paramScope2.ensureSymbolTable();
    if (!Collections.disjoint(map2.keySet(), map1.keySet()))
      codeBug(); 
    for (Map.Entry<String, Symbol> entry : map2.entrySet()) {
      Symbol symbol = (Symbol)entry.getValue();
      symbol.setContainingTable(paramScope2);
      map1.put((String)entry.getKey(), symbol);
    } 
  }
  
  public static Scope splitScope(Scope paramScope) {
    Scope scope = new Scope(paramScope.getType());
    scope.symbolTable = paramScope.symbolTable;
    paramScope.symbolTable = null;
    scope.parent = paramScope.parent;
    scope.setParentScope(paramScope.getParentScope());
    scope.setParentScope(scope);
    paramScope.parent = scope;
    scope.top = paramScope.top;
    return scope;
  }
  
  public void addChildScope(Scope paramScope) {
    if (this.childScopes == null)
      this.childScopes = new ArrayList<>(); 
    this.childScopes.add(paramScope);
    paramScope.setParentScope(this);
  }
  
  public void clearParentScope() {
    this.parentScope = null;
  }
  
  public List<Scope> getChildScopes() {
    return this.childScopes;
  }
  
  public Scope getDefiningScope(String paramString) {
    for (Scope scope = this; scope != null; scope = scope.parentScope) {
      Map<String, Symbol> map = scope.getSymbolTable();
      if (map != null && map.containsKey(paramString))
        return scope; 
    } 
    return null;
  }
  
  public Scope getParentScope() {
    return this.parentScope;
  }
  
  public List<AstNode> getStatements() {
    ArrayList<AstNode> arrayList = new ArrayList();
    for (Node node = getFirstChild(); node != null; node = node.getNext())
      arrayList.add((AstNode)node); 
    return arrayList;
  }
  
  public Symbol getSymbol(String paramString) {
    Symbol symbol;
    Map<String, Symbol> map = this.symbolTable;
    if (map == null) {
      paramString = null;
    } else {
      symbol = map.get(paramString);
    } 
    return symbol;
  }
  
  public Map<String, Symbol> getSymbolTable() {
    return this.symbolTable;
  }
  
  public ScriptNode getTop() {
    return this.top;
  }
  
  public void putSymbol(Symbol paramSymbol) {
    if (paramSymbol.getName() != null) {
      ensureSymbolTable();
      this.symbolTable.put(paramSymbol.getName(), paramSymbol);
      paramSymbol.setContainingTable(this);
      this.top.addSymbol(paramSymbol);
      return;
    } 
    throw new IllegalArgumentException("null symbol name");
  }
  
  public void replaceWith(Scope paramScope) {
    List<Scope> list = this.childScopes;
    if (list != null) {
      Iterator<Scope> iterator = list.iterator();
      while (iterator.hasNext())
        paramScope.addChildScope(iterator.next()); 
      this.childScopes.clear();
      this.childScopes = null;
    } 
    Map<String, Symbol> map = this.symbolTable;
    if (map != null && !map.isEmpty())
      joinScopes(this, paramScope); 
  }
  
  public void setParentScope(Scope paramScope) {
    this.parentScope = paramScope;
    if (paramScope == null) {
      paramScope = this;
    } else {
      paramScope = paramScope.top;
    } 
    this.top = (ScriptNode)paramScope;
  }
  
  public void setSymbolTable(Map<String, Symbol> paramMap) {
    this.symbolTable = paramMap;
  }
  
  public void setTop(ScriptNode paramScriptNode) {
    this.top = paramScriptNode;
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append("{\n");
    for (AstNode astNode : this) {
      stringBuilder.append(astNode.toSource(paramInt + 1));
      if (astNode.getType() == 162)
        stringBuilder.append("\n"); 
    } 
    stringBuilder.append(makeIndent(paramInt));
    stringBuilder.append("}\n");
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      Iterator<AstNode> iterator = iterator();
      while (iterator.hasNext())
        ((AstNode)iterator.next()).visit(paramNodeVisitor); 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/Scope.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */