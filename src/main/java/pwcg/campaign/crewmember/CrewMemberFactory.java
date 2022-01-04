package pwcg.campaign.crewmember;

import java.util.HashMap;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.company.Company;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.CampaignValidatorMedals;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFemaleConverter;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.personnel.CrewMemberGroundInitialVictoryBuilder;
import pwcg.campaign.personnel.CrewMemberPictureBuilder;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;

public class CrewMemberFactory
{
    private Campaign campaign;
    private Company company;
    private CompanyPersonnel companyPersonnel;

    public CrewMemberFactory (Campaign campaign, Company company, CompanyPersonnel companyPersonnel)
    {
        this.campaign = campaign;
        this.company = company;
        this.companyPersonnel = companyPersonnel;
    }

    public CrewMember createPlayer(CampaignGeneratorModel generatorModel) throws PWCGException 
    {
        CrewMember player = new CrewMember();

        player.setName(generatorModel.getPlayerName());
        player.setRank(generatorModel.getPlayerRank());
        ICountry country = CountryFactory.makeCountryByService(generatorModel.getService());
        player.setCountry(country.getCountry());
        player.setAiSkillLevel(AiSkillLevel.PLAYER);
        player.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_ACTIVE, null, null);
        player.setSquadronId(company.getCompanyId());
        player.setSerialNumber(campaign.getSerialNumber().getLastPlayerSerialNumber());
        player.setInactiveDate(DateUtils.getEndOfWar());
        
        makeCrewMemberPicture(player);

        CampaignValidatorMedals medalFixer = new CampaignValidatorMedals(campaign);
        medalFixer.assignMissingMedalsForSquadMember(player);

        return player;
     }

    public CrewMember createInitialAICrewMember(String rank) throws PWCGException 
    {
        CrewMember newCrewMember = new CrewMember();
        
        HashMap<String, String> namesUsed = company.getNamesInUse(campaign);
        ArmedService service = company.determineServiceForSquadron(campaign.getDate());
        
        String squaddieName = CrewMemberNames.getInstance().getName(company.determineServiceForSquadron(campaign.getDate()), namesUsed);
        
        newCrewMember.setName(squaddieName);
        newCrewMember.setRank(rank);
        newCrewMember.setCountry(company.getCountry().getCountry());
        newCrewMember.setSquadronId(company.getCompanyId());
        newCrewMember.setSerialNumber(campaign.getSerialNumber().getNextCrewMemberSerialNumber());
        newCrewMember.setInactiveDate(DateUtils.getEndOfWar());

        makeCrewMemberPicture(newCrewMember);
                
        IRankHelper rankObj = RankFactory.createRankHelper();
        int rankPos = rankObj.getRankPosByService(rank, service);
        
        int numMissions = createMissionsFlown(rankPos);
        newCrewMember.setBattlesFought(numMissions);
        
        PwcgRoleCategory companyPrimaryRoleCategory = company.determineSquadronPrimaryRoleCategory(campaign.getDate());
        if (GroundVictimGenerator.shouldUse(companyPrimaryRoleCategory))
        {
            CrewMemberGroundInitialVictoryBuilder initialVictoryBuilder = new CrewMemberGroundInitialVictoryBuilder(campaign, company);
            initialVictoryBuilder.createCrewMemberVictories(newCrewMember, rankPos);            
        }
        
        CrewMemberSkill crewMemberSkill = new CrewMemberSkill(campaign);
        crewMemberSkill.advanceCrewMemberSkillForInitialCreation(newCrewMember, company);
        
        CampaignValidatorMedals medalFixer = new CampaignValidatorMedals(campaign);
        medalFixer.assignMissingMedalsForSquadMember(newCrewMember);
        
        newCrewMember = CrewMemberFemaleConverter.possiblyConvertToFemale(service, newCrewMember, namesUsed);
        
        return newCrewMember;
    }

    private void makeCrewMemberPicture(CrewMember newCrewMember) throws PWCGException
    {
        ArmedService service = company.determineServiceForSquadron(campaign.getDate());
        CrewMembers companyMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(companyPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        CrewMemberPictureBuilder crewMemberPictureBuilder = new CrewMemberPictureBuilder(service, companyMembers);
        String picPath = crewMemberPictureBuilder.assignCrewMemberPicture();
        newCrewMember.setPicName(picPath);
    }

    private int createMissionsFlown(int rankPos)
    {
        int missionFactorForRank = rankPos;
        if (missionFactorForRank > 3)
        {
            missionFactorForRank = 3;   
        }
        missionFactorForRank = 3 - missionFactorForRank;
        
        int numMissions = (missionFactorForRank * 10) + RandomNumberGenerator.getRandom((missionFactorForRank + 1) * 10);
        return numMissions;
    }
}
