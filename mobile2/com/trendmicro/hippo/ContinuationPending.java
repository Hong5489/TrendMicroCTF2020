package com.trendmicro.hippo;

public class ContinuationPending extends RuntimeException {
  private static final long serialVersionUID = 4956008116771118856L;
  
  private Object applicationState;
  
  private NativeContinuation continuationState;
  
  protected ContinuationPending(NativeContinuation paramNativeContinuation) {
    this.continuationState = paramNativeContinuation;
  }
  
  public Object getApplicationState() {
    return this.applicationState;
  }
  
  public Object getContinuation() {
    return this.continuationState;
  }
  
  NativeContinuation getContinuationState() {
    return this.continuationState;
  }
  
  public void setApplicationState(Object paramObject) {
    this.applicationState = paramObject;
  }
  
  public void setContinuation(NativeContinuation paramNativeContinuation) {
    this.continuationState = paramNativeContinuation;
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/com/trendmicro/hippo/ContinuationPending.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */