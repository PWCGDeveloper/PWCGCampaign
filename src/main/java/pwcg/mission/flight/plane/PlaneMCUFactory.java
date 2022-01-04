package pwcg.mission.flight.plane;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
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
        List<CrewMember> crewsForFlight = buildFlightCrews(numPlanes);
        if (crewsForFlight.size() < numPlanes)
        {
            numPlanes = crewsForFlight.size();
        }
        List<EquippedTank> planesTypesForFlight = buildEquipmentForFllght(numPlanes);
        List<PlaneMcu> planesForFlight = createPlanes(planesTypesForFlight, crewsForFlight);
        
        return planesForFlight;
    }
    
    public static PlaneMcu createPlaneMcuByTankType (Campaign campaign, EquippedTank equippedPlane, ICountry country, CrewMember crewMember) throws PWCGException
    {
        PlaneMcu plane = new PlaneMcu(campaign, crewMember);
        plane.buildPlane(equippedPlane, country);
        return plane;
    }

	private List<EquippedTank> buildEquipmentForFllght(int numPlanes) throws PWCGException 
	{
		Equipment equipmentForSquadron = flightInformation.getCampaign().getEquipmentManager().getEquipmentForSquadron(flightInformation.getSquadron().getCompanyId());
        FlightPlaneTypeBuilder planeTypeBuilder = new FlightPlaneTypeBuilder(equipmentForSquadron, numPlanes);
        List<EquippedTank> planesTypesForFlight =planeTypeBuilder.getPlaneListForFlight();
		return planesTypesForFlight;
	}

	private List<CrewMember> buildFlightCrews(int numPlanes) throws PWCGException 
	{
		FlightCrewBuilder flightCrewBuilder = new FlightCrewBuilder(flightInformation);
        List<CrewMember> crewsForFlight = flightCrewBuilder.createCrewAssignmentsForFlight(numPlanes);
		return crewsForFlight;
	}

    private List<PlaneMcu> createPlanes(List<EquippedTank> planesTypesForFlight, List<CrewMember> crewsForFlight) throws PWCGException
    {        
        List<PlaneMcu> planesForFlight = new ArrayList<>();
        for (int index = 0; index < planesTypesForFlight.size(); ++index)
        {
        	try
        	{
	            EquippedTank equippedPlane = planesTypesForFlight.get(index);
	            CrewMember crewMember = crewsForFlight.get(index);            
	            PlaneMcu plane = createPlaneMcuByTankType(flightInformation.getCampaign(), equippedPlane, flightInformation.getSquadron().getCountry(), crewMember);
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
	    plane.setDesc(plane.getCrewMember().getNameAndRank());
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
        if (plane.getCrewMember().isPlayer())
        {
            aiLevel = AiSkillLevel.PLAYER;

        }
        else if (plane.getCrewMember() instanceof TankAce)
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
        return plane.getCrewMember().getAiSkillLevel();
    }
 }
