package com.silentnotary.ui.files.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.silentnotary.R;
import com.silentnotary.util.FileUtil;
import com.silentnotary.util.ImageUtil;
import com.silentnotary.widget.ImageButtonEffect;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by albert on 10/3/17.
 */

public class UploadFilesRecyclerView extends RecyclerView {
    public UploadFilesRecyclerView(Context context) {
        super(context);
        initAdapter();
    }

    public UploadFilesRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAdapter();
    }

    private void initAdapter() {
        this.setAdapter(new UploadFilesRecyclerView.FilesAdapter());
    }

    public List<File> getFiles() {
        return ((FilesAdapter) getAdapter()).getFiles();
    }

    public void dataChange(List<File> files) {
        ((FilesAdapter) getAdapter())
                .newList(files);
    }

    public void setOnDataChanged(Runnable onDataChanged) {
        ((FilesAdapter) getAdapter()).setOnDataChanged(onDataChanged);
    }

    public class FilesAdapter extends Adapter<FilesAdapter.FilesViewHolder> {
        public List<File> getFiles() {
            return files;
        }

        List<File> files = new ArrayList<>();
        Runnable ondataChanged;

        public void setOnDataChanged(Runnable ondataChanged) {
            this.ondataChanged = ondataChanged;
        }

        public void removeItem(int index) {
            this.files.remove(index);
            notifyDataSetChanged();

            if (ondataChanged != null)
                ondataChanged.run();
        }

        public void newList(List<File> files) {
            this.files.clear();
            notifyDataSetChanged();
            this.files.addAll(files);
            notifyDataSetChanged();
            if (ondataChanged != null)
                ondataChanged.run();
        }

        public void addItem(File file) {
            this.files.add(file);
            notifyDataSetChanged();
            if (ondataChanged != null)
                ondataChanged.run();
        }

        @Override
        public FilesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.uploadfiles_recyclerview_item, parent, false);
            return new FilesViewHolder(inflate);
        }


        int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) >= reqHeight
                        && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }

        @Override
        public void onBindViewHolder(FilesViewHolder holder, int position) {
            View itemView = holder.getView();
            ImageView thumbnail = itemView.findViewById(R.id.imageViewUploadFile);
            TextView textView = itemView.findViewById(R.id.textViewUploadFile);
            TextView textViewFileSize = itemView.findViewById(R.id.textViewFileSize);
            View removeBtn = itemView.findViewById(R.id.removeButtonUploadFile);
            File file = this.files.get(position);

            try {
                if (ImageUtil.isImage(file)) {
                    thumbnail.setImageResource(R.drawable.loader);
                    io.reactivex.Observable.<Bitmap>create(s -> {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                        options.inSampleSize = calculateInSampleSize(options, 50, 50);

                        options.inJustDecodeBounds = false;
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                        s.onNext(bitmap);
                        s.onComplete();
                    })
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(thumbnail::setImageBitmap);

                } else if (file.getAbsolutePath().endsWith("wav")) {
                    thumbnail.setImageResource(R.drawable.ic_mic_black_24dp);
                } else {
                    thumbnail.setImageResource(R.drawable.ic_insert_drive_file_black_24dp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            textViewFileSize.setText(FileUtil.readableFileSize(file.length()));
            textView.setText(file.getName());

            removeBtn.setOnTouchListener(new ImageButtonEffect(view -> removeItem(position)));

        }

        @Override
        public int getItemCount() {
            return this.files.size();
        }

        class FilesViewHolder extends ViewHolder {
            private View view;

            public View getView() {
                return view;
            }

            public FilesViewHolder(View itemView) {
                super(itemView);
                this.view = itemView;
            }
        }
    }
}
