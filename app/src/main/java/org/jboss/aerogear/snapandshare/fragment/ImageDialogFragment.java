/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.aerogear.snapandshare.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.jboss.aerogear.snapandshare.R;

import java.io.File;

/**
 * Created by summers on 10/31/14.
 */
public class ImageDialogFragment extends DialogFragment {

    private static final String FILE = "org.jboss.aerogear.snapandshare.FILE";

    public static ImageDialogFragment newInstance(File file) {

        Bundle arguments = new Bundle();
        arguments.putString(FILE, file.getAbsolutePath());

        ImageDialogFragment fragment = new ImageDialogFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_dialog, null);
        ImageView image = (ImageView) view.findViewById(R.id.image);

        File file = new File(getArguments().getString(FILE));

        Picasso.with(getActivity())
                .load(file)
                .resizeDimen(R.dimen.image_dialog_width, R.dimen.image_dialog_height)
                .centerCrop()
                .into(image);

        return view;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
