package pwcg.mission.flight.crew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadmember.SquadronMember;

public class CrewPlanePayloadPairing
{
    public static final String NO_PLANE_ASSIGNED = "No plane assigned";
    public static final int NO_PAYLOAD_ASSIGNED = -1;
    
    private SquadronMember pilot;
    private EquippedPlane plane;
    private int payloadId = NO_PAYLOAD_ASSIGNED;
    private Map<String, String> modifications = new HashMap<>();

    public CrewPlanePayloadPairing(SquadronMember pilot, EquippedPlane plane)
    {
        this.pilot = pilot;
        this.plane = plane;
    }
    
    public SquadronMember getPilot()
    {
        return pilot;
    }

    public EquippedPlane getPlane()
    {
        return plane;
    }

    public void setPlane(EquippedPlane plane)
    {
        this.plane = plane;
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
}
