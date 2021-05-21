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
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.crew.FlightCrewBuilder;

public class PlaneMCUFactory
{    
    private FlightInformation flightInformation;
	
    public PlaneMCUFactory(FlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    public List<PlaneMcu> createPlanesForFlight(int numPlanes) throws PWCGException
    {
        List<SquadronMember> crewsForFlight = buildFlightCrews(numPlanes);
        if (crewsForFlight.size() < numPlanes)
        {
            numPlanes = crewsForFlight.size();
        }
        List<EquippedPlane> planesTypesForFlight = buildEquipmentForFllght(numPlanes);
        List<PlaneMcu> planesForFlight = createPlanes(planesTypesForFlight, crewsForFlight);
        
        return planesForFlight;
    }
    
    public static PlaneMcu createPlaneMcuByPlaneType (Campaign campaign, EquippedPlane equippedPlane, ICountry country, SquadronMember pilot)
    {
        PlaneMcu plane = new PlaneMcu(campaign, equippedPlane, country, pilot);
        return plane;
    }

	private List<EquippedPlane> buildEquipmentForFllght(int numPlanes) throws PWCGException 
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

	private void initializePlaneParameters(List<PlaneMcu> planesForFlight) throws PWCGException
	{
		int numInFormation = 1;
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
        aiPlane.setNumberInFormation(numInFormation);
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
        if (squadronPrimaryRole.isRoleCategory(RoleCategory.FIGHTER) || !plane.isNovice())
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
