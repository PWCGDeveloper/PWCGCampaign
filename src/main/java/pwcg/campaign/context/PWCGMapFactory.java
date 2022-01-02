package pwcg.campaign.context;

import pwcg.core.exception.PWCGException;
import pwcg.product.bos.map.bodenplatte.BodenplatteMap;
import pwcg.product.bos.map.east1944.East1944Map;
import pwcg.product.bos.map.east1945.East1945Map;
import pwcg.product.bos.map.kuban.KubanMap;
import pwcg.product.bos.map.moscow.MoscowMap;
import pwcg.product.bos.map.stalingrad.StalingradMap;

public class PWCGMapFactory
{
    public static PWCGMap getMap(FrontMapIdentifier frontMapIdentifier) throws PWCGException
    {
        PWCGMap map = null;
        
        if (frontMapIdentifier == FrontMapIdentifier.MOSCOW_MAP)
        {
            map = new MoscowMap();
            map.configure();
        }
        if (frontMapIdentifier == FrontMapIdentifier.STALINGRAD_MAP)
        {
            map = new StalingradMap();
            map.configure();
        }
        if (frontMapIdentifier == FrontMapIdentifier.BODENPLATTE_MAP)
        {
            map = new BodenplatteMap();
            map.configure();
        }
        if (frontMapIdentifier == FrontMapIdentifier.KUBAN_MAP)
        {
            map = new KubanMap();
            map.configure();
        }
        if (frontMapIdentifier == FrontMapIdentifier.EAST1944_MAP)
        {
            map = new East1944Map();
            map.configure();
        }
        if (frontMapIdentifier == FrontMapIdentifier.EAST1945_MAP)
        {
            map = new East1945Map();
            map.configure();
        }

        return map;
    }
}
