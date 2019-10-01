package pwcg.campaign.context;

import pwcg.campaign.plane.SquadronPlaneAssignment;
import pwcg.campaign.squadron.Squadron;

public class SquadronUnavailablePlaneMapper 
{
    public static void mapPlaneToAvailableType(Squadron squadron)
    {
        for (SquadronPlaneAssignment planeAssignment : squadron.getPlaneAssignments())
        {
            if (planeAssignment.getArchType().equals("b25"))
            {
                planeAssignment.setArchType("a20");
            }
        }
    }
}
