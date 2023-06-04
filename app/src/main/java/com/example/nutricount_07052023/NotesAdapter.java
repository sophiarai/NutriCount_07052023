package com.example.nutricount_07052023;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nutricount_07052023.Database.NoteEntity;

import java.util.List;

public class NotesAdapter extends ArrayAdapter {

    private Context context;

    public NotesAdapter(Context context, List<NoteEntity> notes) {
        super(context, 0, notes);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_custom_notes_list, parent, false);
        }

        NoteEntity currentNote = (NoteEntity) getItem(position);

        // Zeigen Sie die Eintragswerte in den entsprechenden Views an
        TextView allCaloriesTextView = convertView.findViewById(R.id.textView_notes_allCalories);
        TextView burnedCaloriesTextView = convertView.findViewById(R.id.textView_notes_burnedCalories);
        TextView consumedCaloriesTextView = convertView.findViewById(R.id.textView_notes_consumedCalories);
        TextView dateTextView = convertView.findViewById(R.id.textView_notes_date);

        if (currentNote != null) {
            allCaloriesTextView.setText(String.valueOf(currentNote.getAllCalories()));
            burnedCaloriesTextView.setText(String.valueOf(currentNote.getBurnedCalories()));
            consumedCaloriesTextView.setText(String.valueOf(currentNote.getConsumedCalories()));
            dateTextView.setText(currentNote.getDay().toString());
        }

        return convertView;
    }
}
