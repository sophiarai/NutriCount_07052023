package com.example.nutricount_07052023;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends ArrayAdapter {

    private Context context;

    public NotesAdapter(Context context, List<Note> notes) {
        super(context, 0, notes);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_custom_notes_list, parent, false);
        }

        Note currentNote = (Note) getItem(position);

        // Zeigen Sie die Eintragswerte in den entsprechenden Views an
        TextView burnedCaloriesTextView = convertView.findViewById(R.id.textView_notes_burnedCalories);
        TextView consumedCaloriesTextView = convertView.findViewById(R.id.textView_notes_consumedCalories);
        TextView dateTextView = convertView.findViewById(R.id.textView_notes_date);

        if (currentNote != null) {
            burnedCaloriesTextView.setText(String.valueOf(currentNote.getBurnedCalories()));
            consumedCaloriesTextView.setText(String.valueOf(currentNote.getConsumedCalories()));
            dateTextView.setText(currentNote.getDate().toString());
        }

        return convertView;
    }
}
