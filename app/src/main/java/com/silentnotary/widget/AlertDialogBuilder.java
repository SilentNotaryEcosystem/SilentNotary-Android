package com.silentnotary.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import com.silentnotary.model.FileToUpload;

import io.reactivex.Observable;

/**
 * Created by albert on 9/28/17.
 */

public class AlertDialogBuilder {

    public static Observable<FileToUpload> buildUploadFileCommentDialog(Context context, Uri fileUri) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final EditText edittext = new EditText(context);
        alert.setTitle("Select file comment");
        alert.setView(edittext);
        return Observable.create(s -> {
            alert.setPositiveButton("Upload", (dialog, whichButton) -> {
                String commentText = edittext.getText().toString();
                s.onNext(new FileToUpload(commentText, fileUri));
                s.onComplete();
            });
            alert.setNegativeButton("Cancel", (dialog, whichButton) -> dialog.dismiss());
            alert.show();
        });
    }
}
