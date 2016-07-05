package com.example.ruofei.bus_locator.BusTracker;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ruofei.bus_locator.R;
import com.example.ruofei.bus_locator.SetBusNotificationFragment;

import java.util.List;

/**
 * Created by ruofei on 6/11/2016.
 */
public class TrackedBusAdapter extends RecyclerView.Adapter<TrackedBusAdapter.MyViewHolder> {

    public final String TAG = this.getClass().getName();
    private List<TrackedBus> trackedBusList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView trackedBusRoute, trackedBusTime, trackedBusstopNum;

        public  MyViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            context = view.getContext();
            trackedBusRoute = (TextView) view.findViewById(R.id.trackedBusRoute);
            trackedBusTime = (TextView) view.findViewById(R.id.trackedBusTime);
            trackedBusstopNum = (TextView) view.findViewById(R.id.trackedBusstopNum);
        }

        @Override
        public void onClick(View v) {
//            TextView nameTextView = (TextView)v.findViewById(R.id.routeName);
//            String routeName = nameTextView.getText().toString();
//            Log.e(TAG,routeName);
//            Intent intent =  new Intent(context, MainActivity.class);
//            // TODO: user shared preference
//            MainActivity.mCurrentRoute = routeName;
//            intent.putExtra(Constants.ROUTE_NAME_KEY,routeName);
//            intent.putExtra(Constants.INTENT_CALL_FROM_KEY, TAG);
//            context.startActivity(intent);
            setUpNotification();
        }
    }

    public void setUpNotification() {
        DialogFragment newFragment = new SetBusNotificationFragment();
        newFragment.show(((AppCompatActivity) context).getFragmentManager(), "missiles");
    }


    public TrackedBusAdapter(List<TrackedBus> trackedBusList){
        this.trackedBusList = trackedBusList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itetmView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tracked_bus,parent,false);
        return new MyViewHolder(itetmView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TrackedBus trackedBus = trackedBusList.get(position);
        holder.trackedBusRoute.setText(trackedBus.getRouteName());
        holder.trackedBusTime.setText(trackedBus.getEstimatedTime());
        holder.trackedBusstopNum.setText(trackedBus.getBusstopNum());
    }

    @Override
    public int getItemCount() {
        return trackedBusList.size();
    }
}
