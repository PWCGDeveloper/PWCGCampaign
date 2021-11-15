package pwcg.campaign.io.json;

import pwcg.core.exception.PWCGException;
import pwcg.gui.ScreenIdentifierOverrideSet;

public class ScreenIdentifierOverrideIOJson 
{
    public static void writeJson(String directoryName, String filename, ScreenIdentifierOverrideSet screenIdentifierOverrideSet) throws PWCGException
    {
        PwcgJsonWriter<ScreenIdentifierOverrideSet> jsonWriter = new PwcgJsonWriter<>();
        jsonWriter.writeAsJson(screenIdentifierOverrideSet, directoryName, filename + ".json");
    }

    public static ScreenIdentifierOverrideSet readJson(String directoryName, String filename) throws PWCGException
    {       
        JsonObjectReader<ScreenIdentifierOverrideSet> jsoReader = new JsonObjectReader<>(ScreenIdentifierOverrideSet.class);
        ScreenIdentifierOverrideSet screenIdentifierOverrideSet = jsoReader.readJsonFile(directoryName, filename + ".json");
        return screenIdentifierOverrideSet;
    }
}
