package android.support.v7.recyclerview.extensions;

import android.support.v7.util.AdapterListUpdateCallback;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;
import java.util.List;

public abstract class ListAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
  private final AsyncListDiffer<T> mHelper;
  
  protected ListAdapter(AsyncDifferConfig<T> paramAsyncDifferConfig) {
    this.mHelper = new AsyncListDiffer<>((ListUpdateCallback)new AdapterListUpdateCallback(this), paramAsyncDifferConfig);
  }
  
  protected ListAdapter(DiffUtil.ItemCallback<T> paramItemCallback) {
    this.mHelper = new AsyncListDiffer<>((ListUpdateCallback)new AdapterListUpdateCallback(this), (new AsyncDifferConfig.Builder<>(paramItemCallback)).build());
  }
  
  protected T getItem(int paramInt) {
    return this.mHelper.getCurrentList().get(paramInt);
  }
  
  public int getItemCount() {
    return this.mHelper.getCurrentList().size();
  }
  
  public void submitList(List<T> paramList) {
    this.mHelper.submitList(paramList);
  }
}


/* Location:              /root/Downloads/trendmicro/mobile2/test/classes-dex2jar.jar!/android/support/v7/recyclerview/extensions/ListAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */