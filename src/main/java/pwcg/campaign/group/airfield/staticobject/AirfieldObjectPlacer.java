package pwcg.campaign.group.airfield.staticobject;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.factory.AirfieldObjectSelectorFactory;
import pwcg.campaign.factory.HotSpotTranslatorFactory;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.group.airfield.hotspot.AirfieldHotSpotTranslator;
import pwcg.campaign.group.airfield.hotspot.HotSpot;
import pwcg.campaign.group.airfield.hotspot.HotSpotType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.builder.AAAUnitBuilder;
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

    public AirfieldObjects createAirfieldObjects() throws PWCGException 
    {
        createApproachAA();
        createHotSpotObjects();
        return airfieldObjects;
    }

    private void createHotSpotObjects() throws PWCGException
    {
        AirfieldHotSpotTranslator hotSpotTranslator = HotSpotTranslatorFactory.createHotSpotTranslatorFactory(mission, airfield);
        
        List<HotSpot> hotSpots = hotSpotTranslator.getHotSpots();
        for (HotSpot hotSpot : hotSpots)
        {       
            if (hotSpot.getHotSpotType() == HotSpotType.HOTSPOT_AAA)
            {
                addAAA(hotSpot);
            }
            else 
            {
                addAirfieldObject(hotSpot);
            }
        }
    }

    private void addAAA(HotSpot hotSpot) throws PWCGException
    {
        if (!airfield.determineCountryOnDate(campaign.getDate()).isNeutral())
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
                mission.getGroundUnitBuilder().addMissionAAA(aaaUnit);
            }
        }
    }

    private void addAirfieldObject(HotSpot hotSpot) throws PWCGException 
    {
        AirfieldObjectSelector  airfieldObjectSelector = AirfieldObjectSelectorFactory.createAirfieldObjectSelector(campaign.getDate());
        IVehicle airfieldObject  = airfieldObjectSelector.createAirfieldObject(hotSpot, airfield, airfieldCountry);
        airfieldObjects.addAirfieldObject(airfieldObject);
    }
    

    private void createApproachAA() throws PWCGException
    {
        AirfieldApproachAABuilder airfieldApproachAABuilder = new AirfieldApproachAABuilder(mission, airfield, airfieldCountry);
        List<GroundUnitCollection> airfieldApproachAA = airfieldApproachAABuilder.addAirfieldApproachAA();
        for (GroundUnitCollection aaaUnit : airfieldApproachAA)
        {
            mission.getGroundUnitBuilder().addMissionAAA(aaaUnit);
        }
    }
}
