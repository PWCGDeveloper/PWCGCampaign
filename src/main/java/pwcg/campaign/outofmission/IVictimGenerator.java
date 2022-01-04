package pwcg.campaign.outofmission;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.EquippedTank;
import pwcg.core.exception.PWCGException;

public interface IVictimGenerator
{
    CrewMember generateVictimAiCrew() throws PWCGException;
    EquippedTank generateVictimPlane() throws PWCGException;
}
