package pwcg.mission.playerunit.crew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberSorter;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.SerialNumber.SerialNumberClassification;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.playerunit.PlayerUnitInformation;

public class UnitCrewBuilder
{
    private PlayerUnitInformation unitInformation;

    private Map <Integer, CrewMember> assignedCrewMap = new HashMap <>();
    private Map <Integer, CrewMember> unassignedCrewMap = new HashMap <>();
    
	public UnitCrewBuilder(PlayerUnitInformation unitInformation)
	{
        this.unitInformation = unitInformation;
	}

    public List<CrewMember> createCrewAssignmentsForFlight(int numCrewNeeded) throws PWCGException 
    {
        CrewFactory crewFactory = new CrewFactory(unitInformation);
        unassignedCrewMap = crewFactory.createCrews();
        
        assignPlayersToCrew();
        assignAiCrewMembersToPlanes(numCrewNeeded);
        
        List<CrewMember> sortedByRank = sortCrewsByRank();
        return sortedByRank;
    }

    private void assignPlayersToCrew() throws PWCGException
    {
        if (Company.isPlayerCompany(unitInformation.getCampaign(), unitInformation.getCompany().getCompanyId()))
        {
            List<CrewMember> participatingPlayerCrews = new ArrayList<>();
            for (CrewMember crewMember : unitInformation.getParticipatingPlayersForCompany())
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
        return CrewMemberSorter.sortCrewMembers(unitInformation.getCampaign(), assignedCrewMap);
    }
}
