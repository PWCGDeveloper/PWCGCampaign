package pwcg.campaign.api;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.bomb.BombingWaypoints.BombingAltitudeLevel;

public interface IMissionAltitudeGenerator
{
    int flightAltitude(Campaign campaign) throws PWCGException;
    int getBombingAltitude(BombingAltitudeLevel bombingAltitudeLevel);
    int getLowAltitudePatrolAltitude();
}