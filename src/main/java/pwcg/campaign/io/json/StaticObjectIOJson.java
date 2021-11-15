package pwcg.campaign.io.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;
import pwcg.mission.ground.vehicle.VehicleDefinition;

public class StaticObjectIOJson 
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
		PwcgJsonWriter<VehicleDefinition> jsonWriter = new PwcgJsonWriter<>();
		String staticObjectDir = PWCGContext.getInstance().getDirectoryManager().getPwcgStaticObjectDir();
		jsonWriter.writeAsJson(vehicleDefinition, staticObjectDir, vehicleDefinition.getVehicleType() + ".json");
	}

	public static List<VehicleDefinition> readJson() throws PWCGException
	{
		List<VehicleDefinition>  vehicleSets = new ArrayList<>();
		
		String staticObjectDir = PWCGContext.getInstance().getDirectoryManager().getPwcgStaticObjectDir();
		List<File> jsonFiles = FileUtils.getFilesWithFilter(staticObjectDir, ".json");
		for (File jsonFile : jsonFiles)
		{
			JsonObjectReader<VehicleDefinition> jsonReader = new JsonObjectReader<>(VehicleDefinition.class);
			VehicleDefinition vehicleDefinition = jsonReader.readJsonFile(staticObjectDir, jsonFile.getName()); 			
			vehicleSets.add(vehicleDefinition);
		}
		
		return vehicleSets;
	}
}
