package com.silentnotary.widget;

import android.content.Context;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.File;

import com.silentnotary.R;

import io.reactivex.Observable;

/**
 * Created by albert on 9/28/17.
 */

public class FilePickerBuilder {


    public static Observable<File> build(Context context) {
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.MULTI_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;

        FilePickerDialog dialog = new FilePickerDialog(context, properties);
        dialog.setTitle(context.getString(R.string.select_file));
        dialog.show();
        return Observable.create(s -> {
            dialog.setDialogSelectionListener(files -> {
                if (files.length > 0) {
                    for (String location : files) {
                        s.onNext(new File(location));
                    }
                    s.onComplete();
                }
            });
        });


    }
}
