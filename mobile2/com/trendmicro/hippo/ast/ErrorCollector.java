package com.trendmicro.hippo.ast;

import com.trendmicro.hippo.EvaluatorException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ErrorCollector implements IdeErrorReporter {
  private List<ParseProblem> errors = new ArrayList<>();
  
  public void error(String paramString1, String paramString2, int paramInt1, int paramInt2) {
    this.errors.add(new ParseProblem(ParseProblem.Type.Error, paramString1, paramString2, paramInt1, paramInt2));
  }
  
  public void error(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2) {
    throw new UnsupportedOperationException();
  }
  
  public List<ParseProblem> getErrors() {
    return this.errors;
  }
  
  public EvaluatorException runtimeError(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2) {
    throw new UnsupportedOperationException();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(this.errors.size() * 100);
    Iterator<ParseProblem> iterator = this.errors.iterator();
    while (iterator.hasNext()) {
      stringBuilder.append(((ParseProblem)iterator.next()).toString());
      stringBuilder.append("\n");
    } 
    return stringBuilder.toString();
  }
  
  public void warning(String paramString1, String paramString2, int paramInt1, int paramInt2) {
    this.errors.add(new ParseProblem(ParseProblem.Type.Warning, paramString1, paramString2, paramInt1, paramInt2));
  }
  
  public void warning(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2) {
    throw new UnsupportedOperationException();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ast/ErrorCollector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */