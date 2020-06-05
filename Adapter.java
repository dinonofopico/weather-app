package za.co.dvtweatherapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    LayoutInflater inflater;
    List<Weather> weather;


    public Adapter(Context ctx, List<Weather> weather){

        this.inflater = LayoutInflater.from(ctx);
        this.weather = weather;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = inflater.inflate(R.layout.weather_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.day.setText(weather.get(i).getDay());
        viewHolder.temperature.setText(weather.get(i).getTemperature());
        Picasso.get().load(weather.get(i).getWeatherIcon()).into(viewHolder.weatherIcon);

    }

    @Override
    public int getItemCount() {
        return weather.size();
    }


    public class ViewHolder extends  RecyclerView.ViewHolder {

        TextView day, temperature;
        ImageView weatherIcon;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            day = itemView.findViewById(R.id.day);
            temperature = itemView.findViewById(R.id.temperature);
            weatherIcon = itemView.findViewById(R.id.weatherIcon);

        }
    }
}
