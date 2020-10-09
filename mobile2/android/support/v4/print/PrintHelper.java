package android.support.v4.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class PrintHelper {
  public static final int COLOR_MODE_COLOR = 2;
  
  public static final int COLOR_MODE_MONOCHROME = 1;
  
  static final boolean IS_MIN_MARGINS_HANDLING_CORRECT;
  
  private static final String LOG_TAG = "PrintHelper";
  
  private static final int MAX_PRINT_SIZE = 3500;
  
  public static final int ORIENTATION_LANDSCAPE = 1;
  
  public static final int ORIENTATION_PORTRAIT = 2;
  
  static final boolean PRINT_ACTIVITY_RESPECTS_ORIENTATION;
  
  public static final int SCALE_MODE_FILL = 2;
  
  public static final int SCALE_MODE_FIT = 1;
  
  int mColorMode = 2;
  
  final Context mContext;
  
  BitmapFactory.Options mDecodeOptions = null;
  
  final Object mLock = new Object();
  
  int mOrientation = 1;
  
  int mScaleMode = 2;
  
  static {
    int i = Build.VERSION.SDK_INT;
    boolean bool1 = false;
    if (i < 20 || Build.VERSION.SDK_INT > 23) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    PRINT_ACTIVITY_RESPECTS_ORIENTATION = bool2;
    boolean bool2 = bool1;
    if (Build.VERSION.SDK_INT != 23)
      bool2 = true; 
    IS_MIN_MARGINS_HANDLING_CORRECT = bool2;
  }
  
  public PrintHelper(Context paramContext) {
    this.mContext = paramContext;
  }
  
  static Bitmap convertBitmapForColorMode(Bitmap paramBitmap, int paramInt) {
    if (paramInt != 1)
      return paramBitmap; 
    Bitmap bitmap = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    Paint paint = new Paint();
    ColorMatrix colorMatrix = new ColorMatrix();
    colorMatrix.setSaturation(0.0F);
    paint.setColorFilter((ColorFilter)new ColorMatrixColorFilter(colorMatrix));
    canvas.drawBitmap(paramBitmap, 0.0F, 0.0F, paint);
    canvas.setBitmap(null);
    return bitmap;
  }
  
  private static PrintAttributes.Builder copyAttributes(PrintAttributes paramPrintAttributes) {
    PrintAttributes.Builder builder = (new PrintAttributes.Builder()).setMediaSize(paramPrintAttributes.getMediaSize()).setResolution(paramPrintAttributes.getResolution()).setMinMargins(paramPrintAttributes.getMinMargins());
    if (paramPrintAttributes.getColorMode() != 0)
      builder.setColorMode(paramPrintAttributes.getColorMode()); 
    if (Build.VERSION.SDK_INT >= 23 && paramPrintAttributes.getDuplexMode() != 0)
      builder.setDuplexMode(paramPrintAttributes.getDuplexMode()); 
    return builder;
  }
  
  static Matrix getMatrix(int paramInt1, int paramInt2, RectF paramRectF, int paramInt3) {
    Matrix matrix = new Matrix();
    float f = paramRectF.width() / paramInt1;
    if (paramInt3 == 2) {
      f = Math.max(f, paramRectF.height() / paramInt2);
    } else {
      f = Math.min(f, paramRectF.height() / paramInt2);
    } 
    matrix.postScale(f, f);
    matrix.postTranslate((paramRectF.width() - paramInt1 * f) / 2.0F, (paramRectF.height() - paramInt2 * f) / 2.0F);
    return matrix;
  }
  
  static boolean isPortrait(Bitmap paramBitmap) {
    boolean bool;
    if (paramBitmap.getWidth() <= paramBitmap.getHeight()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private Bitmap loadBitmap(Uri paramUri, BitmapFactory.Options paramOptions) throws FileNotFoundException {
    if (paramUri != null) {
      Context context = this.mContext;
      if (context != null) {
        InputStream inputStream = null;
        try {
          InputStream inputStream1 = context.getContentResolver().openInputStream(paramUri);
          inputStream = inputStream1;
          return BitmapFactory.decodeStream(inputStream1, null, paramOptions);
        } finally {
          if (inputStream != null)
            try {
              inputStream.close();
            } catch (IOException iOException) {
              Log.w("PrintHelper", "close fail ", iOException);
            }  
        } 
      } 
    } 
    throw new IllegalArgumentException("bad argument to loadBitmap");
  }
  
  public static boolean systemSupportsPrint() {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 19) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public int getColorMode() {
    return this.mColorMode;
  }
  
  public int getOrientation() {
    return (Build.VERSION.SDK_INT >= 19 && this.mOrientation == 0) ? 1 : this.mOrientation;
  }
  
  public int getScaleMode() {
    return this.mScaleMode;
  }
  
  Bitmap loadConstrainedBitmap(Uri paramUri) throws FileNotFoundException {
    if (paramUri != null && this.mContext != null) {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      loadBitmap(paramUri, options);
      int i = options.outWidth;
      int j = options.outHeight;
      if (i <= 0 || j <= 0)
        return null; 
      int k = Math.max(i, j);
      int m;
      for (m = 1; k > 3500; m <<= 1)
        k >>>= 1; 
      if (m <= 0 || Math.min(i, j) / m <= 0)
        return null; 
      Object object = this.mLock;
      /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
      try {
        BitmapFactory.Options options1 = new BitmapFactory.Options();
        this();
        this.mDecodeOptions = options1;
        options1.inMutable = true;
        this.mDecodeOptions.inSampleSize = m;
        options1 = this.mDecodeOptions;
        try {
          /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
          try {
            object = loadBitmap(paramUri, options1);
          } finally {
            object = null;
          } 
        } finally {}
      } finally {}
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
      throw paramUri;
    } 
    throw new IllegalArgumentException("bad argument to getScaledBitmap");
  }
  
  public void printBitmap(String paramString, Bitmap paramBitmap) {
    printBitmap(paramString, paramBitmap, (OnPrintFinishCallback)null);
  }
  
  public void printBitmap(String paramString, Bitmap paramBitmap, OnPrintFinishCallback paramOnPrintFinishCallback) {
    PrintAttributes.MediaSize mediaSize;
    if (Build.VERSION.SDK_INT < 19 || paramBitmap == null)
      return; 
    PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
    if (isPortrait(paramBitmap)) {
      mediaSize = PrintAttributes.MediaSize.UNKNOWN_PORTRAIT;
    } else {
      mediaSize = PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE;
    } 
    PrintAttributes printAttributes = (new PrintAttributes.Builder()).setMediaSize(mediaSize).setColorMode(this.mColorMode).build();
    printManager.print(paramString, new PrintBitmapAdapter(paramString, this.mScaleMode, paramBitmap, paramOnPrintFinishCallback), printAttributes);
  }
  
  public void printBitmap(String paramString, Uri paramUri) throws FileNotFoundException {
    printBitmap(paramString, paramUri, (OnPrintFinishCallback)null);
  }
  
  public void printBitmap(String paramString, Uri paramUri, OnPrintFinishCallback paramOnPrintFinishCallback) throws FileNotFoundException {
    if (Build.VERSION.SDK_INT < 19)
      return; 
    PrintUriAdapter printUriAdapter = new PrintUriAdapter(paramString, paramUri, paramOnPrintFinishCallback, this.mScaleMode);
    PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
    PrintAttributes.Builder builder = new PrintAttributes.Builder();
    builder.setColorMode(this.mColorMode);
    int i = this.mOrientation;
    if (i == 1 || i == 0) {
      builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE);
    } else if (i == 2) {
      builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_PORTRAIT);
    } 
    printManager.print(paramString, printUriAdapter, builder.build());
  }
  
  public void setColorMode(int paramInt) {
    this.mColorMode = paramInt;
  }
  
  public void setOrientation(int paramInt) {
    this.mOrientation = paramInt;
  }
  
  public void setScaleMode(int paramInt) {
    this.mScaleMode = paramInt;
  }
  
  void writeBitmap(final PrintAttributes attributes, final int fittingMode, final Bitmap bitmap, final ParcelFileDescriptor fileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
    final PrintAttributes pdfAttributes;
    if (IS_MIN_MARGINS_HANDLING_CORRECT) {
      printAttributes = attributes;
    } else {
      printAttributes = copyAttributes(attributes).setMinMargins(new PrintAttributes.Margins(0, 0, 0, 0)).build();
    } 
    (new AsyncTask<Void, Void, Throwable>() {
        protected Throwable doInBackground(Void... param1VarArgs) {
          try {
            if (cancellationSignal.isCanceled())
              return null; 
            PrintedPdfDocument printedPdfDocument = new PrintedPdfDocument();
            this(PrintHelper.this.mContext, pdfAttributes);
            Bitmap bitmap = PrintHelper.convertBitmapForColorMode(bitmap, pdfAttributes.getColorMode());
            boolean bool = cancellationSignal.isCanceled();
            if (bool)
              return null; 
            try {
              RectF rectF;
              PdfDocument.Page page = printedPdfDocument.startPage(1);
              if (PrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT) {
                rectF = new RectF();
                this(page.getInfo().getContentRect());
              } else {
                PrintedPdfDocument printedPdfDocument1 = new PrintedPdfDocument();
                this(PrintHelper.this.mContext, attributes);
                PdfDocument.Page page1 = printedPdfDocument1.startPage(1);
                rectF = new RectF();
                this(page1.getInfo().getContentRect());
                printedPdfDocument1.finishPage(page1);
                printedPdfDocument1.close();
              } 
              Matrix matrix = PrintHelper.getMatrix(bitmap.getWidth(), bitmap.getHeight(), rectF, fittingMode);
              if (!PrintHelper.IS_MIN_MARGINS_HANDLING_CORRECT) {
                matrix.postTranslate(rectF.left, rectF.top);
                page.getCanvas().clipRect(rectF);
              } 
              page.getCanvas().drawBitmap(bitmap, matrix, null);
              printedPdfDocument.finishPage(page);
              bool = cancellationSignal.isCanceled();
              if (bool)
                return null; 
              FileOutputStream fileOutputStream = new FileOutputStream();
              this(fileDescriptor.getFileDescriptor());
              printedPdfDocument.writeTo(fileOutputStream);
              return null;
            } finally {
              printedPdfDocument.close();
              ParcelFileDescriptor parcelFileDescriptor = fileDescriptor;
              if (parcelFileDescriptor != null)
                try {
                  fileDescriptor.close();
                } catch (IOException iOException) {} 
              if (bitmap != bitmap)
                bitmap.recycle(); 
            } 
          } finally {}
        }
        
        protected void onPostExecute(Throwable param1Throwable) {
          if (cancellationSignal.isCanceled()) {
            writeResultCallback.onWriteCancelled();
          } else if (param1Throwable == null) {
            writeResultCallback.onWriteFinished(new PageRange[] { PageRange.ALL_PAGES });
          } else {
            Log.e("PrintHelper", "Error writing printed content", param1Throwable);
            writeResultCallback.onWriteFailed(null);
          } 
        }
      }).execute((Object[])new Void[0]);
  }
  
  public static interface OnPrintFinishCallback {
    void onFinish();
  }
  
  private class PrintBitmapAdapter extends PrintDocumentAdapter {
    private PrintAttributes mAttributes;
    
    private final Bitmap mBitmap;
    
    private final PrintHelper.OnPrintFinishCallback mCallback;
    
    private final int mFittingMode;
    
    private final String mJobName;
    
    PrintBitmapAdapter(String param1String, int param1Int, Bitmap param1Bitmap, PrintHelper.OnPrintFinishCallback param1OnPrintFinishCallback) {
      this.mJobName = param1String;
      this.mFittingMode = param1Int;
      this.mBitmap = param1Bitmap;
      this.mCallback = param1OnPrintFinishCallback;
    }
    
    public void onFinish() {
      PrintHelper.OnPrintFinishCallback onPrintFinishCallback = this.mCallback;
      if (onPrintFinishCallback != null)
        onPrintFinishCallback.onFinish(); 
    }
    
    public void onLayout(PrintAttributes param1PrintAttributes1, PrintAttributes param1PrintAttributes2, CancellationSignal param1CancellationSignal, PrintDocumentAdapter.LayoutResultCallback param1LayoutResultCallback, Bundle param1Bundle) {
      this.mAttributes = param1PrintAttributes2;
      param1LayoutResultCallback.onLayoutFinished((new PrintDocumentInfo.Builder(this.mJobName)).setContentType(1).setPageCount(1).build(), true ^ param1PrintAttributes2.equals(param1PrintAttributes1));
    }
    
    public void onWrite(PageRange[] param1ArrayOfPageRange, ParcelFileDescriptor param1ParcelFileDescriptor, CancellationSignal param1CancellationSignal, PrintDocumentAdapter.WriteResultCallback param1WriteResultCallback) {
      PrintHelper.this.writeBitmap(this.mAttributes, this.mFittingMode, this.mBitmap, param1ParcelFileDescriptor, param1CancellationSignal, param1WriteResultCallback);
    }
  }
  
  private class PrintUriAdapter extends PrintDocumentAdapter {
    PrintAttributes mAttributes;
    
    Bitmap mBitmap;
    
    final PrintHelper.OnPrintFinishCallback mCallback;
    
    final int mFittingMode;
    
    final Uri mImageFile;
    
    final String mJobName;
    
    AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;
    
    PrintUriAdapter(String param1String, Uri param1Uri, PrintHelper.OnPrintFinishCallback param1OnPrintFinishCallback, int param1Int) {
      this.mJobName = param1String;
      this.mImageFile = param1Uri;
      this.mCallback = param1OnPrintFinishCallback;
      this.mFittingMode = param1Int;
      this.mBitmap = null;
    }
    
    void cancelLoad() {
      synchronized (PrintHelper.this.mLock) {
        if (PrintHelper.this.mDecodeOptions != null) {
          if (Build.VERSION.SDK_INT < 24)
            PrintHelper.this.mDecodeOptions.requestCancelDecode(); 
          PrintHelper.this.mDecodeOptions = null;
        } 
        return;
      } 
    }
    
    public void onFinish() {
      super.onFinish();
      cancelLoad();
      AsyncTask<Uri, Boolean, Bitmap> asyncTask = this.mLoadBitmap;
      if (asyncTask != null)
        asyncTask.cancel(true); 
      PrintHelper.OnPrintFinishCallback onPrintFinishCallback = this.mCallback;
      if (onPrintFinishCallback != null)
        onPrintFinishCallback.onFinish(); 
      Bitmap bitmap = this.mBitmap;
      if (bitmap != null) {
        bitmap.recycle();
        this.mBitmap = null;
      } 
    }
    
    public void onLayout(PrintAttributes param1PrintAttributes1, PrintAttributes param1PrintAttributes2, CancellationSignal param1CancellationSignal, PrintDocumentAdapter.LayoutResultCallback param1LayoutResultCallback, Bundle param1Bundle) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: aload_2
      //   4: putfield mAttributes : Landroid/print/PrintAttributes;
      //   7: aload_0
      //   8: monitorexit
      //   9: aload_3
      //   10: invokevirtual isCanceled : ()Z
      //   13: ifeq -> 22
      //   16: aload #4
      //   18: invokevirtual onLayoutCancelled : ()V
      //   21: return
      //   22: aload_0
      //   23: getfield mBitmap : Landroid/graphics/Bitmap;
      //   26: ifnull -> 64
      //   29: aload #4
      //   31: new android/print/PrintDocumentInfo$Builder
      //   34: dup
      //   35: aload_0
      //   36: getfield mJobName : Ljava/lang/String;
      //   39: invokespecial <init> : (Ljava/lang/String;)V
      //   42: iconst_1
      //   43: invokevirtual setContentType : (I)Landroid/print/PrintDocumentInfo$Builder;
      //   46: iconst_1
      //   47: invokevirtual setPageCount : (I)Landroid/print/PrintDocumentInfo$Builder;
      //   50: invokevirtual build : ()Landroid/print/PrintDocumentInfo;
      //   53: iconst_1
      //   54: aload_2
      //   55: aload_1
      //   56: invokevirtual equals : (Ljava/lang/Object;)Z
      //   59: ixor
      //   60: invokevirtual onLayoutFinished : (Landroid/print/PrintDocumentInfo;Z)V
      //   63: return
      //   64: aload_0
      //   65: new android/support/v4/print/PrintHelper$PrintUriAdapter$1
      //   68: dup
      //   69: aload_0
      //   70: aload_3
      //   71: aload_2
      //   72: aload_1
      //   73: aload #4
      //   75: invokespecial <init> : (Landroid/support/v4/print/PrintHelper$PrintUriAdapter;Landroid/os/CancellationSignal;Landroid/print/PrintAttributes;Landroid/print/PrintAttributes;Landroid/print/PrintDocumentAdapter$LayoutResultCallback;)V
      //   78: iconst_0
      //   79: anewarray android/net/Uri
      //   82: invokevirtual execute : ([Ljava/lang/Object;)Landroid/os/AsyncTask;
      //   85: putfield mLoadBitmap : Landroid/os/AsyncTask;
      //   88: return
      //   89: astore_1
      //   90: aload_0
      //   91: monitorexit
      //   92: aload_1
      //   93: athrow
      // Exception table:
      //   from	to	target	type
      //   2	9	89	finally
      //   90	92	89	finally
    }
    
    public void onWrite(PageRange[] param1ArrayOfPageRange, ParcelFileDescriptor param1ParcelFileDescriptor, CancellationSignal param1CancellationSignal, PrintDocumentAdapter.WriteResultCallback param1WriteResultCallback) {
      PrintHelper.this.writeBitmap(this.mAttributes, this.mFittingMode, this.mBitmap, param1ParcelFileDescriptor, param1CancellationSignal, param1WriteResultCallback);
    }
  }
  
  class null extends AsyncTask<Uri, Boolean, Bitmap> {
    protected Bitmap doInBackground(Uri... param1VarArgs) {
      try {
        return PrintHelper.this.loadConstrainedBitmap(this.this$1.mImageFile);
      } catch (FileNotFoundException fileNotFoundException) {
        return null;
      } 
    }
    
    protected void onCancelled(Bitmap param1Bitmap) {
      layoutResultCallback.onLayoutCancelled();
      this.this$1.mLoadBitmap = null;
    }
    
    protected void onPostExecute(Bitmap param1Bitmap) {
      // Byte code:
      //   0: aload_0
      //   1: aload_1
      //   2: invokespecial onPostExecute : (Ljava/lang/Object;)V
      //   5: aload_1
      //   6: astore_2
      //   7: aload_1
      //   8: ifnull -> 110
      //   11: getstatic android/support/v4/print/PrintHelper.PRINT_ACTIVITY_RESPECTS_ORIENTATION : Z
      //   14: ifeq -> 32
      //   17: aload_1
      //   18: astore_2
      //   19: aload_0
      //   20: getfield this$1 : Landroid/support/v4/print/PrintHelper$PrintUriAdapter;
      //   23: getfield this$0 : Landroid/support/v4/print/PrintHelper;
      //   26: getfield mOrientation : I
      //   29: ifne -> 110
      //   32: aload_0
      //   33: monitorenter
      //   34: aload_0
      //   35: getfield this$1 : Landroid/support/v4/print/PrintHelper$PrintUriAdapter;
      //   38: getfield mAttributes : Landroid/print/PrintAttributes;
      //   41: invokevirtual getMediaSize : ()Landroid/print/PrintAttributes$MediaSize;
      //   44: astore_3
      //   45: aload_0
      //   46: monitorexit
      //   47: aload_1
      //   48: astore_2
      //   49: aload_3
      //   50: ifnull -> 110
      //   53: aload_1
      //   54: astore_2
      //   55: aload_3
      //   56: invokevirtual isPortrait : ()Z
      //   59: aload_1
      //   60: invokestatic isPortrait : (Landroid/graphics/Bitmap;)Z
      //   63: if_icmpeq -> 110
      //   66: new android/graphics/Matrix
      //   69: dup
      //   70: invokespecial <init> : ()V
      //   73: astore_2
      //   74: aload_2
      //   75: ldc 90.0
      //   77: invokevirtual postRotate : (F)Z
      //   80: pop
      //   81: aload_1
      //   82: iconst_0
      //   83: iconst_0
      //   84: aload_1
      //   85: invokevirtual getWidth : ()I
      //   88: aload_1
      //   89: invokevirtual getHeight : ()I
      //   92: aload_2
      //   93: iconst_1
      //   94: invokestatic createBitmap : (Landroid/graphics/Bitmap;IIIILandroid/graphics/Matrix;Z)Landroid/graphics/Bitmap;
      //   97: astore_2
      //   98: goto -> 110
      //   101: astore_1
      //   102: aload_0
      //   103: monitorexit
      //   104: aload_1
      //   105: athrow
      //   106: astore_1
      //   107: goto -> 102
      //   110: aload_0
      //   111: getfield this$1 : Landroid/support/v4/print/PrintHelper$PrintUriAdapter;
      //   114: aload_2
      //   115: putfield mBitmap : Landroid/graphics/Bitmap;
      //   118: aload_2
      //   119: ifnull -> 176
      //   122: new android/print/PrintDocumentInfo$Builder
      //   125: dup
      //   126: aload_0
      //   127: getfield this$1 : Landroid/support/v4/print/PrintHelper$PrintUriAdapter;
      //   130: getfield mJobName : Ljava/lang/String;
      //   133: invokespecial <init> : (Ljava/lang/String;)V
      //   136: iconst_1
      //   137: invokevirtual setContentType : (I)Landroid/print/PrintDocumentInfo$Builder;
      //   140: iconst_1
      //   141: invokevirtual setPageCount : (I)Landroid/print/PrintDocumentInfo$Builder;
      //   144: invokevirtual build : ()Landroid/print/PrintDocumentInfo;
      //   147: astore_1
      //   148: aload_0
      //   149: getfield val$newPrintAttributes : Landroid/print/PrintAttributes;
      //   152: aload_0
      //   153: getfield val$oldPrintAttributes : Landroid/print/PrintAttributes;
      //   156: invokevirtual equals : (Ljava/lang/Object;)Z
      //   159: istore #4
      //   161: aload_0
      //   162: getfield val$layoutResultCallback : Landroid/print/PrintDocumentAdapter$LayoutResultCallback;
      //   165: aload_1
      //   166: iconst_1
      //   167: iload #4
      //   169: ixor
      //   170: invokevirtual onLayoutFinished : (Landroid/print/PrintDocumentInfo;Z)V
      //   173: goto -> 184
      //   176: aload_0
      //   177: getfield val$layoutResultCallback : Landroid/print/PrintDocumentAdapter$LayoutResultCallback;
      //   180: aconst_null
      //   181: invokevirtual onLayoutFailed : (Ljava/lang/CharSequence;)V
      //   184: aload_0
      //   185: getfield this$1 : Landroid/support/v4/print/PrintHelper$PrintUriAdapter;
      //   188: aconst_null
      //   189: putfield mLoadBitmap : Landroid/os/AsyncTask;
      //   192: return
      // Exception table:
      //   from	to	target	type
      //   34	45	101	finally
      //   45	47	106	finally
      //   102	104	106	finally
    }
    
    protected void onPreExecute() {
      cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            public void onCancel() {
              this.this$2.this$1.cancelLoad();
              PrintHelper.PrintUriAdapter.null.this.cancel(false);
            }
          });
    }
  }
  
  class null implements CancellationSignal.OnCancelListener {
    public void onCancel() {
      this.this$2.this$1.cancelLoad();
      this.this$2.cancel(false);
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v4/print/PrintHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */