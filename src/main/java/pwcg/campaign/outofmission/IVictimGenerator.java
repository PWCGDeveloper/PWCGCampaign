package pwcg.campaign.outofmission;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.core.exception.PWCGException;

public interface IVictimGenerator
{
    CrewMember generateVictimAiCrew() throws PWCGException;
    EquippedPlane generateVictimPlane() throws PWCGException;
}
