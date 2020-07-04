package pwcg.gui.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.mission.flight.crew.CrewPlanePayloadPairing;

public class BriefingAssignmentData
{
	private Squadron squadron;
    private List<CrewPlanePayloadPairing> assignedCrewPlanes = new ArrayList<>();
    private Map<Integer, SquadronMember> assignedPilots = new HashMap<>();
    private Map<Integer, SquadronMember> unAssignedPilots = new HashMap<>();
    private Map<Integer, EquippedPlane> assignedPlanes = new HashMap<>();
    private Map<Integer, EquippedPlane> unAssignedPlanes = new HashMap<>();

    public void reset()
    {
        assignedCrewPlanes.clear();
        assignedPilots.clear();
        unAssignedPilots.clear();
        assignedPlanes.clear();
        unAssignedPlanes.clear();
    }

    public void addPilot(SquadronMember squadronMember)
    {
        unAssignedPilots.put(squadronMember.getSerialNumber(), squadronMember);
    }

    public void addPlane(EquippedPlane equippedPlane)
    {
        unAssignedPlanes.put(equippedPlane.getSerialNumber(), equippedPlane);
    }

    public void assignPilot(int pilotSerialNumber, int planeSerialNumber)
    {
        SquadronMember assignedPilot = unAssignedPilots.remove(pilotSerialNumber);
        assignedPilots.put(assignedPilot.getSerialNumber(), assignedPilot);
        
        EquippedPlane equippedPlane = unAssignedPlanes.remove(planeSerialNumber);
        assignedPlanes.put(equippedPlane.getSerialNumber(), equippedPlane);
        
        CrewPlanePayloadPairing crewPlane = new CrewPlanePayloadPairing(assignedPilot, equippedPlane);
        crewPlane.setPayloadId(CrewPlanePayloadPairing.NO_PAYLOAD_ASSIGNED);
        crewPlane.clearModification();

        assignedCrewPlanes.add(crewPlane);
    }

    public void unassignPilot(int pilotSerialNumber)
    {
        CrewPlanePayloadPairing crewPlane = this.findAssignedCrewPairingByPilot(pilotSerialNumber);
        assignedCrewPlanes.remove(crewPlane);

        assignedPilots.remove(crewPlane.getPilot().getSerialNumber());
        unAssignedPilots.put(crewPlane.getPilot().getSerialNumber(), crewPlane.getPilot());

        assignedPlanes.remove(crewPlane.getPlane().getSerialNumber());
        unAssignedPlanes.put(crewPlane.getPlane().getSerialNumber(), crewPlane.getPlane());
    }
    
    public void changePlane(int pilotSerialNumber, Integer planeSerialNumber)
    {
        CrewPlanePayloadPairing crewPlane = this.findAssignedCrewPairingByPilot(pilotSerialNumber);

        assignedPlanes.remove(crewPlane.getPlane().getSerialNumber());
        unAssignedPlanes.put(crewPlane.getPlane().getSerialNumber(), crewPlane.getPlane());
        
        EquippedPlane equippedPlane = unAssignedPlanes.remove(planeSerialNumber);
        assignedPlanes.put(equippedPlane.getSerialNumber(), equippedPlane);
 
        crewPlane.setPlane(equippedPlane);
        crewPlane.setPayloadId(CrewPlanePayloadPairing.NO_PAYLOAD_ASSIGNED);
        crewPlane.clearModification();
    }

    public void modifyPayload(Integer pilotSerialNumber, int payloadId) 
    {
        CrewPlanePayloadPairing crewPlane = this.findAssignedCrewPairingByPilot(pilotSerialNumber);
        crewPlane.setPayloadId(payloadId);
    }

    public Map<Integer, SquadronMember> getAssignedPilots()
    {
        return assignedPilots;
    }

    public Map<Integer, SquadronMember> getUnassignedPilots()
    {
        return unAssignedPilots;
    }

    public Map<Integer, EquippedPlane> getAssignedPlanes()
    {
        return assignedPlanes;
    }

    public Map<Integer, EquippedPlane> getUnassignedPlanes()
    {
        return unAssignedPlanes;
    }

	public Squadron getSquadron() 
	{
		return squadron;
	}

	public void setSquadron(Squadron squadron) 
	{
		this.squadron = squadron;
	}

    public CrewPlanePayloadPairing findAssignedCrewPairingByPilot(int pilotSerialNumber)
    {
        for (CrewPlanePayloadPairing crewPlane : assignedCrewPlanes)
        {
            if (crewPlane.getPilot().getSerialNumber() == pilotSerialNumber)
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

    public void movePilotUp(int pilotSerialNumber)
    {
        for (int playerToBeMovedIndex = 0; playerToBeMovedIndex < assignedCrewPlanes.size(); ++playerToBeMovedIndex)
        {
            CrewPlanePayloadPairing assignedCrew = assignedCrewPlanes.get(playerToBeMovedIndex);
            if (assignedCrew.getPilot().getSerialNumber() == pilotSerialNumber)
            {
                if (playerToBeMovedIndex != 0)
                {
                    assignedCrewPlanes = movePilotUpByIndex(playerToBeMovedIndex);
                    return;
                }
            }
        } 
    }

    private List<CrewPlanePayloadPairing> movePilotUpByIndex(int playerToBeMovedIndex)
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

    public void movePilotDown(int pilotSerialNumber)
    {
        for (int playerToBeMovedIndex = 0; playerToBeMovedIndex < assignedCrewPlanes.size(); ++playerToBeMovedIndex)
        {
            CrewPlanePayloadPairing assignedCrew = assignedCrewPlanes.get(playerToBeMovedIndex);
            if (assignedCrew.getPilot().getSerialNumber() == pilotSerialNumber)
            {
                if (playerToBeMovedIndex != (assignedCrewPlanes.size()-1))
                {
                    assignedCrewPlanes = movePilotDownFromIndex(playerToBeMovedIndex);
                    return;
                }
            }
        }
 
    }

    private List<CrewPlanePayloadPairing> movePilotDownFromIndex(int playerToBeMovedIndex)
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
