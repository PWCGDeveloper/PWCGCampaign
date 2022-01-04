package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionSquadronFlightTypes;
import pwcg.mission.flight.FlightTypes;

public class TestMissionFlightTypeBuilder
{
    public static MissionSquadronFlightTypes buildFlightType (Campaign campaign, FlightTypes playerFlightType) throws PWCGException
    {
        Company squadron = campaign.findReferenceSquadron();
        MissionSquadronFlightTypes playerFlightTypes = new MissionSquadronFlightTypes();
        playerFlightTypes.add(squadron, playerFlightType);
        return playerFlightTypes;
    }
}
