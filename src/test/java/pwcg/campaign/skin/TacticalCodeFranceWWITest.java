package pwcg.campaign.skin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.plane.PlaneMcu;

@ExtendWith(MockitoExtension.class)
public class TacticalCodeFranceWWITest
{
    @Mock Campaign campaign;
    @Mock Squadron squadron;
    @Mock PlaneMcu plane;
    @Mock ICountry country;
    @Mock Skin skin;
    
    @Test
    public void testValidTacticalCode() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.FRANCE);

        Mockito.when(squadron.getSquadronTacticalCodeColorOverride()).thenReturn(TacticalCodeColor.NONE);
        Mockito.when(plane.getAircraftIdCode()).thenReturn("12");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(skin.getTacticalCodeColor()).thenReturn(TacticalCodeColor.WHITE);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);
        Mockito.when(skin.getTacticalCodeType()).thenReturn(TacticalCodeType.CODE_POSITION_ONE_AND_TWO);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("12", tacticalCode.formCodeString());
        Assertions.assertEquals("11", tacticalCode.formCodeColorString());
    }
    
    @Test
    public void testValidTacticalCodeOneNumber() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.FRANCE);

        Mockito.when(squadron.getSquadronTacticalCodeColorOverride()).thenReturn(TacticalCodeColor.NONE);
        Mockito.when(plane.getAircraftIdCode()).thenReturn("1");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(skin.getTacticalCodeColor()).thenReturn(TacticalCodeColor.RED);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);
        Mockito.when(skin.getTacticalCodeType()).thenReturn(TacticalCodeType.CODE_POSITION_ONE_AND_TWO);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("%201", tacticalCode.formCodeString());
        Assertions.assertEquals("22", tacticalCode.formCodeColorString());
    }
    
    @Test
    public void testValidTacticalCodePosition1() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.FRANCE);

        Mockito.when(plane.getAircraftIdCode()).thenReturn("1");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);
        Mockito.when(skin.getTacticalCodeType()).thenReturn(TacticalCodeType.CODE_POSITION_ONE);

        PWCGException  exception = Assertions.assertThrows(PWCGException.class, () -> {
            TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        });
        Assertions.assertEquals("Invalid code type: WWI France does not support position 1", exception.getMessage());
    }
    
    @Test
    public void testValidTacticalCodePosition2() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.FRANCE);

        Mockito.when(plane.getAircraftIdCode()).thenReturn("1");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);
        Mockito.when(skin.getTacticalCodeType()).thenReturn(TacticalCodeType.CODE_POSITION_TWO);
        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertNull(tacticalCode);
    }
    
    @Test
    public void testValidTacticalCodePosition3() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.FRANCE);

        Mockito.when(plane.getAircraftIdCode()).thenReturn("1");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);
        Mockito.when(skin.getTacticalCodeType()).thenReturn(TacticalCodeType.CODE_POSITION_THREE);
        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertNull(tacticalCode);
    }
 }
