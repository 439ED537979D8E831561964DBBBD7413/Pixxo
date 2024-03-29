package com.pixxo.breezil.pixxo.ui.saved_edit;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.model.EditedModel;
import com.pixxo.breezil.pixxo.ui.adapter.EditImageGridAdapter;
import com.pixxo.breezil.pixxo.ui.bottom_sheet.ChooseImageBottomDialogFragment;
import com.pixxo.breezil.pixxo.utils.helper.BitmapHelper;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dagger.android.support.AndroidSupportInjection;

import static com.pixxo.breezil.pixxo.utils.Constant.PIXXO_EDITED;
import static com.pixxo.breezil.pixxo.utils.Constant.STORAGE_PERMISSION_CODE;
import static com.pixxo.breezil.pixxo.utils.Constant.ZERO;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditSavedFragment extends Fragment implements AdapterView.OnItemClickListener {

    List<EditedModel> gridItems;
    GridView gridView;
    TextView editEmptyText;
    Button clickEditBtn;
    ChooseImageBottomDialogFragment chooseImageBottomDialogFragment = new ChooseImageBottomDialogFragment();

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
        editEmptyText = view.findViewById(R.id.emptyText);
        clickEditBtn = view.findViewById(R.id.clickToEditbtn);


        if(PIXXO_EDITED != null){
            setGridAdapter(PIXXO_EDITED);
        }
        return view;

    }

    private void setGridAdapter(String path) {
        gridItems = createGridItems(path);

        EditImageGridAdapter adapter = new EditImageGridAdapter(getActivity(), gridItems,getFragmentManager());
        Collections.reverse(gridItems);



        if(gridItems.size() > ZERO){
            gridView.setAdapter(adapter);
        }else {
            editEmptyText.setVisibility(View.VISIBLE);
            clickEditBtn.setVisibility(View.VISIBLE);
            clickEditBtn.setOnClickListener(v -> chooseImageBottomDialogFragment.show(getFragmentManager(),getString(R.string.choose_image))
            );
        }


    }

    private List<EditedModel> createGridItems(String directoryPath) {
        List<EditedModel> items = new ArrayList<>();



        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            // List all the items within the folder..
            File[] files = new File(directoryPath).listFiles(new ImageFileFilter());

            File filePath = new File(PIXXO_EDITED);

            if(filePath.exists()){
                if(files.length > 0 ){
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
                }
            }





        }else{
            ActivityCompat.requestPermissions(getActivity(),
            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }

        return items;
    }

    private boolean isImageFile(String filePath) {
        // Add other formats as desired
        return filePath.endsWith(getString(R.string.jpg)) || filePath.endsWith(getString(R.string.png));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (gridItems.get(position).isDirectory()) {
            setGridAdapter(gridItems.get(position).getPath());
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
