package pwcg.campaign.skin;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class TacticalCodeSquadronConfigTest
{
    @Test
    public void testValidTacticalCode() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        for (Squadron squadron : squadronManager.getAllSquadrons())
        {
            if (squadron.getCountry().getCountry() == Country.GERMANY)
            {
                validateGermanSquadron(squadron);
            }
            else if (squadron.getCountry().getCountry() == Country.ITALY)
            {
                validateItalianSquadron(squadron);
            }
            else if (squadron.getCountry().getCountry() == Country.BRITAIN)
            {
                validateBritishSquadron(squadron);
            }
            else if (squadron.getCountry().getCountry() == Country.USA)
            {
                validateAmericanSquadron(squadron);
            }
            else if (squadron.getCountry().getCountry() == Country.RUSSIA)
            {
                validateRussianSquadron(squadron);
            }
        }
    }

    private void validateRussianSquadron(Squadron squadron) throws PWCGException
    {
        Assertions.assertEquals("", squadron.determineUnitIdCode(DateUtils.getDateYYYYMMDD("19440101")));
        Assertions.assertEquals("", squadron.determineSubUnitIdCode(DateUtils.getDateYYYYMMDD("19440101")));
    }

    private void validateAmericanSquadron(Squadron squadron) throws PWCGException
    {
        Assertions.assertFalse(StringUtils.isBlank(squadron.determineUnitIdCode(DateUtils.getDateYYYYMMDD("19440101"))));
        Assertions.assertEquals("", squadron.determineSubUnitIdCode(DateUtils.getDateYYYYMMDD("19440101")));
    }

    private void validateBritishSquadron(Squadron squadron) throws PWCGException
    {
        Assertions.assertFalse(StringUtils.isBlank(squadron.determineUnitIdCode(DateUtils.getDateYYYYMMDD("19440101"))));
        Assertions.assertEquals("", squadron.determineSubUnitIdCode(DateUtils.getDateYYYYMMDD("19440101")));
    }

    private void validateGermanSquadron(Squadron squadron) throws PWCGException
    {
        Date date = DateUtils.getDateYYYYMMDD("19440101");
        
        if (squadron.determineDisplayName(date).contains("JG") || 
                squadron.determineDisplayName(date).contains("JV") || 
                squadron.determineDisplayName(date).contains("Sch.G") || 
                squadron.determineDisplayName(date).contains("SG"))
        {
            Assertions.assertTrue(StringUtils.isBlank(squadron.determineUnitIdCode(DateUtils.getDateYYYYMMDD("19440101"))));
            if (squadron.determineDisplayName(DateUtils.getDateYYYYMMDD("19440101")).contains("IV."))
            {
                Assertions.assertEquals("+", squadron.determineSubUnitIdCode(DateUtils.getDateYYYYMMDD("19440101"))); 
            }
            else if (squadron.determineDisplayName(DateUtils.getDateYYYYMMDD("19440101")).contains("III."))
            {
                Assertions.assertEquals("~", squadron.determineSubUnitIdCode(DateUtils.getDateYYYYMMDD("19440101"))); 
            }
            else if (squadron.determineDisplayName(DateUtils.getDateYYYYMMDD("19440101")).contains("II."))
            {
                Assertions.assertEquals("-", squadron.determineSubUnitIdCode(DateUtils.getDateYYYYMMDD("19440101"))); 
            }
        }
        else
        {
            Assertions.assertFalse(StringUtils.isBlank(squadron.determineUnitIdCode(DateUtils.getDateYYYYMMDD("19440101"))));
            Assertions.assertFalse(StringUtils.isBlank(squadron.determineSubUnitIdCode(DateUtils.getDateYYYYMMDD("19440101"))));
        }
        
    }

    private void validateItalianSquadron(Squadron squadron) throws PWCGException
    {
        Assertions.assertFalse(StringUtils.isBlank(squadron.determineUnitIdCode(DateUtils.getDateYYYYMMDD("19440101"))));
        Assertions.assertEquals("", squadron.determineSubUnitIdCode(DateUtils.getDateYYYYMMDD("19440101")));
    }
}
