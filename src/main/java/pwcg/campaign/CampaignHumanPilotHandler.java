package pwcg.campaign;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class CampaignHumanPilotHandler
{
    private Campaign campaign;
    
    public CampaignHumanPilotHandler(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public int addNewPilot(String humanPilotName, String humanPilotRank, int pilotToReplaceSerialNumber, int squadronId) throws PWCGException
    {
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
        SquadronPersonnel squadronSquadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(squadronId);

        SquadronMember newPlayer = addHumanPilot(humanPilotName, humanPilotRank, squadron, squadronSquadronPersonnel);
        removeAiPilot(pilotToReplaceSerialNumber, squadronSquadronPersonnel);
        
        return newPlayer.getSerialNumber();
    }

    private SquadronMember addHumanPilot(String humanPilotName, String humanPilotRank, Squadron squadron, SquadronPersonnel playerSquadronPersonnel) throws PWCGException
    {
        CampaignPilotGeneratorModel pilotModel = new CampaignPilotGeneratorModel();
        pilotModel.setPlayerRank(humanPilotRank);
        pilotModel.setPlayerName(humanPilotName);
        pilotModel.setService(squadron.determineServiceForSquadron(campaign.getDate()));

        SquadronMemberFactory squadronMemberFactory = new SquadronMemberFactory(campaign, squadron, playerSquadronPersonnel);
        SquadronMember newPlayer = squadronMemberFactory.createPlayer(pilotModel);
        playerSquadronPersonnel.addSquadronMember(newPlayer);
        return newPlayer;
    }

    private void removeAiPilot(int pilotToReplaceSerialNumber, SquadronPersonnel playerSquadronPersonnel) throws PWCGException
    {
        SquadronMember aiToRemove = playerSquadronPersonnel.getSquadronMember(pilotToReplaceSerialNumber);
        if (aiToRemove != null)
        {
            playerSquadronPersonnel.removeSquadronMember(aiToRemove);
        }
    }
}
