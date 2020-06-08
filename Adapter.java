package za.co.dvtweatherapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter extends RecyclerView.Adapter {

    LayoutInflater inflater;
    List<Weather> weather;



    public Adapter(Context ctx, List<Weather> weather){

        this.inflater = LayoutInflater.from(ctx);
        this.weather = weather;
    }

    @Override
    public int getItemViewType(int position) {

        if(weather.get(position).getPosition() == 0){

            return 0;
        }

//        if(weather.get(position).getPosition() == 1){
//
//            return 1;
//        }
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view;


        if (i == 0){

            view = inflater.inflate(R.layout.main_temperature, viewGroup, false);
            return new ViewHolderTwo(view);

        }
//        else if(i == 1){
//
//            view = inflater.inflate(R.layout.min_max_row, viewGroup, false);
//            return  new ViewHolderThree(view);
//        }

        view = inflater.inflate(R.layout.weather_list, viewGroup, false);

        return new ViewHolderOne(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        if(weather.get(i).getPosition() == 0){

            ViewHolderTwo viewHolder1 = (ViewHolderTwo) viewHolder;
//
//            viewHolder1.currentTemp.setText(weather.get(i).getDay());
            viewHolder1.currentTemp.setText(weather.get(i).getTemperature());
            viewHolder1.currentTempImg.setImageResource(weather.get(i).getWeatherIcon());
//

//
        }
//        else if(weather.get(i).getPosition() == 1){
//
//            ViewHolderThree viewHolderThree = (ViewHolderThree) viewHolder;
//            viewHolderThree.min.setText(weather.get(i).getMin());
//            viewHolderThree.max.setText(weather.get(i).getMax());
//        }
        else {
//
            ViewHolderOne viewHolder2 = (ViewHolderOne) viewHolder;
//
            viewHolder2.day.setText(weather.get(i).getDay());
            viewHolder2.temperature.setText(weather.get(i).getTemperature());
            viewHolder2.weatherIcon.setImageResource(weather.get(i).getWeatherIcon());
            viewHolder2.contentList.setBackgroundColor(weather.get(i).getBgColor());
//
//
        }


    }

    @Override
    public int getItemCount() {
        return weather.size();
    }


    public class ViewHolderOne extends  RecyclerView.ViewHolder {

        TextView day, temperature;
        ImageView weatherIcon;
        CardView contentList;

        public ViewHolderOne(@NonNull View itemView) {

            super(itemView);

            day = itemView.findViewById(R.id.day);
            temperature = itemView.findViewById(R.id.temperature);
            weatherIcon = itemView.findViewById(R.id.weatherIcon);
            contentList = itemView.findViewById(R.id.contentList);


        }
    }

    public class ViewHolderTwo extends  RecyclerView.ViewHolder {

        ImageView currentTempImg;
        TextView currentTemp;


        public ViewHolderTwo(@NonNull View itemView) {
            super(itemView);


            currentTempImg = itemView.findViewById(R.id.currentTempImg);
            currentTemp = itemView.findViewById(R.id.currentTemp);
        }


    }

    public  class  ViewHolderThree extends  RecyclerView.ViewHolder{

        TextView max, min;

        public ViewHolderThree(@NonNull View itemView) {
            super(itemView);

            max = itemView.findViewById(R.id.max_temperature);
            min = itemView.findViewById(R.id.min_temperature);
        }
    }

}
