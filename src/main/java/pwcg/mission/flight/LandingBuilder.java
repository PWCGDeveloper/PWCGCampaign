package pwcg.mission.flight;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.PWCGLocation;
import pwcg.mission.mcu.McuLanding;

public class LandingBuilder
{
    Campaign campaign;
    
    public LandingBuilder (Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public McuLanding createLanding(IAirfield airfield) throws PWCGException, PWCGException  
    {
        PWCGLocation location = airfield.getLandingLocation();
        
        McuLanding landing = new McuLanding();
        landing.setDesc("Land");
        landing.setName("Land");
        landing.setPosition(location.getPosition());
        landing.setOrientation(location.getOrientation());
        
        return landing;
    }

}
