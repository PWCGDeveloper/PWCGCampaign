package pwcg.campaign.context;

import pwcg.campaign.plane.SquadronPlaneAssignment;
import pwcg.campaign.squadron.Squadron;

public class SquadronUnavailablePlaneMapper 
{
    public static void mapPlaneToAvailableType(Squadron squadron)
    {
        for (SquadronPlaneAssignment planeAssignment : squadron.getPlaneAssignments())
        {
            if (planeAssignment.getArchType().equals("tempest"))
            {
                planeAssignment.setArchType("spitfire");
            }
            else  if (planeAssignment.getArchType().equals("p51"))
            {
                planeAssignment.setArchType("p47");
            }
            else  if (planeAssignment.getArchType().equals("p38"))
            {
                planeAssignment.setArchType("p47");
            }
            else  if (planeAssignment.getArchType().equals("b25"))
            {
                planeAssignment.setArchType("a20");
            }
        }
    }
}
