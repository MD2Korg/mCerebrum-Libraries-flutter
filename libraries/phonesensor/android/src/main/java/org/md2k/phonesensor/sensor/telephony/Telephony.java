package org.md2k.phonesensor.sensor.telephony;

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

/**
 * Telephony module. Keeps track of changes in the network operator and information:
 * - Current network operator information
 * - Cell Location ID's
 * - Neighbor cell towers
 * - Signal strength
 *
 * @author denzil
 */
public class Telephony /*extends AbstractSensor*/ {
/*
    private TelephonyManager telephonyManager = null;
    private TelephonyState telephonyState = new TelephonyState();
    private static SignalStrength lastSignalStrength = null;
    private String[] requiredPermissions=new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
    };

    *//**
     * Broadcasted event: new telephony information is available
     *//*
    public static final String ACTION_AWARE_TELEPHONY = "ACTION_AWARE_TELEPHONY";

    *//**
     * Broadcasted event: connected to a new CDMA tower
     *//*
    public static final String ACTION_AWARE_CDMA_TOWER = "ACTION_AWARE_CDMA_TOWER";

    *//**
     * Broadcasted event: connected to a new GSM tower
     *//*
    public static final String ACTION_AWARE_GSM_TOWER = "ACTION_AWARE_GSM_TOWER";

    *//**
     * Broadcasted event: detected GSM tower neighbor
     *//*
    public static final String ACTION_AWARE_GSM_TOWER_NEIGHBOR = "ACTION_AWARE_GSM_TOWER_NEIGHBOR";

    Telephony(Context context) {
        super(context);
    }

    @Override
    public boolean isSupported() {
        return false;
    }

    @Override
    public boolean hasPermission() {
        return false;
    }

    @Override
    public void getPermission(Activity activity, PermissionCallback permissionCallback) {

    }

    @Override
    public HashMap<String, String> getSensorInfo() {
        return null;
    }


    public interface AWARESensorObserver {
        void onSignalStrengthChanged(SignalStrength strength);

        void onCellChanged(CellLocation cellLocation);
    }

    @Override
    public void startSensing() {
        telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(telephonyState, PhoneStateListener.LISTEN_CELL_LOCATION | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

    }
    @Override
    public void stopSensing() {
        telephonyManager.listen(telephonyState, PhoneStateListener.LISTEN_NONE);
    }

    @Override
    public SensorSettings getDefaultSettings() {
        return null;
    }

    *//**
     * Tracks cell location and telephony information:
     * - GSM: CID, LAC, PSC (UMTS Primary Scrambling Code)
     * - CDMA: base station ID, Latitude, Longitude, Network ID, System ID
     * - Telephony: IMEI/MEID/ESN, software version, line number, network MMC, network code, network name, network type, phone type, sim code, sim operator, sim serial, subscriber ID
     *
     * @author df
     *//*
    public class TelephonyState extends PhoneStateListener {

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            lastSignalStrength = signalStrength;
        }

        @Override
        public void onCellLocationChanged(CellLocation location) {
            super.onCellLocationChanged(location);
            if (location instanceof GsmCellLocation) {
                GsmCellLocation l = (GsmCellLocation) location;
                int[] data = TelephonyData.createGSMData(l.getCid(), l.getLac(), l.getPsc());
*//*
                rowData.put(GSM_Data.SIGNAL_STRENGTH, lastSignalStrength.getGsmSignalStrength());
                rowData.put(GSM_Data.GSM_BER, lastSignalStrength.getGsmBitErrorRate());


                    Intent newGSM = new Intent(Telephony.ACTION_AWARE_GSM_TOWER);
                    sendBroadcast(newGSM);
*//*

            } else {
                CdmaCellLocation l= (CdmaCellLocation) location;
                int[] data  = TelephonyData.createCDMAData(l.getBaseStationId(),l.getBaseStationLatitude(), l.getBaseStationLongitude(), l.getSystemId(), l.getNetworkId());
*//*
                rowData.put(CDMA_Data.SIGNAL_STRENGTH, lastSignalStrength.getCdmaDbm());
                rowData.put(CDMA_Data.CDMA_ECIO, lastSignalStrength.getCdmaEcio());
                rowData.put(CDMA_Data.EVDO_DBM, lastSignalStrength.getEvdoDbm());
                rowData.put(CDMA_Data.EVDO_ECIO, lastSignalStrength.getEvdoEcio());
                rowData.put(CDMA_Data.EVDO_SNR, lastSignalStrength.getEvdoSnr());

                try {
                    getContentResolver().insert(CDMA_Data.CONTENT_URI, rowData);

                    Intent newCDMA = new Intent(Telephony.ACTION_AWARE_CDMA_TOWER);
                    sendBroadcast(newCDMA);

                    if (Aware.DEBUG) Log.d(TAG, "CDMA tower:" + rowData.toString());
                } catch (SQLiteException e) {
                    if (Aware.DEBUG) Log.d(TAG, e.getMessage());
                } catch (SQLException e) {
                    if (Aware.DEBUG) Log.d(TAG, e.getMessage());
                }
*//*
            }
*//*
            ContentValues rowData = new ContentValues();
            rowData.put(Telephony_Data.DEVICE_ID, device_id);
            rowData.put(Telephony_Data.DATA_ENABLED, telephonyManager.getDataState());
            rowData.put(Telephony_Data.IMEI_MEID_ESN, Encrypter.hash(getApplicationContext(), telephonyManager.getDeviceId()));
            rowData.put(Telephony_Data.SOFTWARE_VERSION, telephonyManager.getDeviceSoftwareVersion());
            rowData.put(Telephony_Data.LINE_NUMBER, Encrypter.hashPhone(getApplicationContext(), telephonyManager.getLine1Number()));
            rowData.put(Telephony_Data.NETWORK_COUNTRY_ISO_MCC, telephonyManager.getNetworkCountryIso());
            rowData.put(Telephony_Data.NETWORK_OPERATOR_CODE, telephonyManager.getNetworkOperator());
            rowData.put(Telephony_Data.NETWORK_OPERATOR_NAME, telephonyManager.getNetworkOperatorName());
            rowData.put(Telephony_Data.NETWORK_TYPE, telephonyManager.getNetworkType());
            rowData.put(Telephony_Data.PHONE_TYPE, telephonyManager.getPhoneType());
            rowData.put(Telephony_Data.SIM_STATE, telephonyManager.getSimState());
            rowData.put(Telephony_Data.SIM_OPERATOR_CODE, telephonyManager.getSimOperator());
            rowData.put(Telephony_Data.SIM_OPERATOR_NAME, telephonyManager.getSimOperatorName());
            rowData.put(Telephony_Data.SIM_SERIAL, Encrypter.hash(getApplicationContext(), telephonyManager.getSimSerialNumber()));
            rowData.put(Telephony_Data.SUBSCRIBER_ID, Encrypter.hash(getApplicationContext(), telephonyManager.getSubscriberId()));
*//*
        }
    }*/
}
