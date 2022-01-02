package pwcg.aar.tabulate.debrief;

import java.util.List;

import pwcg.aar.data.AARContext;
import pwcg.aar.ui.display.model.AARMedalPanelData;
import pwcg.aar.ui.events.MedalEventGenerator;
import pwcg.aar.ui.events.model.MedalEvent;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class MedalPanelEventTabulator
{
    private Campaign campaign;
    private AARContext aarContext;

    private AARMedalPanelData medalPanelData = new AARMedalPanelData();

    public MedalPanelEventTabulator (Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
        
    public AARMedalPanelData tabulateForAARMedalPanel() throws PWCGException
    {
        MedalEventGenerator medalEventGenerator = new MedalEventGenerator(campaign);
        List<MedalEvent> medalEventsForCampaignMembers = medalEventGenerator.createCrewMemberMedalEvents(aarContext.getPersonnelAwards().getCampaignMemberMedals());
        
        if (!medalEventsForCampaignMembers.isEmpty())
        {
            medalPanelData.setMedalsAwarded(medalEventsForCampaignMembers);
        }
        return medalPanelData;
    }
}
