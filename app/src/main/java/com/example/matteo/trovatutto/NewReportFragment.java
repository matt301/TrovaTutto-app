package com.example.matteo.trovatutto;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

public class NewReportFragment extends Fragment  implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private AppCompatButton btn_sendreport, btn_insertfoto;
    private EditText et_report_title, et_report_subtitle, et_report_address,et_report_description;
    private TextView tv_report_category;
    private Spinner category_spinner;
    private ProgressBar progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_report,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view){

        btn_sendreport        = view.findViewById(R.id.btn_sendreport);
        btn_insertfoto        = view.findViewById(R.id.btn_insertfoto);

        category_spinner      = view.findViewById(R.id.sp_report_category);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.category_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            category_spinner.setAdapter(adapter);

        et_report_title       = view.findViewById(R.id.et_report_title);
        et_report_subtitle    = view.findViewById(R.id.et_report_subtitle);
        tv_report_category    = view.findViewById(R.id.tv_report_category);
        et_report_address     = view.findViewById(R.id.et_report_address);
        et_report_description = view.findViewById(R.id.et_report_description);

        progress              = view.findViewById(R.id.progress);

        btn_sendreport.setOnClickListener(this);
        btn_insertfoto.setOnClickListener(this);
        category_spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    public void onClick(View v) {}

}
