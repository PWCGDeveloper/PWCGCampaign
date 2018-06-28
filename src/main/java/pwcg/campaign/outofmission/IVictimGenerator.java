package pwcg.campaign.outofmission;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public interface IVictimGenerator
{
    SquadronMember generateVictimAiCrew() throws PWCGException;
}
