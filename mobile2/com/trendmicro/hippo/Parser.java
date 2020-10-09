package com.trendmicro.hippo;

import com.trendmicro.hippo.ast.ArrayComprehension;
import com.trendmicro.hippo.ast.ArrayComprehensionLoop;
import com.trendmicro.hippo.ast.ArrayLiteral;
import com.trendmicro.hippo.ast.Assignment;
import com.trendmicro.hippo.ast.AstNode;
import com.trendmicro.hippo.ast.AstRoot;
import com.trendmicro.hippo.ast.Block;
import com.trendmicro.hippo.ast.BreakStatement;
import com.trendmicro.hippo.ast.CatchClause;
import com.trendmicro.hippo.ast.Comment;
import com.trendmicro.hippo.ast.ConditionalExpression;
import com.trendmicro.hippo.ast.ContinueStatement;
import com.trendmicro.hippo.ast.DestructuringForm;
import com.trendmicro.hippo.ast.DoLoop;
import com.trendmicro.hippo.ast.ElementGet;
import com.trendmicro.hippo.ast.EmptyExpression;
import com.trendmicro.hippo.ast.EmptyStatement;
import com.trendmicro.hippo.ast.ErrorNode;
import com.trendmicro.hippo.ast.ExpressionStatement;
import com.trendmicro.hippo.ast.FunctionCall;
import com.trendmicro.hippo.ast.FunctionNode;
import com.trendmicro.hippo.ast.GeneratorExpression;
import com.trendmicro.hippo.ast.GeneratorExpressionLoop;
import com.trendmicro.hippo.ast.IdeErrorReporter;
import com.trendmicro.hippo.ast.IfStatement;
import com.trendmicro.hippo.ast.InfixExpression;
import com.trendmicro.hippo.ast.Jump;
import com.trendmicro.hippo.ast.KeywordLiteral;
import com.trendmicro.hippo.ast.Label;
import com.trendmicro.hippo.ast.LabeledStatement;
import com.trendmicro.hippo.ast.LetNode;
import com.trendmicro.hippo.ast.Loop;
import com.trendmicro.hippo.ast.Name;
import com.trendmicro.hippo.ast.NewExpression;
import com.trendmicro.hippo.ast.NumberLiteral;
import com.trendmicro.hippo.ast.ObjectLiteral;
import com.trendmicro.hippo.ast.ObjectProperty;
import com.trendmicro.hippo.ast.ParenthesizedExpression;
import com.trendmicro.hippo.ast.PropertyGet;
import com.trendmicro.hippo.ast.RegExpLiteral;
import com.trendmicro.hippo.ast.ReturnStatement;
import com.trendmicro.hippo.ast.Scope;
import com.trendmicro.hippo.ast.ScriptNode;
import com.trendmicro.hippo.ast.StringLiteral;
import com.trendmicro.hippo.ast.SwitchCase;
import com.trendmicro.hippo.ast.SwitchStatement;
import com.trendmicro.hippo.ast.Symbol;
import com.trendmicro.hippo.ast.ThrowStatement;
import com.trendmicro.hippo.ast.TryStatement;
import com.trendmicro.hippo.ast.UnaryExpression;
import com.trendmicro.hippo.ast.VariableDeclaration;
import com.trendmicro.hippo.ast.VariableInitializer;
import com.trendmicro.hippo.ast.WhileLoop;
import com.trendmicro.hippo.ast.WithStatement;
import com.trendmicro.hippo.ast.XmlDotQuery;
import com.trendmicro.hippo.ast.XmlElemRef;
import com.trendmicro.hippo.ast.XmlExpression;
import com.trendmicro.hippo.ast.XmlFragment;
import com.trendmicro.hippo.ast.XmlLiteral;
import com.trendmicro.hippo.ast.XmlMemberGet;
import com.trendmicro.hippo.ast.XmlPropRef;
import com.trendmicro.hippo.ast.XmlString;
import com.trendmicro.hippo.ast.Yield;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Parser {
  public static final int ARGC_LIMIT = 65536;
  
  static final int CLEAR_TI_MASK = 65535;
  
  private static final int GET_ENTRY = 2;
  
  private static final int METHOD_ENTRY = 8;
  
  private static final int PROP_ENTRY = 1;
  
  private static final int SET_ENTRY = 4;
  
  static final int TI_AFTER_EOL = 65536;
  
  static final int TI_CHECK_LABEL = 131072;
  
  boolean calledByCompileFunction;
  
  CompilerEnvirons compilerEnv;
  
  private int currentFlaggedToken = 0;
  
  private Comment currentJsDocComment;
  
  private LabeledStatement currentLabel;
  
  Scope currentScope;
  
  ScriptNode currentScriptOrFn;
  
  private int currentToken;
  
  private boolean defaultUseStrictDirective;
  
  private int endFlags;
  
  private IdeErrorReporter errorCollector;
  
  private ErrorReporter errorReporter;
  
  private boolean inDestructuringAssignment;
  
  private boolean inForInit;
  
  protected boolean inUseStrictDirective;
  
  private Map<String, LabeledStatement> labelSet;
  
  private List<Jump> loopAndSwitchSet;
  
  private List<Loop> loopSet;
  
  protected int nestingOfFunction;
  
  private boolean parseFinished;
  
  private int prevNameTokenLineno;
  
  private int prevNameTokenStart;
  
  private String prevNameTokenString = "";
  
  private List<Comment> scannedComments;
  
  private char[] sourceChars;
  
  private String sourceURI;
  
  private int syntaxErrorCount;
  
  private TokenStream ts;
  
  public Parser() {
    this(new CompilerEnvirons());
  }
  
  public Parser(CompilerEnvirons paramCompilerEnvirons) {
    this(paramCompilerEnvirons, paramCompilerEnvirons.getErrorReporter());
  }
  
  public Parser(CompilerEnvirons paramCompilerEnvirons, ErrorReporter paramErrorReporter) {
    this.compilerEnv = paramCompilerEnvirons;
    this.errorReporter = paramErrorReporter;
    if (paramErrorReporter instanceof IdeErrorReporter)
      this.errorCollector = (IdeErrorReporter)paramErrorReporter; 
  }
  
  private void addError(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, String paramString3, int paramInt4) {
    this.syntaxErrorCount++;
    paramString1 = lookupMessage(paramString1, paramString2);
    IdeErrorReporter ideErrorReporter = this.errorCollector;
    if (ideErrorReporter != null) {
      ideErrorReporter.error(paramString1, this.sourceURI, paramInt1, paramInt2);
    } else {
      this.errorReporter.error(paramString1, this.sourceURI, paramInt3, paramString3, paramInt4);
    } 
  }
  
  private AstNode addExpr() throws IOException {
    AstNode astNode = mulExpr();
    while (true) {
      InfixExpression infixExpression;
      int i = peekToken();
      int j = this.ts.tokenBeg;
      if (i == 21 || i == 22) {
        consumeToken();
        infixExpression = new InfixExpression(i, astNode, mulExpr(), j);
        continue;
      } 
      return (AstNode)infixExpression;
    } 
  }
  
  private void addStrictWarning(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, String paramString3, int paramInt4) {
    if (this.compilerEnv.isStrictMode())
      addWarning(paramString1, paramString2, paramInt1, paramInt2, paramInt3, paramString3, paramInt4); 
  }
  
  private void addWarning(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, String paramString3, int paramInt4) {
    String str = lookupMessage(paramString1, paramString2);
    if (this.compilerEnv.reportWarningAsError()) {
      addError(paramString1, paramString2, paramInt1, paramInt2, paramInt3, paramString3, paramInt4);
    } else {
      IdeErrorReporter ideErrorReporter = this.errorCollector;
      if (ideErrorReporter != null) {
        ideErrorReporter.warning(str, this.sourceURI, paramInt1, paramInt2);
      } else {
        this.errorReporter.warning(str, this.sourceURI, paramInt3, paramString3, paramInt4);
      } 
    } 
  }
  
  private AstNode andExpr() throws IOException {
    InfixExpression infixExpression;
    AstNode astNode1 = bitOrExpr();
    AstNode astNode2 = astNode1;
    if (matchToken(106, true)) {
      int i = this.ts.tokenBeg;
      infixExpression = new InfixExpression(106, astNode1, andExpr(), i);
    } 
    return (AstNode)infixExpression;
  }
  
  private List<AstNode> argumentList() throws IOException {
    if (matchToken(89, true))
      return null; 
    null = new ArrayList();
    boolean bool = this.inForInit;
    this.inForInit = false;
    try {
      while (peekToken() != 89) {
        if (peekToken() == 73)
          reportError("msg.yield.parenthesized"); 
        AstNode astNode = assignExpr();
        int i = peekToken();
        if (i == 120) {
          try {
            null.add(generatorExpression(astNode, 0, true));
          } catch (IOException iOException) {}
        } else {
          null.add(iOException);
        } 
        boolean bool1 = matchToken(90, true);
        if (!bool1)
          break; 
      } 
      this.inForInit = bool;
      return null;
    } finally {
      this.inForInit = bool;
    } 
  }
  
  private AstNode arrayComprehension(AstNode paramAstNode, int paramInt) throws IOException {
    ArrayList<ArrayComprehensionLoop> arrayList = new ArrayList();
    while (peekToken() == 120)
      arrayList.add(arrayComprehensionLoop()); 
    int i = -1;
    ConditionData conditionData = null;
    if (peekToken() == 113) {
      consumeToken();
      i = this.ts.tokenBeg - paramInt;
      conditionData = condition();
    } 
    mustMatchToken(85, "msg.no.bracket.arg", true);
    ArrayComprehension arrayComprehension = new ArrayComprehension(paramInt, this.ts.tokenEnd - paramInt);
    arrayComprehension.setResult(paramAstNode);
    arrayComprehension.setLoops(arrayList);
    if (conditionData != null) {
      arrayComprehension.setIfPosition(i);
      arrayComprehension.setFilter(conditionData.condition);
      arrayComprehension.setFilterLp(conditionData.lp - paramInt);
      arrayComprehension.setFilterRp(conditionData.rp - paramInt);
    } 
    return (AstNode)arrayComprehension;
  }
  
  private ArrayComprehensionLoop arrayComprehensionLoop() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial nextToken : ()I
    //   4: bipush #120
    //   6: if_icmpeq -> 14
    //   9: aload_0
    //   10: invokespecial codeBug : ()Ljava/lang/RuntimeException;
    //   13: pop
    //   14: aload_0
    //   15: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   18: getfield tokenBeg : I
    //   21: istore_1
    //   22: iconst_m1
    //   23: istore_2
    //   24: iconst_m1
    //   25: istore_3
    //   26: iconst_m1
    //   27: istore #4
    //   29: iconst_m1
    //   30: istore #5
    //   32: iconst_0
    //   33: istore #6
    //   35: new com/trendmicro/hippo/ast/ArrayComprehensionLoop
    //   38: dup
    //   39: iload_1
    //   40: invokespecial <init> : (I)V
    //   43: astore #7
    //   45: aload_0
    //   46: aload #7
    //   48: invokevirtual pushScope : (Lcom/trendmicro/hippo/ast/Scope;)V
    //   51: iconst_1
    //   52: istore #8
    //   54: aload_0
    //   55: bipush #39
    //   57: iconst_1
    //   58: invokespecial matchToken : (IZ)Z
    //   61: istore #9
    //   63: iload_2
    //   64: istore #10
    //   66: iload #9
    //   68: ifeq -> 111
    //   71: aload_0
    //   72: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   75: invokevirtual getString : ()Ljava/lang/String;
    //   78: ldc_w 'each'
    //   81: invokevirtual equals : (Ljava/lang/Object;)Z
    //   84: ifeq -> 101
    //   87: aload_0
    //   88: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   91: getfield tokenBeg : I
    //   94: iload_1
    //   95: isub
    //   96: istore #10
    //   98: goto -> 111
    //   101: aload_0
    //   102: ldc_w 'msg.no.paren.for'
    //   105: invokevirtual reportError : (Ljava/lang/String;)V
    //   108: iload_2
    //   109: istore #10
    //   111: aload_0
    //   112: bipush #88
    //   114: ldc_w 'msg.no.paren.for'
    //   117: iconst_1
    //   118: invokespecial mustMatchToken : (ILjava/lang/String;Z)Z
    //   121: ifeq -> 134
    //   124: aload_0
    //   125: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   128: getfield tokenBeg : I
    //   131: iload_1
    //   132: isub
    //   133: istore_3
    //   134: aconst_null
    //   135: astore #11
    //   137: aload_0
    //   138: invokespecial peekToken : ()I
    //   141: istore_2
    //   142: iload_2
    //   143: bipush #39
    //   145: if_icmpeq -> 185
    //   148: iload_2
    //   149: bipush #84
    //   151: if_icmpeq -> 170
    //   154: iload_2
    //   155: bipush #86
    //   157: if_icmpeq -> 170
    //   160: aload_0
    //   161: ldc_w 'msg.bad.var'
    //   164: invokevirtual reportError : (Ljava/lang/String;)V
    //   167: goto -> 195
    //   170: aload_0
    //   171: invokespecial destructuringPrimaryExpr : ()Lcom/trendmicro/hippo/ast/AstNode;
    //   174: astore #11
    //   176: aload_0
    //   177: aload #11
    //   179: invokevirtual markDestructuring : (Lcom/trendmicro/hippo/ast/AstNode;)V
    //   182: goto -> 195
    //   185: aload_0
    //   186: invokespecial consumeToken : ()V
    //   189: aload_0
    //   190: invokespecial createNameNode : ()Lcom/trendmicro/hippo/ast/Name;
    //   193: astore #11
    //   195: aload #11
    //   197: invokevirtual getType : ()I
    //   200: bipush #39
    //   202: if_icmpne -> 220
    //   205: aload_0
    //   206: sipush #154
    //   209: aload_0
    //   210: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   213: invokevirtual getString : ()Ljava/lang/String;
    //   216: iconst_1
    //   217: invokevirtual defineSymbol : (ILjava/lang/String;Z)V
    //   220: aload_0
    //   221: invokespecial nextToken : ()I
    //   224: istore_2
    //   225: iload_2
    //   226: bipush #39
    //   228: if_icmpeq -> 254
    //   231: iload_2
    //   232: bipush #52
    //   234: if_icmpeq -> 240
    //   237: goto -> 300
    //   240: aload_0
    //   241: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   244: getfield tokenBeg : I
    //   247: iload_1
    //   248: isub
    //   249: istore #5
    //   251: goto -> 307
    //   254: ldc_w 'of'
    //   257: aload_0
    //   258: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   261: invokevirtual getString : ()Ljava/lang/String;
    //   264: invokevirtual equals : (Ljava/lang/Object;)Z
    //   267: ifeq -> 300
    //   270: iload #10
    //   272: iconst_m1
    //   273: if_icmpeq -> 283
    //   276: aload_0
    //   277: ldc_w 'msg.invalid.for.each'
    //   280: invokevirtual reportError : (Ljava/lang/String;)V
    //   283: aload_0
    //   284: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   287: getfield tokenBeg : I
    //   290: iload_1
    //   291: isub
    //   292: istore #5
    //   294: iconst_1
    //   295: istore #6
    //   297: goto -> 307
    //   300: aload_0
    //   301: ldc_w 'msg.in.after.for.name'
    //   304: invokevirtual reportError : (Ljava/lang/String;)V
    //   307: aload_0
    //   308: invokespecial expr : ()Lcom/trendmicro/hippo/ast/AstNode;
    //   311: astore #12
    //   313: aload_0
    //   314: bipush #89
    //   316: ldc_w 'msg.no.paren.for.ctrl'
    //   319: iconst_1
    //   320: invokespecial mustMatchToken : (ILjava/lang/String;Z)Z
    //   323: ifeq -> 337
    //   326: aload_0
    //   327: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   330: getfield tokenBeg : I
    //   333: iload_1
    //   334: isub
    //   335: istore #4
    //   337: aload #7
    //   339: aload_0
    //   340: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   343: getfield tokenEnd : I
    //   346: iload_1
    //   347: isub
    //   348: invokevirtual setLength : (I)V
    //   351: aload #7
    //   353: aload #11
    //   355: invokevirtual setIterator : (Lcom/trendmicro/hippo/ast/AstNode;)V
    //   358: aload #7
    //   360: aload #12
    //   362: invokevirtual setIteratedObject : (Lcom/trendmicro/hippo/ast/AstNode;)V
    //   365: aload #7
    //   367: iload #5
    //   369: invokevirtual setInPosition : (I)V
    //   372: aload #7
    //   374: iload #10
    //   376: invokevirtual setEachPosition : (I)V
    //   379: iload #10
    //   381: iconst_m1
    //   382: if_icmpeq -> 388
    //   385: goto -> 391
    //   388: iconst_0
    //   389: istore #8
    //   391: aload #7
    //   393: iload #8
    //   395: invokevirtual setIsForEach : (Z)V
    //   398: aload #7
    //   400: iload_3
    //   401: iload #4
    //   403: invokevirtual setParens : (II)V
    //   406: aload #7
    //   408: iload #6
    //   410: invokevirtual setIsForOf : (Z)V
    //   413: aload_0
    //   414: invokevirtual popScope : ()V
    //   417: aload #7
    //   419: areturn
    //   420: astore #11
    //   422: aload_0
    //   423: invokevirtual popScope : ()V
    //   426: aload #11
    //   428: athrow
    // Exception table:
    //   from	to	target	type
    //   54	63	420	finally
    //   71	98	420	finally
    //   101	108	420	finally
    //   111	134	420	finally
    //   137	142	420	finally
    //   160	167	420	finally
    //   170	182	420	finally
    //   185	195	420	finally
    //   195	220	420	finally
    //   220	225	420	finally
    //   240	251	420	finally
    //   254	270	420	finally
    //   276	283	420	finally
    //   283	294	420	finally
    //   300	307	420	finally
    //   307	313	420	finally
    //   313	337	420	finally
    //   337	379	420	finally
    //   391	413	420	finally
  }
  
  private AstNode arrayLiteral() throws IOException {
    if (this.currentToken != 84)
      codeBug(); 
    int i = this.ts.tokenBeg;
    int j = this.ts.tokenEnd;
    ArrayList<EmptyExpression> arrayList = new ArrayList();
    ArrayLiteral arrayLiteral = new ArrayLiteral(i);
    int k = 1;
    int m = -1;
    byte b = 0;
    while (true) {
      int n = peekToken();
      if (n == 90) {
        consumeToken();
        m = this.ts.tokenEnd;
        if (!k) {
          k = 1;
          continue;
        } 
        arrayList.add(new EmptyExpression(this.ts.tokenBeg, 1));
        b++;
        continue;
      } 
      if (n == 85) {
        consumeToken();
        j = this.ts.tokenEnd;
        arrayLiteral.setDestructuringLength(arrayList.size() + k);
        arrayLiteral.setSkipCount(b);
        k = j;
        if (m != -1) {
          warnTrailingComma(i, arrayList, m);
          k = j;
        } 
      } else {
        if (n == 120 && k == 0 && arrayList.size() == 1)
          return arrayComprehension((AstNode)arrayList.get(0), i); 
        if (n == 0) {
          reportError("msg.no.bracket.arg");
          k = j;
        } else {
          if (k == 0)
            reportError("msg.no.bracket.arg"); 
          arrayList.add(assignExpr());
          k = 0;
          m = -1;
          continue;
        } 
      } 
      Iterator<EmptyExpression> iterator = arrayList.iterator();
      while (iterator.hasNext())
        arrayLiteral.addElement((AstNode)iterator.next()); 
      arrayLiteral.setLength(k - i);
      return (AstNode)arrayLiteral;
    } 
  }
  
  private AstNode arrowFunction(AstNode paramAstNode) throws IOException {
    byte b;
    int i = this.ts.lineno;
    if (paramAstNode != null) {
      b = paramAstNode.getPosition();
    } else {
      b = -1;
    } 
    FunctionNode functionNode = new FunctionNode(b);
    functionNode.setFunctionType(4);
    functionNode.setJsDocNode(getAndResetJsDoc());
    HashMap<Object, Object> hashMap = new HashMap<>();
    HashSet<String> hashSet = new HashSet();
    PerFunctionVariables perFunctionVariables = new PerFunctionVariables(functionNode);
    try {
      if (paramAstNode instanceof ParenthesizedExpression) {
        functionNode.setParens(0, paramAstNode.getLength());
        paramAstNode = ((ParenthesizedExpression)paramAstNode).getExpression();
        if (!(paramAstNode instanceof EmptyExpression))
          arrowFunctionParams(functionNode, paramAstNode, (Map)hashMap, hashSet); 
      } else {
        arrowFunctionParams(functionNode, paramAstNode, (Map)hashMap, hashSet);
      } 
      if (!hashMap.isEmpty()) {
        Node node = new Node();
        this(90);
        for (Map.Entry<Object, Object> entry : hashMap.entrySet())
          node.addChildToBack(createDestructuringAssignment(123, (Node)entry.getValue(), createName((String)entry.getKey()))); 
        functionNode.putProp(23, node);
      } 
      functionNode.setBody(parseFunctionBody(4, functionNode));
      functionNode.setEncodedSourceBounds(b, this.ts.tokenEnd);
      functionNode.setLength(this.ts.tokenEnd - b);
      perFunctionVariables.restore();
      if (functionNode.isGenerator())
        return (AstNode)makeErrorNode(); 
      functionNode.setSourceName(this.sourceURI);
      functionNode.setBaseLineno(i);
      return (AstNode)functionNode;
    } finally {
      perFunctionVariables.restore();
    } 
  }
  
  private void arrowFunctionParams(FunctionNode paramFunctionNode, AstNode paramAstNode, Map<String, Node> paramMap, Set<String> paramSet) {
    String str;
    if (paramAstNode instanceof ArrayLiteral || paramAstNode instanceof ObjectLiteral) {
      markDestructuring(paramAstNode);
      paramFunctionNode.addParam(paramAstNode);
      str = this.currentScriptOrFn.getNextTempName();
      defineSymbol(88, str, false);
      paramMap.put(str, paramAstNode);
      return;
    } 
    if (paramAstNode instanceof InfixExpression && paramAstNode.getType() == 90) {
      arrowFunctionParams((FunctionNode)str, ((InfixExpression)paramAstNode).getLeft(), paramMap, paramSet);
      arrowFunctionParams((FunctionNode)str, ((InfixExpression)paramAstNode).getRight(), paramMap, paramSet);
    } else if (paramAstNode instanceof Name) {
      str.addParam(paramAstNode);
      str = ((Name)paramAstNode).getIdentifier();
      defineSymbol(88, str);
      if (this.inUseStrictDirective) {
        if ("eval".equals(str) || "arguments".equals(str))
          reportError("msg.bad.id.strict", str); 
        if (paramSet.contains(str))
          addError("msg.dup.param.strict", str); 
        paramSet.add(str);
      } 
    } else {
      reportError("msg.no.parm", paramAstNode.getPosition(), paramAstNode.getLength());
      str.addParam((AstNode)makeErrorNode());
    } 
  }
  
  private AstNode assignExpr() throws IOException {
    AstNode astNode2;
    int i = peekToken();
    if (i == 73)
      return returnOrYield(i, true); 
    AstNode astNode1 = condExpr();
    int j = 0;
    int k = peekTokenOrEOL();
    i = k;
    if (k == 1) {
      j = 1;
      i = peekToken();
    } 
    if (91 <= i && i <= 102) {
      if (this.inDestructuringAssignment)
        reportError("msg.destruct.default.vals"); 
      consumeToken();
      Comment comment = getAndResetJsDoc();
      markDestructuring(astNode1);
      j = this.ts.tokenBeg;
      Assignment assignment = new Assignment(i, astNode1, assignExpr(), j);
      if (comment != null)
        assignment.setJsDocNode(comment); 
    } else if (i == 83) {
      astNode2 = astNode1;
      if (this.currentJsDocComment != null) {
        astNode1.setJsDocNode(getAndResetJsDoc());
        astNode2 = astNode1;
      } 
    } else {
      astNode2 = astNode1;
      if (j == 0) {
        astNode2 = astNode1;
        if (i == 165) {
          consumeToken();
          astNode2 = arrowFunction(astNode1);
        } 
      } 
    } 
    return astNode2;
  }
  
  private AstNode attributeAccess() throws IOException {
    int i = nextToken();
    int j = this.ts.tokenBeg;
    if (i != 23) {
      if (i != 39) {
        if (i != 84) {
          reportError("msg.no.name.after.xmlAttr");
          return (AstNode)makeErrorNode();
        } 
        return (AstNode)xmlElemRef(j, null, -1);
      } 
      return propertyName(j, this.ts.getString(), 0);
    } 
    saveNameTokenData(this.ts.tokenBeg, "*", this.ts.lineno);
    return propertyName(j, "*", 0);
  }
  
  private void autoInsertSemicolon(AstNode paramAstNode) throws IOException {
    int i = peekFlaggedToken();
    int j = paramAstNode.getPosition();
    int k = 0xFFFF & i;
    if (k != -1 && k != 0)
      if (k != 83) {
        if (k != 87) {
          if ((0x10000 & i) == 0) {
            reportError("msg.no.semi.stmt");
          } else {
            warnMissingSemi(j, nodeEnd(paramAstNode));
          } 
          return;
        } 
      } else {
        consumeToken();
        paramAstNode.setLength(this.ts.tokenEnd - j);
        return;
      }  
    warnMissingSemi(j, Math.max(j + 1, nodeEnd(paramAstNode)));
  }
  
  private AstNode bitAndExpr() throws IOException {
    InfixExpression infixExpression;
    AstNode astNode = eqExpr();
    while (matchToken(11, true)) {
      int i = this.ts.tokenBeg;
      infixExpression = new InfixExpression(11, astNode, eqExpr(), i);
    } 
    return (AstNode)infixExpression;
  }
  
  private AstNode bitOrExpr() throws IOException {
    InfixExpression infixExpression;
    AstNode astNode = bitXorExpr();
    while (matchToken(9, true)) {
      int i = this.ts.tokenBeg;
      infixExpression = new InfixExpression(9, astNode, bitXorExpr(), i);
    } 
    return (AstNode)infixExpression;
  }
  
  private AstNode bitXorExpr() throws IOException {
    InfixExpression infixExpression;
    AstNode astNode = bitAndExpr();
    while (matchToken(10, true)) {
      int i = this.ts.tokenBeg;
      infixExpression = new InfixExpression(10, astNode, bitAndExpr(), i);
    } 
    return (AstNode)infixExpression;
  }
  
  private AstNode block() throws IOException {
    if (this.currentToken != 86)
      codeBug(); 
    consumeToken();
    int i = this.ts.tokenBeg;
    null = new Scope(i);
    null.setLineno(this.ts.lineno);
    pushScope(null);
    try {
      statements((AstNode)null);
      mustMatchToken(87, "msg.no.brace.block", true);
      null.setLength(this.ts.tokenEnd - i);
      return (AstNode)null;
    } finally {
      popScope();
    } 
  }
  
  private BreakStatement breakStatement() throws IOException {
    Label label1;
    Jump jump;
    if (this.currentToken != 121)
      codeBug(); 
    consumeToken();
    int i = this.ts.lineno;
    int j = this.ts.tokenBeg;
    int k = this.ts.tokenEnd;
    Name name = null;
    if (peekTokenOrEOL() == 39) {
      name = createNameNode();
      k = getNodeEnd((AstNode)name);
    } 
    LabeledStatement labeledStatement = matchJumpLabelName();
    if (labeledStatement == null) {
      labeledStatement = null;
    } else {
      label1 = labeledStatement.getFirstLabel();
    } 
    Label label2 = label1;
    if (label1 == null) {
      label2 = label1;
      if (name == null) {
        List<Jump> list = this.loopAndSwitchSet;
        if (list == null || list.size() == 0) {
          reportError("msg.bad.break", j, k - j);
          Label label = label1;
        } else {
          List<Jump> list1 = this.loopAndSwitchSet;
          jump = list1.get(list1.size() - 1);
        } 
      } 
    } 
    BreakStatement breakStatement = new BreakStatement(j, k - j);
    breakStatement.setBreakLabel(name);
    if (jump != null)
      breakStatement.setBreakTarget(jump); 
    breakStatement.setLineno(i);
    return breakStatement;
  }
  
  private void checkBadIncDec(UnaryExpression paramUnaryExpression) {
    int i = removeParens(paramUnaryExpression.getOperand()).getType();
    if (i != 39 && i != 33 && i != 36 && i != 68 && i != 38) {
      String str;
      if (paramUnaryExpression.getType() == 107) {
        str = "msg.bad.incr";
      } else {
        str = "msg.bad.decr";
      } 
      reportError(str);
    } 
  }
  
  private void checkCallRequiresActivation(AstNode paramAstNode) {
    if ((paramAstNode.getType() == 39 && "eval".equals(((Name)paramAstNode).getIdentifier())) || (paramAstNode.getType() == 33 && "eval".equals(((PropertyGet)paramAstNode).getProperty().getIdentifier())))
      setRequiresActivation(); 
  }
  
  private RuntimeException codeBug() throws RuntimeException {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("ts.cursor=");
    stringBuilder.append(this.ts.cursor);
    stringBuilder.append(", ts.tokenBeg=");
    stringBuilder.append(this.ts.tokenBeg);
    stringBuilder.append(", currentToken=");
    stringBuilder.append(this.currentToken);
    throw Kit.codeBug(stringBuilder.toString());
  }
  
  private AstNode condExpr() throws IOException {
    AstNode astNode1 = orExpr();
    AstNode astNode2 = astNode1;
    if (matchToken(103, true)) {
      int i = this.ts.lineno;
      int j = this.ts.tokenBeg;
      int k = -1;
      boolean bool = this.inForInit;
      this.inForInit = false;
      try {
        AstNode astNode3 = assignExpr();
        this.inForInit = bool;
        if (mustMatchToken(104, "msg.no.colon.cond", true))
          k = this.ts.tokenBeg; 
        AstNode astNode4 = assignExpr();
        int m = astNode1.getPosition();
        ConditionalExpression conditionalExpression = new ConditionalExpression(m, getNodeEnd(astNode4) - m);
        conditionalExpression.setLineno(i);
        conditionalExpression.setTestExpression(astNode1);
        conditionalExpression.setTrueExpression(astNode3);
        conditionalExpression.setFalseExpression(astNode4);
        conditionalExpression.setQuestionMarkPosition(j - m);
      } finally {
        this.inForInit = bool;
      } 
    } 
    return astNode2;
  }
  
  private ConditionData condition() throws IOException {
    ConditionData conditionData = new ConditionData();
    if (mustMatchToken(88, "msg.no.paren.cond", true))
      conditionData.lp = this.ts.tokenBeg; 
    conditionData.condition = expr();
    if (mustMatchToken(89, "msg.no.paren.after.cond", true))
      conditionData.rp = this.ts.tokenBeg; 
    if (conditionData.condition instanceof Assignment)
      addStrictWarning("msg.equal.as.assign", "", conditionData.condition.getPosition(), conditionData.condition.getLength()); 
    return conditionData;
  }
  
  private void consumeToken() {
    this.currentFlaggedToken = 0;
  }
  
  private ContinueStatement continueStatement() throws IOException {
    List<Loop> list1;
    Loop loop;
    if (this.currentToken != 122)
      codeBug(); 
    consumeToken();
    int i = this.ts.lineno;
    int j = this.ts.tokenBeg;
    int k = this.ts.tokenEnd;
    Name name = null;
    if (peekTokenOrEOL() == 39) {
      name = createNameNode();
      k = getNodeEnd((AstNode)name);
    } 
    LabeledStatement labeledStatement = matchJumpLabelName();
    List<Loop> list2 = null;
    if (labeledStatement == null && name == null) {
      list1 = this.loopSet;
      if (list1 == null || list1.size() == 0) {
        reportError("msg.continue.outside");
      } else {
        list2 = this.loopSet;
        loop = list2.get(list2.size() - 1);
      } 
    } else {
      if (list1 == null || !(list1.getStatement() instanceof Loop))
        reportError("msg.continue.nonloop", j, k - j); 
      if (list1 == null) {
        list2 = null;
      } else {
        loop = (Loop)list1.getStatement();
      } 
    } 
    ContinueStatement continueStatement = new ContinueStatement(j, k - j);
    if (loop != null)
      continueStatement.setTarget(loop); 
    continueStatement.setLabel(name);
    continueStatement.setLineno(i);
    return continueStatement;
  }
  
  private Name createNameNode() {
    return createNameNode(false, 39);
  }
  
  private Name createNameNode(boolean paramBoolean, int paramInt) {
    int i = this.ts.tokenBeg;
    String str1 = this.ts.getString();
    int j = this.ts.lineno;
    if (!"".equals(this.prevNameTokenString)) {
      i = this.prevNameTokenStart;
      str1 = this.prevNameTokenString;
      j = this.prevNameTokenLineno;
      this.prevNameTokenStart = 0;
      this.prevNameTokenString = "";
      this.prevNameTokenLineno = 0;
    } 
    String str2 = str1;
    if (str1 == null)
      if (this.compilerEnv.isIdeMode()) {
        str2 = "";
      } else {
        codeBug();
        str2 = str1;
      }  
    Name name = new Name(i, str2);
    name.setLineno(j);
    if (paramBoolean)
      checkActivationName(str2, paramInt); 
    return name;
  }
  
  private StringLiteral createStringLiteral() {
    int i = this.ts.tokenBeg;
    StringLiteral stringLiteral = new StringLiteral(i, this.ts.tokenEnd - i);
    stringLiteral.setLineno(this.ts.lineno);
    stringLiteral.setValue(this.ts.getString());
    stringLiteral.setQuoteCharacter(this.ts.getQuoteChar());
    return stringLiteral;
  }
  
  private AstNode defaultXmlNamespace() throws IOException {
    if (this.currentToken != 117)
      codeBug(); 
    consumeToken();
    mustHaveXML();
    setRequiresActivation();
    int i = this.ts.lineno;
    int j = this.ts.tokenBeg;
    if (!matchToken(39, true) || !"xml".equals(this.ts.getString()))
      reportError("msg.bad.namespace"); 
    if (!matchToken(39, true) || !"namespace".equals(this.ts.getString()))
      reportError("msg.bad.namespace"); 
    if (!matchToken(91, true))
      reportError("msg.bad.namespace"); 
    AstNode astNode = expr();
    UnaryExpression unaryExpression = new UnaryExpression(j, getNodeEnd(astNode) - j);
    unaryExpression.setOperator(75);
    unaryExpression.setOperand(astNode);
    unaryExpression.setLineno(i);
    return (AstNode)new ExpressionStatement((AstNode)unaryExpression, true);
  }
  
  private AstNode destructuringPrimaryExpr() throws IOException, ParserException {
    try {
      this.inDestructuringAssignment = true;
      return primaryExpr();
    } finally {
      this.inDestructuringAssignment = false;
    } 
  }
  
  private DoLoop doLoop() throws IOException {
    if (this.currentToken != 119)
      codeBug(); 
    consumeToken();
    int i = this.ts.tokenBeg;
    DoLoop doLoop = new DoLoop(i);
    doLoop.setLineno(this.ts.lineno);
    enterLoop((Loop)doLoop);
    try {
      AstNode astNode = getNextStatementAfterInlineComments((AstNode)doLoop);
      mustMatchToken(118, "msg.no.while.do", true);
      doLoop.setWhilePosition(this.ts.tokenBeg - i);
      ConditionData conditionData = condition();
      doLoop.setCondition(conditionData.condition);
      doLoop.setParens(conditionData.lp - i, conditionData.rp - i);
      int j = getNodeEnd(astNode);
      doLoop.setBody(astNode);
      exitLoop();
      if (matchToken(83, true))
        j = this.ts.tokenEnd; 
      return doLoop;
    } finally {
      exitLoop();
    } 
  }
  
  private void enterLoop(Loop paramLoop) {
    if (this.loopSet == null)
      this.loopSet = new ArrayList<>(); 
    this.loopSet.add(paramLoop);
    if (this.loopAndSwitchSet == null)
      this.loopAndSwitchSet = new ArrayList<>(); 
    this.loopAndSwitchSet.add(paramLoop);
    pushScope((Scope)paramLoop);
    LabeledStatement labeledStatement = this.currentLabel;
    if (labeledStatement != null) {
      labeledStatement.setStatement((AstNode)paramLoop);
      this.currentLabel.getFirstLabel().setLoop((Jump)paramLoop);
      paramLoop.setRelative(-this.currentLabel.getPosition());
    } 
  }
  
  private void enterSwitch(SwitchStatement paramSwitchStatement) {
    if (this.loopAndSwitchSet == null)
      this.loopAndSwitchSet = new ArrayList<>(); 
    this.loopAndSwitchSet.add(paramSwitchStatement);
  }
  
  private AstNode eqExpr() throws IOException {
    AstNode astNode = relExpr();
    while (true) {
      int i = peekToken();
      int j = this.ts.tokenBeg;
      if (i != 12 && i != 13 && i != 46 && i != 47)
        return astNode; 
      consumeToken();
      int k = i;
      int m = k;
      if (this.compilerEnv.getLanguageVersion() == 120)
        if (i == 12) {
          m = 46;
        } else {
          m = k;
          if (i == 13)
            m = 47; 
        }  
      InfixExpression infixExpression = new InfixExpression(m, astNode, relExpr(), j);
    } 
  }
  
  private void exitLoop() {
    List<Loop> list1 = this.loopSet;
    Loop loop = list1.remove(list1.size() - 1);
    List<Jump> list = this.loopAndSwitchSet;
    list.remove(list.size() - 1);
    if (loop.getParent() != null)
      loop.setRelative(loop.getParent().getPosition()); 
    popScope();
  }
  
  private void exitSwitch() {
    List<Jump> list = this.loopAndSwitchSet;
    list.remove(list.size() - 1);
  }
  
  private AstNode expr() throws IOException {
    InfixExpression infixExpression;
    AstNode astNode = assignExpr();
    int i = astNode.getPosition();
    while (matchToken(90, true)) {
      int j = this.ts.tokenBeg;
      if (this.compilerEnv.isStrictMode() && !astNode.hasSideEffects())
        addStrictWarning("msg.no.side.effects", "", i, nodeEnd(astNode) - i); 
      if (peekToken() == 73)
        reportError("msg.yield.parenthesized"); 
      infixExpression = new InfixExpression(90, astNode, assignExpr(), j);
    } 
    return (AstNode)infixExpression;
  }
  
  private Loop forLoop() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield currentToken : I
    //   4: bipush #120
    //   6: if_icmpeq -> 14
    //   9: aload_0
    //   10: invokespecial codeBug : ()Ljava/lang/RuntimeException;
    //   13: pop
    //   14: aload_0
    //   15: invokespecial consumeToken : ()V
    //   18: aload_0
    //   19: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   22: getfield tokenBeg : I
    //   25: istore_1
    //   26: aload_0
    //   27: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   30: getfield lineno : I
    //   33: istore_2
    //   34: iconst_m1
    //   35: istore_3
    //   36: iconst_m1
    //   37: istore #4
    //   39: iconst_m1
    //   40: istore #5
    //   42: aconst_null
    //   43: astore #6
    //   45: new com/trendmicro/hippo/ast/Scope
    //   48: dup
    //   49: invokespecial <init> : ()V
    //   52: astore #7
    //   54: aload_0
    //   55: aload #7
    //   57: invokevirtual pushScope : (Lcom/trendmicro/hippo/ast/Scope;)V
    //   60: aload_0
    //   61: bipush #39
    //   63: iconst_1
    //   64: invokespecial matchToken : (IZ)Z
    //   67: istore #8
    //   69: iload #8
    //   71: ifeq -> 134
    //   74: ldc_w 'each'
    //   77: aload_0
    //   78: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   81: invokevirtual getString : ()Ljava/lang/String;
    //   84: invokevirtual equals : (Ljava/lang/Object;)Z
    //   87: istore #8
    //   89: iload #8
    //   91: ifeq -> 119
    //   94: iconst_1
    //   95: istore #9
    //   97: aload_0
    //   98: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   101: getfield tokenBeg : I
    //   104: istore #10
    //   106: iload #10
    //   108: iload_1
    //   109: isub
    //   110: istore_3
    //   111: goto -> 137
    //   114: astore #11
    //   116: goto -> 777
    //   119: aload_0
    //   120: ldc_w 'msg.no.paren.for'
    //   123: invokevirtual reportError : (Ljava/lang/String;)V
    //   126: goto -> 134
    //   129: astore #11
    //   131: goto -> 777
    //   134: iconst_0
    //   135: istore #9
    //   137: aload_0
    //   138: bipush #88
    //   140: ldc_w 'msg.no.paren.for'
    //   143: iconst_1
    //   144: invokespecial mustMatchToken : (ILjava/lang/String;Z)Z
    //   147: istore #8
    //   149: iload #8
    //   151: ifeq -> 177
    //   154: aload_0
    //   155: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   158: getfield tokenBeg : I
    //   161: istore #10
    //   163: iload #10
    //   165: iload_1
    //   166: isub
    //   167: istore #5
    //   169: goto -> 177
    //   172: astore #11
    //   174: goto -> 777
    //   177: aload_0
    //   178: aload_0
    //   179: invokespecial peekToken : ()I
    //   182: invokespecial forLoopInit : (I)Lcom/trendmicro/hippo/ast/AstNode;
    //   185: astore #12
    //   187: aload_0
    //   188: bipush #52
    //   190: iconst_1
    //   191: invokespecial matchToken : (IZ)Z
    //   194: istore #8
    //   196: iload #8
    //   198: ifeq -> 232
    //   201: aload_0
    //   202: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   205: getfield tokenBeg : I
    //   208: iload_1
    //   209: isub
    //   210: istore #4
    //   212: aload_0
    //   213: invokespecial expr : ()Lcom/trendmicro/hippo/ast/AstNode;
    //   216: astore #11
    //   218: iconst_1
    //   219: istore #10
    //   221: iconst_0
    //   222: istore #8
    //   224: goto -> 447
    //   227: astore #11
    //   229: goto -> 777
    //   232: aload_0
    //   233: getfield compilerEnv : Lcom/trendmicro/hippo/CompilerEnvirons;
    //   236: invokevirtual getLanguageVersion : ()I
    //   239: istore #10
    //   241: iload #10
    //   243: sipush #200
    //   246: if_icmplt -> 305
    //   249: aload_0
    //   250: bipush #39
    //   252: iconst_1
    //   253: invokespecial matchToken : (IZ)Z
    //   256: ifeq -> 305
    //   259: ldc_w 'of'
    //   262: aload_0
    //   263: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   266: invokevirtual getString : ()Ljava/lang/String;
    //   269: invokevirtual equals : (Ljava/lang/Object;)Z
    //   272: istore #8
    //   274: iload #8
    //   276: ifeq -> 305
    //   279: iconst_1
    //   280: istore #8
    //   282: aload_0
    //   283: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   286: getfield tokenBeg : I
    //   289: iload_1
    //   290: isub
    //   291: istore #4
    //   293: aload_0
    //   294: invokespecial expr : ()Lcom/trendmicro/hippo/ast/AstNode;
    //   297: astore #11
    //   299: iconst_0
    //   300: istore #10
    //   302: goto -> 447
    //   305: aload_0
    //   306: bipush #83
    //   308: ldc_w 'msg.no.semi.for'
    //   311: iconst_1
    //   312: invokespecial mustMatchToken : (ILjava/lang/String;Z)Z
    //   315: pop
    //   316: aload_0
    //   317: invokespecial peekToken : ()I
    //   320: istore #10
    //   322: iload #10
    //   324: bipush #83
    //   326: if_icmpne -> 362
    //   329: new com/trendmicro/hippo/ast/EmptyExpression
    //   332: astore #11
    //   334: aload #11
    //   336: aload_0
    //   337: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   340: getfield tokenBeg : I
    //   343: iconst_1
    //   344: invokespecial <init> : (II)V
    //   347: aload #11
    //   349: aload_0
    //   350: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   353: getfield lineno : I
    //   356: invokevirtual setLineno : (I)V
    //   359: goto -> 368
    //   362: aload_0
    //   363: invokespecial expr : ()Lcom/trendmicro/hippo/ast/AstNode;
    //   366: astore #11
    //   368: aload_0
    //   369: bipush #83
    //   371: ldc_w 'msg.no.semi.for.cond'
    //   374: iconst_1
    //   375: invokespecial mustMatchToken : (ILjava/lang/String;Z)Z
    //   378: pop
    //   379: aload_0
    //   380: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   383: getfield tokenEnd : I
    //   386: istore #10
    //   388: aload_0
    //   389: invokespecial peekToken : ()I
    //   392: istore #13
    //   394: iload #13
    //   396: bipush #89
    //   398: if_icmpne -> 435
    //   401: new com/trendmicro/hippo/ast/EmptyExpression
    //   404: astore #6
    //   406: aload #6
    //   408: iload #10
    //   410: iconst_1
    //   411: invokespecial <init> : (II)V
    //   414: aload #6
    //   416: aload_0
    //   417: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   420: getfield lineno : I
    //   423: invokevirtual setLineno : (I)V
    //   426: iconst_0
    //   427: istore #10
    //   429: iconst_0
    //   430: istore #8
    //   432: goto -> 447
    //   435: aload_0
    //   436: invokespecial expr : ()Lcom/trendmicro/hippo/ast/AstNode;
    //   439: astore #6
    //   441: iconst_0
    //   442: istore #10
    //   444: iconst_0
    //   445: istore #8
    //   447: aload_0
    //   448: bipush #89
    //   450: ldc_w 'msg.no.paren.for.ctrl'
    //   453: iconst_1
    //   454: invokespecial mustMatchToken : (ILjava/lang/String;Z)Z
    //   457: istore #14
    //   459: iload #14
    //   461: ifeq -> 487
    //   464: aload_0
    //   465: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   468: getfield tokenBeg : I
    //   471: istore #13
    //   473: iload #13
    //   475: iload_1
    //   476: isub
    //   477: istore #13
    //   479: goto -> 490
    //   482: astore #11
    //   484: goto -> 777
    //   487: iconst_m1
    //   488: istore #13
    //   490: iload #10
    //   492: ifne -> 547
    //   495: iload #8
    //   497: ifeq -> 503
    //   500: goto -> 547
    //   503: new com/trendmicro/hippo/ast/ForLoop
    //   506: astore #15
    //   508: aload #15
    //   510: iload_1
    //   511: invokespecial <init> : (I)V
    //   514: aload #15
    //   516: aload #12
    //   518: invokevirtual setInitializer : (Lcom/trendmicro/hippo/ast/AstNode;)V
    //   521: aload #15
    //   523: aload #11
    //   525: invokevirtual setCondition : (Lcom/trendmicro/hippo/ast/AstNode;)V
    //   528: aload #15
    //   530: aload #6
    //   532: invokevirtual setIncrement : (Lcom/trendmicro/hippo/ast/AstNode;)V
    //   535: aload #15
    //   537: astore #11
    //   539: goto -> 659
    //   542: astore #11
    //   544: goto -> 777
    //   547: new com/trendmicro/hippo/ast/ForInLoop
    //   550: astore #6
    //   552: aload #6
    //   554: iload_1
    //   555: invokespecial <init> : (I)V
    //   558: aload #12
    //   560: instanceof com/trendmicro/hippo/ast/VariableDeclaration
    //   563: ifeq -> 597
    //   566: aload #12
    //   568: checkcast com/trendmicro/hippo/ast/VariableDeclaration
    //   571: invokevirtual getVariables : ()Ljava/util/List;
    //   574: invokeinterface size : ()I
    //   579: istore #10
    //   581: iload #10
    //   583: iconst_1
    //   584: if_icmple -> 597
    //   587: aload_0
    //   588: ldc_w 'msg.mult.index'
    //   591: invokevirtual reportError : (Ljava/lang/String;)V
    //   594: goto -> 597
    //   597: iload #8
    //   599: ifeq -> 614
    //   602: iload #9
    //   604: ifeq -> 614
    //   607: aload_0
    //   608: ldc_w 'msg.invalid.for.each'
    //   611: invokevirtual reportError : (Ljava/lang/String;)V
    //   614: aload #6
    //   616: aload #12
    //   618: invokevirtual setIterator : (Lcom/trendmicro/hippo/ast/AstNode;)V
    //   621: aload #6
    //   623: aload #11
    //   625: invokevirtual setIteratedObject : (Lcom/trendmicro/hippo/ast/AstNode;)V
    //   628: aload #6
    //   630: iload #4
    //   632: invokevirtual setInPosition : (I)V
    //   635: aload #6
    //   637: iload #9
    //   639: invokevirtual setIsForEach : (Z)V
    //   642: aload #6
    //   644: iload_3
    //   645: invokevirtual setEachPosition : (I)V
    //   648: aload #6
    //   650: iload #8
    //   652: invokevirtual setIsForOf : (Z)V
    //   655: aload #6
    //   657: astore #11
    //   659: aload_0
    //   660: getfield currentScope : Lcom/trendmicro/hippo/ast/Scope;
    //   663: aload #11
    //   665: invokevirtual replaceWith : (Lcom/trendmicro/hippo/ast/Scope;)V
    //   668: aload_0
    //   669: invokevirtual popScope : ()V
    //   672: aload_0
    //   673: aload #11
    //   675: invokespecial enterLoop : (Lcom/trendmicro/hippo/ast/Loop;)V
    //   678: aload_0
    //   679: aload #11
    //   681: invokespecial getNextStatementAfterInlineComments : (Lcom/trendmicro/hippo/ast/AstNode;)Lcom/trendmicro/hippo/ast/AstNode;
    //   684: astore #6
    //   686: aload #11
    //   688: aload_0
    //   689: aload #6
    //   691: invokespecial getNodeEnd : (Lcom/trendmicro/hippo/ast/AstNode;)I
    //   694: iload_1
    //   695: isub
    //   696: invokevirtual setLength : (I)V
    //   699: aload #11
    //   701: aload #6
    //   703: invokevirtual setBody : (Lcom/trendmicro/hippo/ast/AstNode;)V
    //   706: aload_0
    //   707: invokespecial exitLoop : ()V
    //   710: aload_0
    //   711: getfield currentScope : Lcom/trendmicro/hippo/ast/Scope;
    //   714: aload #7
    //   716: if_acmpne -> 723
    //   719: aload_0
    //   720: invokevirtual popScope : ()V
    //   723: aload #11
    //   725: iload #5
    //   727: iload #13
    //   729: invokevirtual setParens : (II)V
    //   732: aload #11
    //   734: iload_2
    //   735: invokevirtual setLineno : (I)V
    //   738: aload #11
    //   740: areturn
    //   741: astore #11
    //   743: aload_0
    //   744: invokespecial exitLoop : ()V
    //   747: aload #11
    //   749: athrow
    //   750: astore #11
    //   752: goto -> 777
    //   755: astore #11
    //   757: goto -> 777
    //   760: astore #11
    //   762: goto -> 777
    //   765: astore #11
    //   767: goto -> 777
    //   770: astore #11
    //   772: goto -> 777
    //   775: astore #11
    //   777: aload_0
    //   778: getfield currentScope : Lcom/trendmicro/hippo/ast/Scope;
    //   781: aload #7
    //   783: if_acmpne -> 790
    //   786: aload_0
    //   787: invokevirtual popScope : ()V
    //   790: aload #11
    //   792: athrow
    // Exception table:
    //   from	to	target	type
    //   60	69	775	finally
    //   74	89	129	finally
    //   97	106	114	finally
    //   119	126	129	finally
    //   137	149	770	finally
    //   154	163	172	finally
    //   177	196	770	finally
    //   201	218	227	finally
    //   232	241	770	finally
    //   249	274	172	finally
    //   282	299	114	finally
    //   305	322	770	finally
    //   329	347	172	finally
    //   347	359	172	finally
    //   362	368	770	finally
    //   368	394	770	finally
    //   401	414	172	finally
    //   414	426	172	finally
    //   435	441	770	finally
    //   447	459	765	finally
    //   464	473	482	finally
    //   503	535	542	finally
    //   547	581	760	finally
    //   587	594	755	finally
    //   607	614	755	finally
    //   614	655	755	finally
    //   659	678	750	finally
    //   678	706	741	finally
    //   706	710	750	finally
    //   743	747	750	finally
    //   747	750	750	finally
  }
  
  private AstNode forLoopInit(int paramInt) throws IOException {
    try {
      AstNode astNode;
      this.inForInit = true;
      if (paramInt == 83) {
        EmptyExpression emptyExpression = new EmptyExpression();
        this(this.ts.tokenBeg, 1);
        emptyExpression.setLineno(this.ts.lineno);
      } else {
        if (paramInt == 123 || paramInt == 154) {
          consumeToken();
          return (AstNode)variables(paramInt, this.ts.tokenBeg, false);
        } 
        astNode = expr();
        markDestructuring(astNode);
      } 
      return astNode;
    } finally {
      this.inForInit = false;
    } 
  }
  
  private FunctionNode function(int paramInt) throws IOException {
    Name name1;
    byte b;
    int i = paramInt;
    int j = this.ts.lineno;
    int k = this.ts.tokenBeg;
    String str = null;
    Name name2 = null;
    null = null;
    AstNode astNode = null;
    if (matchToken(39, true)) {
      name2 = createNameNode(true, 39);
      if (this.inUseStrictDirective) {
        str = name2.getIdentifier();
        if ("eval".equals(str) || "arguments".equals(str))
          reportError("msg.bad.id.strict", str); 
      } 
      name1 = name2;
      if (!matchToken(88, true)) {
        name1 = name2;
        null = astNode;
        if (this.compilerEnv.isAllowMemberExprAsFunctionName()) {
          name1 = null;
          null = memberExprTail(false, (AstNode)name2);
        } 
        mustMatchToken(88, "msg.no.paren.parms", true);
      } 
    } else if (!matchToken(88, true)) {
      Name name = name2;
      if (this.compilerEnv.isAllowMemberExprAsFunctionName())
        null = memberExpr(false); 
      mustMatchToken(88, "msg.no.paren.parms", true);
    } 
    if (this.currentToken == 88) {
      b = this.ts.tokenBeg;
    } else {
      b = -1;
    } 
    if (null != null)
      i = 2; 
    if (i != 2 && name1 != null && name1.length() > 0)
      defineSymbol(110, name1.getIdentifier()); 
    FunctionNode functionNode = new FunctionNode(k, name1);
    functionNode.setFunctionType(paramInt);
    if (b != -1)
      functionNode.setLp(b - k); 
    functionNode.setJsDocNode(getAndResetJsDoc());
    PerFunctionVariables perFunctionVariables = new PerFunctionVariables(functionNode);
    try {
      parseFunctionParams(functionNode);
      functionNode.setBody(parseFunctionBody(paramInt, functionNode));
      functionNode.setEncodedSourceBounds(k, this.ts.tokenEnd);
      functionNode.setLength(this.ts.tokenEnd - k);
      if (this.compilerEnv.isStrictMode() && !functionNode.getBody().hasConsistentReturnUsage()) {
        String str1;
        String str2;
        if (name1 != null && name1.length() > 0) {
          str2 = "msg.no.return.value";
        } else {
          str2 = "msg.anon.no.return.value";
        } 
        if (name1 == null) {
          str1 = "";
        } else {
          str1 = str1.getIdentifier();
        } 
        addStrictWarning(str2, str1);
      } 
      perFunctionVariables.restore();
      if (null != null) {
        Kit.codeBug();
        functionNode.setMemberExprNode(null);
      } 
      functionNode.setSourceName(this.sourceURI);
      functionNode.setBaseLineno(j);
      functionNode.setEndLineno(this.ts.lineno);
      return functionNode;
    } finally {
      perFunctionVariables.restore();
    } 
  }
  
  private AstNode generatorExpression(AstNode paramAstNode, int paramInt) throws IOException {
    return generatorExpression(paramAstNode, paramInt, false);
  }
  
  private AstNode generatorExpression(AstNode paramAstNode, int paramInt, boolean paramBoolean) throws IOException {
    ArrayList<GeneratorExpressionLoop> arrayList = new ArrayList();
    while (peekToken() == 120)
      arrayList.add(generatorExpressionLoop()); 
    int i = -1;
    ConditionData conditionData = null;
    if (peekToken() == 113) {
      consumeToken();
      i = this.ts.tokenBeg - paramInt;
      conditionData = condition();
    } 
    if (!paramBoolean)
      mustMatchToken(89, "msg.no.paren.let", true); 
    GeneratorExpression generatorExpression = new GeneratorExpression(paramInt, this.ts.tokenEnd - paramInt);
    generatorExpression.setResult(paramAstNode);
    generatorExpression.setLoops(arrayList);
    if (conditionData != null) {
      generatorExpression.setIfPosition(i);
      generatorExpression.setFilter(conditionData.condition);
      generatorExpression.setFilterLp(conditionData.lp - paramInt);
      generatorExpression.setFilterRp(conditionData.rp - paramInt);
    } 
    return (AstNode)generatorExpression;
  }
  
  private GeneratorExpressionLoop generatorExpressionLoop() throws IOException {
    if (nextToken() != 120)
      codeBug(); 
    int i = this.ts.tokenBeg;
    int j = -1;
    int k = -1;
    int m = -1;
    GeneratorExpressionLoop generatorExpressionLoop = new GeneratorExpressionLoop(i);
    pushScope((Scope)generatorExpressionLoop);
    try {
      Name name;
      if (mustMatchToken(88, "msg.no.paren.for", true))
        j = this.ts.tokenBeg - i; 
      AstNode astNode1 = null;
      int n = peekToken();
      if (n != 39) {
        if (n != 84 && n != 86) {
          reportError("msg.bad.var");
        } else {
          astNode1 = destructuringPrimaryExpr();
          markDestructuring(astNode1);
        } 
      } else {
        consumeToken();
        name = createNameNode();
      } 
      if (name.getType() == 39)
        defineSymbol(154, this.ts.getString(), true); 
      if (mustMatchToken(52, "msg.in.after.for.name", true))
        m = this.ts.tokenBeg - i; 
      AstNode astNode2 = expr();
      if (mustMatchToken(89, "msg.no.paren.for.ctrl", true))
        k = this.ts.tokenBeg - i; 
      generatorExpressionLoop.setLength(this.ts.tokenEnd - i);
      generatorExpressionLoop.setIterator((AstNode)name);
      generatorExpressionLoop.setIteratedObject(astNode2);
      generatorExpressionLoop.setInPosition(m);
      generatorExpressionLoop.setParens(j, k);
      return generatorExpressionLoop;
    } finally {
      popScope();
    } 
  }
  
  private Comment getAndResetJsDoc() {
    Comment comment = this.currentJsDocComment;
    this.currentJsDocComment = null;
    return comment;
  }
  
  private String getDirective(AstNode paramAstNode) {
    if (paramAstNode instanceof ExpressionStatement) {
      paramAstNode = ((ExpressionStatement)paramAstNode).getExpression();
      if (paramAstNode instanceof StringLiteral)
        return ((StringLiteral)paramAstNode).getValue(); 
    } 
    return null;
  }
  
  private AstNode getNextStatementAfterInlineComments(AstNode paramAstNode) throws IOException {
    AstNode astNode1 = statement();
    AstNode astNode2 = astNode1;
    if (162 == astNode1.getType()) {
      astNode2 = statement();
      if (paramAstNode != null) {
        paramAstNode.setInlineComment(astNode1);
      } else {
        astNode2.setInlineComment(astNode1);
      } 
    } 
    return astNode2;
  }
  
  private int getNodeEnd(AstNode paramAstNode) {
    return paramAstNode.getPosition() + paramAstNode.getLength();
  }
  
  private int getNumberOfEols(String paramString) {
    int i = 0;
    int j = paramString.length() - 1;
    while (j >= 0) {
      int k = i;
      if (paramString.charAt(j) == '\n')
        k = i + 1; 
      j--;
      i = k;
    } 
    return i;
  }
  
  private IfStatement ifStatement() throws IOException {
    AstNode astNode2;
    AstNode astNode3;
    if (this.currentToken != 113)
      codeBug(); 
    consumeToken();
    int i = this.ts.tokenBeg;
    int j = this.ts.lineno;
    int k = -1;
    IfStatement ifStatement = new IfStatement(i);
    ConditionData conditionData = condition();
    AstNode astNode1 = getNextStatementAfterInlineComments((AstNode)ifStatement);
    List<Comment> list = null;
    if (matchToken(114, true)) {
      if (peekToken() == 162) {
        list = this.scannedComments;
        ifStatement.setElseKeyWordInlineComment((AstNode)list.get(list.size() - 1));
        consumeToken();
      } 
      k = this.ts.tokenBeg - i;
      astNode2 = statement();
    } 
    if (astNode2 != null) {
      astNode3 = astNode2;
    } else {
      astNode3 = astNode1;
    } 
    ifStatement.setLength(getNodeEnd(astNode3) - i);
    ifStatement.setCondition(conditionData.condition);
    ifStatement.setParens(conditionData.lp - i, conditionData.rp - i);
    ifStatement.setThenPart(astNode1);
    ifStatement.setElsePart(astNode2);
    ifStatement.setElsePosition(k);
    ifStatement.setLineno(j);
    return ifStatement;
  }
  
  private AstNode let(boolean paramBoolean, int paramInt) throws IOException {
    null = new LetNode(paramInt);
    null.setLineno(this.ts.lineno);
    boolean bool = true;
    if (mustMatchToken(88, "msg.no.paren.after.let", true))
      null.setLp(this.ts.tokenBeg - paramInt); 
    pushScope((Scope)null);
    try {
      null.setVariables(variables(154, this.ts.tokenBeg, paramBoolean));
      if (mustMatchToken(89, "msg.no.paren.let", true))
        null.setRp(this.ts.tokenBeg - paramInt); 
      if (paramBoolean && peekToken() == 86) {
        consumeToken();
        int i = this.ts.tokenBeg;
        AstNode astNode = statements();
        mustMatchToken(87, "msg.no.curly.let", true);
        astNode.setLength(this.ts.tokenEnd - i);
        null.setLength(this.ts.tokenEnd - paramInt);
        null.setBody(astNode);
        null.setType(154);
      } else {
        AstNode astNode = expr();
        null.setLength(getNodeEnd(astNode) - paramInt);
        null.setBody(astNode);
        if (paramBoolean) {
          ExpressionStatement expressionStatement = new ExpressionStatement();
          if (!insideFunction()) {
            paramBoolean = bool;
          } else {
            paramBoolean = false;
          } 
          this((AstNode)null, paramBoolean);
          expressionStatement.setLineno(null.getLineno());
          return (AstNode)expressionStatement;
        } 
      } 
      return (AstNode)null;
    } finally {
      popScope();
    } 
  }
  
  private AstNode letStatement() throws IOException {
    VariableDeclaration variableDeclaration;
    if (this.currentToken != 154)
      codeBug(); 
    consumeToken();
    int i = this.ts.lineno;
    int j = this.ts.tokenBeg;
    if (peekToken() == 88) {
      AstNode astNode = let(true, j);
    } else {
      variableDeclaration = variables(154, j, true);
    } 
    variableDeclaration.setLineno(i);
    return (AstNode)variableDeclaration;
  }
  
  private int lineBeginningFor(int paramInt) {
    if (this.sourceChars == null)
      return -1; 
    if (paramInt <= 0)
      return 0; 
    char[] arrayOfChar = this.sourceChars;
    int i = paramInt;
    if (paramInt >= arrayOfChar.length)
      i = arrayOfChar.length - 1; 
    while (--i >= 0) {
      if (ScriptRuntime.isJSLineTerminator(arrayOfChar[i]))
        return i + 1; 
    } 
    return 0;
  }
  
  private ErrorNode makeErrorNode() {
    ErrorNode errorNode = new ErrorNode(this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
    errorNode.setLineno(this.ts.lineno);
    return errorNode;
  }
  
  private LabeledStatement matchJumpLabelName() throws IOException {
    LabeledStatement labeledStatement1;
    Map<String, LabeledStatement> map = null;
    LabeledStatement labeledStatement2 = null;
    if (peekTokenOrEOL() == 39) {
      consumeToken();
      map = this.labelSet;
      if (map != null)
        labeledStatement2 = map.get(this.ts.getString()); 
      labeledStatement1 = labeledStatement2;
      if (labeledStatement2 == null) {
        reportError("msg.undef.label");
        labeledStatement1 = labeledStatement2;
      } 
    } 
    return labeledStatement1;
  }
  
  private boolean matchToken(int paramInt, boolean paramBoolean) throws IOException {
    int i;
    for (i = peekToken(); i == 162 && paramBoolean; i = peekToken())
      consumeToken(); 
    if (i != paramInt)
      return false; 
    consumeToken();
    return true;
  }
  
  private AstNode memberExpr(boolean paramBoolean) throws IOException {
    NewExpression newExpression;
    int i = peekToken();
    int j = this.ts.lineno;
    if (i != 30) {
      AstNode astNode = primaryExpr();
    } else {
      consumeToken();
      int k = this.ts.tokenBeg;
      newExpression = new NewExpression(k);
      AstNode astNode = memberExpr(false);
      i = getNodeEnd(astNode);
      newExpression.setTarget(astNode);
      if (matchToken(88, true)) {
        int m = this.ts.tokenBeg;
        List<AstNode> list = argumentList();
        if (list != null && list.size() > 65536)
          reportError("msg.too.many.constructor.args"); 
        int n = this.ts.tokenBeg;
        i = this.ts.tokenEnd;
        if (list != null)
          newExpression.setArguments(list); 
        newExpression.setParens(m - k, n - k);
      } 
      if (matchToken(86, true)) {
        ObjectLiteral objectLiteral = objectLiteral();
        i = getNodeEnd((AstNode)objectLiteral);
        newExpression.setInitializer(objectLiteral);
      } 
      newExpression.setLength(i - k);
    } 
    newExpression.setLineno(j);
    return memberExprTail(paramBoolean, (AstNode)newExpression);
  }
  
  private AstNode memberExprTail(boolean paramBoolean, AstNode paramAstNode) throws IOException {
    if (paramAstNode == null)
      codeBug(); 
    int i = paramAstNode.getPosition();
    while (true) {
      FunctionCall functionCall;
      int j = peekToken();
      if (j != 84) {
        AstNode astNode1;
        if (j != 88) {
          XmlDotQuery xmlDotQuery;
          if (j != 109 && j != 144) {
            if (j != 147) {
              if (j == 162) {
                int i1 = this.currentFlaggedToken;
                peekUntilNonComment(j);
                j = this.currentFlaggedToken;
                if ((0x10000 & j) != 0)
                  i1 = j; 
                this.currentFlaggedToken = i1;
                continue;
              } 
            } else {
              consumeToken();
              int i2 = this.ts.tokenBeg;
              int i1 = -1;
              int i3 = this.ts.lineno;
              mustHaveXML();
              setRequiresActivation();
              AstNode astNode2 = expr();
              j = getNodeEnd(astNode2);
              if (mustMatchToken(89, "msg.no.paren", true)) {
                i1 = this.ts.tokenBeg;
                j = this.ts.tokenEnd;
              } 
              XmlDotQuery xmlDotQuery1 = new XmlDotQuery(i, j - i);
              xmlDotQuery1.setLeft(paramAstNode);
              xmlDotQuery1.setRight(astNode2);
              xmlDotQuery1.setOperatorPosition(i2);
              xmlDotQuery1.setRp(i1 - i);
              xmlDotQuery1.setLineno(i3);
              xmlDotQuery = xmlDotQuery1;
              continue;
            } 
          } else {
            int i1 = this.ts.lineno;
            astNode1 = propertyAccess(j, (AstNode)xmlDotQuery);
            astNode1.setLineno(i1);
            continue;
          } 
        } else if (paramBoolean) {
          int i1 = this.ts.lineno;
          consumeToken();
          checkCallRequiresActivation(astNode1);
          FunctionCall functionCall1 = new FunctionCall(i);
          functionCall1.setTarget(astNode1);
          functionCall1.setLineno(i1);
          functionCall1.setLp(this.ts.tokenBeg - i);
          List<AstNode> list = argumentList();
          if (list != null && list.size() > 65536)
            reportError("msg.too.many.function.args"); 
          functionCall1.setArguments(list);
          functionCall1.setRp(this.ts.tokenBeg - i);
          functionCall1.setLength(this.ts.tokenEnd - i);
          functionCall = functionCall1;
          continue;
        } 
        return (AstNode)functionCall;
      } 
      consumeToken();
      int n = this.ts.tokenBeg;
      int k = -1;
      int m = this.ts.lineno;
      AstNode astNode = expr();
      j = getNodeEnd(astNode);
      if (mustMatchToken(85, "msg.no.bracket.index", true)) {
        k = this.ts.tokenBeg;
        j = this.ts.tokenEnd;
      } 
      ElementGet elementGet2 = new ElementGet(i, j - i);
      elementGet2.setTarget((AstNode)functionCall);
      elementGet2.setElement(astNode);
      elementGet2.setParens(n, k);
      elementGet2.setLineno(m);
      ElementGet elementGet1 = elementGet2;
    } 
  }
  
  private ObjectProperty methodDefinition(int paramInt1, AstNode paramAstNode, int paramInt2) throws IOException {
    FunctionNode functionNode = function(2);
    Name name = functionNode.getFunctionName();
    if (name != null && name.length() != 0)
      reportError("msg.bad.prop"); 
    ObjectProperty objectProperty = new ObjectProperty(paramInt1);
    if (paramInt2 != 2) {
      if (paramInt2 != 4) {
        if (paramInt2 == 8) {
          objectProperty.setIsNormalMethod();
          functionNode.setFunctionIsNormalMethod();
        } 
      } else {
        objectProperty.setIsSetterMethod();
        functionNode.setFunctionIsSetterMethod();
      } 
    } else {
      objectProperty.setIsGetterMethod();
      functionNode.setFunctionIsGetterMethod();
    } 
    paramInt2 = getNodeEnd((AstNode)functionNode);
    objectProperty.setLeft(paramAstNode);
    objectProperty.setRight((AstNode)functionNode);
    objectProperty.setLength(paramInt2 - paramInt1);
    return objectProperty;
  }
  
  private AstNode mulExpr() throws IOException {
    AstNode astNode = unaryExpr();
    while (true) {
      int i = peekToken();
      int j = this.ts.tokenBeg;
      switch (i) {
        default:
          return astNode;
        case 23:
        case 24:
        case 25:
          break;
      } 
      consumeToken();
      InfixExpression infixExpression = new InfixExpression(i, astNode, unaryExpr(), j);
    } 
  }
  
  private void mustHaveXML() {
    if (!this.compilerEnv.isXmlAvailable())
      reportError("msg.XML.not.available"); 
  }
  
  private boolean mustMatchToken(int paramInt1, String paramString, int paramInt2, int paramInt3, boolean paramBoolean) throws IOException {
    if (matchToken(paramInt1, paramBoolean))
      return true; 
    reportError(paramString, paramInt2, paramInt3);
    return false;
  }
  
  private boolean mustMatchToken(int paramInt, String paramString, boolean paramBoolean) throws IOException {
    return mustMatchToken(paramInt, paramString, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg, paramBoolean);
  }
  
  private AstNode name(int paramInt1, int paramInt2) throws IOException {
    String str = this.ts.getString();
    int i = this.ts.tokenBeg;
    paramInt2 = this.ts.lineno;
    if ((0x20000 & paramInt1) != 0 && peekToken() == 104) {
      Label label = new Label(i, this.ts.tokenEnd - i);
      label.setName(str);
      label.setLineno(this.ts.lineno);
      return (AstNode)label;
    } 
    saveNameTokenData(i, str, paramInt2);
    return (AstNode)(this.compilerEnv.isXmlAvailable() ? propertyName(-1, str, 0) : createNameNode(true, 39));
  }
  
  private AstNode nameOrLabel() throws IOException {
    if (this.currentToken == 39) {
      int i = this.ts.tokenBeg;
      this.currentFlaggedToken |= 0x20000;
      AstNode astNode1 = expr();
      if (astNode1.getType() != 131) {
        ExpressionStatement expressionStatement = new ExpressionStatement(astNode1, insideFunction() ^ true);
        ((AstNode)expressionStatement).lineno = astNode1.lineno;
        return (AstNode)expressionStatement;
      } 
      LabeledStatement labeledStatement = new LabeledStatement(i);
      recordLabel((Label)astNode1, labeledStatement);
      labeledStatement.setLineno(this.ts.lineno);
      AstNode astNode2 = null;
      while (true) {
        astNode1 = astNode2;
        if (peekToken() == 39) {
          this.currentFlaggedToken |= 0x20000;
          astNode1 = expr();
          if (astNode1.getType() != 131) {
            null = new ExpressionStatement(astNode1, insideFunction() ^ true);
            autoInsertSemicolon((AstNode)null);
            break;
          } 
          recordLabel((Label)null, labeledStatement);
          continue;
        } 
        break;
      } 
      try {
        AstNode astNode;
        this.currentLabel = labeledStatement;
        ExpressionStatement expressionStatement = null;
        if (null == null) {
          AstNode astNode3 = statementHelper();
          astNode = astNode3;
          if (peekToken() == 162) {
            astNode = astNode3;
            if (astNode3.getLineno() == ((Comment)this.scannedComments.get(this.scannedComments.size() - 1)).getLineno()) {
              astNode3.setInlineComment((AstNode)this.scannedComments.get(this.scannedComments.size() - 1));
              consumeToken();
              astNode = astNode3;
            } 
          } 
        } 
        this.currentLabel = null;
        for (Label label : labeledStatement.getLabels())
          this.labelSet.remove(label.getName()); 
        if (astNode.getParent() == null) {
          i = getNodeEnd(astNode) - i;
        } else {
          i = getNodeEnd(astNode);
        } 
        return (AstNode)labeledStatement;
      } finally {
        this.currentLabel = null;
        for (Label label : labeledStatement.getLabels())
          this.labelSet.remove(label.getName()); 
      } 
    } 
    throw codeBug();
  }
  
  private int nextFlaggedToken() throws IOException {
    peekToken();
    int i = this.currentFlaggedToken;
    consumeToken();
    return i;
  }
  
  private int nextToken() throws IOException {
    int i = peekToken();
    consumeToken();
    return i;
  }
  
  private int nodeEnd(AstNode paramAstNode) {
    return paramAstNode.getPosition() + paramAstNode.getLength();
  }
  
  private static final boolean nowAllSet(int paramInt1, int paramInt2, int paramInt3) {
    boolean bool;
    if ((paramInt1 & paramInt3) != paramInt3 && (paramInt2 & paramInt3) == paramInt3) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private ObjectLiteral objectLiteral() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   4: getfield tokenBeg : I
    //   7: istore_1
    //   8: aload_0
    //   9: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   12: getfield lineno : I
    //   15: istore_2
    //   16: iconst_m1
    //   17: istore_3
    //   18: new java/util/ArrayList
    //   21: dup
    //   22: invokespecial <init> : ()V
    //   25: astore #4
    //   27: aconst_null
    //   28: astore #5
    //   30: aconst_null
    //   31: astore #6
    //   33: aload_0
    //   34: getfield inUseStrictDirective : Z
    //   37: ifeq -> 58
    //   40: new java/util/HashSet
    //   43: dup
    //   44: invokespecial <init> : ()V
    //   47: astore #5
    //   49: new java/util/HashSet
    //   52: dup
    //   53: invokespecial <init> : ()V
    //   56: astore #6
    //   58: aload_0
    //   59: invokespecial getAndResetJsDoc : ()Lcom/trendmicro/hippo/ast/Comment;
    //   62: astore #7
    //   64: aconst_null
    //   65: astore #8
    //   67: iconst_1
    //   68: istore #9
    //   70: iconst_1
    //   71: istore #10
    //   73: aload_0
    //   74: invokespecial peekToken : ()I
    //   77: istore #11
    //   79: aload_0
    //   80: invokespecial getAndResetJsDoc : ()Lcom/trendmicro/hippo/ast/Comment;
    //   83: astore #12
    //   85: iload #11
    //   87: istore #13
    //   89: iload #11
    //   91: sipush #162
    //   94: if_icmpne -> 109
    //   97: aload_0
    //   98: invokespecial consumeToken : ()V
    //   101: aload_0
    //   102: iload #11
    //   104: invokespecial peekUntilNonComment : (I)I
    //   107: istore #13
    //   109: iload #13
    //   111: bipush #87
    //   113: if_icmpne -> 135
    //   116: iload_3
    //   117: iconst_m1
    //   118: if_icmpeq -> 132
    //   121: aload_0
    //   122: iload_1
    //   123: aload #4
    //   125: iload_3
    //   126: invokespecial warnTrailingComma : (ILjava/util/List;I)V
    //   129: goto -> 582
    //   132: goto -> 582
    //   135: aload_0
    //   136: invokespecial objliteralProperty : ()Lcom/trendmicro/hippo/ast/AstNode;
    //   139: astore #14
    //   141: aload #14
    //   143: ifnonnull -> 164
    //   146: aload_0
    //   147: ldc_w 'msg.bad.prop'
    //   150: invokevirtual reportError : (Ljava/lang/String;)V
    //   153: aload #8
    //   155: astore #14
    //   157: iload #9
    //   159: istore #13
    //   161: goto -> 395
    //   164: aload_0
    //   165: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   168: invokevirtual getString : ()Ljava/lang/String;
    //   171: astore #8
    //   173: aload_0
    //   174: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   177: getfield tokenBeg : I
    //   180: istore_3
    //   181: aload_0
    //   182: invokespecial consumeToken : ()V
    //   185: aload_0
    //   186: invokespecial peekToken : ()I
    //   189: istore #11
    //   191: iload #11
    //   193: bipush #90
    //   195: if_icmpeq -> 364
    //   198: iload #11
    //   200: bipush #104
    //   202: if_icmpeq -> 364
    //   205: iload #11
    //   207: bipush #87
    //   209: if_icmpeq -> 364
    //   212: iload #11
    //   214: bipush #88
    //   216: if_icmpne -> 226
    //   219: bipush #8
    //   221: istore #13
    //   223: goto -> 275
    //   226: iload #10
    //   228: istore #13
    //   230: aload #14
    //   232: invokevirtual getType : ()I
    //   235: bipush #39
    //   237: if_icmpne -> 275
    //   240: ldc_w 'get'
    //   243: aload #8
    //   245: invokevirtual equals : (Ljava/lang/Object;)Z
    //   248: ifeq -> 257
    //   251: iconst_2
    //   252: istore #13
    //   254: goto -> 275
    //   257: iload #10
    //   259: istore #13
    //   261: ldc_w 'set'
    //   264: aload #8
    //   266: invokevirtual equals : (Ljava/lang/Object;)Z
    //   269: ifeq -> 275
    //   272: iconst_4
    //   273: istore #13
    //   275: iload #13
    //   277: iconst_2
    //   278: if_icmpeq -> 287
    //   281: iload #13
    //   283: iconst_4
    //   284: if_icmpne -> 309
    //   287: aload_0
    //   288: invokespecial objliteralProperty : ()Lcom/trendmicro/hippo/ast/AstNode;
    //   291: astore #14
    //   293: aload #14
    //   295: ifnonnull -> 305
    //   298: aload_0
    //   299: ldc_w 'msg.bad.prop'
    //   302: invokevirtual reportError : (Ljava/lang/String;)V
    //   305: aload_0
    //   306: invokespecial consumeToken : ()V
    //   309: aload #14
    //   311: ifnonnull -> 320
    //   314: aconst_null
    //   315: astore #14
    //   317: goto -> 395
    //   320: aload_0
    //   321: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   324: invokevirtual getString : ()Ljava/lang/String;
    //   327: astore #8
    //   329: aload_0
    //   330: iload_3
    //   331: aload #14
    //   333: iload #13
    //   335: invokespecial methodDefinition : (ILcom/trendmicro/hippo/ast/AstNode;I)Lcom/trendmicro/hippo/ast/ObjectProperty;
    //   338: astore #15
    //   340: aload #14
    //   342: aload #12
    //   344: invokevirtual setJsDocNode : (Lcom/trendmicro/hippo/ast/Comment;)V
    //   347: aload #4
    //   349: aload #15
    //   351: invokeinterface add : (Ljava/lang/Object;)Z
    //   356: pop
    //   357: aload #8
    //   359: astore #14
    //   361: goto -> 395
    //   364: aload #14
    //   366: aload #12
    //   368: invokevirtual setJsDocNode : (Lcom/trendmicro/hippo/ast/Comment;)V
    //   371: aload #4
    //   373: aload_0
    //   374: aload #14
    //   376: iload #13
    //   378: invokespecial plainProperty : (Lcom/trendmicro/hippo/ast/AstNode;I)Lcom/trendmicro/hippo/ast/ObjectProperty;
    //   381: invokeinterface add : (Ljava/lang/Object;)Z
    //   386: pop
    //   387: iload #9
    //   389: istore #13
    //   391: aload #8
    //   393: astore #14
    //   395: aload_0
    //   396: getfield inUseStrictDirective : Z
    //   399: ifeq -> 556
    //   402: aload #14
    //   404: ifnull -> 556
    //   407: iload #13
    //   409: iconst_1
    //   410: if_icmpeq -> 503
    //   413: iload #13
    //   415: iconst_2
    //   416: if_icmpeq -> 469
    //   419: iload #13
    //   421: iconst_4
    //   422: if_icmpeq -> 435
    //   425: iload #13
    //   427: bipush #8
    //   429: if_icmpeq -> 503
    //   432: goto -> 556
    //   435: aload #6
    //   437: aload #14
    //   439: invokeinterface contains : (Ljava/lang/Object;)Z
    //   444: ifeq -> 456
    //   447: aload_0
    //   448: ldc_w 'msg.dup.obj.lit.prop.strict'
    //   451: aload #14
    //   453: invokevirtual addError : (Ljava/lang/String;Ljava/lang/String;)V
    //   456: aload #6
    //   458: aload #14
    //   460: invokeinterface add : (Ljava/lang/Object;)Z
    //   465: pop
    //   466: goto -> 556
    //   469: aload #5
    //   471: aload #14
    //   473: invokeinterface contains : (Ljava/lang/Object;)Z
    //   478: ifeq -> 490
    //   481: aload_0
    //   482: ldc_w 'msg.dup.obj.lit.prop.strict'
    //   485: aload #14
    //   487: invokevirtual addError : (Ljava/lang/String;Ljava/lang/String;)V
    //   490: aload #5
    //   492: aload #14
    //   494: invokeinterface add : (Ljava/lang/Object;)Z
    //   499: pop
    //   500: goto -> 556
    //   503: aload #5
    //   505: aload #14
    //   507: invokeinterface contains : (Ljava/lang/Object;)Z
    //   512: ifne -> 527
    //   515: aload #6
    //   517: aload #14
    //   519: invokeinterface contains : (Ljava/lang/Object;)Z
    //   524: ifeq -> 536
    //   527: aload_0
    //   528: ldc_w 'msg.dup.obj.lit.prop.strict'
    //   531: aload #14
    //   533: invokevirtual addError : (Ljava/lang/String;Ljava/lang/String;)V
    //   536: aload #5
    //   538: aload #14
    //   540: invokeinterface add : (Ljava/lang/Object;)Z
    //   545: pop
    //   546: aload #6
    //   548: aload #14
    //   550: invokeinterface add : (Ljava/lang/Object;)Z
    //   555: pop
    //   556: aload_0
    //   557: invokespecial getAndResetJsDoc : ()Lcom/trendmicro/hippo/ast/Comment;
    //   560: pop
    //   561: aload_0
    //   562: bipush #90
    //   564: iconst_1
    //   565: invokespecial matchToken : (IZ)Z
    //   568: ifeq -> 582
    //   571: aload_0
    //   572: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   575: getfield tokenEnd : I
    //   578: istore_3
    //   579: goto -> 64
    //   582: aload_0
    //   583: bipush #87
    //   585: ldc_w 'msg.no.brace.prop'
    //   588: iconst_1
    //   589: invokespecial mustMatchToken : (ILjava/lang/String;Z)Z
    //   592: pop
    //   593: new com/trendmicro/hippo/ast/ObjectLiteral
    //   596: dup
    //   597: iload_1
    //   598: aload_0
    //   599: getfield ts : Lcom/trendmicro/hippo/TokenStream;
    //   602: getfield tokenEnd : I
    //   605: iload_1
    //   606: isub
    //   607: invokespecial <init> : (II)V
    //   610: astore #14
    //   612: aload #7
    //   614: ifnull -> 624
    //   617: aload #14
    //   619: aload #7
    //   621: invokevirtual setJsDocNode : (Lcom/trendmicro/hippo/ast/Comment;)V
    //   624: aload #14
    //   626: aload #4
    //   628: invokevirtual setElements : (Ljava/util/List;)V
    //   631: aload #14
    //   633: iload_2
    //   634: invokevirtual setLineno : (I)V
    //   637: aload #14
    //   639: areturn
  }
  
  private AstNode objliteralProperty() throws IOException {
    switch (peekToken()) {
      default:
        return (AstNode)((this.compilerEnv.isReservedKeywordAsIdentifier() && TokenStream.isKeyword(this.ts.getString(), this.compilerEnv.getLanguageVersion(), this.inUseStrictDirective)) ? createNameNode() : null);
      case 41:
        return (AstNode)createStringLiteral();
      case 40:
        return (AstNode)new NumberLiteral(this.ts.tokenBeg, this.ts.getString(), this.ts.getNumber());
      case 39:
        break;
    } 
    return (AstNode)createNameNode();
  }
  
  private AstNode orExpr() throws IOException {
    InfixExpression infixExpression;
    AstNode astNode1 = andExpr();
    AstNode astNode2 = astNode1;
    if (matchToken(105, true)) {
      int i = this.ts.tokenBeg;
      infixExpression = new InfixExpression(105, astNode1, orExpr(), i);
    } 
    return (AstNode)infixExpression;
  }
  
  private AstNode parenExpr() throws IOException {
    boolean bool = this.inForInit;
    this.inForInit = false;
    try {
      AstNode astNode;
      ErrorNode errorNode;
      Comment comment1 = getAndResetJsDoc();
      int i = this.ts.lineno;
      int j = this.ts.tokenBeg;
      if (peekToken() == 89) {
        EmptyExpression emptyExpression = new EmptyExpression();
        this(j);
      } else {
        astNode = expr();
      } 
      if (peekToken() == 120) {
        astNode = generatorExpression(astNode, j);
        return astNode;
      } 
      mustMatchToken(89, "msg.no.paren", true);
      if (astNode.getType() == 129 && peekToken() != 165) {
        reportError("msg.syntax");
        errorNode = makeErrorNode();
        return (AstNode)errorNode;
      } 
      int k = this.ts.tokenEnd;
      ParenthesizedExpression parenthesizedExpression = new ParenthesizedExpression();
      this(j, k - j, (AstNode)errorNode);
      parenthesizedExpression.setLineno(i);
      Comment comment2 = comment1;
      if (comment1 == null)
        comment2 = getAndResetJsDoc(); 
      if (comment2 != null)
        parenthesizedExpression.setJsDocNode(comment2); 
      return (AstNode)parenthesizedExpression;
    } finally {
      this.inForInit = bool;
    } 
  }
  
  private AstRoot parse() throws IOException {
    AstRoot astRoot = new AstRoot(0);
    this.currentScriptOrFn = (ScriptNode)astRoot;
    this.currentScope = (Scope)astRoot;
    int i = this.ts.lineno;
    int j = 0;
    int k = 1;
    boolean bool1 = this.inUseStrictDirective;
    boolean bool2 = this.defaultUseStrictDirective;
    this.inUseStrictDirective = bool2;
    int m = j;
    int n = k;
    if (bool2) {
      astRoot.setInStrictMode(true);
      n = k;
      m = j;
    } 
    while (true) {
      k = m;
      j = n;
      try {
        int i1 = peekToken();
        if (i1 > 0) {
          AstNode astNode;
          if (i1 == 110) {
            k = m;
            j = n;
            consumeToken();
            k = m;
            j = n;
            try {
              if (this.calledByCompileFunction) {
                i1 = 2;
              } else {
                i1 = 1;
              } 
              k = m;
              j = n;
              FunctionNode functionNode = function(i1);
              i1 = n;
            } catch (ParserException parserException) {
              this.inUseStrictDirective = bool1;
            } 
          } else if (i1 == 162) {
            k = m;
            j = n;
            astNode = (AstNode)this.scannedComments.get(this.scannedComments.size() - 1);
            k = m;
            j = n;
            consumeToken();
            i1 = n;
          } else {
            k = m;
            j = n;
            AstNode astNode1 = statement();
            i1 = n;
            astNode = astNode1;
            if (n) {
              k = m;
              j = n;
              String str = getDirective(astNode1);
              if (str == null) {
                i1 = 0;
                astNode = astNode1;
              } else {
                i1 = n;
                astNode = astNode1;
                k = m;
                j = n;
                if (str.equals("use strict")) {
                  k = m;
                  j = n;
                  this.inUseStrictDirective = true;
                  k = m;
                  j = n;
                  astRoot.setInStrictMode(true);
                  astNode = astNode1;
                  i1 = n;
                } 
              } 
            } 
          } 
          k = m;
          j = i1;
          m = getNodeEnd(astNode);
          k = m;
          j = i1;
          astRoot.addChildToBack((Node)astNode);
          k = m;
          j = i1;
          astNode.setParent((AstNode)astRoot);
          n = i1;
          continue;
        } 
      } catch (StackOverflowError stackOverflowError) {
      
      } finally {
        Exception exception;
      } 
      this.inUseStrictDirective = bool1;
    } 
  }
  
  private AstNode parseFunctionBody(int paramInt, FunctionNode paramFunctionNode) throws IOException {
    int i = 0;
    int j = i;
    if (!matchToken(86, true))
      if (this.compilerEnv.getLanguageVersion() < 180 && paramInt != 4) {
        reportError("msg.no.brace.body");
        j = i;
      } else {
        j = 1;
      }  
    if (paramInt == 4) {
      i = 1;
    } else {
      i = 0;
    } 
    this.nestingOfFunction++;
    int k = this.ts.tokenBeg;
    Block block = new Block(k);
    paramInt = 1;
    boolean bool = this.inUseStrictDirective;
    block.setLineno(this.ts.lineno);
    if (j) {
      try {
        AstNode astNode = assignExpr();
        ReturnStatement returnStatement = new ReturnStatement();
        this(astNode.getPosition(), astNode.getLength(), astNode);
        returnStatement.putProp(25, Boolean.TRUE);
        block.putProp(25, Boolean.TRUE);
      } catch (ParserException parserException) {
      
      } finally {
        this.nestingOfFunction--;
        this.inUseStrictDirective = bool;
      } 
    } else {
      while (true) {
        i = peekToken();
        if (i != -1 && i != 0 && i != 87) {
          FunctionNode functionNode;
          if (i != 110) {
            if (i != 162) {
              AstNode astNode2 = statement();
              i = paramInt;
              AstNode astNode1 = astNode2;
              if (paramInt != 0) {
                String str = getDirective(astNode2);
                if (str == null) {
                  i = 0;
                  astNode1 = astNode2;
                } else {
                  i = paramInt;
                  astNode1 = astNode2;
                  if (str.equals("use strict")) {
                    this.inUseStrictDirective = true;
                    paramFunctionNode.setInStrictMode(true);
                    i = paramInt;
                    astNode1 = astNode2;
                    if (!bool) {
                      setRequiresActivation();
                      i = paramInt;
                      astNode1 = astNode2;
                    } 
                  } 
                } 
              } 
            } else {
              consumeToken();
              AstNode astNode = (AstNode)this.scannedComments.get(this.scannedComments.size() - 1);
              i = paramInt;
            } 
          } else {
            consumeToken();
            functionNode = function(1);
            i = paramInt;
          } 
          block.addStatement((AstNode)functionNode);
          paramInt = i;
          continue;
        } 
        break;
      } 
    } 
    this.nestingOfFunction--;
    this.inUseStrictDirective = bool;
    i = this.ts.tokenEnd;
    getAndResetJsDoc();
    paramInt = i;
    if (j == 0) {
      paramInt = i;
      if (mustMatchToken(87, "msg.no.brace.after.body", true))
        paramInt = this.ts.tokenEnd; 
    } 
    block.setLength(paramInt - k);
    return (AstNode)block;
  }
  
  private void parseFunctionParams(FunctionNode paramFunctionNode) throws IOException {
    if (matchToken(89, true)) {
      paramFunctionNode.setRp(this.ts.tokenBeg - paramFunctionNode.getPosition());
      return;
    } 
    HashMap<Object, Object> hashMap = null;
    HashSet<String> hashSet = new HashSet();
    while (true) {
      HashMap<Object, Object> hashMap1;
      int i = peekToken();
      if (i == 84 || i == 86) {
        AstNode astNode = destructuringPrimaryExpr();
        markDestructuring(astNode);
        paramFunctionNode.addParam(astNode);
        HashMap<Object, Object> hashMap2 = hashMap;
        if (hashMap == null)
          hashMap2 = new HashMap<>(); 
        String str = this.currentScriptOrFn.getNextTempName();
        defineSymbol(88, str, false);
        hashMap2.put(str, astNode);
        hashMap1 = hashMap2;
      } else if (mustMatchToken(39, "msg.no.parm", true)) {
        Name name = createNameNode();
        Comment comment = getAndResetJsDoc();
        if (comment != null)
          name.setJsDocNode(comment); 
        paramFunctionNode.addParam((AstNode)name);
        String str = this.ts.getString();
        defineSymbol(88, str);
        if (this.inUseStrictDirective) {
          if ("eval".equals(str) || "arguments".equals(str))
            reportError("msg.bad.id.strict", str); 
          if (hashSet.contains(str))
            addError("msg.dup.param.strict", str); 
          hashSet.add(str);
        } 
      } else {
        paramFunctionNode.addParam((AstNode)makeErrorNode());
      } 
      if (!matchToken(90, true)) {
        if (hashMap1 != null) {
          Node node = new Node(90);
          for (Map.Entry<Object, Object> entry : hashMap1.entrySet())
            node.addChildToBack(createDestructuringAssignment(123, (Node)entry.getValue(), createName((String)entry.getKey()))); 
          paramFunctionNode.putProp(23, node);
        } 
        if (mustMatchToken(89, "msg.no.paren.after.parms", true))
          paramFunctionNode.setRp(this.ts.tokenBeg - paramFunctionNode.getPosition()); 
        return;
      } 
    } 
  }
  
  private int peekFlaggedToken() throws IOException {
    peekToken();
    return this.currentFlaggedToken;
  }
  
  private int peekToken() throws IOException {
    if (this.currentFlaggedToken != 0)
      return this.currentToken; 
    int i = this.ts.getLineno();
    int j = this.ts.getToken();
    int k = 0;
    while (true) {
      if (j == 1 || j == 162) {
        if (j == 1) {
          i++;
          k = 1;
          j = this.ts.getToken();
          continue;
        } 
        if (this.compilerEnv.isRecordingComments()) {
          String str = this.ts.getAndResetCurrentComment();
          recordComment(i, str);
          getNumberOfEols(str);
        } else {
          j = this.ts.getToken();
          continue;
        } 
      } 
      this.currentToken = j;
      if (k) {
        k = 65536;
      } else {
        k = 0;
      } 
      this.currentFlaggedToken = k | j;
      return this.currentToken;
    } 
  }
  
  private int peekTokenOrEOL() throws IOException {
    int i = peekToken();
    if ((this.currentFlaggedToken & 0x10000) != 0)
      i = 1; 
    return i;
  }
  
  private int peekUntilNonComment(int paramInt) throws IOException {
    while (paramInt == 162) {
      consumeToken();
      paramInt = peekToken();
    } 
    return paramInt;
  }
  
  private ObjectProperty plainProperty(AstNode paramAstNode, int paramInt) throws IOException {
    int i = peekToken();
    if ((i == 90 || i == 87) && paramInt == 39 && this.compilerEnv.getLanguageVersion() >= 180) {
      if (!this.inDestructuringAssignment)
        reportError("msg.bad.object.init"); 
      Name name = new Name(paramAstNode.getPosition(), paramAstNode.getString());
      ObjectProperty objectProperty1 = new ObjectProperty();
      objectProperty1.putProp(26, Boolean.TRUE);
      objectProperty1.setLeftAndRight(paramAstNode, (AstNode)name);
      return objectProperty1;
    } 
    mustMatchToken(104, "msg.no.colon.prop", true);
    ObjectProperty objectProperty = new ObjectProperty();
    objectProperty.setOperatorPosition(this.ts.tokenBeg);
    objectProperty.setLeftAndRight(paramAstNode, assignExpr());
    return objectProperty;
  }
  
  private AstNode primaryExpr() throws IOException {
    int i = peekFlaggedToken();
    int j = 0xFFFF & i;
    if (j != -1) {
      if (j != 0) {
        if (j != 24) {
          if (j != 84) {
            if (j != 86) {
              if (j != 88) {
                if (j != 101) {
                  if (j != 110) {
                    if (j != 128) {
                      if (j != 148) {
                        if (j != 154) {
                          String str1;
                          String str2;
                          switch (j) {
                            default:
                              consumeToken();
                              reportError("msg.syntax");
                              consumeToken();
                              return (AstNode)makeErrorNode();
                            case 42:
                            case 43:
                            case 44:
                            case 45:
                              consumeToken();
                              i = this.ts.tokenBeg;
                              return (AstNode)new KeywordLiteral(i, this.ts.tokenEnd - i, j);
                            case 41:
                              consumeToken();
                              return (AstNode)createStringLiteral();
                            case 40:
                              consumeToken();
                              str1 = this.ts.getString();
                              if (this.inUseStrictDirective && this.ts.isNumberOldOctal())
                                reportError("msg.no.old.octal.strict"); 
                              str2 = str1;
                              if (this.ts.isNumberBinary()) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("0b");
                                stringBuilder.append(str1);
                                str2 = stringBuilder.toString();
                              } 
                              str1 = str2;
                              if (this.ts.isNumberOldOctal()) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("0");
                                stringBuilder.append(str2);
                                str1 = stringBuilder.toString();
                              } 
                              str2 = str1;
                              if (this.ts.isNumberOctal()) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("0o");
                                stringBuilder.append(str1);
                                str2 = stringBuilder.toString();
                              } 
                              str1 = str2;
                              if (this.ts.isNumberHex()) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("0x");
                                stringBuilder.append(str2);
                                str1 = stringBuilder.toString();
                              } 
                              return (AstNode)new NumberLiteral(this.ts.tokenBeg, str1, this.ts.getNumber());
                            case 39:
                              break;
                          } 
                          consumeToken();
                          return name(i, j);
                        } 
                        consumeToken();
                        return let(false, this.ts.tokenBeg);
                      } 
                      consumeToken();
                      mustHaveXML();
                      return attributeAccess();
                    } 
                    consumeToken();
                    reportError("msg.reserved.id", this.ts.getString());
                  } else {
                    consumeToken();
                    return (AstNode)function(2);
                  } 
                } else {
                  consumeToken();
                  this.ts.readRegExp(j);
                  j = this.ts.tokenBeg;
                  RegExpLiteral regExpLiteral = new RegExpLiteral(j, this.ts.tokenEnd - j);
                  regExpLiteral.setValue(this.ts.getString());
                  regExpLiteral.setFlags(this.ts.readAndClearRegExpFlags());
                  return (AstNode)regExpLiteral;
                } 
              } else {
                consumeToken();
                return parenExpr();
              } 
            } else {
              consumeToken();
              return (AstNode)objectLiteral();
            } 
          } else {
            consumeToken();
            return arrayLiteral();
          } 
        } else {
          consumeToken();
          this.ts.readRegExp(j);
          j = this.ts.tokenBeg;
          RegExpLiteral regExpLiteral = new RegExpLiteral(j, this.ts.tokenEnd - j);
          regExpLiteral.setValue(this.ts.getString());
          regExpLiteral.setFlags(this.ts.readAndClearRegExpFlags());
          return (AstNode)regExpLiteral;
        } 
      } else {
        consumeToken();
        reportError("msg.unexpected.eof");
      } 
    } else {
      consumeToken();
    } 
    consumeToken();
    return (AstNode)makeErrorNode();
  }
  
  private AstNode propertyAccess(int paramInt, AstNode paramAstNode) throws IOException {
    PropertyGet propertyGet1;
    AstNode astNode;
    PropertyGet propertyGet2;
    if (paramAstNode == null)
      codeBug(); 
    byte b = 0;
    int i = this.ts.lineno;
    int j = this.ts.tokenBeg;
    consumeToken();
    if (paramInt == 144) {
      mustHaveXML();
      b = 4;
    } 
    if (!this.compilerEnv.isXmlAvailable()) {
      if (nextToken() != 39 && (!this.compilerEnv.isReservedKeywordAsIdentifier() || !TokenStream.isKeyword(this.ts.getString(), this.compilerEnv.getLanguageVersion(), this.inUseStrictDirective)))
        reportError("msg.no.name.after.dot"); 
      propertyGet1 = new PropertyGet(paramAstNode, createNameNode(true, 33), j);
      propertyGet1.setLineno(i);
      return (AstNode)propertyGet1;
    } 
    i = nextToken();
    if (i != 23) {
      if (i != 39) {
        if (i != 50) {
          if (i != 128) {
            if (i != 148) {
              if (this.compilerEnv.isReservedKeywordAsIdentifier()) {
                String str = Token.keywordToName(i);
                if (str != null) {
                  saveNameTokenData(this.ts.tokenBeg, str, this.ts.lineno);
                  astNode = propertyName(-1, str, b);
                } else {
                  reportError("msg.no.name.after.dot");
                  return (AstNode)makeErrorNode();
                } 
              } else {
                reportError("msg.no.name.after.dot");
                return (AstNode)makeErrorNode();
              } 
            } else {
              astNode = attributeAccess();
            } 
          } else {
            String str = this.ts.getString();
            saveNameTokenData(this.ts.tokenBeg, str, this.ts.lineno);
            astNode = propertyName(-1, str, b);
          } 
        } else {
          saveNameTokenData(this.ts.tokenBeg, "throw", this.ts.lineno);
          astNode = propertyName(-1, "throw", b);
        } 
      } else {
        astNode = propertyName(-1, this.ts.getString(), b);
      } 
    } else {
      saveNameTokenData(this.ts.tokenBeg, "*", this.ts.lineno);
      astNode = propertyName(-1, "*", b);
    } 
    boolean bool = astNode instanceof com.trendmicro.hippo.ast.XmlRef;
    if (bool) {
      XmlMemberGet xmlMemberGet = new XmlMemberGet();
    } else {
      propertyGet2 = new PropertyGet();
    } 
    if (bool && paramInt == 109)
      propertyGet2.setType(109); 
    paramInt = propertyGet1.getPosition();
    propertyGet2.setPosition(paramInt);
    propertyGet2.setLength(getNodeEnd(astNode) - paramInt);
    propertyGet2.setOperatorPosition(j - paramInt);
    propertyGet2.setLineno(propertyGet1.getLineno());
    propertyGet2.setLeft((AstNode)propertyGet1);
    propertyGet2.setRight(astNode);
    return (AstNode)propertyGet2;
  }
  
  private AstNode propertyName(int paramInt1, String paramString, int paramInt2) throws IOException {
    int i;
    if (paramInt1 != -1) {
      i = paramInt1;
    } else {
      i = this.ts.tokenBeg;
    } 
    int j = this.ts.lineno;
    int k = -1;
    Name name2 = createNameNode(true, this.currentToken);
    Name name3 = null;
    Name name1 = name2;
    if (matchToken(145, true)) {
      name3 = name2;
      k = this.ts.tokenBeg;
      int m = nextToken();
      if (m != 23) {
        if (m != 39) {
          if (m != 84) {
            reportError("msg.no.name.after.coloncolon");
            return (AstNode)makeErrorNode();
          } 
          return (AstNode)xmlElemRef(paramInt1, name3, k);
        } 
        name1 = createNameNode();
      } else {
        saveNameTokenData(this.ts.tokenBeg, "*", this.ts.lineno);
        name1 = createNameNode(false, -1);
      } 
    } 
    if (name3 == null && paramInt2 == 0 && paramInt1 == -1)
      return (AstNode)name1; 
    XmlPropRef xmlPropRef = new XmlPropRef(i, getNodeEnd((AstNode)name1) - i);
    xmlPropRef.setAtPos(paramInt1);
    xmlPropRef.setNamespace(name3);
    xmlPropRef.setColonPos(k);
    xmlPropRef.setPropName(name1);
    xmlPropRef.setLineno(j);
    return (AstNode)xmlPropRef;
  }
  
  private String readFully(Reader paramReader) throws IOException {
    paramReader = new BufferedReader(paramReader);
    try {
      char[] arrayOfChar = new char[1024];
      StringBuilder stringBuilder = new StringBuilder();
      this(1024);
      while (true) {
        int i = paramReader.read(arrayOfChar, 0, 1024);
        if (i != -1) {
          stringBuilder.append(arrayOfChar, 0, i);
          continue;
        } 
        return stringBuilder.toString();
      } 
    } finally {
      try {
        paramReader.close();
      } finally {
        paramReader = null;
      } 
    } 
  }
  
  private void recordComment(int paramInt, String paramString) {
    if (this.scannedComments == null)
      this.scannedComments = new ArrayList<>(); 
    Comment comment = new Comment(this.ts.tokenBeg, this.ts.getTokenLength(), this.ts.commentType, paramString);
    if (this.ts.commentType == Token.CommentType.JSDOC && this.compilerEnv.isRecordingLocalJsDocComments()) {
      Comment comment1 = new Comment(this.ts.tokenBeg, this.ts.getTokenLength(), this.ts.commentType, paramString);
      this.currentJsDocComment = comment1;
      comment1.setLineno(paramInt);
    } 
    comment.setLineno(paramInt);
    this.scannedComments.add(comment);
  }
  
  private void recordLabel(Label paramLabel, LabeledStatement paramLabeledStatement) throws IOException {
    if (peekToken() != 104)
      codeBug(); 
    consumeToken();
    String str = paramLabel.getName();
    Map<String, LabeledStatement> map = this.labelSet;
    if (map == null) {
      this.labelSet = new HashMap<>();
    } else {
      LabeledStatement labeledStatement = map.get(str);
      if (labeledStatement != null) {
        if (this.compilerEnv.isIdeMode()) {
          Label label = labeledStatement.getLabelByName(str);
          reportError("msg.dup.label", label.getAbsolutePosition(), label.getLength());
        } 
        reportError("msg.dup.label", paramLabel.getPosition(), paramLabel.getLength());
      } 
    } 
    paramLabeledStatement.addLabel(paramLabel);
    this.labelSet.put(str, paramLabeledStatement);
  }
  
  private AstNode relExpr() throws IOException {
    AstNode astNode = shiftExpr();
    while (true) {
      int i = peekToken();
      int j = this.ts.tokenBeg;
      if (i != 52) {
        if (i != 53)
          switch (i) {
            default:
              return astNode;
            case 14:
            case 15:
            case 16:
            case 17:
              break;
          }  
      } else if (this.inForInit) {
      
      } 
      consumeToken();
      InfixExpression infixExpression = new InfixExpression(i, astNode, shiftExpr(), j);
    } 
  }
  
  private AstNode returnOrYield(int paramInt, boolean paramBoolean) throws IOException {
    ReturnStatement returnStatement;
    ExpressionStatement expressionStatement;
    boolean bool = insideFunction();
    byte b = 4;
    if (!bool) {
      String str;
      if (paramInt == 4) {
        str = "msg.bad.return";
      } else {
        str = "msg.bad.yield";
      } 
      reportError(str);
    } 
    consumeToken();
    int i = this.ts.lineno;
    int j = this.ts.tokenBeg;
    int k = this.ts.tokenEnd;
    AstNode astNode = null;
    int m = peekTokenOrEOL();
    if (m != -1 && m != 0 && m != 1 && m != 73 && m != 83 && m != 85 && m != 87 && m != 89) {
      astNode = expr();
      k = getNodeEnd(astNode);
    } 
    m = this.endFlags;
    if (paramInt == 4) {
      int n = this.endFlags;
      paramInt = b;
      if (astNode == null)
        paramInt = 2; 
      this.endFlags = n | paramInt;
      ReturnStatement returnStatement1 = new ReturnStatement(j, k - j, astNode);
      returnStatement = returnStatement1;
      if (nowAllSet(m, this.endFlags, 6)) {
        addStrictWarning("msg.return.inconsistent", "", j, k - j);
        returnStatement = returnStatement1;
      } 
    } else {
      if (!insideFunction())
        reportError("msg.bad.yield"); 
      this.endFlags |= 0x8;
      Yield yield2 = new Yield(j, k - j, (AstNode)returnStatement);
      setRequiresActivation();
      setIsGenerator();
      Yield yield1 = yield2;
      if (!paramBoolean)
        expressionStatement = new ExpressionStatement((AstNode)yield2); 
    } 
    if (insideFunction() && nowAllSet(m, this.endFlags, 12)) {
      Name name = ((FunctionNode)this.currentScriptOrFn).getFunctionName();
      if (name == null || name.length() == 0) {
        addError("msg.anon.generator.returns", "");
        expressionStatement.setLineno(i);
        return (AstNode)expressionStatement;
      } 
      addError("msg.generator.returns", name.getIdentifier());
    } 
    expressionStatement.setLineno(i);
    return (AstNode)expressionStatement;
  }
  
  private void saveNameTokenData(int paramInt1, String paramString, int paramInt2) {
    this.prevNameTokenStart = paramInt1;
    this.prevNameTokenString = paramString;
    this.prevNameTokenLineno = paramInt2;
  }
  
  private AstNode shiftExpr() throws IOException {
    AstNode astNode = addExpr();
    while (true) {
      int i = peekToken();
      int j = this.ts.tokenBeg;
      switch (i) {
        default:
          return astNode;
        case 18:
        case 19:
        case 20:
          break;
      } 
      consumeToken();
      InfixExpression infixExpression = new InfixExpression(i, astNode, addExpr(), j);
    } 
  }
  
  private AstNode statement() throws IOException {
    int i = this.ts.tokenBeg;
    try {
      AstNode astNode = statementHelper();
      if (astNode != null) {
        if (this.compilerEnv.isStrictMode() && !astNode.hasSideEffects()) {
          String str;
          int j = astNode.getPosition();
          j = Math.max(j, lineBeginningFor(j));
          if (astNode instanceof EmptyStatement) {
            str = "msg.extra.trailing.semi";
          } else {
            str = "msg.no.side.effects";
          } 
          addStrictWarning(str, "", j, nodeEnd(astNode) - j);
        } 
        if (peekToken() == 162 && astNode.getLineno() == ((Comment)this.scannedComments.get(this.scannedComments.size() - 1)).getLineno()) {
          astNode.setInlineComment((AstNode)this.scannedComments.get(this.scannedComments.size() - 1));
          consumeToken();
        } 
        return astNode;
      } 
    } catch (ParserException parserException) {}
    while (true) {
      int j = peekTokenOrEOL();
      consumeToken();
      if (j != -1 && j != 0 && j != 1 && j != 83)
        continue; 
      break;
    } 
    return (AstNode)new EmptyStatement(i, this.ts.tokenBeg - i);
  }
  
  private AstNode statementHelper() throws IOException {
    LabeledStatement labeledStatement = this.currentLabel;
    if (labeledStatement != null && labeledStatement.getStatement() != null)
      this.currentLabel = null; 
    int i = peekToken();
    int j = this.ts.tokenBeg;
    if (i != -1) {
      if (i != 4)
        if (i != 39) {
          if (i != 50) {
            if (i != 73) {
              EmptyStatement emptyStatement;
              if (i != 86) {
                if (i != 110) {
                  if (i != 113) {
                    if (i != 115) {
                      if (i != 82) {
                        if (i != 83) {
                          if (i != 154) {
                            if (i != 155)
                              if (i != 161) {
                                if (i != 162) {
                                  ExpressionStatement expressionStatement;
                                  ContinueStatement continueStatement;
                                  BreakStatement breakStatement;
                                  AstNode astNode1;
                                  switch (i) {
                                    default:
                                      j = this.ts.lineno;
                                      expressionStatement = new ExpressionStatement(expr(), true ^ insideFunction());
                                      expressionStatement.setLineno(j);
                                      autoInsertSemicolon((AstNode)expressionStatement);
                                      return (AstNode)expressionStatement;
                                    case 124:
                                      if (this.inUseStrictDirective)
                                        reportError("msg.no.with.strict"); 
                                      return (AstNode)withStatement();
                                    case 122:
                                      continueStatement = continueStatement();
                                      autoInsertSemicolon((AstNode)continueStatement);
                                      return (AstNode)continueStatement;
                                    case 121:
                                      breakStatement = breakStatement();
                                      autoInsertSemicolon((AstNode)breakStatement);
                                      return (AstNode)breakStatement;
                                    case 120:
                                      return (AstNode)forLoop();
                                    case 119:
                                      return (AstNode)doLoop();
                                    case 118:
                                      return (AstNode)whileLoop();
                                    case 117:
                                      astNode1 = defaultXmlNamespace();
                                      autoInsertSemicolon(astNode1);
                                      return astNode1;
                                    case 123:
                                      break;
                                  } 
                                } else {
                                  List<Comment> list = this.scannedComments;
                                  return (AstNode)list.get(list.size() - 1);
                                } 
                              } else {
                                consumeToken();
                                KeywordLiteral keywordLiteral = new KeywordLiteral(this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg, i);
                                keywordLiteral.setLineno(this.ts.lineno);
                                autoInsertSemicolon((AstNode)keywordLiteral);
                                return (AstNode)keywordLiteral;
                              }  
                            consumeToken();
                            j = this.ts.lineno;
                            VariableDeclaration variableDeclaration = variables(this.currentToken, this.ts.tokenBeg, true);
                            variableDeclaration.setLineno(j);
                          } else {
                            AstNode astNode1 = letStatement();
                            if (!(astNode1 instanceof VariableDeclaration) || peekToken() != 83)
                              return astNode1; 
                          } 
                        } else {
                          consumeToken();
                          j = this.ts.tokenBeg;
                          emptyStatement = new EmptyStatement(j, this.ts.tokenEnd - j);
                          emptyStatement.setLineno(this.ts.lineno);
                          return (AstNode)emptyStatement;
                        } 
                      } else {
                        return (AstNode)tryStatement();
                      } 
                    } else {
                      return (AstNode)switchStatement();
                    } 
                  } else {
                    return (AstNode)ifStatement();
                  } 
                } else {
                  consumeToken();
                  return (AstNode)function(3);
                } 
              } else {
                return block();
              } 
              autoInsertSemicolon((AstNode)emptyStatement);
              return (AstNode)emptyStatement;
            } 
          } else {
            ThrowStatement throwStatement = throwStatement();
            autoInsertSemicolon((AstNode)throwStatement);
            return (AstNode)throwStatement;
          } 
        } else {
          AstNode astNode1 = nameOrLabel();
          if (!(astNode1 instanceof ExpressionStatement))
            return astNode1; 
          autoInsertSemicolon(astNode1);
          return astNode1;
        }  
      AstNode astNode = returnOrYield(i, false);
      autoInsertSemicolon(astNode);
      return astNode;
    } 
    consumeToken();
    return (AstNode)makeErrorNode();
  }
  
  private AstNode statements() throws IOException {
    return statements(null);
  }
  
  private AstNode statements(AstNode paramAstNode) throws IOException {
    Block block;
    if (this.currentToken != 86 && !this.compilerEnv.isIdeMode())
      codeBug(); 
    int i = this.ts.tokenBeg;
    if (paramAstNode == null)
      block = new Block(i); 
    block.setLineno(this.ts.lineno);
    while (true) {
      int j = peekToken();
      if (j > 0 && j != 87) {
        block.addChild(statement());
        continue;
      } 
      break;
    } 
    block.setLength(this.ts.tokenBeg - i);
    return (AstNode)block;
  }
  
  private SwitchStatement switchStatement() throws IOException {
    if (this.currentToken != 115)
      codeBug(); 
    consumeToken();
    int i = this.ts.tokenBeg;
    SwitchStatement switchStatement = new SwitchStatement(i);
    if (mustMatchToken(88, "msg.no.paren.switch", true))
      switchStatement.setLp(this.ts.tokenBeg - i); 
    switchStatement.setLineno(this.ts.lineno);
    switchStatement.setExpression(expr());
    enterSwitch(switchStatement);
    try {
      if (mustMatchToken(89, "msg.no.paren.after.switch", true))
        switchStatement.setRp(this.ts.tokenBeg - i); 
      mustMatchToken(86, "msg.no.brace.switch", true);
      boolean bool = false;
      while (true) {
        int j = nextToken();
        int k = this.ts.tokenBeg;
        int m = this.ts.lineno;
        AstNode astNode = null;
        if (j != 87) {
          if (j != 162) {
            if (j != 116) {
              if (j != 117) {
                reportError("msg.bad.switch");
                break;
              } 
              if (bool)
                reportError("msg.double.switch.default"); 
              bool = true;
              mustMatchToken(104, "msg.no.colon.case", true);
            } else {
              astNode = expr();
              mustMatchToken(104, "msg.no.colon.case", true);
            } 
            SwitchCase switchCase = new SwitchCase();
            this(k);
            switchCase.setExpression(astNode);
            switchCase.setLength(this.ts.tokenEnd - i);
            switchCase.setLineno(m);
            while (true) {
              m = peekToken();
              if (m != 87 && m != 116 && m != 117 && m != 0) {
                if (m == 162) {
                  Comment comment = this.scannedComments.get(this.scannedComments.size() - 1);
                  if (switchCase.getInlineComment() == null && comment.getLineno() == switchCase.getLineno()) {
                    switchCase.setInlineComment((AstNode)comment);
                  } else {
                    switchCase.addStatement((AstNode)comment);
                  } 
                  consumeToken();
                  continue;
                } 
                switchCase.addStatement(statement());
                continue;
              } 
              break;
            } 
            switchStatement.addCase(switchCase);
            continue;
          } 
          switchStatement.addChild((AstNode)this.scannedComments.get(this.scannedComments.size() - 1));
          continue;
        } 
        switchStatement.setLength(this.ts.tokenEnd - i);
        break;
      } 
      return switchStatement;
    } finally {
      exitSwitch();
    } 
  }
  
  private ThrowStatement throwStatement() throws IOException {
    if (this.currentToken != 50)
      codeBug(); 
    consumeToken();
    int i = this.ts.tokenBeg;
    int j = this.ts.lineno;
    if (peekTokenOrEOL() == 1)
      reportError("msg.bad.throw.eol"); 
    ThrowStatement throwStatement = new ThrowStatement(i, expr());
    throwStatement.setLineno(j);
    return throwStatement;
  }
  
  private TryStatement tryStatement() throws IOException {
    ArrayList<CatchClause> arrayList;
    AstNode astNode2;
    if (this.currentToken != 82)
      codeBug(); 
    consumeToken();
    Comment comment1 = getAndResetJsDoc();
    int i = this.ts.tokenBeg;
    int j = this.ts.lineno;
    int k = -1;
    TryStatement tryStatement = new TryStatement(i);
    int m = peekToken();
    int n = m;
    if (m == 162) {
      List<Comment> list = this.scannedComments;
      tryStatement.setInlineComment((AstNode)list.get(list.size() - 1));
      consumeToken();
      n = peekToken();
    } 
    if (n != 86)
      reportError("msg.no.brace.try"); 
    AstNode astNode1 = getNextStatementAfterInlineComments((AstNode)tryStatement);
    int i1 = getNodeEnd(astNode1);
    Comment comment2 = null;
    Comment comment3 = null;
    m = 0;
    int i2 = peekToken();
    if (i2 == 125) {
      comment2 = comment3;
      i2 = n;
      n = k;
      while (matchToken(125, true)) {
        ArrayList<CatchClause> arrayList1;
        byte b;
        int i3 = this.ts.lineno;
        if (m != 0)
          reportError("msg.catch.unreachable"); 
        int i4 = this.ts.tokenBeg;
        if (mustMatchToken(88, "msg.no.paren.catch", true)) {
          i1 = this.ts.tokenBeg;
        } else {
          i1 = -1;
        } 
        mustMatchToken(39, "msg.bad.catchcond", true);
        Name name = createNameNode();
        comment3 = getAndResetJsDoc();
        if (comment3 != null)
          name.setJsDocNode(comment3); 
        String str = name.getIdentifier();
        if (this.inUseStrictDirective && ("eval".equals(str) || "arguments".equals(str)))
          reportError("msg.bad.id.strict", str); 
        if (matchToken(113, true)) {
          k = this.ts.tokenBeg;
          astNode2 = expr();
        } else {
          m = 1;
          k = -1;
          str = null;
        } 
        if (mustMatchToken(89, "msg.bad.catchcond", true)) {
          b = this.ts.tokenBeg;
        } else {
          b = -1;
        } 
        mustMatchToken(86, "msg.no.brace.catchblock", true);
        Block block = (Block)statements();
        int i5 = getNodeEnd((AstNode)block);
        CatchClause catchClause = new CatchClause(i4);
        catchClause.setVarName(name);
        catchClause.setCatchCondition((AstNode)str);
        catchClause.setBody(block);
        if (k != -1)
          catchClause.setIfPosition(k - i4); 
        catchClause.setParens(i1, b);
        catchClause.setLineno(i3);
        if (mustMatchToken(87, "msg.no.brace.after.body", true)) {
          i1 = this.ts.tokenEnd;
        } else {
          i1 = i5;
        } 
        catchClause.setLength(i1 - i4);
        Comment comment = comment2;
        if (comment2 == null)
          arrayList1 = new ArrayList(); 
        arrayList1.add(catchClause);
        arrayList = arrayList1;
      } 
    } else {
      n = -1;
      if (i2 != 126)
        mustMatchToken(126, "msg.try.no.catchfinally", true); 
    } 
    comment3 = null;
    if (matchToken(126, true)) {
      n = this.ts.tokenBeg;
      astNode2 = statement();
      i1 = getNodeEnd(astNode2);
    } 
    tryStatement.setLength(i1 - i);
    tryStatement.setTryBlock(astNode1);
    tryStatement.setCatchClauses(arrayList);
    tryStatement.setFinallyBlock(astNode2);
    if (n != -1)
      tryStatement.setFinallyPosition(n - i); 
    tryStatement.setLineno(j);
    if (comment1 != null)
      tryStatement.setJsDocNode(comment1); 
    return tryStatement;
  }
  
  private AstNode unaryExpr() throws IOException {
    int i = peekToken();
    int j = i;
    if (i == 162) {
      consumeToken();
      j = peekUntilNonComment(i);
    } 
    i = this.ts.lineno;
    if (j != -1) {
      if (j != 14) {
        if (j != 127) {
          if (j != 21) {
            if (j != 22) {
              if (j != 26 && j != 27) {
                if (j != 31) {
                  if (j != 32) {
                    if (j == 107 || j == 108) {
                      consumeToken();
                      UnaryExpression unaryExpression1 = new UnaryExpression(j, this.ts.tokenBeg, memberExpr(true));
                      unaryExpression1.setLineno(i);
                      checkBadIncDec(unaryExpression1);
                      return (AstNode)unaryExpression1;
                    } 
                  } else {
                    consumeToken();
                    UnaryExpression unaryExpression1 = new UnaryExpression(j, this.ts.tokenBeg, unaryExpr());
                    unaryExpression1.setLineno(i);
                    return (AstNode)unaryExpression1;
                  } 
                } else {
                  consumeToken();
                  UnaryExpression unaryExpression1 = new UnaryExpression(j, this.ts.tokenBeg, unaryExpr());
                  unaryExpression1.setLineno(i);
                  return (AstNode)unaryExpression1;
                } 
              } else {
                consumeToken();
                UnaryExpression unaryExpression1 = new UnaryExpression(j, this.ts.tokenBeg, unaryExpr());
                unaryExpression1.setLineno(i);
                return (AstNode)unaryExpression1;
              } 
            } else {
              consumeToken();
              UnaryExpression unaryExpression1 = new UnaryExpression(29, this.ts.tokenBeg, unaryExpr());
              unaryExpression1.setLineno(i);
              return (AstNode)unaryExpression1;
            } 
          } else {
            consumeToken();
            UnaryExpression unaryExpression1 = new UnaryExpression(28, this.ts.tokenBeg, unaryExpr());
            unaryExpression1.setLineno(i);
            return (AstNode)unaryExpression1;
          } 
        } else {
          consumeToken();
          UnaryExpression unaryExpression1 = new UnaryExpression(j, this.ts.tokenBeg, unaryExpr());
          unaryExpression1.setLineno(i);
          return (AstNode)unaryExpression1;
        } 
      } else if (this.compilerEnv.isXmlAvailable()) {
        consumeToken();
        return memberExprTail(true, xmlInitializer());
      } 
      AstNode astNode = memberExpr(true);
      j = peekTokenOrEOL();
      if (j != 107 && j != 108)
        return astNode; 
      consumeToken();
      UnaryExpression unaryExpression = new UnaryExpression(j, this.ts.tokenBeg, astNode, true);
      unaryExpression.setLineno(i);
      checkBadIncDec(unaryExpression);
      return (AstNode)unaryExpression;
    } 
    consumeToken();
    return (AstNode)makeErrorNode();
  }
  
  private VariableDeclaration variables(int paramInt1, int paramInt2, boolean paramBoolean) throws IOException {
    VariableDeclaration variableDeclaration = new VariableDeclaration(paramInt2);
    variableDeclaration.setType(paramInt1);
    variableDeclaration.setLineno(this.ts.lineno);
    Comment comment = getAndResetJsDoc();
    if (comment != null)
      variableDeclaration.setJsDocNode(comment); 
    while (true) {
      AstNode astNode1;
      comment = null;
      Name name = null;
      int i = peekToken();
      int j = this.ts.tokenBeg;
      int k = this.ts.tokenEnd;
      if (i == 84 || i == 86) {
        astNode1 = destructuringPrimaryExpr();
        k = getNodeEnd(astNode1);
        if (!(astNode1 instanceof DestructuringForm))
          reportError("msg.bad.assign.left", j, k - j); 
        markDestructuring(astNode1);
      } else {
        mustMatchToken(39, "msg.bad.var", true);
        name = createNameNode();
        name.setLineno(this.ts.getLineno());
        if (this.inUseStrictDirective) {
          String str = this.ts.getString();
          if ("eval".equals(str) || "arguments".equals(this.ts.getString()))
            reportError("msg.bad.id.strict", str); 
        } 
        defineSymbol(paramInt1, this.ts.getString(), this.inForInit);
      } 
      i = this.ts.lineno;
      Comment comment1 = getAndResetJsDoc();
      AstNode astNode2 = null;
      if (matchToken(91, true)) {
        astNode2 = assignExpr();
        k = getNodeEnd(astNode2);
      } 
      VariableInitializer variableInitializer = new VariableInitializer(j, k - j);
      if (astNode1 != null) {
        if (astNode2 == null && !this.inForInit)
          reportError("msg.destruct.assign.no.init"); 
        variableInitializer.setTarget(astNode1);
      } else {
        variableInitializer.setTarget((AstNode)name);
      } 
      variableInitializer.setInitializer(astNode2);
      variableInitializer.setType(paramInt1);
      variableInitializer.setJsDocNode(comment1);
      variableInitializer.setLineno(i);
      variableDeclaration.addVariable(variableInitializer);
      if (!matchToken(90, true)) {
        variableDeclaration.setLength(k - paramInt2);
        variableDeclaration.setIsStatement(paramBoolean);
        return variableDeclaration;
      } 
    } 
  }
  
  private void warnMissingSemi(int paramInt1, int paramInt2) {
    if (this.compilerEnv.isStrictMode()) {
      int[] arrayOfInt = new int[2];
      String str = this.ts.getLine(paramInt2, arrayOfInt);
      if (this.compilerEnv.isIdeMode())
        paramInt1 = Math.max(paramInt1, paramInt2 - arrayOfInt[1]); 
      if (str != null) {
        addStrictWarning("msg.missing.semi", "", paramInt1, paramInt2 - paramInt1, arrayOfInt[0], str, arrayOfInt[1]);
      } else {
        addStrictWarning("msg.missing.semi", "", paramInt1, paramInt2 - paramInt1);
      } 
    } 
  }
  
  private void warnTrailingComma(int paramInt1, List<?> paramList, int paramInt2) {
    if (this.compilerEnv.getWarnTrailingComma()) {
      if (!paramList.isEmpty())
        paramInt1 = ((AstNode)paramList.get(0)).getPosition(); 
      paramInt1 = Math.max(paramInt1, lineBeginningFor(paramInt2));
      addWarning("msg.extra.trailing.comma", paramInt1, paramInt2 - paramInt1);
    } 
  }
  
  private WhileLoop whileLoop() throws IOException {
    if (this.currentToken != 118)
      codeBug(); 
    consumeToken();
    int i = this.ts.tokenBeg;
    null = new WhileLoop(i);
    null.setLineno(this.ts.lineno);
    enterLoop((Loop)null);
    try {
      ConditionData conditionData = condition();
      null.setCondition(conditionData.condition);
      null.setParens(conditionData.lp - i, conditionData.rp - i);
      AstNode astNode = getNextStatementAfterInlineComments((AstNode)null);
      null.setLength(getNodeEnd(astNode) - i);
      null.setBody(astNode);
      return null;
    } finally {
      exitLoop();
    } 
  }
  
  private WithStatement withStatement() throws IOException {
    if (this.currentToken != 124)
      codeBug(); 
    consumeToken();
    Comment comment = getAndResetJsDoc();
    int i = this.ts.lineno;
    int j = this.ts.tokenBeg;
    int k = -1;
    int m = -1;
    if (mustMatchToken(88, "msg.no.paren.with", true))
      k = this.ts.tokenBeg; 
    AstNode astNode1 = expr();
    if (mustMatchToken(89, "msg.no.paren.after.with", true))
      m = this.ts.tokenBeg; 
    WithStatement withStatement = new WithStatement(j);
    AstNode astNode2 = getNextStatementAfterInlineComments((AstNode)withStatement);
    withStatement.setLength(getNodeEnd(astNode2) - j);
    withStatement.setJsDocNode(comment);
    withStatement.setExpression(astNode1);
    withStatement.setStatement(astNode2);
    withStatement.setParens(k, m);
    withStatement.setLineno(i);
    return withStatement;
  }
  
  private XmlElemRef xmlElemRef(int paramInt1, Name paramName, int paramInt2) throws IOException {
    int k;
    int i = this.ts.tokenBeg;
    int j = -1;
    if (paramInt1 != -1) {
      k = paramInt1;
    } else {
      k = i;
    } 
    AstNode astNode = expr();
    int m = getNodeEnd(astNode);
    if (mustMatchToken(85, "msg.no.bracket.index", true)) {
      j = this.ts.tokenBeg;
      m = this.ts.tokenEnd;
    } 
    XmlElemRef xmlElemRef = new XmlElemRef(k, m - k);
    xmlElemRef.setNamespace(paramName);
    xmlElemRef.setColonPos(paramInt2);
    xmlElemRef.setAtPos(paramInt1);
    xmlElemRef.setExpression(astNode);
    xmlElemRef.setBrackets(i, j);
    return xmlElemRef;
  }
  
  private AstNode xmlInitializer() throws IOException {
    if (this.currentToken != 14)
      codeBug(); 
    int i = this.ts.tokenBeg;
    int j = this.ts.getFirstXMLToken();
    if (j != 146 && j != 149) {
      reportError("msg.syntax");
      return (AstNode)makeErrorNode();
    } 
    XmlLiteral xmlLiteral = new XmlLiteral(i);
    xmlLiteral.setLineno(this.ts.lineno);
    while (true) {
      AstNode astNode;
      if (j != 146) {
        if (j != 149) {
          reportError("msg.syntax");
          return (AstNode)makeErrorNode();
        } 
        xmlLiteral.addFragment((XmlFragment)new XmlString(this.ts.tokenBeg, this.ts.getString()));
        return (AstNode)xmlLiteral;
      } 
      xmlLiteral.addFragment((XmlFragment)new XmlString(this.ts.tokenBeg, this.ts.getString()));
      mustMatchToken(86, "msg.syntax", true);
      j = this.ts.tokenBeg;
      if (peekToken() == 87) {
        EmptyExpression emptyExpression = new EmptyExpression(j, this.ts.tokenEnd - j);
      } else {
        astNode = expr();
      } 
      mustMatchToken(87, "msg.syntax", true);
      XmlExpression xmlExpression = new XmlExpression(j, astNode);
      xmlExpression.setIsXmlAttribute(this.ts.isXMLAttribute());
      xmlExpression.setLength(this.ts.tokenEnd - j);
      xmlLiteral.addFragment((XmlFragment)xmlExpression);
      j = this.ts.getNextXMLToken();
    } 
  }
  
  void addError(String paramString) {
    addError(paramString, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
  }
  
  void addError(String paramString, int paramInt) {
    addError(paramString, Character.toString((char)paramInt), this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
  }
  
  void addError(String paramString, int paramInt1, int paramInt2) {
    addError(paramString, null, paramInt1, paramInt2);
  }
  
  void addError(String paramString1, String paramString2) {
    addError(paramString1, paramString2, this.ts.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
  }
  
  void addError(String paramString1, String paramString2, int paramInt1, int paramInt2) {
    this.syntaxErrorCount++;
    paramString2 = lookupMessage(paramString1, paramString2);
    IdeErrorReporter ideErrorReporter = this.errorCollector;
    if (ideErrorReporter != null) {
      ideErrorReporter.error(paramString2, this.sourceURI, paramInt1, paramInt2);
    } else {
      String str;
      TokenStream tokenStream = this.ts;
      if (tokenStream != null) {
        paramInt2 = tokenStream.getLineno();
        str = this.ts.getLine();
        paramInt1 = this.ts.getOffset();
      } else {
        paramInt2 = 1;
        paramInt1 = 1;
        str = "";
      } 
      this.errorReporter.error(paramString2, this.sourceURI, paramInt2, str, paramInt1);
    } 
  }
  
  void addStrictWarning(String paramString1, String paramString2) {
    int i = -1;
    int j = -1;
    TokenStream tokenStream = this.ts;
    if (tokenStream != null) {
      i = tokenStream.tokenBeg;
      j = this.ts.tokenEnd - this.ts.tokenBeg;
    } 
    addStrictWarning(paramString1, paramString2, i, j);
  }
  
  void addStrictWarning(String paramString1, String paramString2, int paramInt1, int paramInt2) {
    if (this.compilerEnv.isStrictMode())
      addWarning(paramString1, paramString2, paramInt1, paramInt2); 
  }
  
  void addWarning(String paramString, int paramInt1, int paramInt2) {
    addWarning(paramString, null, paramInt1, paramInt2);
  }
  
  void addWarning(String paramString1, String paramString2) {
    int i = -1;
    int j = -1;
    TokenStream tokenStream = this.ts;
    if (tokenStream != null) {
      i = tokenStream.tokenBeg;
      j = this.ts.tokenEnd - this.ts.tokenBeg;
    } 
    addWarning(paramString1, paramString2, i, j);
  }
  
  void addWarning(String paramString1, String paramString2, int paramInt1, int paramInt2) {
    String str = lookupMessage(paramString1, paramString2);
    if (this.compilerEnv.reportWarningAsError()) {
      addError(paramString1, paramString2, paramInt1, paramInt2);
    } else {
      IdeErrorReporter ideErrorReporter = this.errorCollector;
      if (ideErrorReporter != null) {
        ideErrorReporter.warning(str, this.sourceURI, paramInt1, paramInt2);
      } else {
        this.errorReporter.warning(str, this.sourceURI, this.ts.getLineno(), this.ts.getLine(), this.ts.getOffset());
      } 
    } 
  }
  
  protected void checkActivationName(String paramString, int paramInt) {
    boolean bool2;
    if (!insideFunction())
      return; 
    boolean bool1 = false;
    if ("arguments".equals(paramString) && ((FunctionNode)this.currentScriptOrFn).getFunctionType() != 4) {
      bool2 = true;
    } else if (this.compilerEnv.getActivationNames() != null && this.compilerEnv.getActivationNames().contains(paramString)) {
      bool2 = true;
    } else {
      bool2 = bool1;
      if ("length".equals(paramString)) {
        bool2 = bool1;
        if (paramInt == 33) {
          bool2 = bool1;
          if (this.compilerEnv.getLanguageVersion() == 120)
            bool2 = true; 
        } 
      } 
    } 
    if (bool2)
      setRequiresActivation(); 
  }
  
  protected void checkMutableReference(Node paramNode) {
    if ((paramNode.getIntProp(16, 0) & 0x4) != 0)
      reportError("msg.bad.assign.left"); 
  }
  
  Node createDestructuringAssignment(int paramInt, Node paramNode1, Node paramNode2) {
    String str = this.currentScriptOrFn.getNextTempName();
    paramNode1 = destructuringAssignmentHelper(paramInt, paramNode1, paramNode2, str);
    paramNode1.getLastChild().addChildToBack(createName(str));
    return paramNode1;
  }
  
  protected Node createName(int paramInt, String paramString, Node paramNode) {
    Node node = createName(paramString);
    node.setType(paramInt);
    if (paramNode != null)
      node.addChildToBack(paramNode); 
    return node;
  }
  
  protected Node createName(String paramString) {
    checkActivationName(paramString, 39);
    return Node.newString(39, paramString);
  }
  
  protected Node createNumber(double paramDouble) {
    return Node.newNumber(paramDouble);
  }
  
  protected Scope createScopeNode(int paramInt1, int paramInt2) {
    Scope scope = new Scope();
    scope.setType(paramInt1);
    scope.setLineno(paramInt2);
    return scope;
  }
  
  void defineSymbol(int paramInt, String paramString) {
    defineSymbol(paramInt, paramString, false);
  }
  
  void defineSymbol(int paramInt, String paramString, boolean paramBoolean) {
    Symbol symbol;
    String str1;
    byte b;
    if (paramString == null) {
      if (this.compilerEnv.isIdeMode())
        return; 
      codeBug();
    } 
    Scope scope = this.currentScope.getDefiningScope(paramString);
    if (scope != null) {
      symbol = scope.getSymbol(paramString);
    } else {
      symbol = null;
    } 
    if (symbol != null) {
      b = symbol.getDeclType();
    } else {
      b = -1;
    } 
    String str2 = "msg.var.redecl";
    if (symbol != null && (b == 155 || paramInt == 155 || (scope == this.currentScope && b == 154))) {
      if (b == 155) {
        str1 = "msg.const.redecl";
      } else if (b == 154) {
        str1 = "msg.let.redecl";
      } else if (b == 123) {
        str1 = str2;
      } else if (b == 110) {
        str1 = "msg.fn.redecl";
      } else {
        str1 = "msg.parm.redecl";
      } 
      addError(str1, paramString);
      return;
    } 
    if (paramInt != 88) {
      if (paramInt != 110 && paramInt != 123)
        if (paramInt != 154) {
          if (paramInt != 155)
            throw codeBug(); 
        } else {
          if (!paramBoolean && (this.currentScope.getType() == 113 || this.currentScope instanceof Loop)) {
            addError("msg.let.decl.not.in.block");
            return;
          } 
          this.currentScope.putSymbol(new Symbol(paramInt, paramString));
          return;
        }  
      if (str1 != null) {
        if (b == 123) {
          addStrictWarning("msg.var.redecl", paramString);
        } else if (b == 88) {
          addStrictWarning("msg.var.hides.arg", paramString);
        } 
      } else {
        this.currentScriptOrFn.putSymbol(new Symbol(paramInt, paramString));
      } 
      return;
    } 
    if (str1 != null)
      addWarning("msg.dup.parms", paramString); 
    this.currentScriptOrFn.putSymbol(new Symbol(paramInt, paramString));
  }
  
  boolean destructuringArray(ArrayLiteral paramArrayLiteral, int paramInt, String paramString, Node paramNode, List<String> paramList) {
    byte b1;
    boolean bool = true;
    if (paramInt == 155) {
      b1 = 156;
    } else {
      b1 = 8;
    } 
    byte b2 = 0;
    for (AstNode astNode : paramArrayLiteral.getElements()) {
      String str;
      if (astNode.getType() == 129) {
        b2++;
        continue;
      } 
      Node node = new Node(36, createName(paramString), createNumber(b2));
      if (astNode.getType() == 39) {
        str = astNode.getString();
        paramNode.addChildToBack(new Node(b1, createName(49, str, null), node));
        if (paramInt != -1) {
          defineSymbol(paramInt, str, true);
          paramList.add(str);
        } 
      } else {
        paramNode.addChildToBack(destructuringAssignmentHelper(paramInt, (Node)str, node, this.currentScriptOrFn.getNextTempName()));
      } 
      b2++;
      bool = false;
    } 
    return bool;
  }
  
  Node destructuringAssignmentHelper(int paramInt, Node paramNode1, Node paramNode2, String paramString) {
    Scope scope = createScopeNode(159, paramNode1.getLineno());
    scope.addChildToFront(new Node(154, createName(39, paramString, paramNode2)));
    try {
      pushScope(scope);
      defineSymbol(154, paramString, true);
      popScope();
      paramNode2 = new Node(90);
      scope.addChildToBack(paramNode2);
      ArrayList<String> arrayList = new ArrayList();
      boolean bool = true;
      int i = paramNode1.getType();
      if (i != 33 && i != 36) {
        if (i != 66) {
          if (i != 67) {
            reportError("msg.bad.assign.left");
          } else {
            bool = destructuringObject((ObjectLiteral)paramNode1, paramInt, paramString, paramNode2, arrayList);
          } 
        } else {
          bool = destructuringArray((ArrayLiteral)paramNode1, paramInt, paramString, paramNode2, arrayList);
        } 
      } else {
        if (paramInt == 123 || paramInt == 154 || paramInt == 155)
          reportError("msg.bad.assign.left"); 
        paramNode2.addChildToBack(simpleAssignment(paramNode1, createName(paramString)));
      } 
      if (bool)
        paramNode2.addChildToBack(createNumber(0.0D)); 
      return (Node)scope;
    } finally {
      popScope();
    } 
  }
  
  boolean destructuringObject(ObjectLiteral paramObjectLiteral, int paramInt, String paramString, Node paramNode, List<String> paramList) {
    byte b;
    boolean bool = true;
    if (paramInt == 155) {
      b = 156;
    } else {
      b = 8;
    } 
    for (ObjectProperty objectProperty : paramObjectLiteral.getElements()) {
      Node node;
      String str;
      int i = 0;
      TokenStream tokenStream = this.ts;
      if (tokenStream != null)
        i = tokenStream.lineno; 
      AstNode astNode1 = objectProperty.getLeft();
      if (astNode1 instanceof Name) {
        node = Node.newString(((Name)astNode1).getIdentifier());
        node = new Node(33, createName(paramString), node);
      } else if (node instanceof StringLiteral) {
        node = Node.newString(((StringLiteral)node).getValue());
        node = new Node(33, createName(paramString), node);
      } else if (node instanceof NumberLiteral) {
        node = createNumber((int)((NumberLiteral)node).getNumber());
        node = new Node(36, createName(paramString), node);
      } else {
        throw codeBug();
      } 
      node.setLineno(i);
      AstNode astNode2 = objectProperty.getRight();
      if (astNode2.getType() == 39) {
        str = ((Name)astNode2).getIdentifier();
        paramNode.addChildToBack(new Node(b, createName(49, str, null), node));
        if (paramInt != -1) {
          defineSymbol(paramInt, str, true);
          paramList.add(str);
        } 
      } else {
        paramNode.addChildToBack(destructuringAssignmentHelper(paramInt, (Node)str, node, this.currentScriptOrFn.getNextTempName()));
      } 
      bool = false;
    } 
    return bool;
  }
  
  public boolean eof() {
    return this.ts.eof();
  }
  
  public boolean inUseStrictDirective() {
    return this.inUseStrictDirective;
  }
  
  boolean insideFunction() {
    boolean bool;
    if (this.nestingOfFunction != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  String lookupMessage(String paramString) {
    return lookupMessage(paramString, null);
  }
  
  String lookupMessage(String paramString1, String paramString2) {
    if (paramString2 == null) {
      paramString1 = ScriptRuntime.getMessage0(paramString1);
    } else {
      paramString1 = ScriptRuntime.getMessage1(paramString1, paramString2);
    } 
    return paramString1;
  }
  
  void markDestructuring(AstNode paramAstNode) {
    if (paramAstNode instanceof DestructuringForm) {
      ((DestructuringForm)paramAstNode).setIsDestructuring(true);
    } else if (paramAstNode instanceof ParenthesizedExpression) {
      markDestructuring(((ParenthesizedExpression)paramAstNode).getExpression());
    } 
  }
  
  public AstRoot parse(Reader paramReader, String paramString, int paramInt) throws IOException {
    if (!this.parseFinished) {
      if (this.compilerEnv.isIdeMode())
        return parse(readFully(paramReader), paramString, paramInt); 
      try {
        this.sourceURI = paramString;
        TokenStream tokenStream = new TokenStream();
        this(this, paramReader, null, paramInt);
        this.ts = tokenStream;
        return parse();
      } finally {
        this.parseFinished = true;
      } 
    } 
    throw new IllegalStateException("parser reused");
  }
  
  public AstRoot parse(String paramString1, String paramString2, int paramInt) {
    if (!this.parseFinished) {
      this.sourceURI = paramString2;
      if (this.compilerEnv.isIdeMode())
        this.sourceChars = paramString1.toCharArray(); 
      this.ts = new TokenStream(this, null, paramString1, paramInt);
      try {
        AstRoot astRoot = parse();
        this.parseFinished = true;
        return astRoot;
      } catch (IOException iOException) {
        IllegalStateException illegalStateException = new IllegalStateException();
        this();
        throw illegalStateException;
      } finally {}
      this.parseFinished = true;
      throw paramString1;
    } 
    throw new IllegalStateException("parser reused");
  }
  
  void popScope() {
    this.currentScope = this.currentScope.getParentScope();
  }
  
  void pushScope(Scope paramScope) {
    Scope scope = paramScope.getParentScope();
    if (scope != null) {
      if (scope != this.currentScope)
        codeBug(); 
    } else {
      this.currentScope.addChildScope(paramScope);
    } 
    this.currentScope = paramScope;
  }
  
  protected AstNode removeParens(AstNode paramAstNode) {
    while (paramAstNode instanceof ParenthesizedExpression)
      paramAstNode = ((ParenthesizedExpression)paramAstNode).getExpression(); 
    return paramAstNode;
  }
  
  void reportError(String paramString) {
    reportError(paramString, null);
  }
  
  void reportError(String paramString, int paramInt1, int paramInt2) {
    reportError(paramString, null, paramInt1, paramInt2);
  }
  
  void reportError(String paramString1, String paramString2) {
    TokenStream tokenStream = this.ts;
    if (tokenStream == null) {
      reportError(paramString1, paramString2, 1, 1);
    } else {
      reportError(paramString1, paramString2, tokenStream.tokenBeg, this.ts.tokenEnd - this.ts.tokenBeg);
    } 
  }
  
  void reportError(String paramString1, String paramString2, int paramInt1, int paramInt2) {
    addError(paramString1, paramString2, paramInt1, paramInt2);
    if (this.compilerEnv.recoverFromErrors())
      return; 
    throw new ParserException();
  }
  
  public void setDefaultUseStrictDirective(boolean paramBoolean) {
    this.defaultUseStrictDirective = paramBoolean;
  }
  
  protected void setIsGenerator() {
    if (insideFunction())
      ((FunctionNode)this.currentScriptOrFn).setIsGenerator(); 
  }
  
  protected void setRequiresActivation() {
    if (insideFunction())
      ((FunctionNode)this.currentScriptOrFn).setRequiresActivation(); 
  }
  
  protected Node simpleAssignment(Node paramNode1, Node paramNode2) {
    AstNode astNode;
    Node node1;
    Node node2;
    int i = paramNode1.getType();
    if (i != 33 && i != 36) {
      if (i != 39) {
        if (i == 68) {
          paramNode1 = paramNode1.getFirstChild();
          checkMutableReference(paramNode1);
          return new Node(69, paramNode1, paramNode2);
        } 
        throw codeBug();
      } 
      String str = ((Name)paramNode1).getIdentifier();
      if (this.inUseStrictDirective && ("eval".equals(str) || "arguments".equals(str)))
        reportError("msg.bad.id.strict", str); 
      paramNode1.setType(49);
      return new Node(8, paramNode1, paramNode2);
    } 
    if (paramNode1 instanceof PropertyGet) {
      AstNode astNode1 = ((PropertyGet)paramNode1).getTarget();
      Name name2 = ((PropertyGet)paramNode1).getProperty();
      astNode = astNode1;
      Name name1 = name2;
    } else if (astNode instanceof ElementGet) {
      AstNode astNode1 = ((ElementGet)astNode).getTarget();
      AstNode astNode2 = ((ElementGet)astNode).getElement();
      astNode = astNode1;
      astNode1 = astNode2;
    } else {
      Node node = astNode.getFirstChild();
      node2 = astNode.getLastChild();
      node1 = node;
    } 
    if (i == 33) {
      i = 35;
      node2.setType(41);
    } else {
      i = 37;
    } 
    return new Node(i, node1, node2, paramNode2);
  }
  
  private static class ConditionData {
    AstNode condition;
    
    int lp = -1;
    
    int rp = -1;
    
    private ConditionData() {}
  }
  
  private static class ParserException extends RuntimeException {
    private static final long serialVersionUID = 5882582646773765630L;
    
    private ParserException() {}
  }
  
  protected class PerFunctionVariables {
    private Scope savedCurrentScope;
    
    private ScriptNode savedCurrentScriptOrFn = Parser.this.currentScriptOrFn;
    
    private int savedEndFlags;
    
    private boolean savedInForInit;
    
    private Map<String, LabeledStatement> savedLabelSet;
    
    private List<Jump> savedLoopAndSwitchSet;
    
    private List<Loop> savedLoopSet;
    
    PerFunctionVariables(FunctionNode param1FunctionNode) {
      Parser.this.currentScriptOrFn = (ScriptNode)param1FunctionNode;
      this.savedCurrentScope = Parser.this.currentScope;
      Parser.this.currentScope = (Scope)param1FunctionNode;
      this.savedLabelSet = Parser.this.labelSet;
      Parser.access$202(Parser.this, null);
      this.savedLoopSet = Parser.this.loopSet;
      Parser.access$302(Parser.this, null);
      this.savedLoopAndSwitchSet = Parser.this.loopAndSwitchSet;
      Parser.access$402(Parser.this, null);
      this.savedEndFlags = Parser.this.endFlags;
      Parser.access$502(Parser.this, 0);
      this.savedInForInit = Parser.this.inForInit;
      Parser.access$602(Parser.this, false);
    }
    
    void restore() {
      Parser.this.currentScriptOrFn = this.savedCurrentScriptOrFn;
      Parser.this.currentScope = this.savedCurrentScope;
      Parser.access$202(Parser.this, this.savedLabelSet);
      Parser.access$302(Parser.this, this.savedLoopSet);
      Parser.access$402(Parser.this, this.savedLoopAndSwitchSet);
      Parser.access$502(Parser.this, this.savedEndFlags);
      Parser.access$602(Parser.this, this.savedInForInit);
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/Parser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */