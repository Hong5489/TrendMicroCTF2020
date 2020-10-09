package androidx.versionedparcelable;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseIntArray;

class VersionedParcelParcel extends VersionedParcel {
  private static final boolean DEBUG = false;
  
  private static final String TAG = "VersionedParcelParcel";
  
  private int mCurrentField = -1;
  
  private final int mEnd;
  
  private int mNextRead = 0;
  
  private final int mOffset;
  
  private final Parcel mParcel;
  
  private final SparseIntArray mPositionLookup = new SparseIntArray();
  
  private final String mPrefix;
  
  VersionedParcelParcel(Parcel paramParcel) {
    this(paramParcel, paramParcel.dataPosition(), paramParcel.dataSize(), "");
  }
  
  VersionedParcelParcel(Parcel paramParcel, int paramInt1, int paramInt2, String paramString) {
    this.mParcel = paramParcel;
    this.mOffset = paramInt1;
    this.mEnd = paramInt2;
    this.mNextRead = paramInt1;
    this.mPrefix = paramString;
  }
  
  private int readUntilField(int paramInt) {
    while (true) {
      int i = this.mNextRead;
      if (i < this.mEnd) {
        this.mParcel.setDataPosition(i);
        int j = this.mParcel.readInt();
        i = this.mParcel.readInt();
        this.mNextRead += j;
        if (i == paramInt)
          return this.mParcel.dataPosition(); 
        continue;
      } 
      return -1;
    } 
  }
  
  public void closeField() {
    int i = this.mCurrentField;
    if (i >= 0) {
      i = this.mPositionLookup.get(i);
      int j = this.mParcel.dataPosition();
      this.mParcel.setDataPosition(i);
      this.mParcel.writeInt(j - i);
      this.mParcel.setDataPosition(j);
    } 
  }
  
  protected VersionedParcel createSubParcel() {
    Parcel parcel = this.mParcel;
    int i = parcel.dataPosition();
    int j = this.mNextRead;
    int k = j;
    if (j == this.mOffset)
      k = this.mEnd; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.mPrefix);
    stringBuilder.append("  ");
    return new VersionedParcelParcel(parcel, i, k, stringBuilder.toString());
  }
  
  public boolean readBoolean() {
    boolean bool;
    if (this.mParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public Bundle readBundle() {
    return this.mParcel.readBundle(getClass().getClassLoader());
  }
  
  public byte[] readByteArray() {
    int i = this.mParcel.readInt();
    if (i < 0)
      return null; 
    byte[] arrayOfByte = new byte[i];
    this.mParcel.readByteArray(arrayOfByte);
    return arrayOfByte;
  }
  
  public double readDouble() {
    return this.mParcel.readDouble();
  }
  
  public boolean readField(int paramInt) {
    paramInt = readUntilField(paramInt);
    if (paramInt == -1)
      return false; 
    this.mParcel.setDataPosition(paramInt);
    return true;
  }
  
  public float readFloat() {
    return this.mParcel.readFloat();
  }
  
  public int readInt() {
    return this.mParcel.readInt();
  }
  
  public long readLong() {
    return this.mParcel.readLong();
  }
  
  public <T extends Parcelable> T readParcelable() {
    return (T)this.mParcel.readParcelable(getClass().getClassLoader());
  }
  
  public String readString() {
    return this.mParcel.readString();
  }
  
  public IBinder readStrongBinder() {
    return this.mParcel.readStrongBinder();
  }
  
  public void setOutputField(int paramInt) {
    closeField();
    this.mCurrentField = paramInt;
    this.mPositionLookup.put(paramInt, this.mParcel.dataPosition());
    writeInt(0);
    writeInt(paramInt);
  }
  
  public void writeBoolean(boolean paramBoolean) {
    this.mParcel.writeInt(paramBoolean);
  }
  
  public void writeBundle(Bundle paramBundle) {
    this.mParcel.writeBundle(paramBundle);
  }
  
  public void writeByteArray(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte != null) {
      this.mParcel.writeInt(paramArrayOfbyte.length);
      this.mParcel.writeByteArray(paramArrayOfbyte);
    } else {
      this.mParcel.writeInt(-1);
    } 
  }
  
  public void writeByteArray(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    if (paramArrayOfbyte != null) {
      this.mParcel.writeInt(paramArrayOfbyte.length);
      this.mParcel.writeByteArray(paramArrayOfbyte, paramInt1, paramInt2);
    } else {
      this.mParcel.writeInt(-1);
    } 
  }
  
  public void writeDouble(double paramDouble) {
    this.mParcel.writeDouble(paramDouble);
  }
  
  public void writeFloat(float paramFloat) {
    this.mParcel.writeFloat(paramFloat);
  }
  
  public void writeInt(int paramInt) {
    this.mParcel.writeInt(paramInt);
  }
  
  public void writeLong(long paramLong) {
    this.mParcel.writeLong(paramLong);
  }
  
  public void writeParcelable(Parcelable paramParcelable) {
    this.mParcel.writeParcelable(paramParcelable, 0);
  }
  
  public void writeString(String paramString) {
    this.mParcel.writeString(paramString);
  }
  
  public void writeStrongBinder(IBinder paramIBinder) {
    this.mParcel.writeStrongBinder(paramIBinder);
  }
  
  public void writeStrongInterface(IInterface paramIInterface) {
    this.mParcel.writeStrongInterface(paramIInterface);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/androidx/versionedparcelable/VersionedParcelParcel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */