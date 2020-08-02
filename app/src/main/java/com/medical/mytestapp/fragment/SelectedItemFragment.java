package com.medical.mytestapp.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.medical.mytestapp.LocalDbManager.DatabaseHelper;
import com.medical.mytestapp.R;
import com.medical.mytestapp.ViewModel.SearchViewModel;
import com.medical.mytestapp.ui.MainActivity;
import static com.medical.mytestapp.LocalDbManager.DatabaseContract.FeedEntry.COLUMN_SEARCH_COMMENT;
import static com.medical.mytestapp.LocalDbManager.DatabaseContract.FeedEntry.COLUMN_SEARCH_ID;
import static com.medical.mytestapp.LocalDbManager.DatabaseContracter.FeedEntry.TABLE_SEARCH;


public class SelectedItemFragment extends Fragment implements View.OnClickListener {

    private Intent intent;
    private String toolbartitle;
    private ImageView imgback;
    private ImageView imgSelected;
    private TextView titleHeader;
    private SearchViewModel mainViewmodel;
    private String imgReceieved;
    private String idReceieved;
    private String savedTitle= null;
    private String imgSelect= null;
    private String id_image= null;
    private Button btnSave;
    private Button btnClear;
    private DatabaseHelper dh= null;
    private TextInputEditText edit_Commentsection=null;
    private String imgID = null;



    public SelectedItemFragment() {
    }


    public static SelectedItemFragment newInstance() {
        SelectedItemFragment fragment = new SelectedItemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        showUIinitialize(v);
        intent = getActivity().getIntent();
       // dh = new DatabaseHelper(getContext());

        if(savedInstanceState!=null){
        String savedTitle =  savedInstanceState.getString("title");
        String imgSelect =  savedInstanceState.getString("image");
        imgID =  savedInstanceState.getString("id");
        titleHeader.setText(savedTitle);
        Glide.with(getContext()).load(imgSelect).into(imgSelected);
        }

        imgback.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        return v;
    }

    private void showUIinitialize(View view) {
        imgback = (ImageView) view.findViewById(R.id.img_backNavigate);
        btnSave = (Button)view.findViewById(R.id.btnSave);
        btnClear = (Button)view.findViewById(R.id.btnClear);
        imgSelected = (ImageView) view.findViewById(R.id.detailImage);
        titleHeader = (TextView) view.findViewById(R.id.tv_ToolbarTitle);
        toolbartitle=   getActivity().getIntent().getStringExtra("title");
        imgReceieved=   getActivity().getIntent().getStringExtra("image");
        idReceieved=   getActivity().getIntent().getStringExtra("id");
        edit_Commentsection =(TextInputEditText) view.findViewById(R.id.edit_Commentsection);
        titleHeader.setText(toolbartitle);
        Glide.with(getActivity()).load(imgReceieved.toString()).into(imgSelected);
        readForImage(id_image);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.img_backNavigate:
            navigateBack();
            break;
            case R.id.btnSave:
                String dataToAdded = edit_Commentsection.getText().toString();
                String image_id = idReceieved;
                dh.update(image_id,dataToAdded);
                break;
            case R.id.btnClear:
                edit_Commentsection.setText(getResources().getString(R.string.empty));
                break;
        }
    }

    private void navigateBack() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
          savedInstanceState.putString("title",savedTitle);
          savedInstanceState.putString("image",imgSelect);
          savedInstanceState.putString("id",id_image);
          savedInstanceState.putString("comment",id_image);
    }

    public void readForImage(String imgID){
        String query = "SELECT " + COLUMN_SEARCH_COMMENT + " FROM " + TABLE_SEARCH + " WHERE " + COLUMN_SEARCH_ID + "='" + idReceieved+"' ";
        dh = new DatabaseHelper(getContext());
        SQLiteDatabase db = dh.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            String itemname =  cursor.getString(cursor.getColumnIndex(COLUMN_SEARCH_COMMENT));
            edit_Commentsection.setText(itemname);
        }
        cursor.close();
        db.close();
    }


}