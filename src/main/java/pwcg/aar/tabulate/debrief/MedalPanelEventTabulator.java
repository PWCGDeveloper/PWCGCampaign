package pwcg.aar.tabulate.debrief;

import java.util.ArrayList;
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
        List<MedalEvent> medalEventsForCampaignMembersOutOfMission = medalEventGenerator.createPilotMedalEvents(aarContext.getReconciledOutOfMissionData().getPersonnelAwards().getCampaignMemberMedals());
        List<MedalEvent> medalEventsForCampaignMembersInMission = medalEventGenerator.createPilotMedalEvents(aarContext.getReconciledInMissionData().getPersonnelAwards().getCampaignMemberMedals());
        
        List<MedalEvent> medalEventsForCampaignMembers = new ArrayList<>();
        medalEventsForCampaignMembers.addAll(medalEventsForCampaignMembersOutOfMission);
        medalEventsForCampaignMembers.addAll(medalEventsForCampaignMembersInMission);
        
        List<MedalEvent> medalEventsForSquadronMembers = getMedalsForCurrentSquadron(medalEventsForCampaignMembers);
        medalPanelData.setMedalsAwarded(medalEventsForSquadronMembers);

        return medalPanelData;
    }
    
    private List<MedalEvent> getMedalsForCurrentSquadron(List<MedalEvent> medalEventsForCampaignMembers) throws PWCGException
    {
        List<MedalEvent> medalEventsForSquadronMembers = new ArrayList<>();
        for (MedalEvent medalEvent : medalEventsForCampaignMembers)
        {
            if (medalEvent.getPilot().getSquadronId() == campaign.getSquadronId())
            {
                medalEventsForSquadronMembers.add(medalEvent);
            }
        }
        return medalEventsForSquadronMembers;
    }

}
