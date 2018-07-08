package pwcg.campaign.personnel;

import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberFactory;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;

public class InitialSquadronStaffer 
{
    private Campaign campaign;
    private Squadron squadron;
    private SquadronMemberFactory squadronMemberFactory;
    private SquadronPersonnel squadronPersonnel;
    private int pilotsNeeded = Squadron.SQUADRON_STAFF_SIZE;
    
	public InitialSquadronStaffer(Campaign campaign, Squadron squadron) 
	{
        this.campaign = campaign;
        this.squadron = squadron;
        
        squadronPersonnel = new SquadronPersonnel(campaign, squadron);
        squadronMemberFactory = new SquadronMemberFactory(campaign, squadron, squadronPersonnel);
	}

    public SquadronPersonnel generatePersonnel() throws PWCGException 
    {
        calculatePilotsNeeded();
        generateAIPilots();        
        return squadronPersonnel;
    }
    
    public void addPlayerToCampaign(CampaignGeneratorModel generatorModel) throws PWCGException
    {
        SquadronMember player =  squadronMemberFactory.createPlayer(generatorModel);
        addSquadronMember(player);
    }

    private void addSquadronMember(SquadronMember player) throws PWCGException
    {
        if (squadronPersonnel.getActiveSquadronMembers().getActiveCount(campaign.getDate()) < pilotsNeeded)
        {
            squadronPersonnel.addSquadronMember(player);
        }
    }

	private void generateAIPilots() throws PWCGException 
	{	 	
		addAiPilots();
		validateMissionsFlownForInitialPilots();
		setAiSkillLevel();
	}
    
    private void calculatePilotsNeeded() throws PWCGException
    {
        List<Ace> aces = campaign.getPersonnelManager().getCampaignAces().getCampaignAcesBySquadron(squadron.getSquadronId());
        pilotsNeeded -= aces.size();
    }


    private void addAiPilots() throws PWCGException
    {
        IRankHelper rankObj = RankFactory.createRankHelper();
        List<String> ranks = rankObj.getRanksByService(squadron.determineServiceForSquadron(campaign.getDate()));
        
        addNumAiPilotsAtRank(1, 0);
        
        addNumAiPilotsAtRank(2, 1);
        
        if (ranks.size() == 4)
        {
            addNumAiPilotsAtRank(4, 2);
            addNumAiPilotsAtRank(5, 3);
        }
        else if (ranks.size() == 5)
        {
            addNumAiPilotsAtRank(4, 2);
            addNumAiPilotsAtRank(3, 3);
            addNumAiPilotsAtRank(2, 4);
        }
        else
        {
            throw new PWCGException ("Unexpected number of ranks in service: " + ranks.size());
        }
    }
    
    private void addNumAiPilotsAtRank(int initialNumPilots, int rankPos) throws PWCGException
    {
        int refinedNumPilots = refineNumPilotsAtRank(initialNumPilots, rankPos);
        
        IRankHelper rankObj = RankFactory.createRankHelper();
        List<String> ranks = rankObj.getRanksByService(squadron.determineServiceForSquadron(campaign.getDate()));

        for (int i = 0; i < refinedNumPilots; ++i)
        {
            SquadronMember pilot =  squadronMemberFactory.createAIPilot (ranks.get(rankPos));
            addSquadronMember(pilot);
        }
    }

    private int refineNumPilotsAtRank(int numPilots, int rankPos) throws PWCGException
    {
        SquadronMembers squadronMembersAlreadyWithSquadron = squadronPersonnel.getActiveSquadronMembersWithAces();
        for (SquadronMember squadronMember : squadronMembersAlreadyWithSquadron.getSquadronMemberCollection().values())
        {
            IRankHelper rankObj = RankFactory.createRankHelper();
            int squadronMemberRankPos = rankObj.getRankPosByService(squadronMember.getRank(), squadron.determineServiceForSquadron(campaign.getDate()));
            if (rankPos == squadronMemberRankPos)
            {
                --numPilots;
            }
        }
        
        if (numPilots < 0)
        {
            numPilots = 0;
        }
        
        return numPilots;
    }

    private void validateMissionsFlownForInitialPilots() throws PWCGException
    {
        Map<Integer, SquadronMember> squadronMembers = SquadronMemberFilter.filterActiveAI(squadronPersonnel.getActiveSquadronMembers().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : squadronMembers.values())
        {
            int minimumMissions = 1 + (squadronMember.getSquadronMemberVictories().getAirToAirVictories() * 3);
            if (!squadronMember.isPlayer())
            {
                if (squadronMember.getMissionFlown() < minimumMissions)
                {
                    squadronMember.setMissionFlown(minimumMissions);
                }
            }
        }
    }

    private void setAiSkillLevel() throws PWCGException
    {
        Map<Integer, SquadronMember> squadronMembers = SquadronMemberFilter.filterActiveAI(squadronPersonnel.getActiveSquadronMembers().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : squadronMembers.values())
        {
            AiPilotSkillGenerator aiPilotSkillGenerator = new AiPilotSkillGenerator();
            AiSkillLevel aiSkillLevel = aiPilotSkillGenerator.calculatePilotQualityByRankAndService(campaign, squadron, squadronMember.getRank());
            squadronMember.setAiSkillLevel(aiSkillLevel);
        }
    }

}
