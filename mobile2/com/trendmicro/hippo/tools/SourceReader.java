package com.trendmicro.hippo.tools;

import com.trendmicro.hippo.commonjs.module.provider.ParsedContentType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SourceReader {
  public static Object readFileOrUrl(String paramString1, boolean paramBoolean, String paramString2) throws IOException {
    InputStream inputStream;
    URL uRL = toUrl(paramString1);
    String str1 = null;
    String str2 = null;
    String str3 = null;
    File file = null;
    if (uRL == null) {
      try {
        file = new File();
        str1 = str2;
        try {
          this(paramString1);
          paramString1 = null;
          str1 = str2;
          int i = (int)file.length();
          str1 = str2;
          FileInputStream fileInputStream = new FileInputStream();
          str1 = str2;
          this(file);
          str2 = paramString1;
          int j = i;
        } finally {}
      } finally {}
    } else {
      String str;
      str1 = str2;
      URLConnection uRLConnection = uRL.openConnection();
      str1 = str2;
      InputStream inputStream2 = uRLConnection.getInputStream();
      if (paramBoolean) {
        InputStream inputStream3 = inputStream2;
        ParsedContentType parsedContentType = new ParsedContentType();
        inputStream3 = inputStream2;
        this(uRLConnection.getContentType());
        inputStream3 = inputStream2;
        str = parsedContentType.getContentType();
        inputStream3 = inputStream2;
        String str4 = parsedContentType.getEncoding();
      } else {
        paramString1 = null;
      } 
      inputStream = inputStream2;
      int j = uRLConnection.getContentLength();
      str3 = str;
      InputStream inputStream1 = inputStream2;
      int i = j;
      str2 = paramString1;
      if (j > 1048576) {
        i = -1;
        str2 = paramString1;
        inputStream1 = inputStream2;
        str3 = str;
      } 
      j = i;
    } 
    if (inputStream != null)
      inputStream.close(); 
    throw paramString1;
  }
  
  public static URL toUrl(String paramString) {
    if (paramString.indexOf(':') >= 2)
      try {
        return new URL(paramString);
      } catch (MalformedURLException malformedURLException) {} 
    return null;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/tools/SourceReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */