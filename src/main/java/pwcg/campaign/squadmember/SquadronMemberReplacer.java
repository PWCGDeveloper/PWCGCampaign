package pwcg.campaign.squadmember;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPilotGeneratorModel;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronManager;
import pwcg.coop.CoopUserManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;

public class SquadronMemberReplacer  implements ISquadronMemberReplacer
{
    protected Campaign campaign;
    
    public SquadronMemberReplacer(Campaign campaign)
    {
        this.campaign = campaign;
    }
	
    public SquadronMember createPersona(String playerPilotName, String rank, String squadronName, String coopUser) throws PWCGUserException, Exception
    {        
        Squadron newPlayerSquadron = getNewPlayerSquadron(squadronName);
    	SquadronPersonnel newPlayerSquadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(newPlayerSquadron.getSquadronId());

        SquadronMember newSquadronMewmber = addnewPilotToCampaign(playerPilotName, rank, newPlayerSquadron, newPlayerSquadronPersonnel);        
        removeAiSquadronMember(rank, newPlayerSquadron, newPlayerSquadronPersonnel);
        
        if (campaign.isCoop())
        {
            CoopUserManager.getIntance().createCoopPersona(campaign.getName(), newSquadronMewmber, coopUser);
        }
        
        return newSquadronMewmber;
    }

	private Squadron getNewPlayerSquadron(String squadronName) throws PWCGException 
	{
		SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        Squadron newPlayerSquadron = squadronManager.getSquadronByName(squadronName, campaign.getDate());
		return newPlayerSquadron;
	}

	private SquadronMember addnewPilotToCampaign(String playerPilotName, String rank, Squadron newPlayerSquadron,
			SquadronPersonnel newPlayerSquadronPersonnel) throws PWCGException 
	{
	    CampaignPilotGeneratorModel pilotModel = new CampaignPilotGeneratorModel();
        pilotModel.setPlayerRank(rank);
        pilotModel.setPlayerName(playerPilotName);
        pilotModel.setService(newPlayerSquadron.determineServiceForSquadron(campaign.getDate()));
        
        SquadronMemberFactory squadronMemberFactory = new SquadronMemberFactory(campaign, newPlayerSquadron, newPlayerSquadronPersonnel);
        SquadronMember newPlayer = squadronMemberFactory.createPlayer(pilotModel);

        newPlayerSquadronPersonnel.addSquadronMember(newPlayer);
		return newPlayer;
	}

	private void removeAiSquadronMember(String rank, Squadron newPlayerSquadron, SquadronPersonnel newPlayerSquadronPersonnel) throws PWCGException 
	{
		AiPilotRemovalChooser pilotRemovalChooser = new AiPilotRemovalChooser(campaign);
        SquadronMember squadronMemberToReplace = pilotRemovalChooser.findAiPilotToRemove(rank, newPlayerSquadron.getSquadronId());
        newPlayerSquadronPersonnel.removeSquadronMember(squadronMemberToReplace);
	}
}
