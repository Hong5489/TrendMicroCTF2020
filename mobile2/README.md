# Mobile 2
Description *(Forgot to copy, all based on memory)*
```
Here is a APK file. Need to get 5 keys to unlock/decrypt the flag
```
[Keybox.apk](Keybox.apk)

Lets try to unzip it:
```bash
unzip Keybox.apk
Archive:  Keybox.apk
  inflating: res/anim/design_snackbar_in.xml  
 extracting: META-INF/android.support.design_material.version  
 extracting: res/drawable-hdpi-v4/abc_list_longpressed_holo.9.png  
  inflating: res/layout/design_text_input_password_icon.xml  
 extracting: res/drawable-xxhdpi-v4/abc_ic_star_half_black_16dp.png  
 extracting: META-INF/androidx.localbroadcastmanager_localbroadcastmanager.version  
 extracting: res/drawable-xhdpi-v4/notification_bg_low_pressed.9.png  
 extracting: res/drawable-xxxhdpi-v4/abc_btn_switch_to_on_mtrl_00012.9.png  
 extracting: res/mipmap-hdpi-v4/ic_greyflag_round.png  
  inflating: res/color-v23/abc_btn_colored_text_material.xml  
 extracting: res/mipmap-xhdpi-v4/ic_redkey.png  
...

ls
AndroidManifest.xml  assets  classes2.dex  classes.dex  com  Keybox.apk  META-INF  res  resources.arsc
```
Then I used `dex2jar` from this [Github](https://github.com/DexPatcher/dex2jar) *(The offical one doesn't work)*

Run the script for both dex files:
```bash
../../../dex-tools-2.1-20190905-lanchon/d2j-dex2jar.sh classes.dex classes2.dex 
Picked up _JAVA_OPTIONS: -Dawt.useSystemAAFontSettings=on -Dswing.aatext=true
dex2jar classes.dex -> ./classes-dex2jar.jar
dex2jar classes2.dex -> ./classes2-dex2jar.jar
```
Then I used the [JD-GUI](http://java-decompiler.github.io/) to view the `jar` files

You can also choose to save all the sources in the file menu

## Static Analysis
In the assets folder got many files looks suspicious:
```
flag  key_0  key_1  key_2  key_3  key_4  _main  _preload
```
Notice every key folder got a `ciphertext.js` (Encrypted javascript file) and a `key_%i.enc` (Encrypted key file) 

In the flag folder, notice a `code.js` implement an encryption looks like [RC4 Cipher](https://en.wikipedia.org/wiki/RC4)

Also got a encrypted flag file :`flag.enc`

According to the `FlagActivity` source code, we must find all 5 keys to decrypt the flag:
```java
public class FlagActivity extends AppCompatActivity {
  protected void onCreate(Bundle paramBundle) {
  	...
	String str1 = null.getStringExtra("key0");
	String str2 = null.getStringExtra("key1");
	String str3 = null.getStringExtra("key2");
	String str4 = null.getStringExtra("key3");
	String str5 = null.getStringExtra("key4");
	textView1 = (TextView)findViewById(2131230800);
	TextView textView2 = (TextView)findViewById(2131230801);
	try {
	  null = getAssets().open("flag/flag.enc");
	  byte[] arrayOfByte1 = new byte[null.available()];
	  byte[] arrayOfByte2 = new byte[null.available()];
	  arrayOfByte2 = new byte[null.available()];
	  null.read(arrayOfByte1);
	  null.close();
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
	        ...
```
In the `MainActivity`, we saw some lines that should lead us to the key:
```java
String str1 = this.decrypter.decrypt_key(0);
String str2 = this.decrypter.decrypt_key(1);
String str3 = this.decrypter.decrypt_key(2);
String str4 = this.decrypter.decrypt_key(3);
String str5 = this.decrypter.decrypt_key(4);
```
And notice `decrypter` is `HippoLoader` class:
```java
HippoLoader hippoLoader2 = new HippoLoader();
this.decrypter = hippoLoader2;
```
Then in `HippoLoader` source found the `decrypt_key` function:
```java
public String decrypt_key(int paramInt) {
    byte[] arrayOfByte;
    Log.d("TMCTF", String.format("Calling decrypt_key(%d)", new Object[] { Integer.valueOf(paramInt) }));
    String str = String.format("key_%d/key_%d.enc", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(paramInt) });
    AssetManager assetManager = this.androidContextObject.getAssets();
    Context context = ContextFactory.getGlobal().enterContext();
    context.setOptimizationLevel(-1);
    ImporterTopLevel importerTopLevel = new ImporterTopLevel(context);
    try {
      null = assetManager.open(this.path_preload);
      arrayOfByte = new byte[null.available()];
      null.read(arrayOfByte);
      null.close();
      null = new String();
      this(arrayOfByte);
      ScriptableObject.putProperty(importerTopLevel, "androidContext", Context.javaToJS(this.androidContextObject, importerTopLevel));
      context.evaluateString(importerTopLevel, null, this.path_preload, 1, null);
      arrayOfByte = null;
      null = assetManager.open(str);
      byte[] arrayOfByte1 = new byte[null.available()];
      null.read(arrayOfByte1);
      ScriptableObject.putProperty(importerTopLevel, "ciphertext_bytes", Context.javaToJS(arrayOfByte1, importerTopLevel));
      Object object = importerTopLevel.get("process", importerTopLevel);
      Singleton singleton1 = this.singleton;
      try {
        String str1 = singleton1.keykey(paramInt);
        if (object instanceof Function)
          object1 = ((Function)object).call(context, importerTopLevel, importerTopLevel, new Object[] { str1, arrayOfByte1 }); 
        Object object1 = (String)object1;
        Context.exit();
        return object1;
      } catch (Exception null) {
        if (arrayOfByte.getClass() == com.trendmicro.hippo.JavaScriptException.class) {
          StringBuilder stringBuilder = new StringBuilder();
          this();
          stringBuilder.append("Javascript exception - ");
          stringBuilder.append(arrayOfByte.getMessage());
          Log(stringBuilder.toString());
          Context.exit();
          return null;
        } 
        Log(String.format("Exception in decrypt_key()", new Object[0]));
        arrayOfByte.printStackTrace();
        Context.exit();
        return null;
      } finally {}
    } catch (Exception null) {
    
    } finally {}
    if (arrayOfByte.getClass() == com.trendmicro.hippo.JavaScriptException.class) {
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("Javascript exception - ");
      stringBuilder.append(arrayOfByte.getMessage());
      Log(stringBuilder.toString());
      Context.exit();
      return null;
    } 
    Log(String.format("Exception in decrypt_key()", new Object[0]));
    arrayOfByte.printStackTrace();
    Context.exit();
    return null;
  }
```
Notice the `ciphertext_bytes` and `process` mention in the function, that also in the `code.js` file we saw previously

The `process` function is the RC4 encryption function:
```js
function process(key, arr, bool) {

    if(!key) {
        throw "No password supplied";
    }

	var s = [], j = 0, x, res = [];
	for (var i = 0; i < 256; i++) {
		s[i] = i;
	}
	for (i = 0; i < 256; i++) {
		j = (j + s[i] + key.charCodeAt(i % key.length)) % 256;
		x = s[i];
		s[i] = s[j];
		s[j] = x;
	}
	i = 0;
	j = 0;
	for (var y = 0; y < arr.length; y++) {
		i = (i + 1) % 256;
		j = (j + s[i]) % 256;
		x = s[i];
		s[i] = s[j];
		s[j] = x;
		res.push(String.fromCharCode(arr[y] ^ s[(s[i] + s[j]) % 256]));
	}

	if(bool) {
	    return(bin2String(res));
	} else {
    	return(res);
    }
}
```
## First Key
We need to know the `key` the decrypt the all of the flag key

Trace where is the key parameter:
```java
 Object object = importerTopLevel.get("process", importerTopLevel);
      Singleton singleton1 = this.singleton;
      try {
        String str1 = singleton1.keykey(paramInt);
        if (object instanceof Function)
          object1 = ((Function)object).call(context, importerTopLevel, importerTopLevel, new Object[] { str1, arrayOfByte1 }); 
```
It should be the `str1`, which is `singleton1.keykey(paramInt)`

Lets see the source of `keykey` function:
```java
public String keykey(int paramInt) {
    String str = null;
    if (paramInt != 0) {
      if (paramInt != 1) {
        if (paramInt != 2) {
          if (paramInt != 3) {
            if (paramInt == 4)
              str = this.flagkey4_key; 
          } else {
            str = this.flagkey3_key;
          } 
        } else {
          str = this.flagkey2_key;
        } 
      } else {
        str = this.flagkey1_key;
      } 
    } else {
      str = this.flagkey0_key;
    } 
    return str;
  }
```
Then I tried to find the where it initialize the `flagkey` by using `grep -r "flagkey0_key" ./`command

I found the `flagkey0_key` in `KEY0HintActivity` class:
```java
singleton.flagkey0_key = singleton.hintkey0;
```
Therefore, we need to find the `hintkey0`

Found it at the Singleton constructor function:
```java
private Singleton() {
    try {
      ContextFactoryFactory contextFactoryFactory = new ContextFactoryFactory();
      this();
      this.factory = contextFactoryFactory;
      this.hintkey0 = contextFactoryFactory.CreateKey(0);
      this.hintkeymain = this.factory.CreateKey(1);
      this.key4_box = new String[] { "", "", "" };
      this.box_index = 0;
    } catch (Exception exception) {}
}
```
Notice the `contextFactoryFactory` is in different module:
```java
import com.trendmicro.hippo.ContextFactoryFactory;
```

Found the key at the `ContextFactoryFactory` file!!
```java
private String hintkey0 = new String("TrendMicro");
```
Then I use Python to decrypt the first key:
```py
from arc4 import ARC4
enc = open("assets/key_0/key_0.enc",'rb').read()
arc4 = ARC4('TrendMicro')
print(arc4.encrypt(enc))
```
Result:
```
b'KEY0-7135446200'
```
Yeah!! We found the first key!

## Second Key
Then how we decrypt the `ciphertext.js`?

I tried the first key did not work, but the keykey:`TrendMicro` works!
```js
function entrypoint(key, ciphertext_bytes) {
   Log(path_ciphertext + ":entrypoint()");

    titleView.setText("Key Zero Hints");
    textView.setText(
    " First off -- good luck! As a reward for playing, Key Zero has been decrypted for you.\n\nNow for the hints:\n\n" +
    "1. The 'Flag' is stored in this APK file. The flag is encrypted. When decrypted, it contains a string matching the regular expression '^TMCTF\{[A-Za-z0-9]*\}$'" + "\n\n" +
    "2. There are five 'Flag Keys.' All five Flag Keys are used to decrypt the Flag. Keys match /^KEY[0-4]-.[0-9]+/" + "\n\n" +
    "3. To decrypt the five Flag Keys you must solve five levels." + "\n\n" +
    "4. Each level has its own hint, located in the following files. The hints are encrypted. The hints for Key Zero were decrypted for you. You must figure out the other four:" + "\n" +
    "      key_0/ciphertext.js (already decrypted)" + "\n" +
    "      key_1/ciphertext.js" + "\n" +
    "      key_2/ciphertext.js" + "\n" +
    "      key_3/ciphertext.js" + "\n" +
    "      key_4/ciphertext.js" + "\n" +
    "\n" +
    /* the above ciphertext.js files are ALL encrypted using the same password */
    "5. The file assets/_preload/code.js is included before every executed javascript file." + "\n\n" +
    "6. There are a number of ways to decompile the APK file. You should do that." + "\n\n" +
    "7. Android intents can unlock many of the hints and keys.\n\n" +
    "8. Use the Android Studio Android Emulator to run the challenge. It's the simplest way." + "\n\n\n\n\n\n\n" +
    "https://www.trendmicro.com/en_us/contact.html"
    );

    return(true);
}

function verify_unlocked() {
    return(true);
}
```
Saw the comment mention all `ciphertext.js` is encrypted with the same password!!

So we can decrypt all and save using Python:
```py
from arc4 import ARC4

for i in range(5):
	enc = open(f"assets/key_{i}/ciphertext.js",'rb').read()
	arc4 = ARC4('TrendMicro')
	open(f"hint{i}.js","wb").write(arc4.encrypt(enc))
```
The hint for second key is:
```
"To unlock Key 1, you must call Trend Micro"
```
I guess it means we need to make a phone call to the Trend Micro company

By searching the `flagkey1_key`, found it at `Unlocker` file:
```java
else if (str.equals("android.intent.action.PHONE_STATE")) {
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
```
The regular expression `[^\\d.]` find all characters except number
, so it just delete all non-number characters

Therefore, the key of the second key should be one of the Trend Micro contact

It is Japan company then I first the [Japan number first](https://www.trendmicro.com/en_us/contact.html#t6):
```py
enc = open("assets/key_1/key_1.enc",'rb').read()
arc4 = ARC4('81353343618')
print(arc4.encrypt(enc))
```
It works! We found the second key!!
```
b'KEY1-1047645455'
```

## Third Key
The hint for third key is:
```
To unlock KEY2, send the secret code.
```
Found the `flagkey2_key` at the Unlocker also:
```java
public void onReceive(Context paramContext, Intent paramIntent) {
    Intent intent;
    Singleton singleton = Singleton.getInstance();
    str = paramIntent.getAction();
    if (str.equals("android.provider.Telephony.SECRET_CODE")) {
      null = paramIntent.getData().getHost();
      if (null.equals("8736364276")) {
        Singleton.getInstance(Boolean.valueOf(true));
      } else {
        singleton.flagkey2_key = null;
      } 
      intent = new Intent();
      intent.setClassName("com.trendmicro.keybox", "com.trendmicro.keybox.KeyboxMainActivity");
      intent.setFlags(268435456);
      paramContext.startActivity(intent);
    } 
```
Can see the key should be `8736364276`?

But when I tried it did not work :(

Then I stuck for awhile..

I look thought the `AndroidManifest.xml` file, then I saw `android_secret_code` ,`8736364275`,`
87363642769`

Searching online for the `SECRET_CODE` thing, the secret_code is stored in the AndroidManifest.xml 

[Android Secret Codes](https://simonmarquis.github.io/Android-SecretCodes/)

It maybe the password I guess

It works for `8736364275`!! Nice
```
b'KEY2-9517232028'
```

## Forth Key
The hint for forth key is:
```
Unlock KEY3 with the right text message
```
Found `flagkey3_key` at `Observer` class:
```java
public void onChange(boolean paramBoolean) {
  if (this.context == null)
    return; 
  try {
    ContextFactoryFactory contextFactoryFactory = new ContextFactoryFactory();
    this();
    this.factory = contextFactoryFactory;
    this.uri = Uri.parse(contextFactoryFactory.resolverFactory());
  } catch (Exception exception) {}
  super.onChange(paramBoolean);
  try {
    Cursor cursor1 = this.context.getContentResolver().query(this.uri, null, null, null, null);
    this.cursor = cursor1;
    cursor1.moveToFirst();
    for (byte b = 0; b < this.cursor.getColumnCount(); b++) {
      String.format("%s == %s", new Object[] { this.cursor.getColumnName(b), this.cursor.getString(b) });
      if (this.cursor.getString(b) != null && this.cursor.getColumnName(b) != null && this.cursor.getString(b).equals(this.cursor.getColumnName(b)) && this.cursor.getInt(this.cursor.getColumnIndex("type")) == 1) {
        String str1 = this.cursor.getString(b);
        this.message = str1;
        this.singleton.flagkey3_key = str1;
        Intent intent = new Intent();
        this();
        intent.setClassName("com.trendmicro.keybox", "com.trendmicro.keybox.KeyboxMainActivity");
        intent.setFlags(268435456);
        this.context.startActivity(intent);
      } 
      String str = new String();
      this();
      this.message = str;
    } 
    this.cursor.close();
  } catch (Exception exception) {
    Log.d("TMCTF", String.format("SMS observer error: %s", new Object[] { exception.getLocalizedMessage() }));
  } 
}
```
I know it should related to SMS, notice the `contextFactoryFactory.resolverFactory()` is `content://sms`
```java
public String contextURL = new String("content://sms");
public String resolverFactory() { return this.contextURL; }
```
And I search for the `Cursor` class in android, it just a table in a database

`cursor.getString(b)` is get the column value at index b, `cursor.getColumnName(b)` is get the column name at index b

Notice the statement in the if condition:
```java
this.cursor.getString(b).equals(this.cursor.getColumnName(b))
```
It means the column name must equal to column value

Then it put the column value into the forth key:
```java
String str1 = this.cursor.getString(b);
this.message = str1;
this.singleton.flagkey3_key = str1;
```
We can just brute force all the column name, can look the column name at the [Android Documentation](https://developer.android.com/reference/android/provider/Telephony.TextBasedSmsColumns)

Column name `body` is the key for the forth key!!
```
b'KEY3-2510789910'
```

## Fifth Key
The hint for fifth key was quite long:
```js
// These coordinates are considered the ''canonical'' GPS coordinates of the
// Trend Micro offices for purposes of this competition
// Please forgive me for converting some names (mostly Brazil) - it made things simpler
// for your humble CTF creator :)

//
var Locations = [
  { location: "Amsterdam", latitude:52.3084375, longitude:4.9426875, hash: "2dce283e0e268a42d62e34467db274c9c38c358f" },
  { location: "Aukland", latitude:-36.842294, longitude:174.756654, hash: "794a4f25478d31bf87ece956fc7c95466a27c06a" },
  { location: "Austin", latitude:30.3981626, longitude:-97.7203696, hash: "5b06f1f08503b4e6346926667d318f0f9d7e9fd1" },
  { location: "Bangalore", latitude:12.9735089, longitude:77.6164228, hash: "91f6fcb18482cc6fe303108f4283180b4fa20599" },
  ...
  ...
  ...
];


function entrypoint(key, ciphertext_bytes) {
    Log(path_ciphertext + ":entrypoint()");

    var location_match = '';

    for(var index=0 ; index < Locations.length; index++) {
        location = Locations[index];
        var location_latitude = parseFloat(location.latitude);
        var location_longitude = parseFloat(location.longitude);

        var singleton_latitude = parseFloat(singleton.latitude);
        var singleton_longitude = parseFloat(singleton.longitude);

        // why actually calculate distance when you can just fake it ;)
        if(Math.abs(location_latitude - singleton_latitude) < 0.001 && Math.abs(location_longitude - singleton_longitude) < 0.001 ) {
            Log("Matched Location " + location.location);
            var SHA1 = new Hashes.SHA1;
            hash = SHA1.hex(location.location)
            if( hash == location.hash) {
                location_match = "Welcome to " + location.location;
            } else {
                location_match = "Welcome to " + location.location;
                singleton.push(location.hash);
            }
            break;
        } else {
            location_match = ""
        }
    }


    titleView.setText("Key Four Hints");
    textView.setText(
    "Visit " + /* all three of */ "the headquarters to unlock Key 4" + "\n\n" +
    location_match
    );

    return(true)
}
```
The main point of the hint is these lines:
```js
var SHA1 = new Hashes.SHA1;
hash = SHA1.hex(location.location)
if( hash == location.hash) {
    location_match = "Welcome to " + location.location;
} else {
    location_match = "Welcome to " + location.location;
    singleton.push(location.hash);
}
```
Only if the hash is not equal the location hash, then it execute `singleton.push(location.hash)`

Check the method:
```js
public void push(String paramString) {
  if (!paramString.equals(this.key4_box[2])) {
    String[] arrayOfString = this.key4_box;
    arrayOfString[0] = arrayOfString[1];
    arrayOfString[1] = arrayOfString[2];
    arrayOfString[2] = paramString;
    this.flagkey4_key = String.join("", (CharSequence[])arrayOfString);
  } 
  try {
    paramString = String.format("key_%d/key_%d.enc", new Object[] { Integer.valueOf(4), Integer.valueOf(4) });
    InputStream inputStream = this.androidContextObject.getAssets().open(paramString);
    byte[] arrayOfByte = new byte[inputStream.available()];
    inputStream.read(arrayOfByte);
    Oracle oracle = new Oracle();
    this(this.flagkey4_key.getBytes());
    String str = new String();
    this(oracle.process(arrayOfByte));
    if (str.matches("KEY4-.*")) {
      Intent intent = new Intent();
      this();
      intent.setClassName("com.trendmicro.keybox", "com.trendmicro.keybox.KeyboxMainActivity");
      intent.setFlags(268435456);
      this.androidContextObject.startActivity(intent);
    } 
  } catch (Exception exception) {}
}
```
The main code is here:
```java
if (!paramString.equals(this.key4_box[2])) {
    String[] arrayOfString = this.key4_box;
    arrayOfString[0] = arrayOfString[1];
    arrayOfString[1] = arrayOfString[2];
    arrayOfString[2] = paramString;
    this.flagkey4_key = String.join("", (CharSequence[])arrayOfString);
} 
```
Therefore, we need find three hashes that are incorrect, join them together is the key of fifth key

I used Python to find the three hashes:
```py
city = {
"Amsterdam" : "2dce283e0e268a42d62e34467db274c9c38c358f" ,
"Aukland" : "794a4f25478d31bf87ece956fc7c95466a27c06a" ,
"Austin" : "5b06f1f08503b4e6346926667d318f0f9d7e9fd1" ,
"Bangalore" : "91f6fcb18482cc6fe303108f4283180b4fa20599" ,
...
...
... }
key4 = "" 
for k,v in city.items():
    if hashlib.sha1(k.encode()).hexdigest() != v:
        key4 = v + key4
enc = open("assets/key_4/key_4.enc",'rb').read()
arc4 = ARC4(key4)
print(arc4.encrypt(enc))
```
And we found the fifth key!! We can decrypt the flag now!!
```
b'KEY4-4721296569'
```
## Flag
According to the `FlagActivity` and the hint is the flag folder:
```js
function decrypt_flag() {
     Log("DECRYPT THE FLAG()");
     // flag=bin2String(process(key0,process(key1,process(key2,process(key3,process(key4, flag_ciphertext))))));
     Log("KEY0" + key0);
     Log("KEY1" + key1);
     Log("KEY2" + key2);
     Log("KEY3" + key3);
     Log("KEY4" + key4);
     Log("FLAG " + flag);
     // return(flag);
 }
 function verify_unlocked() {
    return(true);
 }
```
We need to decrypt the flag recursively:
```py
flag = open("assets/flag/flag.enc","rb").read()
for i in range(5):
  arc4 = ARC4(key[i])
  flag = arc4.encrypt(flag)
print(flag)
```
Thats it!! Longest analysis for a CTF challenge!
```
TMCTF{pzDbkfWGcE}
```
[Full Python Script](solve.py)