package org.jboss.aerogear.snapandshare;

import org.jboss.aerogear.android.RecordId;

import java.io.File;

/**
 * Created by summers on 11/3/14.
 */
public class PhotoHolder {

    @RecordId
    private Integer id = null;

    private File image;

    public PhotoHolder(File image) {
        this.image = image;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public Integer getId() {
        return null;
    }

    public void setId(Integer id) {
        //noop
    }
}
