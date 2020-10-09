package android.support.v4.provider;

import android.content.Context;
import android.net.Uri;
import android.provider.DocumentsContract;

class TreeDocumentFile extends DocumentFile {
  private Context mContext;
  
  private Uri mUri;
  
  TreeDocumentFile(DocumentFile paramDocumentFile, Context paramContext, Uri paramUri) {
    super(paramDocumentFile);
    this.mContext = paramContext;
    this.mUri = paramUri;
  }
  
  private static void closeQuietly(AutoCloseable paramAutoCloseable) {
    if (paramAutoCloseable != null)
      try {
        paramAutoCloseable.close();
      } catch (RuntimeException runtimeException) {
        throw runtimeException;
      } catch (Exception exception) {} 
  }
  
  private static Uri createFile(Context paramContext, Uri paramUri, String paramString1, String paramString2) {
    try {
      return DocumentsContract.createDocument(paramContext.getContentResolver(), paramUri, paramString1, paramString2);
    } catch (Exception exception) {
      return null;
    } 
  }
  
  public boolean canRead() {
    return DocumentsContractApi19.canRead(this.mContext, this.mUri);
  }
  
  public boolean canWrite() {
    return DocumentsContractApi19.canWrite(this.mContext, this.mUri);
  }
  
  public DocumentFile createDirectory(String paramString) {
    Uri uri = createFile(this.mContext, this.mUri, "vnd.android.document/directory", paramString);
    if (uri != null) {
      TreeDocumentFile treeDocumentFile = new TreeDocumentFile(this, this.mContext, uri);
    } else {
      uri = null;
    } 
    return (DocumentFile)uri;
  }
  
  public DocumentFile createFile(String paramString1, String paramString2) {
    Uri uri = createFile(this.mContext, this.mUri, paramString1, paramString2);
    if (uri != null) {
      TreeDocumentFile treeDocumentFile = new TreeDocumentFile(this, this.mContext, uri);
    } else {
      uri = null;
    } 
    return (DocumentFile)uri;
  }
  
  public boolean delete() {
    try {
      return DocumentsContract.deleteDocument(this.mContext.getContentResolver(), this.mUri);
    } catch (Exception exception) {
      return false;
    } 
  }
  
  public boolean exists() {
    return DocumentsContractApi19.exists(this.mContext, this.mUri);
  }
  
  public String getName() {
    return DocumentsContractApi19.getName(this.mContext, this.mUri);
  }
  
  public String getType() {
    return DocumentsContractApi19.getType(this.mContext, this.mUri);
  }
  
  public Uri getUri() {
    return this.mUri;
  }
  
  public boolean isDirectory() {
    return DocumentsContractApi19.isDirectory(this.mContext, this.mUri);
  }
  
  public boolean isFile() {
    return DocumentsContractApi19.isFile(this.mContext, this.mUri);
  }
  
  public boolean isVirtual() {
    return DocumentsContractApi19.isVirtual(this.mContext, this.mUri);
  }
  
  public long lastModified() {
    return DocumentsContractApi19.lastModified(this.mContext, this.mUri);
  }
  
  public long length() {
    return DocumentsContractApi19.length(this.mContext, this.mUri);
  }
  
  public DocumentFile[] listFiles() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mContext : Landroid/content/Context;
    //   4: invokevirtual getContentResolver : ()Landroid/content/ContentResolver;
    //   7: astore_1
    //   8: aload_0
    //   9: getfield mUri : Landroid/net/Uri;
    //   12: astore_2
    //   13: aload_2
    //   14: aload_2
    //   15: invokestatic getDocumentId : (Landroid/net/Uri;)Ljava/lang/String;
    //   18: invokestatic buildChildDocumentsUriUsingTree : (Landroid/net/Uri;Ljava/lang/String;)Landroid/net/Uri;
    //   21: astore_3
    //   22: new java/util/ArrayList
    //   25: dup
    //   26: invokespecial <init> : ()V
    //   29: astore #4
    //   31: aconst_null
    //   32: astore_2
    //   33: aconst_null
    //   34: astore #5
    //   36: aload_1
    //   37: aload_3
    //   38: iconst_1
    //   39: anewarray java/lang/String
    //   42: dup
    //   43: iconst_0
    //   44: ldc 'document_id'
    //   46: aastore
    //   47: aconst_null
    //   48: aconst_null
    //   49: aconst_null
    //   50: invokevirtual query : (Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   53: astore_1
    //   54: aload_1
    //   55: astore #5
    //   57: aload_1
    //   58: astore_2
    //   59: aload_1
    //   60: invokeinterface moveToNext : ()Z
    //   65: ifeq -> 103
    //   68: aload_1
    //   69: astore #5
    //   71: aload_1
    //   72: astore_2
    //   73: aload_1
    //   74: iconst_0
    //   75: invokeinterface getString : (I)Ljava/lang/String;
    //   80: astore_3
    //   81: aload_1
    //   82: astore #5
    //   84: aload_1
    //   85: astore_2
    //   86: aload #4
    //   88: aload_0
    //   89: getfield mUri : Landroid/net/Uri;
    //   92: aload_3
    //   93: invokestatic buildDocumentUriUsingTree : (Landroid/net/Uri;Ljava/lang/String;)Landroid/net/Uri;
    //   96: invokevirtual add : (Ljava/lang/Object;)Z
    //   99: pop
    //   100: goto -> 54
    //   103: aload_1
    //   104: astore_2
    //   105: aload_2
    //   106: invokestatic closeQuietly : (Ljava/lang/AutoCloseable;)V
    //   109: goto -> 166
    //   112: astore_2
    //   113: goto -> 230
    //   116: astore_3
    //   117: aload_2
    //   118: astore #5
    //   120: new java/lang/StringBuilder
    //   123: astore_1
    //   124: aload_2
    //   125: astore #5
    //   127: aload_1
    //   128: invokespecial <init> : ()V
    //   131: aload_2
    //   132: astore #5
    //   134: aload_1
    //   135: ldc 'Failed query: '
    //   137: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   140: pop
    //   141: aload_2
    //   142: astore #5
    //   144: aload_1
    //   145: aload_3
    //   146: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   149: pop
    //   150: aload_2
    //   151: astore #5
    //   153: ldc 'DocumentFile'
    //   155: aload_1
    //   156: invokevirtual toString : ()Ljava/lang/String;
    //   159: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
    //   162: pop
    //   163: goto -> 105
    //   166: aload #4
    //   168: aload #4
    //   170: invokevirtual size : ()I
    //   173: anewarray android/net/Uri
    //   176: invokevirtual toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   179: checkcast [Landroid/net/Uri;
    //   182: astore_2
    //   183: aload_2
    //   184: arraylength
    //   185: anewarray android/support/v4/provider/DocumentFile
    //   188: astore #5
    //   190: iconst_0
    //   191: istore #6
    //   193: iload #6
    //   195: aload_2
    //   196: arraylength
    //   197: if_icmpge -> 227
    //   200: aload #5
    //   202: iload #6
    //   204: new android/support/v4/provider/TreeDocumentFile
    //   207: dup
    //   208: aload_0
    //   209: aload_0
    //   210: getfield mContext : Landroid/content/Context;
    //   213: aload_2
    //   214: iload #6
    //   216: aaload
    //   217: invokespecial <init> : (Landroid/support/v4/provider/DocumentFile;Landroid/content/Context;Landroid/net/Uri;)V
    //   220: aastore
    //   221: iinc #6, 1
    //   224: goto -> 193
    //   227: aload #5
    //   229: areturn
    //   230: aload #5
    //   232: invokestatic closeQuietly : (Ljava/lang/AutoCloseable;)V
    //   235: aload_2
    //   236: athrow
    // Exception table:
    //   from	to	target	type
    //   36	54	116	java/lang/Exception
    //   36	54	112	finally
    //   59	68	116	java/lang/Exception
    //   59	68	112	finally
    //   73	81	116	java/lang/Exception
    //   73	81	112	finally
    //   86	100	116	java/lang/Exception
    //   86	100	112	finally
    //   120	124	112	finally
    //   127	131	112	finally
    //   134	141	112	finally
    //   144	150	112	finally
    //   153	163	112	finally
  }
  
  public boolean renameTo(String paramString) {
    try {
      Uri uri = DocumentsContract.renameDocument(this.mContext.getContentResolver(), this.mUri, paramString);
      if (uri != null) {
        this.mUri = uri;
        return true;
      } 
      return false;
    } catch (Exception exception) {
      return false;
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v4/provider/TreeDocumentFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */