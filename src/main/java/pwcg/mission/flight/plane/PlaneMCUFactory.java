package pwcg.mission.flight.plane;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.constants.Callsign;
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
        
        Equipment equipmentForSquadron = campaign.getEquipmentManager().getEquipmentForSquadron(squadron.getSquadronId());
        FlightPlaneTypeBuilder planeTypeBuilder = new FlightPlaneTypeBuilder(equipmentForSquadron, numPlanes);
        List<EquippedPlane> planesTypesForFlight =planeTypeBuilder.getPlaneListForFlight();
        
        createPlanes(planesTypesForFlight, crewsForFlight);
        
        return planesForFlight;
    }

    private void createPlanes(List<EquippedPlane> planesTypesForFlight, List<SquadronMember> crewsForFlight) throws PWCGException
    {        
        for (int index = 0; index < planesTypesForFlight.size(); ++index)
        {
            EquippedPlane equippedPlane = planesTypesForFlight.get(index);
            SquadronMember pilot = crewsForFlight.get(index);            
            PlaneMCU plane = createPlaneMcuByPlaneType(equippedPlane, squadron.getCountry(), pilot);

            plane.setIndex(IndexGenerator.getInstance().getNextIndex());
            planesForFlight.add(plane);            
        }
        
        initializePlaneParameters();
    }
    
    public PlaneMCU createPlaneMcuByPlaneType (EquippedPlane equippedPlane, ICountry country, SquadronMember pilot)
    {
        PlaneMCU plane = new PlaneMCU(equippedPlane, country, pilot);
        return plane;
    }

	private void initializePlaneParameters() throws PWCGException
	{
		int numInFormation = 1;
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

	private void setPlaneCallsign(int numInFormation, PlaneMCU plane)
	{
		Callsign callsign = squadron.determineCurrentCallsign(campaign.getDate());

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
