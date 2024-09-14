package com.example.autobike.bluetooth;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetooth.databinding.ItemDeviceBinding;

import java.util.List;

public class MyDeviceAdapter extends RecyclerView.Adapter<MyDeviceAdapter.ViewHolder> {

    private final List<MyDevice> lists;
    private OnItemClickListener onItemClickListener;
    public MyDeviceAdapter(List<MyDevice> lists) {
        this.lists = lists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDeviceBinding binding = ItemDeviceBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false);
        return new ViewHolder(binding);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);//单击
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        ItemDeviceBinding binding = DataBindingUtil.getBinding(holder.binding.getRoot());
        if (binding != null) {
            binding.setDevice(lists.get(position));
            binding.executePendingBindings();
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        //点击事件
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(v, position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return lists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ItemDeviceBinding binding;

        public ViewHolder(@NonNull ItemDeviceBinding itemTextDataRvBinding) {
            super(itemTextDataRvBinding.getRoot());
            binding = itemTextDataRvBinding;
        }
    }
}
