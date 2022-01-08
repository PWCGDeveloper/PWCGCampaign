package pwcg.mission.flight.plane;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.flight.FlightInformation;

public class PlaneMcuFactory
{    
    private FlightInformation flightInformation;
	
    public PlaneMcuFactory(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public List<PlaneMcu> createPlanesForFlight(int numPlanes) throws PWCGException
    {
        List<PlaneMcu> planesForFlight = createPlanes(numPlanes);        
        return planesForFlight;
    }

    private List<PlaneMcu> createPlanes(int numPlanes) throws PWCGException
    {        
        List<PlaneMcu> planesForFlight = new ArrayList<>();
        for (int index = 0; index < numPlanes; ++index)
        {
        	try
        	{
	            PlaneMcu plane = createPlaneMcuByPlaneType();
	            if (index > 0)
	            {
	                PlaneMcu leadPlane = planesForFlight.get(0);
	                plane.setTarget(leadPlane.getLinkTrId());
	            }
	            planesForFlight.add(plane);
        	}
        	catch (Exception e)
        	{
        		e.printStackTrace();
        		PWCGLogger.log(LogLevel.ERROR, e.getMessage());
        	}
        }
        
        initializePlaneParameters(planesForFlight);
		return planesForFlight;
    }
    
    private PlaneMcu createPlaneMcuByPlaneType () throws PWCGException
    {
        PlaneMcu plane = new PlaneMcu(flightInformation.getCampaign());
        plane.buildPlane(flightInformation.getPlaneType(), flightInformation.getCountry());
        return plane;
    }

	private void initializePlaneParameters(List<PlaneMcu> planesForFlight) throws PWCGException
	{
		int numInFormation = 1;
        for (PlaneMcu plane : planesForFlight)
        {
            setPlaceInFormation(numInFormation, plane);
            setPlaneDescription(plane);
            setAiSkillLevelForPlane(plane);
            ++numInFormation;
        }
	}

	private void setPlaceInFormation(int numInFormation, PlaneMcu aiPlane)
	{
        aiPlane.setNumberInFormation(numInFormation);
	}

	private void setPlaneDescription(PlaneMcu plane) throws PWCGException
	{
	    plane.setDesc(plane.getDisplayName());
	}

	private void setAiSkillLevelForPlane(PlaneMcu plane) throws PWCGException
	{
	    AiSkillLevel aiLevel = AiSkillLevel.COMMON;
 		plane.setAiLevel(aiLevel);
	}
 }
