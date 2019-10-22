package pwcg.campaign.context;

import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.map.bodenplatte.BodenplatteMap;
import pwcg.product.bos.map.kuban.KubanMap;
import pwcg.product.bos.map.moscow.MoscowMap;
import pwcg.product.bos.map.stalingrad.StalingradMap;
import pwcg.product.fc.map.arras.ArrasMap;

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
        if (frontMapIdentifier == FrontMapIdentifier.ARRAS_MAP)
        {
            map = new ArrasMap();
            map.configure();
        }

        return map;
    }
}
