package pwcg.mission.flight;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
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
        Orientation landingOrientation = createLandingOrientation(airfield);
        Coordinate landCoords = createLandingPosition(airfield, landingOrientation);
        
        McuLanding landing = new McuLanding();
        landing.setDesc("Land");
        landing.setName("Land");
        landing.setPosition(landCoords);        
        landing.setOrientation(landingOrientation);
        
        return landing;
    }

    private Orientation createLandingOrientation(IAirfield airfield) throws PWCGException
    {
        Orientation landingOrientation = new Orientation();
        landingOrientation.setyOri(airfield.getPlaneOrientation() - 180);
        return landingOrientation;
    }

    private Coordinate createLandingPosition(IAirfield airfield, Orientation landingOrientation) throws PWCGException, PWCGException
    {
        int landingDistance = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.LandingDistanceKey);
        Coordinate landCoords = MathUtils.calcNextCoord(
                airfield.getPlanePosition().getPosition(), 
                airfield.getPlaneOrientation(), 
                landingDistance);
        
        landCoords.setYPos(0.0);

        return landCoords;
    }


}
