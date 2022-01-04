package pwcg.aar.outofmission.phase2.resupply;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.resupply.ISquadronNeed;
import pwcg.campaign.resupply.ResupplySquadronChooser;
import pwcg.campaign.resupply.equipment.SquadronEquipmentNeed;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResupplySquadronChooserTest
{
    private Campaign campaign;
    
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
    }

    @Test
    public void testChoosePlayerSquadronWhenDepleted() throws PWCGException
    {
        Map<Integer, ISquadronNeed> needs = getSquadronNeeds(7, 9, 2);
        ResupplySquadronChooser resupplySquadronChooser = new ResupplySquadronChooser(campaign, needs);
        for (int i = 0; i < 3; ++i)
        {
            ISquadronNeed selectedSquadronNeed = resupplySquadronChooser.getNeedySquadron();
            selectedSquadronNeed.noteResupply();
            assert(selectedSquadronNeed.getSquadronId() == 20111051);
        }
    }

    @Test
    public void testChoosePlayerSquadronUntilDepletedNotTrue() throws PWCGException
    {
        Map<Integer, ISquadronNeed> needs = getSquadronNeeds(6, 9, 2);
        ResupplySquadronChooser resupplySquadronChooser = new ResupplySquadronChooser(campaign, needs);
        for (int i = 0; i < 3; ++i)
        {
            ISquadronNeed selectedSquadronNeed = resupplySquadronChooser.getNeedySquadron();
            selectedSquadronNeed.noteResupply();
            if (i < 2)
            {
                assert(selectedSquadronNeed.getSquadronId() == 20111051);
            }
            else
            {
                assert(selectedSquadronNeed.getSquadronId() == 20111052);
            }
        }
    }

    @Test
    public void testChooseSquadronsUntilNoMoreReplacementsNeeded() throws PWCGException
    {
        Map<Integer, ISquadronNeed> needs = getSquadronNeeds(3, 2, 1);
        ResupplySquadronChooser resupplySquadronChooser = new ResupplySquadronChooser(campaign, needs);
        
        int i_jg51Count = 0;
        int i_jg52Count = 0;
        int ii_jg52Count = 0;
        for (int i = 0; i < 6; ++i)
        {
            ISquadronNeed selectedSquadronNeed = resupplySquadronChooser.getNeedySquadron();
            selectedSquadronNeed.noteResupply();
            if (selectedSquadronNeed.getSquadronId() == 20111051)
            {
                ++i_jg51Count;
            }
            else if (selectedSquadronNeed.getSquadronId() == 20111052)
            {
                ++i_jg52Count;
            }
            else if (selectedSquadronNeed.getSquadronId() == 20112052)
            {
                ++ii_jg52Count;
            }
        }

        assert(i_jg51Count == 3);
        assert(i_jg52Count == 2);
        assert(ii_jg52Count == 1);
        
        ISquadronNeed selectedSquadronNeed = resupplySquadronChooser.getNeedySquadron();
        assert(selectedSquadronNeed == null);        
    }

    private Map<Integer, ISquadronNeed> getSquadronNeeds(int playerPlanesNeeded, int i_jg52PlanesNeeded, int ii_jg52PlanesNeeded) throws PWCGException
    {
        Map<Integer, ISquadronNeed> needs = new HashMap<>();
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        
        Company playerSquadron = squadronManager.getCompany(20111051);
        SquadronEquipmentNeed playerSquadronEquipmentNeed = new SquadronEquipmentNeed(campaign, playerSquadron);
        playerSquadronEquipmentNeed.setPlanesNeeded(playerPlanesNeeded);
        needs.put(playerSquadron.getCompanyId(), playerSquadronEquipmentNeed);
        
        Company i_jg52 = squadronManager.getCompany(20111052);
        SquadronEquipmentNeed i_jg52EquipmentNeed = new SquadronEquipmentNeed(campaign, i_jg52);
        i_jg52EquipmentNeed.setPlanesNeeded(i_jg52PlanesNeeded);
        needs.put(i_jg52.getCompanyId(), i_jg52EquipmentNeed);        
        
        Company ii_jg52 = squadronManager.getCompany(20112052);
        SquadronEquipmentNeed ii_jg52EquipmentNeed = new SquadronEquipmentNeed(campaign, ii_jg52);
        ii_jg52EquipmentNeed.setPlanesNeeded(ii_jg52PlanesNeeded);
        needs.put(ii_jg52.getCompanyId(), ii_jg52EquipmentNeed);        
        
        return needs;
    }
}
