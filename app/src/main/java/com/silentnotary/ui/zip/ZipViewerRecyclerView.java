package com.silentnotary.ui.zip;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.silentnotary.R;
import com.silentnotary.databinding.ZipListviewItemBinding;
import com.silentnotary.util.ImageUtil;
import com.silentnotary.widget.ImageButtonEffect;
import com.silentnotary.widget.list.AbstractRecyclerView;

/**
 * Created by albert on 10/27/17.
 */

public class ZipViewerRecyclerView extends AbstractRecyclerView<ZipFileModel>{
    public ZipViewerRecyclerView(Context context) {
        super(context);
    }

    public ZipViewerRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public interface OnItemClick{
        void onClick(ZipFileModel zipFileModel);
    }
    OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public int getLayoutId() {
        return R.layout.zip_listview_item;
    }

    @Override
    public void bind(ViewDataBinding bind, ZipFileModel item) {
        ZipListviewItemBinding binding = (ZipListviewItemBinding) bind;

        binding.setItem(item);

        View root = binding.getRoot();
        ImageView imageView = root.findViewById(R.id.imageViewFileType);
        if(ImageUtil.isImage(item.getFile())){
            imageView.setImageResource(R.drawable.ic_picture);
        }else if(item.getFile().getAbsolutePath().endsWith("wav")) {
            imageView.setImageResource(R.drawable.ic_radiomic);
        }

        root.setOnTouchListener(new ImageButtonEffect(view -> {
           if(onItemClick!=null){
               onItemClick.onClick(item);
           }
        }, ImageButtonEffect.LIGHT_GRAY_COLOR));

    }
}
