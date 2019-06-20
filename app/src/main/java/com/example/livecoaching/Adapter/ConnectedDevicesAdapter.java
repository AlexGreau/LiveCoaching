package com.example.livecoaching.Adapter;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.livecoaching.ConnectActivity;
import com.example.livecoaching.Model.ApplicationState;
import com.example.livecoaching.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ConnectedDevicesAdapter extends RecyclerView.Adapter<ConnectedDevicesAdapter.ConnectedDevicesHolder> {
    final private DeviceClickListener deviceClickListener;
    private List<BluetoothDevice> connectedDevices;

    public interface DeviceClickListener{
        void onChooseClickListener(int clickedIndex);
    }

    public ConnectedDevicesAdapter (DeviceClickListener listener){
        this.deviceClickListener = listener;
        this.connectedDevices =  ApplicationState.getInstance().getConnectedDevices();
    }

    class ConnectedDevicesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public CardView deviceLayout;
        public TextView name;
        public TextView details;

        public ConnectedDevicesHolder(@NonNull View itemView) {
            super(itemView);
            deviceLayout = (CardView) itemView.findViewById(R.id.device);
            name = (TextView) itemView.findViewById(R.id.device_name);
            details = (TextView) itemView.findViewById(R.id.device_details);
        }

        @Override
        public void onClick(View v) {
            System.out.println("from listener inside holder for devices");
            deviceClickListener.onChooseClickListener(getAdapterPosition());
        }

        public void bind (BluetoothDevice btDevice, int index){
            name.setText(btDevice.getName());
            details.setText(btDevice.getAddress());
        }
    }

    @NonNull
    @Override
    public ConnectedDevicesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardView item =(CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.device_connected, viewGroup, false);
        ConnectedDevicesHolder vh = new ConnectedDevicesHolder(item);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectedDevicesHolder connectedDevicesHolder, int i) {
        connectedDevicesHolder.bind(connectedDevices.get(connectedDevicesHolder.getAdapterPosition()),connectedDevicesHolder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return this.connectedDevices.size();
    }


}
