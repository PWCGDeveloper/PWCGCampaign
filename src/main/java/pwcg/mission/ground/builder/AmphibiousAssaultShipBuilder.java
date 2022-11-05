package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.battle.AmphibiousAssault;
import pwcg.campaign.battle.AmphibiousAssaultShipDefinition;
import pwcg.campaign.factory.CountryFactory;
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
    private AmphibiousAssault amphibiousAssault;
    private List<AmphibiousAssaultShipDefinition> shipsForMission;
    private GroundUnitCollection amphibiousAssaultShips;
    
    public AmphibiousAssaultShipBuilder(Mission mission, AmphibiousAssault amphibiousAssault, List<AmphibiousAssaultShipDefinition> shipsForMission)
    {
        this.mission = mission;
        this.shipsForMission = shipsForMission;
        this.amphibiousAssault = amphibiousAssault;
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

        GroundUnitCollection landingCraftGroundUnit = new GroundUnitCollection(mission.getCampaign(), "Landing Craft", groundUnitCollectionData);
        for (AmphibiousAssaultShipDefinition amphibiousAssaultShip : shipsForMission)
        {
            if (amphibiousAssaultShip.getShipClass() == AmphibiousAssaultShipClass.LANDING_CRAFT || 
                amphibiousAssaultShip.getShipClass() == AmphibiousAssaultShipClass.LANDING_CRAFT_TANK)
            {
                GroundUnitInformation groundUnitInformation = createGroundUnitInformationForUnit(amphibiousAssaultShip);
                LandingCraftUnit landingCraftUnit = new LandingCraftUnit(groundUnitInformation);
                landingCraftUnit.createGroundUnit();
                landingCraftGroundUnit.addGroundUnit(landingCraftUnit);
                amphibiousAssaultShip.setLandingCraftGroundUnit(landingCraftGroundUnit);
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

        GroundUnitCollection destroyers = new GroundUnitCollection(mission.getCampaign(), "Destroyer", groundUnitCollectionData);
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
        ICountry country = CountryFactory.makeCountryByCountry(amphibiousAssault.getAggressorCountry());
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_SHIPPING, startPosition, country, "Landing Craft");
        return targetDefinition;
    }

    private Coordinate makeLandingCraftStartPosition(AmphibiousAssaultShipDefinition amphibiousAssaultShip) throws PWCGException
    {
        double angle = MathUtils.adjustAngle(amphibiousAssaultShip.getOrientation().getyOri(), 180);
        Coordinate startPosition = MathUtils.calcNextCoord(mission.getCampaignMap(), amphibiousAssaultShip.getDestination(), angle, amphibiousAssault.getLandingCraftBackOff());
        return startPosition;
    }
 }
