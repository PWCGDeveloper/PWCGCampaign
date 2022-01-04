package pwcg.campaign.personnel;

import java.util.HashMap;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberNames;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.factory.RankFactory;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;

public class CrewMemberReplacementFactory
{
    private Campaign campaign;
    private ArmedService service;

    public CrewMemberReplacementFactory (Campaign campaign, ArmedService service)
    {
        this.campaign = campaign;
        this.service = service;
    }

    public CrewMember createAIReplacementCrewMember() throws PWCGException 
    {
        CrewMember replacementCrewMember = new CrewMember();
        
        HashMap<String, String> namesUsed = new HashMap<>();
        
        NewCrewMemberRankDetermination rankDetermination = new NewCrewMemberRankDetermination();
        String rank = rankDetermination.getReplacementCrewMemberRank(service);
        
        AiSkillLevel aiSkillLevel = determineAiSkillLevel(rank);
        
        String squaddieName = CrewMemberNames.getInstance().getName(service, namesUsed);
        replacementCrewMember.setName(squaddieName);
        replacementCrewMember.setRank(rank);
        replacementCrewMember.setCountry(service.getCountry().getCountry());
        replacementCrewMember.setSquadronId(Company.REPLACEMENT);
        replacementCrewMember.setSerialNumber(campaign.getSerialNumber().getNextCrewMemberSerialNumber());
        replacementCrewMember.setBattlesFought(0);        
        replacementCrewMember.setAiSkillLevel(aiSkillLevel);
        makeCrewMemberPicture(replacementCrewMember);
        
        replacementCrewMember = CrewMemberFemaleConverter.possiblyConvertToFemale(service, replacementCrewMember, namesUsed);

        return replacementCrewMember;
    }

    private AiSkillLevel determineAiSkillLevel(String rank)
    {
        IRankHelper rankHelper = RankFactory.createRankHelper();
        int rankPos = rankHelper.getRankPosByService(rank, service);
        if (rankPos > 2) 
        {
            return service.getServiceQuality().getQuality(campaign.getDate()).getNewAiSkillLevelForQuality();
        }
        
        return AiSkillLevel.COMMON;
    }

    private void makeCrewMemberPicture(CrewMember newCrewMember) throws PWCGException
    {
        CrewMemberPictureBuilder crewMemberPictureBuilder = new CrewMemberPictureBuilder(service, new CrewMembers());
        String picPath = crewMemberPictureBuilder.assignCrewMemberPicture();
        newCrewMember.setPicName(picPath);
    }

    public static CrewMember createBlankCrewMember()
    {
        CrewMember crewMember = new CrewMember();
        return crewMember;
    }

}
