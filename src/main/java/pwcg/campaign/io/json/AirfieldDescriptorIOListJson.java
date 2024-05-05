package pwcg.campaign.io.json;

import pwcg.campaign.group.airfield.AirfieldDescriptorListSet;
import pwcg.core.exception.PWCGException;

public class AirfieldDescriptorIOListJson
{
	public static void writeJson(String directoryName, String filename, AirfieldDescriptorListSet descSet) throws PWCGException
	{
		PwcgJsonWriter<AirfieldDescriptorListSet> jsonWriter = new PwcgJsonWriter<>();
		jsonWriter.writeAsJson(descSet, directoryName, filename + ".json");
	}

	public static AirfieldDescriptorListSet readJson(String directoryName, String filename) throws PWCGException
	{
		JsonObjectReader<AirfieldDescriptorListSet> jsonReader = new JsonObjectReader<>(AirfieldDescriptorListSet.class);
		AirfieldDescriptorListSet descSet = jsonReader.readJsonFile(directoryName, filename + ".json");
		return descSet;
	}
}
