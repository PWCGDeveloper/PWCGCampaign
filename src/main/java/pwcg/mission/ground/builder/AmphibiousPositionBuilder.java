package pwcg.mission.ground.builder;

import pwcg.campaign.battle.AmphibiousAssaultShipDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;

public class AmphibiousPositionBuilder
{
    private AmphibiousAssaultShipDefinition amphibiousAssaultShip;
    private Coordinate assaultPosition;
    private Coordinate defensePosition;
    private Orientation assaultOrientation;
    private Orientation defenseOrientation;

    public AmphibiousPositionBuilder (AmphibiousAssaultShipDefinition amphibiousAssaultShip)
    {
        this.amphibiousAssaultShip = amphibiousAssaultShip;
    }
    
    public void buildPositionAndOrientation(Mission mission) throws PWCGException
    {
        defensePosition = MathUtils.calcNextCoord(mission.getCampaignMap(), amphibiousAssaultShip.getDestination(), amphibiousAssaultShip.getOrientation().getyOri(), 1500);
        assaultPosition = MathUtils.calcNextCoord(mission.getCampaignMap(), amphibiousAssaultShip.getDestination(), amphibiousAssaultShip.getOrientation().getyOri(), 100);

        double defenseAngle = MathUtils.adjustAngle(amphibiousAssaultShip.getOrientation().getyOri(), 180);
        defenseOrientation = new Orientation(defenseAngle);
        assaultOrientation = amphibiousAssaultShip.getOrientation().copy();
    }

    public Coordinate getAssaultPosition()
    {
        return assaultPosition;
    }

    public Coordinate getDefensePosition()
    {
        return defensePosition;
    }

    public Orientation getAssaultOrientation()
    {
        return assaultOrientation;
    }

    public Orientation getDefenseOrientation()
    {
        return defenseOrientation;
    }
 }
