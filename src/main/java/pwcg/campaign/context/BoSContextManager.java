package pwcg.campaign.context;

import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.plane.payload.IPayloadFactory;
import pwcg.campaign.ww2.plane.payload.BoSPayloadFactory;
import pwcg.core.exception.PWCGException;

public class BoSContextManager extends PWCGContextManagerBase implements IPWCGContextManager
{ 
    /**
     * 
     */
	protected BoSContextManager()
    {
        campaignStartDates.add("01/10/1941");
        campaignStartDates.add("01/11/1941");
        campaignStartDates.add("01/12/1941");
        campaignStartDates.add("01/01/1942");
        campaignStartDates.add("01/02/1942");
        campaignStartDates.add("01/03/1942");
        campaignStartDates.add("01/04/1942");
        campaignStartDates.add("01/05/1942");
        campaignStartDates.add("01/06/1942");
        campaignStartDates.add("01/07/1942");
        campaignStartDates.add("01/08/1942");
        campaignStartDates.add("01/09/1942");
        campaignStartDates.add("01/10/1942");
        campaignStartDates.add("01/11/1942");
        campaignStartDates.add("01/12/1942");
        campaignStartDates.add("01/01/1943");
        campaignStartDates.add("01/02/1943");
        campaignStartDates.add("01/03/1943");
        campaignStartDates.add("01/04/1943");
        campaignStartDates.add("01/05/1943");
        campaignStartDates.add("01/06/1943");
        campaignStartDates.add("01/07/1943");
        campaignStartDates.add("01/08/1943");
        campaignStartDates.add("01/09/1943");
        campaignStartDates.add("01/10/1943");
    }
	

    /**
     * Initialize context to the base map for the product
     * 
     * @throws PWCGException 
     */
    @Override
    protected void initialize() throws PWCGException  
    {
        PWCGMap moscowMap = PWCGMapFactory.getMap(FrontMapIdentifier.MOSCOW_MAP);
        PWCGMap stalingradMap = PWCGMapFactory.getMap(FrontMapIdentifier.STALINGRAD_MAP);
        PWCGMap kubanMap = PWCGMapFactory.getMap(FrontMapIdentifier.KUBAN_MAP);
        
        pwcgMaps.put(moscowMap.getMapIdentifier(), moscowMap);
        pwcgMaps.put(stalingradMap.getMapIdentifier(), stalingradMap);
        pwcgMaps.put(kubanMap.getMapIdentifier(), kubanMap);

        super.initialize();
    }

    @Override
    public void initializeMap() throws PWCGException  
    {
        changeContext(FrontMapIdentifier.STALINGRAD_MAP);
    }

    @Override
    public IPayloadFactory getPayloadFactory() throws PWCGException  
    {
        return new BoSPayloadFactory();
    }

    @Override
    public PWCGDirectoryManager getDirectoryManager()
    {
        return new PWCGDirectoryManager(false);
    }

    @Override
    public boolean determineUseMovingFront() throws PWCGException
    {        
        if (campaign != null)
        {
            return campaign.useMovingFrontInCampaign();
        }
        
        return testUseMovingFront;
    }
}
