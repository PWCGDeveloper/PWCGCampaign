package pwcg.campaign.io.json;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.target.locator.targettype.TargetPreferenceSet;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class TargetPreferenceIOJsonTest
{
    @Test
    public void readJsonFranceTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.ROF);
        TargetPreferenceSet targetPreference = TargetPreferenceIOJson.readJson("France");
        assert (targetPreference.getTargetPreferences().size() > 0);
    }

    @Test
    public void readJsonChannelTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.ROF);
        TargetPreferenceSet targetPreference = TargetPreferenceIOJson.readJson("Channel");
        assert (targetPreference.getTargetPreferences().size() > 0);
    }

    @Test
    public void readJsonGaliciaTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.ROF);
        TargetPreferenceSet targetPreference = TargetPreferenceIOJson.readJson("Galicia");
        assert (targetPreference.getTargetPreferences().size() > 0);
    }
    
    @Test
    public void readJsonMoscowTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        TargetPreferenceSet targetPreference = TargetPreferenceIOJson.readJson("Moscow");
        assert (targetPreference.getTargetPreferences().size() > 0);
    }

    @Test
    public void readJsonStalingradTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        TargetPreferenceSet targetPreference = TargetPreferenceIOJson.readJson("Stalingrad");
        assert (targetPreference.getTargetPreferences().size() > 0);
    }

    @Test
    public void readJsonKubanTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        TargetPreferenceSet targetPreference = TargetPreferenceIOJson.readJson("Kuban");
        assert (targetPreference.getTargetPreferences().size() > 0);
    }

}
