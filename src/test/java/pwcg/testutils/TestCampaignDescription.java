package pwcg.testutils;

import java.util.Date;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.plane.PwcgRole;

public class TestCampaignDescription
{
    private boolean isRoF;
    private ArmedService service;
    private PwcgRole role;
    private Date campaignDate;
    private FrontMapIdentifier map;
    private Campaign campaign;

    public boolean isRoF()
    {
        return isRoF;
    }

    public void setRoF(boolean isRoF)
    {
        this.isRoF = isRoF;
    }

    public ArmedService getService()
    {
        return service;
    }
    
    public void setService(ArmedService service)
    {
        this.service = service;
    }
    
    public PwcgRole getRole()
    {
        return role;
    }
    
    public void setRole(PwcgRole role)
    {
        this.role = role;
    }
    
    public Date getCampaignDate()
    {
        return campaignDate;
    }
    
    public void setCampaignDate(Date campaignDate)
    {
        this.campaignDate = campaignDate;
    }
    
    public FrontMapIdentifier getMap()
    {
        return map;
    }
    
    public void setMap(FrontMapIdentifier map)
    {
        this.map = map;
    }

    public Campaign getCampaign()
    {
        return campaign;
    }

    public void setCampaign(Campaign campaign)
    {
        this.campaign = campaign;
    }
}
