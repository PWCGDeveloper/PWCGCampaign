package pwcg.campaign.io.json;

import pwcg.product.bos.airfield.BoSAirfieldConfiguration;
import pwcg.core.exception.PWCGException;

public class BoSAirfieldDescriptorIOJson
{
	public static void writeJson(String directoryName, String filename, BoSAirfieldConfiguration.AirfieldDescriptorSet descSet) throws PWCGException
	{
		JsonWriter<BoSAirfieldConfiguration.AirfieldDescriptorSet> jsonWriter = new JsonWriter<>();
		jsonWriter.writeAsJson(descSet, directoryName, filename + ".json");
	}

	public static BoSAirfieldConfiguration.AirfieldDescriptorSet readJson(String directoryName, String filename) throws PWCGException
	{
		JsonObjectReader<BoSAirfieldConfiguration.AirfieldDescriptorSet> jsonReader = new JsonObjectReader<>(BoSAirfieldConfiguration.AirfieldDescriptorSet.class);
		BoSAirfieldConfiguration.AirfieldDescriptorSet descSet = jsonReader.readJsonFile(directoryName, filename + ".json");
		return descSet;
	}
}
