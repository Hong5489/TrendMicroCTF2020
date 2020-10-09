package com.trendmicro.keybox;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import com.trendmicro.hippo.ContextFactoryFactory;

public class Observer extends ContentObserver {
  Context context;
  
  Cursor cursor;
  
  ContextFactoryFactory factory;
  
  String message;
  
  Singleton singleton = Singleton.getInstance();
  
  Uri uri;
  
  public Observer(Handler paramHandler) {
    super(paramHandler);
  }
  
  public void onChange(boolean paramBoolean) {
    if (this.context == null)
      return; 
    try {
      ContextFactoryFactory contextFactoryFactory = new ContextFactoryFactory();
      this();
      this.factory = contextFactoryFactory;
      this.uri = Uri.parse(contextFactoryFactory.resolverFactory());
    } catch (Exception exception) {}
    super.onChange(paramBoolean);
    try {
      Cursor cursor = this.context.getContentResolver().query(this.uri, null, null, null, null);
      this.cursor = cursor;
      cursor.moveToFirst();
      for (byte b = 0; b < this.cursor.getColumnCount(); b++) {
        String.format("%s == %s", new Object[] { this.cursor.getColumnName(b), this.cursor.getString(b) });
        if (this.cursor.getString(b) != null && this.cursor.getColumnName(b) != null && this.cursor.getString(b).equals(this.cursor.getColumnName(b)) && this.cursor.getInt(this.cursor.getColumnIndex("type")) == 1) {
          String str1 = this.cursor.getString(b);
          this.message = str1;
          this.singleton.flagkey3_key = str1;
          Intent intent = new Intent();
          this();
          intent.setClassName("com.trendmicro.keybox", "com.trendmicro.keybox.KeyboxMainActivity");
          intent.setFlags(268435456);
          this.context.startActivity(intent);
        } 
        String str = new String();
        this();
        this.message = str;
      } 
      this.cursor.close();
    } catch (Exception exception) {
      Log.d("TMCTF", String.format("SMS observer error: %s", new Object[] { exception.getLocalizedMessage() }));
    } 
  }
  
  public void setContext(Context paramContext) {
    this.context = paramContext;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes2-dex2jar.jar!/com/trendmicro/keybox/Observer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */