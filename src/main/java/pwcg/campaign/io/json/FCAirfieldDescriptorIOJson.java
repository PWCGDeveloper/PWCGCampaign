package pwcg.campaign.io.json;

import pwcg.core.exception.PWCGException;
import pwcg.product.fc.airfield.FCAirfieldConfiguration;

public class FCAirfieldDescriptorIOJson
{
	public static void writeJson(String directoryName, String filename, FCAirfieldConfiguration.AirfieldDescriptorSet descSet) throws PWCGException
	{
		JsonWriter<FCAirfieldConfiguration.AirfieldDescriptorSet> jsonWriter = new JsonWriter<>();
		jsonWriter.writeAsJson(descSet, directoryName, filename + ".json");
	}

	public static FCAirfieldConfiguration.AirfieldDescriptorSet readJson(String directoryName, String filename) throws PWCGException
	{
		JsonObjectReader<FCAirfieldConfiguration.AirfieldDescriptorSet> jsonReader = new JsonObjectReader<>(FCAirfieldConfiguration.AirfieldDescriptorSet.class);
		FCAirfieldConfiguration.AirfieldDescriptorSet descSet = jsonReader.readJsonFile(directoryName, filename + ".json");
		return descSet;
	}
}
