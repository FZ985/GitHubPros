package api29.libs.adapter.base.diff;

import java.util.List;

import androidx.annotation.NonNull;

public interface ListChangeListener<T> {
    /**
     * Called after the current List has been updated.
     *
     * @param previousList The previous list.
     * @param currentList The new current list.
     */
    void onCurrentListChanged(@NonNull List<T> previousList, @NonNull List<T> currentList);
}
