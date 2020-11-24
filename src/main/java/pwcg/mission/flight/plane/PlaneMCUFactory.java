package pwcg.mission.flight.plane;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.Role;
import pwcg.campaign.plane.RoleCategory;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.constants.Callsign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.crew.FlightCrewBuilder;

public class PlaneMCUFactory
{
    public static final int NUM_IN_FORMATION_START = 1;
    
    private IFlightInformation flightInformation;
	
    public PlaneMCUFactory(IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public List<PlaneMcu> createPlanesForFlight(int numPlanes) throws PWCGException
    {
        List<SquadronMember> crewsForFlight = buildFlightCrews(numPlanes);
        List<EquippedPlane> planesTypesForFlight = buildEquipmentForFLlght(numPlanes);
        List<PlaneMcu> planesForFlight = createPlanes(planesTypesForFlight, crewsForFlight);
        
        return planesForFlight;
    }
    
    public static PlaneMcu createPlaneMcuByPlaneType (Campaign campaign, EquippedPlane equippedPlane, ICountry country, SquadronMember pilot)
    {
        PlaneMcu plane = new PlaneMcu(campaign, equippedPlane, country, pilot);
        return plane;
    }

	private List<EquippedPlane> buildEquipmentForFLlght(int numPlanes) throws PWCGException 
	{
		Equipment equipmentForSquadron = flightInformation.getCampaign().getEquipmentManager().getEquipmentForSquadron(flightInformation.getSquadron().getSquadronId());
        FlightPlaneTypeBuilder planeTypeBuilder = new FlightPlaneTypeBuilder(equipmentForSquadron, numPlanes);
        List<EquippedPlane> planesTypesForFlight =planeTypeBuilder.getPlaneListForFlight();
		return planesTypesForFlight;
	}

	private List<SquadronMember> buildFlightCrews(int numPlanes) throws PWCGException 
	{
		FlightCrewBuilder flightCrewBuilder = new FlightCrewBuilder(flightInformation);
        List<SquadronMember> crewsForFlight = flightCrewBuilder.createCrewAssignmentsForFlight(numPlanes);
		return crewsForFlight;
	}

    private List<PlaneMcu> createPlanes(List<EquippedPlane> planesTypesForFlight, List<SquadronMember> crewsForFlight) throws PWCGException
    {        
        List<PlaneMcu> planesForFlight = new ArrayList<>();
        for (int index = 0; index < planesTypesForFlight.size(); ++index)
        {
        	try
        	{
	            EquippedPlane equippedPlane = planesTypesForFlight.get(index);
	            SquadronMember pilot = crewsForFlight.get(index);            
	            PlaneMcu plane = createPlaneMcuByPlaneType(flightInformation.getCampaign(), equippedPlane, flightInformation.getSquadron().getCountry(), pilot);	
	            planesForFlight.add(plane);
        	}
        	catch (Exception e)
        	{
        		e.printStackTrace();
        		System.out.println(e.getMessage());
        	}
        }
        
        initializePlaneParameters(planesForFlight);
		return planesForFlight;
    }

	private void initializePlaneParameters(List<PlaneMcu> planesForFlight) throws PWCGException
	{
		int numInFormation = NUM_IN_FORMATION_START;
        for (PlaneMcu plane : planesForFlight)
        {
            setPlaceInFormation(numInFormation, plane);
            setPlaneDescription(plane);
            setPlaneCallsign(numInFormation, plane);
            setAiSkillLevelForPlane(plane);
            ++numInFormation;
        }
	}

	private void setPlaceInFormation(int numInFormation, PlaneMcu aiPlane)
	{
		if (flightInformation.isVirtual())
		{
		    aiPlane.setNumberInFormation(NUM_IN_FORMATION_START);
		}
		else
		{
		    aiPlane.setNumberInFormation(numInFormation);
		}
	}

	private void setPlaneDescription(PlaneMcu plane) throws PWCGException
	{
	    plane.setDesc(plane.getPilot().getNameAndRank());
	}

	private void setPlaneCallsign(int numInFormation, PlaneMcu plane)
	{
		Callsign callsign = flightInformation.getSquadron().determineCurrentCallsign(flightInformation.getCampaign().getDate());

		plane.setCallsign(callsign);
		plane.setCallnum(numInFormation);
	}

	private void setAiSkillLevelForPlane(PlaneMcu plane) throws PWCGException
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

    private AiSkillLevel assignAiSkillLevel(PlaneMcu plane) throws PWCGException
    {
        Role squadronPrimaryRole = flightInformation.getSquadron().determineSquadronPrimaryRole(flightInformation.getCampaign().getDate());
        AiSkillLevel aiLevel;
        if (squadronPrimaryRole.isRoleCategory(RoleCategory.FIGHTER))
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
