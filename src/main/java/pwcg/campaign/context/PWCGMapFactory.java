package pwcg.campaign.context;

import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.map.bodenplatte.BodenplatteMap;
import pwcg.product.bos.map.kuban.KubanMap;
import pwcg.product.bos.map.moscow.MoscowMap;
import pwcg.product.bos.map.stalingrad.StalingradMap;
import pwcg.product.fc.map.arras.ArrasMap;
import pwcg.product.rof.map.channel.ChannelMap;
import pwcg.product.rof.map.france.FranceMap;
import pwcg.product.rof.map.galicia.GaliciaMap;

public class PWCGMapFactory
{
    public static PWCGMap getMap(FrontMapIdentifier frontMapIdentifier) throws PWCGException
    {
        PWCGMap map = null;
        
        if (frontMapIdentifier == FrontMapIdentifier.FRANCE_MAP)
        {
            map = new FranceMap();
            map.configure();
        }
        if (frontMapIdentifier == FrontMapIdentifier.CHANNEL_MAP)
        {
            map = new ChannelMap();
            map.configure();
        }
        if (frontMapIdentifier == FrontMapIdentifier.GALICIA_MAP)
        {
            map = new GaliciaMap();
            map.configure();
        }
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
