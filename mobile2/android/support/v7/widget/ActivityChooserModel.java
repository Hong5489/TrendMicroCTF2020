package android.support.v7.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.DataSetObservable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

class ActivityChooserModel extends DataSetObservable {
  static final String ATTRIBUTE_ACTIVITY = "activity";
  
  static final String ATTRIBUTE_TIME = "time";
  
  static final String ATTRIBUTE_WEIGHT = "weight";
  
  static final boolean DEBUG = false;
  
  private static final int DEFAULT_ACTIVITY_INFLATION = 5;
  
  private static final float DEFAULT_HISTORICAL_RECORD_WEIGHT = 1.0F;
  
  public static final String DEFAULT_HISTORY_FILE_NAME = "activity_choser_model_history.xml";
  
  public static final int DEFAULT_HISTORY_MAX_LENGTH = 50;
  
  private static final String HISTORY_FILE_EXTENSION = ".xml";
  
  private static final int INVALID_INDEX = -1;
  
  static final String LOG_TAG = ActivityChooserModel.class.getSimpleName();
  
  static final String TAG_HISTORICAL_RECORD = "historical-record";
  
  static final String TAG_HISTORICAL_RECORDS = "historical-records";
  
  private static final Map<String, ActivityChooserModel> sDataModelRegistry;
  
  private static final Object sRegistryLock = new Object();
  
  private final List<ActivityResolveInfo> mActivities = new ArrayList<>();
  
  private OnChooseActivityListener mActivityChoserModelPolicy;
  
  private ActivitySorter mActivitySorter = new DefaultSorter();
  
  boolean mCanReadHistoricalData = true;
  
  final Context mContext;
  
  private final List<HistoricalRecord> mHistoricalRecords = new ArrayList<>();
  
  private boolean mHistoricalRecordsChanged = true;
  
  final String mHistoryFileName;
  
  private int mHistoryMaxSize = 50;
  
  private final Object mInstanceLock = new Object();
  
  private Intent mIntent;
  
  private boolean mReadShareHistoryCalled = false;
  
  private boolean mReloadActivities = false;
  
  static {
    sDataModelRegistry = new HashMap<>();
  }
  
  private ActivityChooserModel(Context paramContext, String paramString) {
    this.mContext = paramContext.getApplicationContext();
    if (!TextUtils.isEmpty(paramString) && !paramString.endsWith(".xml")) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramString);
      stringBuilder.append(".xml");
      this.mHistoryFileName = stringBuilder.toString();
    } else {
      this.mHistoryFileName = paramString;
    } 
  }
  
  private boolean addHistoricalRecord(HistoricalRecord paramHistoricalRecord) {
    boolean bool = this.mHistoricalRecords.add(paramHistoricalRecord);
    if (bool) {
      this.mHistoricalRecordsChanged = true;
      pruneExcessiveHistoricalRecordsIfNeeded();
      persistHistoricalDataIfNeeded();
      sortActivitiesIfNeeded();
      notifyChanged();
    } 
    return bool;
  }
  
  private void ensureConsistentState() {
    boolean bool1 = loadActivitiesIfNeeded();
    boolean bool2 = readHistoricalDataIfNeeded();
    pruneExcessiveHistoricalRecordsIfNeeded();
    if (bool1 | bool2) {
      sortActivitiesIfNeeded();
      notifyChanged();
    } 
  }
  
  public static ActivityChooserModel get(Context paramContext, String paramString) {
    synchronized (sRegistryLock) {
      ActivityChooserModel activityChooserModel1 = sDataModelRegistry.get(paramString);
      ActivityChooserModel activityChooserModel2 = activityChooserModel1;
      if (activityChooserModel1 == null) {
        activityChooserModel2 = new ActivityChooserModel();
        this(paramContext, paramString);
        sDataModelRegistry.put(paramString, activityChooserModel2);
      } 
      return activityChooserModel2;
    } 
  }
  
  private boolean loadActivitiesIfNeeded() {
    if (this.mReloadActivities && this.mIntent != null) {
      this.mReloadActivities = false;
      this.mActivities.clear();
      List<ResolveInfo> list = this.mContext.getPackageManager().queryIntentActivities(this.mIntent, 0);
      int i = list.size();
      for (byte b = 0; b < i; b++) {
        ResolveInfo resolveInfo = list.get(b);
        this.mActivities.add(new ActivityResolveInfo(resolveInfo));
      } 
      return true;
    } 
    return false;
  }
  
  private void persistHistoricalDataIfNeeded() {
    if (this.mReadShareHistoryCalled) {
      if (!this.mHistoricalRecordsChanged)
        return; 
      this.mHistoricalRecordsChanged = false;
      if (!TextUtils.isEmpty(this.mHistoryFileName))
        (new PersistHistoryAsyncTask()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[] { new ArrayList<>(this.mHistoricalRecords), this.mHistoryFileName }); 
      return;
    } 
    throw new IllegalStateException("No preceding call to #readHistoricalData");
  }
  
  private void pruneExcessiveHistoricalRecordsIfNeeded() {
    int i = this.mHistoricalRecords.size() - this.mHistoryMaxSize;
    if (i <= 0)
      return; 
    this.mHistoricalRecordsChanged = true;
    for (byte b = 0; b < i; b++)
      HistoricalRecord historicalRecord = this.mHistoricalRecords.remove(0); 
  }
  
  private boolean readHistoricalDataIfNeeded() {
    if (this.mCanReadHistoricalData && this.mHistoricalRecordsChanged && !TextUtils.isEmpty(this.mHistoryFileName)) {
      this.mCanReadHistoricalData = false;
      this.mReadShareHistoryCalled = true;
      readHistoricalDataImpl();
      return true;
    } 
    return false;
  }
  
  private void readHistoricalDataImpl() {
    try {
      FileInputStream fileInputStream = this.mContext.openFileInput(this.mHistoryFileName);
      try {
        XmlPullParser xmlPullParser = Xml.newPullParser();
        xmlPullParser.setInput(fileInputStream, "UTF-8");
        int i;
        for (i = 0; i != 1 && i != 2; i = xmlPullParser.next());
        if ("historical-records".equals(xmlPullParser.getName())) {
          List<HistoricalRecord> list = this.mHistoricalRecords;
          list.clear();
          while (true) {
            i = xmlPullParser.next();
            if (i == 1) {
              if (fileInputStream != null) {
                try {
                  fileInputStream.close();
                  break;
                } catch (IOException iOException) {}
                return;
              } 
              break;
            } 
            if (i == 3 || i == 4)
              continue; 
            if ("historical-record".equals(xmlPullParser.getName())) {
              String str = xmlPullParser.getAttributeValue(null, "activity");
              long l = Long.parseLong(xmlPullParser.getAttributeValue(null, "time"));
              float f = Float.parseFloat(xmlPullParser.getAttributeValue(null, "weight"));
              HistoricalRecord historicalRecord = new HistoricalRecord();
              this(str, l, f);
              list.add(historicalRecord);
              continue;
            } 
            XmlPullParserException xmlPullParserException = new XmlPullParserException();
            this("Share records file not well-formed.");
            throw xmlPullParserException;
          } 
        } else {
          XmlPullParserException xmlPullParserException = new XmlPullParserException();
          this("Share records file does not start with historical-records tag.");
          throw xmlPullParserException;
        } 
      } catch (XmlPullParserException xmlPullParserException) {
        String str = LOG_TAG;
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append("Error reading historical recrod file: ");
        stringBuilder.append(this.mHistoryFileName);
        Log.e(str, stringBuilder.toString(), (Throwable)xmlPullParserException);
        if (iOException != null)
          iOException.close(); 
      } catch (IOException iOException1) {
        String str = LOG_TAG;
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append("Error reading historical recrod file: ");
        stringBuilder.append(this.mHistoryFileName);
        Log.e(str, stringBuilder.toString(), iOException1);
        if (iOException != null)
          iOException.close(); 
      } finally {
        Exception exception;
      } 
      return;
    } catch (FileNotFoundException fileNotFoundException) {
      return;
    } 
  }
  
  private boolean sortActivitiesIfNeeded() {
    if (this.mActivitySorter != null && this.mIntent != null && !this.mActivities.isEmpty() && !this.mHistoricalRecords.isEmpty()) {
      this.mActivitySorter.sort(this.mIntent, this.mActivities, Collections.unmodifiableList(this.mHistoricalRecords));
      return true;
    } 
    return false;
  }
  
  public Intent chooseActivity(int paramInt) {
    synchronized (this.mInstanceLock) {
      if (this.mIntent == null)
        return null; 
      ensureConsistentState();
      ActivityResolveInfo activityResolveInfo = this.mActivities.get(paramInt);
      ComponentName componentName = new ComponentName();
      this(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name);
      Intent intent = new Intent();
      this(this.mIntent);
      intent.setComponent(componentName);
      if (this.mActivityChoserModelPolicy != null) {
        Intent intent1 = new Intent();
        this(intent);
        if (this.mActivityChoserModelPolicy.onChooseActivity(this, intent1))
          return null; 
      } 
      HistoricalRecord historicalRecord = new HistoricalRecord();
      this(componentName, System.currentTimeMillis(), 1.0F);
      addHistoricalRecord(historicalRecord);
      return intent;
    } 
  }
  
  public ResolveInfo getActivity(int paramInt) {
    synchronized (this.mInstanceLock) {
      ensureConsistentState();
      return ((ActivityResolveInfo)this.mActivities.get(paramInt)).resolveInfo;
    } 
  }
  
  public int getActivityCount() {
    synchronized (this.mInstanceLock) {
      ensureConsistentState();
      return this.mActivities.size();
    } 
  }
  
  public int getActivityIndex(ResolveInfo paramResolveInfo) {
    synchronized (this.mInstanceLock) {
      ensureConsistentState();
      List<ActivityResolveInfo> list = this.mActivities;
      int i = list.size();
      for (byte b = 0; b < i; b++) {
        if (((ActivityResolveInfo)list.get(b)).resolveInfo == paramResolveInfo)
          return b; 
      } 
      return -1;
    } 
  }
  
  public ResolveInfo getDefaultActivity() {
    synchronized (this.mInstanceLock) {
      ensureConsistentState();
      if (!this.mActivities.isEmpty())
        return ((ActivityResolveInfo)this.mActivities.get(0)).resolveInfo; 
      return null;
    } 
  }
  
  public int getHistoryMaxSize() {
    synchronized (this.mInstanceLock) {
      return this.mHistoryMaxSize;
    } 
  }
  
  public int getHistorySize() {
    synchronized (this.mInstanceLock) {
      ensureConsistentState();
      return this.mHistoricalRecords.size();
    } 
  }
  
  public Intent getIntent() {
    synchronized (this.mInstanceLock) {
      return this.mIntent;
    } 
  }
  
  public void setActivitySorter(ActivitySorter paramActivitySorter) {
    synchronized (this.mInstanceLock) {
      if (this.mActivitySorter == paramActivitySorter)
        return; 
      this.mActivitySorter = paramActivitySorter;
      if (sortActivitiesIfNeeded())
        notifyChanged(); 
      return;
    } 
  }
  
  public void setDefaultActivity(int paramInt) {
    synchronized (this.mInstanceLock) {
      float f;
      ensureConsistentState();
      ActivityResolveInfo activityResolveInfo1 = this.mActivities.get(paramInt);
      ActivityResolveInfo activityResolveInfo2 = this.mActivities.get(0);
      if (activityResolveInfo2 != null) {
        f = activityResolveInfo2.weight - activityResolveInfo1.weight + 5.0F;
      } else {
        f = 1.0F;
      } 
      ComponentName componentName = new ComponentName();
      this(activityResolveInfo1.resolveInfo.activityInfo.packageName, activityResolveInfo1.resolveInfo.activityInfo.name);
      HistoricalRecord historicalRecord = new HistoricalRecord();
      this(componentName, System.currentTimeMillis(), f);
      addHistoricalRecord(historicalRecord);
      return;
    } 
  }
  
  public void setHistoryMaxSize(int paramInt) {
    synchronized (this.mInstanceLock) {
      if (this.mHistoryMaxSize == paramInt)
        return; 
      this.mHistoryMaxSize = paramInt;
      pruneExcessiveHistoricalRecordsIfNeeded();
      if (sortActivitiesIfNeeded())
        notifyChanged(); 
      return;
    } 
  }
  
  public void setIntent(Intent paramIntent) {
    synchronized (this.mInstanceLock) {
      if (this.mIntent == paramIntent)
        return; 
      this.mIntent = paramIntent;
      this.mReloadActivities = true;
      ensureConsistentState();
      return;
    } 
  }
  
  public void setOnChooseActivityListener(OnChooseActivityListener paramOnChooseActivityListener) {
    synchronized (this.mInstanceLock) {
      this.mActivityChoserModelPolicy = paramOnChooseActivityListener;
      return;
    } 
  }
  
  public static interface ActivityChooserModelClient {
    void setActivityChooserModel(ActivityChooserModel param1ActivityChooserModel);
  }
  
  public static final class ActivityResolveInfo implements Comparable<ActivityResolveInfo> {
    public final ResolveInfo resolveInfo;
    
    public float weight;
    
    public ActivityResolveInfo(ResolveInfo param1ResolveInfo) {
      this.resolveInfo = param1ResolveInfo;
    }
    
    public int compareTo(ActivityResolveInfo param1ActivityResolveInfo) {
      return Float.floatToIntBits(param1ActivityResolveInfo.weight) - Float.floatToIntBits(this.weight);
    }
    
    public boolean equals(Object param1Object) {
      if (this == param1Object)
        return true; 
      if (param1Object == null)
        return false; 
      if (getClass() != param1Object.getClass())
        return false; 
      param1Object = param1Object;
      return !(Float.floatToIntBits(this.weight) != Float.floatToIntBits(((ActivityResolveInfo)param1Object).weight));
    }
    
    public int hashCode() {
      return Float.floatToIntBits(this.weight) + 31;
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("[");
      stringBuilder.append("resolveInfo:");
      stringBuilder.append(this.resolveInfo.toString());
      stringBuilder.append("; weight:");
      stringBuilder.append(new BigDecimal(this.weight));
      stringBuilder.append("]");
      return stringBuilder.toString();
    }
  }
  
  public static interface ActivitySorter {
    void sort(Intent param1Intent, List<ActivityChooserModel.ActivityResolveInfo> param1List, List<ActivityChooserModel.HistoricalRecord> param1List1);
  }
  
  private static final class DefaultSorter implements ActivitySorter {
    private static final float WEIGHT_DECAY_COEFFICIENT = 0.95F;
    
    private final Map<ComponentName, ActivityChooserModel.ActivityResolveInfo> mPackageNameToActivityMap = new HashMap<>();
    
    public void sort(Intent param1Intent, List<ActivityChooserModel.ActivityResolveInfo> param1List, List<ActivityChooserModel.HistoricalRecord> param1List1) {
      Map<ComponentName, ActivityChooserModel.ActivityResolveInfo> map = this.mPackageNameToActivityMap;
      map.clear();
      int i = param1List.size();
      int j;
      for (j = 0; j < i; j++) {
        ActivityChooserModel.ActivityResolveInfo activityResolveInfo = param1List.get(j);
        activityResolveInfo.weight = 0.0F;
        map.put(new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name), activityResolveInfo);
      } 
      j = param1List1.size();
      float f;
      for (f = 1.0F; --j >= 0; f = f1) {
        ActivityChooserModel.HistoricalRecord historicalRecord = param1List1.get(j);
        ActivityChooserModel.ActivityResolveInfo activityResolveInfo = map.get(historicalRecord.activity);
        float f1 = f;
        if (activityResolveInfo != null) {
          activityResolveInfo.weight += historicalRecord.weight * f;
          f1 = f * 0.95F;
        } 
        j--;
      } 
      Collections.sort(param1List);
    }
  }
  
  public static final class HistoricalRecord {
    public final ComponentName activity;
    
    public final long time;
    
    public final float weight;
    
    public HistoricalRecord(ComponentName param1ComponentName, long param1Long, float param1Float) {
      this.activity = param1ComponentName;
      this.time = param1Long;
      this.weight = param1Float;
    }
    
    public HistoricalRecord(String param1String, long param1Long, float param1Float) {
      this(ComponentName.unflattenFromString(param1String), param1Long, param1Float);
    }
    
    public boolean equals(Object param1Object) {
      if (this == param1Object)
        return true; 
      if (param1Object == null)
        return false; 
      if (getClass() != param1Object.getClass())
        return false; 
      param1Object = param1Object;
      ComponentName componentName = this.activity;
      if (componentName == null) {
        if (((HistoricalRecord)param1Object).activity != null)
          return false; 
      } else if (!componentName.equals(((HistoricalRecord)param1Object).activity)) {
        return false;
      } 
      return (this.time != ((HistoricalRecord)param1Object).time) ? false : (!(Float.floatToIntBits(this.weight) != Float.floatToIntBits(((HistoricalRecord)param1Object).weight)));
    }
    
    public int hashCode() {
      int i;
      ComponentName componentName = this.activity;
      if (componentName == null) {
        i = 0;
      } else {
        i = componentName.hashCode();
      } 
      long l = this.time;
      return ((1 * 31 + i) * 31 + (int)(l ^ l >>> 32L)) * 31 + Float.floatToIntBits(this.weight);
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("[");
      stringBuilder.append("; activity:");
      stringBuilder.append(this.activity);
      stringBuilder.append("; time:");
      stringBuilder.append(this.time);
      stringBuilder.append("; weight:");
      stringBuilder.append(new BigDecimal(this.weight));
      stringBuilder.append("]");
      return stringBuilder.toString();
    }
  }
  
  public static interface OnChooseActivityListener {
    boolean onChooseActivity(ActivityChooserModel param1ActivityChooserModel, Intent param1Intent);
  }
  
  private final class PersistHistoryAsyncTask extends AsyncTask<Object, Void, Void> {
    public Void doInBackground(Object... param1VarArgs) {
      // Byte code:
      //   0: aload_1
      //   1: iconst_0
      //   2: aaload
      //   3: checkcast java/util/List
      //   6: astore_2
      //   7: aload_1
      //   8: iconst_1
      //   9: aaload
      //   10: checkcast java/lang/String
      //   13: astore_1
      //   14: aload_0
      //   15: getfield this$0 : Landroid/support/v7/widget/ActivityChooserModel;
      //   18: getfield mContext : Landroid/content/Context;
      //   21: aload_1
      //   22: iconst_0
      //   23: invokevirtual openFileOutput : (Ljava/lang/String;I)Ljava/io/FileOutputStream;
      //   26: astore_3
      //   27: invokestatic newSerializer : ()Lorg/xmlpull/v1/XmlSerializer;
      //   30: astore #4
      //   32: aload_2
      //   33: astore #5
      //   35: aload_2
      //   36: astore #5
      //   38: aload_2
      //   39: astore #5
      //   41: aload_2
      //   42: astore #5
      //   44: aload #4
      //   46: aload_3
      //   47: aconst_null
      //   48: invokeinterface setOutput : (Ljava/io/OutputStream;Ljava/lang/String;)V
      //   53: aload_2
      //   54: astore #5
      //   56: aload_2
      //   57: astore #5
      //   59: aload_2
      //   60: astore #5
      //   62: aload_2
      //   63: astore #5
      //   65: aload #4
      //   67: ldc 'UTF-8'
      //   69: iconst_1
      //   70: invokestatic valueOf : (Z)Ljava/lang/Boolean;
      //   73: invokeinterface startDocument : (Ljava/lang/String;Ljava/lang/Boolean;)V
      //   78: aload_2
      //   79: astore #5
      //   81: aload_2
      //   82: astore #5
      //   84: aload_2
      //   85: astore #5
      //   87: aload_2
      //   88: astore #5
      //   90: aload #4
      //   92: aconst_null
      //   93: ldc 'historical-records'
      //   95: invokeinterface startTag : (Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
      //   100: pop
      //   101: aload_2
      //   102: astore #5
      //   104: aload_2
      //   105: astore #5
      //   107: aload_2
      //   108: astore #5
      //   110: aload_2
      //   111: astore #5
      //   113: aload_2
      //   114: invokeinterface size : ()I
      //   119: istore #6
      //   121: iconst_0
      //   122: istore #7
      //   124: aload_2
      //   125: astore_1
      //   126: iload #7
      //   128: iload #6
      //   130: if_icmpge -> 262
      //   133: aload_1
      //   134: astore #5
      //   136: aload_1
      //   137: astore #5
      //   139: aload_1
      //   140: astore #5
      //   142: aload_1
      //   143: astore #5
      //   145: aload_1
      //   146: iconst_0
      //   147: invokeinterface remove : (I)Ljava/lang/Object;
      //   152: checkcast android/support/v7/widget/ActivityChooserModel$HistoricalRecord
      //   155: astore_2
      //   156: aload_1
      //   157: astore #5
      //   159: aload_1
      //   160: astore #5
      //   162: aload_1
      //   163: astore #5
      //   165: aload_1
      //   166: astore #5
      //   168: aload #4
      //   170: aconst_null
      //   171: ldc 'historical-record'
      //   173: invokeinterface startTag : (Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
      //   178: pop
      //   179: aload_1
      //   180: astore #5
      //   182: aload_1
      //   183: astore #5
      //   185: aload_1
      //   186: astore #5
      //   188: aload_1
      //   189: astore #5
      //   191: aload #4
      //   193: aconst_null
      //   194: ldc 'activity'
      //   196: aload_2
      //   197: getfield activity : Landroid/content/ComponentName;
      //   200: invokevirtual flattenToString : ()Ljava/lang/String;
      //   203: invokeinterface attribute : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
      //   208: pop
      //   209: aload #4
      //   211: aconst_null
      //   212: ldc 'time'
      //   214: aload_2
      //   215: getfield time : J
      //   218: invokestatic valueOf : (J)Ljava/lang/String;
      //   221: invokeinterface attribute : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
      //   226: pop
      //   227: aload #4
      //   229: aconst_null
      //   230: ldc 'weight'
      //   232: aload_2
      //   233: getfield weight : F
      //   236: invokestatic valueOf : (F)Ljava/lang/String;
      //   239: invokeinterface attribute : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
      //   244: pop
      //   245: aload #4
      //   247: aconst_null
      //   248: ldc 'historical-record'
      //   250: invokeinterface endTag : (Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
      //   255: pop
      //   256: iinc #7, 1
      //   259: goto -> 126
      //   262: aload #4
      //   264: aconst_null
      //   265: ldc 'historical-records'
      //   267: invokeinterface endTag : (Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
      //   272: pop
      //   273: aload #4
      //   275: invokeinterface endDocument : ()V
      //   280: aload_0
      //   281: getfield this$0 : Landroid/support/v7/widget/ActivityChooserModel;
      //   284: iconst_1
      //   285: putfield mCanReadHistoricalData : Z
      //   288: aload_3
      //   289: ifnull -> 514
      //   292: aload_3
      //   293: invokevirtual close : ()V
      //   296: goto -> 507
      //   299: astore_1
      //   300: goto -> 316
      //   303: astore_1
      //   304: goto -> 382
      //   307: astore_1
      //   308: goto -> 445
      //   311: astore_1
      //   312: goto -> 517
      //   315: astore_1
      //   316: getstatic android/support/v7/widget/ActivityChooserModel.LOG_TAG : Ljava/lang/String;
      //   319: astore_2
      //   320: new java/lang/StringBuilder
      //   323: astore #5
      //   325: aload #5
      //   327: invokespecial <init> : ()V
      //   330: aload #5
      //   332: ldc 'Error writing historical record file: '
      //   334: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   337: pop
      //   338: aload #5
      //   340: aload_0
      //   341: getfield this$0 : Landroid/support/v7/widget/ActivityChooserModel;
      //   344: getfield mHistoryFileName : Ljava/lang/String;
      //   347: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   350: pop
      //   351: aload_2
      //   352: aload #5
      //   354: invokevirtual toString : ()Ljava/lang/String;
      //   357: aload_1
      //   358: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   361: pop
      //   362: aload_0
      //   363: getfield this$0 : Landroid/support/v7/widget/ActivityChooserModel;
      //   366: iconst_1
      //   367: putfield mCanReadHistoricalData : Z
      //   370: aload_3
      //   371: ifnull -> 514
      //   374: aload_3
      //   375: invokevirtual close : ()V
      //   378: goto -> 507
      //   381: astore_1
      //   382: getstatic android/support/v7/widget/ActivityChooserModel.LOG_TAG : Ljava/lang/String;
      //   385: astore #5
      //   387: new java/lang/StringBuilder
      //   390: astore_2
      //   391: aload_2
      //   392: invokespecial <init> : ()V
      //   395: aload_2
      //   396: ldc 'Error writing historical record file: '
      //   398: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   401: pop
      //   402: aload_2
      //   403: aload_0
      //   404: getfield this$0 : Landroid/support/v7/widget/ActivityChooserModel;
      //   407: getfield mHistoryFileName : Ljava/lang/String;
      //   410: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   413: pop
      //   414: aload #5
      //   416: aload_2
      //   417: invokevirtual toString : ()Ljava/lang/String;
      //   420: aload_1
      //   421: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   424: pop
      //   425: aload_0
      //   426: getfield this$0 : Landroid/support/v7/widget/ActivityChooserModel;
      //   429: iconst_1
      //   430: putfield mCanReadHistoricalData : Z
      //   433: aload_3
      //   434: ifnull -> 514
      //   437: aload_3
      //   438: invokevirtual close : ()V
      //   441: goto -> 507
      //   444: astore_1
      //   445: getstatic android/support/v7/widget/ActivityChooserModel.LOG_TAG : Ljava/lang/String;
      //   448: astore_2
      //   449: new java/lang/StringBuilder
      //   452: astore #5
      //   454: aload #5
      //   456: invokespecial <init> : ()V
      //   459: aload #5
      //   461: ldc 'Error writing historical record file: '
      //   463: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   466: pop
      //   467: aload #5
      //   469: aload_0
      //   470: getfield this$0 : Landroid/support/v7/widget/ActivityChooserModel;
      //   473: getfield mHistoryFileName : Ljava/lang/String;
      //   476: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   479: pop
      //   480: aload_2
      //   481: aload #5
      //   483: invokevirtual toString : ()Ljava/lang/String;
      //   486: aload_1
      //   487: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   490: pop
      //   491: aload_0
      //   492: getfield this$0 : Landroid/support/v7/widget/ActivityChooserModel;
      //   495: iconst_1
      //   496: putfield mCanReadHistoricalData : Z
      //   499: aload_3
      //   500: ifnull -> 514
      //   503: aload_3
      //   504: invokevirtual close : ()V
      //   507: goto -> 514
      //   510: astore_1
      //   511: goto -> 507
      //   514: aconst_null
      //   515: areturn
      //   516: astore_1
      //   517: aload_0
      //   518: getfield this$0 : Landroid/support/v7/widget/ActivityChooserModel;
      //   521: iconst_1
      //   522: putfield mCanReadHistoricalData : Z
      //   525: aload_3
      //   526: ifnull -> 537
      //   529: aload_3
      //   530: invokevirtual close : ()V
      //   533: goto -> 537
      //   536: astore_2
      //   537: aload_1
      //   538: athrow
      //   539: astore_3
      //   540: getstatic android/support/v7/widget/ActivityChooserModel.LOG_TAG : Ljava/lang/String;
      //   543: astore_2
      //   544: new java/lang/StringBuilder
      //   547: dup
      //   548: invokespecial <init> : ()V
      //   551: astore #5
      //   553: aload #5
      //   555: ldc 'Error writing historical record file: '
      //   557: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   560: pop
      //   561: aload #5
      //   563: aload_1
      //   564: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   567: pop
      //   568: aload_2
      //   569: aload #5
      //   571: invokevirtual toString : ()Ljava/lang/String;
      //   574: aload_3
      //   575: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   578: pop
      //   579: aconst_null
      //   580: areturn
      // Exception table:
      //   from	to	target	type
      //   14	27	539	java/io/FileNotFoundException
      //   44	53	444	java/lang/IllegalArgumentException
      //   44	53	381	java/lang/IllegalStateException
      //   44	53	315	java/io/IOException
      //   44	53	311	finally
      //   65	78	444	java/lang/IllegalArgumentException
      //   65	78	381	java/lang/IllegalStateException
      //   65	78	315	java/io/IOException
      //   65	78	311	finally
      //   90	101	444	java/lang/IllegalArgumentException
      //   90	101	381	java/lang/IllegalStateException
      //   90	101	315	java/io/IOException
      //   90	101	311	finally
      //   113	121	444	java/lang/IllegalArgumentException
      //   113	121	381	java/lang/IllegalStateException
      //   113	121	315	java/io/IOException
      //   113	121	311	finally
      //   145	156	444	java/lang/IllegalArgumentException
      //   145	156	381	java/lang/IllegalStateException
      //   145	156	315	java/io/IOException
      //   145	156	311	finally
      //   168	179	444	java/lang/IllegalArgumentException
      //   168	179	381	java/lang/IllegalStateException
      //   168	179	315	java/io/IOException
      //   168	179	311	finally
      //   191	209	444	java/lang/IllegalArgumentException
      //   191	209	381	java/lang/IllegalStateException
      //   191	209	315	java/io/IOException
      //   191	209	311	finally
      //   209	256	307	java/lang/IllegalArgumentException
      //   209	256	303	java/lang/IllegalStateException
      //   209	256	299	java/io/IOException
      //   209	256	516	finally
      //   262	280	307	java/lang/IllegalArgumentException
      //   262	280	303	java/lang/IllegalStateException
      //   262	280	299	java/io/IOException
      //   262	280	516	finally
      //   292	296	510	java/io/IOException
      //   316	362	516	finally
      //   374	378	510	java/io/IOException
      //   382	425	516	finally
      //   437	441	510	java/io/IOException
      //   445	491	516	finally
      //   503	507	510	java/io/IOException
      //   529	533	536	java/io/IOException
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v7/widget/ActivityChooserModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */