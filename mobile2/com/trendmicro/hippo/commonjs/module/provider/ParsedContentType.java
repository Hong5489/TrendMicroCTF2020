package com.trendmicro.hippo.commonjs.module.provider;

import java.io.Serializable;
import java.util.StringTokenizer;

public final class ParsedContentType implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private final String contentType;
  
  private final String encoding;
  
  public ParsedContentType(String paramString) {
    String str1 = null;
    String str2 = null;
    String str3 = str1;
    String str4 = str2;
    if (paramString != null) {
      StringTokenizer stringTokenizer = new StringTokenizer(paramString, ";");
      str3 = str1;
      str4 = str2;
      if (stringTokenizer.hasMoreTokens()) {
        str1 = stringTokenizer.nextToken().trim();
        while (true) {
          str3 = str1;
          str4 = str2;
          if (stringTokenizer.hasMoreTokens()) {
            str4 = stringTokenizer.nextToken().trim();
            if (str4.startsWith("charset=")) {
              str2 = str4.substring(8).trim();
              int i = str2.length();
              str3 = str1;
              str4 = str2;
              if (i > 0) {
                String str = str2;
                if (str2.charAt(0) == '"')
                  str = str2.substring(1); 
                str3 = str1;
                str4 = str;
                if (str.charAt(i - 1) == '"') {
                  str4 = str.substring(0, i - 1);
                  str3 = str1;
                } 
              } 
              break;
            } 
            continue;
          } 
          break;
        } 
      } 
    } 
    this.contentType = str3;
    this.encoding = str4;
  }
  
  public String getContentType() {
    return this.contentType;
  }
  
  public String getEncoding() {
    return this.encoding;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/commonjs/module/provider/ParsedContentType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */