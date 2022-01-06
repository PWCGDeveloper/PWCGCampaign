package pwcg.gui.rofmap.brief.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.EquippedTank;
import pwcg.mission.playerunit.crew.CrewVehiclePayloadPairing;

public class BriefingCrewMemberAssignmentData
{
	private Company squadron;
    private List<CrewVehiclePayloadPairing> assignedCrewPlanes = new ArrayList<>();
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
        
        CrewVehiclePayloadPairing crewPlane = new CrewVehiclePayloadPairing(assignedCrewMember, equippedPlane);
        crewPlane.setPayloadId(CrewVehiclePayloadPairing.NO_PAYLOAD_ASSIGNED);
        crewPlane.clearModification();

        assignedCrewPlanes.add(crewPlane);
    }

    public void unassignCrewMember(int crewMemberSerialNumber)
    {
        CrewVehiclePayloadPairing crewPlane = this.findAssignedCrewPairingByCrewMember(crewMemberSerialNumber);
        assignedCrewPlanes.remove(crewPlane);

        unAssignedCrewMembers.put(crewPlane.getCrewMember().getSerialNumber(), crewPlane.getCrewMember());
        unAssignedPlanes.put(crewPlane.getPlane().getSerialNumber(), crewPlane.getPlane());
    }
    
    public void changePlane(int crewMemberSerialNumber, Integer planeSerialNumber)
    {
        CrewVehiclePayloadPairing crewPlane = this.findAssignedCrewPairingByCrewMember(crewMemberSerialNumber);

        unAssignedPlanes.put(crewPlane.getPlane().getSerialNumber(), crewPlane.getPlane());
        
        EquippedTank equippedPlane = unAssignedPlanes.remove(planeSerialNumber);
 
        crewPlane.setPlane(equippedPlane);
        crewPlane.setPayloadId(CrewVehiclePayloadPairing.NO_PAYLOAD_ASSIGNED);
        crewPlane.clearModification();
    }

    public void modifyPayload(Integer crewMemberSerialNumber, int payloadId) 
    {
        CrewVehiclePayloadPairing crewPlane = this.findAssignedCrewPairingByCrewMember(crewMemberSerialNumber);
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

	public Company getSquadron() 
	{
		return squadron;
	}

	public void setSquadron(Company squadron) 
	{
		this.squadron = squadron;
	}

    public CrewVehiclePayloadPairing findAssignedCrewPairingByCrewMember(int crewMemberSerialNumber)
    {
        for (CrewVehiclePayloadPairing crewPlane : assignedCrewPlanes)
        {
            if (crewPlane.getCrewMember().getSerialNumber() == crewMemberSerialNumber)
            {
                return crewPlane;
            }
        }
        return null;
    }

    public CrewVehiclePayloadPairing findAssignedCrewPairingByPlane(int planeSerialNumber)
    {
        for (CrewVehiclePayloadPairing crewPlane : assignedCrewPlanes)
        {
            if (crewPlane.getPlane().getSerialNumber() == planeSerialNumber)
            {
                return crewPlane;
            }
        }
        return null;
    }

    public List<CrewVehiclePayloadPairing> getCrews()
    {
        List<CrewVehiclePayloadPairing> copyOfAssignedCrewPlanes = new ArrayList<>();
        copyOfAssignedCrewPlanes.addAll(assignedCrewPlanes);
        return copyOfAssignedCrewPlanes;
    }

    public void moveCrewMemberUp(int crewMemberSerialNumber)
    {
        for (int playerToBeMovedIndex = 0; playerToBeMovedIndex < assignedCrewPlanes.size(); ++playerToBeMovedIndex)
        {
            CrewVehiclePayloadPairing assignedCrew = assignedCrewPlanes.get(playerToBeMovedIndex);
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

    private List<CrewVehiclePayloadPairing> moveCrewMemberUpByIndex(int playerToBeMovedIndex)
    {
        List<CrewVehiclePayloadPairing> copyOfAssignedCrewPlanes = new ArrayList<>();
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
            CrewVehiclePayloadPairing assignedCrew = assignedCrewPlanes.get(playerToBeMovedIndex);
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

    private List<CrewVehiclePayloadPairing> moveCrewMemberDownFromIndex(int playerToBeMovedIndex)
    {
        List<CrewVehiclePayloadPairing> copyOfAssignedCrewPlanes = new ArrayList<>();
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
