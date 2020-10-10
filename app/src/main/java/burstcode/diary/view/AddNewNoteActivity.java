package burstcode.diary.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import burstcode.diary.R;
import burstcode.diary.model.Note;

public class AddNewNoteActivity extends AppCompatActivity {
    public static final int RESULT_DELETE = 666;
    private Toolbar toolbar;
    //Bottom menu
    private ImageView btnDatePicker, btnTimePicker, btnColorPicker, btnDelete;

    //Title and content
    private EditText edtTitle, edtContent;

    //DatePicker
    private LinearLayout datePickerContainer;
    private DatePicker datePicker;
    private Button btnSubmitDatePicker;

    //TimePicker
    private LinearLayout timePickerContainer;
    private TimePicker timePicker;

    //ColorPicker
    private boolean isShowingCP = false;
    private ColorPicker colorPicker;


    private boolean isNewNote = true;
    private Note note = null;
    private boolean pickedDate = false, pickedTime = false, pickedColor = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);
        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarAddNote);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);

        //UI
        edtTitle = findViewById(R.id.edtTitle);
        edtContent = findViewById(R.id.edtContent);
        btnDatePicker = findViewById(R.id.btnNavDatePicker);
        btnTimePicker = findViewById(R.id.btnNavTimePicker);
        btnColorPicker = findViewById(R.id.btnNavColorPicker);
        btnDelete = findViewById(R.id.btnNavDelete);

        datePickerContainer = findViewById(R.id.datePickerContainer);
        datePicker = findViewById(R.id.datePicker);
        btnSubmitDatePicker = findViewById(R.id.btnSubmitDatePicker);

        timePickerContainer = findViewById(R.id.timePickerContainer);
        timePicker = findViewById(R.id.timePicker);

        colorPicker = new ColorPicker(this, 255, 155, 222);


        //passed data
        Intent intent = getIntent();
        isNewNote = intent.getBooleanExtra("isNewNote", true);
        if (!isNewNote) {
            note = (Note) intent.getSerializableExtra("currentNote");
            edtTitle.setText(note.getTitle());
            edtContent.setText(note.getContent());
            pickedDate = true;
            pickedTime = true;
            pickedColor = true;
        } else note = new Note(0,0,0,0,0,"","",0);

        setOnClickBottomMenu();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setOnClickBottomMenu() {
        //Date Picker
        btnDatePicker.setOnClickListener(view -> {
            if(datePickerContainer.getVisibility() == View.VISIBLE){
                btnDatePicker.setColorFilter(R.color.colorLightGrey);
                datePickerContainer.setVisibility(View.GONE);
            } else {
                btnDatePicker.setColorFilter(R.color.colorPrimary);
                datePickerContainer.setVisibility(View.VISIBLE);
                btnTimePicker.setColorFilter(R.color.colorLightGrey);
                timePickerContainer.setVisibility(View.GONE);
                btnColorPicker.setColorFilter(R.color.colorLightGrey);
                colorPicker.dismiss();
            }
        });
        btnSubmitDatePicker.setOnClickListener(view -> {
            pickedDate = true;
            note.setDay(datePicker.getDayOfMonth());
            note.setMonth(datePicker.getMonth() + 1);
            note.setYear(datePicker.getYear());
            btnDatePicker.setColorFilter(R.color.colorLightGrey);
            datePickerContainer.setVisibility(View.GONE);
        });

        //Time Picker
        timePicker.setIs24HourView(true);
        btnTimePicker.setOnClickListener(view -> {
            if(timePickerContainer.getVisibility() == View.VISIBLE){
                btnTimePicker.setColorFilter(R.color.colorLightGrey);
                timePickerContainer.setVisibility(View.GONE);
            } else {
                btnTimePicker.setColorFilter(R.color.colorPrimary);
                timePickerContainer.setVisibility(View.VISIBLE);
                btnDatePicker.setColorFilter(R.color.colorLightGrey);
                datePickerContainer.setVisibility(View.GONE);
                btnColorPicker.setColorFilter(R.color.colorLightGrey);
                colorPicker.dismiss();
            }
        });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                pickedTime = true;
                note.setHour(i);
                note.setMinute(i1);
            }
        });

        //Color Picker
        btnColorPicker.setOnClickListener(view -> {
            if(isShowingCP){
                btnColorPicker.setColorFilter(R.color.colorLightGrey);
                colorPicker.dismiss();
                isShowingCP = false;
            } else {
                isShowingCP = true;
                btnColorPicker.setColorFilter(R.color.colorPrimary);
                colorPicker.show();
                btnDatePicker.setColorFilter(R.color.colorLightGrey);
                datePickerContainer.setVisibility(View.GONE);
                btnTimePicker.setColorFilter(R.color.colorLightGrey);
                timePickerContainer.setVisibility(View.GONE);
            }
        });
        colorPicker.setCallback(color -> {
            pickedColor = true;
            isShowingCP = false;
            note.setColor(color);
            btnColorPicker.setColorFilter(R.color.colorLightGrey);
            colorPicker.dismiss();
        });

        //Delete
        btnDelete.setOnClickListener(view -> {
            btnDelete.setColorFilter(R.color.colorPrimary);
            Intent returnIntent = new Intent();
            setResult(RESULT_DELETE, returnIntent);
            finish();
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btnSubmitAddNote){
            if (edtContent.getText().toString().trim().isEmpty() || edtTitle.getText().toString().isEmpty()){
                Toast.makeText(this, "Please enter all information and try again.", Toast.LENGTH_SHORT).show();
            } else if(!pickedDate) Toast.makeText(this, "Please choose your date", Toast.LENGTH_SHORT).show();
            else if (!pickedTime) Toast.makeText(this, "Please choose your time", Toast.LENGTH_SHORT).show();
            else if (!pickedColor) Toast.makeText(this, "Please choose your color", Toast.LENGTH_SHORT).show();
            else {
                if (isNewNote){
                    Intent returnIntent = new Intent();
                    note.setContent(edtContent.getText().toString());
                    note.setTitle(edtTitle.getText().toString());
                    returnIntent.putExtra("newNote", note);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
        return super.onSupportNavigateUp();
    }
}