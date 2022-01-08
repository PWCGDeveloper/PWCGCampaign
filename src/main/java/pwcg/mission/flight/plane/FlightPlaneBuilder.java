package pwcg.mission.flight.plane;

import java.util.List;

import pwcg.campaign.context.Country;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightInformation;

public class FlightPlaneBuilder 
{
	private FlightInformation flightInformation;
	
	public FlightPlaneBuilder(FlightInformation flightInformation)
	{
		this.flightInformation = flightInformation;
	}
	
    public static void buildPlanes(FlightInformation flightInformation) throws PWCGException
    {
        FlightPlaneBuilder flightPlaneBuilder = new FlightPlaneBuilder(flightInformation);
        List<PlaneMcu> planes = flightPlaneBuilder.createPlanesForFlight();
        flightInformation.setPlanes(planes);
    }
	
    private List<PlaneMcu> createPlanesForFlight() throws PWCGException 
    {
    	int numPlanesInMission = calcNumPlanesInFlight();
    	List<PlaneMcu> planes = createPlanes(numPlanesInMission);
		return planes;
    }
    
    private int calcNumPlanesInFlight() throws PWCGException
    {
        int planesInMission = 4;
        if ((flightInformation.getCountry().getCountry() == Country.RUSSIA) && 
            (flightInformation.getCampaign().getDate().before(DateUtils.getDateYYYYMMDD("19431001"))))
        {
            planesInMission = 3;
        }
        else if(RandomNumberGenerator.getRandom(100) <40)
        {
            planesInMission = 2;
        }
        
        return planesInMission;
    }

	private List<PlaneMcu> createPlanes(int numPlanesInFlight) throws PWCGException 
    {        
        PlaneMcuFactory planeGeneratorPlayer = new PlaneMcuFactory(flightInformation);
        List<PlaneMcu> planes = planeGeneratorPlayer.createPlanesForFlight(numPlanesInFlight);
		return planes;
    }
}
