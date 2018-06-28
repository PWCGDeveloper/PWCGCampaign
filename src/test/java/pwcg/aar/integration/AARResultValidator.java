package pwcg.aar.integration;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
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

    public void validateInMission() throws PWCGException
    {
        assert(campaign.getDate().after(DateUtils.getDateYYYYMMDD("19411101")));
        assert(campaign.getPlayer().getVictories().size() == expectedResults.getPlayerAirVictories());
        assert(campaign.getPlayer().getGroundVictories().size() == expectedResults.getPlayerGroundVictories());
        
        SquadronMember otherPilot = campaign.getPersonnelManager().getAnyCampaignMember(expectedResults.getOtherPilotSerialNumber());        
        assert(otherPilot.getVictories().size() == expectedResults.getOtherAirVictories());
        assert(otherPilot.getGroundVictories().size() == expectedResults.getOtherGroundVictories());
        for (Integer serialNumber : expectedResults.getLostPilots())
        {
            SquadronMember lostPilot = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            assert(lostPilot.getPilotActiveStatus() <= SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        }
    }

    public void validateLeave() throws PWCGException
    {
        assert(campaign.getDate().after(DateUtils.getDateYYYYMMDD("19411101")));
        assert(campaign.getPlayer().getVictories().size() == 0);
        assert(campaign.getPlayer().getGroundVictories().size() == 0);
        
        for (Integer serialNumber : expectedResults.getLostPilots())
        {
            SquadronMember lostPilot = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
            assert(lostPilot.getPilotActiveStatus() <= SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        }
    }
}
