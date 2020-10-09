package com.trendmicro.keybox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Unlocker extends BroadcastReceiver {
  public void onReceive(Context paramContext, Intent paramIntent) {
    Intent intent;
    Singleton singleton = Singleton.getInstance();
    str = paramIntent.getAction();
    if (str.equals("android.provider.Telephony.SECRET_CODE")) {
      String str1 = paramIntent.getData().getHost();
      if (str1.equals("8736364276")) {
        Singleton.getInstance(Boolean.valueOf(true));
      } else {
        singleton.flagkey2_key = str1;
      } 
      intent = new Intent();
      intent.setClassName("com.trendmicro.keybox", "com.trendmicro.keybox.KeyboxMainActivity");
      intent.setFlags(268435456);
      paramContext.startActivity(intent);
    } else if (str.equals("android.intent.action.PHONE_STATE")) {
      str = intent.getStringExtra("incoming_number");
      if (str != null)
        singleton.flagkey1_key = str.replaceAll("[^\\d.]", ""); 
      Bundle bundle = intent.getExtras();
      if (bundle != null)
        for (String str : bundle.keySet()) {
          Object object = bundle.get(str);
          if (str.equals("state") && object.toString().equals("IDLE")) {
            Intent intent1 = new Intent();
            intent1.setClassName("com.trendmicro.keybox", "com.trendmicro.keybox.KeyboxMainActivity");
            intent1.setFlags(268435456);
            paramContext.startActivity(intent1);
          } 
        }  
    } 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes2-dex2jar.jar!/com/trendmicro/keybox/Unlocker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */