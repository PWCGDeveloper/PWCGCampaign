package pwcg.campaign.squadmember;

import pwcg.core.exception.PWCGUserException;

public interface ISquadronMemberReplacer
{
    public SquadronMember createPilot(String playerPilotName, String rank, String squadronName, String coopUser) throws PWCGUserException, Exception;
}
