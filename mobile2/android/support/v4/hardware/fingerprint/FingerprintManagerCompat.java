package android.support.v4.hardware.fingerprint;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import android.support.v4.os.CancellationSignal;
import java.security.Signature;
import javax.crypto.Cipher;
import javax.crypto.Mac;

public final class FingerprintManagerCompat {
  private final Context mContext;
  
  private FingerprintManagerCompat(Context paramContext) {
    this.mContext = paramContext;
  }
  
  public static FingerprintManagerCompat from(Context paramContext) {
    return new FingerprintManagerCompat(paramContext);
  }
  
  private static FingerprintManager getFingerprintManagerOrNull(Context paramContext) {
    return paramContext.getPackageManager().hasSystemFeature("android.hardware.fingerprint") ? (FingerprintManager)paramContext.getSystemService(FingerprintManager.class) : null;
  }
  
  static CryptoObject unwrapCryptoObject(FingerprintManager.CryptoObject paramCryptoObject) {
    return (paramCryptoObject == null) ? null : ((paramCryptoObject.getCipher() != null) ? new CryptoObject(paramCryptoObject.getCipher()) : ((paramCryptoObject.getSignature() != null) ? new CryptoObject(paramCryptoObject.getSignature()) : ((paramCryptoObject.getMac() != null) ? new CryptoObject(paramCryptoObject.getMac()) : null)));
  }
  
  private static FingerprintManager.AuthenticationCallback wrapCallback(final AuthenticationCallback callback) {
    return new FingerprintManager.AuthenticationCallback() {
        public void onAuthenticationError(int param1Int, CharSequence param1CharSequence) {
          callback.onAuthenticationError(param1Int, param1CharSequence);
        }
        
        public void onAuthenticationFailed() {
          callback.onAuthenticationFailed();
        }
        
        public void onAuthenticationHelp(int param1Int, CharSequence param1CharSequence) {
          callback.onAuthenticationHelp(param1Int, param1CharSequence);
        }
        
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult param1AuthenticationResult) {
          callback.onAuthenticationSucceeded(new FingerprintManagerCompat.AuthenticationResult(FingerprintManagerCompat.unwrapCryptoObject(param1AuthenticationResult.getCryptoObject())));
        }
      };
  }
  
  private static FingerprintManager.CryptoObject wrapCryptoObject(CryptoObject paramCryptoObject) {
    return (paramCryptoObject == null) ? null : ((paramCryptoObject.getCipher() != null) ? new FingerprintManager.CryptoObject(paramCryptoObject.getCipher()) : ((paramCryptoObject.getSignature() != null) ? new FingerprintManager.CryptoObject(paramCryptoObject.getSignature()) : ((paramCryptoObject.getMac() != null) ? new FingerprintManager.CryptoObject(paramCryptoObject.getMac()) : null)));
  }
  
  public void authenticate(CryptoObject paramCryptoObject, int paramInt, CancellationSignal paramCancellationSignal, AuthenticationCallback paramAuthenticationCallback, Handler paramHandler) {
    if (Build.VERSION.SDK_INT >= 23) {
      FingerprintManager fingerprintManager = getFingerprintManagerOrNull(this.mContext);
      if (fingerprintManager != null) {
        if (paramCancellationSignal != null) {
          CancellationSignal cancellationSignal = (CancellationSignal)paramCancellationSignal.getCancellationSignalObject();
        } else {
          paramCancellationSignal = null;
        } 
        fingerprintManager.authenticate(wrapCryptoObject(paramCryptoObject), (CancellationSignal)paramCancellationSignal, paramInt, wrapCallback(paramAuthenticationCallback), paramHandler);
      } 
    } 
  }
  
  public boolean hasEnrolledFingerprints() {
    int i = Build.VERSION.SDK_INT;
    boolean bool = false;
    if (i >= 23) {
      FingerprintManager fingerprintManager = getFingerprintManagerOrNull(this.mContext);
      boolean bool1 = bool;
      if (fingerprintManager != null) {
        bool1 = bool;
        if (fingerprintManager.hasEnrolledFingerprints())
          bool1 = true; 
      } 
      return bool1;
    } 
    return false;
  }
  
  public boolean isHardwareDetected() {
    int i = Build.VERSION.SDK_INT;
    boolean bool = false;
    if (i >= 23) {
      FingerprintManager fingerprintManager = getFingerprintManagerOrNull(this.mContext);
      boolean bool1 = bool;
      if (fingerprintManager != null) {
        bool1 = bool;
        if (fingerprintManager.isHardwareDetected())
          bool1 = true; 
      } 
      return bool1;
    } 
    return false;
  }
  
  public static abstract class AuthenticationCallback {
    public void onAuthenticationError(int param1Int, CharSequence param1CharSequence) {}
    
    public void onAuthenticationFailed() {}
    
    public void onAuthenticationHelp(int param1Int, CharSequence param1CharSequence) {}
    
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult param1AuthenticationResult) {}
  }
  
  public static final class AuthenticationResult {
    private final FingerprintManagerCompat.CryptoObject mCryptoObject;
    
    public AuthenticationResult(FingerprintManagerCompat.CryptoObject param1CryptoObject) {
      this.mCryptoObject = param1CryptoObject;
    }
    
    public FingerprintManagerCompat.CryptoObject getCryptoObject() {
      return this.mCryptoObject;
    }
  }
  
  public static class CryptoObject {
    private final Cipher mCipher;
    
    private final Mac mMac;
    
    private final Signature mSignature;
    
    public CryptoObject(Signature param1Signature) {
      this.mSignature = param1Signature;
      this.mCipher = null;
      this.mMac = null;
    }
    
    public CryptoObject(Cipher param1Cipher) {
      this.mCipher = param1Cipher;
      this.mSignature = null;
      this.mMac = null;
    }
    
    public CryptoObject(Mac param1Mac) {
      this.mMac = param1Mac;
      this.mCipher = null;
      this.mSignature = null;
    }
    
    public Cipher getCipher() {
      return this.mCipher;
    }
    
    public Mac getMac() {
      return this.mMac;
    }
    
    public Signature getSignature() {
      return this.mSignature;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v4/hardware/fingerprint/FingerprintManagerCompat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */