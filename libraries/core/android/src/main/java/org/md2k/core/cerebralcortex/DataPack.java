package org.md2k.core.cerebralcortex;

import android.util.Log;

import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream.DataStream;
import org.md2k.mcerebrumapi.data.MCData;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCDataDescriptor;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import io.reactivex.annotations.NonNull;

class DataPack {
    /**
     * Main upload method for an individual <code>DataStream</code>.
     *
     * <p>
     * This method is responsible for offloading all unsynced data from low-frequency sources.
     * The data is offloaded to an SQLite database.
     * </p>
     *
     * @param dsc        <code>DataSourceClient</code> to upload.
     * @param dsMetadata Metadata for the data stream.
     */
    static boolean createMessagePack(MCDataSourceResult dsc, ArrayList<MCData> objects, String filename) {
        ArrayList<String> headers = generateHeaders(dsc);
        int dataLength = determineDataLength(objects.get(0));
        File tempFile = new File(filename+".temp");
        try {
            MessagePacker packer = MessagePack.newDefaultPacker(new FileOutputStream(tempFile));

            // Pack headers
            packer.packArrayHeader(headers.size());
            for (String header : headers) {
                packer.packString(header);
            }

            for (MCData row : objects) {  // checks if dataType is an array
                // Pack data
                packer.packArrayHeader(dataLength + 2);
                packer.packLong(row.getTimestamp() * 1000);
                //TODO: local time
                packer.packLong((row.getTimestamp()) * 1000);
                packData(packer, row);
            }
            packer.close();
            return zipFile(tempFile, new File(filename));
        } catch (IOException e) {
            Log.e("CerebralCortex", "MessagePack creation failed" + e);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Constructs an ArrayList of headers from the name field of the <code>DataDescriptor</code>.
     *
     * @param dsMetadata Metadata of the datastream.
     * @param dsc        <code>DataSourceClient</code> used to get the <code>Ds_id</code> for troubleshooting.
     * @return The ArrayList of headers.
     */
    private static ArrayList<String> generateHeaders(MCDataSourceResult dsc) {
        ArrayList<MCDataDescriptor> dataDescList = dsc.getDataSource().getDataDescriptors();
        ArrayList<String> headers = new ArrayList<>();
        headers.add("Timestamp");
        headers.add("Localtime");
        for (MCDataDescriptor dataDescriptor : dataDescList) {
            headers.add(dataDescriptor.getName());
        }
        int k = 0;
        for (String header : headers) {
            if (header.contains(" ")) {
                headers.set(k, header.replaceAll(" ", "_"));
            }
            k++;
        }
        return headers;
    }

    /**
     * Determines if the <code>DataDescriptor</code> properly describes each column of data. If the
     * <code>DataDescriptor</code> and number of data columns aren't the same, the datastream will not be uploaded.
     *
     * @param mcData List of <code>RowObjects</code> queried from the database.
     * @return The number of columns of data.
     */
    private static int determineDataLength(@NonNull MCData mcData) {
        int dataLength = 0;
        switch (mcData.getSampleType()) {
            case BOOLEAN_ARRAY:
                dataLength = ((boolean[]) mcData.getSample()).length;
                break;
            case BYTE_ARRAY:
                dataLength = ((byte[]) mcData.getSample()).length;
                break;
            case INT_ARRAY:
                dataLength = ((int[]) mcData.getSample()).length;
                break;
            case DOUBLE_ARRAY:
                dataLength = ((double[]) mcData.getSample()).length;
                break;
            case LONG_ARRAY:
                dataLength = ((long[]) mcData.getSample()).length;
                break;
            case STRING_ARRAY:
                dataLength = ((String[]) mcData.getSample()).length;
                break;
            case OBJECT:
                dataLength = 1;
                break;
        }
/*
        if (datalength != headers.size() - 2) { // -1 because of "Timestamp"
            FL.e(TAG, "DataDescriptor not properly defined. This datastream (" + dsMetadata.getName() + ") will not be uploaded. Ds_id: " + dsc.getDs_id());
            canUpload = false;
        }
*/
        return dataLength;
    }

    /**
     * Determines the specific <code>DataType</code> of the data and packs it accordingly.
     * Note that Array headers are not packed here as the data's array length is determined in <code>determingDataLength</code>
     * and packed in <code>publishDataStream</code>.
     *
     * @param packer MessagePacker that packs the data into the messagepack buffer.
     * @param data   Data to pack.
     * @return The amended MessagePacker.
     */
    private static void packData(MessagePacker packer, MCData data) {
        try {
            switch (data.getSampleType()) {
                case BOOLEAN_ARRAY:
                    boolean[] booleans = data.getSample();
                    for (boolean element : booleans) packer.packBoolean(element);
                    break;
                case BYTE_ARRAY:
                    byte[] bytes = data.getSample();
                    for (byte element : bytes) packer.packByte(element);
                    break;
                case INT_ARRAY:
                    int[] ints = data.getSample();
                    for (int element : ints) packer.packInt(element);
                    break;
                case LONG_ARRAY:
                    long[] longs = data.getSample();
                    for (long element : longs) packer.packLong(element);
                    break;
                case DOUBLE_ARRAY:
                    double[] doubles = data.getSample();
                    for (double element : doubles) packer.packDouble(element);
                    break;
                case STRING_ARRAY:
                    String[] strings = data.getSample();
                    for (String element : strings) packer.packString(element);
                    break;
                case OBJECT:
                    String stringObject = data.getSample();
                    packer.packString(stringObject);
                    break;
            }
        } catch (IOException e) {
            Log.e("MessagePack", "Data packing failed " + e);
            e.printStackTrace();
        }
    }

    /**
     * Compresses the given MessagePack file using GZIP. The given MessagePack is deleted after compression.
     *
     * @param inFile MessagePack to compress.
     */
    private static boolean zipFile(File inFile, File outFile) {
        try {

            FileInputStream input = new FileInputStream(inFile);
            GZIPOutputStream gzipOut = new GZIPOutputStream(new FileOutputStream(outFile));
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) != -1) {
//                Log.e("MessagePack", "Writing buffer");
                gzipOut.write(buffer, 0, len);
            }
//            Log.e("MessagePack", "Closing files");
            gzipOut.close();
            input.close();
            return inFile.delete();
        } catch (IOException e) {
//            Log.e("CerebralCortex", "Compressed file creation failed" + e);
            e.printStackTrace();
            return false;
        }
    }
}
