package com.trendmicro.keybox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import java.io.InputStream;

public class FlagActivity extends AppCompatActivity {
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131427356);
    setSupportActionBar((Toolbar)findViewById(2131230925));
    Singleton.getInstance();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    Intent intent = getIntent();
    String str1 = intent.getStringExtra("key0");
    String str2 = intent.getStringExtra("key1");
    String str3 = intent.getStringExtra("key2");
    String str4 = intent.getStringExtra("key3");
    String str5 = intent.getStringExtra("key4");
    TextView textView1 = (TextView)findViewById(2131230800);
    TextView textView2 = (TextView)findViewById(2131230801);
    try {
      InputStream inputStream = getAssets().open("flag/flag.enc");
      byte[] arrayOfByte1 = new byte[inputStream.available()];
      byte[] arrayOfByte2 = new byte[inputStream.available()];
      arrayOfByte2 = new byte[inputStream.available()];
      inputStream.read(arrayOfByte1);
      inputStream.close();
      Oracle oracle2 = new Oracle();
      this(str1.getBytes());
      Oracle oracle1 = new Oracle();
      try {
        this(str2.getBytes());
        Oracle oracle = new Oracle();
        try {
          this(str3.getBytes());
          Oracle oracle3 = new Oracle();
          this(str4.getBytes());
          Oracle oracle4 = new Oracle();
          try {
            this(str5.getBytes());
            byte[] arrayOfByte = oracle4.process(oracle3.process(oracle.process(oracle1.process(oracle2.process(arrayOfByte1)))));
            String str = new String();
            this(arrayOfByte);
            Log.d("TMCTF", String.format("PLAINTEXT FLAG: %s", new Object[] { str }));
            if (str.matches("TMCTF\\{.*\\}")) {
              textView1.setText(str);
              textView2.setText("Keybox Complete!");
            } 
          } catch (Exception null) {}
        } catch (Exception null) {}
      } catch (Exception null) {}
    } catch (Exception exception) {}
    Log.d("TMCTF", "Oracle exception");
    exception.printStackTrace();
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes2-dex2jar.jar!/com/trendmicro/keybox/FlagActivity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */