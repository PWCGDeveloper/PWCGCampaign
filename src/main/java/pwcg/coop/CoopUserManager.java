package pwcg.coop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.CoopUserIOJson;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.coop.model.CoopPersona;
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
        CoopUser addedCoopUser = new CoopUser();
        addedCoopUser.setUsername(username);
        
        coopUsers.put(username, addedCoopUser);
        writeUser(addedCoopUser);
        return addedCoopUser;
    }

    public List<CoopPersona> getPersonasForCampaign(Campaign campaign) throws PWCGException
    {
        List<CoopPersona> personasForCampaign = new ArrayList<>();
        for (CoopUser coopUser : coopUsers.values())
        {
            for (CoopPersona coopPersona : coopUser.getUserPersonas())
            {
                if (coopPersona.getCampaignName().equals(campaign.getCampaignData().getName()))
                {
                    personasForCampaign.add(coopPersona);
                }
            }
        }
        return personasForCampaign;
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
            for (CoopPersona coopPersona : userToBeRemoved.getUserPersonas())
            {
                CoopPersonaRetirement.retirePersona(coopPersona);
            }
            
            removeUserFile(userToBeRemoved);
            coopUsers.remove(username);
        }
    }

    public void createCoopPersona(Campaign campaign, SquadronMember newSquadronMewmber, String coopUsername) throws PWCGException
    {
        CoopUser coopUser = getCoopUser(coopUsername);
        if (coopUser == null)
        {
            coopUser = buildCoopUser(coopUsername);
        }

        if (coopUser != null)
        {
            CoopPersona persona = new CoopPersona();
            persona.setCoopUsername(coopUsername);
            persona.setSerialNumber(newSquadronMewmber.getSerialNumber());
            persona.setCampaignName(campaign.getCampaignData().getName());
            coopUser.addPersona(persona);
            writeUser(coopUser);
        }
        else
        {
            throw new PWCGException ("Could not find coop user for new persona: " + coopUsername);
        }
    }

    private void removeUserFile(CoopUser userToBeRemoved) throws PWCGException
    {
        String coopUserDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCoopDir() + "Users\\";                    
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

    public CoopPersona getCoopPersona(int serialNumber)
    {
        for (CoopUser coopUser : coopUsers.values())
        {
            for (CoopPersona coopPersona : coopUser.getUserPersonas())
            {
                if (coopPersona.getSerialNumber() == serialNumber)
                {
                    return coopPersona;
                }
            }
        }
        return null;
    }
}
