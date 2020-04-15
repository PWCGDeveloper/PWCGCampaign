package pwcg.campaign;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCacheBase;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class CampaignGeneratorTest
{

	@Before
	public void setup() throws PWCGException
	{
    	PWCGContext.setProduct(PWCGProduct.FC);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.ARRAS_MAP);
	}
	
    @Test
    public void createWWICampaign () throws PWCGException
    {        
    	Campaign campaign = generateCampaign(SquadronTestProfile.ESC_3_PROFILE.getSquadronId(), DateUtils.getDateYYYYMMDD("19171001"));
    	assert(campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList().size() == 1);
        SquadronMember player = campaign.findReferencePlayer();
        assert (player.determineSquadron().getSquadronId() == SquadronTestProfile.ESC_3_PROFILE.getSquadronId());
        assert (player.determineSquadron().determineSquadronCountry(campaign.getDate()).getCountry() == Country.FRANCE);
        assert (campaign.getCampaignData().getName().equals(CampaignCacheBase.TEST_CAMPAIGN_NAME));
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

    public Campaign generateCampaign(
                    int squadronId,
                    Date campaignDate) throws PWCGException 
    {
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        
        Squadron squadron = squadronManager.getSquadron(squadronId);
        
        ArmedService service = squadron.determineServiceForSquadron(campaignDate);
        String squadronName = squadron.determineDisplayName(campaignDate);
        
        IRankHelper rank = RankFactory.createRankHelper();
        String rankName = rank.getRankByService(2, service);

        CampaignGeneratorModel generatorModel = new CampaignGeneratorModel();
        generatorModel.setCampaignDate(campaignDate);
        generatorModel.setCampaignName(CampaignCacheBase.TEST_CAMPAIGN_NAME);
        generatorModel.setPlayerName(CampaignCacheBase.TEST_PLAYER_NAME);
        generatorModel.setPlayerRank(rankName);
        generatorModel.setPlayerRegion("");
        generatorModel.setService(service);
        generatorModel.setSquadronName(squadronName);
        
        CampaignGenerator generator = new CampaignGenerator(generatorModel);
        Campaign campaign = generator.generate();          

        PWCGContext.getInstance().setCampaign(campaign);
        
        return campaign;
    }


}
