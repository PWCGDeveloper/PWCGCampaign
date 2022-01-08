package pwcg.gui.rofmap.brief.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.EquippedTank;
import pwcg.mission.playerunit.crew.CrewTankPayloadPairing;

public class BriefingCrewMemberAssignmentData
{
	private Company squadron;
    private List<CrewTankPayloadPairing> assignedCrewPlanes = new ArrayList<>();
    private Map<Integer, CrewMember> unAssignedCrewMembers = new HashMap<>();
    private Map<Integer, EquippedTank> unAssignedPlanes = new HashMap<>();

    public void reset()
    {
        assignedCrewPlanes.clear();
        unAssignedCrewMembers.clear();
        unAssignedPlanes.clear();
    }

    public void addCrewMember(CrewMember crewMember)
    {
        unAssignedCrewMembers.put(crewMember.getSerialNumber(), crewMember);
    }

    public void addPlane(EquippedTank equippedPlane)
    {
        unAssignedPlanes.put(equippedPlane.getSerialNumber(), equippedPlane);
    }

    public void assignCrewMember(int crewMemberSerialNumber, int planeSerialNumber)
    {
        CrewMember assignedCrewMember = unAssignedCrewMembers.remove(crewMemberSerialNumber);        
        EquippedTank equippedPlane = unAssignedPlanes.remove(planeSerialNumber);
        
        CrewTankPayloadPairing crewPlane = new CrewTankPayloadPairing(assignedCrewMember, equippedPlane);
        crewPlane.setPayloadId(CrewTankPayloadPairing.NO_PAYLOAD_ASSIGNED);
        crewPlane.clearModification();

        assignedCrewPlanes.add(crewPlane);
    }

    public void unassignCrewMember(int crewMemberSerialNumber)
    {
        CrewTankPayloadPairing crewPlane = this.findAssignedCrewPairingByCrewMember(crewMemberSerialNumber);
        assignedCrewPlanes.remove(crewPlane);

        unAssignedCrewMembers.put(crewPlane.getCrewMember().getSerialNumber(), crewPlane.getCrewMember());
        unAssignedPlanes.put(crewPlane.getTank().getSerialNumber(), crewPlane.getTank());
    }
    
    public void changePlane(int crewMemberSerialNumber, Integer planeSerialNumber)
    {
        CrewTankPayloadPairing crewPlane = this.findAssignedCrewPairingByCrewMember(crewMemberSerialNumber);

        unAssignedPlanes.put(crewPlane.getTank().getSerialNumber(), crewPlane.getTank());
        
        EquippedTank equippedPlane = unAssignedPlanes.remove(planeSerialNumber);
 
        crewPlane.setPlane(equippedPlane);
        crewPlane.setPayloadId(CrewTankPayloadPairing.NO_PAYLOAD_ASSIGNED);
        crewPlane.clearModification();
    }

    public void modifyPayload(Integer crewMemberSerialNumber, int payloadId) 
    {
        CrewTankPayloadPairing crewPlane = this.findAssignedCrewPairingByCrewMember(crewMemberSerialNumber);
        crewPlane.setPayloadId(payloadId);
    }

    public Map<Integer, CrewMember> getUnassignedCrewMembers()
    {
        return unAssignedCrewMembers;
    }

    public Map<Integer, EquippedTank> getUnassignedPlanes()
    {
        return unAssignedPlanes;
    }

	public Company getCompany() 
	{
		return squadron;
	}

	public void setSquadron(Company squadron) 
	{
		this.squadron = squadron;
	}

    public CrewTankPayloadPairing findAssignedCrewPairingByCrewMember(int crewMemberSerialNumber)
    {
        for (CrewTankPayloadPairing crewPlane : assignedCrewPlanes)
        {
            if (crewPlane.getCrewMember().getSerialNumber() == crewMemberSerialNumber)
            {
                return crewPlane;
            }
        }
        return null;
    }

    public CrewTankPayloadPairing findAssignedCrewPairingByPlane(int planeSerialNumber)
    {
        for (CrewTankPayloadPairing crewPlane : assignedCrewPlanes)
        {
            if (crewPlane.getTank().getSerialNumber() == planeSerialNumber)
            {
                return crewPlane;
            }
        }
        return null;
    }

    public List<CrewTankPayloadPairing> getCrews()
    {
        List<CrewTankPayloadPairing> copyOfAssignedCrewPlanes = new ArrayList<>();
        copyOfAssignedCrewPlanes.addAll(assignedCrewPlanes);
        return copyOfAssignedCrewPlanes;
    }

    public void moveCrewMemberUp(int crewMemberSerialNumber)
    {
        for (int playerToBeMovedIndex = 0; playerToBeMovedIndex < assignedCrewPlanes.size(); ++playerToBeMovedIndex)
        {
            CrewTankPayloadPairing assignedCrew = assignedCrewPlanes.get(playerToBeMovedIndex);
            if (assignedCrew.getCrewMember().getSerialNumber() == crewMemberSerialNumber)
            {
                if (playerToBeMovedIndex != 0)
                {
                    assignedCrewPlanes = moveCrewMemberUpByIndex(playerToBeMovedIndex);
                    return;
                }
            }
        } 
    }

    private List<CrewTankPayloadPairing> moveCrewMemberUpByIndex(int playerToBeMovedIndex)
    {
        List<CrewTankPayloadPairing> copyOfAssignedCrewPlanes = new ArrayList<>();
        for (int movingIndex = 0; movingIndex < assignedCrewPlanes.size(); ++movingIndex)
        {
            if (movingIndex == (playerToBeMovedIndex-1))
            {
                copyOfAssignedCrewPlanes.add(assignedCrewPlanes.get(playerToBeMovedIndex));
            }
            else if (movingIndex == (playerToBeMovedIndex))
            {
                copyOfAssignedCrewPlanes.add(assignedCrewPlanes.get(playerToBeMovedIndex-1));
            }
            else
            {
                copyOfAssignedCrewPlanes.add(assignedCrewPlanes.get(movingIndex));
            }
        }

        return copyOfAssignedCrewPlanes;
    }

    public void moveCrewMemberDown(int crewMemberSerialNumber)
    {
        for (int playerToBeMovedIndex = 0; playerToBeMovedIndex < assignedCrewPlanes.size(); ++playerToBeMovedIndex)
        {
            CrewTankPayloadPairing assignedCrew = assignedCrewPlanes.get(playerToBeMovedIndex);
            if (assignedCrew.getCrewMember().getSerialNumber() == crewMemberSerialNumber)
            {
                if (playerToBeMovedIndex != (assignedCrewPlanes.size()-1))
                {
                    assignedCrewPlanes = moveCrewMemberDownFromIndex(playerToBeMovedIndex);
                    return;
                }
            }
        }
 
    }

    private List<CrewTankPayloadPairing> moveCrewMemberDownFromIndex(int playerToBeMovedIndex)
    {
        List<CrewTankPayloadPairing> copyOfAssignedCrewPlanes = new ArrayList<>();
        for (int movingIndex = 0; movingIndex < assignedCrewPlanes.size(); ++movingIndex)
        {
            if (movingIndex == (playerToBeMovedIndex))
            {
                copyOfAssignedCrewPlanes.add(assignedCrewPlanes.get(playerToBeMovedIndex+1));
            }
            else if (movingIndex == (playerToBeMovedIndex+1))
            {
                copyOfAssignedCrewPlanes.add(assignedCrewPlanes.get(playerToBeMovedIndex));
            }
            else
            {
                copyOfAssignedCrewPlanes.add(assignedCrewPlanes.get(movingIndex));
            }
               
        }
        return copyOfAssignedCrewPlanes;
    }    
}
