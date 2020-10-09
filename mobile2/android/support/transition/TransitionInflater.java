package android.support.transition;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.InflateException;
import android.view.ViewGroup;
import java.io.IOException;
import java.lang.reflect.Constructor;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class TransitionInflater {
  private static final ArrayMap<String, Constructor> CONSTRUCTORS;
  
  private static final Class<?>[] CONSTRUCTOR_SIGNATURE = new Class[] { Context.class, AttributeSet.class };
  
  private final Context mContext;
  
  static {
    CONSTRUCTORS = new ArrayMap();
  }
  
  private TransitionInflater(Context paramContext) {
    this.mContext = paramContext;
  }
  
  private Object createCustom(AttributeSet paramAttributeSet, Class<?> paramClass, String paramString) {
    StringBuilder stringBuilder2;
    String str = paramAttributeSet.getAttributeValue(null, "class");
    if (str != null)
      try {
        synchronized (CONSTRUCTORS) {
          Constructor<?> constructor2 = (Constructor)CONSTRUCTORS.get(str);
          Constructor<?> constructor1 = constructor2;
          if (constructor2 == null) {
            Class<?> clazz = this.mContext.getClassLoader().loadClass(str).asSubclass(paramClass);
            constructor1 = constructor2;
            if (clazz != null) {
              constructor1 = clazz.getConstructor(CONSTRUCTOR_SIGNATURE);
              constructor1.setAccessible(true);
              CONSTRUCTORS.put(str, constructor1);
            } 
          } 
          paramAttributeSet = (AttributeSet)constructor1.newInstance(new Object[] { this.mContext, paramAttributeSet });
          return paramAttributeSet;
        } 
      } catch (Exception exception) {
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Could not instantiate ");
        stringBuilder2.append(paramClass);
        stringBuilder2.append(" class ");
        stringBuilder2.append(str);
        throw new InflateException(stringBuilder2.toString(), exception);
      }  
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append((String)stringBuilder2);
    stringBuilder1.append(" tag must have a 'class' attribute");
    throw new InflateException(stringBuilder1.toString());
  }
  
  private Transition createTransitionFromXml(XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Transition paramTransition) throws XmlPullParserException, IOException {
    TransitionSet transitionSet;
    Transition transition = null;
    int i = paramXmlPullParser.getDepth();
    if (paramTransition instanceof TransitionSet) {
      transitionSet = (TransitionSet)paramTransition;
    } else {
      transitionSet = null;
    } 
    while (true) {
      int j = paramXmlPullParser.next();
      if ((j != 3 || paramXmlPullParser.getDepth() > i) && j != 1) {
        StringBuilder stringBuilder;
        if (j != 2)
          continue; 
        String str = paramXmlPullParser.getName();
        if ("fade".equals(str)) {
          transition = new Fade(this.mContext, paramAttributeSet);
        } else if ("changeBounds".equals(str)) {
          transition = new ChangeBounds(this.mContext, paramAttributeSet);
        } else if ("slide".equals(str)) {
          transition = new Slide(this.mContext, paramAttributeSet);
        } else if ("explode".equals(str)) {
          transition = new Explode(this.mContext, paramAttributeSet);
        } else if ("changeImageTransform".equals(str)) {
          transition = new ChangeImageTransform(this.mContext, paramAttributeSet);
        } else if ("changeTransform".equals(str)) {
          transition = new ChangeTransform(this.mContext, paramAttributeSet);
        } else if ("changeClipBounds".equals(str)) {
          transition = new ChangeClipBounds(this.mContext, paramAttributeSet);
        } else if ("autoTransition".equals(str)) {
          transition = new AutoTransition(this.mContext, paramAttributeSet);
        } else if ("changeScroll".equals(str)) {
          transition = new ChangeScroll(this.mContext, paramAttributeSet);
        } else if ("transitionSet".equals(str)) {
          transition = new TransitionSet(this.mContext, paramAttributeSet);
        } else if ("transition".equals(str)) {
          transition = (Transition)createCustom(paramAttributeSet, Transition.class, "transition");
        } else if ("targets".equals(str)) {
          getTargetIds(paramXmlPullParser, paramAttributeSet, paramTransition);
        } else if ("arcMotion".equals(str)) {
          if (paramTransition != null) {
            paramTransition.setPathMotion(new ArcMotion(this.mContext, paramAttributeSet));
          } else {
            throw new RuntimeException("Invalid use of arcMotion element");
          } 
        } else if ("pathMotion".equals(str)) {
          if (paramTransition != null) {
            paramTransition.setPathMotion((PathMotion)createCustom(paramAttributeSet, PathMotion.class, "pathMotion"));
          } else {
            throw new RuntimeException("Invalid use of pathMotion element");
          } 
        } else if ("patternPathMotion".equals(str)) {
          if (paramTransition != null) {
            paramTransition.setPathMotion(new PatternPathMotion(this.mContext, paramAttributeSet));
          } else {
            throw new RuntimeException("Invalid use of patternPathMotion element");
          } 
        } else {
          stringBuilder = new StringBuilder();
          stringBuilder.append("Unknown scene name: ");
          stringBuilder.append(paramXmlPullParser.getName());
          throw new RuntimeException(stringBuilder.toString());
        } 
        Transition transition1 = transition;
        if (transition != null) {
          if (!paramXmlPullParser.isEmptyElementTag())
            createTransitionFromXml(paramXmlPullParser, (AttributeSet)stringBuilder, transition); 
          if (transitionSet != null) {
            transitionSet.addTransition(transition);
            transition1 = null;
          } else if (paramTransition == null) {
            transition1 = transition;
          } else {
            throw new InflateException("Could not add transition to another transition.");
          } 
        } 
        transition = transition1;
        continue;
      } 
      break;
    } 
    return transition;
  }
  
  private TransitionManager createTransitionManagerFromXml(XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, ViewGroup paramViewGroup) throws XmlPullParserException, IOException {
    int i = paramXmlPullParser.getDepth();
    TransitionManager transitionManager = null;
    while (true) {
      int j = paramXmlPullParser.next();
      if ((j != 3 || paramXmlPullParser.getDepth() > i) && j != 1) {
        if (j != 2)
          continue; 
        String str = paramXmlPullParser.getName();
        if (str.equals("transitionManager")) {
          transitionManager = new TransitionManager();
          continue;
        } 
        if (str.equals("transition") && transitionManager != null) {
          loadTransition(paramAttributeSet, paramXmlPullParser, paramViewGroup, transitionManager);
          continue;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown scene name: ");
        stringBuilder.append(paramXmlPullParser.getName());
        throw new RuntimeException(stringBuilder.toString());
      } 
      break;
    } 
    return transitionManager;
  }
  
  public static TransitionInflater from(Context paramContext) {
    return new TransitionInflater(paramContext);
  }
  
  private void getTargetIds(XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Transition paramTransition) throws XmlPullParserException, IOException {
    int i = paramXmlPullParser.getDepth();
    while (true) {
      TypedArray typedArray;
      int j = paramXmlPullParser.next();
      if ((j != 3 || paramXmlPullParser.getDepth() > i) && j != 1) {
        if (j != 2)
          continue; 
        if (paramXmlPullParser.getName().equals("target")) {
          typedArray = this.mContext.obtainStyledAttributes(paramAttributeSet, Styleable.TRANSITION_TARGET);
          j = TypedArrayUtils.getNamedResourceId(typedArray, paramXmlPullParser, "targetId", 1, 0);
          if (j != 0) {
            paramTransition.addTarget(j);
          } else {
            j = TypedArrayUtils.getNamedResourceId(typedArray, paramXmlPullParser, "excludeId", 2, 0);
            if (j != 0) {
              paramTransition.excludeTarget(j, true);
            } else {
              String str = TypedArrayUtils.getNamedString(typedArray, paramXmlPullParser, "targetName", 4);
              if (str != null) {
                paramTransition.addTarget(str);
              } else {
                str = TypedArrayUtils.getNamedString(typedArray, paramXmlPullParser, "excludeName", 5);
                if (str != null) {
                  paramTransition.excludeTarget(str, true);
                } else {
                  String str1 = TypedArrayUtils.getNamedString(typedArray, paramXmlPullParser, "excludeClass", 3);
                  if (str1 != null) {
                    str = str1;
                    try {
                      paramTransition.excludeTarget(Class.forName(str1), true);
                      typedArray.recycle();
                    } catch (ClassNotFoundException classNotFoundException) {
                      typedArray.recycle();
                      StringBuilder stringBuilder = new StringBuilder();
                      stringBuilder.append("Could not create ");
                      stringBuilder.append(str);
                      throw new RuntimeException(stringBuilder.toString(), classNotFoundException);
                    } 
                    continue;
                  } 
                  str = str1;
                  String str2 = TypedArrayUtils.getNamedString(typedArray, (XmlPullParser)classNotFoundException, "targetClass", 0);
                  str1 = str2;
                  if (str2 != null) {
                    str = str1;
                    paramTransition.addTarget(Class.forName(str1));
                  } 
                } 
              } 
            } 
          } 
        } else {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Unknown scene name: ");
          stringBuilder.append(classNotFoundException.getName());
          throw new RuntimeException(stringBuilder.toString());
        } 
      } else {
        break;
      } 
      typedArray.recycle();
    } 
  }
  
  private void loadTransition(AttributeSet paramAttributeSet, XmlPullParser paramXmlPullParser, ViewGroup paramViewGroup, TransitionManager paramTransitionManager) throws Resources.NotFoundException {
    Scene scene1;
    Scene scene2;
    TypedArray typedArray = this.mContext.obtainStyledAttributes(paramAttributeSet, Styleable.TRANSITION_MANAGER);
    int i = TypedArrayUtils.getNamedResourceId(typedArray, paramXmlPullParser, "transition", 2, -1);
    int j = TypedArrayUtils.getNamedResourceId(typedArray, paramXmlPullParser, "fromScene", 0, -1);
    XmlPullParser xmlPullParser = null;
    if (j < 0) {
      paramAttributeSet = null;
    } else {
      scene1 = Scene.getSceneForLayout(paramViewGroup, j, this.mContext);
    } 
    j = TypedArrayUtils.getNamedResourceId(typedArray, paramXmlPullParser, "toScene", 1, -1);
    if (j < 0) {
      paramXmlPullParser = xmlPullParser;
    } else {
      scene2 = Scene.getSceneForLayout(paramViewGroup, j, this.mContext);
    } 
    if (i >= 0) {
      Transition transition = inflateTransition(i);
      if (transition != null)
        if (scene2 != null) {
          if (scene1 == null) {
            paramTransitionManager.setTransition(scene2, transition);
          } else {
            paramTransitionManager.setTransition(scene1, scene2, transition);
          } 
        } else {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("No toScene for transition ID ");
          stringBuilder.append(i);
          throw new RuntimeException(stringBuilder.toString());
        }  
    } 
    typedArray.recycle();
  }
  
  public Transition inflateTransition(int paramInt) {
    Exception exception;
    XmlResourceParser xmlResourceParser = this.mContext.getResources().getXml(paramInt);
    try {
      Transition transition = createTransitionFromXml((XmlPullParser)xmlResourceParser, Xml.asAttributeSet((XmlPullParser)xmlResourceParser), null);
      xmlResourceParser.close();
      return transition;
    } catch (XmlPullParserException xmlPullParserException) {
      InflateException inflateException = new InflateException();
      this(xmlPullParserException.getMessage(), (Throwable)xmlPullParserException);
      throw inflateException;
    } catch (IOException iOException) {
      InflateException inflateException = new InflateException();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append(xmlResourceParser.getPositionDescription());
      stringBuilder.append(": ");
      stringBuilder.append(iOException.getMessage());
      this(stringBuilder.toString(), iOException);
      throw inflateException;
    } finally {}
    xmlResourceParser.close();
    throw exception;
  }
  
  public TransitionManager inflateTransitionManager(int paramInt, ViewGroup paramViewGroup) {
    XmlResourceParser xmlResourceParser = this.mContext.getResources().getXml(paramInt);
    try {
      TransitionManager transitionManager = createTransitionManagerFromXml((XmlPullParser)xmlResourceParser, Xml.asAttributeSet((XmlPullParser)xmlResourceParser), paramViewGroup);
      xmlResourceParser.close();
      return transitionManager;
    } catch (XmlPullParserException xmlPullParserException) {
      InflateException inflateException = new InflateException();
      this(xmlPullParserException.getMessage());
      inflateException.initCause((Throwable)xmlPullParserException);
      throw inflateException;
    } catch (IOException iOException) {
      InflateException inflateException = new InflateException();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append(xmlResourceParser.getPositionDescription());
      stringBuilder.append(": ");
      stringBuilder.append(iOException.getMessage());
      this(stringBuilder.toString());
      inflateException.initCause(iOException);
      throw inflateException;
    } finally {}
    xmlResourceParser.close();
    throw paramViewGroup;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/transition/TransitionInflater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */