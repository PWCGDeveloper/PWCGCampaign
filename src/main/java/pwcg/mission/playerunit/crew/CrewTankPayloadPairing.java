package pwcg.mission.playerunit.crew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.EquippedTank;

public class CrewTankPayloadPairing
{
    public static final String NO_PLANE_ASSIGNED = "No plane assigned";
    public static final int NO_PAYLOAD_ASSIGNED = -1;
    
    private CrewMember crewMember;
    private EquippedTank vehicle;
    private int payloadId = NO_PAYLOAD_ASSIGNED;
    private Map<String, String> modifications = new HashMap<>();

    public CrewTankPayloadPairing(CrewMember crewMember, EquippedTank plane)
    {
        this.crewMember = crewMember;
        this.vehicle = plane;
    }
    
    public CrewMember getCrewMember()
    {
        return crewMember;
    }

    public EquippedTank getTank()
    {
        return vehicle;
    }

    public void setPlane(EquippedTank plane)
    {
        this.vehicle = plane;
    }

    public int getPayloadId()
    {
        return payloadId;
    }

    public void setPayloadId(int payloadId)
    {
        this.payloadId = payloadId;
    }

    public List<String> getModifications()
    {
        return new ArrayList<String>(modifications.values());
    }

    public void addModification(String modification)
    {
        this.modifications.put(modification, modification);
    }

    public void clearModification()
    {
        this.modifications.clear();
    }

	public void removeModification(String modification) 
	{
		if (modifications.containsKey(modification))
		{
			this.modifications.remove(modification);
		}
	}

    public void setModifications(List<String> sourceModifications)
    {
        this.modifications.clear();
        for (String sourceModification : sourceModifications)
        {
            addModification(sourceModification);
        }
    }
}
