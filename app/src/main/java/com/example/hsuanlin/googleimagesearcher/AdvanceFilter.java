package com.example.hsuanlin.googleimagesearcher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.zip.Inflater;

/**
 * Created by hsuanlin on 2015/8/5.
 */
public class AdvanceFilter extends DialogFragment {

    private Spinner spImageSize;
    private Spinner spColorFilter;
    private Spinner spImageType;
    private EditText etSiteFilter;

    public interface AdvanceFilterListener{
        void onFinishSettingFragment(String imageSize, String colorFilter, String imageType, String siteFilter);
    }

    public AdvanceFilter()
    {
    }

    public static AdvanceFilter newInstance()
    {
        AdvanceFilter frag = new AdvanceFilter();
        return frag;
    }
    private void initSetting( View view )
    {
        spImageSize = (Spinner) view.findViewById(R.id.spImageSize);
        spColorFilter = (Spinner) view.findViewById(R.id.spColorFilter);
        spImageType = (Spinner) view.findViewById(R.id.spImageType);
        etSiteFilter = (EditText) view.findViewById(R.id.etSiteFilter);

        String[] sizeOption = {"icon","small","xxlarge","huge"};
        String[] colorOption = {"black","blue","brown","gray","green","orange","pink","purple","red","teal","white","yellow"};
        String[] typeOption = {"face","photo","clipart","lineart"};

        ArrayAdapter<String> sizeList = new ArrayAdapter<String>(view.getContext(), R.layout.support_simple_spinner_dropdown_item, sizeOption);
        ArrayAdapter<String> colorList = new ArrayAdapter<String>(view.getContext(), R.layout.support_simple_spinner_dropdown_item, colorOption);
        ArrayAdapter<String> typeList = new ArrayAdapter<String>(view.getContext(), R.layout.support_simple_spinner_dropdown_item, typeOption);

        spImageSize.setAdapter(sizeList);
        spColorFilter.setAdapter(colorList);
        spImageType.setAdapter(typeList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.advanced_filters, container );

        initSetting(view);

        Button btSubmit = (Button) view.findViewById(R.id.btSubmit);
        Button btCancel = (Button) view.findViewById(R.id.btCancel);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvanceFilterListener listenner = (AdvanceFilterListener) getActivity();
                listenner.onFinishSettingFragment(spImageSize.getSelectedItem().toString(), spColorFilter.getSelectedItem().toString(), spImageType.getSelectedItem().toString(), etSiteFilter.getText().toString());
                dismiss();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }
}
