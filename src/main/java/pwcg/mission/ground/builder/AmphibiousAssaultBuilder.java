package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pwcg.campaign.battle.AmphibiousAssault;
import pwcg.campaign.battle.AmphibiousAssaultShip;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.IGroundUnit;

public class AmphibiousAssaultBuilder
{
    private Mission mission;
    private AmphibiousAssault amphibiousAssault;
    
    public AmphibiousAssaultBuilder(Mission mission, AmphibiousAssault amphibiousAssault)
    {
        
    }
    
    public GroundUnitCollection generateAmphibiousAssault() throws PWCGException
    {
        AmphibiousAssaultShipBuilder amphibiousAssaultShipBuilder = new AmphibiousAssaultShipBuilder(mission, amphibiousAssault);
        GroundUnitCollection ships = amphibiousAssaultShipBuilder.makeShips(amphibiousAssault);
        GroundUnitCollection defense = makeDefense(amphibiousAssault);
        
        ships.merge(defense);

        List<IGroundUnit> primaryAssaultSegmentGroundUnits = new ArrayList<>();
        primaryAssaultSegmentGroundUnits.add(ships.getPrimaryGroundUnit());

        ships.setPrimaryGroundUnit(primaryAssaultSegmentGroundUnits.get(0));
        ships.finishGroundUnitCollection();
        return ships;
    }

    private Coordinate makeLandingCraftStartPosition(AmphibiousAssaultShip amphibiousAssaultShip) throws PWCGException
    {
        double angle = MathUtils.adjustAngle(amphibiousAssaultShip.getOrientation().getyOri(), 180);
        Coordinate startPosition = MathUtils.calcNextCoord(amphibiousAssaultShip.getDestination(), angle, 2000);
        return startPosition;
    }

    private static GroundUnitCollection makeDefense(AmphibiousAssault amphibiousAssault)
    {
        // Make Defending MGs 200 yards from ship landing at orientation = ship orientation
        // Make Defending AT guns 500 yards
        // Make Defending Artillery 2KM
        return null;
    }
 }
