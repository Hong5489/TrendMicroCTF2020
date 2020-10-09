package com.trendmicro.hippo.ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XmlLiteral extends AstNode {
  private List<XmlFragment> fragments = new ArrayList<>();
  
  public XmlLiteral() {}
  
  public XmlLiteral(int paramInt) {
    super(paramInt);
  }
  
  public XmlLiteral(int paramInt1, int paramInt2) {
    super(paramInt1, paramInt2);
  }
  
  public void addFragment(XmlFragment paramXmlFragment) {
    assertNotNull(paramXmlFragment);
    this.fragments.add(paramXmlFragment);
    paramXmlFragment.setParent(this);
  }
  
  public List<XmlFragment> getFragments() {
    return this.fragments;
  }
  
  public void setFragments(List<XmlFragment> paramList) {
    assertNotNull(paramList);
    this.fragments.clear();
    Iterator<XmlFragment> iterator = paramList.iterator();
    while (iterator.hasNext())
      addFragment(iterator.next()); 
  }
  
  public String toSource(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder(250);
    Iterator<XmlFragment> iterator = this.fragments.iterator();
    while (iterator.hasNext())
      stringBuilder.append(((XmlFragment)iterator.next()).toSource(0)); 
    return stringBuilder.toString();
  }
  
  public void visit(NodeVisitor paramNodeVisitor) {
    if (paramNodeVisitor.visit(this)) {
      Iterator<XmlFragment> iterator = this.fragments.iterator();
      while (iterator.hasNext())
        ((XmlFragment)iterator.next()).visit(paramNodeVisitor); 
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/XmlLiteral.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */