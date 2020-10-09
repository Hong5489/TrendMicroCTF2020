package com.trendmicro.hippo.commonjs.module.provider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;

public class UrlModuleSourceProvider extends ModuleSourceProviderBase {
  private static final long serialVersionUID = 1L;
  
  private final Iterable<URI> fallbackUris;
  
  private final Iterable<URI> privilegedUris;
  
  private final UrlConnectionExpiryCalculator urlConnectionExpiryCalculator;
  
  private final UrlConnectionSecurityDomainProvider urlConnectionSecurityDomainProvider;
  
  public UrlModuleSourceProvider(Iterable<URI> paramIterable1, Iterable<URI> paramIterable2) {
    this(paramIterable1, paramIterable2, new DefaultUrlConnectionExpiryCalculator(), null);
  }
  
  public UrlModuleSourceProvider(Iterable<URI> paramIterable1, Iterable<URI> paramIterable2, UrlConnectionExpiryCalculator paramUrlConnectionExpiryCalculator, UrlConnectionSecurityDomainProvider paramUrlConnectionSecurityDomainProvider) {
    this.privilegedUris = paramIterable1;
    this.fallbackUris = paramIterable2;
    this.urlConnectionExpiryCalculator = paramUrlConnectionExpiryCalculator;
    this.urlConnectionSecurityDomainProvider = paramUrlConnectionSecurityDomainProvider;
  }
  
  private void close(URLConnection paramURLConnection) {
    try {
      paramURLConnection.getInputStream().close();
    } catch (IOException iOException) {
      onFailedClosingUrlConnection(paramURLConnection, iOException);
    } 
  }
  
  private static String getCharacterEncoding(URLConnection paramURLConnection) {
    ParsedContentType parsedContentType = new ParsedContentType(paramURLConnection.getContentType());
    String str2 = parsedContentType.getEncoding();
    if (str2 != null)
      return str2; 
    String str1 = parsedContentType.getContentType();
    return (str1 != null && str1.startsWith("text/")) ? "8859_1" : "utf-8";
  }
  
  private static Reader getReader(URLConnection paramURLConnection) throws IOException {
    return new InputStreamReader(paramURLConnection.getInputStream(), getCharacterEncoding(paramURLConnection));
  }
  
  private Object getSecurityDomain(URLConnection paramURLConnection) {
    Object object;
    UrlConnectionSecurityDomainProvider urlConnectionSecurityDomainProvider = this.urlConnectionSecurityDomainProvider;
    if (urlConnectionSecurityDomainProvider == null) {
      paramURLConnection = null;
    } else {
      object = urlConnectionSecurityDomainProvider.getSecurityDomain(paramURLConnection);
    } 
    return object;
  }
  
  private ModuleSource loadFromPathList(String paramString, Object paramObject, Iterable<URI> paramIterable) throws IOException, URISyntaxException {
    if (paramIterable == null)
      return null; 
    for (URI uRI : paramIterable) {
      ModuleSource moduleSource = loadFromUri(uRI.resolve(paramString), uRI, paramObject);
      if (moduleSource != null)
        return moduleSource; 
    } 
    return null;
  }
  
  protected boolean entityNeedsRevalidation(Object paramObject) {
    return (!(paramObject instanceof URLValidator) || ((URLValidator)paramObject).entityNeedsRevalidation());
  }
  
  protected ModuleSource loadFromActualUri(URI paramURI1, URI paramURI2, Object paramObject) throws IOException {
    if (paramURI2 == null) {
      uRL = null;
    } else {
      uRL = paramURI2.toURL();
    } 
    URL uRL = new URL(uRL, paramURI1.toString());
    long l = System.currentTimeMillis();
    URLConnection uRLConnection = openUrlConnection(uRL);
    if (paramObject instanceof URLValidator) {
      paramObject = paramObject;
      if (!paramObject.appliesTo(paramURI1))
        paramObject = null; 
    } else {
      paramObject = null;
    } 
    if (paramObject != null)
      paramObject.applyConditionals(uRLConnection); 
    try {
      uRLConnection.connect();
      if (paramObject != null && !paramObject.updateValidator(uRLConnection, l, this.urlConnectionExpiryCalculator)) {
        close(uRLConnection);
        return NOT_MODIFIED;
      } 
      paramObject = getReader(uRLConnection);
      Object object = getSecurityDomain(uRLConnection);
      URLValidator uRLValidator = new URLValidator();
      this(paramURI1, uRLConnection, l, this.urlConnectionExpiryCalculator);
      return new ModuleSource((Reader)paramObject, object, paramURI1, paramURI2, uRLValidator);
    } catch (FileNotFoundException fileNotFoundException) {
      return null;
    } catch (RuntimeException runtimeException) {
      close(uRLConnection);
      throw runtimeException;
    } catch (IOException iOException) {
      close(uRLConnection);
      throw iOException;
    } 
  }
  
  protected ModuleSource loadFromFallbackLocations(String paramString, Object paramObject) throws IOException, URISyntaxException {
    return loadFromPathList(paramString, paramObject, this.fallbackUris);
  }
  
  protected ModuleSource loadFromPrivilegedLocations(String paramString, Object paramObject) throws IOException, URISyntaxException {
    return loadFromPathList(paramString, paramObject, this.privilegedUris);
  }
  
  protected ModuleSource loadFromUri(URI paramURI1, URI paramURI2, Object paramObject) throws IOException, URISyntaxException {
    ModuleSource moduleSource1;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramURI1);
    stringBuilder.append(".js");
    ModuleSource moduleSource2 = loadFromActualUri(new URI(stringBuilder.toString()), paramURI2, paramObject);
    if (moduleSource2 != null) {
      moduleSource1 = moduleSource2;
    } else {
      moduleSource1 = loadFromActualUri((URI)moduleSource1, paramURI2, paramObject);
    } 
    return moduleSource1;
  }
  
  protected void onFailedClosingUrlConnection(URLConnection paramURLConnection, IOException paramIOException) {}
  
  protected URLConnection openUrlConnection(URL paramURL) throws IOException {
    return paramURL.openConnection();
  }
  
  private static class URLValidator implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final String entityTags;
    
    private long expiry;
    
    private final long lastModified;
    
    private final URI uri;
    
    public URLValidator(URI param1URI, URLConnection param1URLConnection, long param1Long, UrlConnectionExpiryCalculator param1UrlConnectionExpiryCalculator) {
      this.uri = param1URI;
      this.lastModified = param1URLConnection.getLastModified();
      this.entityTags = getEntityTags(param1URLConnection);
      this.expiry = calculateExpiry(param1URLConnection, param1Long, param1UrlConnectionExpiryCalculator);
    }
    
    private long calculateExpiry(URLConnection param1URLConnection, long param1Long, UrlConnectionExpiryCalculator param1UrlConnectionExpiryCalculator) {
      boolean bool = "no-cache".equals(param1URLConnection.getHeaderField("Pragma"));
      long l = 0L;
      if (bool)
        return 0L; 
      String str = param1URLConnection.getHeaderField("Cache-Control");
      if (str != null) {
        if (str.indexOf("no-cache") != -1)
          return 0L; 
        int i = getMaxAge(str);
        if (-1 != i) {
          l = System.currentTimeMillis();
          long l1 = Math.max(Math.max(0L, l - param1URLConnection.getDate()), param1URLConnection.getHeaderFieldInt("Age", 0) * 1000L);
          return i * 1000L + l - l1 + l - param1Long;
        } 
      } 
      param1Long = param1URLConnection.getHeaderFieldDate("Expires", -1L);
      if (param1Long != -1L)
        return param1Long; 
      if (param1UrlConnectionExpiryCalculator == null) {
        param1Long = l;
      } else {
        param1Long = param1UrlConnectionExpiryCalculator.calculateExpiry(param1URLConnection);
      } 
      return param1Long;
    }
    
    private String getEntityTags(URLConnection param1URLConnection) {
      List list = param1URLConnection.getHeaderFields().get("ETag");
      if (list == null || list.isEmpty())
        return null; 
      StringBuilder stringBuilder = new StringBuilder();
      Iterator<String> iterator = list.iterator();
      stringBuilder.append(iterator.next());
      while (iterator.hasNext()) {
        stringBuilder.append(", ");
        stringBuilder.append(iterator.next());
      } 
      return stringBuilder.toString();
    }
    
    private int getMaxAge(String param1String) {
      int i = param1String.indexOf("max-age");
      if (i == -1)
        return -1; 
      i = param1String.indexOf('=', i + 7);
      if (i == -1)
        return -1; 
      int j = param1String.indexOf(',', i + 1);
      if (j == -1) {
        param1String = param1String.substring(i + 1);
      } else {
        param1String = param1String.substring(i + 1, j);
      } 
      try {
        return Integer.parseInt(param1String);
      } catch (NumberFormatException numberFormatException) {
        return -1;
      } 
    }
    
    private boolean isResourceChanged(URLConnection param1URLConnection) throws IOException {
      boolean bool = param1URLConnection instanceof HttpURLConnection;
      boolean bool1 = true;
      boolean bool2 = true;
      if (bool) {
        if (((HttpURLConnection)param1URLConnection).getResponseCode() != 304)
          bool2 = false; 
        return bool2;
      } 
      if (this.lastModified != param1URLConnection.getLastModified()) {
        bool2 = bool1;
      } else {
        bool2 = false;
      } 
      return bool2;
    }
    
    boolean appliesTo(URI param1URI) {
      return this.uri.equals(param1URI);
    }
    
    void applyConditionals(URLConnection param1URLConnection) {
      long l = this.lastModified;
      if (l != 0L)
        param1URLConnection.setIfModifiedSince(l); 
      String str = this.entityTags;
      if (str != null && str.length() > 0)
        param1URLConnection.addRequestProperty("If-None-Match", this.entityTags); 
    }
    
    boolean entityNeedsRevalidation() {
      boolean bool;
      if (System.currentTimeMillis() > this.expiry) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean updateValidator(URLConnection param1URLConnection, long param1Long, UrlConnectionExpiryCalculator param1UrlConnectionExpiryCalculator) throws IOException {
      boolean bool = isResourceChanged(param1URLConnection);
      if (!bool)
        this.expiry = calculateExpiry(param1URLConnection, param1Long, param1UrlConnectionExpiryCalculator); 
      return bool;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/commonjs/module/provider/UrlModuleSourceProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */