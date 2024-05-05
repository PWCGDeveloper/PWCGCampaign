package pwcg.campaign.io.json;

import pwcg.campaign.group.airfield.AirfieldDescriptorMapSet;
import pwcg.core.exception.PWCGException;

public class AirfieldDescriptorIOMapJson
{
	public static void writeJson(String directoryName, String filename, AirfieldDescriptorMapSet descSet) throws PWCGException
	{
		PwcgJsonWriter<AirfieldDescriptorMapSet> jsonWriter = new PwcgJsonWriter<>();
		jsonWriter.writeAsJson(descSet, directoryName, filename + ".json");
	}

	public static AirfieldDescriptorMapSet readJson(String directoryName, String filename) throws PWCGException
	{
		JsonObjectReader<AirfieldDescriptorMapSet> jsonReader = new JsonObjectReader<>(AirfieldDescriptorMapSet.class);
		AirfieldDescriptorMapSet descSet = jsonReader.readJsonFile(directoryName, filename + ".json");
		return descSet;
	}
}
