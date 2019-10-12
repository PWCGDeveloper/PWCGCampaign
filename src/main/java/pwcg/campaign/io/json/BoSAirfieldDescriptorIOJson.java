package pwcg.campaign.io.json;

import pwcg.core.exception.PWCGException;
import pwcg.product.bos.airfield.AirfieldDescriptorSet;

public class BoSAirfieldDescriptorIOJson
{
	public static void writeJson(String directoryName, String filename, AirfieldDescriptorSet descSet) throws PWCGException
	{
		JsonWriter<AirfieldDescriptorSet> jsonWriter = new JsonWriter<>();
		jsonWriter.writeAsJson(descSet, directoryName, filename + ".json");
	}

	public static AirfieldDescriptorSet readJson(String directoryName, String filename) throws PWCGException
	{
		JsonObjectReader<AirfieldDescriptorSet> jsonReader = new JsonObjectReader<>(AirfieldDescriptorSet.class);
		AirfieldDescriptorSet descSet = jsonReader.readJsonFile(directoryName, filename + ".json");
		return descSet;
	}
}
