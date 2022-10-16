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
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.plane.PlaneMcu;

@ExtendWith(MockitoExtension.class)
public class TacticalCodeItalyTest
{
    @Mock Campaign campaign;
    @Mock Squadron squadron;
    @Mock SquadronMember pilot;
    @Mock PlaneMcu plane;
    @Mock ICountry country;
    @Mock Skin skin;
    
    @Test
    public void testValidFighterTacticalCode() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430801"));
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(squadron.determineUnitIdCode(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn("123");
        Mockito.when(country.getCountry()).thenReturn(Country.ITALY);
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("123", tacticalCode.formCodeString());
        Assertions.assertEquals("000", tacticalCode.formCodeColorString());
    }

    @Test
    public void testGoodHandlingWithNoAssignedUnitCode() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430801"));
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.ITALY);

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
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430801"));
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.ITALY);

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
        Mockito.when(country.getCountry()).thenReturn(Country.ITALY);

        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(false);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("", tacticalCode.formCodeString());
        Assertions.assertEquals("", tacticalCode.formCodeColorString());
    }

}
