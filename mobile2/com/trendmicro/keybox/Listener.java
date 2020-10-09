package com.trendmicro.keybox;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class Listener implements LocationListener {
  KEY4HintActivity delegate;
  
  Boolean did_change = Boolean.FALSE;
  
  double latitude = 0.0D;
  
  double latitude_last = 0.0D;
  
  double longitude = 0.0D;
  
  double longitude_last = 0.0D;
  
  Singleton singleton;
  
  public void onLocationChanged(Location paramLocation) {
    this.singleton = Singleton.getInstance();
    paramLocation.toString();
    this.latitude = paramLocation.getLatitude();
    this.longitude = paramLocation.getLongitude();
    if (this.latitude != this.singleton.latitude || this.longitude != this.singleton.longitude) {
      this.singleton.latitude = this.latitude;
      this.singleton.longitude = this.longitude;
      this.latitude_last = this.latitude;
      this.longitude_last = this.longitude;
      this.did_change = Boolean.TRUE;
      Intent intent = new Intent();
      intent.setClassName("com.trendmicro.keybox", "com.trendmicro.keybox.KEY4HintActivity");
      intent.setFlags(268435456);
      intent.putExtra("latitude", this.latitude);
      intent.putExtra("longitude", this.longitude);
      intent.putExtra("key", this.singleton.hintkey4);
      this.delegate.startActivity(intent);
      return;
    } 
    this.did_change = Boolean.FALSE;
  }
  
  public void onProviderDisabled(String paramString) {}
  
  public void onProviderEnabled(String paramString) {}
  
  public void onStatusChanged(String paramString, int paramInt, Bundle paramBundle) {}
  
  public void setDelegate(KEY4HintActivity paramKEY4HintActivity) {
    this.delegate = paramKEY4HintActivity;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes2-dex2jar.jar!/com/trendmicro/keybox/Listener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */