package pwcg.campaign.personnel;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberFactory;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.factory.RankFactory;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;

public class InitialCompanyStaffer 
{
    private Campaign campaign;
    private Company squadron;
    private CrewMemberFactory squadronMemberFactory;
    private CompanyPersonnel squadronPersonnel;
    
	public InitialCompanyStaffer(Campaign campaign, Company squadron) 
	{
        this.campaign = campaign;
        this.squadron = squadron;
        
        squadronPersonnel = new CompanyPersonnel(campaign, squadron);
        squadronMemberFactory = new CrewMemberFactory(campaign, squadron, squadronPersonnel);
	}

    public CompanyPersonnel generatePersonnel() throws PWCGException 
    {
        generateAICrewMembers();        
        return squadronPersonnel;
    }
    
    public void addPlayerToCampaign(CampaignGeneratorModel generatorModel) throws PWCGException
    {
        CrewMember player =  squadronMemberFactory.createPlayer(generatorModel);
        addCrewMember(player);
    }

    private void addCrewMember(CrewMember crewMember) throws PWCGException
    {
        if ((squadronPersonnel.getCrewMembersWithAces().getCrewMemberList().size()) < Company.COMPANY_STAFF_SIZE)
        {
            squadronPersonnel.addCrewMember(crewMember);
        }
    }

	private void generateAICrewMembers() throws PWCGException 
	{	 	
		addAiCrewMembers();
		validateMissionsFlownForInitialCrewMembers();
		setAiSkillLevel();
	}

    private void addAiCrewMembers() throws PWCGException
    {
        IRankHelper rankObj = RankFactory.createRankHelper();
        List<String> ranks = rankObj.getRanksByService(squadron.determineServiceForSquadron(campaign.getDate()));
        
        addNumAiCrewMembersAtRank(1, 0);
        
        addNumAiCrewMembersAtRank(2, 1);
        
        if (ranks.size() == 4)
        {
            addNumAiCrewMembersAtRank(5, 2);
            addNumAiCrewMembersAtRank(8, 3);
        }
        else if (ranks.size() == 5)
        {
            addNumAiCrewMembersAtRank(4, 2);
            addNumAiCrewMembersAtRank(5, 3);
            addNumAiCrewMembersAtRank(4, 4);
        }
        else
        {
            throw new PWCGException ("Unexpected number of ranks in service: " + ranks.size());
        }
    }
    
    private void addNumAiCrewMembersAtRank(int initialNumCrewMembers, int rankPos) throws PWCGException
    {
        int refinedNumCrewMembers = refineNumCrewMembersAtRank(initialNumCrewMembers, rankPos);
        
        IRankHelper rankObj = RankFactory.createRankHelper();
        List<String> ranks = rankObj.getRanksByService(squadron.determineServiceForSquadron(campaign.getDate()));

        for (int i = 0; i < refinedNumCrewMembers; ++i)
        {
            CrewMember crewMember =  squadronMemberFactory.createInitialAICrewMember (ranks.get(rankPos));
            addCrewMember(crewMember);
        }
    }

    private int refineNumCrewMembersAtRank(int numCrewMembers, int rankPos) throws PWCGException
    {
        CrewMembers squadronMembersAlreadyWithSquadron = CrewMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        for (CrewMember crewMember : squadronMembersAlreadyWithSquadron.getCrewMemberCollection().values())
        {
            IRankHelper rankObj = RankFactory.createRankHelper();
            int squadronMemberRankPos = rankObj.getRankPosByService(crewMember.getRank(), squadron.determineServiceForSquadron(campaign.getDate()));
            if (rankPos == squadronMemberRankPos)
            {
                --numCrewMembers;
            }
        }
        
        if (numCrewMembers < 0)
        {
            numCrewMembers = 0;
        }
        
        return numCrewMembers;
    }

    private void validateMissionsFlownForInitialCrewMembers() throws PWCGException
    {
        CrewMembers squadronMembers = CrewMemberFilter.filterActiveAINoWounded(squadronPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        for (CrewMember crewMember : squadronMembers.getCrewMemberList())
        {
            int minimumMissions = 1 + (crewMember.getCrewMemberVictories().getAirToAirVictoryCount() * 3);
            if (!crewMember.isPlayer())
            {
                if (crewMember.getBattlesFought() < minimumMissions)
                {
                    crewMember.setBattlesFought(minimumMissions);
                }
            }
        }
    }

    private void setAiSkillLevel() throws PWCGException
    {
        CrewMembers squadronMembers = CrewMemberFilter.filterActiveAINoWounded(squadronPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        for (CrewMember crewMember : squadronMembers.getCrewMemberList())
        {
            AiCrewMemberSkillGenerator aiCrewMemberSkillGenerator = new AiCrewMemberSkillGenerator();
            AiSkillLevel aiSkillLevel = aiCrewMemberSkillGenerator.calculateCrewMemberQualityByRankAndService(campaign, squadron, crewMember.getRank());
            crewMember.setAiSkillLevel(aiSkillLevel);
        }
    }

}
