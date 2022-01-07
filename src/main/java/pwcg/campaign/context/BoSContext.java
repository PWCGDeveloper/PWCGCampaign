package pwcg.campaign.context;

import pwcg.core.exception.PWCGException;

public class BoSContext extends PWCGContextBase implements IPWCGContextManager
{ 
	protected BoSContext()
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
        campaignStartDates.add("01/11/1943");
        campaignStartDates.add("01/12/1943");
        campaignStartDates.add("01/01/1944");
        campaignStartDates.add("01/02/1944");
        campaignStartDates.add("01/03/1944");
        campaignStartDates.add("01/04/1944");
        campaignStartDates.add("01/05/1944");
        campaignStartDates.add("01/06/1944");
        campaignStartDates.add("01/07/1944");
        campaignStartDates.add("01/08/1944");
        campaignStartDates.add("01/09/1944");
        campaignStartDates.add("01/10/1944");
        campaignStartDates.add("01/11/1944");
        campaignStartDates.add("01/12/1944");
        campaignStartDates.add("01/01/1945");
        campaignStartDates.add("01/02/1945");
    }

    @Override
    protected void initialize() throws PWCGException  
    {
        PWCGMap stalingradMap = PWCGMapFactory.getMap(FrontMapIdentifier.STALINGRAD_MAP);
        PWCGMap kubanMap = PWCGMapFactory.getMap(FrontMapIdentifier.KUBAN_MAP);
        PWCGMap east1944Map = PWCGMapFactory.getMap(FrontMapIdentifier.EAST1944_MAP);
        PWCGMap east1945Map = PWCGMapFactory.getMap(FrontMapIdentifier.EAST1945_MAP);
        PWCGMap bodenplatteMap = PWCGMapFactory.getMap(FrontMapIdentifier.BODENPLATTE_MAP);
        
        pwcgMaps.put(stalingradMap.getMapIdentifier(), stalingradMap);
        pwcgMaps.put(kubanMap.getMapIdentifier(), kubanMap);
        pwcgMaps.put(east1944Map.getMapIdentifier(), east1944Map);
        pwcgMaps.put(east1945Map.getMapIdentifier(), east1945Map);
        pwcgMaps.put(bodenplatteMap.getMapIdentifier(), bodenplatteMap);

        super.initialize();
    }

    @Override
    public void initializeMap() throws PWCGException  
    {
        changeContext(FrontMapIdentifier.MOSCOW_MAP);
    }

    @Override
    public PWCGDirectoryProductManager getDirectoryManager()
    {
        return new PWCGDirectoryProductManager(PWCGProduct.BOS);
    }
}
