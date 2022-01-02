package pwcg.coop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.io.json.CoopUserIOJson;
import pwcg.coop.model.CoopCampaignPersonas;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;

public class CoopUserManager
{
    private static CoopUserManager instance = null;
    private  Map<String, CoopUser> coopUsers = new TreeMap<>();
    
    private CoopUserManager() {};
    
    public static CoopUserManager getIntance() throws PWCGException
    {
        if (instance == null)
        {
            instance = new CoopUserManager();
            instance.init();
        }
        return instance;
    }

    private void init() throws PWCGException
    {
        List<CoopUser> coopUsersList = CoopUserIOJson.readCoopUsers();
        for (CoopUser coopUser : coopUsersList)
        {
            coopUsers.put(coopUser.getUsername(), coopUser);
        }
    }
    
    public List<CoopUser> getAllCoopUsers() throws PWCGException
    {
        return new ArrayList<>(coopUsers.values());
    }

    public CoopUser buildCoopUser(String username) throws PWCGException 
    {
        CoopUser addedCoopUser = new CoopUser(username, CoopUser.COOP_FORMAT_VERSION);
        
        coopUsers.put(username, addedCoopUser);
        writeUser(addedCoopUser);
        return addedCoopUser;
    }

    public List<Integer> getPersonasForCampaign(String campaignName) throws PWCGException
    {
        List<Integer> personasForCampaign = new ArrayList<>();
        for (CoopUser coopUser : coopUsers.values())
        {
            for (Integer campaignPersona : coopUser.getUserPersonas(campaignName))
            {
                personasForCampaign.add(campaignPersona);
            }
        }
        return personasForCampaign;
    }

    public boolean isCrewMemberCoopPersona(String campaignName, CrewMember crewMember) throws PWCGException
    {
        for (CoopUser coopUser : coopUsers.values())
        {
            for (Integer campaignPersona : coopUser.getUserPersonas(campaignName))
            {
                if (campaignPersona == crewMember.getSerialNumber())
                {
                    return true;
                }
            }
        }
        return false;
    }

    public CoopUser getCoopUserForCrewMember(String campaignName, int serialNumber) throws PWCGException
    {
        for (CoopUser coopUser : coopUsers.values())
        {
            if (coopUser.getCoopCampaignPersona(campaignName) != null)
            {
                if (coopUser.hasPersona(campaignName, serialNumber))
                {
                    return coopUser;
                }
            }
        }
        return null;
    }

    public CoopUser getCoopUser(String username) throws PWCGException 
    {
        return coopUsers.get(username);
    }
    
    public void removeCoopUser(String username) throws PWCGException
    {
        CoopUser userToBeRemoved = getCoopUser(username);
        if (userToBeRemoved != null)
        {
            for (CoopCampaignPersonas campaignPersona : userToBeRemoved.getCoopCampaignPersonas())
            {
                for (int serialNumber : campaignPersona.getSerialNumbers())
                {
                    CoopPersonaRetirement.retirePersona(campaignPersona.getCampaignName(), serialNumber);
                }
            }
            
            removeUserFile(userToBeRemoved);
            coopUsers.remove(username);
        }
    }

    public void createCoopPersona(String campaignName, CrewMember newCrewMember, String coopUsername) throws PWCGException
    {
        CoopUser coopUser = getCoopUser(coopUsername);
        if (coopUser == null)
        {
            coopUser = buildCoopUser(coopUsername);
        }

        if (coopUser != null)
        {
            CoopCampaignPersonas coopCampaignPersona = coopUser.getCoopCampaignPersona(campaignName);
            if (coopCampaignPersona == null)
            {
                coopCampaignPersona = new CoopCampaignPersonas(campaignName);
            }
            
            coopCampaignPersona.addPersona(newCrewMember.getSerialNumber(), newCrewMember.getName());
            coopUser.addCoopCampaignPersonas(campaignName, coopCampaignPersona);
            writeUser(coopUser);
        }
        else
        {
            throw new PWCGException ("Could not find coop user for new persona: " + coopUsername);
        }
    }

    private void removeUserFile(CoopUser userToBeRemoved) throws PWCGException
    {
        String coopUserDir = PWCGDirectoryUserManager.getInstance().getPwcgCoopDir();
        File coopUserFile = new File(coopUserDir + userToBeRemoved.getUsername() + ".json");
        if (coopUserFile.exists())
        {
            coopUserFile.delete();
        }
    }

    public boolean isDuplicateUser(String username) throws PWCGException 
    {
        return coopUsers.containsKey(username);
    }

    private void writeUser(CoopUser coopUser) throws PWCGException
    {
        CoopUserIOJson.writeJson(coopUser);
    }
}
