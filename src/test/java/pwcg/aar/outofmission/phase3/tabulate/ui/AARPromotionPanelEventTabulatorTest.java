package pwcg.aar.outofmission.phase3.tabulate.ui;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.AARTestSetup;
import pwcg.aar.tabulate.debrief.PromotionPanelEventTabulator;
import pwcg.aar.ui.display.model.AARPromotionPanelData;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AARPromotionPanelEventTabulatorTest extends AARTestSetup
{
    private Map<Integer, String> promotionsAwarded = new HashMap<>();
    
    @BeforeEach
    public void setupTest() throws PWCGException
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
