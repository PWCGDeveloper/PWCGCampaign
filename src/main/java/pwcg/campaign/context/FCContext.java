package pwcg.campaign.context;

import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.core.exception.PWCGException;
import pwcg.product.fc.plane.payload.FCPayloadFactory;

public class FCContext extends PWCGContextBase implements IPWCGContextManager
{
    protected FCContext()
    {
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
        PWCGMap westernFrontMap = PWCGMapFactory.configureMap(FrontMapIdentifier.WESTERN_FRONT_MAP);
        
        pwcgMaps.put(westernFrontMap.getMapIdentifier(), westernFrontMap);

        super.initialize();
    }

    @Override
    public IPayloadFactory getPayloadFactory() throws PWCGException  
    {
        return new FCPayloadFactory();
    }

    @Override
    public PWCGDirectoryProductManager getDirectoryManager()
    {
        return new PWCGDirectoryProductManager(PWCGProduct.FC);
    }

    @Override
    public FrontMapIdentifier getDefaultMapForProduct()
    {
        return FrontMapIdentifier.WESTERN_FRONT_MAP;
    }

    @Override
    public PWCGProduct getProduct()
    {
        return PWCGProduct.FC;
    }
}
