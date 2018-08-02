package com.silentnotary.ui.main.view;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.silentnotary.R;

/**
 * Created by albert on 10/13/17.
 */

public class MainToolbar {
    private Toolbar toolbar;
    private View mBackButton;
    private View mMenuButton;
    private View mAddButton;
    private View mAddFileButton;
    private View mLoader;

    private TextView mTextView;

    public MainToolbar(Toolbar toolbar){
        this.toolbar = toolbar;

        this.mBackButton = toolbar.findViewById(R.id.toolbar_back);
        this.mMenuButton = toolbar.findViewById(R.id.toolbar_menu);
        this.mTextView   = toolbar.findViewById(R.id.toolbar_title);
        this.mAddButton = toolbar.findViewById(R.id.toolbar_addBtb);
        this.mAddFileButton = toolbar.findViewById(R.id.toolbar_addFileBtn);
        this.mLoader = toolbar.findViewById(R.id.toolbar_loader);

    }
    public MainToolbar setOnBackClicked(View.OnClickListener listener){
        this.mBackButton.setOnClickListener(listener);
        return this;
    }

    public void showLoader(){
        this.mLoader.setVisibility(View.VISIBLE);
    }
    public void hideLoader(){
        this.mLoader.setVisibility(View.GONE);
    }

    public void setButtonUploadVisible(boolean visibility){
        this.mAddButton.setVisibility(visibility? View.VISIBLE: View.GONE);
    }
    public void setButtonAddFileVisible(boolean visibility){
        this.mAddFileButton.setVisibility(visibility? View.VISIBLE: View.GONE);
    }
    public MainToolbar setOnUploadFilesButtonClick(View.OnClickListener listener){
        this.mAddButton.setOnClickListener(listener);
        return this;
    }
    public MainToolbar setOnAddFileButtonClick(View.OnClickListener listener){
        this.mAddFileButton.setOnClickListener(listener);
        return this;
    }
    public MainToolbar setOnNavigationButterClick(View.OnClickListener listener){
        this.mMenuButton.setOnClickListener(listener);
        return this;
    }
    public void setBackButtonVisibility(boolean visibility){
        int backButtonVisibility =
                visibility? View.VISIBLE: View.GONE;
        int menuButtonVisibility =
                visibility? View.GONE: View.VISIBLE;
        this.mMenuButton.setVisibility(menuButtonVisibility);
        this.mBackButton.setVisibility(backButtonVisibility);
    }

    public void proceedFragment(IFragmentChangable.FRAGMENT_PAGE page){

        this.setButtonUploadVisible(false);
        this.setButtonAddFileVisible(false);
        switch (page){
            case Conversation:
                setTitle("Conversations");
                setBackButtonVisibility(false);
                break;
            case Files:
                setTitle("My Files");
                setBackButtonVisibility(false);
                this.setButtonUploadVisible(true);
                break;
            case UploadFile:
                setTitle("Upload");
                this.setButtonAddFileVisible(true);
                setBackButtonVisibility(false);
                break;
            case FileDetail:
                setTitle("Details");
                setBackButtonVisibility(true);
                break;
            case ZipViewer:
                setBackButtonVisibility(true);
                setTitle("Browse");
                break;
            case UserProfile:
                setTitle("Profile");
            default:
                break;
        }

    }

    private void setTitle(String title) {
        mTextView.setText(title);
    }
}
