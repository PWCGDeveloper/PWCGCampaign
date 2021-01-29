package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionSquadronFlightTypes;
import pwcg.mission.flight.FlightTypes;

public class TestMissionFlightTypeBuilder
{
    public static MissionSquadronFlightTypes buildFlightType (Campaign campaign, FlightTypes playerFlightType) throws PWCGException
    {
        Squadron squadron = campaign.findReferenceSquadron();
        MissionSquadronFlightTypes playerFlightTypes = new MissionSquadronFlightTypes();
        playerFlightTypes.add(squadron, playerFlightType);
        return playerFlightTypes;
    }
}
