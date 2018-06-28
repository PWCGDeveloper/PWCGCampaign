package pwcg.campaign;

import pwcg.campaign.personnel.InitialSquadronStaffer;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class CampaignSquadronGenerator 
{
    private Campaign campaign;
    private Squadron squadron;
    
    public CampaignSquadronGenerator(Campaign campaign, Squadron squadron)
    {
        this.campaign = campaign;
        this.squadron = squadron;
    }
    
    public void createSquadron(CampaignGeneratorModel generatorModel) throws PWCGException
    {
        createSquadronStaff(generatorModel);
    }

    private void createSquadronStaff(CampaignGeneratorModel generatorModel) throws PWCGException
    {
        InitialSquadronStaffer squadronStaffer = new InitialSquadronStaffer(campaign, squadron);
        if (squadron.getSquadronId() == campaign.getSquadronId())
        {
            squadronStaffer.addPlayerToCampaign(generatorModel);
        }
        SquadronPersonnel squadronPersonnel = squadronStaffer.generatePersonnel();
        campaign.getPersonnelManager().addPersonnelForSquadron(squadronPersonnel);
    }
}
