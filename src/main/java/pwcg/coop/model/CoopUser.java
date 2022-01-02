package pwcg.coop.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoopUser
{
    public static final String COOP_FORMAT_VERSION = "V2";
    
    private String formatVersion;
    private String username;
    private Map<String, CoopCampaignPersonas> campaignPersonas = new HashMap<>();
    
    public CoopUser(String username, String formatVersion)
    {
        this.username = username;
        this.formatVersion = formatVersion;
    }

    public String getUsername()
    {
        return username;
    }

    public void addPersona(String campaignName, int serialNumber, String crewMemberName)
    {
        if (!campaignPersonas.containsKey(campaignName))
        {
            CoopCampaignPersonas coopCampaignPersona = new CoopCampaignPersonas(campaignName);
            campaignPersonas.put(campaignName, coopCampaignPersona);
        }

        CoopCampaignPersonas coopCampaignPersona = campaignPersonas.get(campaignName);
        coopCampaignPersona.addPersona(serialNumber, crewMemberName);
    }
    
    public void removePersona(String campaignName, int serialNumber)
    {
        if (campaignPersonas.containsKey(campaignName))
        {
            campaignPersonas.get(campaignName).remove(serialNumber);
        }
    }
    
    public boolean hasPersona(String campaignName, int serialNumber)
    {
        if (campaignPersonas.containsKey(campaignName))
        {
            if (campaignPersonas.get(campaignName).hasPersona(serialNumber))
            {
                return true;
            }
        }
        
        return false;
    }

    public List<CoopCampaignPersonas> getCoopCampaignPersonas()
    {
        return new ArrayList<CoopCampaignPersonas>(campaignPersonas.values());
    }

    public CoopCampaignPersonas getCoopCampaignPersona(String campaignName)
    {
        return campaignPersonas.get(campaignName);
    }

    public List<Integer> getUserPersonas(String campaignName)
    {
        if (campaignPersonas.containsKey(campaignName))
        {
            return new ArrayList<Integer>(campaignPersonas.get(campaignName).getSerialNumbers());
        }
        return new ArrayList<>();
    }

    public void addCoopCampaignPersonas(String campaignName, CoopCampaignPersonas coopCampaignPersona)
    {
        campaignPersonas.put(campaignName, coopCampaignPersona);
    }

    public String getFormatVersion()
    {
        return formatVersion;
    }

    public void validate()
    {
        if (campaignPersonas == null)
        {
            campaignPersonas = new HashMap<>();
        }
    }    
}
