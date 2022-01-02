package pwcg.campaign;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.squadron.Company;
import pwcg.campaign.squadron.SquadronManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCacheBase;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
public class CampaignGeneratorTest
{
	public CampaignGeneratorTest() throws PWCGException
	{
    	PWCGContext.setProduct(PWCGProduct.FC);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.ARRAS_MAP);
	}
	
    @Test
    public void createWWICampaign () throws PWCGException
    {        
    	Campaign campaign = generateCampaign(SquadronTestProfile.ESC_3_PROFILE.getSquadronId(), DateUtils.getDateYYYYMMDD("19171001"));
    	assert(campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList().size() == 1);
        CrewMember player = campaign.findReferencePlayer();
        Assertions.assertTrue (player.determineSquadron().getSquadronId() == SquadronTestProfile.ESC_3_PROFILE.getSquadronId());
        Assertions.assertTrue (player.determineSquadron().determineSquadronCountry(campaign.getDate()).getCountry() == Country.FRANCE);
        Assertions.assertTrue (campaign.getCampaignData().getName().equals(CampaignCacheBase.TEST_CAMPAIGN_NAME));
        assert(campaign.getPersonnelManager().getAllSquadronPersonnel().size() > 30);
        
        for (CompanyPersonnel squadronPersonnel : campaign.getPersonnelManager().getAllSquadronPersonnel())
        {
            CrewMembers squadronMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
            assert(squadronMembers.getCrewMemberList().size() == Company.SQUADRON_STAFF_SIZE);
        }
        
        
        for (Equipment equipment : campaign.getEquipmentManager().getEquipmentAllSquadrons().values())
        {
            assert(equipment.getActiveEquippedPlanes().size() == Company.SQUADRON_EQUIPMENT_SIZE);
        }
    }

    public Campaign generateCampaign(
                    int squadronId,
                    Date campaignDate) throws PWCGException 
    {
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        
        Company squadron = squadronManager.getSquadron(squadronId);
        
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
