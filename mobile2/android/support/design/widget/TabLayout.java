package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.design.R;
import android.support.design.animation.AnimationUtils;
import android.support.design.internal.ThemeEnforcement;
import android.support.design.internal.ViewUtils;
import android.support.design.resources.MaterialResources;
import android.support.design.ripple.RippleUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.Pools;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PointerIconCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.DecorView;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.TooltipCompat;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

@DecorView
public class TabLayout extends HorizontalScrollView {
  private static final int ANIMATION_DURATION = 300;
  
  static final int DEFAULT_GAP_TEXT_ICON = 8;
  
  private static final int DEFAULT_HEIGHT = 48;
  
  private static final int DEFAULT_HEIGHT_WITH_TEXT_ICON = 72;
  
  static final int FIXED_WRAP_GUTTER_MIN = 16;
  
  public static final int GRAVITY_CENTER = 1;
  
  public static final int GRAVITY_FILL = 0;
  
  public static final int INDICATOR_GRAVITY_BOTTOM = 0;
  
  public static final int INDICATOR_GRAVITY_CENTER = 1;
  
  public static final int INDICATOR_GRAVITY_STRETCH = 3;
  
  public static final int INDICATOR_GRAVITY_TOP = 2;
  
  private static final int INVALID_WIDTH = -1;
  
  private static final int MIN_INDICATOR_WIDTH = 24;
  
  public static final int MODE_FIXED = 1;
  
  public static final int MODE_SCROLLABLE = 0;
  
  private static final int TAB_MIN_WIDTH_MARGIN = 56;
  
  private static final Pools.Pool<Tab> tabPool = (Pools.Pool<Tab>)new Pools.SynchronizedPool(16);
  
  private AdapterChangeListener adapterChangeListener;
  
  private int contentInsetStart;
  
  private BaseOnTabSelectedListener currentVpSelectedListener;
  
  boolean inlineLabel;
  
  int mode;
  
  private TabLayoutOnPageChangeListener pageChangeListener;
  
  private PagerAdapter pagerAdapter;
  
  private DataSetObserver pagerAdapterObserver;
  
  private final int requestedTabMaxWidth;
  
  private final int requestedTabMinWidth;
  
  private ValueAnimator scrollAnimator;
  
  private final int scrollableTabMinWidth;
  
  private BaseOnTabSelectedListener selectedListener;
  
  private final ArrayList<BaseOnTabSelectedListener> selectedListeners = new ArrayList<>();
  
  private Tab selectedTab;
  
  private boolean setupViewPagerImplicitly;
  
  private final SlidingTabIndicator slidingTabIndicator;
  
  final int tabBackgroundResId;
  
  int tabGravity;
  
  ColorStateList tabIconTint;
  
  PorterDuff.Mode tabIconTintMode;
  
  int tabIndicatorAnimationDuration;
  
  boolean tabIndicatorFullWidth;
  
  int tabIndicatorGravity;
  
  int tabMaxWidth = Integer.MAX_VALUE;
  
  int tabPaddingBottom;
  
  int tabPaddingEnd;
  
  int tabPaddingStart;
  
  int tabPaddingTop;
  
  ColorStateList tabRippleColorStateList;
  
  Drawable tabSelectedIndicator;
  
  int tabTextAppearance;
  
  ColorStateList tabTextColors;
  
  float tabTextMultiLineSize;
  
  float tabTextSize;
  
  private final RectF tabViewContentBounds = new RectF();
  
  private final Pools.Pool<TabView> tabViewPool = (Pools.Pool<TabView>)new Pools.SimplePool(12);
  
  private final ArrayList<Tab> tabs = new ArrayList<>();
  
  boolean unboundedRipple;
  
  ViewPager viewPager;
  
  public TabLayout(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public TabLayout(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.tabStyle);
  }
  
  public TabLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    setHorizontalScrollBarEnabled(false);
    SlidingTabIndicator slidingTabIndicator = new SlidingTabIndicator(paramContext);
    this.slidingTabIndicator = slidingTabIndicator;
    super.addView((View)slidingTabIndicator, 0, (ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-2, -1));
    TypedArray typedArray2 = ThemeEnforcement.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.TabLayout, paramInt, R.style.Widget_Design_TabLayout, new int[] { R.styleable.TabLayout_tabTextAppearance });
    this.slidingTabIndicator.setSelectedIndicatorHeight(typedArray2.getDimensionPixelSize(R.styleable.TabLayout_tabIndicatorHeight, -1));
    this.slidingTabIndicator.setSelectedIndicatorColor(typedArray2.getColor(R.styleable.TabLayout_tabIndicatorColor, 0));
    setSelectedTabIndicator(MaterialResources.getDrawable(paramContext, typedArray2, R.styleable.TabLayout_tabIndicator));
    setSelectedTabIndicatorGravity(typedArray2.getInt(R.styleable.TabLayout_tabIndicatorGravity, 0));
    setTabIndicatorFullWidth(typedArray2.getBoolean(R.styleable.TabLayout_tabIndicatorFullWidth, true));
    paramInt = typedArray2.getDimensionPixelSize(R.styleable.TabLayout_tabPadding, 0);
    this.tabPaddingBottom = paramInt;
    this.tabPaddingEnd = paramInt;
    this.tabPaddingTop = paramInt;
    this.tabPaddingStart = paramInt;
    this.tabPaddingStart = typedArray2.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingStart, this.tabPaddingStart);
    this.tabPaddingTop = typedArray2.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingTop, this.tabPaddingTop);
    this.tabPaddingEnd = typedArray2.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingEnd, this.tabPaddingEnd);
    this.tabPaddingBottom = typedArray2.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingBottom, this.tabPaddingBottom);
    paramInt = typedArray2.getResourceId(R.styleable.TabLayout_tabTextAppearance, R.style.TextAppearance_Design_Tab);
    this.tabTextAppearance = paramInt;
    TypedArray typedArray1 = paramContext.obtainStyledAttributes(paramInt, R.styleable.TextAppearance);
    try {
      this.tabTextSize = typedArray1.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, 0);
      this.tabTextColors = MaterialResources.getColorStateList(paramContext, typedArray1, R.styleable.TextAppearance_android_textColor);
      typedArray1.recycle();
      if (typedArray2.hasValue(R.styleable.TabLayout_tabTextColor))
        this.tabTextColors = MaterialResources.getColorStateList(paramContext, typedArray2, R.styleable.TabLayout_tabTextColor); 
      if (typedArray2.hasValue(R.styleable.TabLayout_tabSelectedTextColor)) {
        paramInt = typedArray2.getColor(R.styleable.TabLayout_tabSelectedTextColor, 0);
        this.tabTextColors = createColorStateList(this.tabTextColors.getDefaultColor(), paramInt);
      } 
      this.tabIconTint = MaterialResources.getColorStateList(paramContext, typedArray2, R.styleable.TabLayout_tabIconTint);
      this.tabIconTintMode = ViewUtils.parseTintMode(typedArray2.getInt(R.styleable.TabLayout_tabIconTintMode, -1), null);
      this.tabRippleColorStateList = MaterialResources.getColorStateList(paramContext, typedArray2, R.styleable.TabLayout_tabRippleColor);
      this.tabIndicatorAnimationDuration = typedArray2.getInt(R.styleable.TabLayout_tabIndicatorAnimationDuration, 300);
      this.requestedTabMinWidth = typedArray2.getDimensionPixelSize(R.styleable.TabLayout_tabMinWidth, -1);
      this.requestedTabMaxWidth = typedArray2.getDimensionPixelSize(R.styleable.TabLayout_tabMaxWidth, -1);
      this.tabBackgroundResId = typedArray2.getResourceId(R.styleable.TabLayout_tabBackground, 0);
      this.contentInsetStart = typedArray2.getDimensionPixelSize(R.styleable.TabLayout_tabContentStart, 0);
      this.mode = typedArray2.getInt(R.styleable.TabLayout_tabMode, 1);
      this.tabGravity = typedArray2.getInt(R.styleable.TabLayout_tabGravity, 0);
      this.inlineLabel = typedArray2.getBoolean(R.styleable.TabLayout_tabInlineLabel, false);
      this.unboundedRipple = typedArray2.getBoolean(R.styleable.TabLayout_tabUnboundedRipple, false);
      typedArray2.recycle();
      Resources resources = getResources();
      this.tabTextMultiLineSize = resources.getDimensionPixelSize(R.dimen.design_tab_text_size_2line);
      this.scrollableTabMinWidth = resources.getDimensionPixelSize(R.dimen.design_tab_scrollable_min_width);
      return;
    } finally {
      typedArray1.recycle();
    } 
  }
  
  private void addTabFromItemView(TabItem paramTabItem) {
    Tab tab = newTab();
    if (paramTabItem.text != null)
      tab.setText(paramTabItem.text); 
    if (paramTabItem.icon != null)
      tab.setIcon(paramTabItem.icon); 
    if (paramTabItem.customLayout != 0)
      tab.setCustomView(paramTabItem.customLayout); 
    if (!TextUtils.isEmpty(paramTabItem.getContentDescription()))
      tab.setContentDescription(paramTabItem.getContentDescription()); 
    addTab(tab);
  }
  
  private void addTabView(Tab paramTab) {
    TabView tabView = paramTab.view;
    this.slidingTabIndicator.addView((View)tabView, paramTab.getPosition(), (ViewGroup.LayoutParams)createLayoutParamsForTabs());
  }
  
  private void addViewInternal(View paramView) {
    if (paramView instanceof TabItem) {
      addTabFromItemView((TabItem)paramView);
      return;
    } 
    throw new IllegalArgumentException("Only TabItem instances can be added to TabLayout");
  }
  
  private void animateToTab(int paramInt) {
    if (paramInt == -1)
      return; 
    if (getWindowToken() == null || !ViewCompat.isLaidOut((View)this) || this.slidingTabIndicator.childrenNeedLayout()) {
      setScrollPosition(paramInt, 0.0F, true);
      return;
    } 
    int i = getScrollX();
    int j = calculateScrollXForTab(paramInt, 0.0F);
    if (i != j) {
      ensureScrollAnimator();
      this.scrollAnimator.setIntValues(new int[] { i, j });
      this.scrollAnimator.start();
    } 
    this.slidingTabIndicator.animateIndicatorToPosition(paramInt, this.tabIndicatorAnimationDuration);
  }
  
  private void applyModeAndGravity() {
    int i = 0;
    if (this.mode == 0)
      i = Math.max(0, this.contentInsetStart - this.tabPaddingStart); 
    ViewCompat.setPaddingRelative((View)this.slidingTabIndicator, i, 0, 0, 0);
    i = this.mode;
    if (i != 0) {
      if (i == 1)
        this.slidingTabIndicator.setGravity(1); 
    } else {
      this.slidingTabIndicator.setGravity(8388611);
    } 
    updateTabViews(true);
  }
  
  private int calculateScrollXForTab(int paramInt, float paramFloat) {
    int i = this.mode;
    int j = 0;
    if (i == 0) {
      View view2;
      View view1 = this.slidingTabIndicator.getChildAt(paramInt);
      if (paramInt + 1 < this.slidingTabIndicator.getChildCount()) {
        view2 = this.slidingTabIndicator.getChildAt(paramInt + 1);
      } else {
        view2 = null;
      } 
      if (view1 != null) {
        paramInt = view1.getWidth();
      } else {
        paramInt = 0;
      } 
      if (view2 != null)
        j = view2.getWidth(); 
      i = view1.getLeft() + paramInt / 2 - getWidth() / 2;
      paramInt = (int)((paramInt + j) * 0.5F * paramFloat);
      if (ViewCompat.getLayoutDirection((View)this) == 0) {
        paramInt = i + paramInt;
      } else {
        paramInt = i - paramInt;
      } 
      return paramInt;
    } 
    return 0;
  }
  
  private void configureTab(Tab paramTab, int paramInt) {
    paramTab.setPosition(paramInt);
    this.tabs.add(paramInt, paramTab);
    int i = this.tabs.size();
    while (++paramInt < i) {
      ((Tab)this.tabs.get(paramInt)).setPosition(paramInt);
      paramInt++;
    } 
  }
  
  private static ColorStateList createColorStateList(int paramInt1, int paramInt2) {
    int[][] arrayOfInt = new int[2][];
    int[] arrayOfInt1 = new int[2];
    arrayOfInt[0] = SELECTED_STATE_SET;
    arrayOfInt1[0] = paramInt2;
    paramInt2 = 0 + 1;
    arrayOfInt[paramInt2] = EMPTY_STATE_SET;
    arrayOfInt1[paramInt2] = paramInt1;
    return new ColorStateList(arrayOfInt, arrayOfInt1);
  }
  
  private LinearLayout.LayoutParams createLayoutParamsForTabs() {
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -1);
    updateTabViewLayoutParams(layoutParams);
    return layoutParams;
  }
  
  private TabView createTabView(Tab paramTab) {
    TabView tabView;
    Pools.Pool<TabView> pool1 = this.tabViewPool;
    if (pool1 != null) {
      TabView tabView1 = (TabView)pool1.acquire();
    } else {
      pool1 = null;
    } 
    Pools.Pool<TabView> pool2 = pool1;
    if (pool1 == null)
      tabView = new TabView(getContext()); 
    tabView.setTab(paramTab);
    tabView.setFocusable(true);
    tabView.setMinimumWidth(getTabMinWidth());
    if (TextUtils.isEmpty(paramTab.contentDesc)) {
      tabView.setContentDescription(paramTab.text);
    } else {
      tabView.setContentDescription(paramTab.contentDesc);
    } 
    return tabView;
  }
  
  private void dispatchTabReselected(Tab paramTab) {
    for (int i = this.selectedListeners.size() - 1; i >= 0; i--)
      ((BaseOnTabSelectedListener<Tab>)this.selectedListeners.get(i)).onTabReselected(paramTab); 
  }
  
  private void dispatchTabSelected(Tab paramTab) {
    for (int i = this.selectedListeners.size() - 1; i >= 0; i--)
      ((BaseOnTabSelectedListener<Tab>)this.selectedListeners.get(i)).onTabSelected(paramTab); 
  }
  
  private void dispatchTabUnselected(Tab paramTab) {
    for (int i = this.selectedListeners.size() - 1; i >= 0; i--)
      ((BaseOnTabSelectedListener<Tab>)this.selectedListeners.get(i)).onTabUnselected(paramTab); 
  }
  
  private void ensureScrollAnimator() {
    if (this.scrollAnimator == null) {
      ValueAnimator valueAnimator = new ValueAnimator();
      this.scrollAnimator = valueAnimator;
      valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
      this.scrollAnimator.setDuration(this.tabIndicatorAnimationDuration);
      this.scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
              TabLayout.this.scrollTo(((Integer)param1ValueAnimator.getAnimatedValue()).intValue(), 0);
            }
          });
    } 
  }
  
  private int getDefaultHeight() {
    byte b3;
    byte b1 = 0;
    byte b2 = 0;
    int i = this.tabs.size();
    while (true) {
      b3 = b1;
      if (b2 < i) {
        Tab tab = this.tabs.get(b2);
        if (tab != null && tab.getIcon() != null && !TextUtils.isEmpty(tab.getText())) {
          b3 = 1;
          break;
        } 
        b2++;
        continue;
      } 
      break;
    } 
    if (b3 && !this.inlineLabel) {
      b3 = 72;
    } else {
      b3 = 48;
    } 
    return b3;
  }
  
  private int getTabMinWidth() {
    int i = this.requestedTabMinWidth;
    if (i != -1)
      return i; 
    if (this.mode == 0) {
      i = this.scrollableTabMinWidth;
    } else {
      i = 0;
    } 
    return i;
  }
  
  private int getTabScrollRange() {
    return Math.max(0, this.slidingTabIndicator.getWidth() - getWidth() - getPaddingLeft() - getPaddingRight());
  }
  
  private void removeTabViewAt(int paramInt) {
    TabView tabView = (TabView)this.slidingTabIndicator.getChildAt(paramInt);
    this.slidingTabIndicator.removeViewAt(paramInt);
    if (tabView != null) {
      tabView.reset();
      this.tabViewPool.release(tabView);
    } 
    requestLayout();
  }
  
  private void setSelectedTabView(int paramInt) {
    int i = this.slidingTabIndicator.getChildCount();
    if (paramInt < i)
      for (byte b = 0; b < i; b++) {
        View view = this.slidingTabIndicator.getChildAt(b);
        boolean bool1 = false;
        if (b == paramInt) {
          bool2 = true;
        } else {
          bool2 = false;
        } 
        view.setSelected(bool2);
        boolean bool2 = bool1;
        if (b == paramInt)
          bool2 = true; 
        view.setActivated(bool2);
      }  
  }
  
  private void setupWithViewPager(ViewPager paramViewPager, boolean paramBoolean1, boolean paramBoolean2) {
    ViewPager viewPager = this.viewPager;
    if (viewPager != null) {
      TabLayoutOnPageChangeListener tabLayoutOnPageChangeListener = this.pageChangeListener;
      if (tabLayoutOnPageChangeListener != null)
        viewPager.removeOnPageChangeListener(tabLayoutOnPageChangeListener); 
      AdapterChangeListener adapterChangeListener = this.adapterChangeListener;
      if (adapterChangeListener != null)
        this.viewPager.removeOnAdapterChangeListener(adapterChangeListener); 
    } 
    BaseOnTabSelectedListener baseOnTabSelectedListener = this.currentVpSelectedListener;
    if (baseOnTabSelectedListener != null) {
      removeOnTabSelectedListener(baseOnTabSelectedListener);
      this.currentVpSelectedListener = null;
    } 
    if (paramViewPager != null) {
      this.viewPager = paramViewPager;
      if (this.pageChangeListener == null)
        this.pageChangeListener = new TabLayoutOnPageChangeListener(this); 
      this.pageChangeListener.reset();
      paramViewPager.addOnPageChangeListener(this.pageChangeListener);
      baseOnTabSelectedListener = new ViewPagerOnTabSelectedListener(paramViewPager);
      this.currentVpSelectedListener = baseOnTabSelectedListener;
      addOnTabSelectedListener(baseOnTabSelectedListener);
      PagerAdapter pagerAdapter = paramViewPager.getAdapter();
      if (pagerAdapter != null)
        setPagerAdapter(pagerAdapter, paramBoolean1); 
      if (this.adapterChangeListener == null)
        this.adapterChangeListener = new AdapterChangeListener(); 
      this.adapterChangeListener.setAutoRefresh(paramBoolean1);
      paramViewPager.addOnAdapterChangeListener(this.adapterChangeListener);
      setScrollPosition(paramViewPager.getCurrentItem(), 0.0F, true);
    } else {
      this.viewPager = null;
      setPagerAdapter((PagerAdapter)null, false);
    } 
    this.setupViewPagerImplicitly = paramBoolean2;
  }
  
  private void updateAllTabs() {
    byte b = 0;
    int i = this.tabs.size();
    while (b < i) {
      ((Tab)this.tabs.get(b)).updateView();
      b++;
    } 
  }
  
  private void updateTabViewLayoutParams(LinearLayout.LayoutParams paramLayoutParams) {
    if (this.mode == 1 && this.tabGravity == 0) {
      paramLayoutParams.width = 0;
      paramLayoutParams.weight = 1.0F;
    } else {
      paramLayoutParams.width = -2;
      paramLayoutParams.weight = 0.0F;
    } 
  }
  
  public void addOnTabSelectedListener(BaseOnTabSelectedListener paramBaseOnTabSelectedListener) {
    if (!this.selectedListeners.contains(paramBaseOnTabSelectedListener))
      this.selectedListeners.add(paramBaseOnTabSelectedListener); 
  }
  
  public void addTab(Tab paramTab) {
    addTab(paramTab, this.tabs.isEmpty());
  }
  
  public void addTab(Tab paramTab, int paramInt) {
    addTab(paramTab, paramInt, this.tabs.isEmpty());
  }
  
  public void addTab(Tab paramTab, int paramInt, boolean paramBoolean) {
    if (paramTab.parent == this) {
      configureTab(paramTab, paramInt);
      addTabView(paramTab);
      if (paramBoolean)
        paramTab.select(); 
      return;
    } 
    throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
  }
  
  public void addTab(Tab paramTab, boolean paramBoolean) {
    addTab(paramTab, this.tabs.size(), paramBoolean);
  }
  
  public void addView(View paramView) {
    addViewInternal(paramView);
  }
  
  public void addView(View paramView, int paramInt) {
    addViewInternal(paramView);
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    addViewInternal(paramView);
  }
  
  public void addView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    addViewInternal(paramView);
  }
  
  public void clearOnTabSelectedListeners() {
    this.selectedListeners.clear();
  }
  
  protected Tab createTabFromPool() {
    Tab tab1 = (Tab)tabPool.acquire();
    Tab tab2 = tab1;
    if (tab1 == null)
      tab2 = new Tab(); 
    return tab2;
  }
  
  int dpToPx(int paramInt) {
    return Math.round((getResources().getDisplayMetrics()).density * paramInt);
  }
  
  public FrameLayout.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return generateDefaultLayoutParams();
  }
  
  public int getSelectedTabPosition() {
    byte b;
    Tab tab = this.selectedTab;
    if (tab != null) {
      b = tab.getPosition();
    } else {
      b = -1;
    } 
    return b;
  }
  
  public Tab getTabAt(int paramInt) {
    return (paramInt < 0 || paramInt >= getTabCount()) ? null : this.tabs.get(paramInt);
  }
  
  public int getTabCount() {
    return this.tabs.size();
  }
  
  public int getTabGravity() {
    return this.tabGravity;
  }
  
  public ColorStateList getTabIconTint() {
    return this.tabIconTint;
  }
  
  public int getTabIndicatorGravity() {
    return this.tabIndicatorGravity;
  }
  
  int getTabMaxWidth() {
    return this.tabMaxWidth;
  }
  
  public int getTabMode() {
    return this.mode;
  }
  
  public ColorStateList getTabRippleColor() {
    return this.tabRippleColorStateList;
  }
  
  public Drawable getTabSelectedIndicator() {
    return this.tabSelectedIndicator;
  }
  
  public ColorStateList getTabTextColors() {
    return this.tabTextColors;
  }
  
  public boolean hasUnboundedRipple() {
    return this.unboundedRipple;
  }
  
  public boolean isInlineLabel() {
    return this.inlineLabel;
  }
  
  public boolean isTabIndicatorFullWidth() {
    return this.tabIndicatorFullWidth;
  }
  
  public Tab newTab() {
    Tab tab = createTabFromPool();
    tab.parent = this;
    tab.view = createTabView(tab);
    return tab;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (this.viewPager == null) {
      ViewParent viewParent = getParent();
      if (viewParent instanceof ViewPager)
        setupWithViewPager((ViewPager)viewParent, true, true); 
    } 
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (this.setupViewPagerImplicitly) {
      setupWithViewPager((ViewPager)null);
      this.setupViewPagerImplicitly = false;
    } 
  }
  
  protected void onDraw(Canvas paramCanvas) {
    for (byte b = 0; b < this.slidingTabIndicator.getChildCount(); b++) {
      View view = this.slidingTabIndicator.getChildAt(b);
      if (view instanceof TabView)
        ((TabView)view).drawBackground(paramCanvas); 
    } 
    super.onDraw(paramCanvas);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = dpToPx(getDefaultHeight()) + getPaddingTop() + getPaddingBottom();
    int j = View.MeasureSpec.getMode(paramInt2);
    if (j != Integer.MIN_VALUE) {
      if (j == 0)
        paramInt2 = View.MeasureSpec.makeMeasureSpec(i, 1073741824); 
    } else {
      paramInt2 = View.MeasureSpec.makeMeasureSpec(Math.min(i, View.MeasureSpec.getSize(paramInt2)), 1073741824);
    } 
    j = View.MeasureSpec.getSize(paramInt1);
    if (View.MeasureSpec.getMode(paramInt1) != 0) {
      i = this.requestedTabMaxWidth;
      if (i <= 0)
        i = j - dpToPx(56); 
      this.tabMaxWidth = i;
    } 
    super.onMeasure(paramInt1, paramInt2);
    j = getChildCount();
    i = 1;
    paramInt1 = 1;
    if (j == 1) {
      View view = getChildAt(0);
      j = 0;
      int k = this.mode;
      if (k != 0) {
        if (k != 1) {
          paramInt1 = j;
        } else if (view.getMeasuredWidth() == getMeasuredWidth()) {
          paramInt1 = 0;
        } 
      } else if (view.getMeasuredWidth() < getMeasuredWidth()) {
        paramInt1 = i;
      } else {
        paramInt1 = 0;
      } 
      if (paramInt1 != 0) {
        paramInt1 = getChildMeasureSpec(paramInt2, getPaddingTop() + getPaddingBottom(), (view.getLayoutParams()).height);
        view.measure(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), paramInt1);
      } 
    } 
  }
  
  void populateFromPagerAdapter() {
    removeAllTabs();
    PagerAdapter pagerAdapter = this.pagerAdapter;
    if (pagerAdapter != null) {
      int i = pagerAdapter.getCount();
      int j;
      for (j = 0; j < i; j++)
        addTab(newTab().setText(this.pagerAdapter.getPageTitle(j)), false); 
      ViewPager viewPager = this.viewPager;
      if (viewPager != null && i > 0) {
        j = viewPager.getCurrentItem();
        if (j != getSelectedTabPosition() && j < getTabCount())
          selectTab(getTabAt(j)); 
      } 
    } 
  }
  
  protected boolean releaseFromTabPool(Tab paramTab) {
    return tabPool.release(paramTab);
  }
  
  public void removeAllTabs() {
    for (int i = this.slidingTabIndicator.getChildCount() - 1; i >= 0; i--)
      removeTabViewAt(i); 
    Iterator<Tab> iterator = this.tabs.iterator();
    while (iterator.hasNext()) {
      Tab tab = iterator.next();
      iterator.remove();
      tab.reset();
      releaseFromTabPool(tab);
    } 
    this.selectedTab = null;
  }
  
  public void removeOnTabSelectedListener(BaseOnTabSelectedListener paramBaseOnTabSelectedListener) {
    this.selectedListeners.remove(paramBaseOnTabSelectedListener);
  }
  
  public void removeTab(Tab paramTab) {
    if (paramTab.parent == this) {
      removeTabAt(paramTab.getPosition());
      return;
    } 
    throw new IllegalArgumentException("Tab does not belong to this TabLayout.");
  }
  
  public void removeTabAt(int paramInt) {
    int i;
    Tab tab = this.selectedTab;
    if (tab != null) {
      i = tab.getPosition();
    } else {
      i = 0;
    } 
    removeTabViewAt(paramInt);
    tab = this.tabs.remove(paramInt);
    if (tab != null) {
      tab.reset();
      releaseFromTabPool(tab);
    } 
    int j = this.tabs.size();
    for (int k = paramInt; k < j; k++)
      ((Tab)this.tabs.get(k)).setPosition(k); 
    if (i == paramInt) {
      if (this.tabs.isEmpty()) {
        tab = null;
      } else {
        tab = this.tabs.get(Math.max(0, paramInt - 1));
      } 
      selectTab(tab);
    } 
  }
  
  void selectTab(Tab paramTab) {
    selectTab(paramTab, true);
  }
  
  void selectTab(Tab paramTab, boolean paramBoolean) {
    Tab tab = this.selectedTab;
    if (tab == paramTab) {
      if (tab != null) {
        dispatchTabReselected(paramTab);
        animateToTab(paramTab.getPosition());
      } 
    } else {
      byte b;
      if (paramTab != null) {
        b = paramTab.getPosition();
      } else {
        b = -1;
      } 
      if (paramBoolean) {
        if ((tab == null || tab.getPosition() == -1) && b != -1) {
          setScrollPosition(b, 0.0F, true);
        } else {
          animateToTab(b);
        } 
        if (b != -1)
          setSelectedTabView(b); 
      } 
      this.selectedTab = paramTab;
      if (tab != null)
        dispatchTabUnselected(tab); 
      if (paramTab != null)
        dispatchTabSelected(paramTab); 
    } 
  }
  
  public void setInlineLabel(boolean paramBoolean) {
    if (this.inlineLabel != paramBoolean) {
      this.inlineLabel = paramBoolean;
      for (byte b = 0; b < this.slidingTabIndicator.getChildCount(); b++) {
        View view = this.slidingTabIndicator.getChildAt(b);
        if (view instanceof TabView)
          ((TabView)view).updateOrientation(); 
      } 
      applyModeAndGravity();
    } 
  }
  
  public void setInlineLabelResource(int paramInt) {
    setInlineLabel(getResources().getBoolean(paramInt));
  }
  
  @Deprecated
  public void setOnTabSelectedListener(BaseOnTabSelectedListener paramBaseOnTabSelectedListener) {
    BaseOnTabSelectedListener baseOnTabSelectedListener = this.selectedListener;
    if (baseOnTabSelectedListener != null)
      removeOnTabSelectedListener(baseOnTabSelectedListener); 
    this.selectedListener = paramBaseOnTabSelectedListener;
    if (paramBaseOnTabSelectedListener != null)
      addOnTabSelectedListener(paramBaseOnTabSelectedListener); 
  }
  
  void setPagerAdapter(PagerAdapter paramPagerAdapter, boolean paramBoolean) {
    PagerAdapter pagerAdapter = this.pagerAdapter;
    if (pagerAdapter != null) {
      DataSetObserver dataSetObserver = this.pagerAdapterObserver;
      if (dataSetObserver != null)
        pagerAdapter.unregisterDataSetObserver(dataSetObserver); 
    } 
    this.pagerAdapter = paramPagerAdapter;
    if (paramBoolean && paramPagerAdapter != null) {
      if (this.pagerAdapterObserver == null)
        this.pagerAdapterObserver = new PagerAdapterObserver(); 
      paramPagerAdapter.registerDataSetObserver(this.pagerAdapterObserver);
    } 
    populateFromPagerAdapter();
  }
  
  void setScrollAnimatorListener(Animator.AnimatorListener paramAnimatorListener) {
    ensureScrollAnimator();
    this.scrollAnimator.addListener(paramAnimatorListener);
  }
  
  public void setScrollPosition(int paramInt, float paramFloat, boolean paramBoolean) {
    setScrollPosition(paramInt, paramFloat, paramBoolean, true);
  }
  
  void setScrollPosition(int paramInt, float paramFloat, boolean paramBoolean1, boolean paramBoolean2) {
    int i = Math.round(paramInt + paramFloat);
    if (i < 0 || i >= this.slidingTabIndicator.getChildCount())
      return; 
    if (paramBoolean2)
      this.slidingTabIndicator.setIndicatorPositionFromTabPosition(paramInt, paramFloat); 
    ValueAnimator valueAnimator = this.scrollAnimator;
    if (valueAnimator != null && valueAnimator.isRunning())
      this.scrollAnimator.cancel(); 
    scrollTo(calculateScrollXForTab(paramInt, paramFloat), 0);
    if (paramBoolean1)
      setSelectedTabView(i); 
  }
  
  public void setSelectedTabIndicator(int paramInt) {
    if (paramInt != 0) {
      setSelectedTabIndicator(AppCompatResources.getDrawable(getContext(), paramInt));
    } else {
      setSelectedTabIndicator((Drawable)null);
    } 
  }
  
  public void setSelectedTabIndicator(Drawable paramDrawable) {
    if (this.tabSelectedIndicator != paramDrawable) {
      this.tabSelectedIndicator = paramDrawable;
      ViewCompat.postInvalidateOnAnimation((View)this.slidingTabIndicator);
    } 
  }
  
  public void setSelectedTabIndicatorColor(int paramInt) {
    this.slidingTabIndicator.setSelectedIndicatorColor(paramInt);
  }
  
  public void setSelectedTabIndicatorGravity(int paramInt) {
    if (this.tabIndicatorGravity != paramInt) {
      this.tabIndicatorGravity = paramInt;
      ViewCompat.postInvalidateOnAnimation((View)this.slidingTabIndicator);
    } 
  }
  
  @Deprecated
  public void setSelectedTabIndicatorHeight(int paramInt) {
    this.slidingTabIndicator.setSelectedIndicatorHeight(paramInt);
  }
  
  public void setTabGravity(int paramInt) {
    if (this.tabGravity != paramInt) {
      this.tabGravity = paramInt;
      applyModeAndGravity();
    } 
  }
  
  public void setTabIconTint(ColorStateList paramColorStateList) {
    if (this.tabIconTint != paramColorStateList) {
      this.tabIconTint = paramColorStateList;
      updateAllTabs();
    } 
  }
  
  public void setTabIconTintResource(int paramInt) {
    setTabIconTint(AppCompatResources.getColorStateList(getContext(), paramInt));
  }
  
  public void setTabIndicatorFullWidth(boolean paramBoolean) {
    this.tabIndicatorFullWidth = paramBoolean;
    ViewCompat.postInvalidateOnAnimation((View)this.slidingTabIndicator);
  }
  
  public void setTabMode(int paramInt) {
    if (paramInt != this.mode) {
      this.mode = paramInt;
      applyModeAndGravity();
    } 
  }
  
  public void setTabRippleColor(ColorStateList paramColorStateList) {
    if (this.tabRippleColorStateList != paramColorStateList) {
      this.tabRippleColorStateList = paramColorStateList;
      for (byte b = 0; b < this.slidingTabIndicator.getChildCount(); b++) {
        View view = this.slidingTabIndicator.getChildAt(b);
        if (view instanceof TabView)
          ((TabView)view).updateBackgroundDrawable(getContext()); 
      } 
    } 
  }
  
  public void setTabRippleColorResource(int paramInt) {
    setTabRippleColor(AppCompatResources.getColorStateList(getContext(), paramInt));
  }
  
  public void setTabTextColors(int paramInt1, int paramInt2) {
    setTabTextColors(createColorStateList(paramInt1, paramInt2));
  }
  
  public void setTabTextColors(ColorStateList paramColorStateList) {
    if (this.tabTextColors != paramColorStateList) {
      this.tabTextColors = paramColorStateList;
      updateAllTabs();
    } 
  }
  
  @Deprecated
  public void setTabsFromPagerAdapter(PagerAdapter paramPagerAdapter) {
    setPagerAdapter(paramPagerAdapter, false);
  }
  
  public void setUnboundedRipple(boolean paramBoolean) {
    if (this.unboundedRipple != paramBoolean) {
      this.unboundedRipple = paramBoolean;
      for (byte b = 0; b < this.slidingTabIndicator.getChildCount(); b++) {
        View view = this.slidingTabIndicator.getChildAt(b);
        if (view instanceof TabView)
          ((TabView)view).updateBackgroundDrawable(getContext()); 
      } 
    } 
  }
  
  public void setUnboundedRippleResource(int paramInt) {
    setUnboundedRipple(getResources().getBoolean(paramInt));
  }
  
  public void setupWithViewPager(ViewPager paramViewPager) {
    setupWithViewPager(paramViewPager, true);
  }
  
  public void setupWithViewPager(ViewPager paramViewPager, boolean paramBoolean) {
    setupWithViewPager(paramViewPager, paramBoolean, false);
  }
  
  public boolean shouldDelayChildPressedState() {
    boolean bool;
    if (getTabScrollRange() > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  void updateTabViews(boolean paramBoolean) {
    for (byte b = 0; b < this.slidingTabIndicator.getChildCount(); b++) {
      View view = this.slidingTabIndicator.getChildAt(b);
      view.setMinimumWidth(getTabMinWidth());
      updateTabViewLayoutParams((LinearLayout.LayoutParams)view.getLayoutParams());
      if (paramBoolean)
        view.requestLayout(); 
    } 
  }
  
  private class AdapterChangeListener implements ViewPager.OnAdapterChangeListener {
    private boolean autoRefresh;
    
    public void onAdapterChanged(ViewPager param1ViewPager, PagerAdapter param1PagerAdapter1, PagerAdapter param1PagerAdapter2) {
      if (TabLayout.this.viewPager == param1ViewPager)
        TabLayout.this.setPagerAdapter(param1PagerAdapter2, this.autoRefresh); 
    }
    
    void setAutoRefresh(boolean param1Boolean) {
      this.autoRefresh = param1Boolean;
    }
  }
  
  public static interface BaseOnTabSelectedListener<T extends Tab> {
    void onTabReselected(T param1T);
    
    void onTabSelected(T param1T);
    
    void onTabUnselected(T param1T);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Mode {}
  
  public static interface OnTabSelectedListener extends BaseOnTabSelectedListener<Tab> {}
  
  private class PagerAdapterObserver extends DataSetObserver {
    public void onChanged() {
      TabLayout.this.populateFromPagerAdapter();
    }
    
    public void onInvalidated() {
      TabLayout.this.populateFromPagerAdapter();
    }
  }
  
  private class SlidingTabIndicator extends LinearLayout {
    private final GradientDrawable defaultSelectionIndicator;
    
    private ValueAnimator indicatorAnimator;
    
    private int indicatorLeft = -1;
    
    private int indicatorRight = -1;
    
    private int layoutDirection = -1;
    
    private int selectedIndicatorHeight;
    
    private final Paint selectedIndicatorPaint;
    
    int selectedPosition = -1;
    
    float selectionOffset;
    
    SlidingTabIndicator(Context param1Context) {
      super(param1Context);
      setWillNotDraw(false);
      this.selectedIndicatorPaint = new Paint();
      this.defaultSelectionIndicator = new GradientDrawable();
    }
    
    private void calculateTabViewContentBounds(TabLayout.TabView param1TabView, RectF param1RectF) {
      int i = param1TabView.getContentWidth();
      int j = i;
      if (i < TabLayout.this.dpToPx(24))
        j = TabLayout.this.dpToPx(24); 
      int k = (param1TabView.getLeft() + param1TabView.getRight()) / 2;
      i = j / 2;
      j /= 2;
      param1RectF.set((k - i), 0.0F, (j + k), 0.0F);
    }
    
    private void updateIndicatorPosition() {
      byte b1;
      byte b2;
      View view = getChildAt(this.selectedPosition);
      if (view != null && view.getWidth() > 0) {
        b1 = view.getLeft();
        b2 = view.getRight();
        int i = b1;
        int j = b2;
        if (!TabLayout.this.tabIndicatorFullWidth) {
          i = b1;
          j = b2;
          if (view instanceof TabLayout.TabView) {
            calculateTabViewContentBounds((TabLayout.TabView)view, TabLayout.this.tabViewContentBounds);
            i = (int)TabLayout.this.tabViewContentBounds.left;
            j = (int)TabLayout.this.tabViewContentBounds.right;
          } 
        } 
        b1 = i;
        b2 = j;
        if (this.selectionOffset > 0.0F) {
          b1 = i;
          b2 = j;
          if (this.selectedPosition < getChildCount() - 1) {
            view = getChildAt(this.selectedPosition + 1);
            int k = view.getLeft();
            int m = view.getRight();
            b1 = k;
            b2 = m;
            if (!TabLayout.this.tabIndicatorFullWidth) {
              b1 = k;
              b2 = m;
              if (view instanceof TabLayout.TabView) {
                calculateTabViewContentBounds((TabLayout.TabView)view, TabLayout.this.tabViewContentBounds);
                b1 = (int)TabLayout.this.tabViewContentBounds.left;
                b2 = (int)TabLayout.this.tabViewContentBounds.right;
              } 
            } 
            float f = this.selectionOffset;
            b1 = (int)(b1 * f + (1.0F - f) * i);
            b2 = (int)(b2 * f + (1.0F - f) * j);
          } 
        } 
      } else {
        b1 = -1;
        b2 = -1;
      } 
      setIndicatorPosition(b1, b2);
    }
    
    void animateIndicatorToPosition(final int position, int param1Int2) {
      ValueAnimator valueAnimator = this.indicatorAnimator;
      if (valueAnimator != null && valueAnimator.isRunning())
        this.indicatorAnimator.cancel(); 
      View view = getChildAt(position);
      if (view == null) {
        updateIndicatorPosition();
        return;
      } 
      final int finalTargetLeft = view.getLeft();
      final int finalTargetRight = view.getRight();
      if (!TabLayout.this.tabIndicatorFullWidth && view instanceof TabLayout.TabView) {
        calculateTabViewContentBounds((TabLayout.TabView)view, TabLayout.this.tabViewContentBounds);
        i = (int)TabLayout.this.tabViewContentBounds.left;
        j = (int)TabLayout.this.tabViewContentBounds.right;
      } 
      final int startLeft = this.indicatorLeft;
      final int startRight = this.indicatorRight;
      if (k != i || m != j) {
        ValueAnimator valueAnimator1 = new ValueAnimator();
        this.indicatorAnimator = valueAnimator1;
        valueAnimator1.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        valueAnimator1.setDuration(param1Int2);
        valueAnimator1.setFloatValues(new float[] { 0.0F, 1.0F });
        valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
              public void onAnimationUpdate(ValueAnimator param2ValueAnimator) {
                float f = param2ValueAnimator.getAnimatedFraction();
                TabLayout.SlidingTabIndicator.this.setIndicatorPosition(AnimationUtils.lerp(startLeft, finalTargetLeft, f), AnimationUtils.lerp(startRight, finalTargetRight, f));
              }
            });
        valueAnimator1.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
              public void onAnimationEnd(Animator param2Animator) {
                TabLayout.SlidingTabIndicator.this.selectedPosition = position;
                TabLayout.SlidingTabIndicator.this.selectionOffset = 0.0F;
              }
            });
        valueAnimator1.start();
      } 
    }
    
    boolean childrenNeedLayout() {
      byte b = 0;
      int i = getChildCount();
      while (b < i) {
        if (getChildAt(b).getWidth() <= 0)
          return true; 
        b++;
      } 
      return false;
    }
    
    public void draw(Canvas param1Canvas) {
      int i = 0;
      if (TabLayout.this.tabSelectedIndicator != null)
        i = TabLayout.this.tabSelectedIndicator.getIntrinsicHeight(); 
      if (this.selectedIndicatorHeight >= 0)
        i = this.selectedIndicatorHeight; 
      int j = 0;
      int k = 0;
      int m = TabLayout.this.tabIndicatorGravity;
      if (m != 0) {
        if (m != 1) {
          if (m != 2) {
            if (m != 3) {
              i = j;
            } else {
              i = 0;
              k = getHeight();
            } 
          } else {
            j = 0;
            k = i;
            i = j;
          } 
        } else {
          k = (getHeight() - i) / 2;
          j = (getHeight() + i) / 2;
          i = k;
          k = j;
        } 
      } else {
        i = getHeight() - i;
        k = getHeight();
      } 
      j = this.indicatorLeft;
      if (j >= 0 && this.indicatorRight > j) {
        GradientDrawable gradientDrawable;
        if (TabLayout.this.tabSelectedIndicator != null) {
          Drawable drawable1 = TabLayout.this.tabSelectedIndicator;
        } else {
          gradientDrawable = this.defaultSelectionIndicator;
        } 
        Drawable drawable = DrawableCompat.wrap((Drawable)gradientDrawable);
        drawable.setBounds(this.indicatorLeft, i, this.indicatorRight, k);
        if (this.selectedIndicatorPaint != null)
          if (Build.VERSION.SDK_INT == 21) {
            drawable.setColorFilter(this.selectedIndicatorPaint.getColor(), PorterDuff.Mode.SRC_IN);
          } else {
            DrawableCompat.setTint(drawable, this.selectedIndicatorPaint.getColor());
          }  
        drawable.draw(param1Canvas);
      } 
      super.draw(param1Canvas);
    }
    
    float getIndicatorPosition() {
      return this.selectedPosition + this.selectionOffset;
    }
    
    protected void onLayout(boolean param1Boolean, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      super.onLayout(param1Boolean, param1Int1, param1Int2, param1Int3, param1Int4);
      ValueAnimator valueAnimator = this.indicatorAnimator;
      if (valueAnimator != null && valueAnimator.isRunning()) {
        this.indicatorAnimator.cancel();
        long l = this.indicatorAnimator.getDuration();
        animateIndicatorToPosition(this.selectedPosition, Math.round((1.0F - this.indicatorAnimator.getAnimatedFraction()) * (float)l));
      } else {
        updateIndicatorPosition();
      } 
    }
    
    protected void onMeasure(int param1Int1, int param1Int2) {
      super.onMeasure(param1Int1, param1Int2);
      if (View.MeasureSpec.getMode(param1Int1) != 1073741824)
        return; 
      if (TabLayout.this.mode == 1 && TabLayout.this.tabGravity == 1) {
        int i = getChildCount();
        int j = 0;
        byte b = 0;
        while (b < i) {
          View view = getChildAt(b);
          int m = j;
          if (view.getVisibility() == 0)
            m = Math.max(j, view.getMeasuredWidth()); 
          b++;
          j = m;
        } 
        if (j <= 0)
          return; 
        int k = TabLayout.this.dpToPx(16);
        b = 0;
        if (j * i <= getMeasuredWidth() - k * 2) {
          for (k = 0; k < i; k++) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)getChildAt(k).getLayoutParams();
            if (layoutParams.width != j || layoutParams.weight != 0.0F) {
              layoutParams.width = j;
              layoutParams.weight = 0.0F;
              b = 1;
            } 
          } 
        } else {
          TabLayout.this.tabGravity = 0;
          TabLayout.this.updateTabViews(false);
          b = 1;
        } 
        if (b != 0)
          super.onMeasure(param1Int1, param1Int2); 
      } 
    }
    
    public void onRtlPropertiesChanged(int param1Int) {
      super.onRtlPropertiesChanged(param1Int);
      if (Build.VERSION.SDK_INT < 23 && this.layoutDirection != param1Int) {
        requestLayout();
        this.layoutDirection = param1Int;
      } 
    }
    
    void setIndicatorPosition(int param1Int1, int param1Int2) {
      if (param1Int1 != this.indicatorLeft || param1Int2 != this.indicatorRight) {
        this.indicatorLeft = param1Int1;
        this.indicatorRight = param1Int2;
        ViewCompat.postInvalidateOnAnimation((View)this);
      } 
    }
    
    void setIndicatorPositionFromTabPosition(int param1Int, float param1Float) {
      ValueAnimator valueAnimator = this.indicatorAnimator;
      if (valueAnimator != null && valueAnimator.isRunning())
        this.indicatorAnimator.cancel(); 
      this.selectedPosition = param1Int;
      this.selectionOffset = param1Float;
      updateIndicatorPosition();
    }
    
    void setSelectedIndicatorColor(int param1Int) {
      if (this.selectedIndicatorPaint.getColor() != param1Int) {
        this.selectedIndicatorPaint.setColor(param1Int);
        ViewCompat.postInvalidateOnAnimation((View)this);
      } 
    }
    
    void setSelectedIndicatorHeight(int param1Int) {
      if (this.selectedIndicatorHeight != param1Int) {
        this.selectedIndicatorHeight = param1Int;
        ViewCompat.postInvalidateOnAnimation((View)this);
      } 
    }
  }
  
  class null implements ValueAnimator.AnimatorUpdateListener {
    public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
      float f = param1ValueAnimator.getAnimatedFraction();
      this.this$1.setIndicatorPosition(AnimationUtils.lerp(startLeft, finalTargetLeft, f), AnimationUtils.lerp(startRight, finalTargetRight, f));
    }
  }
  
  class null extends AnimatorListenerAdapter {
    public void onAnimationEnd(Animator param1Animator) {
      this.this$1.selectedPosition = position;
      this.this$1.selectionOffset = 0.0F;
    }
  }
  
  public static class Tab {
    public static final int INVALID_POSITION = -1;
    
    private CharSequence contentDesc;
    
    private View customView;
    
    private Drawable icon;
    
    public TabLayout parent;
    
    private int position = -1;
    
    private Object tag;
    
    private CharSequence text;
    
    public TabLayout.TabView view;
    
    public CharSequence getContentDescription() {
      CharSequence charSequence;
      TabLayout.TabView tabView = this.view;
      if (tabView == null) {
        tabView = null;
      } else {
        charSequence = tabView.getContentDescription();
      } 
      return charSequence;
    }
    
    public View getCustomView() {
      return this.customView;
    }
    
    public Drawable getIcon() {
      return this.icon;
    }
    
    public int getPosition() {
      return this.position;
    }
    
    public Object getTag() {
      return this.tag;
    }
    
    public CharSequence getText() {
      return this.text;
    }
    
    public boolean isSelected() {
      TabLayout tabLayout = this.parent;
      if (tabLayout != null) {
        boolean bool;
        if (tabLayout.getSelectedTabPosition() == this.position) {
          bool = true;
        } else {
          bool = false;
        } 
        return bool;
      } 
      throw new IllegalArgumentException("Tab not attached to a TabLayout");
    }
    
    void reset() {
      this.parent = null;
      this.view = null;
      this.tag = null;
      this.icon = null;
      this.text = null;
      this.contentDesc = null;
      this.position = -1;
      this.customView = null;
    }
    
    public void select() {
      TabLayout tabLayout = this.parent;
      if (tabLayout != null) {
        tabLayout.selectTab(this);
        return;
      } 
      throw new IllegalArgumentException("Tab not attached to a TabLayout");
    }
    
    public Tab setContentDescription(int param1Int) {
      TabLayout tabLayout = this.parent;
      if (tabLayout != null)
        return setContentDescription(tabLayout.getResources().getText(param1Int)); 
      throw new IllegalArgumentException("Tab not attached to a TabLayout");
    }
    
    public Tab setContentDescription(CharSequence param1CharSequence) {
      this.contentDesc = param1CharSequence;
      updateView();
      return this;
    }
    
    public Tab setCustomView(int param1Int) {
      return setCustomView(LayoutInflater.from(this.view.getContext()).inflate(param1Int, (ViewGroup)this.view, false));
    }
    
    public Tab setCustomView(View param1View) {
      this.customView = param1View;
      updateView();
      return this;
    }
    
    public Tab setIcon(int param1Int) {
      TabLayout tabLayout = this.parent;
      if (tabLayout != null)
        return setIcon(AppCompatResources.getDrawable(tabLayout.getContext(), param1Int)); 
      throw new IllegalArgumentException("Tab not attached to a TabLayout");
    }
    
    public Tab setIcon(Drawable param1Drawable) {
      this.icon = param1Drawable;
      updateView();
      return this;
    }
    
    void setPosition(int param1Int) {
      this.position = param1Int;
    }
    
    public Tab setTag(Object param1Object) {
      this.tag = param1Object;
      return this;
    }
    
    public Tab setText(int param1Int) {
      TabLayout tabLayout = this.parent;
      if (tabLayout != null)
        return setText(tabLayout.getResources().getText(param1Int)); 
      throw new IllegalArgumentException("Tab not attached to a TabLayout");
    }
    
    public Tab setText(CharSequence param1CharSequence) {
      if (TextUtils.isEmpty(this.contentDesc) && !TextUtils.isEmpty(param1CharSequence))
        this.view.setContentDescription(param1CharSequence); 
      this.text = param1CharSequence;
      updateView();
      return this;
    }
    
    void updateView() {
      TabLayout.TabView tabView = this.view;
      if (tabView != null)
        tabView.update(); 
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TabGravity {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TabIndicatorGravity {}
  
  public static class TabLayoutOnPageChangeListener implements ViewPager.OnPageChangeListener {
    private int previousScrollState;
    
    private int scrollState;
    
    private final WeakReference<TabLayout> tabLayoutRef;
    
    public TabLayoutOnPageChangeListener(TabLayout param1TabLayout) {
      this.tabLayoutRef = new WeakReference<>(param1TabLayout);
    }
    
    public void onPageScrollStateChanged(int param1Int) {
      this.previousScrollState = this.scrollState;
      this.scrollState = param1Int;
    }
    
    public void onPageScrolled(int param1Int1, float param1Float, int param1Int2) {
      TabLayout tabLayout = this.tabLayoutRef.get();
      if (tabLayout != null) {
        boolean bool2;
        param1Int2 = this.scrollState;
        boolean bool1 = false;
        if (param1Int2 != 2 || this.previousScrollState == 1) {
          bool2 = true;
        } else {
          bool2 = false;
        } 
        if (this.scrollState != 2 || this.previousScrollState != 0)
          bool1 = true; 
        tabLayout.setScrollPosition(param1Int1, param1Float, bool2, bool1);
      } 
    }
    
    public void onPageSelected(int param1Int) {
      TabLayout tabLayout = this.tabLayoutRef.get();
      if (tabLayout != null && tabLayout.getSelectedTabPosition() != param1Int && param1Int < tabLayout.getTabCount()) {
        boolean bool;
        int i = this.scrollState;
        if (i == 0 || (i == 2 && this.previousScrollState == 0)) {
          bool = true;
        } else {
          bool = false;
        } 
        tabLayout.selectTab(tabLayout.getTabAt(param1Int), bool);
      } 
    }
    
    void reset() {
      this.scrollState = 0;
      this.previousScrollState = 0;
    }
  }
  
  class TabView extends LinearLayout {
    private Drawable baseBackgroundDrawable;
    
    private ImageView customIconView;
    
    private TextView customTextView;
    
    private View customView;
    
    private int defaultMaxLines = 2;
    
    private ImageView iconView;
    
    private TabLayout.Tab tab;
    
    private TextView textView;
    
    public TabView(Context param1Context) {
      super(param1Context);
      updateBackgroundDrawable(param1Context);
      ViewCompat.setPaddingRelative((View)this, TabLayout.this.tabPaddingStart, TabLayout.this.tabPaddingTop, TabLayout.this.tabPaddingEnd, TabLayout.this.tabPaddingBottom);
      setGravity(17);
      setOrientation(TabLayout.this.inlineLabel ^ true);
      setClickable(true);
      ViewCompat.setPointerIcon((View)this, PointerIconCompat.getSystemIcon(getContext(), 1002));
    }
    
    private float approximateLineWidth(Layout param1Layout, int param1Int, float param1Float) {
      return param1Layout.getLineWidth(param1Int) * param1Float / param1Layout.getPaint().getTextSize();
    }
    
    private void drawBackground(Canvas param1Canvas) {
      Drawable drawable = this.baseBackgroundDrawable;
      if (drawable != null) {
        drawable.setBounds(getLeft(), getTop(), getRight(), getBottom());
        this.baseBackgroundDrawable.draw(param1Canvas);
      } 
    }
    
    private int getContentWidth() {
      boolean bool = false;
      int i = 0;
      int j = 0;
      TextView textView = this.textView;
      byte b = 0;
      ImageView imageView = this.iconView;
      View view = this.customView;
      while (b < 3) {
        (new View[3])[0] = (View)textView;
        (new View[3])[1] = (View)imageView;
        (new View[3])[2] = view;
        View view1 = (new View[3])[b];
        boolean bool1 = bool;
        int k = i;
        int m = j;
        if (view1 != null) {
          bool1 = bool;
          k = i;
          m = j;
          if (view1.getVisibility() == 0) {
            k = view1.getLeft();
            m = k;
            if (bool)
              m = Math.min(i, k); 
            i = m;
            k = view1.getRight();
            m = k;
            if (bool)
              m = Math.max(j, k); 
            bool1 = true;
            k = i;
          } 
        } 
        b++;
        bool = bool1;
        i = k;
        j = m;
      } 
      return j - i;
    }
    
    private void updateBackgroundDrawable(Context param1Context) {
      LayerDrawable layerDrawable;
      int i = TabLayout.this.tabBackgroundResId;
      GradientDrawable gradientDrawable2 = null;
      if (i != 0) {
        Drawable drawable = AppCompatResources.getDrawable(param1Context, TabLayout.this.tabBackgroundResId);
        this.baseBackgroundDrawable = drawable;
        if (drawable != null && drawable.isStateful())
          this.baseBackgroundDrawable.setState(getDrawableState()); 
      } else {
        this.baseBackgroundDrawable = null;
      } 
      GradientDrawable gradientDrawable1 = new GradientDrawable();
      gradientDrawable1.setColor(0);
      if (TabLayout.this.tabRippleColorStateList != null) {
        RippleDrawable rippleDrawable;
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(1.0E-5F);
        gradientDrawable.setColor(-1);
        ColorStateList colorStateList = RippleUtils.convertToRippleDrawableColor(TabLayout.this.tabRippleColorStateList);
        if (Build.VERSION.SDK_INT >= 21) {
          if (TabLayout.this.unboundedRipple)
            gradientDrawable1 = null; 
          if (!TabLayout.this.unboundedRipple)
            gradientDrawable2 = gradientDrawable; 
          rippleDrawable = new RippleDrawable(colorStateList, (Drawable)gradientDrawable1, (Drawable)gradientDrawable2);
        } else {
          Drawable drawable = DrawableCompat.wrap((Drawable)gradientDrawable);
          DrawableCompat.setTintList(drawable, colorStateList);
          layerDrawable = new LayerDrawable(new Drawable[] { (Drawable)rippleDrawable, drawable });
        } 
      } 
      ViewCompat.setBackground((View)this, (Drawable)layerDrawable);
      TabLayout.this.invalidate();
    }
    
    private void updateTextAndIcon(TextView param1TextView, ImageView param1ImageView) {
      TabLayout.Tab tab2 = this.tab;
      TabLayout.Tab tab3 = null;
      if (tab2 != null && tab2.getIcon() != null) {
        Drawable drawable = DrawableCompat.wrap(this.tab.getIcon()).mutate();
      } else {
        tab2 = null;
      } 
      TabLayout.Tab tab4 = this.tab;
      if (tab4 != null) {
        CharSequence charSequence = tab4.getText();
      } else {
        tab4 = null;
      } 
      if (param1ImageView != null)
        if (tab2 != null) {
          param1ImageView.setImageDrawable((Drawable)tab2);
          param1ImageView.setVisibility(0);
          setVisibility(0);
        } else {
          param1ImageView.setVisibility(8);
          param1ImageView.setImageDrawable(null);
        }  
      int i = TextUtils.isEmpty((CharSequence)tab4) ^ true;
      if (param1TextView != null)
        if (i != 0) {
          param1TextView.setText((CharSequence)tab4);
          param1TextView.setVisibility(0);
          setVisibility(0);
        } else {
          param1TextView.setVisibility(8);
          param1TextView.setText(null);
        }  
      if (param1ImageView != null) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)param1ImageView.getLayoutParams();
        byte b = 0;
        int j = b;
        if (i != 0) {
          j = b;
          if (param1ImageView.getVisibility() == 0)
            j = TabLayout.this.dpToPx(8); 
        } 
        if (TabLayout.this.inlineLabel) {
          if (j != MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams)) {
            MarginLayoutParamsCompat.setMarginEnd(marginLayoutParams, j);
            marginLayoutParams.bottomMargin = 0;
            param1ImageView.setLayoutParams((ViewGroup.LayoutParams)marginLayoutParams);
            param1ImageView.requestLayout();
          } 
        } else if (j != marginLayoutParams.bottomMargin) {
          marginLayoutParams.bottomMargin = j;
          MarginLayoutParamsCompat.setMarginEnd(marginLayoutParams, 0);
          param1ImageView.setLayoutParams((ViewGroup.LayoutParams)marginLayoutParams);
          param1ImageView.requestLayout();
        } 
      } 
      TabLayout.Tab tab1 = this.tab;
      if (tab1 != null) {
        CharSequence charSequence = tab1.contentDesc;
      } else {
        tab1 = null;
      } 
      if (i != 0)
        tab1 = tab3; 
      TooltipCompat.setTooltipText((View)this, (CharSequence)tab1);
    }
    
    protected void drawableStateChanged() {
      super.drawableStateChanged();
      byte b = 0;
      int[] arrayOfInt = getDrawableState();
      Drawable drawable = this.baseBackgroundDrawable;
      int i = b;
      if (drawable != null) {
        i = b;
        if (drawable.isStateful())
          i = false | this.baseBackgroundDrawable.setState(arrayOfInt); 
      } 
      if (i != 0) {
        invalidate();
        TabLayout.this.invalidate();
      } 
    }
    
    public TabLayout.Tab getTab() {
      return this.tab;
    }
    
    public void onInitializeAccessibilityEvent(AccessibilityEvent param1AccessibilityEvent) {
      super.onInitializeAccessibilityEvent(param1AccessibilityEvent);
      param1AccessibilityEvent.setClassName(ActionBar.Tab.class.getName());
    }
    
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo param1AccessibilityNodeInfo) {
      super.onInitializeAccessibilityNodeInfo(param1AccessibilityNodeInfo);
      param1AccessibilityNodeInfo.setClassName(ActionBar.Tab.class.getName());
    }
    
    public void onMeasure(int param1Int1, int param1Int2) {
      // Byte code:
      //   0: iload_1
      //   1: invokestatic getSize : (I)I
      //   4: istore_3
      //   5: iload_1
      //   6: invokestatic getMode : (I)I
      //   9: istore #4
      //   11: aload_0
      //   12: getfield this$0 : Landroid/support/design/widget/TabLayout;
      //   15: invokevirtual getTabMaxWidth : ()I
      //   18: istore #5
      //   20: iload #5
      //   22: ifle -> 53
      //   25: iload #4
      //   27: ifeq -> 36
      //   30: iload_3
      //   31: iload #5
      //   33: if_icmple -> 53
      //   36: aload_0
      //   37: getfield this$0 : Landroid/support/design/widget/TabLayout;
      //   40: getfield tabMaxWidth : I
      //   43: ldc_w -2147483648
      //   46: invokestatic makeMeasureSpec : (II)I
      //   49: istore_1
      //   50: goto -> 53
      //   53: aload_0
      //   54: iload_1
      //   55: iload_2
      //   56: invokespecial onMeasure : (II)V
      //   59: aload_0
      //   60: getfield textView : Landroid/widget/TextView;
      //   63: ifnull -> 320
      //   66: aload_0
      //   67: getfield this$0 : Landroid/support/design/widget/TabLayout;
      //   70: getfield tabTextSize : F
      //   73: fstore #6
      //   75: aload_0
      //   76: getfield defaultMaxLines : I
      //   79: istore #5
      //   81: aload_0
      //   82: getfield iconView : Landroid/widget/ImageView;
      //   85: astore #7
      //   87: aload #7
      //   89: ifnull -> 110
      //   92: aload #7
      //   94: invokevirtual getVisibility : ()I
      //   97: ifne -> 110
      //   100: iconst_1
      //   101: istore #4
      //   103: fload #6
      //   105: fstore #8
      //   107: goto -> 159
      //   110: aload_0
      //   111: getfield textView : Landroid/widget/TextView;
      //   114: astore #7
      //   116: fload #6
      //   118: fstore #8
      //   120: iload #5
      //   122: istore #4
      //   124: aload #7
      //   126: ifnull -> 159
      //   129: fload #6
      //   131: fstore #8
      //   133: iload #5
      //   135: istore #4
      //   137: aload #7
      //   139: invokevirtual getLineCount : ()I
      //   142: iconst_1
      //   143: if_icmple -> 159
      //   146: aload_0
      //   147: getfield this$0 : Landroid/support/design/widget/TabLayout;
      //   150: getfield tabTextMultiLineSize : F
      //   153: fstore #8
      //   155: iload #5
      //   157: istore #4
      //   159: aload_0
      //   160: getfield textView : Landroid/widget/TextView;
      //   163: invokevirtual getTextSize : ()F
      //   166: fstore #6
      //   168: aload_0
      //   169: getfield textView : Landroid/widget/TextView;
      //   172: invokevirtual getLineCount : ()I
      //   175: istore #9
      //   177: aload_0
      //   178: getfield textView : Landroid/widget/TextView;
      //   181: invokestatic getMaxLines : (Landroid/widget/TextView;)I
      //   184: istore #5
      //   186: fload #8
      //   188: fload #6
      //   190: fcmpl
      //   191: ifne -> 206
      //   194: iload #5
      //   196: iflt -> 320
      //   199: iload #4
      //   201: iload #5
      //   203: if_icmpeq -> 320
      //   206: iconst_1
      //   207: istore_3
      //   208: iload_3
      //   209: istore #5
      //   211: aload_0
      //   212: getfield this$0 : Landroid/support/design/widget/TabLayout;
      //   215: getfield mode : I
      //   218: iconst_1
      //   219: if_icmpne -> 290
      //   222: iload_3
      //   223: istore #5
      //   225: fload #8
      //   227: fload #6
      //   229: fcmpl
      //   230: ifle -> 290
      //   233: iload_3
      //   234: istore #5
      //   236: iload #9
      //   238: iconst_1
      //   239: if_icmpne -> 290
      //   242: aload_0
      //   243: getfield textView : Landroid/widget/TextView;
      //   246: invokevirtual getLayout : ()Landroid/text/Layout;
      //   249: astore #7
      //   251: aload #7
      //   253: ifnull -> 287
      //   256: iload_3
      //   257: istore #5
      //   259: aload_0
      //   260: aload #7
      //   262: iconst_0
      //   263: fload #8
      //   265: invokespecial approximateLineWidth : (Landroid/text/Layout;IF)F
      //   268: aload_0
      //   269: invokevirtual getMeasuredWidth : ()I
      //   272: aload_0
      //   273: invokevirtual getPaddingLeft : ()I
      //   276: isub
      //   277: aload_0
      //   278: invokevirtual getPaddingRight : ()I
      //   281: isub
      //   282: i2f
      //   283: fcmpl
      //   284: ifle -> 290
      //   287: iconst_0
      //   288: istore #5
      //   290: iload #5
      //   292: ifeq -> 320
      //   295: aload_0
      //   296: getfield textView : Landroid/widget/TextView;
      //   299: iconst_0
      //   300: fload #8
      //   302: invokevirtual setTextSize : (IF)V
      //   305: aload_0
      //   306: getfield textView : Landroid/widget/TextView;
      //   309: iload #4
      //   311: invokevirtual setMaxLines : (I)V
      //   314: aload_0
      //   315: iload_1
      //   316: iload_2
      //   317: invokespecial onMeasure : (II)V
      //   320: return
    }
    
    public boolean performClick() {
      boolean bool = super.performClick();
      if (this.tab != null) {
        if (!bool)
          playSoundEffect(0); 
        this.tab.select();
        return true;
      } 
      return bool;
    }
    
    void reset() {
      setTab((TabLayout.Tab)null);
      setSelected(false);
    }
    
    public void setSelected(boolean param1Boolean) {
      boolean bool;
      if (isSelected() != param1Boolean) {
        bool = true;
      } else {
        bool = false;
      } 
      super.setSelected(param1Boolean);
      if (bool && param1Boolean && Build.VERSION.SDK_INT < 16)
        sendAccessibilityEvent(4); 
      TextView textView = this.textView;
      if (textView != null)
        textView.setSelected(param1Boolean); 
      ImageView imageView = this.iconView;
      if (imageView != null)
        imageView.setSelected(param1Boolean); 
      View view = this.customView;
      if (view != null)
        view.setSelected(param1Boolean); 
    }
    
    void setTab(TabLayout.Tab param1Tab) {
      if (param1Tab != this.tab) {
        this.tab = param1Tab;
        update();
      } 
    }
    
    final void update() {
      TabLayout.Tab tab = this.tab;
      View view1 = null;
      if (tab != null) {
        view2 = tab.getCustomView();
      } else {
        view2 = null;
      } 
      if (view2 != null) {
        ViewParent viewParent = view2.getParent();
        if (viewParent != this) {
          if (viewParent != null)
            ((ViewGroup)viewParent).removeView(view2); 
          addView(view2);
        } 
        this.customView = view2;
        TextView textView2 = this.textView;
        if (textView2 != null)
          textView2.setVisibility(8); 
        ImageView imageView = this.iconView;
        if (imageView != null) {
          imageView.setVisibility(8);
          this.iconView.setImageDrawable(null);
        } 
        TextView textView1 = (TextView)view2.findViewById(16908308);
        this.customTextView = textView1;
        if (textView1 != null)
          this.defaultMaxLines = TextViewCompat.getMaxLines(textView1); 
        this.customIconView = (ImageView)view2.findViewById(16908294);
      } else {
        view2 = this.customView;
        if (view2 != null) {
          removeView(view2);
          this.customView = null;
        } 
        this.customTextView = null;
        this.customIconView = null;
      } 
      View view2 = this.customView;
      boolean bool1 = false;
      if (view2 == null) {
        if (this.iconView == null) {
          ImageView imageView = (ImageView)LayoutInflater.from(getContext()).inflate(R.layout.design_layout_tab_icon, (ViewGroup)this, false);
          addView((View)imageView, 0);
          this.iconView = imageView;
        } 
        if (tab != null && tab.getIcon() != null) {
          Drawable drawable = DrawableCompat.wrap(tab.getIcon()).mutate();
        } else {
          view2 = view1;
        } 
        if (view2 != null) {
          DrawableCompat.setTintList((Drawable)view2, TabLayout.this.tabIconTint);
          if (TabLayout.this.tabIconTintMode != null)
            DrawableCompat.setTintMode((Drawable)view2, TabLayout.this.tabIconTintMode); 
        } 
        if (this.textView == null) {
          TextView textView = (TextView)LayoutInflater.from(getContext()).inflate(R.layout.design_layout_tab_text, (ViewGroup)this, false);
          addView((View)textView);
          this.textView = textView;
          this.defaultMaxLines = TextViewCompat.getMaxLines(textView);
        } 
        TextViewCompat.setTextAppearance(this.textView, TabLayout.this.tabTextAppearance);
        if (TabLayout.this.tabTextColors != null)
          this.textView.setTextColor(TabLayout.this.tabTextColors); 
        updateTextAndIcon(this.textView, this.iconView);
      } else if (this.customTextView != null || this.customIconView != null) {
        updateTextAndIcon(this.customTextView, this.customIconView);
      } 
      if (tab != null && !TextUtils.isEmpty(tab.contentDesc))
        setContentDescription(tab.contentDesc); 
      boolean bool2 = bool1;
      if (tab != null) {
        bool2 = bool1;
        if (tab.isSelected())
          bool2 = true; 
      } 
      setSelected(bool2);
    }
    
    final void updateOrientation() {
      setOrientation(TabLayout.this.inlineLabel ^ true);
      if (this.customTextView != null || this.customIconView != null) {
        updateTextAndIcon(this.customTextView, this.customIconView);
        return;
      } 
      updateTextAndIcon(this.textView, this.iconView);
    }
  }
  
  public static class ViewPagerOnTabSelectedListener implements OnTabSelectedListener {
    private final ViewPager viewPager;
    
    public ViewPagerOnTabSelectedListener(ViewPager param1ViewPager) {
      this.viewPager = param1ViewPager;
    }
    
    public void onTabReselected(TabLayout.Tab param1Tab) {}
    
    public void onTabSelected(TabLayout.Tab param1Tab) {
      this.viewPager.setCurrentItem(param1Tab.getPosition());
    }
    
    public void onTabUnselected(TabLayout.Tab param1Tab) {}
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/design/widget/TabLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */