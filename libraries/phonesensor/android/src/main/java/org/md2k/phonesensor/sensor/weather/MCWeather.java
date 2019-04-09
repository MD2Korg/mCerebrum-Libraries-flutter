package org.md2k.phonesensor.sensor.weather;

/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center
 * - Syed Monowar Hossain <monowar.hossain@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
public class MCWeather {/*extends MCAbstractSensor {
    private Frequency readFrequency;
    private Handler handlerReadFixed;
    private String OPEN_WEATER_API = "7d7b933da3daa969bfaa629908099a98";

    public MCWeather(Context context) {
        super(context, SensorType.WEATHER,new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_COARSE_LOCATION,
        });
        setWriteAsReceived();
        handlerReadFixed = new Handler();
    }
    public void setReadFrequency(double frequency, TimeUnit timeUnit){
        this.readFrequency=new Frequency(frequency, timeUnit);
    }
    public void setWriteAsReceived(){
        super.setWriteAsReceived();
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            read();
            long readInMillis = (long) (1000.0/ readFrequency.as(TimeUnit.SECONDS).getFrequency());
            handlerReadFixed.postDelayed(runnable, readInMillis);
        }
    };
    private void read(){
        OpenWeatherMapHelper helper = new OpenWeatherMapHelper(OPEN_WEATER_API);
        helper.setUnits(Units.METRIC);
        helper.setLang(Lang.ENGLISH);
        helper.getCurrentWeatherByGeoCoordinates(5.6037, 0.1870, new CurrentWeatherCallback() {
            @Override
            public void onSuccess(CurrentWeather currentWeather) {
                long offset = TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings();
                WeatherData weatherData = new WeatherData(
                        currentWeather.getName(),
                        currentWeather.getSys().getCountry(),
                        currentWeather.getCoord().getLat(),
                        currentWeather.getCoord().getLon(),
                        currentWeather.getMain().getTemp(),
                        currentWeather.getMain().getTempMin(),
                        currentWeather.getMain().getTempMax(),
                        currentWeather.getMain().getPressure(),
                        currentWeather.getMain().getSeaLevel(),
                        currentWeather.getMain().getGrndLevel(),
                        currentWeather.getMain().getHumidity(),
                        currentWeather.getWind().getSpeed(),
                        currentWeather.getWind().getDeg(),
                        currentWeather.getSys().getSunrise()*1000+offset,
                        currentWeather.getSys().getSunrise()*1000+offset,
                        currentWeather.getClouds().getAll()
                );
                setSample(System.currentTimeMillis(), weatherData);
            }

            @Override
            public void onFailure(Throwable throwable) {
                handlerReadFixed.postDelayed(runnable, 1000*60);
            }
        });

    }

    @Override
    public void startSensing() {
        long readInMillis = (long) (1000.0/ readFrequency.as(TimeUnit.SECONDS).getFrequency());

        handlerReadFixed.post(runnable);

    }

    @Override
    public void stopSensing() {
        handlerReadFixed.removeCallbacks(runnable);
    }


    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public HashMap<String, String> getSensorInfo() {
        return null;
    }
    @Override
    protected boolean isChanged(Object prevData, Object curData, Comparison comparison) {
        return true;
    }
*/
}
