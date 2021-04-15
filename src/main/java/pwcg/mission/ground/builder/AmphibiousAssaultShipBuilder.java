package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.battle.AmphibiousAssaultShipDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.unittypes.transport.LandingCraftUnit;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class AmphibiousAssaultShipBuilder
{
    private Mission mission;
    private ICountry shipCountry;
    private List<AmphibiousAssaultShipDefinition> shipsForMission;
    private GroundUnitCollection amphibiousAssaultShips;
    
    public AmphibiousAssaultShipBuilder(Mission mission, List<AmphibiousAssaultShipDefinition> shipsForMission, ICountry shipCountry)
    {
        this.mission = mission;
        this.shipsForMission = shipsForMission;
        this.shipCountry = shipCountry;
    }

    public GroundUnitCollection generateAmphibiousAssautShips() throws PWCGException
    {
        amphibiousAssaultShips = buildLandingCraft();
        GroundUnitCollection destroyers = buildLandingDestroyers();
        amphibiousAssaultShips.merge(destroyers);
        finishGroundUnitCollection();
        return amphibiousAssaultShips;
    }

    private void finishGroundUnitCollection() throws PWCGException
    {
        List<IGroundUnit> primaryAssaultSegmentGroundUnits = new ArrayList<>();
        primaryAssaultSegmentGroundUnits.add(amphibiousAssaultShips.getPrimaryGroundUnit());
        amphibiousAssaultShips.setPrimaryGroundUnit(primaryAssaultSegmentGroundUnits.get(0));
        amphibiousAssaultShips.finishGroundUnitCollection();
    }

    private GroundUnitCollection buildLandingCraft() throws PWCGException
    {
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION, 
                "Landing Craft", 
                TargetType.TARGET_SHIPPING,
                Coalition.getCoalitions());

        GroundUnitCollection landingCraftGroundUnit = new GroundUnitCollection("Landing Craft", groundUnitCollectionData);
        for (AmphibiousAssaultShipDefinition amphibiousAssaultShip : shipsForMission)
        {
            if (amphibiousAssaultShip.getShipType().startsWith("land"))
            {
                GroundUnitInformation groundUnitInformation = createGroundUnitInformationForUnit(amphibiousAssaultShip);
                LandingCraftUnit landingCraftUnit = new LandingCraftUnit(groundUnitInformation);
                landingCraftUnit.createGroundUnit();
                landingCraftGroundUnit.addGroundUnit(landingCraftUnit);
                amphibiousAssaultShip.setGroundUnit(landingCraftGroundUnit);
            }
        }

        return landingCraftGroundUnit;
    }

    private GroundUnitCollection buildLandingDestroyers() throws PWCGException
    {
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION, 
                "Destroyer", 
                TargetType.TARGET_SHIPPING,
                Coalition.getCoalitions());

        GroundUnitCollection destroyers = new GroundUnitCollection("Destroyer", groundUnitCollectionData);
        for (AmphibiousAssaultShipDefinition amphibiousAssaultShip : shipsForMission)
        {
            if (amphibiousAssaultShip.getShipType().startsWith("dest"))
            {
                GroundUnitInformation groundUnitInformation = createGroundUnitInformationForUnit(amphibiousAssaultShip);
                LandingCraftUnit destroyerUnit = new LandingCraftUnit(groundUnitInformation);
                destroyerUnit.createGroundUnit();
                destroyers.addGroundUnit(destroyerUnit);
            }
        }

        return destroyers;
    }

    private GroundUnitInformation createGroundUnitInformationForUnit(AmphibiousAssaultShipDefinition amphibiousAssaultShip) throws PWCGException
    {
        TargetDefinition targetDefinition = makeTargetDefinition(amphibiousAssaultShip);        

        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                mission.getCampaign(), 
                targetDefinition.getCountry(), 
                TargetType.TARGET_SHIPPING,
                targetDefinition.getPosition(), 
                amphibiousAssaultShip.getDestination(),
                amphibiousAssaultShip.getOrientation().copy());

        groundUnitInformation.setDestination(amphibiousAssaultShip.getDestination());
        groundUnitInformation.setUnitSize(GroundUnitSize.GROUND_UNIT_SIZE_TINY);
        return groundUnitInformation;
    }

    private TargetDefinition makeTargetDefinition(AmphibiousAssaultShipDefinition amphibiousAssaultShip) throws PWCGException
    {
        Coordinate startPosition = makeLandingCraftStartPosition(amphibiousAssaultShip);
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_SHIPPING, startPosition, shipCountry);
        return targetDefinition;
    }

    private Coordinate makeLandingCraftStartPosition(AmphibiousAssaultShipDefinition amphibiousAssaultShip) throws PWCGException
    {
        double angle = MathUtils.adjustAngle(amphibiousAssaultShip.getOrientation().getyOri(), 180);
        Coordinate startPosition = MathUtils.calcNextCoord(amphibiousAssaultShip.getDestination(), angle, 700);
        return startPosition;
    }
 }
