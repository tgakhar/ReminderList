package com.example.reminderlist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;

public class ReminderAdapter  extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> implements Filterable {

    List<Reminder> reminderList = new ArrayList<>();
    List<Reminder> reminderListFiltered=new ArrayList<>();
    Context context;
    double currLat;
    double currLong;
    LatLng current;
    private View.OnClickListener OnClick;

    SwitchMaterial.OnCheckedChangeListener statusChangeListener;
    public ReminderAdapter(List<Reminder> reminderList, Context context) {
        this.reminderList = reminderList;
        this.context = context;
        this.reminderListFiltered= reminderList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_reminder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txt_title.setText(reminderListFiltered.get(position).getReminderTitle());
        holder.txt_address.setText(reminderListFiltered.get(position).getReminderLocation());
        if (reminderListFiltered.get(position).getReminderStatus() == 1) {
            holder.switchStatus.setChecked(true);
        } else {
            holder.switchStatus.setChecked(false);
        }
        Log.d("Adapter","Rminder="+reminderListFiltered.get(position).getReminderTitle());


    }
    @Override
    public int getItemCount() {
        return reminderListFiltered.size();
    }
    public void setOnClickListner(View.OnClickListener onClickListner)
    {
        OnClick=onClickListner;

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    reminderListFiltered = reminderList;
                } else {
                    List<Reminder> filteredList = new ArrayList<>();
                    for (Reminder row : reminderList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getReminderTitle().toLowerCase().contains(charString.toLowerCase()) || row.getReminderLocation().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    reminderListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = reminderListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                reminderListFiltered = (ArrayList<Reminder>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }




    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_title,txt_address,txt_distance;
        ImageView img_delete,img_edit;
        SwitchMaterial switchStatus;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_address=itemView.findViewById(R.id.txt_layoutLocation);
            txt_distance=itemView.findViewById(R.id.txt_layoutDistance);
            txt_title=itemView.findViewById(R.id.txt_layoutTitle);
            switchStatus=itemView.findViewById(R.id.switch_status);
            img_delete=itemView.findViewById(R.id.img_delete);
            img_edit=itemView.findViewById(R.id.img_edit);

            img_edit.setTag(this);
            img_edit.setOnClickListener(OnClick);

        }
    }
}
