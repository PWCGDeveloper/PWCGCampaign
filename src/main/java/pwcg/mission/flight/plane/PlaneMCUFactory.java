package pwcg.mission.flight.plane;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.crew.FlightCrewBuilder;

public class PlaneMCUFactory
{
    private Campaign campaign;
    private Squadron squadron;
    private Flight flight;
	
    List<PlaneMCU> planesForFlight = new ArrayList<>();

    public PlaneMCUFactory(Campaign campaign, Squadron squadron, Flight flight)
    {
        this.campaign = campaign;
        this.squadron = squadron;
        this.flight = flight;
    }

    public List<PlaneMCU> createPlanesForFlight(int numPlanes) throws PWCGException
    {
        FlightCrewBuilder flightCrewBuilder = new FlightCrewBuilder(campaign, squadron);
        List<SquadronMember> crewsForFlight = flightCrewBuilder.createCrewAssignmentsForFlight(numPlanes);
        
        FlightPlaneTypeBuilder planeTypeBuilder = new FlightPlaneTypeBuilder(campaign, squadron, numPlanes);
        List<String> planesTypesForFlight =planeTypeBuilder.getPlaneListForFlight();
        
        createPlanes(planesTypesForFlight, crewsForFlight);
        
        return planesForFlight;
    }
    
    public PlaneMCU createPlaneByPlaneType (PlaneType planeType, ICountry country, SquadronMember pilot)
    {
        PlaneMCU plane = new PlaneMCU(planeType, country, pilot);
        return plane;
    }

    private void createPlanes(List<String> planesTypesForFlight, List<SquadronMember> crewsForFlight) throws PWCGException
    {        
        for (int numInFormation = 0; numInFormation < planesTypesForFlight.size(); ++numInFormation)
        {
            String planeTypeDesc = planesTypesForFlight.get(numInFormation);
            SquadronMember pilot = crewsForFlight.get(numInFormation);
            
            PlaneType planeType = PWCGContextManager.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(planeTypeDesc);
            PlaneMCU plane = createPlaneByPlaneType(planeType, squadron.getCountry(), pilot);

            plane.setIndex(IndexGenerator.getInstance().getNextIndex());
            planesForFlight.add(plane);            
        }
        
        initializePlaneParameters();
    }

	private void initializePlaneParameters() throws PWCGException
	{
		int numInFormation = 0;
        for (PlaneMCU plane : planesForFlight)
        {
            ++numInFormation;
            setPlaceInFormation(numInFormation, plane);
            setPlaneDescription(plane);
            setAiSkillLevelForPlane(plane);
        }
	}

	private void setPlaceInFormation(int numInFormation, PlaneMCU aiPlane)
	{
		if (flight.isVirtual())
		{
		    aiPlane.setNumberInFormation(0);
		}
		else
		{
		    aiPlane.setNumberInFormation(numInFormation);
		}
	}

	private void setPlaneDescription(PlaneMCU plane) throws PWCGException
	{
	    plane.setDesc(plane.getPilot().getNameAndRank());
	}

	private void setAiSkillLevelForPlane(PlaneMCU plane) throws PWCGException
	{
	    AiSkillLevel aiLevel = AiSkillLevel.COMMON;
        if (plane.getPilot().isPlayer())
        {
            aiLevel = AiSkillLevel.PLAYER;

        }
        else if (plane.getPilot() instanceof Ace)
        {
            aiLevel = AiSkillLevel.ACE;

        }
        else
        {
    	    aiLevel = assignAiSkillLevel(plane);
        }

		plane.setAiLevel(aiLevel);
	}

    private AiSkillLevel assignAiSkillLevel(PlaneMCU plane) throws PWCGException
    {
        Role primaryRole = squadron.determineSquadronPrimaryRole(campaign.getDate());
        AiSkillLevel aiLevel;
        if (primaryRole == Role.ROLE_FIGHTER || primaryRole == Role.ROLE_HOME_DEFENSE)
        {
            aiLevel = plane.getPilot().getAiSkillLevel();
        }
        else
        {
            aiLevel =  AiSkillLevel.NOVICE;
        }
        return aiLevel;
    }
 }
