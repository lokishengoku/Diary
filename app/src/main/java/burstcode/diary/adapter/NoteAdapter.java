package burstcode.diary.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;

import burstcode.diary.R;
import burstcode.diary.model.Note;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private ArrayList<Note> notes = new ArrayList<>();
    private Context mContext;

    public NoteAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setNotes(ArrayList<Note> notes){
        this.notes = notes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = notes.get(position);

        holder.txtTimeRemaining.setText(timeDurationCounter(note));
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.cardViewNoteContainer.setCardBackgroundColor(color);
        holder.txtContent.setText(note.getContent());
        holder.txtTitle.setText(note.getTitle());
        holder.txtAmPm.setText(note.getHour()>12?"PM":"AM");
        holder.txtCreatedTime.setText(note.getHour()+":"+note.getMinute());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String timeDurationCounter(Note note){
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
        String noteDateStr = (note.getDay()>9?"":"0")+note.getDay()+" "+(note.getMonth()>9?"":"0")+note.getMonth()+" "+note.getYear();
        LocalDate date = LocalDate.now();
        String todayStr = (date.getDayOfMonth()>9?"":"0")+date.getDayOfMonth()+" "+(date.getMonthValue()>9?"":"0")+date.getMonthValue()+" "+date.getYear();

        LocalDate noteDate = LocalDate.parse(noteDateStr, formatter);
        LocalDate today = LocalDate.parse(todayStr, formatter);

        if(noteDate.equals(today))
            return "Today";
        return ChronoUnit.DAYS.between(noteDate, today)+" days ago";
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTimeRemaining, txtCreatedTime, txtAmPm, txtTitle, txtContent;
        private CardView cardViewNoteContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTimeRemaining = itemView.findViewById(R.id.txtTimeRemaining);
            txtCreatedTime = itemView.findViewById(R.id.txtCreatedTime);
            txtAmPm = itemView.findViewById(R.id.txtAmPm);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtContent = itemView.findViewById(R.id.txtContent);
            cardViewNoteContainer = itemView.findViewById(R.id.cardViewNoteContainer);
        }
    }
}
