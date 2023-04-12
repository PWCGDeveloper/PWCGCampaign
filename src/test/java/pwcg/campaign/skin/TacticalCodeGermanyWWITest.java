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
public class TacticalCodeGermanyWWITest
{
    @Mock Campaign campaign;
    @Mock Squadron squadron;
    @Mock PlaneMcu plane;
    @Mock ICountry country;
    @Mock Skin skin;

    @Test
    public void testValidTacticalCodePosition1() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.GERMANY);

        Mockito.when(squadron.getSquadronTacticalCodeColorOverride()).thenReturn(TacticalCodeColor.NONE);
        Mockito.when(plane.getAircraftIdCode()).thenReturn("A");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(plane.getType()).thenReturn("fokkerd7");
        Mockito.when(skin.getTacticalCodeColor()).thenReturn(TacticalCodeColor.WHITE);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);
        Mockito.when(skin.getTacticalCodeType()).thenReturn(TacticalCodeType.CODE_POSITION_ONE);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("A%20", tacticalCode.formCodeString());
        Assertions.assertEquals("11", tacticalCode.formCodeColorString());
    }

    @Test
    public void testValidTacticalCodePosition2() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.GERMANY);

        Mockito.when(squadron.getSquadronTacticalCodeColorOverride()).thenReturn(TacticalCodeColor.NONE);
        Mockito.when(plane.getAircraftIdCode()).thenReturn("A");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(plane.getType()).thenReturn("fokkerd7");
        Mockito.when(skin.getTacticalCodeColor()).thenReturn(TacticalCodeColor.WHITE);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);
        Mockito.when(skin.getTacticalCodeType()).thenReturn(TacticalCodeType.CODE_POSITION_TWO);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("%20A", tacticalCode.formCodeString());
        Assertions.assertEquals("11", tacticalCode.formCodeColorString());
    }

    @Test
    public void testValidTacticalCodeHalberstadtPosition1() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.GERMANY);

        Mockito.when(squadron.getSquadronTacticalCodeColorOverride()).thenReturn(TacticalCodeColor.NONE);
        Mockito.when(plane.getAircraftIdCode()).thenReturn("A");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(plane.getType()).thenReturn("halberstadtcl2");
        Mockito.when(skin.getTacticalCodeColor()).thenReturn(TacticalCodeColor.WHITE);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);
        Mockito.when(skin.getTacticalCodeType()).thenReturn(TacticalCodeType.CODE_POSITION_ONE);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("A%20%20", tacticalCode.formCodeString());
        Assertions.assertEquals("111", tacticalCode.formCodeColorString());
    }

    @Test
    public void testValidTacticalCodeHalberstadtPosition2() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.GERMANY);

        Mockito.when(squadron.getSquadronTacticalCodeColorOverride()).thenReturn(TacticalCodeColor.NONE);
        Mockito.when(plane.getAircraftIdCode()).thenReturn("A");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(plane.getType()).thenReturn("halberstadtcl2");
        Mockito.when(skin.getTacticalCodeColor()).thenReturn(TacticalCodeColor.WHITE);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);
        Mockito.when(skin.getTacticalCodeType()).thenReturn(TacticalCodeType.CODE_POSITION_TWO);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("%20A%20", tacticalCode.formCodeString());
        Assertions.assertEquals("111", tacticalCode.formCodeColorString());
    }
    
    @Test
    public void testValidTacticalCodePosition3() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.GERMANY);

        Mockito.when(plane.getAircraftIdCode()).thenReturn("A");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(plane.getType()).thenReturn("fokkerd7");
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);
        Mockito.when(skin.getTacticalCodeType()).thenReturn(TacticalCodeType.CODE_POSITION_THREE);

        PWCGException  exception = Assertions.assertThrows(PWCGException.class, () -> {
            TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        });
        Assertions.assertEquals("Invalid code type: WWI Germany does not support position 3", exception.getMessage());
    }
    
    @Test
    public void testValidTacticalCodePosition1And2() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.GERMANY);

        Mockito.when(plane.getAircraftIdCode()).thenReturn("A");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(plane.getType()).thenReturn("fokkerd7");
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);
        Mockito.when(skin.getTacticalCodeType()).thenReturn(TacticalCodeType.CODE_POSITION_ONE_AND_TWO);

        PWCGException  exception = Assertions.assertThrows(PWCGException.class, () -> {
            TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        });
        Assertions.assertEquals("Invalid code type: WWI Germany does not support position 1 and 2", exception.getMessage());
    }
}
