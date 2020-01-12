package pwcg.mission.flight;

import java.io.BufferedWriter;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;

public interface IFlight
{
    IFlightData getFlightData();
    Mission getMission();
    Campaign getCampaign();
    void write(BufferedWriter writer) throws PWCGException;
    void finalizeFlight() throws PWCGException;
    void createFlight() throws PWCGException;

    //  TODO - this is a tough ne but needs doing
    Coordinate getTargetPosition();
}