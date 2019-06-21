package pwcg.mission.flight;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;

public class AmbientScrambleBuilder
{
    private Mission mission;
    private Campaign campaign;
    
    public AmbientScrambleBuilder (Mission mission, Campaign campaign)
    {
        this.campaign = campaign;
        this.mission = mission;
    }

    public Flight addPossibleEnemyScramble(Side side, Coordinate airfielPosition) throws PWCGException
    {
        PackageAirfieldScramble packageAirfieldScramble = new PackageAirfieldScramble(mission, campaign, side, airfielPosition);
        Flight enemyScramble = packageAirfieldScramble.createEnemyScramble();
        return enemyScramble;
    }

}
