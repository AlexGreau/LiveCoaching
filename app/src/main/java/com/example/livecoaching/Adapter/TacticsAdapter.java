package com.example.livecoaching.Adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.livecoaching.Model.ApplicationState;
import com.example.livecoaching.Model.Tactic;
import com.example.livecoaching.R;

import java.util.List;

public class TacticsAdapter extends RecyclerView.Adapter<TacticsAdapter.TacticViewHolder> {
    private List<Tactic> catalogue;

    public TacticsAdapter (){
        this.catalogue = ApplicationState.getInstance().getDisplayedList();
    }

    class TacticViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public CardView tacticLayout;
        public TextView name;
        public ImageView img;
        public TacticViewHolder(final View itemView) {
            super(itemView);
            tacticLayout = (CardView) itemView.findViewById(R.id.tacticView);
            name = (TextView) itemView.findViewById(R.id.tactic_name);
            img = (ImageView) itemView.findViewById(R.id.tactic_image);
        }

        @Override
        public void onClick(View v){
            // later will have to make the transition to other activity or special view of tactic
        }

        public void bind (Tactic t, int index){
            name.setText(t.getName());
            tacticLayout.setCardBackgroundColor(Color.parseColor(t.getColor()));
            img.setImageResource(t.getImage());
        }
    }

    @NonNull
    @Override
    public TacticsAdapter.TacticViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardView item =(CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tactic, viewGroup, false);

        TacticViewHolder vh = new TacticViewHolder(item);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TacticsAdapter.TacticViewHolder holder, int i) {
        holder.bind(catalogue.get(i),i);
    }

    @Override
    public int getItemCount() {
        return catalogue.size();
    }


}
