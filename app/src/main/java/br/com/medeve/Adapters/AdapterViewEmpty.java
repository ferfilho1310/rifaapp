package br.com.medeve.Adapters;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class AdapterViewEmpty extends RecyclerView.AdapterDataObserver {

    View view;
    RecyclerView recyclerView;

    public AdapterViewEmpty(View view, RecyclerView recyclerView) {
        this.view = view;
        this.recyclerView = recyclerView;
    }

    private void checkIfEmpty() {
        if (view != null && recyclerView.getAdapter() != null) {
            boolean emptyViewVisible = recyclerView.getAdapter().getItemCount() == 0;
            view.setVisibility(emptyViewVisible ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(emptyViewVisible ? View.GONE : View.VISIBLE);
        }
    }

    public void onChanged() { checkIfEmpty(); }
    public void onItemRangeInserted(int positionStart, int itemCount) { checkIfEmpty(); }
    public void onItemRangeRemoved(int positionStart, int itemCount) { checkIfEmpty(); }

}
