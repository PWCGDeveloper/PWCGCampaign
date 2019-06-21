package pwcg.mission.flight.plane;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.constants.Callsign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Unit;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.crew.FlightCrewBuilder;

public class PlaneMCUFactory
{
    private FlightInformation flightInformation;
	
    public PlaneMCUFactory(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public List<PlaneMCU> createPlanesForFlight(int numPlanes) throws PWCGException
    {
        List<SquadronMember> crewsForFlight = buildFlightCrews(numPlanes);
        List<EquippedPlane> planesTypesForFlight = buildEquipmentForFLlght(numPlanes);
        List<PlaneMCU> planesForFlight = createPlanes(planesTypesForFlight, crewsForFlight);
        
        return planesForFlight;
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

    private List<PlaneMCU> createPlanes(List<EquippedPlane> planesTypesForFlight, List<SquadronMember> crewsForFlight) throws PWCGException
    {        
        List<PlaneMCU> planesForFlight = new ArrayList<>();
        for (int index = 0; index < planesTypesForFlight.size(); ++index)
        {
        	try
        	{
	            EquippedPlane equippedPlane = planesTypesForFlight.get(index);
	            SquadronMember pilot = crewsForFlight.get(index);            
	            PlaneMCU plane = createPlaneMcuByPlaneType(equippedPlane, flightInformation.getSquadron().getCountry(), pilot);
	
	            plane.setIndex(IndexGenerator.getInstance().getNextIndex());
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
    
    public PlaneMCU createPlaneMcuByPlaneType (EquippedPlane equippedPlane, ICountry country, SquadronMember pilot)
    {
        PlaneMCU plane = new PlaneMCU(flightInformation.getCampaign(), equippedPlane, country, pilot);
        return plane;
    }

	private void initializePlaneParameters(List<PlaneMCU> planesForFlight) throws PWCGException
	{
		int numInFormation = Unit.NUM_IN_FORMATION_START;
        for (PlaneMCU plane : planesForFlight)
        {
            setPlaceInFormation(numInFormation, plane);
            setPlaneDescription(plane);
            setPlaneCallsign(numInFormation, plane);
            setAiSkillLevelForPlane(plane);
            ++numInFormation;
        }
	}

	private void setPlaceInFormation(int numInFormation, PlaneMCU aiPlane)
	{
		if (flightInformation.getParticipatingPlayers().size() == 0)
		{
		    aiPlane.setNumberInFormation(Unit.NUM_IN_FORMATION_START);
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

	private void setPlaneCallsign(int numInFormation, PlaneMCU plane)
	{
		Callsign callsign = flightInformation.getSquadron().determineCurrentCallsign(flightInformation.getCampaign().getDate());

		plane.setCallsign(callsign);
		plane.setCallnum(numInFormation);
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
        Role primaryRole = flightInformation.getSquadron().determineSquadronPrimaryRole(flightInformation.getCampaign().getDate());
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
