package pwcg.campaign.io.json;

import pwcg.campaign.group.airfield.AirfieldDescriptorSet;
import pwcg.core.exception.PWCGException;

public class AirfieldDescriptorIOJson
{
	public static void writeJson(String directoryName, String filename, AirfieldDescriptorSet descSet) throws PWCGException
	{
		PwcgJsonWriter<AirfieldDescriptorSet> jsonWriter = new PwcgJsonWriter<>();
		jsonWriter.writeAsJson(descSet, directoryName, filename + ".json");
	}

	public static AirfieldDescriptorSet readJson(String directoryName, String filename) throws PWCGException
	{
		JsonObjectReader<AirfieldDescriptorSet> jsonReader = new JsonObjectReader<>(AirfieldDescriptorSet.class);
		AirfieldDescriptorSet descSet = jsonReader.readJsonFile(directoryName, filename + ".json");
		return descSet;
	}
}
