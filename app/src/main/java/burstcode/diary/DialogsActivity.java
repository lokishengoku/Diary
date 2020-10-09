package burstcode.diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import burstcode.diary.model.Note;

public class DialogsActivity extends AppCompatActivity {
    private static final String TAG = "DialogsActivity";
    private Toolbar toolbar;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference dbRef;

    private ArrayList<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogs);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference().child(user.getUid());

//        writeNewNote(22, 18, 9, 10, 2020, "Test", "just a test", 0x555555);
        notes = new ArrayList<>();
        readData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnMenuLogout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void writeNewNote(int hour, int minute, int day, int month, int year, String title, String content, int color){
        String key = dbRef.push().getKey();
        Note note = new Note(hour, minute, day, month, year, title, content, color);
        Map<String, Object> noteValues = note.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/"+key, noteValues);

        dbRef.updateChildren(childUpdates);
    }

    private void readData(){
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot noteSnapshot : snapshot.getChildren()){
                    HashMap<String, Object> noteMap = (HashMap<String, Object>) noteSnapshot.getValue();
                    assert noteMap != null;
                    Note note = new Note((long)noteMap.get("hour"),
                            (long)noteMap.get("minute"),
                            (long)noteMap.get("day"),
                            (long)noteMap.get("month"),
                            (long)noteMap.get("year"),
                            (String)noteMap.get("title"),
                            (String)noteMap.get("content"),
                            (long)noteMap.get("color"));
                    notes.add(note);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "onCancelled: ", error.toException() );
            }
        });
    }

}