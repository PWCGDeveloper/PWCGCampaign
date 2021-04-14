package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.battle.AmphibiousAssault;
import pwcg.campaign.battle.AmphibiousAssaultShip;
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
import pwcg.mission.ground.unittypes.transport.LandingCraftUnit;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class AmphibiousAssaultShipBuilder
{
    private Mission mission;
    private AmphibiousAssault amphibiousAssault;
    private GroundUnitCollection ships;
    
    public AmphibiousAssaultShipBuilder(Mission mission, AmphibiousAssault amphibiousAssault)
    {
        this.mission = mission;
        this.amphibiousAssault = amphibiousAssault;
    }

    public GroundUnitCollection generateAmphibiousAssautShips() throws PWCGException
    {
        ships = buildLandingCraft();
        GroundUnitCollection destroyers = buildLandingDestroyers();
        ships.merge(destroyers);
        return ships;
    }

    private GroundUnitCollection buildLandingCraft() throws PWCGException
    {
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION, 
                "Landing Craft", 
                TargetType.TARGET_SHIPPING,
                Coalition.getCoalitions());

        GroundUnitCollection landingCraft = new GroundUnitCollection("Landing Craft", groundUnitCollectionData);
        for (AmphibiousAssaultShip amphibiousAssaultShip : getLandingCraftForAssuult(amphibiousAssault.getShips()))
        {
            if (amphibiousAssaultShip.getShipType().startsWith("land"))
            {
                GroundUnitInformation groundUnitInformation = createGroundUnitInformationForUnit(amphibiousAssaultShip);
                LandingCraftUnit landingCraftUnit = new LandingCraftUnit(groundUnitInformation);
                landingCraftUnit.createGroundUnit();
                landingCraft.addGroundUnit(landingCraftUnit);
            }
        }

        return landingCraft;
    }

    private List<AmphibiousAssaultShip> getLandingCraftForAssuult(List<AmphibiousAssaultShip> ships) throws PWCGException
    {
        GroundUnitSize groundUnitSizeConfig = GroundUnitSize.calcNumUnitsByConfig(mission.getCampaign());
        int numLandingCraft = 3;
        if (groundUnitSizeConfig == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            numLandingCraft = 6;
        }
        else if (groundUnitSizeConfig == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            numLandingCraft = 9;
        }
        
        Collections.shuffle(ships);
        List<AmphibiousAssaultShip> shipsForMission = new ArrayList<>();
        for (int i = 0; i < numLandingCraft; ++ i)
        {
            if (i < (ships.size()-1))
            {
                shipsForMission.add(ships.get(i));
            }
        }
        return shipsForMission;
    }

    private GroundUnitCollection buildLandingDestroyers() throws PWCGException
    {
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION, 
                "Destroyer", 
                TargetType.TARGET_SHIPPING,
                Coalition.getCoalitions());

        GroundUnitCollection destroyers = new GroundUnitCollection("Destroyer", groundUnitCollectionData);
        for (AmphibiousAssaultShip amphibiousAssaultShip : amphibiousAssault.getShips())
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

    private GroundUnitInformation createGroundUnitInformationForUnit(AmphibiousAssaultShip amphibiousAssaultShip) throws PWCGException
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

    private TargetDefinition makeTargetDefinition(AmphibiousAssaultShip amphibiousAssaultShip) throws PWCGException
    {
        ICountry shipCountry = CountryFactory.makeCountryByCountry(amphibiousAssault.getAggressorCountry());
        Coordinate startPosition = makeLandingCraftStartPosition(amphibiousAssaultShip);
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_SHIPPING, startPosition, shipCountry);
        return targetDefinition;
    }

    private Coordinate makeLandingCraftStartPosition(AmphibiousAssaultShip amphibiousAssaultShip) throws PWCGException
    {
        double angle = MathUtils.adjustAngle(amphibiousAssaultShip.getOrientation().getyOri(), 180);
        Coordinate startPosition = MathUtils.calcNextCoord(amphibiousAssaultShip.getDestination(), angle, 700);
        return startPosition;
    }
 }
