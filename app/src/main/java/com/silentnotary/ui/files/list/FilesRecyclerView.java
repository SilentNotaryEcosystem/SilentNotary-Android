package com.silentnotary.ui.files.list;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.silentnotary.R;
import com.silentnotary.databinding.FilesRecyclerviewItemBinding;
import com.silentnotary.databinding.FilesRecyclerviewPendingitemBinding;
import com.silentnotary.model.File;
import com.silentnotary.model.PendingFile;
import com.silentnotary.widget.ImageButtonEffect;

/**
 * Created by albert on 9/27/17.
 */

public class FilesRecyclerView extends RecyclerView {

    private static final int TYPE_FILE = 0;
    private static final int TYPE_PENDING_FILE = 1;

    public interface ActionClick {
        void onItemClick(File file);
    }

    public FilesRecyclerView(Context context) {
        super(context);
        initAdapter();
    }

    public FilesRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAdapter();
    }

    public void setOnActionClick(ActionClick actionClick) {
        ((FilesAdapter) getAdapter())
                .setOnActionClick(actionClick);
    }

    private void initAdapter() {
        this.setAdapter(new FilesAdapter());
    }

    public void dataChange(List<File> files) {
        if (((FilesAdapter) getAdapter()).files.hashCode() != files.hashCode())
            ((FilesAdapter) getAdapter())
                    .newList(files);
    }

    public class FilesAdapter extends Adapter<FilesAdapter.FilesViewHolder> {
        List<File> files = new ArrayList<>();
        private ActionClick onActionClick;
        HashSet<File> fileDownloadingInProgress = new HashSet<>();

        public void newList(List<File> files) {
            this.files.clear();
            notifyDataSetChanged();
            this.files.addAll(files);
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return this.files.get(position) instanceof PendingFile ?
                    TYPE_PENDING_FILE : TYPE_FILE;
        }

        @Override
        public FilesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int resId = viewType == TYPE_FILE ?
                    R.layout.files_recyclerview_item : R.layout.files_recyclerview_pendingitem;

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(resId, parent, false);

            return new FilesViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(FilesViewHolder holder, int position) {
            holder.bind(this.files.get(position));
        }

        @Override
        public int getItemCount() {
            return this.files.size();
        }

        public void setOnActionClick(ActionClick onActionClick) {
            this.onActionClick = onActionClick;
        }

        class FilesViewHolder extends ViewHolder {
            private FilesRecyclerviewItemBinding bindingFiles;
            private FilesRecyclerviewPendingitemBinding bindingPedingFiles;
            private View view;
            private final int viewType;

            public void bind(File file) {
                if (viewType == TYPE_FILE) {
                    bindingFiles = DataBindingUtil.bind(itemView);
                    bindingFiles.setItem(file);
                    itemView.setOnTouchListener(new ImageButtonEffect(view -> {
                                if (onActionClick != null) {
                                    onActionClick.onItemClick(file);
                                }
                            })
                    );
                } else if (viewType == TYPE_PENDING_FILE) {
                    PendingFile pendingFile = (PendingFile) file;
                    bindingPedingFiles = DataBindingUtil.bind(itemView);
                    bindingPedingFiles.setItem(pendingFile);
                }
            }

            public FilesViewHolder(View itemView, int viewType) {
                super(itemView);
                this.view = itemView;
                this.viewType = viewType;
            }
        }
    }
}
