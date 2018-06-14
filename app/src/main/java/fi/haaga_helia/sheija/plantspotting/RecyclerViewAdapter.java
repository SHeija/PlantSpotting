package fi.haaga_helia.sheija.plantspotting;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import fi.haaga_helia.sheija.plantspotting.db.models.Entry;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<Entry> entryList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, latinName, note;
        public LinearLayout linearLayout;

        //mitä yhdessä viewholderissa on
        public MyViewHolder(View view) {
            super(view);
            linearLayout = view.findViewById(R.id.Linear);
            name = view.findViewById(R.id.entryName);
            latinName = view.findViewById(R.id.entryLatinName);
            note = view.findViewById(R.id.entryNote);
        }
    }


    public RecyclerViewAdapter(List<Entry> entryList) {
        this.entryList = entryList;
    }

    //pushing entries to rows
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entrylist_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        //laitetaan kentiin juttuja
        Entry entry = entryList.get(position);
        holder.name.setText(entry.getName());
        holder.latinName.setText(entry.getLatinName());
        holder.note.setText(entry.getNote());

        //laitetaan linearlayouttiin onclicklistener
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "This is my message");
            }
        });

    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }
}