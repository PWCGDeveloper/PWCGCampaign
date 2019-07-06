package pwcg.campaign;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.io.json.CoopUserIOJson;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;

public class CoopHostUserBuilder 
{	
    public static final String HOST_USER_NAME = "Host";
        
	public CoopUser getHostUser() throws PWCGException
    {
		CoopUser hostUser = null;
        List<CoopUser> coopUsers = CoopUserIOJson.readCoopUsers();
        for (CoopUser coopUser : coopUsers)
        {
            if (coopUser.getUsername().equals(HOST_USER_NAME))
            {
            	hostUser = coopUser;
            }
        }
        
        if (hostUser == null)
        {
        	hostUser = makeHostUser("HostPassword");
        }
        
		return hostUser;
    }

	private CoopUser makeHostUser(String password) throws PWCGException
	{
		CoopUser hostUser = new CoopUser();
		hostUser.setUsername(HOST_USER_NAME);
		hostUser.setPassword(password);
		hostUser.setApproved(true);
		hostUser.setNote("Campaign Host");
		
		writeHostUser(hostUser);
		return hostUser;
	}
	
	private void writeHostUser(CoopUser hostUser) throws PWCGException
	{
		List<CoopUser> coopUsers = new ArrayList<>();
		coopUsers.add(hostUser);
		CoopUserIOJson.writeJson(coopUsers);
	}
}
