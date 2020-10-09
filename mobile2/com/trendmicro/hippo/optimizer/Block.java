package com.trendmicro.hippo.optimizer;

import com.trendmicro.hippo.Node;
import com.trendmicro.hippo.ObjArray;
import com.trendmicro.hippo.ObjToIntMap;
import com.trendmicro.hippo.ast.Jump;
import java.util.BitSet;
import java.util.HashMap;

class Block {
  static final boolean DEBUG = false;
  
  private static int debug_blockCount;
  
  private int itsBlockID;
  
  private int itsEndNodeIndex;
  
  private BitSet itsLiveOnEntrySet;
  
  private BitSet itsLiveOnExitSet;
  
  private BitSet itsNotDefSet;
  
  private Block[] itsPredecessors;
  
  private int itsStartNodeIndex;
  
  private Block[] itsSuccessors;
  
  private BitSet itsUseBeforeDefSet;
  
  Block(int paramInt1, int paramInt2) {
    this.itsStartNodeIndex = paramInt1;
    this.itsEndNodeIndex = paramInt2;
  }
  
  private static boolean assignType(int[] paramArrayOfint, int paramInt1, int paramInt2) {
    boolean bool;
    int i = paramArrayOfint[paramInt1];
    paramInt2 = paramArrayOfint[paramInt1] | paramInt2;
    paramArrayOfint[paramInt1] = paramInt2;
    if (i != paramInt2) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static Block[] buildBlocks(Node[] paramArrayOfNode) {
    HashMap<Object, Object> hashMap = new HashMap<>();
    ObjArray objArray = new ObjArray();
    int i = 0;
    int j = 0;
    while (j < paramArrayOfNode.length) {
      int k = paramArrayOfNode[j].getType();
      if (k != 5 && k != 6 && k != 7) {
        if (k != 132) {
          k = i;
        } else {
          k = i;
          if (j != i) {
            FatBlock fatBlock = newFatBlock(i, j - 1);
            if (paramArrayOfNode[i].getType() == 132)
              hashMap.put(paramArrayOfNode[i], fatBlock); 
            objArray.add(fatBlock);
            k = j;
          } 
        } 
      } else {
        FatBlock fatBlock = newFatBlock(i, j);
        if (paramArrayOfNode[i].getType() == 132)
          hashMap.put(paramArrayOfNode[i], fatBlock); 
        objArray.add(fatBlock);
        k = j + 1;
      } 
      j++;
      i = k;
    } 
    if (i != paramArrayOfNode.length) {
      FatBlock fatBlock = newFatBlock(i, paramArrayOfNode.length - 1);
      if (paramArrayOfNode[i].getType() == 132)
        hashMap.put(paramArrayOfNode[i], fatBlock); 
      objArray.add(fatBlock);
    } 
    for (j = 0; j < objArray.size(); j++) {
      FatBlock fatBlock = (FatBlock)objArray.get(j);
      Node node = paramArrayOfNode[fatBlock.realBlock.itsEndNodeIndex];
      int k = node.getType();
      if (k != 5 && j < objArray.size() - 1) {
        FatBlock fatBlock1 = (FatBlock)objArray.get(j + 1);
        fatBlock.addSuccessor(fatBlock1);
        fatBlock1.addPredecessor(fatBlock);
      } 
      if (k == 7 || k == 6 || k == 5) {
        node = ((Jump)node).target;
        FatBlock fatBlock1 = (FatBlock)hashMap.get(node);
        node.putProp(6, fatBlock1.realBlock);
        fatBlock.addSuccessor(fatBlock1);
        fatBlock1.addPredecessor(fatBlock);
      } 
    } 
    Block[] arrayOfBlock = new Block[objArray.size()];
    for (j = 0; j < objArray.size(); j++) {
      FatBlock fatBlock = (FatBlock)objArray.get(j);
      Block block = fatBlock.realBlock;
      block.itsSuccessors = fatBlock.getSuccessors();
      block.itsPredecessors = fatBlock.getPredecessors();
      block.itsBlockID = j;
      arrayOfBlock[j] = block;
    } 
    return arrayOfBlock;
  }
  
  private boolean doReachedUseDataFlow() {
    this.itsLiveOnExitSet.clear();
    if (this.itsSuccessors != null) {
      byte b = 0;
      while (true) {
        Block[] arrayOfBlock = this.itsSuccessors;
        if (b < arrayOfBlock.length) {
          this.itsLiveOnExitSet.or((arrayOfBlock[b]).itsLiveOnEntrySet);
          b++;
          continue;
        } 
        break;
      } 
    } 
    return updateEntrySet(this.itsLiveOnEntrySet, this.itsLiveOnExitSet, this.itsUseBeforeDefSet, this.itsNotDefSet);
  }
  
  private boolean doTypeFlow(OptFunctionNode paramOptFunctionNode, Node[] paramArrayOfNode, int[] paramArrayOfint) {
    boolean bool = false;
    int i = this.itsStartNodeIndex;
    while (i <= this.itsEndNodeIndex) {
      Node node = paramArrayOfNode[i];
      boolean bool1 = bool;
      if (node != null)
        bool1 = bool | findDefPoints(paramOptFunctionNode, node, paramArrayOfint); 
      i++;
      bool = bool1;
    } 
    return bool;
  }
  
  private static boolean findDefPoints(OptFunctionNode paramOptFunctionNode, Node paramNode, int[] paramArrayOfint) {
    boolean bool2;
    boolean bool1 = false;
    Node node1 = paramNode.getFirstChild();
    for (Node node2 = node1; node2 != null; node2 = node2.getNext())
      bool1 |= findDefPoints(paramOptFunctionNode, node2, paramArrayOfint); 
    int i = paramNode.getType();
    if (i != 56 && i != 157) {
      if (i != 107 && i != 108) {
        bool2 = bool1;
      } else {
        bool2 = bool1;
        if (node1.getType() == 55) {
          i = paramOptFunctionNode.getVarIndex(node1);
          bool2 = bool1;
          if (!paramOptFunctionNode.fnode.getParamAndVarConst()[i])
            bool2 = bool1 | assignType(paramArrayOfint, i, 1); 
        } 
      } 
    } else {
      i = findExpressionType(paramOptFunctionNode, node1.getNext(), paramArrayOfint);
      int j = paramOptFunctionNode.getVarIndex(paramNode);
      if (paramNode.getType() == 56) {
        boolean bool = bool1;
        if (!paramOptFunctionNode.fnode.getParamAndVarConst()[j])
          bool = bool1 | assignType(paramArrayOfint, j, i); 
        return bool;
      } 
      bool2 = bool1 | assignType(paramArrayOfint, j, i);
    } 
    return bool2;
  }
  
  private static int findExpressionType(OptFunctionNode paramOptFunctionNode, Node paramNode, int[] paramArrayOfint) {
    int i = paramNode.getType();
    if (i != 35 && i != 37)
      if (i != 40) {
        if (i != 90)
          if (i != 103) {
            if (i != 157)
              if (i != 55) {
                if (i != 56)
                  switch (i) {
                    default:
                      switch (i) {
                        default:
                          switch (i) {
                            default:
                              switch (i) {
                                default:
                                  return 3;
                                case 105:
                                case 106:
                                  paramNode = paramNode.getFirstChild();
                                  return findExpressionType(paramOptFunctionNode, paramNode, paramArrayOfint) | findExpressionType(paramOptFunctionNode, paramNode.getNext(), paramArrayOfint);
                                case 107:
                                case 108:
                                  break;
                              } 
                              break;
                            case 27:
                            case 28:
                            case 29:
                              break;
                          } 
                          break;
                        case 21:
                          paramNode = paramNode.getFirstChild();
                          return findExpressionType(paramOptFunctionNode, paramNode, paramArrayOfint) | findExpressionType(paramOptFunctionNode, paramNode.getNext(), paramArrayOfint);
                        case 18:
                        case 19:
                        case 20:
                        case 22:
                        case 23:
                        case 24:
                        case 25:
                          break;
                      } 
                    case 9:
                    case 10:
                    case 11:
                      return 1;
                    case 8:
                      break;
                  }  
              } else {
                return paramArrayOfint[paramOptFunctionNode.getVarIndex(paramNode)];
              }  
          } else {
            paramNode = paramNode.getFirstChild().getNext();
            Node node = paramNode.getNext();
            return findExpressionType(paramOptFunctionNode, paramNode, paramArrayOfint) | findExpressionType(paramOptFunctionNode, node, paramArrayOfint);
          }  
      } else {
        return 1;
      }  
    return findExpressionType(paramOptFunctionNode, paramNode.getLastChild(), paramArrayOfint);
  }
  
  private void initLiveOnEntrySets(OptFunctionNode paramOptFunctionNode, Node[] paramArrayOfNode) {
    int i = paramOptFunctionNode.getVarCount();
    this.itsUseBeforeDefSet = new BitSet(i);
    this.itsNotDefSet = new BitSet(i);
    this.itsLiveOnEntrySet = new BitSet(i);
    this.itsLiveOnExitSet = new BitSet(i);
    for (int j = this.itsStartNodeIndex; j <= this.itsEndNodeIndex; j++)
      lookForVariableAccess(paramOptFunctionNode, paramArrayOfNode[j]); 
    this.itsNotDefSet.flip(0, i);
  }
  
  private void lookForVariableAccess(OptFunctionNode paramOptFunctionNode, Node paramNode) {
    int i = paramNode.getType();
    if (i != 55) {
      if (i != 56)
        if (i != 107 && i != 108) {
          if (i != 138) {
            if (i != 157) {
              for (paramNode = paramNode.getFirstChild(); paramNode != null; paramNode = paramNode.getNext())
                lookForVariableAccess(paramOptFunctionNode, paramNode); 
              return;
            } 
          } else {
            i = paramOptFunctionNode.fnode.getIndexForNameNode(paramNode);
            if (i > -1 && !this.itsNotDefSet.get(i))
              this.itsUseBeforeDefSet.set(i); 
            return;
          } 
        } else {
          paramNode = paramNode.getFirstChild();
          if (paramNode.getType() == 55) {
            i = paramOptFunctionNode.getVarIndex(paramNode);
            if (!this.itsNotDefSet.get(i))
              this.itsUseBeforeDefSet.set(i); 
            this.itsNotDefSet.set(i);
          } else {
            lookForVariableAccess(paramOptFunctionNode, paramNode);
          } 
          return;
        }  
      lookForVariableAccess(paramOptFunctionNode, paramNode.getFirstChild().getNext());
      this.itsNotDefSet.set(paramOptFunctionNode.getVarIndex(paramNode));
    } else {
      i = paramOptFunctionNode.getVarIndex(paramNode);
      if (!this.itsNotDefSet.get(i))
        this.itsUseBeforeDefSet.set(i); 
    } 
  }
  
  private void markAnyTypeVariables(int[] paramArrayOfint) {
    for (byte b = 0; b != paramArrayOfint.length; b++) {
      if (this.itsLiveOnEntrySet.get(b))
        assignType(paramArrayOfint, b, 3); 
    } 
  }
  
  private static FatBlock newFatBlock(int paramInt1, int paramInt2) {
    FatBlock fatBlock = new FatBlock();
    fatBlock.realBlock = new Block(paramInt1, paramInt2);
    return fatBlock;
  }
  
  private void printLiveOnEntrySet(OptFunctionNode paramOptFunctionNode) {}
  
  private static void reachingDefDataFlow(OptFunctionNode paramOptFunctionNode, Node[] paramArrayOfNode, Block[] paramArrayOfBlock, int[] paramArrayOfint) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #4
    //   3: iload #4
    //   5: aload_2
    //   6: arraylength
    //   7: if_icmpge -> 25
    //   10: aload_2
    //   11: iload #4
    //   13: aaload
    //   14: aload_0
    //   15: aload_1
    //   16: invokespecial initLiveOnEntrySets : (Lcom/trendmicro/hippo/optimizer/OptFunctionNode;[Lcom/trendmicro/hippo/Node;)V
    //   19: iinc #4, 1
    //   22: goto -> 3
    //   25: aload_2
    //   26: arraylength
    //   27: newarray boolean
    //   29: astore_1
    //   30: aload_2
    //   31: arraylength
    //   32: newarray boolean
    //   34: astore #5
    //   36: aload_2
    //   37: arraylength
    //   38: iconst_1
    //   39: isub
    //   40: istore #6
    //   42: iconst_0
    //   43: istore #4
    //   45: aload_1
    //   46: iload #6
    //   48: iconst_1
    //   49: bastore
    //   50: aload_1
    //   51: iload #6
    //   53: baload
    //   54: ifne -> 69
    //   57: iload #4
    //   59: istore #7
    //   61: aload #5
    //   63: iload #6
    //   65: baload
    //   66: ifne -> 167
    //   69: aload #5
    //   71: iload #6
    //   73: iconst_1
    //   74: bastore
    //   75: aload_1
    //   76: iload #6
    //   78: iconst_0
    //   79: bastore
    //   80: iload #4
    //   82: istore #7
    //   84: aload_2
    //   85: iload #6
    //   87: aaload
    //   88: invokespecial doReachedUseDataFlow : ()Z
    //   91: ifeq -> 167
    //   94: aload_2
    //   95: iload #6
    //   97: aaload
    //   98: getfield itsPredecessors : [Lcom/trendmicro/hippo/optimizer/Block;
    //   101: astore_0
    //   102: iload #4
    //   104: istore #7
    //   106: aload_0
    //   107: ifnull -> 167
    //   110: iconst_0
    //   111: istore #8
    //   113: iload #4
    //   115: istore #7
    //   117: iload #8
    //   119: aload_0
    //   120: arraylength
    //   121: if_icmpge -> 167
    //   124: aload_0
    //   125: iload #8
    //   127: aaload
    //   128: getfield itsBlockID : I
    //   131: istore #7
    //   133: aload_1
    //   134: iload #7
    //   136: iconst_1
    //   137: bastore
    //   138: iload #7
    //   140: iload #6
    //   142: if_icmple -> 151
    //   145: iconst_1
    //   146: istore #7
    //   148: goto -> 154
    //   151: iconst_0
    //   152: istore #7
    //   154: iload #4
    //   156: iload #7
    //   158: ior
    //   159: istore #4
    //   161: iinc #8, 1
    //   164: goto -> 113
    //   167: iload #6
    //   169: ifne -> 197
    //   172: iload #7
    //   174: ifeq -> 189
    //   177: aload_2
    //   178: arraylength
    //   179: iconst_1
    //   180: isub
    //   181: istore #6
    //   183: iconst_0
    //   184: istore #4
    //   186: goto -> 50
    //   189: aload_2
    //   190: iconst_0
    //   191: aaload
    //   192: aload_3
    //   193: invokespecial markAnyTypeVariables : ([I)V
    //   196: return
    //   197: iinc #6, -1
    //   200: iload #7
    //   202: istore #4
    //   204: goto -> 50
  }
  
  static void runFlowAnalyzes(OptFunctionNode paramOptFunctionNode, Node[] paramArrayOfNode) {
    int i = paramOptFunctionNode.fnode.getParamCount();
    int j = paramOptFunctionNode.fnode.getParamAndVarCount();
    int[] arrayOfInt = new int[j];
    int k;
    for (k = 0; k != i; k++)
      arrayOfInt[k] = 3; 
    for (k = i; k != j; k++)
      arrayOfInt[k] = 0; 
    Block[] arrayOfBlock = buildBlocks(paramArrayOfNode);
    reachingDefDataFlow(paramOptFunctionNode, paramArrayOfNode, arrayOfBlock, arrayOfInt);
    typeFlow(paramOptFunctionNode, paramArrayOfNode, arrayOfBlock, arrayOfInt);
    while (i != j) {
      if (arrayOfInt[i] == 1)
        paramOptFunctionNode.setIsNumberVar(i); 
      i++;
    } 
  }
  
  private static String toString(Block[] paramArrayOfBlock, Node[] paramArrayOfNode) {
    return null;
  }
  
  private static void typeFlow(OptFunctionNode paramOptFunctionNode, Node[] paramArrayOfNode, Block[] paramArrayOfBlock, int[] paramArrayOfint) {
    // Byte code:
    //   0: aload_2
    //   1: arraylength
    //   2: newarray boolean
    //   4: astore #4
    //   6: aload_2
    //   7: arraylength
    //   8: newarray boolean
    //   10: astore #5
    //   12: iconst_0
    //   13: istore #6
    //   15: iconst_0
    //   16: istore #7
    //   18: aload #4
    //   20: iconst_0
    //   21: iconst_1
    //   22: bastore
    //   23: aload #4
    //   25: iload #6
    //   27: baload
    //   28: ifne -> 43
    //   31: iload #7
    //   33: istore #8
    //   35: aload #5
    //   37: iload #6
    //   39: baload
    //   40: ifne -> 150
    //   43: aload #5
    //   45: iload #6
    //   47: iconst_1
    //   48: bastore
    //   49: aload #4
    //   51: iload #6
    //   53: iconst_0
    //   54: bastore
    //   55: iload #7
    //   57: istore #8
    //   59: aload_2
    //   60: iload #6
    //   62: aaload
    //   63: aload_0
    //   64: aload_1
    //   65: aload_3
    //   66: invokespecial doTypeFlow : (Lcom/trendmicro/hippo/optimizer/OptFunctionNode;[Lcom/trendmicro/hippo/Node;[I)Z
    //   69: ifeq -> 150
    //   72: aload_2
    //   73: iload #6
    //   75: aaload
    //   76: getfield itsSuccessors : [Lcom/trendmicro/hippo/optimizer/Block;
    //   79: astore #9
    //   81: iload #7
    //   83: istore #8
    //   85: aload #9
    //   87: ifnull -> 150
    //   90: iconst_0
    //   91: istore #10
    //   93: iload #7
    //   95: istore #8
    //   97: iload #10
    //   99: aload #9
    //   101: arraylength
    //   102: if_icmpge -> 150
    //   105: aload #9
    //   107: iload #10
    //   109: aaload
    //   110: getfield itsBlockID : I
    //   113: istore #8
    //   115: aload #4
    //   117: iload #8
    //   119: iconst_1
    //   120: bastore
    //   121: iload #8
    //   123: iload #6
    //   125: if_icmpge -> 134
    //   128: iconst_1
    //   129: istore #8
    //   131: goto -> 137
    //   134: iconst_0
    //   135: istore #8
    //   137: iload #7
    //   139: iload #8
    //   141: ior
    //   142: istore #7
    //   144: iinc #10, 1
    //   147: goto -> 93
    //   150: iload #6
    //   152: aload_2
    //   153: arraylength
    //   154: iconst_1
    //   155: isub
    //   156: if_icmpne -> 174
    //   159: iload #8
    //   161: ifeq -> 173
    //   164: iconst_0
    //   165: istore #6
    //   167: iconst_0
    //   168: istore #7
    //   170: goto -> 23
    //   173: return
    //   174: iinc #6, 1
    //   177: iload #8
    //   179: istore #7
    //   181: goto -> 23
  }
  
  private static boolean updateEntrySet(BitSet paramBitSet1, BitSet paramBitSet2, BitSet paramBitSet3, BitSet paramBitSet4) {
    boolean bool;
    int i = paramBitSet1.cardinality();
    paramBitSet1.or(paramBitSet2);
    paramBitSet1.and(paramBitSet4);
    paramBitSet1.or(paramBitSet3);
    if (paramBitSet1.cardinality() != i) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private static class FatBlock {
    private ObjToIntMap predecessors = new ObjToIntMap();
    
    Block realBlock;
    
    private ObjToIntMap successors = new ObjToIntMap();
    
    private FatBlock() {}
    
    private static Block[] reduceToArray(ObjToIntMap param1ObjToIntMap) {
      Block[] arrayOfBlock = null;
      if (!param1ObjToIntMap.isEmpty()) {
        Block[] arrayOfBlock1 = new Block[param1ObjToIntMap.size()];
        byte b = 0;
        ObjToIntMap.Iterator iterator = param1ObjToIntMap.newIterator();
        iterator.start();
        while (true) {
          arrayOfBlock = arrayOfBlock1;
          if (!iterator.done()) {
            arrayOfBlock1[b] = ((FatBlock)iterator.getKey()).realBlock;
            iterator.next();
            b++;
            continue;
          } 
          break;
        } 
      } 
      return arrayOfBlock;
    }
    
    void addPredecessor(FatBlock param1FatBlock) {
      this.predecessors.put(param1FatBlock, 0);
    }
    
    void addSuccessor(FatBlock param1FatBlock) {
      this.successors.put(param1FatBlock, 0);
    }
    
    Block[] getPredecessors() {
      return reduceToArray(this.predecessors);
    }
    
    Block[] getSuccessors() {
      return reduceToArray(this.successors);
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/optimizer/Block.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */