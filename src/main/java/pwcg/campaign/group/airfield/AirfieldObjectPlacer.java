package pwcg.campaign.group.airfield;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.IAirfieldObjectSelector;
import pwcg.campaign.api.IHotSpotTranslator;
import pwcg.campaign.api.IStaticPlane;
import pwcg.campaign.factory.AirfieldObjectSelectorFactory;
import pwcg.campaign.factory.HotSpotTranslatorFactory;
import pwcg.campaign.group.EmptySpaceFinder;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.GroundUnitAAAFactory;
import pwcg.mission.ground.unittypes.GroundUnitSpawning;
import pwcg.mission.ground.vehicle.IVehicle;

public class AirfieldObjectPlacer
{
	private AirfieldObjects airfieldObjects = new AirfieldObjects();
    private IAirfield airfield;
	private Date date = null;
    
    public AirfieldObjectPlacer(IAirfield airfield, Date date)
    {
        this.airfield = airfield;
        this.date = date;
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
        
        List<HotSpot> hotSpots = hotSpotTranslator.getHotSpots(airfield, date);
        
        for (HotSpot hotSpot : hotSpots)
        {       
            if (hotSpot.getHotSpotType() == HotSpotType.HOTSPOT_PLANE)
            {
                addStaticPlane(hotSpot, date);
            }
            else if (hotSpot.getHotSpotType() == HotSpotType.HOTSPOT_TOWER)
            {
                addWaterTower(hotSpot);
            }
            else if (hotSpot.getHotSpotType() == HotSpotType.HOTSPOT_AAA)
            {
                addAAA(hotSpot, date);
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
                addStaticPlane(hotSpot, date);
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

    private void addAAA(HotSpot hotSpot, Date date) throws PWCGException
    {
        GroundUnitAAAFactory groundUnitFactory =  new GroundUnitAAAFactory(airfield.getCountry(date), hotSpot.getPosition());
        if (!airfield.createCountry(date).isNeutral())
        {
            GroundUnitSpawning aaa = groundUnitFactory.createAAAMGBattery(1);
            if (aaa != null)
            {
            	airfieldObjects.getAaaForAirfield().add(aaa);
            }
        }
    }

    private void addStaticPlane(HotSpot hotSpot, Date date) throws PWCGException
    {
        AirfieldStaticPlanePlacer airfieldStaticPlane = new AirfieldStaticPlanePlacer();
        IStaticPlane staticPlane = airfieldStaticPlane.getStaticPlane(airfield, date, hotSpot.getPosition());
        if (staticPlane != null)
        {
        	airfieldObjects.getStaticPlanes().add(staticPlane);
        }
    }

    private void addWaterTower(HotSpot hotSpot) throws PWCGException 
    {
        IAirfieldObjectSelector  airfieldObjectSelector = AirfieldObjectSelectorFactory.createAirfieldObjectSelector(date);
        IVehicle airfieldObject  = airfieldObjectSelector.createWaterTower(hotSpot, airfield);
        airfieldObjects.addAirfieldObject(airfieldObject);
    }

    private void addAirfieldObject(HotSpot hotSpot) throws PWCGException 
    {
        IAirfieldObjectSelector  airfieldObjectSelector = AirfieldObjectSelectorFactory.createAirfieldObjectSelector(date);
        IVehicle airfieldObject  = airfieldObjectSelector.createAirfieldObject(hotSpot, airfield);
        airfieldObjects.addAirfieldObject(airfieldObject);
    }
}
