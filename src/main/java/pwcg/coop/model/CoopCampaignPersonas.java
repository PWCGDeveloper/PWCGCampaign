package pwcg.coop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CoopCampaignPersonas
{
    private String campaignName;
    private Map<Integer, String> campaignPilots = new TreeMap<>();

    public CoopCampaignPersonas()
    {
    }
    
    public CoopCampaignPersonas(String campaignName)
    {
        this.campaignName = campaignName;
    }
    
    public String getCampaignName()
    {
        return campaignName;
    }

    public List<Integer> getSerialNumbers()
    {
        return new ArrayList<Integer>(campaignPilots.keySet());
    }

    public void addPersona(int serialNumber, String pilotName)
    {
        if (!campaignPilots.containsKey(serialNumber))
        {
            campaignPilots.put(serialNumber, pilotName);
        }
    }

    public boolean hasPersona(int serialNumber)
    {
        if (campaignPilots.containsKey(serialNumber))
        {
            return true;
        }
        return false;
    }

    public void remove(int serialNumber)
    {
        if (campaignPilots.containsKey(serialNumber))
        {
            campaignPilots.remove(serialNumber);
        }        
    }
}
