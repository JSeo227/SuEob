package com.example.sueobmwodeudji;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sueobmwodeudji.adapter.TimeTableSubListAdapter;
import com.example.sueobmwodeudji.databinding.ActivityTimeTableSecondBinding;
import com.example.sueobmwodeudji.dto.TimeTableDTO;
import com.example.sueobmwodeudji.dto.TimeTableSubFrameModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;

public class TimeTableListActivity extends AppCompatActivity {
    private ActivityTimeTableSecondBinding binding;
    private List<TimeTableSubFrameModel> list = new LinkedList<TimeTableSubFrameModel>();

    private String mEmail;

    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimeTableSecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        db = FirebaseFirestore.getInstance();

        // 툴바
        Toolbar toolbar = binding.toolBar.mainToolBar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("시간표 목록");

        TimeTableListView();
    }

    // 툴바 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.time_table_sub_tool_bar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.add:
                Intent intent = new Intent(this, TimeTableCreateActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void TimeTableListView() {
        list.add(new TimeTableSubFrameModel("기본 시간표"));
        binding.timeTableSubList.setLayoutManager(new LinearLayoutManager(this));
        TimeTableSubListAdapter adapter = new TimeTableSubListAdapter(this, readTimeTable());
        adapter.setOlcl(new TimeTableSubListAdapter.OnListClickListener() {
            @Override
            public void onClick(TimeTableDTO data) {
                //finish();
                // 선택한 시간표를 true로, 나머지 시간표를 false로 변경
                CollectionReference ccc = db.collection("시간표");
                Log.d("ㅂㅈㄷㅂㅈㄷ", "들어옴");
                ccc.whereEqualTo("selected", true).whereEqualTo("email", data.getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot value) {
                        ccc.document(data.getEmail() + " " + data.getYear() + " - " + data.getSemester()).update("selected", true);

                        if (value.getDocuments().size() != 0) {
                            TimeTableDTO dto = value.getDocuments().get(0).toObject(TimeTableDTO.class);
                            ccc.document(dto.getEmail() + " " + dto.getYear() + " - " + dto.getSemester()).update("selected", false);
                        }
                        finish();
                    }
                });
            }
        });
        adapter.setOdcl(new TimeTableSubListAdapter.OnListDeletebtnClickListener() {
            @Override
            public void onClick(TimeTableDTO data) {
                db.collection("시간표")
                        .document(data.getEmail() + " " + data.getYear() + " - " + data.getSemester())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                finish();
                            }
                        });
            }
        });
        binding.timeTableSubList.setAdapter(adapter);
    }

    private Query readTimeTable() {
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        return mFirestore.collection("시간표")
                .whereEqualTo("email", mEmail);
    }


}