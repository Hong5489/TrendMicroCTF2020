package android.support.v4.app;

import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.v4.graphics.drawable.IconCompat;

public class Person {
  private static final String ICON_KEY = "icon";
  
  private static final String IS_BOT_KEY = "isBot";
  
  private static final String IS_IMPORTANT_KEY = "isImportant";
  
  private static final String KEY_KEY = "key";
  
  private static final String NAME_KEY = "name";
  
  private static final String URI_KEY = "uri";
  
  IconCompat mIcon;
  
  boolean mIsBot;
  
  boolean mIsImportant;
  
  String mKey;
  
  CharSequence mName;
  
  String mUri;
  
  Person(Builder paramBuilder) {
    this.mName = paramBuilder.mName;
    this.mIcon = paramBuilder.mIcon;
    this.mUri = paramBuilder.mUri;
    this.mKey = paramBuilder.mKey;
    this.mIsBot = paramBuilder.mIsBot;
    this.mIsImportant = paramBuilder.mIsImportant;
  }
  
  public static Person fromAndroidPerson(android.app.Person paramPerson) {
    IconCompat iconCompat;
    Builder builder = (new Builder()).setName(paramPerson.getName());
    if (paramPerson.getIcon() != null) {
      iconCompat = IconCompat.createFromIcon(paramPerson.getIcon());
    } else {
      iconCompat = null;
    } 
    return builder.setIcon(iconCompat).setUri(paramPerson.getUri()).setKey(paramPerson.getKey()).setBot(paramPerson.isBot()).setImportant(paramPerson.isImportant()).build();
  }
  
  public static Person fromBundle(Bundle paramBundle) {
    Bundle bundle = paramBundle.getBundle("icon");
    Builder builder = (new Builder()).setName(paramBundle.getCharSequence("name"));
    if (bundle != null) {
      IconCompat iconCompat = IconCompat.createFromBundle(bundle);
    } else {
      bundle = null;
    } 
    return builder.setIcon((IconCompat)bundle).setUri(paramBundle.getString("uri")).setKey(paramBundle.getString("key")).setBot(paramBundle.getBoolean("isBot")).setImportant(paramBundle.getBoolean("isImportant")).build();
  }
  
  public IconCompat getIcon() {
    return this.mIcon;
  }
  
  public String getKey() {
    return this.mKey;
  }
  
  public CharSequence getName() {
    return this.mName;
  }
  
  public String getUri() {
    return this.mUri;
  }
  
  public boolean isBot() {
    return this.mIsBot;
  }
  
  public boolean isImportant() {
    return this.mIsImportant;
  }
  
  public android.app.Person toAndroidPerson() {
    Icon icon;
    android.app.Person.Builder builder = (new android.app.Person.Builder()).setName(getName());
    if (getIcon() != null) {
      icon = getIcon().toIcon();
    } else {
      icon = null;
    } 
    return builder.setIcon(icon).setUri(getUri()).setKey(getKey()).setBot(isBot()).setImportant(isImportant()).build();
  }
  
  public Builder toBuilder() {
    return new Builder(this);
  }
  
  public Bundle toBundle() {
    Bundle bundle = new Bundle();
    bundle.putCharSequence("name", this.mName);
    IconCompat iconCompat = this.mIcon;
    if (iconCompat != null) {
      Bundle bundle1 = iconCompat.toBundle();
    } else {
      iconCompat = null;
    } 
    bundle.putBundle("icon", (Bundle)iconCompat);
    bundle.putString("uri", this.mUri);
    bundle.putString("key", this.mKey);
    bundle.putBoolean("isBot", this.mIsBot);
    bundle.putBoolean("isImportant", this.mIsImportant);
    return bundle;
  }
  
  public static class Builder {
    IconCompat mIcon;
    
    boolean mIsBot;
    
    boolean mIsImportant;
    
    String mKey;
    
    CharSequence mName;
    
    String mUri;
    
    public Builder() {}
    
    Builder(Person param1Person) {
      this.mName = param1Person.mName;
      this.mIcon = param1Person.mIcon;
      this.mUri = param1Person.mUri;
      this.mKey = param1Person.mKey;
      this.mIsBot = param1Person.mIsBot;
      this.mIsImportant = param1Person.mIsImportant;
    }
    
    public Person build() {
      return new Person(this);
    }
    
    public Builder setBot(boolean param1Boolean) {
      this.mIsBot = param1Boolean;
      return this;
    }
    
    public Builder setIcon(IconCompat param1IconCompat) {
      this.mIcon = param1IconCompat;
      return this;
    }
    
    public Builder setImportant(boolean param1Boolean) {
      this.mIsImportant = param1Boolean;
      return this;
    }
    
    public Builder setKey(String param1String) {
      this.mKey = param1String;
      return this;
    }
    
    public Builder setName(CharSequence param1CharSequence) {
      this.mName = param1CharSequence;
      return this;
    }
    
    public Builder setUri(String param1String) {
      this.mUri = param1String;
      return this;
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v4/app/Person.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */