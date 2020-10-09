package android.support.constraint.solver.widgets;

import java.util.ArrayList;

public class Snapshot {
  private ArrayList<Connection> mConnections = new ArrayList<>();
  
  private int mHeight;
  
  private int mWidth;
  
  private int mX;
  
  private int mY;
  
  public Snapshot(ConstraintWidget paramConstraintWidget) {
    this.mX = paramConstraintWidget.getX();
    this.mY = paramConstraintWidget.getY();
    this.mWidth = paramConstraintWidget.getWidth();
    this.mHeight = paramConstraintWidget.getHeight();
    ArrayList<ConstraintAnchor> arrayList = paramConstraintWidget.getAnchors();
    byte b = 0;
    int i = arrayList.size();
    while (b < i) {
      ConstraintAnchor constraintAnchor = arrayList.get(b);
      this.mConnections.add(new Connection(constraintAnchor));
      b++;
    } 
  }
  
  public void applyTo(ConstraintWidget paramConstraintWidget) {
    paramConstraintWidget.setX(this.mX);
    paramConstraintWidget.setY(this.mY);
    paramConstraintWidget.setWidth(this.mWidth);
    paramConstraintWidget.setHeight(this.mHeight);
    byte b = 0;
    int i = this.mConnections.size();
    while (b < i) {
      ((Connection)this.mConnections.get(b)).applyTo(paramConstraintWidget);
      b++;
    } 
  }
  
  public void updateFrom(ConstraintWidget paramConstraintWidget) {
    this.mX = paramConstraintWidget.getX();
    this.mY = paramConstraintWidget.getY();
    this.mWidth = paramConstraintWidget.getWidth();
    this.mHeight = paramConstraintWidget.getHeight();
    int i = this.mConnections.size();
    for (byte b = 0; b < i; b++)
      ((Connection)this.mConnections.get(b)).updateFrom(paramConstraintWidget); 
  }
  
  static class Connection {
    private ConstraintAnchor mAnchor;
    
    private int mCreator;
    
    private int mMargin;
    
    private ConstraintAnchor.Strength mStrengh;
    
    private ConstraintAnchor mTarget;
    
    public Connection(ConstraintAnchor param1ConstraintAnchor) {
      this.mAnchor = param1ConstraintAnchor;
      this.mTarget = param1ConstraintAnchor.getTarget();
      this.mMargin = param1ConstraintAnchor.getMargin();
      this.mStrengh = param1ConstraintAnchor.getStrength();
      this.mCreator = param1ConstraintAnchor.getConnectionCreator();
    }
    
    public void applyTo(ConstraintWidget param1ConstraintWidget) {
      param1ConstraintWidget.getAnchor(this.mAnchor.getType()).connect(this.mTarget, this.mMargin, this.mStrengh, this.mCreator);
    }
    
    public void updateFrom(ConstraintWidget param1ConstraintWidget) {
      ConstraintAnchor constraintAnchor = param1ConstraintWidget.getAnchor(this.mAnchor.getType());
      this.mAnchor = constraintAnchor;
      if (constraintAnchor != null) {
        this.mTarget = constraintAnchor.getTarget();
        this.mMargin = this.mAnchor.getMargin();
        this.mStrengh = this.mAnchor.getStrength();
        this.mCreator = this.mAnchor.getConnectionCreator();
      } else {
        this.mTarget = null;
        this.mMargin = 0;
        this.mStrengh = ConstraintAnchor.Strength.STRONG;
        this.mCreator = 0;
      } 
    }
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/constraint/solver/widgets/Snapshot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */