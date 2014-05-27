package com.dragos.androidfilepicker.library;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Dragos Raducanu (raducanu.dragos@gmail.com) on 3/9/14.
 */
public final class ImagePickerConfig {

    final private ArrayList<String> mFileTypes;
    final private String mDisplayMode;
    final private boolean mMultiSelect;


    private ImagePickerConfig(final Builder builder){
        this.mFileTypes = builder.mFileTypes;
        this.mDisplayMode = builder.mDisplayMode;
        this.mMultiSelect = builder.mMultiSelect;

    }
    public static ImagePickerConfig createDefault(Context context) {
        return new Builder(context).build();
    }


    public ArrayList<String> getFileTypes(){
        return this.mFileTypes;
    }

    public String getDisplayMode(){
        return this.mDisplayMode;
    }

    public boolean allowMultiSelect(){
        return this.mMultiSelect;
    }



    public static class Builder {

        private Context context;


        private ArrayList<String> mFileTypes;
        private String mParentPath;
        private String mDisplayMode;

        private boolean mMultiSelect = true;


        public Builder(Context context){
            this.context = context;
        }

        public Builder allowFileTypes(ArrayList<String> fileTypes) {
            this.mFileTypes = fileTypes;
            return this;
        }

        /**
         * sets the display mode for the picker
         * @param displayMode - can be Constants.DISPLAY_MODE_GRID_WITH_ALBUMS, DISPLAY_MODE_GRID_NO_ALBUMS,DISPLAY_MODE_LIST_WITH_ALBUMS or DISPLAY_MODE_LIST_NO_ALBUMS.
         * @return this object in order to chain the config calls.
         */
        public Builder displayMode(String displayMode) {
            this.mDisplayMode = displayMode;
            return this;
        }

        /**
         * sets whether multi selection is enabled or not
         * @param multiSelect status of multi selection
         * @return this object in order to chain the config calls.
         */
        public Builder multiSelect(boolean multiSelect) {
            this.mMultiSelect = multiSelect;
            return this;
        }
        public ImagePickerConfig build() {
            initEmptyFieldsWithDefaultValues();
            return new ImagePickerConfig(this);
        }


        /**
         * Inits the picker with the default configuration.
         */
        private void initEmptyFieldsWithDefaultValues(){
            if(this.mDisplayMode == null) {
                this.mDisplayMode = Constants.DISPLAY_MODE_GRID_WITH_ALBUMS;
            }
        }
    }

}
