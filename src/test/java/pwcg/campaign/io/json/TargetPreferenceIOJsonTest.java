package pwcg.campaign.io.json;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.target.preference.TargetPreferenceSet;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class TargetPreferenceIOJsonTest
{
    @Test
    public void readJsonArrasTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        TargetPreferenceSet targetPreference = TargetPreferenceIOJson.readJson("Arras");
        assert (targetPreference.getTargetPreferences().size() == 0);
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

    @Test
    public void readJsonBodenplatteTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        TargetPreferenceSet targetPreference = TargetPreferenceIOJson.readJson("Bodenplatte");
        assert (targetPreference.getTargetPreferences().size() > 0);
    }
}
