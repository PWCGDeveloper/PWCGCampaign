package pwcg.campaign.io.json;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.target.TargetPreferenceSet;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class TargetPreferenceIOJsonTest
{
    @Test
    public void readJsonFranceTest() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        TargetPreferenceSet targetPreference = TargetPreferenceIOJson.readJson("France");
        assert (targetPreference.getTargetPreferences().size() > 0);
    }

    @Test
    public void readJsonChannelTest() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        TargetPreferenceSet targetPreference = TargetPreferenceIOJson.readJson("Channel");
        assert (targetPreference.getTargetPreferences().size() > 0);
    }

    @Test
    public void readJsonGaliciaTest() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        TargetPreferenceSet targetPreference = TargetPreferenceIOJson.readJson("Galicia");
        assert (targetPreference.getTargetPreferences().size() > 0);
    }
    
    @Test
    public void readJsonMoscowTest() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        TargetPreferenceSet targetPreference = TargetPreferenceIOJson.readJson("Moscow");
        assert (targetPreference.getTargetPreferences().size() > 0);
    }

    @Test
    public void readJsonStalingradTest() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        TargetPreferenceSet targetPreference = TargetPreferenceIOJson.readJson("Stalingrad");
        assert (targetPreference.getTargetPreferences().size() > 0);
    }

    @Test
    public void readJsonKubanTest() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        TargetPreferenceSet targetPreference = TargetPreferenceIOJson.readJson("Kuban");
        assert (targetPreference.getTargetPreferences().size() > 0);
    }

}
