package com.silentnotary.widget.list;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.silentnotary.R;

/**
 * Created by albert on 10/11/17.
 */

public abstract class AbstractRecyclerView<T> extends RecyclerView {

    public AbstractRecyclerView(Context context) {
        super(context);
        setAdapter(new AbstractAdapter());
    }

    public AbstractRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setAdapter(new AbstractAdapter());
    }

    public AbstractRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setAdapter(new AbstractAdapter());
    }

    public abstract int getLayoutId();

    public abstract void bind(ViewDataBinding binding, T item);

    public void newList(List<T> list) {
        Adapter adapter = getAdapter();
        ((AbstractAdapter) adapter)
                .newList(list);
    }

    public class AbstractAdapter extends Adapter<AbstractAdapter.AbstractViewHolder> {
        private List<T> list = new ArrayList<>();


        public void newList(List<T> list) {
            this.list.clear();
            notifyDataSetChanged();
            this.list.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(getLayoutId(), parent, false);
            return new AbstractViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AbstractViewHolder holder, int position) {
            ViewDataBinding binding = DataBindingUtil.bind(holder.getItemView());
            bind(binding, list.get(position));
        }

        @Override
        public int getItemCount() {
            return this.list.size();
        }

        public class AbstractViewHolder extends ViewHolder {

            private View itemView;

            public View getItemView() {
                return itemView;
            }

            public AbstractViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
            }
        }
    }
}
