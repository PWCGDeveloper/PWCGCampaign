package pwcg.campaign.group.airfield.staticobject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IAirfieldObjectSelector;
import pwcg.campaign.api.IHotSpotTranslator;
import pwcg.campaign.api.IStaticPlane;
import pwcg.campaign.factory.AirfieldObjectSelectorFactory;
import pwcg.campaign.factory.HotSpotTranslatorFactory;
import pwcg.campaign.group.EmptySpaceFinder;
import pwcg.campaign.group.airfield.hotspot.HotSpot;
import pwcg.campaign.group.airfield.hotspot.HotSpotType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.factory.AAAUnitFactory;
import pwcg.mission.ground.unittypes.GroundUnitSpawning;
import pwcg.mission.ground.vehicle.IVehicle;

public class AirfieldObjectPlacer
{
	private AirfieldObjects airfieldObjects = new AirfieldObjects();
    private Campaign campaign;
    private IAirfield airfield;
    
    public AirfieldObjectPlacer(Campaign campaign, IAirfield airfield)
    {
        this.campaign = campaign;
        this.airfield = airfield;
    }

    public AirfieldObjects createAirfieldObjectsDefinedHotSpotsOnly() throws PWCGException 
    {        
        createAirfieldObjectsDefinedHotSpots();        
        return airfieldObjects;
    }

    public AirfieldObjects createAirfieldObjectsWithEmptySpace() throws PWCGException 
    {        
        createAirfieldObjectsDefinedHotSpots();        
        createAirfieldObjectsInEmptySpace();        
        return airfieldObjects;
    }

    private void createAirfieldObjectsDefinedHotSpots() throws PWCGException 
    {        
        IHotSpotTranslator hotSpotTranslator = HotSpotTranslatorFactory.createHotSpotTranslatorFactory();
        
        List<HotSpot> hotSpots = hotSpotTranslator.getHotSpots(airfield, campaign.getDate());
        
        for (HotSpot hotSpot : hotSpots)
        {       
            if (hotSpot.getHotSpotType() == HotSpotType.HOTSPOT_PLANE)
            {
                addStaticPlane(hotSpot);
            }
            else if (hotSpot.getHotSpotType() == HotSpotType.HOTSPOT_AAA)
            {
                addAAA(hotSpot);
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

    private void addAAA(HotSpot hotSpot) throws PWCGException
    {
        if (!airfield.createCountry(campaign.getDate()).isNeutral())
        {
            AAAUnitFactory groundUnitFactory = new AAAUnitFactory(campaign, airfield.getCountry(campaign.getDate()), hotSpot.getPosition());
            GroundUnitSpawning aaaMg = groundUnitFactory.createAAAMGBattery(1, 1);
            if (aaaMg != null)
            {
            	airfieldObjects.getAaaForAirfield().add(aaaMg);
            }
        }
    }

    private void addStaticPlane(HotSpot hotSpot) throws PWCGException
    {
        AirfieldStaticPlanePlacer airfieldStaticPlane = new AirfieldStaticPlanePlacer();
        IStaticPlane staticPlane = airfieldStaticPlane.getStaticPlane(airfield, campaign.getDate(), hotSpot.getPosition());
        if (staticPlane != null)
        {
        	airfieldObjects.getStaticPlanes().add(staticPlane);
        }
    }

    private void addAirfieldObject(HotSpot hotSpot) throws PWCGException 
    {
        IAirfieldObjectSelector  airfieldObjectSelector = AirfieldObjectSelectorFactory.createAirfieldObjectSelector(campaign.getDate());
        IVehicle airfieldObject  = airfieldObjectSelector.createAirfieldObject(hotSpot, airfield);
        airfieldObjects.addAirfieldObject(airfieldObject);
    }
}
