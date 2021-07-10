
package  com.google.mlkit.vision.demo.kotlin;


import android.content.Intent;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.mlkit.vision.demo.R;


import java.util.ArrayList;

public class retrieve extends AppCompatActivity
{
    private DatabaseReference myRef;
    private FirebaseDatabase database;

    private FirebaseAuth mFirebaseAuth;
Button c;
    private ArrayList<model> m;
String useriid;
    private adapter arrayAdapter;
RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve);
c=findViewById(R.id.cont);
        database = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        useriid = mFirebaseAuth.getUid();
        m = new ArrayList<model>();
        myRef = database.getReference("tasks").child(useriid);
        recyclerView = (RecyclerView)findViewById(R.id.re);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));

c.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent= new Intent(retrieve.this,ChooserActivity.class);
        startActivity(intent);
    }
});
        myRef.addValueEventListener(new ValueEventListener() {
            ArrayList<model> m2 = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                m2.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                        model c = child.getValue(model.class);
                        m2.add(c);



                }
                arrayAdapter = new adapter(retrieve.this,m2);
                recyclerView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(retrieve.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();

            }
        });






    }

}
