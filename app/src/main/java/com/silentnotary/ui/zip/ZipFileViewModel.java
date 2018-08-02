package com.silentnotary.ui.zip;

import com.trello.rxlifecycle2.components.support.RxFragment;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.silentnotary.util.FileUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by albert on 10/27/17.
 */

public class ZipFileViewModel {
    private RxFragment fragment;

    public ZipFileViewModel(RxFragment fragment) {
        this.fragment = fragment;
    }

    private List<ZipFileModel> unzipArchive(String zipLocation) {
        ArrayList<ZipFileModel> zipFileModels = new ArrayList<>();
        String name = new File(zipLocation).getName();
        int pos = name.lastIndexOf(".");
        if (pos > 0) {
            name = name.substring(0, pos);
        }

        name += "_upziped";

        try {
            File unzipedDir = FileUtil.getFileFromDownloadFolder(name, "unziped");
            unzipedDir.mkdirs();

            ZipFile zipFile = new ZipFile(zipLocation);
            zipFile.extractAll(unzipedDir.getAbsolutePath());

            File[] files = unzipedDir.listFiles();
            for (File file : files) {
                zipFileModels.add(new ZipFileModel(file));
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }

        return zipFileModels;
    }

    public io.reactivex.Observable<List<ZipFileModel>> unzip(String zipLocation) {
        return Observable.<List<ZipFileModel>>create(s -> {
            s.onNext(unzipArchive(zipLocation));
            s.onComplete();
        })
                .compose(fragment.bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
