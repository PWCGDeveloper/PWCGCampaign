package pwcg.campaign.squadmember;

import java.util.HashMap;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.CampaignValidatorMedals;
import pwcg.campaign.personnel.PilotPictureBuilder;
import pwcg.campaign.personnel.SquadronMemberAirInitialVictoryBuilder;
import pwcg.campaign.personnel.SquadronMemberFemaleConverter;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronMemberGroundInitialVictoryBuilder;
import pwcg.campaign.personnel.SquadronMemberStructureInitialVictoryBuilder;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;

public class SquadronMemberFactory
{
    private Campaign campaign;
    private Squadron squadron;
    private SquadronPersonnel squadronPersonnel;

    public SquadronMemberFactory (Campaign campaign, Squadron squadron, SquadronPersonnel squadronPersonnel)
    {
        this.campaign = campaign;
        this.squadron = squadron;
        this.squadronPersonnel = squadronPersonnel;
    }

    public SquadronMember createPlayer(CampaignGeneratorModel generatorModel) throws PWCGException 
    {
        SquadronMember player = new SquadronMember();

        player.setName(generatorModel.getPlayerName());
        player.setRank(generatorModel.getPlayerRank());
        ICountry country = CountryFactory.makeCountryByService(generatorModel.getService());
        player.setCountry(country.getCountry());
        player.setAiSkillLevel(AiSkillLevel.PLAYER);
        player.setPlayerRegion(generatorModel.getPlayerRegion());
        player.setPilotActiveStatus(SquadronMemberStatus.STATUS_ACTIVE, null, null);
        player.setSquadronId(squadron.getSquadronId());
        player.setSerialNumber(campaign.getSerialNumber().getLastPlayerSerialNumber());
        player.setInactiveDate(DateUtils.getEndOfWar());
        
        makePilotPicture(player);

        CampaignValidatorMedals medalFixer = new CampaignValidatorMedals(campaign);
        medalFixer.assignMissingMedalsForSquadMember(player);

        return player;
     }

    public SquadronMember createInitialAIPilot(String rank) throws PWCGException 
    {
        SquadronMember newPilot = new SquadronMember();
        
        HashMap<String, String> namesUsed = squadron.getNamesInUse(campaign);
        ArmedService service = squadron.determineServiceForSquadron(campaign.getDate());
        
        String squaddieName = PilotNames.getInstance().getName(squadron.determineServiceForSquadron(campaign.getDate()), namesUsed);
        
        newPilot.setName(squaddieName);
        newPilot.setRank(rank);
        newPilot.setCountry(squadron.getCountry().getCountry());
        newPilot.setSquadronId(squadron.getSquadronId());
        newPilot.setSerialNumber(campaign.getSerialNumber().getNextPilotSerialNumber());
        newPilot.setInactiveDate(DateUtils.getEndOfWar());

        makePilotPicture(newPilot);
                
        IRankHelper rankObj = RankFactory.createRankHelper();
        int rankPos = rankObj.getRankPosByService(rank, service);
        
        int numMissions = createMissionsFlown(rankPos);
        newPilot.setMissionFlown(numMissions);
        
        PwcgRoleCategory squadronPrimaryRoleCategory = squadron.determineSquadronPrimaryRoleCategory(campaign.getDate());
        if (AirVictimGenerator.shouldUse(squadronPrimaryRoleCategory))
        {
            SquadronMemberAirInitialVictoryBuilder initialVictoryBuilder = new SquadronMemberAirInitialVictoryBuilder(campaign, squadron);
            initialVictoryBuilder.createPilotVictories(newPilot, rankPos);            
        }
        else if (GroundVictimGenerator.shouldUse(squadronPrimaryRoleCategory))
        {
            SquadronMemberGroundInitialVictoryBuilder initialVictoryBuilder = new SquadronMemberGroundInitialVictoryBuilder(campaign, squadron);
            initialVictoryBuilder.createPilotVictories(newPilot, rankPos);            
        }
        else if (StructureVictimGenerator.shouldUse(squadronPrimaryRoleCategory))
        {
            SquadronMemberStructureInitialVictoryBuilder initialVictoryBuilder = new SquadronMemberStructureInitialVictoryBuilder(campaign, squadron);
            initialVictoryBuilder.createPilotVictories(newPilot, rankPos);            
        }
        
        PilotSkill pilotSkill = new PilotSkill(campaign);
        pilotSkill.advancePilotSkillForInitialCreation(newPilot, squadron);
        
        CampaignValidatorMedals medalFixer = new CampaignValidatorMedals(campaign);
        medalFixer.assignMissingMedalsForSquadMember(newPilot);
        
        newPilot = SquadronMemberFemaleConverter.possiblyConvertToFemale(service, newPilot, namesUsed);
        
        return newPilot;
    }

    private void makePilotPicture(SquadronMember newPilot) throws PWCGException
    {
        ArmedService service = squadron.determineServiceForSquadron(campaign.getDate());
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        PilotPictureBuilder pilotPictureBuilder = new PilotPictureBuilder(service, squadronMembers);
        String picPath = pilotPictureBuilder.assignPilotPicture();
        newPilot.setPicName(picPath);
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
