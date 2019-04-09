package org.md2k.phonesensor.sensor;

import java.util.concurrent.TimeUnit;

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
public class Frequency {
    private double frequency;
    private TimeUnit timeUnit;

    public Frequency(double frequency, TimeUnit timeUnit) {
        this.frequency = frequency;
        this.timeUnit = timeUnit;
    }
    private double getFrequencyFromDays(double frequency, TimeUnit timeUnit){
        switch(timeUnit){
            case NANOSECONDS:       return frequency/(24.0*60*60*1000*1000*1000);
            case MILLISECONDS:      return frequency/(24.0*60*60*1000);
            case SECONDS:           return frequency/(24.0*60*60);
            case MINUTES:           return frequency/(24.0*60);
            case HOURS:             return frequency/24.0;
            case DAYS:              return frequency;
            default: return frequency;
        }
    }
    public Frequency as(TimeUnit toTimeUnit){
        return new Frequency(getFrequencyFromDays(getFrequencyInDays(frequency, timeUnit),toTimeUnit), toTimeUnit);
    }

    public double getFrequency() {
        return frequency;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    private double getFrequencyInDays(double frequency, TimeUnit timeUnit){
        switch(timeUnit){
            case NANOSECONDS:       return frequency*24*60*60*1000*1000*1000;
            case MICROSECONDS:      return frequency*24*60*60*1000*1000;
            case MILLISECONDS:      return frequency*24*60*60*1000;
            case SECONDS:           return frequency*24*60*60;
            case MINUTES:           return frequency*24*60;
            case HOURS:             return frequency*24;
            case DAYS:              return frequency;
            default: return frequency;
        }
    }


}
