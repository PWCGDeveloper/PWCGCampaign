package pwcg.campaign.io.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class CoopUserIOJson 
{	
    public static void writeJson(List<CoopUser> coopUsers) throws PWCGException
	{
		for (CoopUser coopUser : coopUsers)
		{
			writeJson(coopUser);
		}
	}

    public static void writeJson(CoopUser coopUser) throws PWCGException
	{
		JsonWriter<CoopUser> jsonWriter = new JsonWriter<>();
        String coopUserDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCoopDir() + "Users\\";
		jsonWriter.writeAsJson(coopUser, coopUserDir, coopUser.getUsername() + ".json");
	}

	public static List<CoopUser> readCoopUsers() throws PWCGException
	{
	    List<CoopUser> coopUsers = new ArrayList<>();
		
		String coopUserDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCoopDir() + "Users\\";
		FileUtils fileUtils = new FileUtils();
		List<File> jsonFiles = fileUtils.getFilesWithFilter(coopUserDir, ".json");
		for (File jsonFile : jsonFiles)
		{
			JsonObjectReader<CoopUser> jsonReader = new JsonObjectReader<>(CoopUser.class);
			CoopUser coopUser = jsonReader.readJsonFile(coopUserDir, jsonFile.getName()); 			
			coopUsers.add(coopUser);
		}
		
		return coopUsers;
	}
}
