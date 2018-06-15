package fi.haaga_helia.sheija.plantspotting;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import fi.haaga_helia.sheija.plantspotting.db.AppDatabase;
import fi.haaga_helia.sheija.plantspotting.db.models.Entry;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<Entry> entryList;
    //jesus take the wheel
    private AppDatabase db;



    //konstruktori
    public RecyclerViewAdapter(List<Entry> entryList, AppDatabase db) { //aaaa
        this.entryList = entryList;
        this.db = db; //aaaa
    }

    //viewholder
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, PopupMenu.OnMenuItemClickListener{
        public TextView name, latinName, note;
        public LinearLayout linearLayout;

        //mitä yhdessä viewholderissa on
        public MyViewHolder(View view) {
            super(view);
            linearLayout = view.findViewById(R.id.Linear);
            name = view.findViewById(R.id.entryName);
            latinName = view.findViewById(R.id.entryLatinName);
            note = view.findViewById(R.id.entryNote);

            //kun viewholderia klikataan
            view.setOnClickListener(this);
            view.setOnCreateContextMenuListener(this);
        }

        //Implements:
        @Override
        public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Log.d("Click-click", "menu at pos "+getLayoutPosition());
            db.entryDao().delete(entryList.get(getLayoutPosition()));
            entryList.remove(getLayoutPosition());
            notifyDataSetChanged();
            return false;
        }

        @Override
        public void onClick(View v) {
            Log.d("Click-click", "Item clicked at position " + getLayoutPosition());
        }

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
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }
}