package pwcg.mission;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.factory.PWCGFlightFactory;
import pwcg.campaign.group.AirfieldManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.factory.FlightFactory;

public class MissionGenerator
{
    Campaign campaign = null;
    
    /**
     * 
     */
    public MissionGenerator()
    {
        campaign = PWCGContextManager.getInstance().getCampaign();
    }
    
    /**
     * @return
     * @throws PWCGException 
     */
    public Mission makeMission() throws PWCGException 
    {
        // Chose a flight type
        FlightTypes flightType = chooseFlightType();
        
        Mission mission = makeSingleMission(flightType);
        
        return mission;
    }
    
    
    /**
     * @return
     * @throws PWCGException 
     */
    public Mission makeLoneWolfMission() throws PWCGException 
    {
        // Chose a flight type
        FlightTypes flightType = FlightTypes.LONE_WOLF;
        
        Mission mission = makeSingleMission(flightType);
        
        return mission;
    }


    /**
     * @param flightType
     * @return
     * @throws PWCGException 
     * @
     */
    private Mission makeSingleMission(FlightTypes flightType) throws PWCGException 
    {
        // Airfields can be in more than one map
        List<FrontMapIdentifier> mapsForAirfield = AirfieldManager.getMapIdForAirfield(campaign.getAirfieldName());
        if (mapsForAirfield.size() < 1)
        {
            throw new PWCGMissionGenerationException ("Airfield " + campaign.getAirfieldName() + " is not in any airfield map");
        }

        // Pick a map to generate the mission on
        int index = RandomNumberGenerator.getRandom(mapsForAirfield.size());
        FrontMapIdentifier mapId = mapsForAirfield.get(index);

        PWCGContextManager.getInstance().changeContext(mapId);
        

        // Release memory from the previous mission
        campaign.setCurrentMission(null);        

        // The one and only place a new mission is made
        Mission mission = new Mission();

        // Set the mission immediately so it is available through the campaign
        campaign.setCurrentMission(mission);
        
        // Now create the mission
        mission.initialize(campaign);
        
        
        mission.generate(flightType);
        
        campaign.resetFerryMission();

        return mission;
    }

    private FlightTypes chooseFlightType() throws PWCGException 
    {
        FlightTypes flightType = FlightTypes.ANY;
        if (campaign.getSquadronMoveEvent().isNeedsFerryMission())
        {
            flightType = FlightTypes.FERRY;
        }
        else
        {
            FlightFactory flightFactory = PWCGFlightFactory.createFlightFactory(campaign);
            flightType = flightFactory.getFlightType(campaign.determineSquadron(), true);
        }
        
        return flightType;
    }
}
