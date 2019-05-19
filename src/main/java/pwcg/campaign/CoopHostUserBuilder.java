package pwcg.campaign;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.io.json.CoopUserIOJson;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;

public class CoopHostUserBuilder 
{
	public CoopUser makeHostUser(String password) throws PWCGException
	{
		CoopUser hostUser = new CoopUser();
		hostUser.setUsername("Host");
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
