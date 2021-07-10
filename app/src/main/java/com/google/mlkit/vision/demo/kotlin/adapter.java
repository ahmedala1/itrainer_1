package com.google.mlkit.vision.demo.kotlin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.mlkit.vision.demo.R;

import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<adapter.MyViewHolder> {

    Context context;

    ArrayList<model> m;

    public adapter(Context c , ArrayList<model> p)
    {
        context = c;
        m = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.date.setText(m.get(position).getDay());
        holder.year.setText(m.get(position).getYear());
        holder.hour.setText(m.get(position).getHour());
        holder.month.setText(m.get(position).getMonth());
        holder.counter.setText(  String.valueOf(m.get(position).getCount()));
        holder.type.setText(  String.valueOf(m.get(position).getType()));

    }

    @Override
    public int getItemCount() {
        return m.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView date,hour,month,year,counter,type;


        public MyViewHolder(View itemView) {

            super(itemView);

            date= (TextView) itemView.findViewById(R.id.day);
            year= (TextView) itemView.findViewById(R.id.year);
            month= (TextView) itemView.findViewById(R.id.monthly);
            hour= (TextView) itemView.findViewById(R.id.hourinfo);
            counter= (TextView) itemView.findViewById(R.id.countinfo);
            type= (TextView) itemView.findViewById(R.id.typeinfo);

        }


    }

}
