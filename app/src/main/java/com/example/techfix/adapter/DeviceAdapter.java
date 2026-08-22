package com.example.techfix.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techfix.R;
import com.example.techfix.model.Device;

import java.util.List;

public class DeviceAdapter
        extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {

    public interface OnDeviceActionListener {
        void onBookRepair(Device device);
    }

    private final List<Device> deviceList;
    private final OnDeviceActionListener listener;

    public DeviceAdapter(
            List<Device> deviceList,
            OnDeviceActionListener listener
    ) {
        this.deviceList = deviceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_device, parent, false);

        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull DeviceViewHolder holder,
            int position
    ) {

        Device device = deviceList.get(position);

        holder.tvDeviceName.setText(
                device.getDeviceName()
        );

        holder.tvDeviceDetails.setText(
                device.getBrand()
                        + " • "
                        + device.getModel()
        );

        holder.btnBookRepair.setOnClickListener(v -> {

            if (listener != null) {
                listener.onBookRepair(device);
            }

        });
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public static class DeviceViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvDeviceName;
        TextView tvDeviceDetails;
        Button btnBookRepair;

        public DeviceViewHolder(
                @NonNull View itemView
        ) {
            super(itemView);

            tvDeviceName =
                    itemView.findViewById(
                            R.id.tvDeviceName
                    );

            tvDeviceDetails =
                    itemView.findViewById(
                            R.id.tvDeviceDetails
                    );

            btnBookRepair =
                    itemView.findViewById(
                            R.id.btnBookRepair
                    );
        }
    }
}