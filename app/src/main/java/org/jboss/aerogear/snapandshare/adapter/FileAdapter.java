package org.jboss.aerogear.snapandshare.adapter;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.snapandshare.R;
import org.jboss.aerogear.snapandshare.fragment.ImageDialogFragment;
import org.jboss.aerogear.snapandshare.util.KeycloakHelper;

import java.io.File;

/**
 * Created by summers on 11/3/14.
 */
public class FileAdapter  extends BaseAdapter {

    final static File imageDirectory;

    static {
        imageDirectory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!imageDirectory.exists()) {
            if (!imageDirectory.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                throw new RuntimeException("failed to create directory");
            }
        }
    }

    private final Activity context;

    public FileAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageDirectory.listFiles().length;
    }

    @Override
    public File getItem(int position) {
        return imageDirectory.listFiles()[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ImageView image;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.image_card, null);
        }
        image = (ImageView) view.findViewById(R.id.image);
        final File file = getItem(position);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDialogFragment fragment = ImageDialogFragment.newInstance(file);

                fragment.show(context.getFragmentManager(), "TAG!");
            }
        });

        view.findViewById(R.id.share_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeycloakHelper.upload(file, new Callback() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(context, "success", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.e("ERROR", e.getMessage(), e);
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }, context);
            }
        });

        Picasso.with(context)
                .load(file)
                .centerInside()
                .resizeDimen(R.dimen.image_card_width, R.dimen.image_card_height)
                .into(image);

        return view;
    }
}