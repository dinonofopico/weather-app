package za.co.dvtweatherapp;

public class Weather {

    private  String day, temperature, max, min;
    private int weatherIcon, bgColor, position;

    public  Weather(){}
    public Weather(String day, String temperature, int weatherIcon, int bgColor, int position,
                   String max, String min) {
        this.day = day;
        this.temperature = temperature;
        this.weatherIcon = weatherIcon;
        this.bgColor = bgColor;
        this.position = position;
        this.max = max;
        this.min = min;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(int weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }
}
