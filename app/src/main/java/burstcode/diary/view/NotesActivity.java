package burstcode.diary.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import burstcode.diary.MainActivity;
import burstcode.diary.R;
import burstcode.diary.adapter.NoteAdapter;
import burstcode.diary.model.Note;

import static burstcode.diary.view.AddNewNoteActivity.RESULT_DELETE;

public class NotesActivity extends AppCompatActivity {
    private static final String TAG = "DialogsActivity";
    public static final int RC_ADD_NOTE = 234;
    private Toolbar toolbar;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference dbRef;

    private ArrayList<Note> notes;
    private RecyclerView recViewNotes;
    private NoteAdapter noteAdapter;

    private FloatingActionButton btnCreateNewNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference().child(user.getUid());

//        writeNewNote(22, 18, 9, 10, 2020, "Test", "just a test", 0x555555);
        recViewNotes = findViewById(R.id.recViewNotes);
        recViewNotes.setLayoutManager(new LinearLayoutManager(this));
        notes = new ArrayList<>();
        noteAdapter = new NoteAdapter(this);
        noteAdapter.setNotes(notes);
        recViewNotes.setAdapter(noteAdapter);
        readData();

        btnCreateNewNote = findViewById(R.id.btnAddNewNote);
        btnCreateNewNote.setOnClickListener(view -> {
            Intent intent = new Intent(NotesActivity.this, AddNewNoteActivity.class);
            intent.putExtra("isNewNote", true);
            startActivityForResult(intent, RC_ADD_NOTE);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btnMenuLogout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void writeNewNote(Note note){
        String key = dbRef.push().getKey();
        Map<String, Object> noteValues = note.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/"+key, noteValues);

        dbRef.updateChildren(childUpdates);
        Toast.makeText(this, "New note added", Toast.LENGTH_SHORT).show();
    }

    private void readData(){
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notes.clear();
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
                noteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "onCancelled: ", error.toException() );
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_ADD_NOTE){
            if (resultCode == Activity.RESULT_OK){
                Note note = (Note) data.getSerializableExtra("newNote");
                writeNewNote(note);
            } else if(resultCode == RESULT_DELETE){

            }
        }
    }
}