package pwcg.mission.flight;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.scramble.AirfieldScramblePackage;

public class AmbientScrambleBuilder
{
    private Mission mission;
    private Campaign campaign;
    
    public AmbientScrambleBuilder (Mission mission, Campaign campaign)
    {
        this.campaign = campaign;
        this.mission = mission;
    }

    public IFlight addPossibleEnemyScramble(Side side, Coordinate airfielPosition) throws PWCGException
    {
        AirfieldScramblePackage packageAirfieldScramble = new AirfieldScramblePackage(mission, campaign, side, airfielPosition);
        IFlight enemyScramble = packageAirfieldScramble.createEnemyScramble();
        return enemyScramble;
    }

}
