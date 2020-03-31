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
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronMemberInitialVictoryBuilder;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.Role;
import pwcg.campaign.plane.RoleCategory;
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

    public SquadronMember createAIPilot(String rank) throws PWCGException 
    {
        SquadronMember newPilot = new SquadronMember();
        
        HashMap<String, String> namesUsed = getNamesInUse();
        
        String squaddieName = createPilotName(rank, namesUsed);
        
        newPilot.setName(squaddieName);
        newPilot.setRank(rank);
        newPilot.setCountry(squadron.getCountry().getCountry());
        newPilot.setSquadronId(squadron.getSquadronId());
        newPilot.setSerialNumber(campaign.getSerialNumber().getNextPilotSerialNumber());
        newPilot.setInactiveDate(DateUtils.getEndOfWar());

        makePilotPicture(newPilot);
                
        IRankHelper rankObj = RankFactory.createRankHelper();
        int rankPos = rankObj.getRankPosByService(rank, squadron.determineServiceForSquadron(campaign.getDate()));
        
        int numMissions = createMissionsFlown(rankPos);
        newPilot.setMissionFlown(numMissions);
        
        Role squadronPrimaryRole = squadron.determineSquadronPrimaryRole(campaign.getDate());
        if (squadronPrimaryRole.isRoleCategory(RoleCategory.FIGHTER))
        {
            SquadronMemberInitialVictoryBuilder initialVictoryBuilder = new SquadronMemberInitialVictoryBuilder(campaign, squadron);
            initialVictoryBuilder.createPilotVictories(newPilot, rankPos);            
        }
        
        PilotSkill pilotSkill = new PilotSkill(campaign);
        pilotSkill.advanceSkillofPilot(newPilot, squadron);
        
        CampaignValidatorMedals medalFixer = new CampaignValidatorMedals(campaign);
        medalFixer.assignMissingMedalsForSquadMember(newPilot);
        
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

    private HashMap<String, String> getNamesInUse() throws PWCGException
    {
        HashMap<String, String> namesUsed = new HashMap <String, String>();
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadronMember : squadronMembers.getSquadronMemberList())
        {
            int index = squadronMember.getName().indexOf(" ");
            String lastName = squadronMember.getName().substring(index + 1);
            namesUsed.put(lastName, lastName);
        }
        return namesUsed;
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

    private String createPilotName(String rank, HashMap<String, String> namesUsed) throws PWCGException 
    {
        String squaddieName = PilotNames.getInstance().getName(squadron.determineServiceForSquadron(campaign.getDate()), namesUsed);
        return squaddieName;
    }
}
