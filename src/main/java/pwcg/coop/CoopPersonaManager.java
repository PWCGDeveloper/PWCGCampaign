package pwcg.coop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.CoopPersonaIOJson;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.coop.model.CoopPersona;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;

public class CoopPersonaManager
{
    private static CoopPersonaManager instance = null;
    private  Map<String, CoopPersona> coopPersonas = new TreeMap<>();

    private CoopPersonaManager() {};
    
    public static CoopPersonaManager getIntance() throws PWCGException
    {
        if (instance == null)
        {
            instance = new CoopPersonaManager();
            instance.init();
        }
        return instance;
    }

    private void init() throws PWCGException
    {        
        List<CoopPersona> coopPersonaList = CoopPersonaIOJson.readCoopPersonas();
        for (CoopPersona coopPersona : coopPersonaList)
        {
            coopPersonas.put(coopPersona.getPilotName(), coopPersona);
        }
    }
    
    public List<CoopPersona> getAllCoopPersonas() throws PWCGException
    {
        return new ArrayList<>(coopPersonas.values());
    }
    
    public List<CoopPersona> getCoopPersonasForCampaign(Campaign campaign) throws PWCGException
    {
        List<CoopPersona> coopPersonasForCampaign = new ArrayList<>();
        for (CoopPersona coopPersona : getAllCoopPersonas())
        {
            if (campaign.getCampaignData().getName().equals(coopPersona.getCampaignName()))
            {
                coopPersonasForCampaign.add(coopPersona);
            }
        }
        return coopPersonasForCampaign;
    }


    public void createCoopPersona(Campaign campaign, SquadronMember newSquadronMewmber, String coopUsername) throws PWCGException 
    {
        CoopPersona coopPersona = new CoopPersona();
        coopPersona.setCampaignName(campaign.getCampaignData().getName());
        coopPersona.setNote("Created by PWCG");
        coopPersona.setPilotName(newSquadronMewmber.getName());
        coopPersona.setPilotRank(newSquadronMewmber.getRank());
        coopPersona.setSerialNumber(newSquadronMewmber.getSerialNumber());
        coopPersona.setSquadronId(newSquadronMewmber.getSquadronId());
        coopPersona.setUsername(coopUsername);
        coopPersona.setApproved(true);
        
        coopPersonas.put(coopPersona.getPilotName(), coopPersona);
        writePersona(coopPersona);
    }
    
    public void buildHostPersonaForCoopCampaign(Campaign campaign) throws PWCGException
    {
        CoopPersona hostPersona = CoopPersonaHostBuilder.buildHostPersona(campaign);
        coopPersonas.put(hostPersona.getPilotName(), hostPersona);
        writePersona(hostPersona);
    }

    public void removeCoopPersonasForUser(CoopUser userToBeRemoved) throws PWCGException
    {
        if (userToBeRemoved != null)
        {
            for (CoopPersona personaToRemove : getCoopPersonasForUser(userToBeRemoved.getUsername()))
            {
                CoopPersonaRetirement coopPersonaManager = new CoopPersonaRetirement();
                coopPersonaManager.retirePersona(personaToRemove);
    
                removePersonaFile(personaToRemove);
                coopPersonas.remove(personaToRemove.getPilotName());
            }
        }
    }

    public List<CoopPersona> getCoopPersonasForUser(String username) throws PWCGException
    {
        List<CoopPersona> coopPersonasForUser = new ArrayList<>();
        for (CoopPersona coopPersona : coopPersonas.values())
        {
            if (coopPersona.getUsername().equalsIgnoreCase(username))
            {
                coopPersonasForUser.add(coopPersona);
            }
        }
        return coopPersonasForUser;
    }

    public CoopPersona getCoopPersona(String pilotName) throws PWCGException 
    {
        return coopPersonas.get(pilotName);
    }

    public List<CoopPersona> getHostPersonaForCampaign(Campaign campaign) throws PWCGException
    {
        List<CoopPersona> hostPersonasForCampaign = new ArrayList<>();
        for (CoopPersona coopPersona : getPersonasForCampaign(campaign))
        {
            if (coopPersona.getUsername().equals(CoopUserManager.HOST_USER_NAME))
            {
                hostPersonasForCampaign.add(coopPersona);
            }
        }
        return hostPersonasForCampaign;
    }

    public List<CoopPersona> getPersonasForCampaign(Campaign campaign) throws PWCGException
    {
        List<CoopPersona> personasForCampaign = new ArrayList<>();
        for (CoopPersona coopPersona : coopPersonas.values())
        {
            if (coopPersona.getCampaignName().equals(campaign.getCampaignData().getName()))
            {
                personasForCampaign.add(coopPersona);
            }
        }
        return personasForCampaign;
    }

    public boolean isDuplicatePersona(String pilotName) throws PWCGException 
    {
        return coopPersonas.containsKey(pilotName);
    }
    
    
    public void acceptPersonaIntoCampaign(String pilotName, int newPilotSerialNumber) throws PWCGException
    {
        CoopPersona acceptedPersona = getCoopPersona(pilotName);
        if (acceptedPersona != null)
        {
            acceptedPersona.setSerialNumber(newPilotSerialNumber);
            acceptedPersona.setApproved(true);
            writePersona(acceptedPersona);
        }
    }
    
    private void removePersonaFile(CoopPersona personaToRemove)
    {
        String coopPersonaDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCoopDir() + "Pilots\\";                    
        File coopPersonaFile = new File(coopPersonaDir + personaToRemove.getPilotName() + ".json");
        if (coopPersonaFile.exists())
        {
            coopPersonaFile.delete();
        }
    }

    private void writePersona(CoopPersona coopPersona) throws PWCGException
    {
        CoopPersonaIOJson.writeJson(coopPersona);
    }
}
