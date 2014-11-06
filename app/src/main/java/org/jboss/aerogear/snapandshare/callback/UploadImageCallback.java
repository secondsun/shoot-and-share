package org.jboss.aerogear.snapandshare.callback;

import android.widget.Toast;

import org.jboss.aerogear.android.pipeline.AbstractActivityCallback;
import org.jboss.aerogear.snapandshare.PhotoActivity;
import org.jboss.aerogear.snapandshare.PhotoHolder;

/**
 * Created by summers on 11/5/14.
 */
public class UploadImageCallback extends AbstractActivityCallback<PhotoHolder> {

    public UploadImageCallback() {
        super(0x42);
    }

    @Override
    public void onSuccess(PhotoHolder o) {
        ((PhotoActivity)getActivity()).dialog.dismiss();
        Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(Exception e) {
        ((PhotoActivity)getActivity()).dialog.dismiss();
        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
    }

}
