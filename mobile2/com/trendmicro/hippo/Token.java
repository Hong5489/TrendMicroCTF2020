package com.trendmicro.hippo;

public class Token {
  public static final int ADD = 21;
  
  public static final int AND = 106;
  
  public static final int ARRAYCOMP = 158;
  
  public static final int ARRAYLIT = 66;
  
  public static final int ARROW = 165;
  
  public static final int ASSIGN = 91;
  
  public static final int ASSIGN_ADD = 98;
  
  public static final int ASSIGN_BITAND = 94;
  
  public static final int ASSIGN_BITOR = 92;
  
  public static final int ASSIGN_BITXOR = 93;
  
  public static final int ASSIGN_DIV = 101;
  
  public static final int ASSIGN_LSH = 95;
  
  public static final int ASSIGN_MOD = 102;
  
  public static final int ASSIGN_MUL = 100;
  
  public static final int ASSIGN_RSH = 96;
  
  public static final int ASSIGN_SUB = 99;
  
  public static final int ASSIGN_URSH = 97;
  
  public static final int BINDNAME = 49;
  
  public static final int BITAND = 11;
  
  public static final int BITNOT = 27;
  
  public static final int BITOR = 9;
  
  public static final int BITXOR = 10;
  
  public static final int BLOCK = 130;
  
  public static final int BREAK = 121;
  
  public static final int CALL = 38;
  
  public static final int CASE = 116;
  
  public static final int CATCH = 125;
  
  public static final int CATCH_SCOPE = 57;
  
  public static final int COLON = 104;
  
  public static final int COLONCOLON = 145;
  
  public static final int COMMA = 90;
  
  public static final int COMMENT = 162;
  
  public static final int CONST = 155;
  
  public static final int CONTINUE = 122;
  
  public static final int DEBUGGER = 161;
  
  public static final int DEC = 108;
  
  public static final int DEFAULT = 117;
  
  public static final int DEFAULTNAMESPACE = 75;
  
  public static final int DELPROP = 31;
  
  public static final int DEL_REF = 70;
  
  public static final int DIV = 24;
  
  public static final int DO = 119;
  
  public static final int DOT = 109;
  
  public static final int DOTDOT = 144;
  
  public static final int DOTQUERY = 147;
  
  public static final int ELSE = 114;
  
  public static final int EMPTY = 129;
  
  public static final int ENTERWITH = 2;
  
  public static final int ENUM_ID = 63;
  
  public static final int ENUM_INIT_ARRAY = 60;
  
  public static final int ENUM_INIT_KEYS = 58;
  
  public static final int ENUM_INIT_VALUES = 59;
  
  public static final int ENUM_INIT_VALUES_IN_ORDER = 61;
  
  public static final int ENUM_NEXT = 62;
  
  public static final int EOF = 0;
  
  public static final int EOL = 1;
  
  public static final int EQ = 12;
  
  public static final int ERROR = -1;
  
  public static final int ESCXMLATTR = 76;
  
  public static final int ESCXMLTEXT = 77;
  
  public static final int EXPORT = 111;
  
  public static final int EXPR_RESULT = 135;
  
  public static final int EXPR_VOID = 134;
  
  public static final int FALSE = 44;
  
  public static final int FINALLY = 126;
  
  public static final int FIRST_ASSIGN = 91;
  
  public static final int FIRST_BYTECODE_TOKEN = 2;
  
  public static final int FOR = 120;
  
  public static final int FUNCTION = 110;
  
  public static final int GE = 17;
  
  public static final int GENEXPR = 163;
  
  public static final int GET = 152;
  
  public static final int GETELEM = 36;
  
  public static final int GETPROP = 33;
  
  public static final int GETPROPNOWARN = 34;
  
  public static final int GETVAR = 55;
  
  public static final int GET_REF = 68;
  
  public static final int GOTO = 5;
  
  public static final int GT = 16;
  
  public static final int HOOK = 103;
  
  public static final int IF = 113;
  
  public static final int IFEQ = 6;
  
  public static final int IFNE = 7;
  
  public static final int IMPORT = 112;
  
  public static final int IN = 52;
  
  public static final int INC = 107;
  
  public static final int INSTANCEOF = 53;
  
  public static final int JSR = 136;
  
  public static final int LABEL = 131;
  
  public static final int LAST_ASSIGN = 102;
  
  public static final int LAST_BYTECODE_TOKEN = 81;
  
  public static final int LAST_TOKEN = 166;
  
  public static final int LB = 84;
  
  public static final int LC = 86;
  
  public static final int LE = 15;
  
  public static final int LEAVEWITH = 3;
  
  public static final int LET = 154;
  
  public static final int LETEXPR = 159;
  
  public static final int LOCAL_BLOCK = 142;
  
  public static final int LOCAL_LOAD = 54;
  
  public static final int LOOP = 133;
  
  public static final int LP = 88;
  
  public static final int LSH = 18;
  
  public static final int LT = 14;
  
  public static final int METHOD = 164;
  
  public static final int MOD = 25;
  
  public static final int MUL = 23;
  
  public static final int NAME = 39;
  
  public static final int NE = 13;
  
  public static final int NEG = 29;
  
  public static final int NEW = 30;
  
  public static final int NOT = 26;
  
  public static final int NULL = 42;
  
  public static final int NUMBER = 40;
  
  public static final int OBJECTLIT = 67;
  
  public static final int OR = 105;
  
  public static final int POS = 28;
  
  public static final int RB = 85;
  
  public static final int RC = 87;
  
  public static final int REF_CALL = 71;
  
  public static final int REF_MEMBER = 78;
  
  public static final int REF_NAME = 80;
  
  public static final int REF_NS_MEMBER = 79;
  
  public static final int REF_NS_NAME = 81;
  
  public static final int REF_SPECIAL = 72;
  
  public static final int REGEXP = 48;
  
  public static final int RESERVED = 128;
  
  public static final int RETHROW = 51;
  
  public static final int RETURN = 4;
  
  public static final int RETURN_RESULT = 65;
  
  public static final int RP = 89;
  
  public static final int RSH = 19;
  
  public static final int SCRIPT = 137;
  
  public static final int SEMI = 83;
  
  public static final int SET = 153;
  
  public static final int SETCONST = 156;
  
  public static final int SETCONSTVAR = 157;
  
  public static final int SETELEM = 37;
  
  public static final int SETELEM_OP = 141;
  
  public static final int SETNAME = 8;
  
  public static final int SETPROP = 35;
  
  public static final int SETPROP_OP = 140;
  
  public static final int SETVAR = 56;
  
  public static final int SET_REF = 69;
  
  public static final int SET_REF_OP = 143;
  
  public static final int SHEQ = 46;
  
  public static final int SHNE = 47;
  
  public static final int STRICT_SETNAME = 74;
  
  public static final int STRING = 41;
  
  public static final int SUB = 22;
  
  public static final int SWITCH = 115;
  
  public static final int TARGET = 132;
  
  public static final int THIS = 43;
  
  public static final int THISFN = 64;
  
  public static final int THROW = 50;
  
  public static final int TO_DOUBLE = 151;
  
  public static final int TO_OBJECT = 150;
  
  public static final int TRUE = 45;
  
  public static final int TRY = 82;
  
  public static final int TYPEOF = 32;
  
  public static final int TYPEOFNAME = 138;
  
  public static final int URSH = 20;
  
  public static final int USE_STACK = 139;
  
  public static final int VAR = 123;
  
  public static final int VOID = 127;
  
  public static final int WHILE = 118;
  
  public static final int WITH = 124;
  
  public static final int WITHEXPR = 160;
  
  public static final int XML = 146;
  
  public static final int XMLATTR = 148;
  
  public static final int XMLEND = 149;
  
  public static final int YIELD = 73;
  
  static final boolean printICode = false;
  
  static final boolean printNames = false;
  
  public static final boolean printTrees = false;
  
  public static boolean isValidToken(int paramInt) {
    boolean bool;
    if (paramInt >= -1 && paramInt <= 166) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static String keywordToName(int paramInt) {
    if (paramInt != 4) {
      if (paramInt != 50) {
        if (paramInt != 73) {
          if (paramInt != 82) {
            if (paramInt != 110) {
              if (paramInt != 161) {
                if (paramInt != 52) {
                  if (paramInt != 53) {
                    if (paramInt != 154) {
                      if (paramInt != 155) {
                        switch (paramInt) {
                          default:
                            switch (paramInt) {
                              default:
                                switch (paramInt) {
                                  default:
                                    return null;
                                  case 127:
                                    return "void";
                                  case 126:
                                    return "finally";
                                  case 125:
                                    return "catch";
                                  case 124:
                                    return "with";
                                  case 123:
                                    return "var";
                                  case 122:
                                    return "continue";
                                  case 121:
                                    return "break";
                                  case 120:
                                    return "for";
                                  case 119:
                                    return "do";
                                  case 118:
                                    return "while";
                                  case 117:
                                    return "default";
                                  case 116:
                                    return "case";
                                  case 115:
                                    return "switch";
                                  case 114:
                                    return "else";
                                  case 113:
                                    break;
                                } 
                                return "if";
                              case 45:
                                return "true";
                              case 44:
                                return "false";
                              case 43:
                                return "this";
                              case 42:
                                break;
                            } 
                            return "null";
                          case 32:
                            return "typeof";
                          case 31:
                            return "delete";
                          case 30:
                            break;
                        } 
                        return "new";
                      } 
                      return "const";
                    } 
                    return "let";
                  } 
                  return "instanceof";
                } 
                return "in";
              } 
              return "debugger";
            } 
            return "function";
          } 
          return "try";
        } 
        return "yield";
      } 
      return "throw";
    } 
    return "return";
  }
  
  public static String name(int paramInt) {
    return String.valueOf(paramInt);
  }
  
  public static String typeToName(int paramInt) {
    switch (paramInt) {
      default:
        throw new IllegalStateException(String.valueOf(paramInt));
      case 165:
        return "ARROW";
      case 164:
        return "METHOD";
      case 163:
        return "GENEXPR";
      case 162:
        return "COMMENT";
      case 161:
        return "DEBUGGER";
      case 160:
        return "WITHEXPR";
      case 159:
        return "LETEXPR";
      case 158:
        return "ARRAYCOMP";
      case 156:
        return "SETCONST";
      case 155:
        return "CONST";
      case 154:
        return "LET";
      case 153:
        return "SET";
      case 152:
        return "GET";
      case 151:
        return "TO_DOUBLE";
      case 150:
        return "TO_OBJECT";
      case 149:
        return "XMLEND";
      case 148:
        return "XMLATTR";
      case 147:
        return "DOTQUERY";
      case 146:
        return "XML";
      case 145:
        return "COLONCOLON";
      case 144:
        return "DOTDOT";
      case 143:
        return "SET_REF_OP";
      case 142:
        return "LOCAL_BLOCK";
      case 141:
        return "SETELEM_OP";
      case 140:
        return "SETPROP_OP";
      case 139:
        return "USE_STACK";
      case 138:
        return "TYPEOFNAME";
      case 137:
        return "SCRIPT";
      case 136:
        return "JSR";
      case 135:
        return "EXPR_RESULT";
      case 134:
        return "EXPR_VOID";
      case 133:
        return "LOOP";
      case 132:
        return "TARGET";
      case 131:
        return "LABEL";
      case 130:
        return "BLOCK";
      case 129:
        return "EMPTY";
      case 128:
        return "RESERVED";
      case 127:
        return "VOID";
      case 126:
        return "FINALLY";
      case 125:
        return "CATCH";
      case 124:
        return "WITH";
      case 123:
        return "VAR";
      case 122:
        return "CONTINUE";
      case 121:
        return "BREAK";
      case 120:
        return "FOR";
      case 119:
        return "DO";
      case 118:
        return "WHILE";
      case 117:
        return "DEFAULT";
      case 116:
        return "CASE";
      case 115:
        return "SWITCH";
      case 114:
        return "ELSE";
      case 113:
        return "IF";
      case 112:
        return "IMPORT";
      case 111:
        return "EXPORT";
      case 110:
        return "FUNCTION";
      case 109:
        return "DOT";
      case 108:
        return "DEC";
      case 107:
        return "INC";
      case 106:
        return "AND";
      case 105:
        return "OR";
      case 104:
        return "COLON";
      case 103:
        return "HOOK";
      case 102:
        return "ASSIGN_MOD";
      case 101:
        return "ASSIGN_DIV";
      case 100:
        return "ASSIGN_MUL";
      case 99:
        return "ASSIGN_SUB";
      case 98:
        return "ASSIGN_ADD";
      case 97:
        return "ASSIGN_URSH";
      case 96:
        return "ASSIGN_RSH";
      case 95:
        return "ASSIGN_LSH";
      case 94:
        return "ASSIGN_BITAND";
      case 93:
        return "ASSIGN_BITXOR";
      case 92:
        return "ASSIGN_BITOR";
      case 91:
        return "ASSIGN";
      case 90:
        return "COMMA";
      case 89:
        return "RP";
      case 88:
        return "LP";
      case 87:
        return "RC";
      case 86:
        return "LC";
      case 85:
        return "RB";
      case 84:
        return "LB";
      case 83:
        return "SEMI";
      case 82:
        return "TRY";
      case 81:
        return "REF_NS_NAME";
      case 80:
        return "REF_NAME";
      case 79:
        return "REF_NS_MEMBER";
      case 78:
        return "REF_MEMBER";
      case 77:
        return "ESCXMLTEXT";
      case 76:
        return "ESCXMLATTR";
      case 75:
        return "DEFAULTNAMESPACE";
      case 73:
        return "YIELD";
      case 72:
        return "REF_SPECIAL";
      case 71:
        return "REF_CALL";
      case 70:
        return "DEL_REF";
      case 69:
        return "SET_REF";
      case 68:
        return "GET_REF";
      case 67:
        return "OBJECTLIT";
      case 66:
        return "ARRAYLIT";
      case 65:
        return "RETURN_RESULT";
      case 64:
        return "THISFN";
      case 63:
        return "ENUM_ID";
      case 62:
        return "ENUM_NEXT";
      case 61:
        return "ENUM_INIT_VALUES_IN_ORDER";
      case 60:
        return "ENUM_INIT_ARRAY";
      case 59:
        return "ENUM_INIT_VALUES";
      case 58:
        return "ENUM_INIT_KEYS";
      case 57:
        return "CATCH_SCOPE";
      case 56:
        return "SETVAR";
      case 55:
        return "GETVAR";
      case 54:
        return "LOCAL_LOAD";
      case 53:
        return "INSTANCEOF";
      case 52:
        return "IN";
      case 51:
        return "RETHROW";
      case 50:
        return "THROW";
      case 49:
        return "BINDNAME";
      case 48:
        return "REGEXP";
      case 47:
        return "SHNE";
      case 46:
        return "SHEQ";
      case 45:
        return "TRUE";
      case 44:
        return "FALSE";
      case 43:
        return "THIS";
      case 42:
        return "NULL";
      case 41:
        return "STRING";
      case 40:
        return "NUMBER";
      case 39:
        return "NAME";
      case 38:
        return "CALL";
      case 37:
        return "SETELEM";
      case 36:
        return "GETELEM";
      case 35:
        return "SETPROP";
      case 34:
        return "GETPROPNOWARN";
      case 33:
        return "GETPROP";
      case 32:
        return "TYPEOF";
      case 31:
        return "DELPROP";
      case 30:
        return "NEW";
      case 29:
        return "NEG";
      case 28:
        return "POS";
      case 27:
        return "BITNOT";
      case 26:
        return "NOT";
      case 25:
        return "MOD";
      case 24:
        return "DIV";
      case 23:
        return "MUL";
      case 22:
        return "SUB";
      case 21:
        return "ADD";
      case 20:
        return "URSH";
      case 19:
        return "RSH";
      case 18:
        return "LSH";
      case 17:
        return "GE";
      case 16:
        return "GT";
      case 15:
        return "LE";
      case 14:
        return "LT";
      case 13:
        return "NE";
      case 12:
        return "EQ";
      case 11:
        return "BITAND";
      case 10:
        return "BITXOR";
      case 9:
        return "BITOR";
      case 8:
        return "SETNAME";
      case 7:
        return "IFNE";
      case 6:
        return "IFEQ";
      case 5:
        return "GOTO";
      case 4:
        return "RETURN";
      case 3:
        return "LEAVEWITH";
      case 2:
        return "ENTERWITH";
      case 1:
        return "EOL";
      case 0:
        return "EOF";
      case -1:
        break;
    } 
    return "ERROR";
  }
  
  public enum CommentType {
    BLOCK_COMMENT, HTML, JSDOC, LINE;
    
    static {
      CommentType commentType = new CommentType("HTML", 3);
      HTML = commentType;
      $VALUES = new CommentType[] { LINE, BLOCK_COMMENT, JSDOC, commentType };
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/Token.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */