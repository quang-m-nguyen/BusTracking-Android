package com.example.ruofei.bus_locator.routes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ruofei.bus_locator.MainActivity;
import com.example.ruofei.bus_locator.MainTabFragment;
import com.example.ruofei.bus_locator.R;
import com.example.ruofei.bus_locator.util.Constants;

import java.util.List;

/**
 * Created by ruofei on 6/11/2016.
 */
public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.MyViewHolder> {

    public final String TAG = this.getClass().getName();
    private List<Route> routesList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView routeNum, routeName;

        public  MyViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            context = view.getContext();
            routeNum = (TextView) view.findViewById(R.id.routeNum);
            routeName = (TextView) view.findViewById(R.id.routeName);
        }

        @Override
        public void onClick(View v) {
            TextView nameTextView = (TextView)v.findViewById(R.id.routeName);
            String routeName = nameTextView.getText().toString();
            Log.d(TAG,routeName);
            Intent intent =  new Intent(context, MainActivity.class);
            // TODO: user shared preference
            MainTabFragment.mCurrentRoute = routeName;
            intent.putExtra(Constants.ROUTE_NAME_KEY,routeName);
            intent.putExtra(Constants.INTENT_CALL_FROM_KEY, TAG);
            context.startActivity(intent);
        }
    }


    public RoutesAdapter(List<Route> routesList){
        this.routesList = routesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itetmView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_route,parent,false);
        return new MyViewHolder(itetmView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Route route = routesList.get(position);
        holder.routeNum.setText(route.getRouteNum());
        holder.routeName.setText(route.getRouteName());

        if(position % 3 ==0)
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.materialColorGreen));
        else if(position % 3==1)
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.materialColorCyan));
        else if(position %3 ==2)
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.materialColorRed));
    }

    @Override
    public int getItemCount() {
        return routesList.size();
    }
}
