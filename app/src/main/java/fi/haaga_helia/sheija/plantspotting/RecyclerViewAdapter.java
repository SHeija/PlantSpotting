package fi.haaga_helia.sheija.plantspotting;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import fi.haaga_helia.sheija.plantspotting.db.AppDatabase;
import fi.haaga_helia.sheija.plantspotting.db.models.Entry;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<Entry> entryList;
    //jesus take the wheel
    private AppDatabase db;
    Context context;




    //konstruktori
    public RecyclerViewAdapter(List<Entry> entryList, Context context) { //aaaa
        this.entryList = entryList;
        this.context = context;
        this.db = getDb();
        //this.db = db; //aaaa
    }

    //viewholder
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, PopupMenu.OnMenuItemClickListener{
        public TextView name, latinName, date, location, note;
        public ImageView imagePath;
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
            imagePath = view.findViewById(R.id.entryImagePath);

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

        holder.imagePath.setImageDrawable (null);

        if (entry.getImagePath() != null) {
            //Picasso.with(context).setLoggingEnabled(true);
            File imageFile = new File(entry.getImagePath());
            Glide.with(context).load(imageFile).into(holder.imagePath);
            //holder.imagePath.setImageURI(Uri.parse(entry.getImagePath()));
            //Picasso.with(context).load(imageFile).into(holder.imagePath);
        }

        ////DEBUG SHIT
        if(position %2 == 1)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#a9a9a9"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
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

