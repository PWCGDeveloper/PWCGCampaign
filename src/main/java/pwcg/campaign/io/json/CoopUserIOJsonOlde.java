package pwcg.campaign.io.json;

import java.io.File;

import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.coop.model.CoopUserOlde;
import pwcg.core.exception.PWCGException;

public class CoopUserIOJsonOlde 
{	
	public static CoopUserOlde readCoopUser(String username) throws PWCGException
	{	    
        String coopUserDir = PWCGDirectoryUserManager.getInstance().getPwcgCoopDir();                    
		File jsonFile = new File(coopUserDir + username + ".json");
		if (jsonFile.exists())
		{
			JsonObjectReader<CoopUserOlde> jsonReader = new JsonObjectReader<>(CoopUserOlde.class);
			CoopUserOlde coopUserOlde = jsonReader.readJsonFile(coopUserDir, jsonFile.getName()); 			
	        return coopUserOlde;
		}
		
		return null;
	}
}
