package pwcg.mission.flight.plane;

import java.util.List;

import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
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
        if (planes.size() == 0)
        {
            throw new PWCGException("No planes for flight");
        }
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
    	int targetPlanesInMission = calcTargetPlanesInFlight();
    	
    	if (targetPlanesInMission < flightInformation.getFlightParticipatingPlayers().size())
    	{
    		targetPlanesInMission = flightInformation.getFlightParticipatingPlayers().size();
    	}
    	
    	int numPilotsAvailable = calcActivePilotsAvailable();
    	if (targetPlanesInMission > numPilotsAvailable)
    	{
    		targetPlanesInMission = numPilotsAvailable;
    	}

    	int numPlanesAvailable = calcNumPlanesAvailable();
    	if (targetPlanesInMission > numPlanesAvailable)
    	{
    		targetPlanesInMission = numPlanesAvailable;
    	}

		return targetPlanesInMission;
    }

	private int calcActivePilotsAvailable() throws PWCGException 
    {
    	int squadronId = flightInformation.getSquadron().getSquadronId();
    	SquadronPersonnel squadronPersonnel = flightInformation.getCampaign().getPersonnelManager().getSquadronPersonnel(squadronId);
    	SquadronMembers activeAiSquadronMembers = squadronPersonnel.getActiveAiSquadronMembers();
		return (activeAiSquadronMembers.getSquadronMemberList().size() + flightInformation.getFlightParticipatingPlayers().size());
	}
    
    private int calcNumPlanesAvailable() 
    {
    	int squadronId = flightInformation.getSquadron().getSquadronId();
    	Equipment squadronEquipment = flightInformation.getCampaign().getEquipmentManager().getEquipmentForSquadron(squadronId);
		return (squadronEquipment.getActiveEquippedPlanes().size());
	}

	private int calcTargetPlanesInFlight() throws PWCGException 
    {
    	FlightSizeCalculator flightSizeCalculator = new FlightSizeCalculator(flightInformation);		
    	int numPlanesInMission = flightSizeCalculator.calcPlanesInFlight();
		return numPlanesInMission;
	}

	private List<PlaneMcu> createPlanes(int numPlanesInFlight) throws PWCGException 
    {        
        PlaneMCUFactory planeGeneratorPlayer = new PlaneMCUFactory(flightInformation);
        List<PlaneMcu> planes = planeGeneratorPlayer.createPlanesForFlight(numPlanesInFlight);
		return planes;
    }
}
