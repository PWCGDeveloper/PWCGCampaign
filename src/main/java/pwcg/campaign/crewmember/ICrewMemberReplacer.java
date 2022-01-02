package pwcg.campaign.crewmember;

import pwcg.core.exception.PWCGUserException;

public interface ICrewMemberReplacer
{
    public CrewMember createPersona(String playerCrewMemberName, String rank, String squadronName, String coopUser) throws PWCGUserException, Exception;
}
