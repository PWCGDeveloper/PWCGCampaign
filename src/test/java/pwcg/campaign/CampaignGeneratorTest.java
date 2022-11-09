package pwcg.campaign;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
public class CampaignGeneratorTest
{
    private static final String TEST_CAMPAIGN_NAME = "CampaignGeneratorTest Campaign";
    private static final String TEST_PLAYER_NAME = "CampaignGeneratorTest Player";
	
    @Test
    public void createWWICampaign () throws PWCGException
    {        
    	Campaign campaign = generateCampaign(SquadronTestProfile.ESC_3_PROFILE.getSquadronId(), DateUtils.getDateYYYYMMDD("19171001"));
    	assert(campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList().size() == 1);
        SquadronMember player = campaign.findReferencePlayer();
        Assertions.assertTrue (player.determineSquadron().getSquadronId() == SquadronTestProfile.ESC_3_PROFILE.getSquadronId());
        Assertions.assertTrue (player.determineSquadron().determineSquadronCountry(campaign.getDate()).getCountry() == Country.FRANCE);
        Assertions.assertTrue (campaign.getCampaignData().getName().equals(TEST_CAMPAIGN_NAME));
        assert(campaign.getPersonnelManager().getAllSquadronPersonnel().size() > 30);
        
        for (SquadronPersonnel squadronPersonnel : campaign.getPersonnelManager().getAllSquadronPersonnel())
        {
            SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
            assert(squadronMembers.getSquadronMemberList().size() == Squadron.SQUADRON_STAFF_SIZE);
        }
        
        
        for (Equipment equipment : campaign.getEquipmentManager().getEquipmentAllSquadrons().values())
        {
            assert(equipment.getActiveEquippedPlanes().size() == Squadron.SQUADRON_EQUIPMENT_SIZE);
        }
    }

    private Campaign generateCampaign(
                    int squadronId,
                    Date campaignDate) throws PWCGException 
    {
        SquadronManager squadronManager = PWCGContext.getInstance(PWCGProduct.FC).getSquadronManager();
        
        Squadron squadron = squadronManager.getSquadron(squadronId);
        
        ArmedService service = squadron.determineServiceForSquadron(campaignDate);
        String squadronName = squadron.determineDisplayName(campaignDate);
        
        IRankHelper rank = RankFactory.createRankHelper();
        String rankName = rank.getRankByService(2, service);

        CampaignGeneratorModel generatorModel = new CampaignGeneratorModel(PWCGProduct.FC);
        generatorModel.setCampaignDate(campaignDate);
        generatorModel.setCampaignName(TEST_CAMPAIGN_NAME);
        generatorModel.setPlayerName(TEST_PLAYER_NAME);
        generatorModel.setPlayerRank(rankName);
        generatorModel.setPlayerRegion("");
        generatorModel.setService(service);
        generatorModel.setSquadronName(squadronName);
        
        CampaignGenerator generator = new CampaignGenerator(generatorModel);
        Campaign campaign = generator.generate(PWCGProduct.FC);          
        
        return campaign;
    }


}
