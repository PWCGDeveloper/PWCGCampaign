package pwcg.aar.inmission.phase3.reconcile.personnel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;

public class PersonnelAceLossInMissionHandler
{
    private Campaign campaign;

    private Map<Integer, Ace> acesKilled = new HashMap<>();
    
    public PersonnelAceLossInMissionHandler(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public Map<Integer, Ace> acesShotDownInMission(List<LogPilot> aceStatusList) throws PWCGException
    {
        for (LogPilot aceStatus: aceStatusList)
        {
            CampaignPersonnelManager campaignPersonnelManager = campaign.getPersonnelManager();            
            SquadronMember pilot = campaignPersonnelManager.getAnyCampaignMember(aceStatus.getSerialNumber());

            if (pilot instanceof Ace)
            {
                Ace ace = (Ace) pilot;
                if (aceStatus.getStatus() == SquadronMemberStatus.STATUS_KIA)
                {
                    acesKilled.put(ace.getSerialNumber(), ace);
                }
            }
        }
        
        return acesKilled;
    }
}
