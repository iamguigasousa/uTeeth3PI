package br.com.puccampinas.uteeth3pi.recycleview;

import android.media.metrics.Event;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

import br.com.puccampinas.uteeth3pi.R;

public class RecyclerViewActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<User> userArrayList;
    MyAdapter myAdapter;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        userArrayList=new ArrayList<User>();
        myAdapter = new MyAdapter(RecyclerViewActivity.this,userArrayList);



    }
}
