package com.trendmicro.keybox;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class KEY1HintActivity extends AppCompatActivity {
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131427358);
    setSupportActionBar((Toolbar)findViewById(2131230925));
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    Singleton singleton = Singleton.getInstance();
    TextView textView1 = (TextView)findViewById(2131230800);
    TextView textView2 = (TextView)findViewById(2131230801);
    String str = getIntent().getStringExtra("hintkey1");
    if (str != null)
      singleton.hintkey1 = str; 
    HippoLoader hippoLoader = new HippoLoader(1);
    hippoLoader.setAndroidContext((Context)this);
    hippoLoader.setTextView((View)textView1);
    hippoLoader.setTitleView((View)textView2);
    if (!hippoLoader.entrypoint())
      hippoLoader.execute("log_ciphertext_js", true); 
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes2-dex2jar.jar!/com/trendmicro/keybox/KEY1HintActivity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */