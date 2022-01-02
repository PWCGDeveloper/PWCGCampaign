package pwcg.coop;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.coop.model.CoopDisplayRecord;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class CoopPersonaDataBuilder
{
    public List<CoopDisplayRecord> getPlayerCrewMembersForUser(Campaign campaign) throws PWCGException
    {
        List<CoopDisplayRecord> coopDisplayRecords = new ArrayList<>();

        for (CrewMember crewMember : campaign.getPersonnelManager().getAllPlayers().getCrewMemberList())
        {
            try
            {
                CoopUser coopUser = CoopUserManager.getIntance().getCoopUserForCrewMember(campaign.getName(), crewMember.getSerialNumber());
                CoopDisplayRecord coopDisplayRecord = formCoopDisplayRecordsForUser(campaign, coopUser, crewMember);
                coopDisplayRecords.add(coopDisplayRecord);
            }
            catch (Exception e)
            {
                PWCGLogger.logException(e);
            }
        }
        return coopDisplayRecords;
    }
    
    
    private CoopDisplayRecord formCoopDisplayRecordsForUser(Campaign campaign, CoopUser coopUser, CrewMember crewMember) throws PWCGException
    {
        String coopUsername = "Not Assigned";
        if (coopUser != null)
        {
            coopUsername = coopUser.getUsername();
        }
        
        CoopDisplayRecord coopDisplayRecord = new CoopDisplayRecord();
        coopDisplayRecord.setUsername(coopUsername);
        coopDisplayRecord.setPilorNameAndRank(crewMember.getNameAndRank());
        coopDisplayRecord.setCampaignName(campaign.getCampaignData().getName());
        coopDisplayRecord.setSquadronName(crewMember.determineSquadron().determineDisplayName(campaign.getDate()));
        coopDisplayRecord.setCrewMemberStatus(crewMember.getCrewMemberActiveStatus());
        coopDisplayRecord.setCrewMemberSerialNumber(crewMember.getSerialNumber());

        return coopDisplayRecord;
    }

}
