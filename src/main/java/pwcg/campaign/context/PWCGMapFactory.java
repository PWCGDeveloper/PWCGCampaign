package pwcg.campaign.context;

import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.ww1.map.channel.ChannelMap;
import pwcg.campaign.ww1.map.france.FranceMap;
import pwcg.campaign.ww1.map.galicia.GaliciaMap;
import pwcg.campaign.ww2.map.kuban.KubanMap;
import pwcg.campaign.ww2.map.moscow.MoscowMap;
import pwcg.campaign.ww2.map.stalingrad.StalingradMap;
import pwcg.core.exception.PWCGException;

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
        if (frontMapIdentifier == FrontMapIdentifier.KUBAN_MAP)
        {
            map = new KubanMap();
            map.configure();
        }

        return map;
    }
}
