package com.trendmicro.keybox;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class KEY3HintActivity extends AppCompatActivity {
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131427360);
    setSupportActionBar((Toolbar)findViewById(2131230925));
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    TextView textView1 = (TextView)findViewById(2131230800);
    TextView textView2 = (TextView)findViewById(2131230801);
    Singleton singleton = Singleton.getInstance();
    LocationManager locationManager = (LocationManager)getSystemService("location");
    locationManager.getProvider("gps");
    new Listener();
    locationManager.getLastKnownLocation("gps");
    String str = getIntent().getStringExtra("hintkey3");
    if (str != null)
      singleton.hintkey3 = str; 
    HippoLoader hippoLoader = new HippoLoader(3);
    hippoLoader.setAndroidContext((Context)this);
    hippoLoader.setTextView((View)textView1);
    hippoLoader.setTitleView((View)textView2);
    if (!hippoLoader.entrypoint())
      hippoLoader.execute("log_ciphertext_js", true); 
  }
  
  protected void onPause() {
    super.onPause();
  }
  
  protected void onResume() {
    Log.d("TMCTF", "onResume()");
    super.onResume();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes2-dex2jar.jar!/com/trendmicro/keybox/KEY3HintActivity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */