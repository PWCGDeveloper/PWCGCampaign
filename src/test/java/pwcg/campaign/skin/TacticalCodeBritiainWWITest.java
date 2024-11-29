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
public class TacticalCodeBritiainWWITest
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
        Mockito.when(country.getCountry()).thenReturn(Country.BRITAIN);

        Mockito.when(squadron.getSquadronTacticalCodeColorOverride()).thenReturn(TacticalCodeColor.NONE);
        Mockito.when(plane.getAircraftIdCode()).thenReturn("A");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(plane.getType()).thenReturn("sopcamel");
        Mockito.when(skin.getTacticalCodeColor()).thenReturn(TacticalCodeColor.WHITE);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);
        Mockito.when(skin.getTacticalCodeType()).thenReturn(TacticalCodeType.CODE_POSITION_ONE);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("A%20%20", tacticalCode.formCodeString());
        Assertions.assertEquals("111", tacticalCode.formCodeColorString());
    }

    @Test
    public void testValidTacticalCodePosition2() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.BRITAIN);

        Mockito.when(squadron.getSquadronTacticalCodeColorOverride()).thenReturn(TacticalCodeColor.NONE);
        Mockito.when(plane.getAircraftIdCode()).thenReturn("A");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(plane.getType()).thenReturn("sopcamel");
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
        Mockito.when(country.getCountry()).thenReturn(Country.BRITAIN);

        Mockito.when(squadron.getSquadronTacticalCodeColorOverride()).thenReturn(TacticalCodeColor.NONE);
        Mockito.when(plane.getAircraftIdCode()).thenReturn("A");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(plane.getType()).thenReturn("se5a");
        Mockito.when(skin.getTacticalCodeColor()).thenReturn(TacticalCodeColor.WHITE);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);
        Mockito.when(skin.getTacticalCodeType()).thenReturn(TacticalCodeType.CODE_POSITION_THREE);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("%20%20A", tacticalCode.formCodeString());
        Assertions.assertEquals("111", tacticalCode.formCodeColorString());
    }

    @Test
    public void testValidTacticalCodePosition1WithDolphin() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.BRITAIN);

        Mockito.when(squadron.getSquadronTacticalCodeColorOverride()).thenReturn(TacticalCodeColor.NONE);
        Mockito.when(plane.getAircraftIdCode()).thenReturn("A");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(plane.getType()).thenReturn("sopdolphin");
        Mockito.when(skin.getTacticalCodeColor()).thenReturn(TacticalCodeColor.WHITE);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);
        Mockito.when(skin.getTacticalCodeType()).thenReturn(TacticalCodeType.CODE_POSITION_ONE);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("A%20", tacticalCode.formCodeString());
        Assertions.assertEquals("11", tacticalCode.formCodeColorString());
    }
    
    @Test
    public void testValidTacticalCodePosition1And2WithBritishSpad() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.BRITAIN);

        Mockito.when(squadron.getSquadronTacticalCodeColorOverride()).thenReturn(TacticalCodeColor.NONE);
        Mockito.when(plane.getAircraftIdCode()).thenReturn("12");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(plane.getType()).thenReturn("spad13");
        Mockito.when(skin.getTacticalCodeColor()).thenReturn(TacticalCodeColor.WHITE);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);
        Mockito.when(skin.getTacticalCodeType()).thenReturn(TacticalCodeType.CODE_POSITION_ONE_AND_TWO);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("12", tacticalCode.formCodeString());
        Assertions.assertEquals("11", tacticalCode.formCodeColorString());
    }
    
    @Test
    public void testValidTacticalCodePosition1And2WithBritishPlane() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.BRITAIN);

        Mockito.when(plane.getAircraftIdCode()).thenReturn("A");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(plane.getType()).thenReturn("sopcamel");
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);
        Mockito.when(skin.getTacticalCodeType()).thenReturn(TacticalCodeType.CODE_POSITION_ONE_AND_TWO);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);

        Assertions.assertNull(tacticalCode);
    }
    
    @Test
    public void testValidTacticalCodePosition3WithDolphin() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.BRITAIN);

        Mockito.when(plane.getAircraftIdCode()).thenReturn("A");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(plane.getType()).thenReturn("sopdolphin");
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);
        Mockito.when(skin.getTacticalCodeType()).thenReturn(TacticalCodeType.CODE_POSITION_THREE);
        
        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);

        Assertions.assertNull(tacticalCode);
    }
    
    @Test
    public void testValidTacticalCodePosition2WithDH4() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.BRITAIN);

        Mockito.when(plane.getAircraftIdCode()).thenReturn("A");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(plane.getType()).thenReturn("aircodh4");
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);
        Mockito.when(skin.getTacticalCodeType()).thenReturn(TacticalCodeType.CODE_POSITION_TWO);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);

        Assertions.assertNull(tacticalCode);
    }
}
