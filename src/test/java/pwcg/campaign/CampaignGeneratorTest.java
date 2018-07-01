package pwcg.campaign;

import java.util.Date;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.CampaignPersonnelFilter;
import pwcg.campaign.personnel.SquadronMemberFilterSpecification;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.CampaignCacheRoF;

@RunWith(MockitoJUnitRunner.class)
public class CampaignGeneratorTest
{

	@Before
	public void setup() throws PWCGException
	{
    	PWCGContextManager.setRoF(true);
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.FRANCE_MAP);
	}
	
    @Test
    public void createWWICampaign () throws PWCGException
    {        
    	Campaign campaign = generateCampaign(101003, DateUtils.getDateYYYYMMDD("19161001"));
        assert (campaign.getCampaignData().getSquadId() == 101003);
        assert (campaign.determineCountry().getCountry() == Country.FRANCE);
        assert (campaign.getName().equals(CampaignCacheRoF.TEST_CAMPAIGN_NAME));
        assert(campaign.getPersonnelManager().getAllSquadronPersonnel().size() > 100);
        
        for (SquadronPersonnel squadronPersonnel : campaign.getPersonnelManager().getAllSquadronPersonnel())
        {
            SquadronMemberFilterSpecification filterSpecification = new SquadronMemberFilterSpecification();
            filterSpecification.setIncludePlayer(true);                
            filterSpecification.setIncludeAces(true);     
            filterSpecification.setSpecifySquadron(squadronPersonnel.getSquadron().getSquadronId());

            CampaignPersonnelFilter filter = new CampaignPersonnelFilter(squadronPersonnel.getActiveSquadronMembersWithAces().getSquadronMembers());
            Map<Integer, SquadronMember> squadronMembers = filter.getFilteredSquadronMembers(filterSpecification);

            if (squadronMembers.size() != Squadron.SQUADRON_STAFF_SIZE)
            {
                squadronMembers = filter.getFilteredSquadronMembers(filterSpecification);
            }
            
            assert(squadronMembers.size() == Squadron.SQUADRON_STAFF_SIZE);
        }
        
        
        for (Equipment equipment : campaign.getEquipmentManager().getEquipmentAllSquadrons().values())
        {
            assert(equipment.getEquippedPlanes().size() == Squadron.SQUADRON_EQUIPMENT_SIZE);
        }
    }

    public Campaign generateCampaign(
                    int squadronId,
                    Date campaignDate) throws PWCGException 
    {
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        
        Squadron squadron = squadronManager.getSquadron(squadronId);
        
        ArmedService service = squadron.determineServiceForSquadron(campaignDate);
        String squadronName = squadron.determineDisplayName(campaignDate);
        
        IRankHelper rank = RankFactory.createRankHelper();
        String rankName = rank.getRankByService(2, service);

        CampaignGeneratorModel generatorModel = new CampaignGeneratorModel();
        generatorModel.setCampaignDate(campaignDate);
        generatorModel.setPlayerName(CampaignCacheRoF.TEST_CAMPAIGN_NAME);
        generatorModel.setPlayerRank(rankName);
        generatorModel.setPlayerRegion("");
        generatorModel.setService(service);
        generatorModel.setSquadronName(squadronName);
        
        CampaignGenerator generator = new CampaignGenerator(generatorModel);
        Campaign campaign = generator.generate();          

        PWCGContextManager.getInstance().setCampaign(campaign);
        
        return campaign;
    }


}
