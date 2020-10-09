package android.support.v7.util;

interface ThreadUtil<T> {
  BackgroundCallback<T> getBackgroundProxy(BackgroundCallback<T> paramBackgroundCallback);
  
  MainThreadCallback<T> getMainThreadProxy(MainThreadCallback<T> paramMainThreadCallback);
  
  public static interface BackgroundCallback<T> {
    void loadTile(int param1Int1, int param1Int2);
    
    void recycleTile(TileList.Tile<T> param1Tile);
    
    void refresh(int param1Int);
    
    void updateRange(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5);
  }
  
  public static interface MainThreadCallback<T> {
    void addTile(int param1Int, TileList.Tile<T> param1Tile);
    
    void removeTile(int param1Int1, int param1Int2);
    
    void updateItemCount(int param1Int1, int param1Int2);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v7/util/ThreadUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */