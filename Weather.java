package za.co.dvtweatherapp;

public class Weather {

    private  String day, temperature, weatherIcon;

    public  Weather(){}
    public Weather(String day, String temperature, String weatherIcon) {
        this.day = day;
        this.temperature = temperature;
        this.weatherIcon = weatherIcon;
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

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }
}
