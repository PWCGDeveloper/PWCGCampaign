package pwcg.campaign.group.airfield.staticobject;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IStaticPlane;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.factory.AirfieldObjectSelectorFactory;
import pwcg.campaign.factory.HotSpotTranslatorFactory;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.group.airfield.RunwayBlockageDetector;
import pwcg.campaign.group.airfield.hotspot.AirfieldHotSpotTranslator;
import pwcg.campaign.group.airfield.hotspot.HotSpot;
import pwcg.campaign.group.airfield.hotspot.HotSpotType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.builder.AAAUnitBuilder;
import pwcg.mission.ground.builder.SearchLightBuilder;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class AirfieldObjectPlacer
{
	private AirfieldObjects airfieldObjects;
    private Mission mission;
    private Campaign campaign;
    private Airfield airfield;
    private ICountry airfieldCountry;
    
    public AirfieldObjectPlacer(Mission mission, Airfield airfield, ICountry airfieldCountry) throws PWCGException
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
        this.airfield = airfield;
        this.airfieldCountry = airfieldCountry;
        this.airfieldObjects = new AirfieldObjects();
    }

    public AirfieldObjects createAirfieldObjects(FrontMapIdentifier mapIdentifier) throws PWCGException 
    {
        createApproachAA();
        createHotSpotObjects(mapIdentifier);
        return airfieldObjects;
    }

    private void createHotSpotObjects(FrontMapIdentifier mapIdentifier) throws PWCGException
    {
        AirfieldHotSpotTranslator hotSpotTranslator = HotSpotTranslatorFactory.createHotSpotTranslatorFactory(mission, airfield);
        
        List<HotSpot> hotSpots = hotSpotTranslator.getHotSpots();
        for (HotSpot hotSpot : hotSpots)
        {       
            if (hotSpot.getHotSpotType() == HotSpotType.HOTSPOT_SEARCHLIGHT)
            {
                addSearchlight(hotSpot);
            }
            else if (hotSpot.getHotSpotType() == HotSpotType.HOTSPOT_AAA)
            {
                addAAA(mapIdentifier, hotSpot);
            }
            else if (hotSpot.getHotSpotType() == HotSpotType.HOTSPOT_PLANE)
            {
                addStaticPlane(hotSpot);
            }
            else 
            {
                addAirfieldObject(mapIdentifier, hotSpot);
            }
        }
    }

    private void addSearchlight(HotSpot hotSpot) throws PWCGException
    {
        if (!airfieldCountry.isNeutral())
        {
            SearchLightBuilder groundUnitFactory =  new SearchLightBuilder(campaign);
            GroundUnitCollection searchLightGroup = groundUnitFactory.createOneSearchLight(airfieldCountry, hotSpot.getPosition());
            if (!RunwayBlockageDetector.isRunwayBlocked(mission.getCampaignMap(), airfield, searchLightGroup.getPosition()))
            {
                mission.getGroundUnitBuilder().addAirfieldVehicle(searchLightGroup);
            }
        }
    }

    private void addAAA(FrontMapIdentifier mapIdentifier, HotSpot hotSpot) throws PWCGException
    {
        if (!airfield.determineCountryOnDate(mapIdentifier, campaign.getDate()).isNeutral())
        {            
            TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_ARTILLERY, hotSpot.getPosition(), airfieldCountry, "Airfield AAA");
            AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(campaign, targetDefinition);

            GroundUnitCollection aaaUnit;

            int roll = RandomNumberGenerator.getRandom(100);
            if (roll < 80)
            {
                aaaUnit = groundUnitFactory.createAAAArtilleryBattery(GroundUnitSize.GROUND_UNIT_SIZE_TINY);
            }
            else
            {
                aaaUnit = groundUnitFactory.createAAAMGBattery(GroundUnitSize.GROUND_UNIT_SIZE_TINY);
            }

            if (aaaUnit != null)
            {
                if (!RunwayBlockageDetector.isRunwayBlocked(mission.getCampaignMap(), airfield, aaaUnit.getPosition()))
                {
                    mission.getGroundUnitBuilder().addMissionAAA(aaaUnit);
                }
            }
        }
    }

    private void addStaticPlane(HotSpot hotSpot) throws PWCGException
    {
        AirfieldStaticPlanePlacer airfieldStaticPlane = new AirfieldStaticPlanePlacer();
        IStaticPlane staticPlane = airfieldStaticPlane.getStaticPlane(campaign, airfield, airfieldCountry,  hotSpot.getPosition());
        if (staticPlane != null)
        {
            if (!RunwayBlockageDetector.isRunwayBlocked(mission.getCampaignMap(), airfield, staticPlane.getPosition()))
            {
                airfieldObjects.addStaticPlane(staticPlane);
            }
        }
    }

    private void addAirfieldObject(FrontMapIdentifier mapIdentifier, HotSpot hotSpot) throws PWCGException 
    {
        AirfieldObjectSelector  airfieldObjectSelector = AirfieldObjectSelectorFactory.createAirfieldObjectSelector(campaign.getDate());
        IVehicle airfieldObject  = airfieldObjectSelector.createAirfieldObject(mapIdentifier, hotSpot, airfield, airfieldCountry);
        if (!RunwayBlockageDetector.isRunwayBlocked(mission.getCampaignMap(), airfield, airfieldObject.getPosition()))
        {
            airfieldObjects.addAirfieldObject(airfieldObject);
        }
    }
    

    private void createApproachAA() throws PWCGException
    {
        AirfieldApproachAABuilder airfieldApproachAABuilder = new AirfieldApproachAABuilder(mission, airfield, airfieldCountry);
        List<GroundUnitCollection> airfieldApproachAA = airfieldApproachAABuilder.addAirfieldApproachAA(mission.getCampaignMap());
        for (GroundUnitCollection aaaUnit : airfieldApproachAA)
        {
            if (!RunwayBlockageDetector.isRunwayBlocked(mission.getCampaignMap(), airfield, aaaUnit.getPosition()))
            {
                mission.getGroundUnitBuilder().addMissionAAA(aaaUnit);
            }
        }
    }
}
