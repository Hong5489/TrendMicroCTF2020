package com.trendmicro.keybox;

import android.content.Context;
import android.content.Intent;
import com.trendmicro.hippo.ContextFactoryFactory;
import java.io.InputStream;

public class Singleton {
  private static volatile Singleton singleton;
  
  Context androidContextObject;
  
  int box_index;
  
  ContextFactoryFactory factory;
  
  public String flagkey0;
  
  public String flagkey0_key;
  
  public String flagkey1;
  
  public String flagkey1_key;
  
  public String flagkey2;
  
  public String flagkey2_key;
  
  public String flagkey3;
  
  public String flagkey3_key;
  
  public String flagkey4;
  
  public String flagkey4_key;
  
  public String hintkey0;
  
  public String hintkey1;
  
  public String hintkey2;
  
  public String hintkey3;
  
  public String hintkey4;
  
  public String hintkeyflag;
  
  public String hintkeymain;
  
  String[] key4_box;
  
  public double latitude;
  
  public double longitude;
  
  private Singleton() {
    try {
      ContextFactoryFactory contextFactoryFactory = new ContextFactoryFactory();
      this();
      this.factory = contextFactoryFactory;
      this.hintkey0 = contextFactoryFactory.CreateKey(0);
      this.hintkeymain = this.factory.CreateKey(1);
      this.key4_box = new String[] { "", "", "" };
      this.box_index = 0;
    } catch (Exception exception) {}
  }
  
  public static Singleton getInstance() {
    if (singleton == null)
      singleton = new Singleton(); 
    return singleton;
  }
  
  public static Singleton getInstance(Boolean paramBoolean) {
    if (paramBoolean.booleanValue())
      singleton = new Singleton(); 
    return singleton;
  }
  
  public String hintkey(int paramInt) {
    String str = null;
    if (paramInt != 0) {
      if (paramInt != 1) {
        if (paramInt != 2) {
          if (paramInt != 3) {
            if (paramInt == 4)
              str = this.hintkey4; 
          } else {
            str = this.hintkey3;
          } 
        } else {
          str = this.hintkey2;
        } 
      } else {
        str = this.hintkey1;
      } 
    } else {
      str = this.hintkey0;
    } 
    return str;
  }
  
  public String hintkey(String paramString) {
    String str = null;
    if (paramString.equals("main")) {
      str = this.hintkeymain;
    } else if (paramString.equals("flag")) {
      str = this.hintkeyflag;
    } 
    return str;
  }
  
  public String keykey(int paramInt) {
    String str = null;
    if (paramInt != 0) {
      if (paramInt != 1) {
        if (paramInt != 2) {
          if (paramInt != 3) {
            if (paramInt == 4)
              str = this.flagkey4_key; 
          } else {
            str = this.flagkey3_key;
          } 
        } else {
          str = this.flagkey2_key;
        } 
      } else {
        str = this.flagkey1_key;
      } 
    } else {
      str = this.flagkey0_key;
    } 
    return str;
  }
  
  public void push(String paramString) {
    if (!paramString.equals(this.key4_box[2])) {
      String[] arrayOfString = this.key4_box;
      arrayOfString[0] = arrayOfString[1];
      arrayOfString[1] = arrayOfString[2];
      arrayOfString[2] = paramString;
      this.flagkey4_key = String.join("", (CharSequence[])arrayOfString);
    } 
    try {
      paramString = String.format("key_%d/key_%d.enc", new Object[] { Integer.valueOf(4), Integer.valueOf(4) });
      InputStream inputStream = this.androidContextObject.getAssets().open(paramString);
      byte[] arrayOfByte = new byte[inputStream.available()];
      inputStream.read(arrayOfByte);
      Oracle oracle = new Oracle();
      this(this.flagkey4_key.getBytes());
      String str = new String();
      this(oracle.process(arrayOfByte));
      if (str.matches("KEY4-.*")) {
        Intent intent = new Intent();
        this();
        intent.setClassName("com.trendmicro.keybox", "com.trendmicro.keybox.KeyboxMainActivity");
        intent.setFlags(268435456);
        this.androidContextObject.startActivity(intent);
      } 
    } catch (Exception exception) {}
  }
  
  public void setAndroidContext(Context paramContext) {
    this.androidContextObject = paramContext;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes2-dex2jar.jar!/com/trendmicro/keybox/Singleton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */