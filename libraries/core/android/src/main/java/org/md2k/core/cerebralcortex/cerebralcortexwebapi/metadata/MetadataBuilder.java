package org.md2k.core.cerebralcortex.cerebralcortexwebapi.metadata;

import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream.Author;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream.DataDescriptor;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream.Module;
import org.md2k.core.cerebralcortex.cerebralcortexwebapi.models.stream.StreamMetadata;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.datakitapi.datasource.metadata.MCDataDescriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetadataBuilder {


    private static String generateDSCString(MCDataSourceResult dsc) { //TODO: Check for use and remove this
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

        List<DataDescriptor> datasource_dataDescriptors = new ArrayList<>();
        for (int i = 0; i < mcDataSourceDescriptors.size(); i++) {
            HashMap<String, String> attr = mcDataSourceDescriptors.get(i).asHashMap();

            DataDescriptor dd = new DataDescriptor(dsc.getDataSource().getDataType().name().replace("_ARRAY", ""), mcDataSourceDescriptors.get(i).getName());
            dd.setDescription(mcDataSourceDescriptors.get(i).getDescription());

            attr.remove("name");
            attr.remove("description");
            if (attr.size() > 0) {
                dd.setAttributes(attr);
            }

            datasource_dataDescriptors.add(dd);
        }

        String datasource_id = dsc.getDataSource().getDataSourceId();
        String datasource_type = dsc.getDataSource().getDataSourceType();

        String application_id = dsc.getDataSource().getApplicationId();
        String application_type = dsc.getDataSource().getApplicationType();


        String platform_id = dsc.getDataSource().getPlatformId();
        String platform_type = dsc.getDataSource().getPlatformType();

        String platformapp_id = dsc.getDataSource().getPlatformAppId();
        String platformapp_type = dsc.getDataSource().getPlatformAppType();


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

        String description = dsc.getDataSource().getDataSourceMetaData().getDescription();
        if(description==null) {
            description="None";
        }

        String moduleVersion = dsc.getDataSource().getApplicationMetaData().getVersion();

        List<Author> moduleAuthors = new ArrayList<>();
        Author a = new Author("Syed Monowar Hossain", "dev@md2k.org"); //TODO: Change this once stream generating modules are required to insert this information.
        moduleAuthors.add(a);


        HashMap<String, Object> moduleAttributes= new HashMap<>();
        moduleAttributes.put("datasource_type",datasource_type);
        moduleAttributes.put("datasource_id",datasource_id);
        moduleAttributes.put("application_type",application_type);
        moduleAttributes.put("application_id",application_id);
        moduleAttributes.put("platform_type",platform_type);
        moduleAttributes.put("platform_id",platform_id);
        moduleAttributes.put("platformapp_type",platformapp_type);
        moduleAttributes.put("platformapp_id",platformapp_id);

        Module module = new Module(application_type, moduleVersion, moduleAuthors, moduleAttributes);
        List<Module> modules = new ArrayList<>();
        modules.add(module);


        StreamMetadata streamMetadata = new StreamMetadata(streamName.toString(), description, datasource_dataDescriptors, modules);
        return streamMetadata;
    }

}
