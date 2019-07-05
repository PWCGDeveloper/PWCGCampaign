package pwcg.campaign.squadmember;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.io.json.CoopPilotIOJson;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadron.Squadron;
import pwcg.coop.model.CoopPilot;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;

public class SquadronMemberReplacer 
{
    private Campaign campaign;
    
    public SquadronMemberReplacer(Campaign campaign)
    {
        this.campaign = campaign;
    }
	
    public void createPilot(String playerPilotName, String rank, String squadronName, String coopUser) throws PWCGUserException, Exception
    {        
        Squadron newPlayerSquadron = getNewPlayerSquadron(squadronName);
    	SquadronPersonnel newPlayerSquadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(newPlayerSquadron.getSquadronId());

        SquadronMember newSquadronMewmber = addnewPilotToCampaign(playerPilotName, rank, newPlayerSquadron, newPlayerSquadronPersonnel);        
        removeAiSquadronMember(rank, newPlayerSquadron, newPlayerSquadronPersonnel);
        
        if (campaign.getCampaignData().isCoop())
        {
            createCoopPilot(newSquadronMewmber, coopUser);
        }
    }

	private Squadron getNewPlayerSquadron(String squadronName) throws PWCGException 
	{
		SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        Squadron newPlayerSquadron = squadronManager.getSquadronByName(squadronName, campaign.getDate());
		return newPlayerSquadron;
	}

	private SquadronMember addnewPilotToCampaign(String playerPilotName, String rank, Squadron newPlayerSquadron,
			SquadronPersonnel newPlayerSquadronPersonnel) throws PWCGException 
	{
		CampaignGeneratorModel generatorModel = new CampaignGeneratorModel();
        generatorModel.setPlayerRank(rank);
        generatorModel.setPlayerName(playerPilotName);
        generatorModel.setService(newPlayerSquadron.determineServiceForSquadron(campaign.getDate()));
        
        SquadronMemberFactory squadronMemberFactory = new SquadronMemberFactory(campaign, newPlayerSquadron, newPlayerSquadronPersonnel);
        SquadronMember newPlayer = squadronMemberFactory.createPlayer(generatorModel);

        newPlayerSquadronPersonnel.addSquadronMember(newPlayer);
		return newPlayer;
	}

	private void removeAiSquadronMember(String rank, Squadron newPlayerSquadron, SquadronPersonnel newPlayerSquadronPersonnel) throws PWCGException 
	{
		AiPilotRemovalChooser pilotRemovalChooser = new AiPilotRemovalChooser(campaign);
        SquadronMember squadronMemberToReplace = pilotRemovalChooser.findAiPilotToRemove(rank, newPlayerSquadron.getSquadronId());
        newPlayerSquadronPersonnel.removeSquadronMember(squadronMemberToReplace);
	}

	private void createCoopPilot(SquadronMember newSquadronMewmber, String coopUser) throws PWCGException 
	{
        CoopPilot coopPilot = new CoopPilot();
        coopPilot.setCampaignName(campaign.getCampaignData().getName());
        coopPilot.setNote("Created by PWCG");
        coopPilot.setPilotName(newSquadronMewmber.getName());
        coopPilot.setPilotRank(newSquadronMewmber.getRank());
        coopPilot.setSerialNumber(newSquadronMewmber.getSerialNumber());
        coopPilot.setSquadronId(newSquadronMewmber.getSquadronId());
        coopPilot.setUsername(coopUser);
        coopPilot.setApproved(true);
	
        CoopPilotIOJson.writeJson(coopPilot);
	}
}
