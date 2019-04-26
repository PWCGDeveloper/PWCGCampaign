package pwcg.aar.outofmission.phase2.resupply;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.resupply.ISquadronNeed;
import pwcg.campaign.resupply.ResupplySquadronChooser;
import pwcg.campaign.resupply.equipment.SquadronEquipmentNeed;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;

public class ResupplySquadronChooserTest
{
    @Mock private Campaign campaign;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        campaign = CampaignCache.makeCampaign(SquadrontTestProfile.JG_51_PROFILE_MOSCOW);
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
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        
        Squadron playerSquadron = squadronManager.getSquadron(20111051);
        SquadronEquipmentNeed playerSquadronEquipmentNeed = new SquadronEquipmentNeed(campaign, playerSquadron);
        playerSquadronEquipmentNeed.setPlanesNeeded(playerPlanesNeeded);
        needs.put(playerSquadron.getSquadronId(), playerSquadronEquipmentNeed);
        
        Squadron i_jg52 = squadronManager.getSquadron(20111052);
        SquadronEquipmentNeed i_jg52EquipmentNeed = new SquadronEquipmentNeed(campaign, i_jg52);
        i_jg52EquipmentNeed.setPlanesNeeded(i_jg52PlanesNeeded);
        needs.put(i_jg52.getSquadronId(), i_jg52EquipmentNeed);        
        
        Squadron ii_jg52 = squadronManager.getSquadron(20112052);
        SquadronEquipmentNeed ii_jg52EquipmentNeed = new SquadronEquipmentNeed(campaign, ii_jg52);
        ii_jg52EquipmentNeed.setPlanesNeeded(ii_jg52PlanesNeeded);
        needs.put(ii_jg52.getSquadronId(), ii_jg52EquipmentNeed);        
        
        return needs;
    }
}
