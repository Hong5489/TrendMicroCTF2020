package android.support.v4.graphics;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.StrictMode;
import android.util.Log;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TypefaceCompatUtil {
  private static final String CACHE_FILE_PREFIX = ".font";
  
  private static final String TAG = "TypefaceCompatUtil";
  
  public static void closeQuietly(Closeable paramCloseable) {
    if (paramCloseable != null)
      try {
        paramCloseable.close();
      } catch (IOException iOException) {} 
  }
  
  public static ByteBuffer copyToDirectBuffer(Context paramContext, Resources paramResources, int paramInt) {
    File file = getTempFile(paramContext);
    paramContext = null;
    if (file == null)
      return null; 
    try {
      ByteBuffer byteBuffer;
      boolean bool = copyToFile(file, paramResources, paramInt);
      if (bool)
        byteBuffer = mmap(file); 
      return byteBuffer;
    } finally {
      file.delete();
    } 
  }
  
  public static boolean copyToFile(File paramFile, Resources paramResources, int paramInt) {
    InputStream inputStream = null;
    try {
      InputStream inputStream1 = paramResources.openRawResource(paramInt);
      inputStream = inputStream1;
      return copyToFile(paramFile, inputStream1);
    } finally {
      closeQuietly(inputStream);
    } 
  }
  
  public static boolean copyToFile(File paramFile, InputStream paramInputStream) {
    FileOutputStream fileOutputStream1 = null;
    FileOutputStream fileOutputStream2 = null;
    StrictMode.ThreadPolicy threadPolicy = StrictMode.allowThreadDiskWrites();
    FileOutputStream fileOutputStream3 = fileOutputStream2;
    FileOutputStream fileOutputStream4 = fileOutputStream1;
    try {
      FileOutputStream fileOutputStream6 = new FileOutputStream();
      fileOutputStream3 = fileOutputStream2;
      fileOutputStream4 = fileOutputStream1;
      this(paramFile, false);
      FileOutputStream fileOutputStream5 = fileOutputStream6;
      fileOutputStream3 = fileOutputStream5;
      fileOutputStream4 = fileOutputStream5;
      byte[] arrayOfByte = new byte[1024];
      while (true) {
        fileOutputStream3 = fileOutputStream5;
        fileOutputStream4 = fileOutputStream5;
        int i = paramInputStream.read(arrayOfByte);
        if (i != -1) {
          fileOutputStream3 = fileOutputStream5;
          fileOutputStream4 = fileOutputStream5;
          fileOutputStream5.write(arrayOfByte, 0, i);
          continue;
        } 
        closeQuietly(fileOutputStream5);
        StrictMode.setThreadPolicy(threadPolicy);
        return true;
      } 
    } catch (IOException iOException) {
      fileOutputStream3 = fileOutputStream4;
      StringBuilder stringBuilder = new StringBuilder();
      fileOutputStream3 = fileOutputStream4;
      this();
      fileOutputStream3 = fileOutputStream4;
      stringBuilder.append("Error copying resource contents to temp file: ");
      fileOutputStream3 = fileOutputStream4;
      stringBuilder.append(iOException.getMessage());
      fileOutputStream3 = fileOutputStream4;
      Log.e("TypefaceCompatUtil", stringBuilder.toString());
      closeQuietly(fileOutputStream4);
      StrictMode.setThreadPolicy(threadPolicy);
      return false;
    } finally {}
    closeQuietly(fileOutputStream3);
    StrictMode.setThreadPolicy(threadPolicy);
    throw paramFile;
  }
  
  public static File getTempFile(Context paramContext) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(".font");
    stringBuilder.append(Process.myPid());
    stringBuilder.append("-");
    stringBuilder.append(Process.myTid());
    stringBuilder.append("-");
    String str = stringBuilder.toString();
    for (byte b = 0; b < 100; b++) {
      File file1 = paramContext.getCacheDir();
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str);
      stringBuilder1.append(b);
      File file2 = new File(file1, stringBuilder1.toString());
      try {
        boolean bool = file2.createNewFile();
        if (bool)
          return file2; 
      } catch (IOException iOException) {}
    } 
    return null;
  }
  
  public static ByteBuffer mmap(Context paramContext, CancellationSignal paramCancellationSignal, Uri paramUri) {
    ContentResolver contentResolver = paramContext.getContentResolver();
    try {
      ParcelFileDescriptor parcelFileDescriptor = contentResolver.openFileDescriptor(paramUri, "r", paramCancellationSignal);
      if (parcelFileDescriptor == null) {
        if (parcelFileDescriptor != null)
          parcelFileDescriptor.close(); 
        return null;
      } 
      try {
        FileInputStream fileInputStream = new FileInputStream();
        this(parcelFileDescriptor.getFileDescriptor());
      } finally {
        paramCancellationSignal = null;
      } 
    } catch (IOException iOException) {
      return null;
    } 
  }
  
  private static ByteBuffer mmap(File paramFile) {
    try {
      FileInputStream fileInputStream = new FileInputStream();
      this(paramFile);
      try {
        FileChannel fileChannel = fileInputStream.getChannel();
        long l = fileChannel.size();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, l);
      } finally {
        Exception exception = null;
      } 
    } catch (IOException iOException) {
      return null;
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v4/graphics/TypefaceCompatUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */