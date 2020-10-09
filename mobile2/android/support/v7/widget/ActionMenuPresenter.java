package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ActionProvider;
import android.support.v7.appcompat.R;
import android.support.v7.view.ActionBarPolicy;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.BaseMenuPresenter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.ShowableListMenu;
import android.support.v7.view.menu.SubMenuBuilder;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

class ActionMenuPresenter extends BaseMenuPresenter implements ActionProvider.SubUiVisibilityListener {
  private static final String TAG = "ActionMenuPresenter";
  
  private final SparseBooleanArray mActionButtonGroups = new SparseBooleanArray();
  
  ActionButtonSubmenu mActionButtonPopup;
  
  private int mActionItemWidthLimit;
  
  private boolean mExpandedActionViewsExclusive;
  
  private int mMaxItems;
  
  private boolean mMaxItemsSet;
  
  private int mMinCellSize;
  
  int mOpenSubMenuId;
  
  OverflowMenuButton mOverflowButton;
  
  OverflowPopup mOverflowPopup;
  
  private Drawable mPendingOverflowIcon;
  
  private boolean mPendingOverflowIconSet;
  
  private ActionMenuPopupCallback mPopupCallback;
  
  final PopupPresenterCallback mPopupPresenterCallback = new PopupPresenterCallback();
  
  OpenOverflowRunnable mPostedOpenRunnable;
  
  private boolean mReserveOverflow;
  
  private boolean mReserveOverflowSet;
  
  private View mScrapActionButtonView;
  
  private boolean mStrictWidthLimit;
  
  private int mWidthLimit;
  
  private boolean mWidthLimitSet;
  
  public ActionMenuPresenter(Context paramContext) {
    super(paramContext, R.layout.abc_action_menu_layout, R.layout.abc_action_menu_item_layout);
  }
  
  private View findViewForItem(MenuItem paramMenuItem) {
    ViewGroup viewGroup = (ViewGroup)this.mMenuView;
    if (viewGroup == null)
      return null; 
    int i = viewGroup.getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = viewGroup.getChildAt(b);
      if (view instanceof MenuView.ItemView && ((MenuView.ItemView)view).getItemData() == paramMenuItem)
        return view; 
    } 
    return null;
  }
  
  public void bindItemView(MenuItemImpl paramMenuItemImpl, MenuView.ItemView paramItemView) {
    paramItemView.initialize(paramMenuItemImpl, 0);
    ActionMenuView actionMenuView = (ActionMenuView)this.mMenuView;
    ActionMenuItemView actionMenuItemView = (ActionMenuItemView)paramItemView;
    actionMenuItemView.setItemInvoker(actionMenuView);
    if (this.mPopupCallback == null)
      this.mPopupCallback = new ActionMenuPopupCallback(); 
    actionMenuItemView.setPopupCallback(this.mPopupCallback);
  }
  
  public boolean dismissPopupMenus() {
    return hideOverflowMenu() | hideSubMenus();
  }
  
  public boolean filterLeftoverView(ViewGroup paramViewGroup, int paramInt) {
    return (paramViewGroup.getChildAt(paramInt) == this.mOverflowButton) ? false : super.filterLeftoverView(paramViewGroup, paramInt);
  }
  
  public boolean flagActionItems() {
    // Byte code:
    //   0: aload_0
    //   1: astore_1
    //   2: aload_1
    //   3: getfield mMenu : Landroid/support/v7/view/menu/MenuBuilder;
    //   6: ifnull -> 25
    //   9: aload_1
    //   10: getfield mMenu : Landroid/support/v7/view/menu/MenuBuilder;
    //   13: invokevirtual getVisibleItems : ()Ljava/util/ArrayList;
    //   16: astore_2
    //   17: aload_2
    //   18: invokevirtual size : ()I
    //   21: istore_3
    //   22: goto -> 29
    //   25: aconst_null
    //   26: astore_2
    //   27: iconst_0
    //   28: istore_3
    //   29: aload_1
    //   30: getfield mMaxItems : I
    //   33: istore #4
    //   35: aload_1
    //   36: getfield mActionItemWidthLimit : I
    //   39: istore #5
    //   41: iconst_0
    //   42: iconst_0
    //   43: invokestatic makeMeasureSpec : (II)I
    //   46: istore #6
    //   48: aload_1
    //   49: getfield mMenuView : Landroid/support/v7/view/menu/MenuView;
    //   52: checkcast android/view/ViewGroup
    //   55: astore #7
    //   57: iconst_0
    //   58: istore #8
    //   60: iconst_0
    //   61: istore #9
    //   63: iconst_0
    //   64: istore #10
    //   66: iconst_0
    //   67: istore #11
    //   69: iconst_0
    //   70: istore #12
    //   72: iload #12
    //   74: iload_3
    //   75: if_icmpge -> 156
    //   78: aload_2
    //   79: iload #12
    //   81: invokevirtual get : (I)Ljava/lang/Object;
    //   84: checkcast android/support/v7/view/menu/MenuItemImpl
    //   87: astore #13
    //   89: aload #13
    //   91: invokevirtual requiresActionButton : ()Z
    //   94: ifeq -> 103
    //   97: iinc #8, 1
    //   100: goto -> 120
    //   103: aload #13
    //   105: invokevirtual requestsActionButton : ()Z
    //   108: ifeq -> 117
    //   111: iinc #9, 1
    //   114: goto -> 120
    //   117: iconst_1
    //   118: istore #11
    //   120: iload #4
    //   122: istore #14
    //   124: aload_1
    //   125: getfield mExpandedActionViewsExclusive : Z
    //   128: ifeq -> 146
    //   131: iload #4
    //   133: istore #14
    //   135: aload #13
    //   137: invokevirtual isActionViewExpanded : ()Z
    //   140: ifeq -> 146
    //   143: iconst_0
    //   144: istore #14
    //   146: iinc #12, 1
    //   149: iload #14
    //   151: istore #4
    //   153: goto -> 72
    //   156: iload #4
    //   158: istore #12
    //   160: aload_1
    //   161: getfield mReserveOverflow : Z
    //   164: ifeq -> 192
    //   167: iload #11
    //   169: ifne -> 186
    //   172: iload #4
    //   174: istore #12
    //   176: iload #8
    //   178: iload #9
    //   180: iadd
    //   181: iload #4
    //   183: if_icmple -> 192
    //   186: iload #4
    //   188: iconst_1
    //   189: isub
    //   190: istore #12
    //   192: iload #12
    //   194: iload #8
    //   196: isub
    //   197: istore #11
    //   199: aload_1
    //   200: getfield mActionButtonGroups : Landroid/util/SparseBooleanArray;
    //   203: astore #13
    //   205: aload #13
    //   207: invokevirtual clear : ()V
    //   210: iconst_0
    //   211: istore #9
    //   213: iconst_0
    //   214: istore #12
    //   216: aload_1
    //   217: getfield mStrictWidthLimit : Z
    //   220: ifeq -> 249
    //   223: aload_1
    //   224: getfield mMinCellSize : I
    //   227: istore #4
    //   229: iload #5
    //   231: iload #4
    //   233: idiv
    //   234: istore #12
    //   236: iload #4
    //   238: iload #5
    //   240: iload #4
    //   242: irem
    //   243: iload #12
    //   245: idiv
    //   246: iadd
    //   247: istore #9
    //   249: iconst_0
    //   250: istore #14
    //   252: iload #10
    //   254: istore #4
    //   256: iload #8
    //   258: istore #10
    //   260: aload #7
    //   262: astore_1
    //   263: iload #5
    //   265: istore #8
    //   267: iload_3
    //   268: istore #5
    //   270: aload_0
    //   271: astore #7
    //   273: iload #14
    //   275: iload #5
    //   277: if_icmpge -> 835
    //   280: aload_2
    //   281: iload #14
    //   283: invokevirtual get : (I)Ljava/lang/Object;
    //   286: checkcast android/support/v7/view/menu/MenuItemImpl
    //   289: astore #15
    //   291: aload #15
    //   293: invokevirtual requiresActionButton : ()Z
    //   296: ifeq -> 430
    //   299: aload #7
    //   301: aload #15
    //   303: aload #7
    //   305: getfield mScrapActionButtonView : Landroid/view/View;
    //   308: aload_1
    //   309: invokevirtual getItemView : (Landroid/support/v7/view/menu/MenuItemImpl;Landroid/view/View;Landroid/view/ViewGroup;)Landroid/view/View;
    //   312: astore #16
    //   314: aload #7
    //   316: getfield mScrapActionButtonView : Landroid/view/View;
    //   319: ifnonnull -> 329
    //   322: aload #7
    //   324: aload #16
    //   326: putfield mScrapActionButtonView : Landroid/view/View;
    //   329: aload #7
    //   331: getfield mStrictWidthLimit : Z
    //   334: ifeq -> 357
    //   337: iload #12
    //   339: aload #16
    //   341: iload #9
    //   343: iload #12
    //   345: iload #6
    //   347: iconst_0
    //   348: invokestatic measureChildForCells : (Landroid/view/View;IIII)I
    //   351: isub
    //   352: istore #12
    //   354: goto -> 366
    //   357: aload #16
    //   359: iload #6
    //   361: iload #6
    //   363: invokevirtual measure : (II)V
    //   366: aload #16
    //   368: invokevirtual getMeasuredWidth : ()I
    //   371: istore #17
    //   373: iload #8
    //   375: iload #17
    //   377: isub
    //   378: istore #8
    //   380: iload #4
    //   382: istore_3
    //   383: iload #4
    //   385: ifne -> 391
    //   388: iload #17
    //   390: istore_3
    //   391: aload #15
    //   393: invokevirtual getGroupId : ()I
    //   396: istore #4
    //   398: iload #4
    //   400: ifeq -> 414
    //   403: aload #13
    //   405: iload #4
    //   407: iconst_1
    //   408: invokevirtual put : (IZ)V
    //   411: goto -> 414
    //   414: aload #15
    //   416: iconst_1
    //   417: invokevirtual setIsActionButton : (Z)V
    //   420: iload #11
    //   422: istore #17
    //   424: iload_3
    //   425: istore #4
    //   427: goto -> 825
    //   430: aload #15
    //   432: invokevirtual requestsActionButton : ()Z
    //   435: ifeq -> 815
    //   438: aload #15
    //   440: invokevirtual getGroupId : ()I
    //   443: istore #18
    //   445: aload #13
    //   447: iload #18
    //   449: invokevirtual get : (I)Z
    //   452: istore #19
    //   454: iload #11
    //   456: ifgt -> 470
    //   459: iload #19
    //   461: ifeq -> 467
    //   464: goto -> 470
    //   467: goto -> 494
    //   470: iload #8
    //   472: ifle -> 494
    //   475: aload #7
    //   477: getfield mStrictWidthLimit : Z
    //   480: ifeq -> 488
    //   483: iload #12
    //   485: ifle -> 494
    //   488: iconst_1
    //   489: istore #20
    //   491: goto -> 497
    //   494: iconst_0
    //   495: istore #20
    //   497: iload #11
    //   499: istore_3
    //   500: iload #20
    //   502: ifeq -> 670
    //   505: aload #7
    //   507: aload #15
    //   509: aload #7
    //   511: getfield mScrapActionButtonView : Landroid/view/View;
    //   514: aload_1
    //   515: invokevirtual getItemView : (Landroid/support/v7/view/menu/MenuItemImpl;Landroid/view/View;Landroid/view/ViewGroup;)Landroid/view/View;
    //   518: astore #16
    //   520: aload #7
    //   522: getfield mScrapActionButtonView : Landroid/view/View;
    //   525: ifnonnull -> 535
    //   528: aload #7
    //   530: aload #16
    //   532: putfield mScrapActionButtonView : Landroid/view/View;
    //   535: aload #7
    //   537: getfield mStrictWidthLimit : Z
    //   540: ifeq -> 575
    //   543: aload #16
    //   545: iload #9
    //   547: iload #12
    //   549: iload #6
    //   551: iconst_0
    //   552: invokestatic measureChildForCells : (Landroid/view/View;IIII)I
    //   555: istore #11
    //   557: iload #12
    //   559: iload #11
    //   561: isub
    //   562: istore #12
    //   564: iload #11
    //   566: ifne -> 572
    //   569: iconst_0
    //   570: istore #20
    //   572: goto -> 584
    //   575: aload #16
    //   577: iload #6
    //   579: iload #6
    //   581: invokevirtual measure : (II)V
    //   584: aload #16
    //   586: invokevirtual getMeasuredWidth : ()I
    //   589: istore #17
    //   591: iload #8
    //   593: iload #17
    //   595: isub
    //   596: istore #8
    //   598: iload #4
    //   600: istore #11
    //   602: iload #4
    //   604: ifne -> 611
    //   607: iload #17
    //   609: istore #11
    //   611: aload #7
    //   613: getfield mStrictWidthLimit : Z
    //   616: ifeq -> 643
    //   619: iload #8
    //   621: iflt -> 630
    //   624: iconst_1
    //   625: istore #4
    //   627: goto -> 633
    //   630: iconst_0
    //   631: istore #4
    //   633: iload #20
    //   635: iload #4
    //   637: iand
    //   638: istore #20
    //   640: goto -> 674
    //   643: iload #8
    //   645: iload #11
    //   647: iadd
    //   648: ifle -> 657
    //   651: iconst_1
    //   652: istore #4
    //   654: goto -> 660
    //   657: iconst_0
    //   658: istore #4
    //   660: iload #20
    //   662: iload #4
    //   664: iand
    //   665: istore #20
    //   667: goto -> 674
    //   670: iload #4
    //   672: istore #11
    //   674: iload #20
    //   676: ifeq -> 698
    //   679: iload #18
    //   681: ifeq -> 698
    //   684: aload #13
    //   686: iload #18
    //   688: iconst_1
    //   689: invokevirtual put : (IZ)V
    //   692: iload_3
    //   693: istore #4
    //   695: goto -> 785
    //   698: iload #19
    //   700: ifeq -> 782
    //   703: aload #13
    //   705: iload #18
    //   707: iconst_0
    //   708: invokevirtual put : (IZ)V
    //   711: iconst_0
    //   712: istore #17
    //   714: iload_3
    //   715: istore #4
    //   717: iload #17
    //   719: iload #14
    //   721: if_icmpge -> 779
    //   724: aload_2
    //   725: iload #17
    //   727: invokevirtual get : (I)Ljava/lang/Object;
    //   730: checkcast android/support/v7/view/menu/MenuItemImpl
    //   733: astore #7
    //   735: iload #4
    //   737: istore_3
    //   738: aload #7
    //   740: invokevirtual getGroupId : ()I
    //   743: iload #18
    //   745: if_icmpne -> 770
    //   748: iload #4
    //   750: istore_3
    //   751: aload #7
    //   753: invokevirtual isActionButton : ()Z
    //   756: ifeq -> 764
    //   759: iload #4
    //   761: iconst_1
    //   762: iadd
    //   763: istore_3
    //   764: aload #7
    //   766: iconst_0
    //   767: invokevirtual setIsActionButton : (Z)V
    //   770: iinc #17, 1
    //   773: iload_3
    //   774: istore #4
    //   776: goto -> 717
    //   779: goto -> 785
    //   782: iload_3
    //   783: istore #4
    //   785: iload #4
    //   787: istore_3
    //   788: iload #20
    //   790: ifeq -> 798
    //   793: iload #4
    //   795: iconst_1
    //   796: isub
    //   797: istore_3
    //   798: aload #15
    //   800: iload #20
    //   802: invokevirtual setIsActionButton : (Z)V
    //   805: iload_3
    //   806: istore #17
    //   808: iload #11
    //   810: istore #4
    //   812: goto -> 825
    //   815: aload #15
    //   817: iconst_0
    //   818: invokevirtual setIsActionButton : (Z)V
    //   821: iload #11
    //   823: istore #17
    //   825: iinc #14, 1
    //   828: iload #17
    //   830: istore #11
    //   832: goto -> 270
    //   835: iconst_1
    //   836: ireturn
  }
  
  public View getItemView(MenuItemImpl paramMenuItemImpl, View paramView, ViewGroup paramViewGroup) {
    boolean bool;
    View view = paramMenuItemImpl.getActionView();
    if (view == null || paramMenuItemImpl.hasCollapsibleActionView())
      view = super.getItemView(paramMenuItemImpl, paramView, paramViewGroup); 
    if (paramMenuItemImpl.isActionViewExpanded()) {
      bool = true;
    } else {
      bool = false;
    } 
    view.setVisibility(bool);
    ActionMenuView actionMenuView = (ActionMenuView)paramViewGroup;
    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
    if (!actionMenuView.checkLayoutParams(layoutParams))
      view.setLayoutParams((ViewGroup.LayoutParams)actionMenuView.generateLayoutParams(layoutParams)); 
    return view;
  }
  
  public MenuView getMenuView(ViewGroup paramViewGroup) {
    MenuView menuView2 = this.mMenuView;
    MenuView menuView1 = super.getMenuView(paramViewGroup);
    if (menuView2 != menuView1)
      ((ActionMenuView)menuView1).setPresenter(this); 
    return menuView1;
  }
  
  public Drawable getOverflowIcon() {
    OverflowMenuButton overflowMenuButton = this.mOverflowButton;
    return (overflowMenuButton != null) ? overflowMenuButton.getDrawable() : (this.mPendingOverflowIconSet ? this.mPendingOverflowIcon : null);
  }
  
  public boolean hideOverflowMenu() {
    if (this.mPostedOpenRunnable != null && this.mMenuView != null) {
      ((View)this.mMenuView).removeCallbacks(this.mPostedOpenRunnable);
      this.mPostedOpenRunnable = null;
      return true;
    } 
    OverflowPopup overflowPopup = this.mOverflowPopup;
    if (overflowPopup != null) {
      overflowPopup.dismiss();
      return true;
    } 
    return false;
  }
  
  public boolean hideSubMenus() {
    ActionButtonSubmenu actionButtonSubmenu = this.mActionButtonPopup;
    if (actionButtonSubmenu != null) {
      actionButtonSubmenu.dismiss();
      return true;
    } 
    return false;
  }
  
  public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder) {
    super.initForMenu(paramContext, paramMenuBuilder);
    Resources resources = paramContext.getResources();
    ActionBarPolicy actionBarPolicy = ActionBarPolicy.get(paramContext);
    if (!this.mReserveOverflowSet)
      this.mReserveOverflow = actionBarPolicy.showsOverflowMenuButton(); 
    if (!this.mWidthLimitSet)
      this.mWidthLimit = actionBarPolicy.getEmbeddedMenuWidthLimit(); 
    if (!this.mMaxItemsSet)
      this.mMaxItems = actionBarPolicy.getMaxActionButtons(); 
    int i = this.mWidthLimit;
    if (this.mReserveOverflow) {
      if (this.mOverflowButton == null) {
        OverflowMenuButton overflowMenuButton = new OverflowMenuButton(this.mSystemContext);
        this.mOverflowButton = overflowMenuButton;
        if (this.mPendingOverflowIconSet) {
          overflowMenuButton.setImageDrawable(this.mPendingOverflowIcon);
          this.mPendingOverflowIcon = null;
          this.mPendingOverflowIconSet = false;
        } 
        int j = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.mOverflowButton.measure(j, j);
      } 
      i -= this.mOverflowButton.getMeasuredWidth();
    } else {
      this.mOverflowButton = null;
    } 
    this.mActionItemWidthLimit = i;
    this.mMinCellSize = (int)((resources.getDisplayMetrics()).density * 56.0F);
    this.mScrapActionButtonView = null;
  }
  
  public boolean isOverflowMenuShowPending() {
    return (this.mPostedOpenRunnable != null || isOverflowMenuShowing());
  }
  
  public boolean isOverflowMenuShowing() {
    boolean bool;
    OverflowPopup overflowPopup = this.mOverflowPopup;
    if (overflowPopup != null && overflowPopup.isShowing()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isOverflowReserved() {
    return this.mReserveOverflow;
  }
  
  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {
    dismissPopupMenus();
    super.onCloseMenu(paramMenuBuilder, paramBoolean);
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    if (!this.mMaxItemsSet)
      this.mMaxItems = ActionBarPolicy.get(this.mContext).getMaxActionButtons(); 
    if (this.mMenu != null)
      this.mMenu.onItemsChanged(true); 
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState))
      return; 
    paramParcelable = paramParcelable;
    if (((SavedState)paramParcelable).openSubMenuId > 0) {
      MenuItem menuItem = this.mMenu.findItem(((SavedState)paramParcelable).openSubMenuId);
      if (menuItem != null)
        onSubMenuSelected((SubMenuBuilder)menuItem.getSubMenu()); 
    } 
  }
  
  public Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState();
    savedState.openSubMenuId = this.mOpenSubMenuId;
    return savedState;
  }
  
  public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder) {
    boolean bool2;
    if (!paramSubMenuBuilder.hasVisibleItems())
      return false; 
    SubMenuBuilder subMenuBuilder;
    for (subMenuBuilder = paramSubMenuBuilder; subMenuBuilder.getParentMenu() != this.mMenu; subMenuBuilder = (SubMenuBuilder)subMenuBuilder.getParentMenu());
    View view = findViewForItem(subMenuBuilder.getItem());
    if (view == null)
      return false; 
    this.mOpenSubMenuId = paramSubMenuBuilder.getItem().getItemId();
    boolean bool1 = false;
    int i = paramSubMenuBuilder.size();
    byte b = 0;
    while (true) {
      bool2 = bool1;
      if (b < i) {
        MenuItem menuItem = paramSubMenuBuilder.getItem(b);
        if (menuItem.isVisible() && menuItem.getIcon() != null) {
          bool2 = true;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    ActionButtonSubmenu actionButtonSubmenu = new ActionButtonSubmenu(this.mContext, paramSubMenuBuilder, view);
    this.mActionButtonPopup = actionButtonSubmenu;
    actionButtonSubmenu.setForceShowIcon(bool2);
    this.mActionButtonPopup.show();
    super.onSubMenuSelected(paramSubMenuBuilder);
    return true;
  }
  
  public void onSubUiVisibilityChanged(boolean paramBoolean) {
    if (paramBoolean) {
      super.onSubMenuSelected(null);
    } else if (this.mMenu != null) {
      this.mMenu.close(false);
    } 
  }
  
  public void setExpandedActionViewsExclusive(boolean paramBoolean) {
    this.mExpandedActionViewsExclusive = paramBoolean;
  }
  
  public void setItemLimit(int paramInt) {
    this.mMaxItems = paramInt;
    this.mMaxItemsSet = true;
  }
  
  public void setMenuView(ActionMenuView paramActionMenuView) {
    this.mMenuView = paramActionMenuView;
    paramActionMenuView.initialize(this.mMenu);
  }
  
  public void setOverflowIcon(Drawable paramDrawable) {
    OverflowMenuButton overflowMenuButton = this.mOverflowButton;
    if (overflowMenuButton != null) {
      overflowMenuButton.setImageDrawable(paramDrawable);
    } else {
      this.mPendingOverflowIconSet = true;
      this.mPendingOverflowIcon = paramDrawable;
    } 
  }
  
  public void setReserveOverflow(boolean paramBoolean) {
    this.mReserveOverflow = paramBoolean;
    this.mReserveOverflowSet = true;
  }
  
  public void setWidthLimit(int paramInt, boolean paramBoolean) {
    this.mWidthLimit = paramInt;
    this.mStrictWidthLimit = paramBoolean;
    this.mWidthLimitSet = true;
  }
  
  public boolean shouldIncludeItem(int paramInt, MenuItemImpl paramMenuItemImpl) {
    return paramMenuItemImpl.isActionButton();
  }
  
  public boolean showOverflowMenu() {
    if (this.mReserveOverflow && !isOverflowMenuShowing() && this.mMenu != null && this.mMenuView != null && this.mPostedOpenRunnable == null && !this.mMenu.getNonActionItems().isEmpty()) {
      this.mPostedOpenRunnable = new OpenOverflowRunnable(new OverflowPopup(this.mContext, this.mMenu, (View)this.mOverflowButton, true));
      ((View)this.mMenuView).post(this.mPostedOpenRunnable);
      super.onSubMenuSelected(null);
      return true;
    } 
    return false;
  }
  
  public void updateMenuView(boolean paramBoolean) {
    ArrayList<MenuItemImpl> arrayList;
    super.updateMenuView(paramBoolean);
    ((View)this.mMenuView).requestLayout();
    if (this.mMenu != null) {
      ArrayList<MenuItemImpl> arrayList1 = this.mMenu.getActionItems();
      int k = arrayList1.size();
      for (byte b = 0; b < k; b++) {
        arrayList = (ArrayList<MenuItemImpl>)((MenuItemImpl)arrayList1.get(b)).getSupportActionProvider();
        if (arrayList != null)
          arrayList.setSubUiVisibilityListener(this); 
      } 
    } 
    if (this.mMenu != null) {
      arrayList = this.mMenu.getNonActionItems();
    } else {
      arrayList = null;
    } 
    int i = 0;
    int j = i;
    if (this.mReserveOverflow) {
      j = i;
      if (arrayList != null) {
        i = arrayList.size();
        j = 0;
        if (i == 1) {
          j = ((MenuItemImpl)arrayList.get(0)).isActionViewExpanded() ^ true;
        } else if (i > 0) {
          j = 1;
        } 
      } 
    } 
    if (j != 0) {
      if (this.mOverflowButton == null)
        this.mOverflowButton = new OverflowMenuButton(this.mSystemContext); 
      ViewGroup viewGroup = (ViewGroup)this.mOverflowButton.getParent();
      if (viewGroup != this.mMenuView) {
        if (viewGroup != null)
          viewGroup.removeView((View)this.mOverflowButton); 
        viewGroup = (ActionMenuView)this.mMenuView;
        viewGroup.addView((View)this.mOverflowButton, (ViewGroup.LayoutParams)viewGroup.generateOverflowButtonLayoutParams());
      } 
    } else {
      OverflowMenuButton overflowMenuButton = this.mOverflowButton;
      if (overflowMenuButton != null && overflowMenuButton.getParent() == this.mMenuView)
        ((ViewGroup)this.mMenuView).removeView((View)this.mOverflowButton); 
    } 
    ((ActionMenuView)this.mMenuView).setOverflowReserved(this.mReserveOverflow);
  }
  
  private class ActionButtonSubmenu extends MenuPopupHelper {
    public ActionButtonSubmenu(Context param1Context, SubMenuBuilder param1SubMenuBuilder, View param1View) {
      super(param1Context, (MenuBuilder)param1SubMenuBuilder, param1View, false, R.attr.actionOverflowMenuStyle);
      if (!((MenuItemImpl)param1SubMenuBuilder.getItem()).isActionButton()) {
        ActionMenuPresenter.OverflowMenuButton overflowMenuButton;
        if (ActionMenuPresenter.this.mOverflowButton == null) {
          View view = (View)ActionMenuPresenter.this.mMenuView;
        } else {
          overflowMenuButton = ActionMenuPresenter.this.mOverflowButton;
        } 
        setAnchorView((View)overflowMenuButton);
      } 
      setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
    }
    
    protected void onDismiss() {
      ActionMenuPresenter.this.mActionButtonPopup = null;
      ActionMenuPresenter.this.mOpenSubMenuId = 0;
      super.onDismiss();
    }
  }
  
  private class ActionMenuPopupCallback extends ActionMenuItemView.PopupCallback {
    public ShowableListMenu getPopup() {
      ShowableListMenu showableListMenu;
      if (ActionMenuPresenter.this.mActionButtonPopup != null) {
        showableListMenu = (ShowableListMenu)ActionMenuPresenter.this.mActionButtonPopup.getPopup();
      } else {
        showableListMenu = null;
      } 
      return showableListMenu;
    }
  }
  
  private class OpenOverflowRunnable implements Runnable {
    private ActionMenuPresenter.OverflowPopup mPopup;
    
    public OpenOverflowRunnable(ActionMenuPresenter.OverflowPopup param1OverflowPopup) {
      this.mPopup = param1OverflowPopup;
    }
    
    public void run() {
      if (ActionMenuPresenter.this.mMenu != null)
        ActionMenuPresenter.this.mMenu.changeMenuMode(); 
      View view = (View)ActionMenuPresenter.this.mMenuView;
      if (view != null && view.getWindowToken() != null && this.mPopup.tryShow())
        ActionMenuPresenter.this.mOverflowPopup = this.mPopup; 
      ActionMenuPresenter.this.mPostedOpenRunnable = null;
    }
  }
  
  private class OverflowMenuButton extends AppCompatImageView implements ActionMenuView.ActionMenuChildView {
    private final float[] mTempPts = new float[2];
    
    public OverflowMenuButton(Context param1Context) {
      super(param1Context, (AttributeSet)null, R.attr.actionOverflowButtonStyle);
      setClickable(true);
      setFocusable(true);
      setVisibility(0);
      setEnabled(true);
      TooltipCompat.setTooltipText((View)this, getContentDescription());
      setOnTouchListener(new ForwardingListener((View)this) {
            public ShowableListMenu getPopup() {
              return (ShowableListMenu)((ActionMenuPresenter.this.mOverflowPopup == null) ? null : ActionMenuPresenter.this.mOverflowPopup.getPopup());
            }
            
            public boolean onForwardingStarted() {
              ActionMenuPresenter.this.showOverflowMenu();
              return true;
            }
            
            public boolean onForwardingStopped() {
              if (ActionMenuPresenter.this.mPostedOpenRunnable != null)
                return false; 
              ActionMenuPresenter.this.hideOverflowMenu();
              return true;
            }
          });
    }
    
    public boolean needsDividerAfter() {
      return false;
    }
    
    public boolean needsDividerBefore() {
      return false;
    }
    
    public boolean performClick() {
      if (super.performClick())
        return true; 
      playSoundEffect(0);
      ActionMenuPresenter.this.showOverflowMenu();
      return true;
    }
    
    protected boolean setFrame(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      boolean bool = super.setFrame(param1Int1, param1Int2, param1Int3, param1Int4);
      Drawable drawable1 = getDrawable();
      Drawable drawable2 = getBackground();
      if (drawable1 != null && drawable2 != null) {
        int i = getWidth();
        param1Int2 = getHeight();
        param1Int1 = Math.max(i, param1Int2) / 2;
        int j = getPaddingLeft();
        int k = getPaddingRight();
        param1Int4 = getPaddingTop();
        param1Int3 = getPaddingBottom();
        i = (i + j - k) / 2;
        param1Int2 = (param1Int2 + param1Int4 - param1Int3) / 2;
        DrawableCompat.setHotspotBounds(drawable2, i - param1Int1, param1Int2 - param1Int1, i + param1Int1, param1Int2 + param1Int1);
      } 
      return bool;
    }
  }
  
  class null extends ForwardingListener {
    null(View param1View) {
      super(param1View);
    }
    
    public ShowableListMenu getPopup() {
      return (ShowableListMenu)((ActionMenuPresenter.this.mOverflowPopup == null) ? null : ActionMenuPresenter.this.mOverflowPopup.getPopup());
    }
    
    public boolean onForwardingStarted() {
      ActionMenuPresenter.this.showOverflowMenu();
      return true;
    }
    
    public boolean onForwardingStopped() {
      if (ActionMenuPresenter.this.mPostedOpenRunnable != null)
        return false; 
      ActionMenuPresenter.this.hideOverflowMenu();
      return true;
    }
  }
  
  private class OverflowPopup extends MenuPopupHelper {
    public OverflowPopup(Context param1Context, MenuBuilder param1MenuBuilder, View param1View, boolean param1Boolean) {
      super(param1Context, param1MenuBuilder, param1View, param1Boolean, R.attr.actionOverflowMenuStyle);
      setGravity(8388613);
      setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
    }
    
    protected void onDismiss() {
      if (ActionMenuPresenter.this.mMenu != null)
        ActionMenuPresenter.this.mMenu.close(); 
      ActionMenuPresenter.this.mOverflowPopup = null;
      super.onDismiss();
    }
  }
  
  private class PopupPresenterCallback implements MenuPresenter.Callback {
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {
      if (param1MenuBuilder instanceof SubMenuBuilder)
        param1MenuBuilder.getRootMenu().close(false); 
      MenuPresenter.Callback callback = ActionMenuPresenter.this.getCallback();
      if (callback != null)
        callback.onCloseMenu(param1MenuBuilder, param1Boolean); 
    }
    
    public boolean onOpenSubMenu(MenuBuilder param1MenuBuilder) {
      boolean bool = false;
      if (param1MenuBuilder == null)
        return false; 
      ActionMenuPresenter.this.mOpenSubMenuId = ((SubMenuBuilder)param1MenuBuilder).getItem().getItemId();
      MenuPresenter.Callback callback = ActionMenuPresenter.this.getCallback();
      if (callback != null)
        bool = callback.onOpenSubMenu(param1MenuBuilder); 
      return bool;
    }
  }
  
  private static class SavedState implements Parcelable {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public ActionMenuPresenter.SavedState createFromParcel(Parcel param2Parcel) {
          return new ActionMenuPresenter.SavedState(param2Parcel);
        }
        
        public ActionMenuPresenter.SavedState[] newArray(int param2Int) {
          return new ActionMenuPresenter.SavedState[param2Int];
        }
      };
    
    public int openSubMenuId;
    
    SavedState() {}
    
    SavedState(Parcel param1Parcel) {
      this.openSubMenuId = param1Parcel.readInt();
    }
    
    public int describeContents() {
      return 0;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      param1Parcel.writeInt(this.openSubMenuId);
    }
  }
  
  static final class null implements Parcelable.Creator<SavedState> {
    public ActionMenuPresenter.SavedState createFromParcel(Parcel param1Parcel) {
      return new ActionMenuPresenter.SavedState(param1Parcel);
    }
    
    public ActionMenuPresenter.SavedState[] newArray(int param1Int) {
      return new ActionMenuPresenter.SavedState[param1Int];
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v7/widget/ActionMenuPresenter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */