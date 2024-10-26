package com.example.autobike;

import static com.example.autobike.Util.Altitude;
import static com.example.autobike.Util.AverageSpeed;
import static com.example.autobike.Util.DistanceSum;
import static com.example.autobike.Util.ElapsedTime;
import static com.example.autobike.Util.ImageURI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.map.R;
import com.example.autobike.entity.SportRecord;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final List<SportRecord> itemList;
    private final Context context;



    public ItemAdapter(List<SportRecord> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.textView.setText(dataList.get(position));
        SportRecord mapItem = itemList.get(position);
        holder.VDistanceSum.setText(String.format("%4.2f", mapItem.getDistanceSum()));

//        holder.VDistanceSum.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(mapItem.getTimeDate()));

        holder.VMapButton.setOnClickListener(v -> {
            // 启动新的 Activity，并传递当前的 item 信息
            Intent intent = new Intent(context, concreteActivity.class);
            intent.putExtra(Altitude, mapItem.getAltitude());
            intent.putExtra(DistanceSum, mapItem.getDistanceSum());
            intent.putExtra(AverageSpeed, mapItem.getAverageSpeed());
            intent.putExtra(ElapsedTime, mapItem.getElapsedTime());
            intent.putExtra(ImageURI, mapItem.getImageUrl());
//            intent.putExtra(TimeDate, mapItem.getTimeDate());


            context.startActivity(intent);  //
        });


        //TODO impl picture performance for item
        //holder.imageView.setImageURI(Uri.parse(mapItem.getImageUrl()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView VDistanceSum, VElapsedTime;
        ImageView imageView;
        Button VMapButton;
        TextView VTimeDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            VDistanceSum = itemView.findViewById(R.id.distance_Sum);
            imageView = itemView.findViewById(R.id.mapView);
            VMapButton = itemView.findViewById(R.id.itemBottom);
            VTimeDate = itemView.findViewById(R.id.timeDate);
        }


    }

}