package pwcg.campaign.target;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;

public class StrategicTargetBuilder
{
    public TargetDefinition getStrategicTarget(Campaign campaign, Mission mission, Squadron squadron) throws PWCGException
    {        
        Coordinate targetGeneralLocation = GeneralTargetLocationGenerator.createTargetGeneralLocation(campaign, mission, squadron);
        
        TargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilder();
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinitionForStrategicFlight(campaign, squadron, FlightTypes.STRATEGIC_BOMB, targetGeneralLocation);
        return targetDefinition;
    }
}
