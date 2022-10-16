package pwcg.campaign.skin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.plane.PlaneMcu;

@ExtendWith(MockitoExtension.class)
public class TacticalCodeGermanyTest
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
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.GERMANY);

        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430801"));

        Mockito.when(squadron.determineDisplayName(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn("I JG.26");
        Mockito.when(squadron.getSquadronTacticalCodeColorOverride()).thenReturn(TacticalCodeColor.NONE);
        
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20101);
        Mockito.when(squadron.determineServiceForSquadron(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn(service);
        Mockito.when(squadron.determineSubUnitIdCode(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn("");
        Mockito.when(plane.getAircraftIdCode()).thenReturn("14");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(plane.getPilot()).thenReturn(pilot);
        Mockito.when(pilot.getRank()).thenReturn("Leutnant");
        Mockito.when(skin.getTacticalCodeColor()).thenReturn(TacticalCodeColor.YELLOW);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("14%20", tacticalCode.formCodeString());
        Assertions.assertEquals("444", tacticalCode.formCodeColorString());
    }
    
    @Test
    public void testValidFighterTacticalCodeCommander() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.GERMANY);

        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430801"));

        Mockito.when(squadron.determineDisplayName(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn("I JG.26");
        
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20101);
        Mockito.when(squadron.determineServiceForSquadron(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn(service);
        Mockito.when(squadron.determineSubUnitIdCode(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn("-");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(plane.getPilot()).thenReturn(pilot);
        Mockito.when(pilot.getRank()).thenReturn("Major");
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("%22%20%3b", tacticalCode.formCodeString());
        Assertions.assertEquals("000", tacticalCode.formCodeColorString());
    }
    
    @Test
    public void testValidFighterTacticalCodeAdjudant() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.GERMANY);

        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430801"));

        Mockito.when(squadron.determineDisplayName(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn("I JG.26");
        
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20101);
        Mockito.when(squadron.determineServiceForSquadron(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn(service);
        Mockito.when(squadron.determineSubUnitIdCode(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn("~");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(plane.getPilot()).thenReturn(pilot);
        Mockito.when(pilot.getRank()).thenReturn("Hauptmann");
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("%21%25%3e", tacticalCode.formCodeString());
        Assertions.assertEquals("000", tacticalCode.formCodeColorString());
    }
    
    @Test
    public void testValidFighterTacticalCodeWithSquadronColor() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.GERMANY);

        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430801"));

        Mockito.when(squadron.determineDisplayName(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn("I JG.26");
        Mockito.when(squadron.getSquadronTacticalCodeColorOverride()).thenReturn(TacticalCodeColor.RED);
        
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20101);
        Mockito.when(squadron.determineServiceForSquadron(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn(service);
        Mockito.when(squadron.determineSubUnitIdCode(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn("+");
        Mockito.when(plane.getAircraftIdCode()).thenReturn("4");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(plane.getPilot()).thenReturn(pilot);
        Mockito.when(pilot.getRank()).thenReturn("Leutnant");
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("%204%3d", tacticalCode.formCodeString());
        Assertions.assertEquals("222", tacticalCode.formCodeColorString());
    }

    @Test
    public void testValidFighterTacticalCodeWithNoColor() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.GERMANY);

        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430801"));
        Mockito.when(squadron.determineDisplayName(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn("I JG.26");
        Mockito.when(squadron.getSquadronTacticalCodeColorOverride()).thenReturn(TacticalCodeColor.NONE);
        
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20101);
        Mockito.when(squadron.determineServiceForSquadron(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn(service);
        Mockito.when(plane.getAircraftIdCode()).thenReturn("14");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(plane.getPilot()).thenReturn(pilot);
        Mockito.when(pilot.getRank()).thenReturn("Leutnant");
        Mockito.when(skin.getTacticalCodeColor()).thenReturn(TacticalCodeColor.NONE);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("14%20", tacticalCode.formCodeString());
        Assertions.assertEquals("000", tacticalCode.formCodeColorString());
    }

    @Test
    public void testGoodHandlingWithNoAssignedAircraftUnitCode() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.GERMANY);

        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430801"));
        Mockito.when(squadron.determineDisplayName(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn("I JG.26");
        
        ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(20101);
        Mockito.when(squadron.determineServiceForSquadron(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn(service);
        Mockito.when(plane.getAircraftIdCode()).thenReturn("");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(plane.getPilot()).thenReturn(pilot);
        Mockito.when(pilot.getRank()).thenReturn("Leutnant");
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("%20%20%20", tacticalCode.formCodeString());
        Assertions.assertEquals("000", tacticalCode.formCodeColorString());
    }

    @Test
    public void testValidBomberTacticalCode() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.GERMANY);

        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430801"));

        Mockito.when(squadron.determineDisplayName(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn("I KG.72");
        Mockito.when(squadron.determineUnitIdCode(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn("HC");
        Mockito.when(squadron.determineSubUnitIdCode(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn("Z");
        Mockito.when(squadron.getSquadronTacticalCodeColorOverride()).thenReturn(TacticalCodeColor.NONE);
        
        Mockito.when(plane.getAircraftIdCode()).thenReturn("X");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(skin.getTacticalCodeColor()).thenReturn(TacticalCodeColor.RED);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("HCXZ", tacticalCode.formCodeString());
        Assertions.assertEquals("0020", tacticalCode.formCodeColorString());
    }

    @Test
    public void testValidBomberTacticalCodeWithSquadronColor() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.GERMANY);

        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430801"));

        Mockito.when(squadron.determineDisplayName(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn("I KG.72");
        Mockito.when(squadron.determineUnitIdCode(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn("HC");
        Mockito.when(squadron.determineSubUnitIdCode(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn("Z");
        Mockito.when(squadron.getSquadronTacticalCodeColorOverride()).thenReturn(TacticalCodeColor.BLUE);
        
        Mockito.when(plane.getAircraftIdCode()).thenReturn("X");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("HCXZ", tacticalCode.formCodeString());
        Assertions.assertEquals("0030", tacticalCode.formCodeColorString());
    }

    @Test
    public void testValidBomberTacticalCodeWithNoColor() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.GERMANY);

        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430801"));

        Mockito.when(squadron.determineDisplayName(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn("I KG.72");
        Mockito.when(squadron.determineUnitIdCode(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn("HC");
        Mockito.when(squadron.determineSubUnitIdCode(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn("Z");
        Mockito.when(squadron.getSquadronTacticalCodeColorOverride()).thenReturn(TacticalCodeColor.NONE);
        
        Mockito.when(plane.getAircraftIdCode()).thenReturn("X");
        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(skin.getTacticalCodeColor()).thenReturn(TacticalCodeColor.NONE);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("HCXZ", tacticalCode.formCodeString());
        Assertions.assertEquals("0020", tacticalCode.formCodeColorString());
    }

    @Test
    public void testGoodHandlingWithNoAssignedUnitCode() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430801"));
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(squadron.determineDisplayName(DateUtils.getDateYYYYMMDD("19430801"))).thenReturn("I KG.72");
        Mockito.when(country.getCountry()).thenReturn(Country.GERMANY);

        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(true);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("%20%20%20%20", tacticalCode.formCodeString());
        Assertions.assertEquals("0000", tacticalCode.formCodeColorString());
    }

    @Test
    public void testNoTacticalCodeForMarkedSkin() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.GERMANY);

        Mockito.when(plane.getSkin()).thenReturn(skin);
        Mockito.when(skin.isUseTacticalCodes()).thenReturn(false);

        TacticalCode tacticalCode = TacticalCodeBuilder.buildTacticalCode(campaign, squadron, plane);
        Assertions.assertEquals("", tacticalCode.formCodeString());
        Assertions.assertEquals("", tacticalCode.formCodeColorString());
    }

}
