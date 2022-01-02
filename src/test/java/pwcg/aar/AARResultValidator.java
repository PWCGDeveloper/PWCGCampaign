package pwcg.aar;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class AARResultValidator
{
    Campaign campaign;
    ExpectedResults expectedResults;

    public AARResultValidator (ExpectedResults expectedResults)
    {
        this.campaign = expectedResults.getCampaign();
        this.expectedResults = expectedResults;
    }

    public void validateInMission(int playerMissionsFlown, int expectedPlayerVictories) throws PWCGException
    {
        CrewMember player = campaign.findReferencePlayer();
        assert(campaign.getDate().after(DateUtils.getDateYYYYMMDD("19411101")));
        assert(player.getCrewMemberVictories().getAirToAirVictoryCount() == expectedResults.getPlayerAirVictories());
        assert(player.getCrewMemberVictories().getGroundVictoryCount() == expectedResults.getPlayerGroundVictories());
        
        CrewMember otherCrewMember = campaign.getPersonnelManager().getAnyCampaignMember(expectedResults.getCrewMemberCrewMemberSerialNumber());        
        assert(otherCrewMember.getCrewMemberVictories().getAirToAirVictoryCount() == expectedResults.getCrewMemberAirVictories());
        assert(otherCrewMember.getCrewMemberVictories().getGroundVictoryCount() == expectedResults.getCrewMemberGroundVictories());
        for (Integer serialNumber : expectedResults.getLostCrewMembers())
        {
            CrewMember lostCrewMember = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            assert(lostCrewMember.getCrewMemberActiveStatus() <= CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        }
        assert(player.getBattlesFought()  == (playerMissionsFlown+1));
        assert(player.getCrewMemberVictories().getAirToAirVictoryCount() == expectedPlayerVictories);
    }

    public void validateLeave() throws PWCGException
    {
        CrewMember player = campaign.findReferencePlayer();
        assert(campaign.getDate().after(DateUtils.getDateYYYYMMDD("19411101")));
        assert(player.getCrewMemberVictories().getAirToAirVictoryCount() == 0);
        assert(player.getCrewMemberVictories().getGroundVictoryCount() == 0);
        
        for (Integer serialNumber : expectedResults.getLostCrewMembers())
        {
            CrewMember lostCrewMember = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            assert(lostCrewMember.getCrewMemberActiveStatus() <= CrewMemberStatus.STATUS_WOUNDED);
        }
    }
}
