package com.trendmicro.keybox;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class KEY4HintActivity extends AppCompatActivity {
  Listener listener;
  
  LocationManager locationManager;
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131427361);
    setSupportActionBar((Toolbar)findViewById(2131230925));
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    TextView textView2 = (TextView)findViewById(2131230800);
    TextView textView1 = (TextView)findViewById(2131230801);
    Singleton singleton = Singleton.getInstance();
    singleton.setAndroidContext((Context)this);
    Intent intent = getIntent();
    String str = intent.getStringExtra("hintkey4");
    intent.getDoubleExtra("latitude", 181.0D);
    intent.getDoubleExtra("longitude", 181.0D);
    if (str != null)
      singleton.hintkey4 = str; 
    Listener listener = new Listener();
    this.listener = listener;
    listener.setDelegate(this);
    LocationManager locationManager = (LocationManager)getSystemService("location");
    this.locationManager = locationManager;
    locationManager.requestLocationUpdates("gps", 0L, 0.0F, this.listener);
    HippoLoader hippoLoader = new HippoLoader(4);
    hippoLoader.setAndroidContext((Context)this);
    hippoLoader.setTextView((View)textView2);
    hippoLoader.setTitleView((View)textView1);
    if (!hippoLoader.entrypoint())
      hippoLoader.execute("log_ciphertext_js", true); 
  }
  
  protected void onPause() {
    super.onPause();
    this.locationManager.removeUpdates(this.listener);
  }
  
  protected void onResume() {
    super.onResume();
    TextView textView1 = (TextView)findViewById(2131230800);
    TextView textView2 = (TextView)findViewById(2131230801);
    Intent intent = getIntent();
    intent.getStringExtra("hintkey4");
    intent.getDoubleExtra("latitude", 181.0D);
    intent.getDoubleExtra("longitude", 181.0D);
    HippoLoader hippoLoader = new HippoLoader(4);
    hippoLoader.setAndroidContext((Context)this);
    hippoLoader.setTextView((View)textView1);
    hippoLoader.setTitleView((View)textView2);
    if (!hippoLoader.entrypoint())
      hippoLoader.execute("log_ciphertext_js", true); 
    this.locationManager.requestLocationUpdates("gps", 0L, 0.0F, this.listener);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes2-dex2jar.jar!/com/trendmicro/keybox/KEY4HintActivity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */