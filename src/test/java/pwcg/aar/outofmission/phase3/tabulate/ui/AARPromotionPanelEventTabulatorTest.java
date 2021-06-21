package pwcg.aar.outofmission.phase3.tabulate.ui;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.AARTestSetup;
import pwcg.aar.tabulate.debrief.PromotionPanelEventTabulator;
import pwcg.aar.ui.display.model.AARPromotionPanelData;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.Silent.class) 
public class AARPromotionPanelEventTabulatorTest extends AARTestSetup
{
    private Map<Integer, String> promotionsAwarded = new HashMap<>();
    
    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        setupAARMocks();

        promotionsAwarded.clear();        
    }

    @Test
    public void testPromotionsAwardedCombined() throws PWCGException 
    {
        promotionsAwarded.put(SerialNumber.AI_STARTING_SERIAL_NUMBER+1, "Sergent");
        promotionsAwarded.put(SerialNumber.AI_STARTING_SERIAL_NUMBER+2,"Lieutenant");
        Mockito.when(personnelAwards.getPromotions()).thenReturn(promotionsAwarded);

        PromotionPanelEventTabulator promotionPanelEventTabulator = new PromotionPanelEventTabulator(campaign, aarContext);
        AARPromotionPanelData promotionPanelData = promotionPanelEventTabulator.tabulateForAARPromotionPanel();
        
        assert(promotionPanelData.getPromotionEventsDuringElapsedTime().size() == 2);
    }
}
