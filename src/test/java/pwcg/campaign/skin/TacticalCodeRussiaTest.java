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
public class TacticalCodeRussiaTest
{
    @Mock Campaign campaign;
    @Mock Squadron squadron;
    @Mock PlaneMcu plane;
    @Mock ICountry country;
    @Mock Skin skin;
    
    @Test
    public void testValidTacticalCode() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.RUSSIA);

        Mockito.when(squadron.getSquadronTacticalCodeColorOverride()).thenReturn(TacticalCodeColor.NONE);
        Mockito.when(squadron.getTacticalCodeStyle()).thenReturn(2);
        Mockito.when(plane.getAircraftIdCode()).thenReturn("12");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(skin.getTacticalCodeColor()).thenReturn(TacticalCodeColor.RED);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("12", tacticalCode.formCodeString());
        Assertions.assertEquals("22", tacticalCode.formCodeColorString());
    }
    
    @Test
    public void testValidTacticalCodeWithOneDigitAndStyle1() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.RUSSIA);

        Mockito.when(squadron.getSquadronTacticalCodeColorOverride()).thenReturn(TacticalCodeColor.NONE);
        Mockito.when(squadron.getTacticalCodeStyle()).thenReturn(1);
        Mockito.when(plane.getAircraftIdCode()).thenReturn("2");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(skin.getTacticalCodeColor()).thenReturn(TacticalCodeColor.RED);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("%20%23", tacticalCode.formCodeString());
        Assertions.assertEquals("22", tacticalCode.formCodeColorString());
    }

    @Test
    public void testValidTacticalCodeWithNoColorWithStyle3() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.RUSSIA);

        Mockito.when(squadron.getSquadronTacticalCodeColorOverride()).thenReturn(TacticalCodeColor.NONE);
        Mockito.when(squadron.getTacticalCodeStyle()).thenReturn(3);
        Mockito.when(plane.getAircraftIdCode()).thenReturn("12");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(skin.getTacticalCodeColor()).thenReturn(TacticalCodeColor.NONE);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("%3b%3c", tacticalCode.formCodeString());
        Assertions.assertEquals("00", tacticalCode.formCodeColorString());
    }

    @Test
    public void testGoodHandlingWithNoAssignedUnitCode() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.RUSSIA);

        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("", tacticalCode.formCodeString());
        Assertions.assertEquals("", tacticalCode.formCodeColorString());
    }

    @Test
    public void testGoodHandlingWithNoAssignedAircraftUnitCode() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.RUSSIA);

        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("", tacticalCode.formCodeString());
        Assertions.assertEquals("", tacticalCode.formCodeColorString());
    }

    @Test
    public void testNoTacticalCodeForMarkedSkin() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.RUSSIA);

        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(false);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("", tacticalCode.formCodeString());
        Assertions.assertEquals("", tacticalCode.formCodeColorString());
    }

}
