package pwcg.campaign.context;

import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.product.rof.plane.payload.RoFPayloadFactory;
import pwcg.core.exception.PWCGException;

public class RoFContext extends PWCGContextManagerBase implements IPWCGContextManager
{
    protected RoFContext()
    {
        campaignStartDates.add("01/01/1916");
        campaignStartDates.add("01/02/1916");
        campaignStartDates.add("01/03/1916");
        campaignStartDates.add("01/04/1916");
        campaignStartDates.add("01/05/1916");
        campaignStartDates.add("01/06/1916");
        campaignStartDates.add("01/07/1916");
        campaignStartDates.add("01/08/1916");
        campaignStartDates.add("01/09/1916");
        campaignStartDates.add("01/10/1916");
        campaignStartDates.add("01/11/1916");
        campaignStartDates.add("01/12/1916");
        campaignStartDates.add("01/01/1917");
        campaignStartDates.add("01/02/1917");
        campaignStartDates.add("01/03/1917");
        campaignStartDates.add("01/04/1917");
        campaignStartDates.add("01/05/1917");
        campaignStartDates.add("01/06/1917");
        campaignStartDates.add("01/07/1917");
        campaignStartDates.add("01/08/1917");
        campaignStartDates.add("01/09/1917");
        campaignStartDates.add("01/10/1917");
        campaignStartDates.add("01/11/1917");
        campaignStartDates.add("01/12/1917");
        campaignStartDates.add("01/01/1918");
        campaignStartDates.add("01/02/1918");
        campaignStartDates.add("01/03/1918");
        campaignStartDates.add("01/04/1918");
        campaignStartDates.add("01/05/1918");
        campaignStartDates.add("01/06/1918");
        campaignStartDates.add("01/07/1918");
        campaignStartDates.add("01/08/1918");
        campaignStartDates.add("01/09/1918");
        campaignStartDates.add("01/10/1918");
    }

    @Override
    protected void initialize() throws PWCGException  
    {
        PWCGMap franceMap = PWCGMapFactory.getMap(FrontMapIdentifier.FRANCE_MAP);
        PWCGMap channelMap = PWCGMapFactory.getMap(FrontMapIdentifier.CHANNEL_MAP);
        PWCGMap galiciaMap = PWCGMapFactory.getMap(FrontMapIdentifier.GALICIA_MAP);
        
        pwcgMaps.put(franceMap.getMapIdentifier(), franceMap);
        pwcgMaps.put(channelMap.getMapIdentifier(), channelMap);
        pwcgMaps.put(galiciaMap.getMapIdentifier(), galiciaMap);

        super.initialize();
    }

    @Override
    public void initializeMap() throws PWCGException  
    {
        changeContext(FrontMapIdentifier.FRANCE_MAP);
    }

    @Override
    public IPayloadFactory getPayloadFactory() throws PWCGException  
    {
        return new RoFPayloadFactory();
    }

    @Override
    public PWCGDirectoryManager getDirectoryManager()
    {
        return new PWCGDirectoryManager(PWCGProduct.ROF);
    }

    @Override
    public boolean determineUseMovingFront() throws PWCGException
    {        
        return true;
    }
}
