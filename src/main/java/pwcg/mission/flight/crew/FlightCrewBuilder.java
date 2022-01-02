package pwcg.mission.flight.crew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberSorter;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.SerialNumber.SerialNumberClassification;
import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;

public class FlightCrewBuilder
{
    private FlightInformation flightInformation;

    private Map <Integer, CrewMember> assignedCrewMap = new HashMap <>();
    private Map <Integer, CrewMember> unassignedCrewMap = new HashMap <>();
    
	public FlightCrewBuilder(FlightInformation flightInformation)
	{
        this.flightInformation = flightInformation;
	}

    public List<CrewMember> createCrewAssignmentsForFlight(int numCrewNeeded) throws PWCGException 
    {
        CrewFactory crewFactory = new CrewFactory(flightInformation);
        unassignedCrewMap = crewFactory.createCrews();
        
        assignPlayersToCrew();
        assignAiCrewMembersToPlanes(numCrewNeeded);
        
        List<CrewMember> sortedByRank = sortCrewsByRank();
        List<CrewMember> finalCrewSequence = playerIsLead(sortedByRank);
        return finalCrewSequence;
    }

    private void assignPlayersToCrew() throws PWCGException
    {
        if (Company.isPlayerSquadron(flightInformation.getCampaign(), flightInformation.getSquadron().getSquadronId()))
        {
            List<CrewMember> participatingPlayerCrews = new ArrayList<>();
            for (CrewMember crewMember : flightInformation.getFlightParticipatingPlayers())
            {
            	participatingPlayerCrews.add(crewMember);
            }

            for (CrewMember  participatingPlayerCrew : participatingPlayerCrews)
            {
                assignedCrewMap.put(participatingPlayerCrew.getSerialNumber(), participatingPlayerCrew);
                unassignedCrewMap.remove(participatingPlayerCrew.getSerialNumber());
            }
        }
    }

	private void assignAiCrewMembersToPlanes(int numCrewNeeded) throws PWCGException
    {
        while (assignedCrewMap.size() < numCrewNeeded)
        {
            List<Integer> unassignedAiCrewSerialNumbers = buildUnassignedAiCrewMembers();
            
            int crewIndex = RandomNumberGenerator.getRandom(unassignedAiCrewSerialNumbers.size());
            int selectedSerialNumber = unassignedAiCrewSerialNumbers.get(crewIndex);
            CrewMember crewToAssign = unassignedCrewMap.get(selectedSerialNumber);
            assignedCrewMap.put(crewToAssign.getSerialNumber(), crewToAssign);
            unassignedCrewMap.remove(crewToAssign.getSerialNumber());
        }
    }
    
	private List<Integer> buildUnassignedAiCrewMembers()
	{
        List<Integer> unassignedAiCrewSerialNumbers = new ArrayList<>();
        List<Integer> unassignedCrewSerialNumbers = new ArrayList<>(unassignedCrewMap.keySet());
        for (int unassignedCrewSerialNumber : unassignedCrewSerialNumbers)
        {
        	if (shouldAssignAICrewMember(unassignedCrewSerialNumber))
        	{
        		unassignedAiCrewSerialNumbers.add(unassignedCrewSerialNumber);
        	}
        }
        return unassignedAiCrewSerialNumbers;
	}
	
	private boolean shouldAssignAICrewMember(int unassignedCrewSerialNumber)
	{
        if (SerialNumber.getSerialNumberClassification(unassignedCrewSerialNumber) == SerialNumberClassification.AI)
        {
            return true;
        }
        else if (SerialNumber.getSerialNumberClassification(unassignedCrewSerialNumber) == SerialNumberClassification.ACE)
        {
            return true;
        }
        else
        {
            return false;
        }
	}
	
    private List<CrewMember> sortCrewsByRank() throws PWCGException
    {
        return CrewMemberSorter.sortCrewMembers(flightInformation.getCampaign(), assignedCrewMap);
    }

    private List<CrewMember> playerIsLead(List<CrewMember> sortedByRank)
    {
        List<CrewMember> withPlayerAsLead = new ArrayList<>();
        if (FlightTypes.isPlayerLead(flightInformation.getFlightType()) && flightInformation.isPlayerFlight())
        {
            for (CrewMember crewMember : sortedByRank)
            {
                if (crewMember.isPlayer())
                {
                    withPlayerAsLead.add(crewMember);
                }
            }
            
            for (CrewMember crewMember : sortedByRank)
            {
                if (!crewMember.isPlayer())
                {
                    withPlayerAsLead.add(crewMember);
                }
            }
            
            return withPlayerAsLead;
        }
        return sortedByRank;
    }
}
