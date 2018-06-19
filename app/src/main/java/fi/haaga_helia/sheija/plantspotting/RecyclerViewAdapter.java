package fi.haaga_helia.sheija.plantspotting;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.List;

import fi.haaga_helia.sheija.plantspotting.db.AppDatabase;
import fi.haaga_helia.sheija.plantspotting.db.models.Entry;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<Entry> entryList;
    private AppDatabase db;
    private Context context;

    //konstruktori
    public RecyclerViewAdapter(List<Entry> entryList, Context context) {
        this.entryList = entryList;
        this.context = context;
        this.db = getDb();
    }

    //viewholder
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, PopupMenu.OnMenuItemClickListener{
        public TextView name, latinName, date, location, note;
        public ImageView image;
        public LinearLayout linearLayout;

        //mitä yhdessä viewholderissa on
        public MyViewHolder(View view) {
            super(view);
            linearLayout = view.findViewById(R.id.Linear);
            name = view.findViewById(R.id.entryName);
            latinName = view.findViewById(R.id.entryLatinName);
            note = view.findViewById(R.id.entryNote);
            date = view.findViewById(R.id.entryDate);
            location = view.findViewById(R.id.entryLocation);
            image = view.findViewById(R.id.entryImage);

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
            deleteAt(getLayoutPosition());
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
        holder.date.setText(entry.getDate());
        holder.location.setText(entry.getLocation());
        holder.note.setText(entry.getNote());

        //reseting the picture to avoid weird bugs
        holder.image.setImageResource(R.drawable.placeholder);

        if (entry.getImagePath() != null) {
            File imageFile = new File(entry.getImagePath());
            Glide.with(context).load(imageFile).into(holder.image);
        }

    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    private void deleteAt(int layoutPosition){
        if (entryList.get(layoutPosition).getImagePath() == null || entryList.get(layoutPosition).getImagePath().equals("")){
            db.entryDao().delete(entryList.get(layoutPosition));
            entryList.remove(layoutPosition);
            notifyDataSetChanged();
        }else{
            File associatedImage = new File(entryList.get(layoutPosition).getImagePath());
            if (associatedImage.delete()){
                db.entryDao().delete(entryList.get(layoutPosition));
                entryList.remove(layoutPosition);
                notifyDataSetChanged();
            }

        }

    }

    public AppDatabase getDb(){
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "entry-database").allowMainThreadQueries()
                .build();
        return db;
    }
}

