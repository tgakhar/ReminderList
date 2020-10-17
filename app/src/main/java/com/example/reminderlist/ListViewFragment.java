package com.example.reminderlist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.v4.os.IResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListViewFragment extends Fragment {
    public static final String TAG = "YOUR-TAG-NAME";
    FirebaseUser curUser;
    FirebaseAuth auth;
    FirebaseFirestore db;
    List<Reminder> reminderList = new ArrayList<>();
    RecyclerView recyclerView_listReminder;
    ReminderAdapter reminderAdapter;
    FloatingActionButton floating_filter;


    public ListViewFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_view, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        floating_filter=view.findViewById(R.id.floating_filterbutton);
        floating_filter.setOnClickListener(filter);
        recyclerView_listReminder = view.findViewById(R.id.recycler_listReminder);
        loadData();
    }

    View.OnClickListener filter=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PopupMenu popupMenu = new PopupMenu(getActivity().getApplicationContext(), v);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {

                        case R.id.popup_active:

                            active();
                            return true;

                        case R.id.popup_inactive:
                            return true;

                        case R.id.popup_all:
                            loadData();
                            return true;

                        default:
                            return false;


                    }
                }
            });
            popupMenu.show();
        }
        };

    private void active() {
        reminderList.clear();
        db.collection("Users").document(curUser.getUid()).collection("Reminder").whereEqualTo("Status",1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String reminderId = (String) document.getId();
                        String reminderTitle = (String) document.getData().get("Title");
                        String reminderLocation = (String) document.getData().get("Address");
                        String reminderDescription = (String) document.getData().get("Description");
                        String reminderDate = (String) document.getData().get("Date");
                        Integer reminderRepeat = ((Long) document.getData().get("Repeat")).intValue();
                        Integer reminderRange = ((Long) document.getData().get("Range")).intValue();
                        Integer reminderStatus = ((Long) document.getData().get("Status")).intValue();
                        Double reminderLat = (Double) document.getData().get("Latitude");
                        Double reminderLong = (Double) document.getData().get("Longitude");

                        addToList(reminderId, reminderTitle, reminderLocation, reminderDescription, reminderDate, reminderRepeat, reminderRange, reminderStatus, reminderLat, reminderLong);

                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }

    private void loadData() {


        reminderList.clear();
        curUser = auth.getCurrentUser();
        CollectionReference collectionReference = db.collection("Users").document(curUser.getUid()).collection("Reminder");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String reminderId = (String) document.getId();
                        String reminderTitle = (String) document.getData().get("Title");
                        String reminderLocation = (String) document.getData().get("Address");
                        String reminderDescription = (String) document.getData().get("Description");
                        String reminderDate = (String) document.getData().get("Date");
                        Integer reminderRepeat = ((Long) document.getData().get("Repeat")).intValue();
                        Integer reminderRange = ((Long) document.getData().get("Range")).intValue();
                        Integer reminderStatus = ((Long) document.getData().get("Status")).intValue();
                        Double reminderLat = (Double) document.getData().get("Latitude");
                        Double reminderLong = (Double) document.getData().get("Longitude");

                        addToList(reminderId, reminderTitle, reminderLocation, reminderDescription, reminderDate, reminderRepeat, reminderRange, reminderStatus, reminderLat, reminderLong);

                    }


                }

            }
        });


    }

    private void addToList(String reminderId, String reminderTitle, String reminderLocation, String reminderDescription, String reminderDate, Integer reminderRepeat, Integer reminderRange, Integer reminderStatus, Double reminderLat, Double reminderLong) {

        reminderList.add(new Reminder(reminderId, reminderTitle, reminderLocation, reminderDescription, reminderDate, reminderRepeat, reminderRange, reminderStatus, reminderLat, reminderLong));

        setReminderRecycler(reminderList);
   
    }

    private void setReminderRecycler(List<Reminder> reminderList) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext()
                , RecyclerView.VERTICAL, false);
        recyclerView_listReminder.setLayoutManager(layoutManager);
        reminderAdapter = new ReminderAdapter(reminderList, getActivity());
        recyclerView_listReminder.setAdapter(reminderAdapter);
    }
}

