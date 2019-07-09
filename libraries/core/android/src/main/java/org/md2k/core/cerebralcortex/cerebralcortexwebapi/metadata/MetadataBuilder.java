package org.md2k.core.cerebralcortex.cerebralcortexwebapi.metadata;

import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream.Algorithm;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream.Annotation;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream.Author;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream.DataStream;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream.ExecutionContext;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream.InputParameters;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream.InputStream;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream.Module;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream.OutputStream;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream.ProcessingModule;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream.Reference;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream.StreamMetadata;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCDataDescriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MetadataBuilder {
    public static DataStream buildDataStreamMetadata(String userUUID, MCDataSourceResult dsc) {

        //From DataKit
        ArrayList<MCDataDescriptor> mcDataDescriptors = dsc.getDataSource().getDataDescriptors();


        List<HashMap<String, String>> datasource_dataDescriptors = new ArrayList<>();
        for (int i = 0; i < mcDataDescriptors.size(); i++)
            datasource_dataDescriptors.add(mcDataDescriptors.get(i).asHashMap());
        String datasource_id = dsc.getDataSource().getDataSourceId();
        String datasource_type = dsc.getDataSource().getDataSourceType();
        HashMap<String, String> datasource_metadata = dsc.getDataSource().getDataSourceMetaData().asHashMap();

        String application_id = dsc.getDataSource().getApplicationId();
        String application_type = dsc.getDataSource().getApplicationType();
        HashMap<String, String> application_metadata = dsc.getDataSource().getApplicationMetaData().asHashMap();
        String platform_id = dsc.getDataSource().getPlatformId();
        String platform_type = dsc.getDataSource().getPlatformType();
        HashMap<String, String> platform_metadata = dsc.getDataSource().getPlatformMetaData().asHashMap();
        String platformapp_id = dsc.getDataSource().getPlatformAppId();
        String platformapp_type = dsc.getDataSource().getPlatformAppType();
        HashMap<String, String> platformapp_metadata = dsc.getDataSource().getPlatformAppMetaData().asHashMap();

        UUID ownerUUID = UUID.fromString(userUUID);
        String stream = userUUID + generateDSCString(dsc);
        UUID streamUUID = UUID.nameUUIDFromBytes(stream.getBytes());

        List<String> nameComponents = new ArrayList<>();
        nameComponents.add(datasource_type);
        nameComponents.add(datasource_id);
        nameComponents.add(application_type);
        nameComponents.add(application_id);
        nameComponents.add(platform_type);
        nameComponents.add(platform_id);
        nameComponents.add(platformapp_id);
        nameComponents.add(platformapp_type);
        nameComponents.removeAll(Collections.singleton(null));

        StringBuilder streamName = new StringBuilder(nameComponents.get(0));
        nameComponents.remove(0);
        for (String s : nameComponents) {
            streamName.append("--").append(s);
        }

        //Algorithm
        String algoMethod = "";
        String algoDescription = "";
        String algoAuthorName = "";
        List<String> authors = new ArrayList<>();
        authors.add(algoAuthorName);
        String algoVersion = "";

        //Reference meta
        String referenceUrl = "http://md2k.org/";

        //ProcessingModule meta
        String processingModuleName = "";
        String processingModuleDescription = "";

//        InputParameters inputParameters = new InputParameters(windowSize, windowOffset, lowLevelThreshold, highLevelThreshold);
        InputParameters inputParameters = new InputParameters();
        Reference reference = new Reference(referenceUrl);
        Algorithm algorithm = new Algorithm(algoMethod, algoDescription, authors, algoVersion, reference);

        List<InputStream> inputStreams = new ArrayList<>();

        List<OutputStream> outputStreams = new ArrayList<>();

        List<Algorithm> algorithms = new ArrayList<>();
        algorithms.add(algorithm);


        List<Annotation> annotations = new ArrayList<>();

        ProcessingModule processingModule = new ProcessingModule(processingModuleName, processingModuleDescription, inputParameters, inputStreams, outputStreams, algorithms);
        ExecutionContext executionContext = new ExecutionContext(processingModule, datasource_metadata, application_metadata, platform_metadata, platformapp_metadata);

//        if(rawOrZip=="zip") {
        return new DataStream("datastream", streamUUID.toString(), ownerUUID.toString(), streamName.toString(), datasource_dataDescriptors, executionContext, annotations);
//        }else{
//            DataPoints dataPoints = new DataPoints("12345", "156789", "0101");
//            List<DataPoints> dataPointsList = new ArrayList<DataPoints>();
//            dataPointsList.add(dataPoints);
//            DataStream dataStream = new DataStream(type, identifier, owner, name, dataDescriptors, executionContext, annotations, dataPointsList);
//            return dataStream;
//        }

    }

    private static String generateDSCString(MCDataSourceResult dsc) {
        StringBuilder result = new StringBuilder();
        ArrayList<MCDataDescriptor> mcDataSourceDescriptors = dsc.getDataSource().getDataDescriptors();
        List<HashMap<String, String>> datasource_dataDescriptors = new ArrayList<>();
        for (int i = 0; i < mcDataSourceDescriptors.size(); i++) {
            datasource_dataDescriptors.add(mcDataSourceDescriptors.get(i).asHashMap());
        }
        for (Map<String, String> map : datasource_dataDescriptors) {
            if (map != null) {
                for (Map.Entry<String, String> meta : map.entrySet()) {
                    result.append(meta.getKey());
                    result.append(meta.getValue());
                }
            }
        }

        result.append(dsc.getDataSource().getDataSourceId());
        result.append(dsc.getDataSource().getDataSourceType());
        if (dsc.getDataSource().getDataSourceMetaData() != null) {
            for (Map.Entry<String, String> meta : dsc.getDataSource().getDataSourceMetaData().asHashMap().entrySet()) {
                result.append(meta.getKey());
                result.append(meta.getValue());
            }
        }

        if (dsc.getDataSource().getApplicationId() != null)
            result.append(dsc.getDataSource().getApplicationId());
        else result.append("null");
        if (dsc.getDataSource().getApplicationType() != null)
            result.append(dsc.getDataSource().getApplicationType());
        else result.append("null");
        if (dsc.getDataSource().getApplicationMetaData() != null) {
            for (Map.Entry<String, String> meta : dsc.getDataSource().getApplicationMetaData().asHashMap().entrySet()) {
                result.append(meta.getKey());
                result.append(meta.getValue());
            }
        }
        if (dsc.getDataSource().getPlatformId() != null)
            result.append(dsc.getDataSource().getPlatformId());
        else result.append("null");
        if (dsc.getDataSource().getPlatformType() != null)
            result.append(dsc.getDataSource().getPlatformType());
        else result.append("null");

        if (dsc.getDataSource().getPlatformMetaData() != null) {
            for (Map.Entry<String, String> meta : dsc.getDataSource().getPlatformMetaData().asHashMap().entrySet()) {
                result.append(meta.getKey());
                result.append(meta.getValue());
            }
        }

        if (dsc.getDataSource().getPlatformAppId() != null)
            result.append(dsc.getDataSource().getPlatformAppId());
        else result.append("null");
        if (dsc.getDataSource().getPlatformAppType() != null)
            result.append(dsc.getDataSource().getPlatformAppType());
        else result.append("null");

        if (dsc.getDataSource().getPlatformAppMetaData() != null) {
            for (Map.Entry<String, String> meta : dsc.getDataSource().getPlatformAppMetaData().asHashMap().entrySet()) {
                result.append(meta.getKey());
                result.append(meta.getValue());
            }
        }
        return result.toString();
    }

    public static StreamMetadata buildStreamMetadata(MCDataSourceResult dsc) {
        ArrayList<MCDataDescriptor> mcDataSourceDescriptors = dsc.getDataSource().getDataDescriptors();
        List<HashMap<String, Object>> datasource_dataDescriptors = new ArrayList<>();
        for (int i = 0; i < mcDataSourceDescriptors.size(); i++) {
            HashMap<String, String> h = mcDataSourceDescriptors.get(i).asHashMap();
            HashMap<String, Object> hh = new HashMap<>();
            hh.put("name",h.get("name"));
            hh.put("type",dsc.getDataSource().getSampleType().name());
            hh.put("attributes", h);
            datasource_dataDescriptors.add(hh);
        }

        String datasource_id = dsc.getDataSource().getDataSourceId();
        String datasource_type = dsc.getDataSource().getDataSourceType();
        HashMap<String, String> datasource_metadata = dsc.getDataSource().getDataSourceMetaData().asHashMap();


        String application_id = null;
        String application_type = null;
        HashMap<String, String> application_metadata = null;
        application_id = dsc.getDataSource().getApplicationId();
        application_type = dsc.getDataSource().getApplicationType();
        application_metadata = dsc.getDataSource().getApplicationMetaData().asHashMap();
        String platform_id = null;
        String platform_type = null;
        HashMap<String, String> platform_metadata = null;
        platform_id = dsc.getDataSource().getPlatformId();
        platform_type = dsc.getDataSource().getPlatformType();
        platform_metadata = dsc.getDataSource().getPlatformMetaData().asHashMap();
        String platformapp_id = null;
        String platformapp_type = null;
        HashMap<String, String> platformapp_metadata = null;
        platformapp_id = dsc.getDataSource().getPlatformAppId();
        platform_type = dsc.getDataSource().getPlatformAppType();
        platform_metadata = dsc.getDataSource().getPlatformAppMetaData().asHashMap();

        String streamName = "";
        List<String> nameComponents = new ArrayList<>();
        nameComponents.add(datasource_type);
        nameComponents.add(datasource_id);
        nameComponents.add(application_type);
        nameComponents.add(application_id);
        nameComponents.add(platform_type);
        nameComponents.add(platform_id);
        nameComponents.add(platformapp_id);
        nameComponents.add(platformapp_type);
        nameComponents.removeAll(Collections.singleton(null));

        streamName = nameComponents.get(0);
        nameComponents.remove(0);
        for (String s : nameComponents) {
            streamName += "--" + s;
        }

        String description = dsc.getDataSource().getDataSourceMetaData().getDescription();
        if(description==null) description="";

        String inputStreamName = "";
        String inputStreamIdentifier = "";
        InputStream inputStream = new InputStream(inputStreamName, inputStreamIdentifier);
        List<InputStream> inputStreams = new ArrayList<InputStream>();

        String annotationName = "";
        String annotationIdentifier = "";
        Annotation annotation = new Annotation(annotationName, annotationIdentifier);
        List<Annotation> annotations = new ArrayList<Annotation>();

        String moduleName = "";
        String moduleVersion = "";
        List<Author> moduleAuthors = new ArrayList<Author>();
        Module module = new Module(moduleName, moduleVersion, moduleAuthors);
        List<Module> modules = new ArrayList<Module>();


        StreamMetadata streamMetadata = new StreamMetadata(streamName, description, datasource_dataDescriptors, inputStreams, annotations, modules);
        return streamMetadata;
    }

}
