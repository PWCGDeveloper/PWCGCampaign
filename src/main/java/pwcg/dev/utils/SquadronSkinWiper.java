package pwcg.dev.utils;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.SquadronIOJson;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class SquadronSkinWiper
{
    static public void main (String[] args)
    {
        try
        {
            PWCGContext.setProduct(PWCGProduct.BOS);
            SquadronSkinWiper wiper = new SquadronSkinWiper();
            wiper.wipe();
            
        }
        catch (Exception e)
        {
             PWCGLogger.logException(e);;
        }
    }

    private void wipe() throws PWCGException
    {
        List<Squadron> squadrons = SquadronIOJson.readJson(); 
        for (Squadron squadron : squadrons)
        {
            squadron.setSkins(new ArrayList<>());
            SquadronIOJson.writeJson(squadron);
        }

    }
}
