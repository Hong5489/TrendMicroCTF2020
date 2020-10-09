package com.trendmicro.keybox;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.trendmicro.hippo.ContextFactoryFactory;
import java.util.Arrays;

public class KeyboxMainActivity extends AppCompatActivity {
  Context context;
  
  HippoLoader decrypter;
  
  ContextFactoryFactory factory;
  
  ImageView flag_image;
  
  TextView flag_text;
  
  HippoLoader flagloader;
  
  Handler handler;
  
  HippoLoader[] hints;
  
  ImageView key0_image;
  
  TextView key0_text;
  
  ImageView key1_image;
  
  TextView key1_text;
  
  ImageView key2_image;
  
  TextView key2_text;
  
  ImageView key3_image;
  
  TextView key3_text;
  
  ImageView key4_image;
  
  TextView key4_text;
  
  HippoLoader mainloader;
  
  Observer observer;
  
  Singleton singleton;
  
  Toast toast;
  
  Uri uri;
  
  private void ShowLockedToast(String paramString) {
    char[] arrayOfChar = new char[1];
    String str = new String();
    if (10 - paramString.length() > 0) {
      char[] arrayOfChar1 = new char[10 - paramString.length()];
      Arrays.fill(arrayOfChar1, ' ');
      str = new String(arrayOfChar1);
    } 
    Toast toast = new Toast(getApplicationContext());
    View view = getLayoutInflater().inflate(2131427383, (ViewGroup)findViewById(2131230828));
    ((TextView)view.findViewById(2131230924)).setText(String.format("%s locked%s", new Object[] { paramString, str }));
    toast.setGravity(16, 0, 0);
    toast.setDuration(0);
    toast.setView(view);
    toast.show();
  }
  
  public void FireKey0Hints(View paramView) {
    if (this.hints[0].verify_unlocked()) {
      startActivity(new Intent((Context)this, KEY0HintActivity.class));
    } else {
      ShowLockedToast("Key Zero hints");
    } 
  }
  
  public void FireKey1Hints(View paramView) {
    if (this.hints[1].verify_unlocked()) {
      startActivity(new Intent((Context)this, KEY1HintActivity.class));
    } else {
      ShowLockedToast("Key One hints");
    } 
  }
  
  public void FireKey2Hints(View paramView) {
    if (this.hints[2].verify_unlocked()) {
      startActivity(new Intent((Context)this, KEY2HintActivity.class));
    } else {
      ShowLockedToast("Key Two hints");
    } 
  }
  
  public void FireKey3Hints(View paramView) {
    if (this.hints[3].verify_unlocked()) {
      startActivity(new Intent((Context)this, KEY3HintActivity.class));
    } else {
      ShowLockedToast("Key Three hints");
    } 
  }
  
  public void FireKey4Hints(View paramView) {
    if (this.hints[4].verify_unlocked()) {
      startActivity(new Intent((Context)this, KEY4HintActivity.class));
    } else {
      ShowLockedToast("Key Four hints");
    } 
  }
  
  public void GetFlag(View paramView) {
    ShowLockedToast("Flag");
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131427362);
    this.singleton = Singleton.getInstance();
    HippoLoader hippoLoader2 = new HippoLoader();
    this.decrypter = hippoLoader2;
    hippoLoader2.setAndroidContext((Context)this);
    this.key0_image = (ImageView)findViewById(2131230816);
    this.key1_image = (ImageView)findViewById(2131230817);
    this.key2_image = (ImageView)findViewById(2131230818);
    this.key3_image = (ImageView)findViewById(2131230819);
    this.key4_image = (ImageView)findViewById(2131230820);
    this.flag_image = (ImageView)findViewById(2131230794);
    this.key0_text = (TextView)findViewById(2131230910);
    this.key1_text = (TextView)findViewById(2131230911);
    this.key2_text = (TextView)findViewById(2131230912);
    this.key3_text = (TextView)findViewById(2131230913);
    this.key4_text = (TextView)findViewById(2131230914);
    this.flag_text = (TextView)findViewById(2131230908);
    this.decrypter.decrypt_key(0);
    this.decrypter.decrypt_key(1);
    this.decrypter.decrypt_key(2);
    this.decrypter.decrypt_key(3);
    this.decrypter.decrypt_key(4);
    this.key0_image.setColorFilter(-3407872, PorterDuff.Mode.DARKEN);
    this.key1_image.setColorFilter(-3407872, PorterDuff.Mode.DARKEN);
    this.key2_image.setColorFilter(-3407872, PorterDuff.Mode.DARKEN);
    this.key3_image.setColorFilter(-3407872, PorterDuff.Mode.DARKEN);
    this.key4_image.setColorFilter(-3407872, PorterDuff.Mode.DARKEN);
    this.flag_image.setColorFilter(-3407872, PorterDuff.Mode.DARKEN);
    this.key0_image.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            KeyboxMainActivity.this.FireKey0Hints(param1View);
          }
        });
    this.key1_image.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            KeyboxMainActivity.this.FireKey1Hints(param1View);
          }
        });
    this.key2_image.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            KeyboxMainActivity.this.FireKey2Hints(param1View);
          }
        });
    this.key3_image.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            KeyboxMainActivity.this.FireKey3Hints(param1View);
          }
        });
    this.key4_image.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            KeyboxMainActivity.this.FireKey4Hints(param1View);
          }
        });
    this.flag_image.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            KeyboxMainActivity.this.GetFlag(param1View);
          }
        });
    this.key0_text.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            KeyboxMainActivity.this.FireKey0Hints(param1View);
          }
        });
    this.key1_text.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            KeyboxMainActivity.this.FireKey1Hints(param1View);
          }
        });
    this.key2_text.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            KeyboxMainActivity.this.FireKey2Hints(param1View);
          }
        });
    this.key3_text.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            KeyboxMainActivity.this.FireKey3Hints(param1View);
          }
        });
    this.key4_text.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            KeyboxMainActivity.this.FireKey4Hints(param1View);
          }
        });
    this.flag_text.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            KeyboxMainActivity.this.GetFlag(param1View);
          }
        });
    Context context = getApplicationContext();
    this.handler = new Handler();
    Observer observer = new Observer(this.handler);
    this.observer = observer;
    observer.setContext(context);
    try {
      ContextFactoryFactory contextFactoryFactory = new ContextFactoryFactory();
      this();
      this.factory = contextFactoryFactory;
      this.uri = Uri.parse(contextFactoryFactory.resolverFactory());
    } catch (Exception exception) {}
    context.getContentResolver().registerContentObserver(this.uri, true, this.observer);
    HippoLoader hippoLoader1 = new HippoLoader("main");
    this.mainloader = hippoLoader1;
    hippoLoader1.setAndroidContext((Context)this);
    this.mainloader.entrypoint();
    this.hints = new HippoLoader[5];
    byte b = 0;
    while (true) {
      HippoLoader[] arrayOfHippoLoader = this.hints;
      if (b < arrayOfHippoLoader.length) {
        arrayOfHippoLoader[b] = new HippoLoader(b);
        this.hints[b].setAndroidContext((Context)this);
        b++;
        continue;
      } 
      break;
    } 
  }
  
  protected void onPause() {
    super.onPause();
  }
  
  protected void onResume() {
    super.onResume();
    this.key0_image = (ImageView)findViewById(2131230816);
    this.key1_image = (ImageView)findViewById(2131230817);
    this.key2_image = (ImageView)findViewById(2131230818);
    this.key3_image = (ImageView)findViewById(2131230819);
    this.key4_image = (ImageView)findViewById(2131230820);
    this.flag_image = (ImageView)findViewById(2131230794);
    this.key0_text = (TextView)findViewById(2131230910);
    this.key1_text = (TextView)findViewById(2131230911);
    this.key2_text = (TextView)findViewById(2131230912);
    this.key3_text = (TextView)findViewById(2131230913);
    this.key4_text = (TextView)findViewById(2131230914);
    this.flag_text = (TextView)findViewById(2131230908);
    String str1 = this.decrypter.decrypt_key(0);
    String str2 = this.decrypter.decrypt_key(1);
    String str3 = this.decrypter.decrypt_key(2);
    String str4 = this.decrypter.decrypt_key(3);
    String str5 = this.decrypter.decrypt_key(4);
    if (str1 != null && str1.matches("KEY0-.*")) {
      this.key0_image.setColorFilter(-16724992, PorterDuff.Mode.DARKEN);
      this.key0_text.setText(str1);
    } 
    if (str2 != null && str2.matches("KEY1-.*")) {
      this.key1_image.setColorFilter(-16724992, PorterDuff.Mode.DARKEN);
      this.key1_text.setText(str2);
      Singleton singleton = this.singleton;
      singleton.hintkey1 = singleton.hintkey0;
    } 
    if (str3 != null && str3.matches("KEY2-.*")) {
      this.key2_image.setColorFilter(-16724992, PorterDuff.Mode.DARKEN);
      this.key2_text.setText(str3);
      Singleton singleton = this.singleton;
      singleton.hintkey2 = singleton.hintkey0;
    } 
    if (str4 != null && str4.matches("KEY3-.*")) {
      this.key3_image.setColorFilter(-16724992, PorterDuff.Mode.DARKEN);
      this.key3_text.setText(str4);
      Singleton singleton = this.singleton;
      singleton.hintkey3 = singleton.hintkey0;
    } 
    if (str5 != null && str5.matches("KEY4-.*")) {
      this.key4_image.setColorFilter(-16724992, PorterDuff.Mode.DARKEN);
      this.key4_text.setText(str5);
      Singleton singleton = this.singleton;
      singleton.hintkey4 = singleton.hintkey0;
    } 
    if (str1 != null && str2 != null && str3 != null && str4 != null && str5 != null && str1.matches("KEY0-.*") && str2.matches("KEY1-.*") && str3.matches("KEY2-.*") && str4.matches("KEY3-.*") && str5.matches("KEY4-.*")) {
      Log.d("TMCTF", "TIME TO UNLOCK THE FLAG!");
      Intent intent = new Intent();
      intent.setClassName("com.trendmicro.keybox", "com.trendmicro.keybox.FlagActivity");
      intent.setFlags(268435456);
      intent.putExtra("key0", str1);
      intent.putExtra("key1", str2);
      intent.putExtra("key2", str3);
      intent.putExtra("key3", str4);
      intent.putExtra("key4", str5);
      startActivity(intent);
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes2-dex2jar.jar!/com/trendmicro/keybox/KeyboxMainActivity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */