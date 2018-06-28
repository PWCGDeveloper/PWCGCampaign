package pwcg.mission.flight.plane;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class FlightPlaneTypeBuilder
{
    private Campaign campaign;
    private Squadron squadron;
    private int numPlanes;
    
    public FlightPlaneTypeBuilder(Campaign campaign, Squadron squadron, int numPlanes)
    {
        this.campaign = campaign;
        this.squadron = squadron;
        this.numPlanes = numPlanes;
    }
    
    public List<String> getPlaneListForFlight() throws PWCGException 
    {        
        if (useMixed())
        {
            return createMixedAircraftSet();
        }
        else
        {
            return createHomogeneousAircraftSet();
        }
    }

    private List<String> createMixedAircraftSet() throws PWCGException
    {        
        List<PlaneType> aircraftTypes = squadron.determineCurrentAircraftList(campaign.getDate());
        if (aircraftTypes.size() <= 0)
        {
            throw new PWCGException ("No planes for player squadron on this date");
        }
     
        TreeMap<String, String> planeTypesForFlight = getPlaneTypesForMixedFlight(numPlanes, aircraftTypes);        
        List<String> aircraftTypeForMission = new ArrayList<String>(planeTypesForFlight.descendingMap().values());

        return aircraftTypeForMission;
    }

    private TreeMap<String, String> getPlaneTypesForMixedFlight(int numPlanes, List<PlaneType> aircraftTypes) {
        TreeMap <String, String> planeTypesForFlight = new TreeMap <String, String>();
        for (int planeNumber = 0; planeNumber < numPlanes; ++ planeNumber)
        {
            int selectedPlaneIndex = RandomNumberGenerator.getRandom(aircraftTypes.size());
            PlaneType planeType = aircraftTypes.get(selectedPlaneIndex);
            String key = "" + planeType.getGoodness() + new Integer(selectedPlaneIndex).toString() + new Integer(planeNumber).toString();
            
            planeTypesForFlight.put(key, aircraftTypes.get(selectedPlaneIndex).getType());
        }
        return planeTypesForFlight;
    }

    private List<String> createHomogeneousAircraftSet() throws PWCGException
    {
        List<PlaneType> aircraftTypes = squadron.determineCurrentAircraftList(campaign.getDate());
        if (aircraftTypes.size() <= 0)
        {
            throw new PWCGException ("No planes for player squadron on this date");
        }
               
        String bestPlaneTypeDesc =  aircraftTypes.get(aircraftTypes.size() - 1).getType();
        
        List<String> aircraftTypeForMission = new ArrayList<String>();
        for (int i = 0; i < numPlanes; ++i)
        {
            aircraftTypeForMission.add(bestPlaneTypeDesc);
        }

        return aircraftTypeForMission;
    }

    private boolean useMixed() throws PWCGException
    {
        if (PWCGContextManager.isRoF())
        {
            
            if (squadron.getCountry().isCountry(Country.BRITAIN))
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            return true;
        }
    }

}
