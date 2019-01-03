package com.example.breezil.pixxo.ui.saved_edit;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.databinding.FragmentEditSavedBinding;
import com.example.breezil.pixxo.model.EditedModel;
import com.example.breezil.pixxo.ui.adapter.EditImageGridAdapter;
import com.example.breezil.pixxo.utils.Constant;
import com.example.breezil.pixxo.utils.helper.BitmapHelper;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dagger.android.support.AndroidSupportInjection;

import static com.example.breezil.pixxo.utils.Constant.PIXXO_EDITED;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditSavedFragment extends Fragment implements AdapterView.OnItemClickListener {

    List<EditedModel> gridItems;
    GridView gridView;

    public EditSavedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_edit_saved, container, false);

        gridView =  view.findViewById(R.id.editGrid);

        if(PIXXO_EDITED != null){
            setGridAdapter(PIXXO_EDITED);
        }
        return view;

    }

    private void setGridAdapter(String path) {


        gridItems = createGridItems(path);

        EditImageGridAdapter adapter = new EditImageGridAdapter(getActivity(), gridItems,getFragmentManager());
        Collections.reverse(gridItems);

        gridView.setAdapter(adapter);
    }

    private List<EditedModel> createGridItems(String directoryPath) {
        List<EditedModel> items = new ArrayList<>();

        // List all the items within the folder.

        File[] files = new File(directoryPath).listFiles(new ImageFileFilter());

        for (File file : files) {

            // Add the directories containing images or sub-directories
            if (file.isDirectory()
                    && file.listFiles(new ImageFileFilter()).length > 0) {

                items.add(new EditedModel(file.getAbsolutePath(), true, null));
            }
            // Add the images
            else {
                Bitmap image = BitmapHelper.decodeBitmapFromFile(file.getAbsolutePath());
                items.add(new EditedModel(file.getAbsolutePath(), false, image));
            }

        }

        return items;
    }

    private boolean isImageFile(String filePath) {
        if (filePath.endsWith(".jpg") || filePath.endsWith(".png"))
        // Add other formats as desired
        {
            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (gridItems.get(position).isDirectory()) {
            setGridAdapter(gridItems.get(position).getPath());
        }
        else {
            // Display the image
        }
    }



    /**
     * This can be used to filter files.
     */
    private class ImageFileFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) return true;
            else return isImageFile(file.getAbsolutePath());

        }
    }
}
