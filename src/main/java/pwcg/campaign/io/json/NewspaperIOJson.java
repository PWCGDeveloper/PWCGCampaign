package pwcg.campaign.io.json;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.newspapers.Newspapers;
import pwcg.core.exception.PWCGException;

public class NewspaperIOJson
{
    public static Newspapers readJson() throws PWCGException
    {        
        JsonObjectReader<Newspapers> jsonReader = new JsonObjectReader<>(Newspapers.class);
        Newspapers newspapers = jsonReader.readJsonFile(PWCGContext.getInstance().getDirectoryManager().getPwcgNewspaperDir(), "Newspapers.json"); 
        return newspapers;
    }

}
