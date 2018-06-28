package pwcg.campaign.io.json;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.group.FixedPositions;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;

@RunWith(MockitoJUnitRunner.class)
public class FixedPositionIOJsonTest
{
    @Test
    public void readJsonFranceAirfieldsTest() throws PWCGException
    {
        String directory = "E:\\PWCG\\workspacePWCGGradle\\PWCGCampaign\\RoFData\\input\\France\\";
        validateRofFieldsForMap(directory);
    }

    @Test
    public void readJsonChannelAirfieldsTest() throws PWCGException
    {
        String directory = "E:\\PWCG\\workspacePWCGGradle\\PWCGCampaign\\RoFData\\input\\Channel\\";
        validateRofFieldsForMap(directory);
    }

    @Test
    public void readJsonGaliciaAirfieldsTest() throws PWCGException
    {
        String directory = "E:\\PWCG\\workspacePWCGGradle\\PWCGCampaign\\RoFData\\input\\Galicia\\";
        validateRofFieldsForMap(directory);
    }

    private void validateRofFieldsForMap(String directory) throws PWCGException, PWCGIOException
    {
        FixedPositions rofAirfieldFixedPositions = FixedPositionIOJson.readJson(directory, "RoFAirfields");
        assert (rofAirfieldFixedPositions.getFixedPositions().size() > 0);
        
        for (String key : rofAirfieldFixedPositions.getFixedPositions().keySet())
        {
            assert (rofAirfieldFixedPositions.getFixedPositions().get(key).getName() != null);
            assert (rofAirfieldFixedPositions.getFixedPositions().get(key).getName().length() > 0);
        }
    }
}
