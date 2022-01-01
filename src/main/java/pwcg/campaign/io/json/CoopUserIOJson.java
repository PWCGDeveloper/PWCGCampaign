package pwcg.campaign.io.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

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
        verifyCoopDirs();
        
		PwcgJsonWriter<CoopUser> jsonWriter = new PwcgJsonWriter<>();
        String coopUserDir = PWCGDirectoryUserManager.getInstance().getPwcgCoopDir();                    
		jsonWriter.writeAsJson(coopUser, coopUserDir, coopUser.getUsername() + ".json");
	}

	public static List<CoopUser> readCoopUsers() throws PWCGException
	{
	    verifyCoopDirs();
	    
	    List<CoopUser> coopUsers = new ArrayList<>();
		
        String coopUserDir = PWCGDirectoryUserManager.getInstance().getPwcgCoopDir();                    
		List<File> jsonFiles = FileUtils.getFilesWithFilter(coopUserDir, ".json");
		for (File jsonFile : jsonFiles)
		{
			JsonObjectReader<CoopUser> jsonReader = new JsonObjectReader<>(CoopUser.class);
			CoopUser coopUser = jsonReader.readJsonFile(coopUserDir, jsonFile.getName()); 
			coopUser.validate();
			try 
			{
			    coopUser.getCoopCampaignPersonas();
	            coopUsers.add(coopUser);
			}
			catch (Exception e)
			{
			    PWCGLogger.log(LogLevel.ERROR, "Fubar " + jsonFile.getName());
			}
		}
		
		return coopUsers;
	}
	
	private static void verifyCoopDirs()
	{
        String coopUserDir = PWCGDirectoryUserManager.getInstance().getPwcgCoopDir();                    
        FileUtils.createDirIfNeeded(coopUserDir);
	}
}
