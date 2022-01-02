package pwcg.gui.rofmap.brief.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadron.Company;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;

public class BriefingCrewMemberAssignmentData
{
	private Company squadron;
    private List<CrewPlanePayloadPairing> assignedCrewPlanes = new ArrayList<>();
    private Map<Integer, CrewMember> unAssignedCrewMembers = new HashMap<>();
    private Map<Integer, EquippedPlane> unAssignedPlanes = new HashMap<>();

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

    public void addPlane(EquippedPlane equippedPlane)
    {
        unAssignedPlanes.put(equippedPlane.getSerialNumber(), equippedPlane);
    }

    public void assignCrewMember(int crewMemberSerialNumber, int planeSerialNumber)
    {
        CrewMember assignedCrewMember = unAssignedCrewMembers.remove(crewMemberSerialNumber);        
        EquippedPlane equippedPlane = unAssignedPlanes.remove(planeSerialNumber);
        
        CrewPlanePayloadPairing crewPlane = new CrewPlanePayloadPairing(assignedCrewMember, equippedPlane);
        crewPlane.setPayloadId(CrewPlanePayloadPairing.NO_PAYLOAD_ASSIGNED);
        crewPlane.clearModification();

        assignedCrewPlanes.add(crewPlane);
    }

    public void unassignCrewMember(int crewMemberSerialNumber)
    {
        CrewPlanePayloadPairing crewPlane = this.findAssignedCrewPairingByCrewMember(crewMemberSerialNumber);
        assignedCrewPlanes.remove(crewPlane);

        unAssignedCrewMembers.put(crewPlane.getCrewMember().getSerialNumber(), crewPlane.getCrewMember());
        unAssignedPlanes.put(crewPlane.getPlane().getSerialNumber(), crewPlane.getPlane());
    }
    
    public void changePlane(int crewMemberSerialNumber, Integer planeSerialNumber)
    {
        CrewPlanePayloadPairing crewPlane = this.findAssignedCrewPairingByCrewMember(crewMemberSerialNumber);

        unAssignedPlanes.put(crewPlane.getPlane().getSerialNumber(), crewPlane.getPlane());
        
        EquippedPlane equippedPlane = unAssignedPlanes.remove(planeSerialNumber);
 
        crewPlane.setPlane(equippedPlane);
        crewPlane.setPayloadId(CrewPlanePayloadPairing.NO_PAYLOAD_ASSIGNED);
        crewPlane.clearModification();
    }

    public void modifyPayload(Integer crewMemberSerialNumber, int payloadId) 
    {
        CrewPlanePayloadPairing crewPlane = this.findAssignedCrewPairingByCrewMember(crewMemberSerialNumber);
        crewPlane.setPayloadId(payloadId);
    }

    public Map<Integer, CrewMember> getUnassignedCrewMembers()
    {
        return unAssignedCrewMembers;
    }

    public Map<Integer, EquippedPlane> getUnassignedPlanes()
    {
        return unAssignedPlanes;
    }

	public Company getSquadron() 
	{
		return squadron;
	}

	public void setSquadron(Company squadron) 
	{
		this.squadron = squadron;
	}

    public CrewPlanePayloadPairing findAssignedCrewPairingByCrewMember(int crewMemberSerialNumber)
    {
        for (CrewPlanePayloadPairing crewPlane : assignedCrewPlanes)
        {
            if (crewPlane.getCrewMember().getSerialNumber() == crewMemberSerialNumber)
            {
                return crewPlane;
            }
        }
        return null;
    }

    public CrewPlanePayloadPairing findAssignedCrewPairingByPlane(int planeSerialNumber)
    {
        for (CrewPlanePayloadPairing crewPlane : assignedCrewPlanes)
        {
            if (crewPlane.getPlane().getSerialNumber() == planeSerialNumber)
            {
                return crewPlane;
            }
        }
        return null;
    }

    public List<CrewPlanePayloadPairing> getCrews()
    {
        List<CrewPlanePayloadPairing> copyOfAssignedCrewPlanes = new ArrayList<>();
        copyOfAssignedCrewPlanes.addAll(assignedCrewPlanes);
        return copyOfAssignedCrewPlanes;
    }

    public void moveCrewMemberUp(int crewMemberSerialNumber)
    {
        for (int playerToBeMovedIndex = 0; playerToBeMovedIndex < assignedCrewPlanes.size(); ++playerToBeMovedIndex)
        {
            CrewPlanePayloadPairing assignedCrew = assignedCrewPlanes.get(playerToBeMovedIndex);
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

    private List<CrewPlanePayloadPairing> moveCrewMemberUpByIndex(int playerToBeMovedIndex)
    {
        List<CrewPlanePayloadPairing> copyOfAssignedCrewPlanes = new ArrayList<>();
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
            CrewPlanePayloadPairing assignedCrew = assignedCrewPlanes.get(playerToBeMovedIndex);
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

    private List<CrewPlanePayloadPairing> moveCrewMemberDownFromIndex(int playerToBeMovedIndex)
    {
        List<CrewPlanePayloadPairing> copyOfAssignedCrewPlanes = new ArrayList<>();
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
