package org.md2k.core.cerebralcortex.cerebralcortexwebapi;

import android.util.Log;

import com.google.gson.Gson;

import org.md2k.core.cerebralcortex.cerebralcortexwebapi.interfaces.CerebralCortexWebApi;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.AuthRequest;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.AuthResponse;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.CCApiErrorMessage;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.MinioBucket;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.MinioBucketsList;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.MinioObjectStats;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.MinioObjectsListInBucket;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.UserMetadata;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.UserRegisterRequest;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.UserSettings;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream.RegisterResponse;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream.StreamMetadata;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


/******************
 * EXAMPLES

 CerebralCortexWebApi ccService = ApiUtils.getCCService("https://fourtytwo.md2k.org/");
 CCWebAPICalls ccWebAPICalls = new CCWebAPICalls(ccService);
 AuthResponse ar = ccWebAPICalls.authenticateUser(username, password);


 List<MinioBucket> buckets = ccWebAPICalls.getMinioBuckets(ar.getAccessToken().toString());

 List<MinioObjectStats> objectList = ccWebAPICalls.getObjectsInBucket(ar.getAccessToken().toString(), buckets.get(0).getBucketName().toString());

 MinioObjectStats object = ccWebAPICalls.getObjectStats(ar.getAccessToken().toString(), buckets.get(0).getBucketName().toString(), "203_mcerebrum_syed_new.pdf");
 MinioObjectStats object = ccWebAPICalls.getObjectStats(ar.getAccessToken().toString(), "configuration", "mperf.zip");


 Boolean result = ccWebAPICalls.downloadMinioObject(ar.getAccessToken().toString(), "configuration", "mperf.zip", "mperf.zip");


 MetadataBuilder metadataBuilder = new MetadataBuilder();
 DataStream dataStreamMetadata = metadataBuilder.buildDataStreamMetadata("datastream", "123", "999", "sampleStream", "zip");
 Boolean resultUpload = ccWebAPICalls.putArchiveDataAndMetadata(ar.getAccessToken().toString(), dataStreamMetadata, "/storage/emulated/0/Android/data/org.md2k.datakit/files/raw/raw2/2017092217_2.csv.gz");

 */


public class CCWebAPICalls {

    /**
     * Instance of the <code>CerebralCortexWebApi</code>.
     */
    private CerebralCortexWebApi ccService;

    /**
     * Constructor
     *
     * @param ccService Instance of the <code>CerebralCortexWebApi</code>.
     */
    public CCWebAPICalls(CerebralCortexWebApi ccService) {
        this.ccService = ccService;
    }

    public ResponseBody registerUser(String userName, String userPassword, String userRole,
                                     ArrayList<UserMetadata> userMetadata, ArrayList<UserSettings> userSettings) {
        AuthRequest authRequest = new AuthRequest(userName, userPassword);
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest(authRequest, userRole,
                userMetadata, userSettings);
        Call<ResponseBody> call = ccService.registerUser(userRegisterRequest);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                return (ResponseBody) response.body();
            } else {
                Gson gson = new Gson();
                try {
                    CCApiErrorMessage errorBody = gson.fromJson(response.errorBody().charStream(),
                            CCApiErrorMessage.class);
                    Log.e("CCWebAPI", "Not successful " + errorBody.getMessage());
                } catch (Exception e) {
                    Log.e("CCWebAPI", "Server URL is not like a Cerebral Cortex instance");
                }
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("CCWebAPICalls", e.getMessage());
            return null;
        }
    }

    /**
     * Authenticates the user.
     *
     * <p>
     * Example
     * <code>
     * CerebralCortexWebApi ccService = ApiUtils.getCCService("https://fourtytwo.md2k.org/");
     * CCWebAPICalls ccWebAPICalls = new CCWebAPICalls(ccService);
     * AuthResponse ar = ccWebAPICalls.authenticateUser(username, password);
     * </code>
     * </p>
     *
     * @param userName     Username
     * @param userPassword Password
     * @return An <code>AuthResponse</code>.
     */
    public AuthResponse authenticateUser(String userName, String userPassword) {
        AuthRequest authRequest = new AuthRequest(userName, userPassword);
        Call<AuthResponse> call = ccService.authenticateUser(authRequest);

        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                return (AuthResponse) response.body();
            } else {
                Gson gson = new Gson();
                try {
                    CCApiErrorMessage errorBody = gson.fromJson(response.errorBody().charStream(),
                            CCApiErrorMessage.class);
                    Log.e("CCWebAPI", "Not successful " + errorBody.getMessage());
                } catch (Exception e) {
                    Log.e("CCWebAPI", "Server URL is not like a Cerebral Cortex instance");
                }
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("CCWebAPICalls", e.getMessage());
            return null;
        }
    }


    /**
     * Returns a list of Minio buckets.
     *
     * <p>
     * Example
     * <code>
     * List<MinioBucket> buckets = ccWebAPICalls.getMinioBuckets(ar.getAccessToken().toString());
     * </code>
     * </p>
     *
     * @param accessToken Authenticated access token.
     * @return A list of Minio buckets.
     */
    public List<MinioBucket> getBucketsList(String accessToken) {
        Call<MinioBucketsList> call = ccService.getBucketsList(accessToken);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                return ((MinioBucketsList) response.body()).getMinioBuckets();
            } else {
                Gson gson = new Gson();
                CCApiErrorMessage errorBody = gson.fromJson(response.errorBody().charStream(),
                        CCApiErrorMessage.class);
                Log.e("CCWebAPI", "Not successful " + errorBody.getMessage());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("CCWebAPICalls", e.getMessage());
            return null;
        }
    }

    /**
     * Gets a list of Minio objects in the given bucket.
     *
     * <p>
     * <code>
     * List<MinioObjectStats> objectList = ccWebAPICalls.getObjectsInBucket(ar.getAccessToken()
     * .toString(), buckets.get(0).getBucketName().toString());
     * </code>
     * </p>
     *
     * @param accessToken Authenticated access token.
     * @param bucketName  Name of the bucket.
     * @return List of Minio objects.
     */
    public List<MinioObjectStats> getObjectsInBucket(String accessToken, String bucketName) {
        Call<MinioObjectsListInBucket> call = ccService.objectsListInBucket(bucketName, accessToken);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                return ((MinioObjectsListInBucket) response.body()).getBucketObjects();
            } else {
                Gson gson = new Gson();
                CCApiErrorMessage errorBody = gson.fromJson(response.errorBody().charStream(),
                        CCApiErrorMessage.class);
                Log.e("CCWebAPI", "Not successful " + errorBody.getMessage());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("CCWebAPICalls", e.getMessage());
            return null;
        }
    }

    /**
     * Gets a single Minio object.
     *
     * <p>
     * Example
     * <code>
     * MinioObjectStats object = ccWebAPICalls.getObjectStats(ar.getAccessToken().toString(),
     * buckets.get(0).getBucketName().toString(), "203_mcerebrum_syed_new.pdf");
     * </code>
     * <code>
     * MinioObjectStats object = ccWebAPICalls.getObjectStats(ar.getAccessToken().toString(),
     * "configuration", "mperf.zip");
     * </code>
     * </p>
     *
     * @param accessToken Authenticated access token.
     * @param bucketName  Name of the bucket.
     * @param objectName  Name of the object.
     * @return The given Minio object.
     */
    public MinioObjectStats getObjectStats(String accessToken, String bucketName, String objectName) {
        Call<MinioObjectStats> call = ccService.getMinioObjectStats(bucketName, objectName, accessToken);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                return (MinioObjectStats) response.body();
            } else {
                Gson gson = new Gson();
                CCApiErrorMessage errorBody = gson.fromJson(response.errorBody().charStream(),
                        CCApiErrorMessage.class);
                Log.e("CCWebAPI", "Not successful " + errorBody.getMessage());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("CCWebAPICalls", e.getMessage());
            return null;
        }
    }

    /**
     * Downloads the given Minio object.
     *
     * <p>
     * Example
     * <code>
     * Boolean result = ccWebAPICalls.downloadMinioObject(ar.getAccessToken().toString(),
     * "configuration", "mperf.zip", "mperf.zip");
     * </code>
     * </p>
     *
     * @param accessToken    Authenticated access token.
     * @param bucketName     Name of the bucket.
     * @param objectName     Name of the object to download.
     * @param outputFileName Name of the file containing the Minio object.
     * @return
     */
    public HashMap downloadMinioObject(String accessToken, String bucketName, String objectName) throws Exception {
        Call<ResponseBody> call = ccService.downloadMinioObject(objectName, bucketName, accessToken);
            Response response = call.execute();
            if (response.isSuccessful()) {
                Gson gson = new Gson();
                return gson.fromJson(((String) response.body()), HashMap.class);
//                return ApiUtils.writeResponseToDisk((ResponseBody) response.body(), outputFileDir, outputFileName);
            } else {
                Gson gson = new Gson();
                CCApiErrorMessage errorBody = gson.fromJson(response.errorBody().charStream(),
                        CCApiErrorMessage.class);
                Log.e("CCWebAPI", "Not successful " + errorBody.getMessage());
                throw new Exception(errorBody.getMessage());
            }
    }

    public Boolean getStreamData(String streamName, String accessToken) {
        Call<ResponseBody> call = ccService.getStreamData(streamName, accessToken);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                // TODO: figure out what should happen when the response is successful
                return true;
            } else {
                Gson gson = new Gson();
                CCApiErrorMessage errorBody = gson.fromJson(response.errorBody().charStream(),
                        CCApiErrorMessage.class);
                Log.e("CCWebAPI", "Not successful " + errorBody.getMessage());
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("CCWebAPICalls", e.getMessage());
            return false;
        }
    }

    public StreamMetadata getStreamMetadata(String streamName, String accessToken) {
        Call<StreamMetadata> call = ccService.getStreamMetadata(streamName, accessToken);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                return (StreamMetadata) response.body();
            } else {
                Gson gson = new Gson();
                CCApiErrorMessage errorBody = gson.fromJson(response.errorBody().charStream(),
                        CCApiErrorMessage.class);
                Log.e("CCWebAPI", "not successful " + errorBody.getMessage());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("CCWebAPICalls", e.getMessage());
            return null;
        }
    }

    public RegisterResponse registerDataStream(String accessToken, StreamMetadata streamMetadata) {
        Call<RegisterResponse> call = ccService.registerDataStream(accessToken, streamMetadata);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                return (RegisterResponse) response.body();
            } else {
                Gson gson = new Gson();
                CCApiErrorMessage errorBody = gson.fromJson(response.errorBody().charStream(),
                        CCApiErrorMessage.class);
//                Log.e("CCWebAPI", "not successful " + errorBody.getMessage());
                return null;
            }
        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e("CCWebAPICalls", e.getMessage());
            return null;
        }
    }

    /**
     * Uploads metadata and puts it with the archive data.
     *
     * <p>
     * Example
     * <code>
     * MetadataBuilder metadataBuilder = new MetadataBuilder();
     * DataStream dataStreamMetadata = metadataBuilder
     * .buildDataStreamMetadata("datastream", "123", "999", "sampleStream", "zip");
     * Boolean resultUpload = ccWebAPICalls
     * .putArchiveDataAndMetadata(ar.getAccessToken().toString(), dataStreamMetadata,
     * "/storage/emulated/0/Android/data/org.md2k.datakit/files/raw/raw2/2017092217_2.csv.gz");
     * </code>
     * </p>
     *
     * @param accessToken Authenticated access token.
     * @param metadata    Metadata to upload.
     * @param filePath    of the Multipart request.
     * @return Whether the upload was successful or not.
     */
//    public Boolean putArchiveDataAndMetadata(String accessToken, DataStream metadata, String filePath, UploadMetadata uploadMetadata) {
//        MultipartBody.Part fileMultiBodyPart = ApiUtils.getUploadFileMultipart(filePath);
//        Call<ResponseBody> call = ccService.putArchiveDataStreamWithMetadata(accessToken, metadata, uploadMetadata,
//                fileMultiBodyPart);
//        try {
//            Response response = call.execute();
//            if (response.isSuccessful()) {
//                Log.d("CCWebAPI", "Successfully uploaded: " + filePath);
//                return true;
//            } else {
//                Gson gson = new Gson();
//                CCApiErrorMessage errorBody = gson.fromJson(response.errorBody().charStream(),
//                        CCApiErrorMessage.class);
//                Log.e("CCWebAPI", "Not successful " + errorBody.getMessage());
//                return false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("CCWebAPICalls", e.getMessage());
//            return false;
//        }
//    }
    public Boolean putDataStream(String hashId, String filePath, String accessToken) {
        File uploadFile = new File(filePath);
        Call<ResponseBody> call = ccService.putDataStream(hashId, uploadFile, accessToken);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                Log.d("CCWebAPI", "Successfully uploaded: " + filePath);
                return true;
            } else {
                Gson gson = new Gson();
                CCApiErrorMessage errorBody = gson.fromJson(response.errorBody().charStream(),
                        CCApiErrorMessage.class);
                Log.e("CCWebAPI", "Not successful " + errorBody.getMessage());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("CCWebAPICalls", e.getMessage());
            return false;
        }
    }
}
