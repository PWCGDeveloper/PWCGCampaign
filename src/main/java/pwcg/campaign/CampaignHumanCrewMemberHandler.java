package pwcg.campaign;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberFactory;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.core.exception.PWCGException;

public class CampaignHumanCrewMemberHandler
{
    private Campaign campaign;
    
    public CampaignHumanCrewMemberHandler(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public int addNewCrewMember(String humanCrewMemberName, String humanCrewMemberRank, int crewMemberToReplaceSerialNumber, int squadronId) throws PWCGException
    {
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(squadronId);
        CompanyPersonnel squadronSquadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(squadronId);

        CrewMember newPlayer = addHumanCrewMember(humanCrewMemberName, humanCrewMemberRank, squadron, squadronSquadronPersonnel);
        removeAiCrewMember(crewMemberToReplaceSerialNumber, squadronSquadronPersonnel);
        
        return newPlayer.getSerialNumber();
    }

    private CrewMember addHumanCrewMember(String humanCrewMemberName, String humanCrewMemberRank, Company squadron, CompanyPersonnel playerSquadronPersonnel) throws PWCGException
    {
        CampaignGeneratorModel generatorModel = new CampaignGeneratorModel();
        generatorModel.setPlayerRank(humanCrewMemberRank);
        generatorModel.setPlayerName(humanCrewMemberName);
        generatorModel.setService(squadron.determineServiceForSquadron(campaign.getDate()));

        CrewMemberFactory squadronMemberFactory = new CrewMemberFactory(campaign, squadron, playerSquadronPersonnel);
        CrewMember newPlayer = squadronMemberFactory.createPlayer(generatorModel);
        playerSquadronPersonnel.addCrewMember(newPlayer);
        return newPlayer;
    }

    private void removeAiCrewMember(int crewMemberToReplaceSerialNumber, CompanyPersonnel playerSquadronPersonnel) throws PWCGException
    {
        CrewMember aiToRemove = playerSquadronPersonnel.getCrewMember(crewMemberToReplaceSerialNumber);
        if (aiToRemove != null)
        {
            playerSquadronPersonnel.removeCrewMember(aiToRemove);
        }
    }
}
