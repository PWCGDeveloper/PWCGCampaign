package pwcg.campaign.io.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;
import pwcg.mission.ground.vehicle.VehicleDefinition;

public class VehicleDefinitionIOJson 
{	
    public static void writeJson(List<VehicleDefinition>  vehicleDefinitions) throws PWCGException
	{
		for (VehicleDefinition vehicleDefinition : vehicleDefinitions)
		{
			writeJson(vehicleDefinition);
		}
	}

    public static void writeJson(VehicleDefinition vehicleDefinition) throws PWCGException
	{
		JsonWriter<VehicleDefinition> jsonWriter = new JsonWriter<>();
		String vehicleDir = PWCGContext.getInstance().getDirectoryManager().getPwcgVehiclesDir();
		jsonWriter.writeAsJson(vehicleDefinition, vehicleDir, vehicleDefinition.getVehicleType() + ".json");
	}

	public static List<VehicleDefinition> readJson() throws PWCGException
	{
		List<VehicleDefinition>  vehicleSets = new ArrayList<>();
		
		String vehicleDir = PWCGContext.getInstance().getDirectoryManager().getPwcgVehiclesDir();
		FileUtils fileUtils = new FileUtils();
		List<File> jsonFiles = fileUtils.getFilesWithFilter(vehicleDir, ".json");
		for (File jsonFile : jsonFiles)
		{
			JsonObjectReader<VehicleDefinition> jsonReader = new JsonObjectReader<>(VehicleDefinition.class);
			VehicleDefinition vehicleDefinition = jsonReader.readJsonFile(vehicleDir, jsonFile.getName()); 			
			vehicleSets.add(vehicleDefinition);
		}
		
		return vehicleSets;
	}
}
