package pwcg.campaign.group.airfield.staticobject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IAirfieldObjectSelector;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IHotSpotTranslator;
import pwcg.campaign.api.IStaticPlane;
import pwcg.campaign.factory.AirfieldObjectSelectorFactory;
import pwcg.campaign.factory.HotSpotTranslatorFactory;
import pwcg.campaign.group.EmptySpaceFinder;
import pwcg.campaign.group.airfield.hotspot.HotSpot;
import pwcg.campaign.group.airfield.hotspot.HotSpotType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.factory.AAAUnitBuilder;
import pwcg.mission.ground.factory.SpotLightBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.vehicle.IVehicle;

public class AirfieldObjectPlacer
{
	private AirfieldObjects airfieldObjects = new AirfieldObjects();
    private Mission mission;
    private Campaign campaign;
    private IAirfield airfield;
    
    public AirfieldObjectPlacer(Mission mission, IAirfield airfield)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
        this.airfield = airfield;
    }

    public AirfieldObjects createAirfieldObjectsWithEmptySpace() throws PWCGException 
    {        
        createAirfieldObjectsDefinedHotSpots();        
        createAirfieldObjectsInEmptySpace();        
        return airfieldObjects;
    }

    private void createAirfieldObjectsDefinedHotSpots() throws PWCGException 
    {        
        IHotSpotTranslator hotSpotTranslator = HotSpotTranslatorFactory.createHotSpotTranslatorFactory(mission);
        
        List<HotSpot> hotSpots = hotSpotTranslator.getHotSpots(airfield, campaign.getDate());
        
        for (HotSpot hotSpot : hotSpots)
        {       
            if (hotSpot.getHotSpotType() == HotSpotType.HOTSPOT_SPOTLIGHT)
            {
                addSpotlight(hotSpot);
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

    private void createAirfieldObjectsInEmptySpace() throws PWCGException 
    {        
        List<HotSpot> hotSpots = selectHotSpotsFromEmptySpace();
        
        for (HotSpot hotSpot : hotSpots)
        {       
            double hotSpotDiceRoll = RandomNumberGenerator.getRandom(100);

            if (hotSpotDiceRoll < 30)
            {
                addStaticPlane(hotSpot);
            }
            else
            {
                addAirfieldObject(hotSpot);
            }
        }
    }

    private List<HotSpot> selectHotSpotsFromEmptySpace() throws PWCGException
    {
        HashSet<Integer> selectedHotSpotIndeces = new HashSet<>();

        EmptySpaceFinder emptySpaceFinder = new EmptySpaceFinder();
        List<HotSpot> hotSpots = emptySpaceFinder.findEmptySpaces(airfield.getPosition(), 500);
        List<HotSpot> selectedHotSpots = new ArrayList<>();
        
        int failCount = 0;
        for (int i = 0; i < 50; ++i)
        {
            while (failCount < 3)
            {
                Integer selectedHotSpotIndex = RandomNumberGenerator.getRandom(hotSpots.size());  
                if (selectedHotSpotIndeces.contains(selectedHotSpotIndex))
                {
                    ++failCount;
                }
                else
                {
                    selectedHotSpotIndeces.add(selectedHotSpotIndex);
                    selectedHotSpots.add(hotSpots.get(selectedHotSpotIndex));
                    failCount = 0;
                    break;
                }
            }
        }
        return selectedHotSpots;
    }

    private void addSpotlight(HotSpot hotSpot) throws PWCGException
    {
        ICountry airfieldCountry = airfield.createCountry(campaign.getDate());
        if (!airfieldCountry.isNeutral())
        {
            SpotLightBuilder groundUnitFactory =  new SpotLightBuilder(campaign);
            IGroundUnitCollection spotLightGroup = groundUnitFactory.createOneSpotLight(airfieldCountry, hotSpot.getPosition());
            airfieldObjects.addSpotlightsForAirfield(spotLightGroup);
        }
    }

    private void addAAA(HotSpot hotSpot) throws PWCGException
    {
        if (!airfield.createCountry(campaign.getDate()).isNeutral())
        {
            AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(campaign, airfield.getCountry(campaign.getDate()), hotSpot.getPosition());
            IGroundUnitCollection aaaMg = groundUnitFactory.createAAAMGBattery(GroundUnitSize.GROUND_UNIT_SIZE_TINY);
            if (aaaMg != null)
            {
            	airfieldObjects.addAaaForAirfield(aaaMg);
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
