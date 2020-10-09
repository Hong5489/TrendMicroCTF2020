package android.support.v4.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParserException;

public class FileProvider extends ContentProvider {
  private static final String ATTR_NAME = "name";
  
  private static final String ATTR_PATH = "path";
  
  private static final String[] COLUMNS = new String[] { "_display_name", "_size" };
  
  private static final File DEVICE_ROOT = new File("/");
  
  private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
  
  private static final String TAG_CACHE_PATH = "cache-path";
  
  private static final String TAG_EXTERNAL = "external-path";
  
  private static final String TAG_EXTERNAL_CACHE = "external-cache-path";
  
  private static final String TAG_EXTERNAL_FILES = "external-files-path";
  
  private static final String TAG_EXTERNAL_MEDIA = "external-media-path";
  
  private static final String TAG_FILES_PATH = "files-path";
  
  private static final String TAG_ROOT_PATH = "root-path";
  
  private static HashMap<String, PathStrategy> sCache = new HashMap<>();
  
  private PathStrategy mStrategy;
  
  private static File buildPath(File paramFile, String... paramVarArgs) {
    int i = paramVarArgs.length;
    byte b = 0;
    while (b < i) {
      String str = paramVarArgs[b];
      File file = paramFile;
      if (str != null)
        file = new File(paramFile, str); 
      b++;
      paramFile = file;
    } 
    return paramFile;
  }
  
  private static Object[] copyOf(Object[] paramArrayOfObject, int paramInt) {
    Object[] arrayOfObject = new Object[paramInt];
    System.arraycopy(paramArrayOfObject, 0, arrayOfObject, 0, paramInt);
    return arrayOfObject;
  }
  
  private static String[] copyOf(String[] paramArrayOfString, int paramInt) {
    String[] arrayOfString = new String[paramInt];
    System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, paramInt);
    return arrayOfString;
  }
  
  private static PathStrategy getPathStrategy(Context paramContext, String paramString) {
    HashMap<String, PathStrategy> hashMap = sCache;
    /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/util/HashMap<[ObjectType{java/lang/String}, InnerObjectType{ObjectType{android/support/v4/content/FileProvider}.Landroid/support/v4/content/FileProvider$PathStrategy;}]>}, name=null} */
    try {
      PathStrategy pathStrategy1 = sCache.get(paramString);
      PathStrategy pathStrategy2 = pathStrategy1;
      if (pathStrategy1 == null) {
        try {
          pathStrategy2 = parsePathStrategy(paramContext, paramString);
          sCache.put(paramString, pathStrategy2);
          /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/util/HashMap<[ObjectType{java/lang/String}, InnerObjectType{ObjectType{android/support/v4/content/FileProvider}.Landroid/support/v4/content/FileProvider$PathStrategy;}]>}, name=null} */
          return pathStrategy2;
        } catch (IOException iOException) {
          IllegalArgumentException illegalArgumentException = new IllegalArgumentException();
          this("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", iOException);
          throw illegalArgumentException;
        } catch (XmlPullParserException xmlPullParserException) {
          IllegalArgumentException illegalArgumentException = new IllegalArgumentException();
          this("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", (Throwable)xmlPullParserException);
          throw illegalArgumentException;
        } finally {}
      } else {
        /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/util/HashMap<[ObjectType{java/lang/String}, InnerObjectType{ObjectType{android/support/v4/content/FileProvider}.Landroid/support/v4/content/FileProvider$PathStrategy;}]>}, name=null} */
        return pathStrategy2;
      } 
    } finally {}
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/util/HashMap<[ObjectType{java/lang/String}, InnerObjectType{ObjectType{android/support/v4/content/FileProvider}.Landroid/support/v4/content/FileProvider$PathStrategy;}]>}, name=null} */
    throw paramContext;
  }
  
  public static Uri getUriForFile(Context paramContext, String paramString, File paramFile) {
    return getPathStrategy(paramContext, paramString).getUriForFile(paramFile);
  }
  
  private static int modeToMode(String paramString) {
    int i;
    if ("r".equals(paramString)) {
      i = 268435456;
    } else {
      if ("w".equals(paramString) || "wt".equals(paramString))
        return 738197504; 
      if ("wa".equals(paramString)) {
        i = 704643072;
      } else if ("rw".equals(paramString)) {
        i = 939524096;
      } else if ("rwt".equals(paramString)) {
        i = 1006632960;
      } else {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid mode: ");
        stringBuilder.append(paramString);
        throw new IllegalArgumentException(stringBuilder.toString());
      } 
    } 
    return i;
  }
  
  private static PathStrategy parsePathStrategy(Context paramContext, String paramString) throws IOException, XmlPullParserException {
    SimplePathStrategy simplePathStrategy = new SimplePathStrategy(paramString);
    ProviderInfo providerInfo = paramContext.getPackageManager().resolveContentProvider(paramString, 128);
    XmlResourceParser xmlResourceParser = providerInfo.loadXmlMetaData(paramContext.getPackageManager(), "android.support.FILE_PROVIDER_PATHS");
    if (xmlResourceParser != null)
      while (true) {
        int i = xmlResourceParser.next();
        if (i != 1) {
          if (i == 2) {
            File file;
            String str1 = xmlResourceParser.getName();
            String str2 = xmlResourceParser.getAttributeValue(null, "name");
            String str3 = xmlResourceParser.getAttributeValue(null, "path");
            ProviderInfo providerInfo1 = null;
            File[] arrayOfFile = null;
            providerInfo = null;
            if ("root-path".equals(str1)) {
              file = DEVICE_ROOT;
            } else if ("files-path".equals(str1)) {
              file = paramContext.getFilesDir();
            } else if ("cache-path".equals(str1)) {
              file = paramContext.getCacheDir();
            } else if ("external-path".equals(str1)) {
              file = Environment.getExternalStorageDirectory();
            } else if ("external-files-path".equals(str1)) {
              arrayOfFile = ContextCompat.getExternalFilesDirs(paramContext, null);
              if (arrayOfFile.length > 0)
                file = arrayOfFile[0]; 
            } else if ("external-cache-path".equals(str1)) {
              arrayOfFile = ContextCompat.getExternalCacheDirs(paramContext);
              providerInfo = providerInfo1;
              if (arrayOfFile.length > 0)
                file = arrayOfFile[0]; 
            } else {
              providerInfo = providerInfo1;
              if (Build.VERSION.SDK_INT >= 21) {
                File[] arrayOfFile1 = arrayOfFile;
                if ("external-media-path".equals(str1)) {
                  File[] arrayOfFile2 = paramContext.getExternalMediaDirs();
                  arrayOfFile1 = arrayOfFile;
                  if (arrayOfFile2.length > 0)
                    file = arrayOfFile2[0]; 
                } 
              } 
            } 
            if (file != null)
              simplePathStrategy.addRoot(str2, buildPath(file, new String[] { str3 })); 
          } 
          continue;
        } 
        return simplePathStrategy;
      }  
    throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
  }
  
  public void attachInfo(Context paramContext, ProviderInfo paramProviderInfo) {
    super.attachInfo(paramContext, paramProviderInfo);
    if (!paramProviderInfo.exported) {
      if (paramProviderInfo.grantUriPermissions) {
        this.mStrategy = getPathStrategy(paramContext, paramProviderInfo.authority);
        return;
      } 
      throw new SecurityException("Provider must grant uri permissions");
    } 
    throw new SecurityException("Provider must not be exported");
  }
  
  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString) {
    return this.mStrategy.getFileForUri(paramUri).delete();
  }
  
  public String getType(Uri paramUri) {
    File file = this.mStrategy.getFileForUri(paramUri);
    int i = file.getName().lastIndexOf('.');
    if (i >= 0) {
      String str = file.getName().substring(i + 1);
      str = MimeTypeMap.getSingleton().getMimeTypeFromExtension(str);
      if (str != null)
        return str; 
    } 
    return "application/octet-stream";
  }
  
  public Uri insert(Uri paramUri, ContentValues paramContentValues) {
    throw new UnsupportedOperationException("No external inserts");
  }
  
  public boolean onCreate() {
    return true;
  }
  
  public ParcelFileDescriptor openFile(Uri paramUri, String paramString) throws FileNotFoundException {
    return ParcelFileDescriptor.open(this.mStrategy.getFileForUri(paramUri), modeToMode(paramString));
  }
  
  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2) {
    File file = this.mStrategy.getFileForUri(paramUri);
    String[] arrayOfString = paramArrayOfString1;
    if (paramArrayOfString1 == null)
      arrayOfString = COLUMNS; 
    paramArrayOfString2 = new String[arrayOfString.length];
    Object[] arrayOfObject = new Object[arrayOfString.length];
    int i = 0;
    int j = arrayOfString.length;
    byte b = 0;
    while (b < j) {
      int k;
      paramString2 = arrayOfString[b];
      if ("_display_name".equals(paramString2)) {
        paramArrayOfString2[i] = "_display_name";
        arrayOfObject[i] = file.getName();
        k = i + 1;
      } else {
        k = i;
        if ("_size".equals(paramString2)) {
          paramArrayOfString2[i] = "_size";
          arrayOfObject[i] = Long.valueOf(file.length());
          k = i + 1;
        } 
      } 
      b++;
      i = k;
    } 
    arrayOfString = copyOf(paramArrayOfString2, i);
    arrayOfObject = copyOf(arrayOfObject, i);
    MatrixCursor matrixCursor = new MatrixCursor(arrayOfString, 1);
    matrixCursor.addRow(arrayOfObject);
    return (Cursor)matrixCursor;
  }
  
  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString) {
    throw new UnsupportedOperationException("No external updates");
  }
  
  static interface PathStrategy {
    File getFileForUri(Uri param1Uri);
    
    Uri getUriForFile(File param1File);
  }
  
  static class SimplePathStrategy implements PathStrategy {
    private final String mAuthority;
    
    private final HashMap<String, File> mRoots = new HashMap<>();
    
    SimplePathStrategy(String param1String) {
      this.mAuthority = param1String;
    }
    
    void addRoot(String param1String, File param1File) {
      if (!TextUtils.isEmpty(param1String))
        try {
          File file = param1File.getCanonicalFile();
          this.mRoots.put(param1String, file);
          return;
        } catch (IOException iOException) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Failed to resolve canonical path for ");
          stringBuilder.append(param1File);
          throw new IllegalArgumentException(stringBuilder.toString(), iOException);
        }  
      throw new IllegalArgumentException("Name must not be empty");
    }
    
    public File getFileForUri(Uri param1Uri) {
      File file1;
      String str1 = param1Uri.getEncodedPath();
      int i = str1.indexOf('/', 1);
      String str2 = Uri.decode(str1.substring(1, i));
      str1 = Uri.decode(str1.substring(i + 1));
      File file2 = this.mRoots.get(str2);
      if (file2 != null) {
        file1 = new File(file2, str1);
        try {
          File file = file1.getCanonicalFile();
          if (file.getPath().startsWith(file2.getPath()))
            return file; 
          throw new SecurityException("Resolved path jumped beyond configured root");
        } catch (IOException iOException) {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("Failed to resolve canonical path for ");
          stringBuilder1.append(file1);
          throw new IllegalArgumentException(stringBuilder1.toString());
        } 
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unable to find configured root for ");
      stringBuilder.append(file1);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public Uri getUriForFile(File param1File) {
      // Byte code:
      //   0: aload_1
      //   1: invokevirtual getCanonicalPath : ()Ljava/lang/String;
      //   4: astore_2
      //   5: aconst_null
      //   6: astore_1
      //   7: aload_0
      //   8: getfield mRoots : Ljava/util/HashMap;
      //   11: invokevirtual entrySet : ()Ljava/util/Set;
      //   14: invokeinterface iterator : ()Ljava/util/Iterator;
      //   19: astore_3
      //   20: aload_3
      //   21: invokeinterface hasNext : ()Z
      //   26: ifeq -> 107
      //   29: aload_3
      //   30: invokeinterface next : ()Ljava/lang/Object;
      //   35: checkcast java/util/Map$Entry
      //   38: astore #4
      //   40: aload #4
      //   42: invokeinterface getValue : ()Ljava/lang/Object;
      //   47: checkcast java/io/File
      //   50: invokevirtual getPath : ()Ljava/lang/String;
      //   53: astore #5
      //   55: aload_1
      //   56: astore #6
      //   58: aload_2
      //   59: aload #5
      //   61: invokevirtual startsWith : (Ljava/lang/String;)Z
      //   64: ifeq -> 101
      //   67: aload_1
      //   68: ifnull -> 97
      //   71: aload_1
      //   72: astore #6
      //   74: aload #5
      //   76: invokevirtual length : ()I
      //   79: aload_1
      //   80: invokeinterface getValue : ()Ljava/lang/Object;
      //   85: checkcast java/io/File
      //   88: invokevirtual getPath : ()Ljava/lang/String;
      //   91: invokevirtual length : ()I
      //   94: if_icmple -> 101
      //   97: aload #4
      //   99: astore #6
      //   101: aload #6
      //   103: astore_1
      //   104: goto -> 20
      //   107: aload_1
      //   108: ifnull -> 243
      //   111: aload_1
      //   112: invokeinterface getValue : ()Ljava/lang/Object;
      //   117: checkcast java/io/File
      //   120: invokevirtual getPath : ()Ljava/lang/String;
      //   123: astore #6
      //   125: aload #6
      //   127: ldc '/'
      //   129: invokevirtual endsWith : (Ljava/lang/String;)Z
      //   132: ifeq -> 149
      //   135: aload_2
      //   136: aload #6
      //   138: invokevirtual length : ()I
      //   141: invokevirtual substring : (I)Ljava/lang/String;
      //   144: astore #6
      //   146: goto -> 162
      //   149: aload_2
      //   150: aload #6
      //   152: invokevirtual length : ()I
      //   155: iconst_1
      //   156: iadd
      //   157: invokevirtual substring : (I)Ljava/lang/String;
      //   160: astore #6
      //   162: new java/lang/StringBuilder
      //   165: dup
      //   166: invokespecial <init> : ()V
      //   169: astore #4
      //   171: aload #4
      //   173: aload_1
      //   174: invokeinterface getKey : ()Ljava/lang/Object;
      //   179: checkcast java/lang/String
      //   182: invokestatic encode : (Ljava/lang/String;)Ljava/lang/String;
      //   185: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   188: pop
      //   189: aload #4
      //   191: bipush #47
      //   193: invokevirtual append : (C)Ljava/lang/StringBuilder;
      //   196: pop
      //   197: aload #4
      //   199: aload #6
      //   201: ldc '/'
      //   203: invokestatic encode : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
      //   206: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   209: pop
      //   210: aload #4
      //   212: invokevirtual toString : ()Ljava/lang/String;
      //   215: astore_1
      //   216: new android/net/Uri$Builder
      //   219: dup
      //   220: invokespecial <init> : ()V
      //   223: ldc 'content'
      //   225: invokevirtual scheme : (Ljava/lang/String;)Landroid/net/Uri$Builder;
      //   228: aload_0
      //   229: getfield mAuthority : Ljava/lang/String;
      //   232: invokevirtual authority : (Ljava/lang/String;)Landroid/net/Uri$Builder;
      //   235: aload_1
      //   236: invokevirtual encodedPath : (Ljava/lang/String;)Landroid/net/Uri$Builder;
      //   239: invokevirtual build : ()Landroid/net/Uri;
      //   242: areturn
      //   243: new java/lang/StringBuilder
      //   246: dup
      //   247: invokespecial <init> : ()V
      //   250: astore_1
      //   251: aload_1
      //   252: ldc 'Failed to find configured root that contains '
      //   254: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   257: pop
      //   258: aload_1
      //   259: aload_2
      //   260: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   263: pop
      //   264: new java/lang/IllegalArgumentException
      //   267: dup
      //   268: aload_1
      //   269: invokevirtual toString : ()Ljava/lang/String;
      //   272: invokespecial <init> : (Ljava/lang/String;)V
      //   275: athrow
      //   276: astore #6
      //   278: new java/lang/StringBuilder
      //   281: dup
      //   282: invokespecial <init> : ()V
      //   285: astore #6
      //   287: aload #6
      //   289: ldc 'Failed to resolve canonical path for '
      //   291: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   294: pop
      //   295: aload #6
      //   297: aload_1
      //   298: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   301: pop
      //   302: new java/lang/IllegalArgumentException
      //   305: dup
      //   306: aload #6
      //   308: invokevirtual toString : ()Ljava/lang/String;
      //   311: invokespecial <init> : (Ljava/lang/String;)V
      //   314: athrow
      // Exception table:
      //   from	to	target	type
      //   0	5	276	java/io/IOException
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v4/content/FileProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */