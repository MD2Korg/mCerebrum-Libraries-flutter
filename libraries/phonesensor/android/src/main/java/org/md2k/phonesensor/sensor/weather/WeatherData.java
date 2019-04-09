package org.md2k.phonesensor.sensor.weather;

public class WeatherData {
    private String city;
    private String country;
    private double latitude;
    private double longitude;
    private double temperature;
    private double temperatureMinimum;
    private double temperatureMaximum;
    private double pressure;
    private double pressureSeaLevel;
    private double pressureGroundLevel;
    private double humidity;
    private double windSpeed;
    private double windDirectionDegree;
    private long sunriseTime;
    private long sunsetTime;
    private double cloudiness;

    public WeatherData() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getTemperatureMinimum() {
        return temperatureMinimum;
    }

    public void setTemperatureMinimum(double temperatureMinimum) {
        this.temperatureMinimum = temperatureMinimum;
    }

    public double getTemperatureMaximum() {
        return temperatureMaximum;
    }

    public void setTemperatureMaximum(double temperatureMaximum) {
        this.temperatureMaximum = temperatureMaximum;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getPressureSeaLevel() {
        return pressureSeaLevel;
    }

    public void setPressureSeaLevel(double pressureSeaLevel) {
        this.pressureSeaLevel = pressureSeaLevel;
    }

    public double getPressureGroundLevel() {
        return pressureGroundLevel;
    }

    public void setPressureGroundLevel(double pressureGroundLevel) {
        this.pressureGroundLevel = pressureGroundLevel;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getWindDirectionDegree() {
        return windDirectionDegree;
    }

    public void setWindDirectionDegree(double windDirectionDegree) {
        this.windDirectionDegree = windDirectionDegree;
    }

    public long getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(long sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public long getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(long sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    public double getCloudiness() {
        return cloudiness;
    }

    public void setCloudiness(double cloudiness) {
        this.cloudiness = cloudiness;
    }
}
