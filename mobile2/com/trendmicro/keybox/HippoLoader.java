package com.trendmicro.keybox;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.View;
import com.trendmicro.hippo.Context;
import com.trendmicro.hippo.ContextFactory;
import com.trendmicro.hippo.Function;
import com.trendmicro.hippo.ImporterTopLevel;
import com.trendmicro.hippo.JavaScriptException;
import com.trendmicro.hippo.Scriptable;
import com.trendmicro.hippo.ScriptableObject;
import java.io.InputStream;

public class HippoLoader {
  Context androidContextObject;
  
  String ciphertextKey;
  
  View imageView;
  
  String password;
  
  String path_ciphertext;
  
  String path_preload;
  
  boolean preloadOnly = false;
  
  Singleton singleton;
  
  View textView;
  
  View titleView;
  
  HippoLoader() {
    this.singleton = Singleton.getInstance();
    this.preloadOnly = true;
    this.path_preload = String.format("_preload/code.js", new Object[0]);
  }
  
  HippoLoader(int paramInt) {
    Singleton singleton = Singleton.getInstance();
    this.singleton = singleton;
    this.ciphertextKey = singleton.hintkey(paramInt);
    this.path_preload = String.format("_preload/code.js", new Object[0]);
    this.path_ciphertext = String.format("key_%d/ciphertext.js", new Object[] { Integer.valueOf(paramInt) });
  }
  
  HippoLoader(String paramString) {
    this.singleton = Singleton.getInstance();
    if (paramString.equals("main")) {
      this.ciphertextKey = this.singleton.hintkeymain;
      this.path_preload = String.format("_preload/code.js", new Object[0]);
      this.path_ciphertext = String.format("_main/ciphertext.js", new Object[0]);
    } else if (paramString.equals("flag")) {
      this.ciphertextKey = "PineappleHead";
      this.path_preload = String.format("flag/code.js", new Object[0]);
      this.path_ciphertext = String.format("flag/ciphertext.js", new Object[0]);
    } 
  }
  
  private void Log(String paramString) {}
  
  public boolean decrypt_flag() {
    this.singleton = Singleton.getInstance();
    AssetManager assetManager = this.androidContextObject.getAssets();
    Context context = ContextFactory.getGlobal().enterContext();
    context.setOptimizationLevel(-1);
    ImporterTopLevel importerTopLevel = new ImporterTopLevel(context);
    try {
      InputStream inputStream1 = assetManager.open(this.path_preload);
      byte[] arrayOfByte2 = new byte[inputStream1.available()];
      inputStream1.read(arrayOfByte2);
      inputStream1.close();
      String str = new String();
      this(arrayOfByte2);
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "androidContext", Context.javaToJS(this.androidContextObject, (Scriptable)importerTopLevel));
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "imageView", Context.javaToJS(this.imageView, (Scriptable)importerTopLevel));
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "textView", Context.javaToJS(this.textView, (Scriptable)importerTopLevel));
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "titleView", Context.javaToJS(this.titleView, (Scriptable)importerTopLevel));
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "code", Context.javaToJS(str, (Scriptable)importerTopLevel));
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "path_ciphertext", Context.javaToJS(this.path_ciphertext, (Scriptable)importerTopLevel));
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "key", Context.javaToJS(this.ciphertextKey, (Scriptable)importerTopLevel));
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "singleton", Context.javaToJS(this.singleton, (Scriptable)importerTopLevel));
      context.evaluateString((Scriptable)importerTopLevel, str, this.path_preload, 1, null);
      InputStream inputStream2 = assetManager.open(this.path_ciphertext);
      byte[] arrayOfByte1 = new byte[inputStream2.available()];
      inputStream2.read(arrayOfByte1);
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "flag_ciphertext", Context.javaToJS(arrayOfByte1, (Scriptable)importerTopLevel));
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "key0", Context.javaToJS(this.singleton.flagkey0, (Scriptable)importerTopLevel));
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "key1", Context.javaToJS(this.singleton.flagkey1, (Scriptable)importerTopLevel));
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "key2", Context.javaToJS(this.singleton.flagkey2, (Scriptable)importerTopLevel));
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "key3", Context.javaToJS(this.singleton.flagkey3, (Scriptable)importerTopLevel));
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "key4", Context.javaToJS(this.singleton.flagkey4, (Scriptable)importerTopLevel));
      Object[] arrayOfObject = new Object[1];
      Object object2 = importerTopLevel.get("process", (Scriptable)importerTopLevel);
      arrayOfObject = new Object[3];
      arrayOfObject[0] = this.ciphertextKey;
      arrayOfObject[1] = arrayOfByte1;
      arrayOfObject[2] = Boolean.valueOf(true);
      if (object2 instanceof Function) {
        Object object = ((Function)object2).call(context, (Scriptable)importerTopLevel, (Scriptable)importerTopLevel, arrayOfObject);
      } else {
        arrayOfByte1 = null;
      } 
      context.evaluateString((Scriptable)importerTopLevel, (String)arrayOfByte1, this.path_ciphertext, 1, null);
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "code", Context.javaToJS(arrayOfByte1, (Scriptable)importerTopLevel));
      Object object1 = importerTopLevel.get("decrypt_flag", (Scriptable)importerTopLevel);
      if (object1 instanceof Function) {
        Context.toString(((Function)object1).call(context, (Scriptable)importerTopLevel, (Scriptable)importerTopLevel, arrayOfObject));
        Context.exit();
        return true;
      } 
      Context.exit();
      return false;
    } catch (Exception exception) {
      if (exception.getClass() == JavaScriptException.class) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append("Javascript exception - ");
        stringBuilder.append(exception.getMessage());
        Log(stringBuilder.toString());
        Context.exit();
        return false;
      } 
      Log.d("TMCTF", String.format("Exception calling %s", new Object[] { "decrypt_flag()" }));
      exception.printStackTrace();
      Context.exit();
      return false;
    } finally {}
    Context.exit();
    throw assetManager;
  }
  
  public String decrypt_key(int paramInt) {
    Object object;
    Log.d("TMCTF", String.format("Calling decrypt_key(%d)", new Object[] { Integer.valueOf(paramInt) }));
    String str = String.format("key_%d/key_%d.enc", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(paramInt) });
    AssetManager assetManager = this.androidContextObject.getAssets();
    Context context = ContextFactory.getGlobal().enterContext();
    context.setOptimizationLevel(-1);
    ImporterTopLevel importerTopLevel = new ImporterTopLevel(context);
    try {
      InputStream inputStream2 = assetManager.open(this.path_preload);
      byte[] arrayOfByte2 = new byte[inputStream2.available()];
      inputStream2.read(arrayOfByte2);
      inputStream2.close();
      String str1 = new String();
      this(arrayOfByte2);
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "androidContext", Context.javaToJS(this.androidContextObject, (Scriptable)importerTopLevel));
      context.evaluateString((Scriptable)importerTopLevel, str1, this.path_preload, 1, null);
      arrayOfByte2 = null;
      InputStream inputStream1 = assetManager.open(str);
      byte[] arrayOfByte1 = new byte[inputStream1.available()];
      inputStream1.read(arrayOfByte1);
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "ciphertext_bytes", Context.javaToJS(arrayOfByte1, (Scriptable)importerTopLevel));
      Object object1 = importerTopLevel.get("process", (Scriptable)importerTopLevel);
      Singleton singleton = this.singleton;
      try {
        String str2 = singleton.keykey(paramInt);
        if (object1 instanceof Function)
          object = ((Function)object1).call(context, (Scriptable)importerTopLevel, (Scriptable)importerTopLevel, new Object[] { str2, arrayOfByte1 }); 
        object = object;
        Context.exit();
        return (String)object;
      } catch (Exception null) {
        if (object.getClass() == JavaScriptException.class) {
          StringBuilder stringBuilder = new StringBuilder();
          this();
          stringBuilder.append("Javascript exception - ");
          stringBuilder.append(object.getMessage());
          Log(stringBuilder.toString());
          Context.exit();
          return null;
        } 
        Log(String.format("Exception in decrypt_key()", new Object[0]));
        object.printStackTrace();
        Context.exit();
        return null;
      } finally {}
    } catch (Exception null) {
    
    } finally {}
    if (object.getClass() == JavaScriptException.class) {
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("Javascript exception - ");
      stringBuilder.append(object.getMessage());
      Log(stringBuilder.toString());
      Context.exit();
      return null;
    } 
    Log(String.format("Exception in decrypt_key()", new Object[0]));
    object.printStackTrace();
    Context.exit();
    return null;
  }
  
  public boolean entrypoint() {
    return execute("entrypoint", false);
  }
  
  public boolean execute(String paramString, boolean paramBoolean) {
    AssetManager assetManager = this.androidContextObject.getAssets();
    Context context = ContextFactory.getGlobal().enterContext();
    context.setOptimizationLevel(-1);
    ImporterTopLevel importerTopLevel = new ImporterTopLevel(context);
    try {
      InputStream inputStream1 = assetManager.open(this.path_preload);
      byte[] arrayOfByte2 = new byte[inputStream1.available()];
      inputStream1.read(arrayOfByte2);
      inputStream1.close();
      String str = new String();
      this(arrayOfByte2);
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "androidContext", Context.javaToJS(this.androidContextObject, (Scriptable)importerTopLevel));
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "imageView", Context.javaToJS(this.imageView, (Scriptable)importerTopLevel));
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "textView", Context.javaToJS(this.textView, (Scriptable)importerTopLevel));
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "titleView", Context.javaToJS(this.titleView, (Scriptable)importerTopLevel));
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "code", Context.javaToJS(str, (Scriptable)importerTopLevel));
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "path_ciphertext", Context.javaToJS(this.path_ciphertext, (Scriptable)importerTopLevel));
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "key", Context.javaToJS(this.ciphertextKey, (Scriptable)importerTopLevel));
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "singleton", Context.javaToJS(this.singleton, (Scriptable)importerTopLevel));
      context.evaluateString((Scriptable)importerTopLevel, str, this.path_preload, 1, null);
      InputStream inputStream2 = assetManager.open(this.path_ciphertext);
      byte[] arrayOfByte1 = new byte[inputStream2.available()];
      inputStream2.read(arrayOfByte1);
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "ciphertext_bytes", Context.javaToJS(arrayOfByte1, (Scriptable)importerTopLevel));
      Object[] arrayOfObject = new Object[1];
      Object object3 = importerTopLevel.get(paramString, (Scriptable)importerTopLevel);
      if (this.preloadOnly || (paramBoolean && object3 instanceof Function)) {
        ((Function)object3).call(context, (Scriptable)importerTopLevel, (Scriptable)importerTopLevel, arrayOfObject);
        Context.exit();
        return true;
      } 
      Object object2 = importerTopLevel.get("process", (Scriptable)importerTopLevel);
      object3 = new Object[2];
      object3[0] = this.ciphertextKey;
      object3[1] = arrayOfByte1;
      if (object2 instanceof Function) {
        Object object = ((Function)object2).call(context, (Scriptable)importerTopLevel, (Scriptable)importerTopLevel, (Object[])object3);
      } else {
        arrayOfByte1 = null;
      } 
      context.evaluateString((Scriptable)importerTopLevel, (String)arrayOfByte1, this.path_ciphertext, 1, null);
      ScriptableObject.putProperty((Scriptable)importerTopLevel, "code", Context.javaToJS(arrayOfByte1, (Scriptable)importerTopLevel));
      Object object1 = importerTopLevel.get(paramString, (Scriptable)importerTopLevel);
      if (object1 instanceof Function) {
        Context.toString(((Function)object1).call(context, (Scriptable)importerTopLevel, (Scriptable)importerTopLevel, (Object[])object3));
        Context.exit();
        return true;
      } 
      Context.exit();
      return false;
    } catch (Exception exception) {
      StringBuilder stringBuilder;
      if (exception.getClass() == JavaScriptException.class) {
        stringBuilder = new StringBuilder();
        this();
        stringBuilder.append("Javascript exception - ");
        stringBuilder.append(exception.getMessage());
        Log(stringBuilder.toString());
        Context.exit();
        return false;
      } 
      Log.d("TMCTF", String.format("Exception calling %s()", new Object[] { stringBuilder }));
      exception.printStackTrace();
      Context.exit();
      return false;
    } finally {}
    Context.exit();
    throw paramString;
  }
  
  public void setAndroidContext(Context paramContext) {
    this.androidContextObject = paramContext;
  }
  
  public void setCiphertextKey(String paramString) {
    this.ciphertextKey = paramString;
  }
  
  public void setImageView(View paramView) {
    this.imageView = paramView;
  }
  
  public void setTextView(View paramView) {
    this.textView = paramView;
  }
  
  public void setTitleView(View paramView) {
    this.titleView = paramView;
  }
  
  public boolean verify_unlocked() {
    this.singleton = Singleton.getInstance();
    try {
      return execute("verify_unlocked", false);
    } catch (Exception exception) {
      return false;
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes2-dex2jar.jar!/com/trendmicro/keybox/HippoLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */