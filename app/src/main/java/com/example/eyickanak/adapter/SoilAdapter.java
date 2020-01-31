package com.example.eyickanak.adapter;

/*Project Name: Kanak-Smart Composting
        Author List : Varad Gorhe ,Team Leader
        Filename    : SoilAdapter.java
        Global Variables:-
        Functions   : -

        Logic is explained in the FragmentPoints.java file.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eyickanak.R;
import com.example.eyickanak.model.Leaderboard;

import java.util.List;

public class SoilAdapter extends RecyclerView.Adapter<SoilAdapter.MyViewHolder> {
    List<String> soils;
    List<String> temp;
    Context context;



    public SoilAdapter(List<String> soils, List<String> temp, Context context) {
        this.soils = soils;
        this.temp = temp;
        this.context = context;

    }

    @NonNull
    @Override
    public SoilAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.soil_list_item, parent, false);
        return new SoilAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.soilReading.setText(soils.get(position));
        if(temp.size() > position)
            holder.tempReading.setText(temp.get(position));
        else
            holder.tempReading.setText("");
    }


    @Override
    public int getItemCount() {
        return soils.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView soilReading;
        private TextView tempReading;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            soilReading = itemView.findViewById(R.id.soilReading);
            tempReading = itemView.findViewById(R.id.tempReading);
        }
    }
}
