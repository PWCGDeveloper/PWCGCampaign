package pwcg.campaign.group.airfield.staticobject;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IAirfieldObjectSelector;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IHotSpotTranslator;
import pwcg.campaign.api.IStaticPlane;
import pwcg.campaign.factory.AirfieldObjectSelectorFactory;
import pwcg.campaign.factory.HotSpotTranslatorFactory;
import pwcg.campaign.group.airfield.hotspot.HotSpot;
import pwcg.campaign.group.airfield.hotspot.HotSpotType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.builder.AAAUnitBuilder;
import pwcg.mission.ground.builder.SearchLightBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.vehicle.IVehicle;

public class AirfieldObjectPlacer
{
	private AirfieldObjects airfieldObjects;
    private Mission mission;
    private Campaign campaign;
    private IAirfield airfield;
    
    public AirfieldObjectPlacer(Mission mission, IAirfield airfield) throws PWCGException
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
        this.airfield = airfield;
        this.airfieldObjects = new AirfieldObjects(airfield.getCountry(campaign.getDate()).getSide());
    }

    public AirfieldObjects createAirfieldObjectsWithEmptySpace() throws PWCGException 
    {
        createHotSpotObjects();
        airfieldObjects.finish(mission);
        return airfieldObjects;
    }

    private void createHotSpotObjects() throws PWCGException
    {
        IHotSpotTranslator hotSpotTranslator = HotSpotTranslatorFactory.createHotSpotTranslatorFactory(mission);
        
        List<HotSpot> hotSpots = hotSpotTranslator.getHotSpots(airfield, campaign.getDate());
        
        for (HotSpot hotSpot : hotSpots)
        {       
            if (hotSpot.getHotSpotType() == HotSpotType.HOTSPOT_SEARCHLIGHT)
            {
                addSearchlight(hotSpot);
            }
            else if (hotSpot.getHotSpotType() == HotSpotType.HOTSPOT_AAA)
            {
                addAAA(hotSpot);
            }
            else if (hotSpot.getHotSpotType() == HotSpotType.HOTSPOT_PLANE)
            {
                addStaticPlane(hotSpot);
            }
            else 
            {
                addAirfieldObject(hotSpot);
            }
        }
    }

    private void addSearchlight(HotSpot hotSpot) throws PWCGException
    {
        ICountry airfieldCountry = airfield.createCountry(campaign.getDate());
        if (!airfieldCountry.isNeutral())
        {
            SearchLightBuilder groundUnitFactory =  new SearchLightBuilder(campaign);
            IGroundUnitCollection searchLightGroup = groundUnitFactory.createOneSearchLight(airfieldCountry, hotSpot.getPosition());
            airfieldObjects.addVehiclesForAirfield(searchLightGroup);
        }
    }

    private void addAAA(HotSpot hotSpot) throws PWCGException
    {
        if (!airfield.createCountry(campaign.getDate()).isNeutral())
        {
            AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(campaign, airfield.getCountry(campaign.getDate()), hotSpot.getPosition());
            IGroundUnitCollection aaaMg;

            int roll = RandomNumberGenerator.getRandom(100);
            if (roll < 80)
            {
                aaaMg = groundUnitFactory.createAAAArtilleryBattery(GroundUnitSize.GROUND_UNIT_SIZE_TINY);
            }
            else
            {
                aaaMg = groundUnitFactory.createAAAMGBattery(GroundUnitSize.GROUND_UNIT_SIZE_TINY);
            }

            if (aaaMg != null)
            {
                airfieldObjects.addVehiclesForAirfield(aaaMg);
            }
        }
    }

    private void addStaticPlane(HotSpot hotSpot) throws PWCGException
    {
        AirfieldStaticPlanePlacer airfieldStaticPlane = new AirfieldStaticPlanePlacer();
        IStaticPlane staticPlane = airfieldStaticPlane.getStaticPlane(airfield, campaign.getDate(), hotSpot.getPosition());
        if (staticPlane != null)
        {
        	airfieldObjects.addStaticPlane(staticPlane);
        }
    }

    private void addAirfieldObject(HotSpot hotSpot) throws PWCGException 
    {
        IAirfieldObjectSelector  airfieldObjectSelector = AirfieldObjectSelectorFactory.createAirfieldObjectSelector(campaign.getDate());
        IVehicle airfieldObject  = airfieldObjectSelector.createAirfieldObject(hotSpot, airfield);
        airfieldObjects.addAirfieldObject(airfieldObject);
    }
}
