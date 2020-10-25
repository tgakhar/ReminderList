package com.example.reminderlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditReminderActivity extends AppCompatActivity {


    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser user;
    Reminder reminder;
    TextView txt_title,txt_address,txt_description,txt_range,txt_repeat,txt_status,txt_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);
        db=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        reminder=getIntent().getExtras().getParcelable("Reminder");
        txt_title=findViewById(R.id.txt_titleAdd);
        txt_address=findViewById(R.id.txt_loationAdd);
        txt_description=findViewById(R.id.txt_addDescription);
        txt_range=findViewById(R.id.txt_addRange);
        txt_repeat=findViewById(R.id.txt_addRepeat);
        txt_status=findViewById(R.id.switch_addRepeat);
        txt_date=findViewById(R.id.txt_addDate);

        setTextData();

    }

    private void setTextData() {

      txt_title.setText(reminder.getReminderTitle());
      txt_address.setText(reminder.getReminderLocation());
      txt_description.setText(reminder.getReminderDescription());
      txt_range.setText(reminder.getReminderRange());
      txt_repeat.setText(reminder.getReminderRepeat());
      txt_status.setText(reminder.getReminderStatus());
      txt_date.setText(reminder.getReminderDate());




    }
}