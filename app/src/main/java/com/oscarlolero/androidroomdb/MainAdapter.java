package com.oscarlolero.androidroomdb;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    //Initialize variable
    private List<MainData> dataList;
    private Activity context;
    private RoomDB database;

    //Create constructor
    public MainAdapter(Activity context, List<MainData> dataList) {
        this.context = context;
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        //Initialize view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull MainAdapter.ViewHolder holder, int position) {
        //Initialize main data
        MainData data = dataList.get(position);

        //Initialize database
        database = RoomDB.getInstance(context);

        //Set text on text view
        holder.textView.setText(data.getText());

        holder.btEdit.setOnClickListener(v -> {
            //Initialize main data
            MainData d = dataList.get(holder.getAdapterPosition());

            //Get id
            int sID = d.getID();

            //Get text
            String sText = d.getText();

            //Create, config and show dialog
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_update);
            int width = WindowManager.LayoutParams.MATCH_PARENT;
            int height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
            dialog.show();

            //Initialize and assign variable
            EditText editText = dialog.findViewById(R.id.edit_text);
            Button btUpdate = dialog.findViewById(R.id.bt_update);

            //Set text on edit text
            editText.setText(sText);

            btUpdate.setOnClickListener(v1 -> {
                dialog.dismiss();

                //Get updated text from edit text
                String uText = editText.getText().toString().trim();
                //Update text in database
                database.mainDao().update(sID, uText);
                //Notify when data is updated
                dataList.clear();
                dataList.addAll(database.mainDao().getAll());
                notifyDataSetChanged();
            });

            holder.btDelete.setOnClickListener(v1 -> {
                //Initialize main data
                MainData mainData = dataList.get(holder.getAdapterPosition());
                //Delete text from database
                database.mainDao().delete(mainData);
                //Notify when data is deleted
                int listPosition = holder.getAdapterPosition();
                dataList.remove(listPosition);
                notifyItemRemoved(listPosition);
                notifyItemRangeChanged(listPosition, dataList.size());
            });
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //Initialize variable
        TextView textView;
        ImageView btEdit, btDelete;

        public ViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text_view);
            btEdit = itemView.findViewById(R.id.bt_edit);
            btDelete = itemView.findViewById(R.id.bt_delete);
        }
    }
}
