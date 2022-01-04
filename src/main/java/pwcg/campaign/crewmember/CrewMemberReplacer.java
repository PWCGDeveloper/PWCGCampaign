package pwcg.campaign.crewmember;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.coop.CoopUserManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;

public class CrewMemberReplacer  implements ICrewMemberReplacer
{
    protected Campaign campaign;
    
    public CrewMemberReplacer(Campaign campaign)
    {
        this.campaign = campaign;
    }
	
    public CrewMember createPersona(String playerCrewMemberName, String rank, String squadronName, String coopUser) throws PWCGUserException, Exception
    {        
        Company newPlayerSquadron = getNewPlayerSquadron(squadronName);
    	CompanyPersonnel newPlayerSquadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(newPlayerSquadron.getCompanyId());

        CrewMember newSquadronMewmber = addnewCrewMemberToCampaign(playerCrewMemberName, rank, newPlayerSquadron, newPlayerSquadronPersonnel);        
        removeAiCrewMember(rank, newPlayerSquadron, newPlayerSquadronPersonnel);
        
        if (campaign.isCoop())
        {
            CoopUserManager.getIntance().createCoopPersona(campaign.getName(), newSquadronMewmber, coopUser);
        }
        
        return newSquadronMewmber;
    }

	private Company getNewPlayerSquadron(String squadronName) throws PWCGException 
	{
		CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        Company newPlayerSquadron = squadronManager.getCompanyByName(squadronName, campaign.getDate());
		return newPlayerSquadron;
	}

	private CrewMember addnewCrewMemberToCampaign(String playerCrewMemberName, String rank, Company newPlayerSquadron,
			CompanyPersonnel newPlayerSquadronPersonnel) throws PWCGException 
	{
		CampaignGeneratorModel generatorModel = new CampaignGeneratorModel();
        generatorModel.setPlayerRank(rank);
        generatorModel.setPlayerName(playerCrewMemberName);
        generatorModel.setService(newPlayerSquadron.determineServiceForSquadron(campaign.getDate()));
        generatorModel.setCampaignName(campaign.getCampaignData().getName());
        generatorModel.setCampaignDate(campaign.getDate());
        generatorModel.setSquadronName(newPlayerSquadron.determineDisplayName(campaign.getDate()));
        generatorModel.validateCampaignInputs();
        
        CrewMemberFactory squadronMemberFactory = new CrewMemberFactory(campaign, newPlayerSquadron, newPlayerSquadronPersonnel);
        CrewMember newPlayer = squadronMemberFactory.createPlayer(generatorModel);

        newPlayerSquadronPersonnel.addCrewMember(newPlayer);
		return newPlayer;
	}

	private void removeAiCrewMember(String rank, Company newPlayerSquadron, CompanyPersonnel newPlayerSquadronPersonnel) throws PWCGException 
	{
		AiCrewMemberRemovalChooser crewMemberRemovalChooser = new AiCrewMemberRemovalChooser(campaign);
        CrewMember squadronMemberToReplace = crewMemberRemovalChooser.findAiCrewMemberToRemove(rank, newPlayerSquadron.getCompanyId());
        newPlayerSquadronPersonnel.removeCrewMember(squadronMemberToReplace);
	}
}
