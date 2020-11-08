package pwcg.aar.campaign.update;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.CampaignPersonnelIOJson;
import pwcg.campaign.personnel.InitialSquadronStaffer;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronManager;
import pwcg.core.exception.PWCGException;

public class CampaignUpdateNewSquadronStaffer
{
    private Campaign campaign;
    private List<Integer> squadronsAdded = new ArrayList<>();
    
    public CampaignUpdateNewSquadronStaffer (Campaign campaign) 
    {
        this.campaign = campaign;
    }

    public List<Integer> staffNewSquadrons() throws PWCGException
    {
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> squadronsToStaff = squadronManager.getActiveSquadrons(campaign.getDate());
        for (Squadron squadron : squadronsToStaff)
        {
            if (campaign.getPersonnelManager().getSquadronPersonnel(squadron.getSquadronId()) == null)
            {
                InitialSquadronStaffer squadronStaffer = new InitialSquadronStaffer(campaign, squadron);
                SquadronPersonnel squadronPersonnel = squadronStaffer.generatePersonnel();
                campaign.getPersonnelManager().addPersonnelForSquadron(squadronPersonnel);
                squadronsAdded.add(squadron.getSquadronId());
                CampaignPersonnelIOJson.writeSquadron(campaign, squadron.getSquadronId());
            }
        }
        
        return squadronsAdded;
    }
}
