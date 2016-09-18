package com.thrifa.ruofei.bus_locator.routes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;
import com.thrifa.ruofei.bus_locator.MainActivity;
import com.thrifa.ruofei.bus_locator.MainTabFragment;
import com.thrifa.ruofei.bus_locator.R;
import com.thrifa.ruofei.bus_locator.util.Constants;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

/**
 * Created by ruofei on 6/11/2016.
 */
public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.MyViewHolder> {

    public final String TAG = this.getClass().getName();
    private List<Route> routesList;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView routeNumView, routeNameView, routeOpDaysView, routeOpHoursView;
        public TextView routeNameInView;

        public String routeIDStr;
        public String routeColorStr;

        public  MyViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            context = view.getContext();
            routeNumView = (TextView) view.findViewById(R.id.routeNum);
            routeNameView = (TextView) view.findViewById(R.id.routeNameCardView);
            routeOpDaysView = (TextView) view.findViewById(R.id.routeOpDaysCardView);
            routeOpHoursView = (TextView) view.findViewById(R.id.routeOpHoursCardView);
            routeNameInView = (TextView) view.findViewById(R.id.routeNameInCardView);
        }

        @Override
        public void onClick(View v) {
            TextView nameTextView = (TextView)v.findViewById(R.id.routeNameInCardView);
            String routeName = nameTextView.getText().toString();

            String token = FirebaseInstanceId.getInstance().getToken();

//            Server server = Server.getInstance(context);
//            server.buildRetrofit(Constants.BUS_LOCATOR_URL);
//            server.setApi(ThrifaServerApi.class);
//            ThrifaServerApi service = (ThrifaServerApi) server.getService();
//            Call<Void> call = service.subscribeBus(routeNameView,token);
//
//            call.enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, Response<Void> response) {
//                    if (response != null) {
//
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//
//                }
//            });

            Log.d(TAG,routeName);
            Intent intent =  new Intent(context, MainActivity.class);
            // TODO: user shared preference
            MainTabFragment.mCurrentRoute = routeName;
            MainTabFragment.mCurrentRouteID = routeIDStr;
            MainTabFragment.mCurrentRouteColor = routeColorStr;

            SharedPreferences sharedPref = context.getSharedPreferences(Constants.CURRENT_STATUS_PREFFENCE,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(Constants.CURRENT_ROUTE_KEY,routeName);
            editor.putString(Constants.CURRENT_ROUTE_ID_KEY,routeIDStr);
            editor.commit();

            intent.putExtra(Constants.ROUTE_NAME_KEY,routeName);
            intent.putExtra(Constants.ROUTE_ID_KEY,routeIDStr);
            intent.putExtra(Constants.INTENT_CALL_FROM_KEY, TAG);
            intent.putExtra(Constants.ROUTE_COLOR, v.getDrawingCacheBackgroundColor());
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
        holder.routeNumView.setText(String.format("%02d", Integer.parseInt(route.getRouteNum())));
        holder.routeNameView.setText(route.getRouteName());
        holder.routeOpDaysView.setText(route.getOpDays());
        holder.routeOpHoursView.setText(route.getOpHours());
        holder.routeNameInView.setText(route.getRouteName());

        holder.routeIDStr = route.getRouteID();
        holder.routeColorStr = route.getColor();

        CardView cardView = (CardView)holder.itemView;

        if(route.getColor() == null) {
            if (position % 3 == 0)
                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.materialColorGreen));
            else if (position % 3 == 1)
                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.materialColorCyan));
            else if (position % 3 == 2)
                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.materialColorRed));
        }
        else {
            cardView.setCardBackgroundColor(Color.parseColor(route.getColor()));
        }
    }

    @Override
    public int getItemCount() {
        return routesList.size();
    }
}
