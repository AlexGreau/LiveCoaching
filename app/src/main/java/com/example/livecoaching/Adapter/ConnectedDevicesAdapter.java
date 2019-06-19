package com.example.livecoaching.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.livecoaching.ConnectActivity;
import com.example.livecoaching.R;

import org.w3c.dom.Text;

public class ConnectedDevicesAdapter extends RecyclerView.Adapter<ConnectedDevicesAdapter.ConnectedDevicesHolder> {
    final private DeviceClickListener deviceClickListener;

    @NonNull
    @Override
    public ConnectedDevicesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectedDevicesHolder connectedDevicesHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public interface DeviceClickListener{
        void onChooseClickListener(int clickedIndex);
    }

    public ConnectedDevicesAdapter (DeviceClickListener listener){
        this.deviceClickListener = listener;
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

        public void bind (){

        }
    }


}
