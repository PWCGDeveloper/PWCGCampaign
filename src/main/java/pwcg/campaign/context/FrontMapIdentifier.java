package pwcg.campaign.context;

import java.util.HashMap;
import java.util.Map;

public enum FrontMapIdentifier
{
    NO_MAP("No Map", PWCGFront.ANY_THEATRE), 
    MOSCOW_MAP("Moscow", PWCGFront.WWII_EASTERN_FRONT), 
    STALINGRAD_MAP("Stalingrad", PWCGFront.WWII_EASTERN_FRONT), 
    KUBAN_MAP("Kuban", PWCGFront.WWII_EASTERN_FRONT), 
    EAST1944_MAP("East1944", PWCGFront.WWII_EASTERN_FRONT), 
    EAST1945_MAP("East1945", PWCGFront.WWII_EASTERN_FRONT), 
    NORMANDY_MAP("Normandy", PWCGFront.WWII_WESTERN_FRONT), 
    BODENPLATTE_MAP("Bodenplatte", PWCGFront.WWII_WESTERN_FRONT), 
    WESTERN_FRONT_MAP("WesternFront", PWCGFront.WWI_WESTERN_FRONT);

    private String mapName = "";
    private PWCGFront front;

    private FrontMapIdentifier(String mapName, PWCGFront front)
    {
        this.mapName = mapName;
        this.front = front;
    }

    public String getMapName()
    {
        return mapName;
    }

    public PWCGFront getFront()
    {
        return front;
    }

    public static FrontMapIdentifier getFrontMapIdentifierForName(String name)
    {
        Map<String, FrontMapIdentifier> frontNameIdentifierMap = new HashMap<String, FrontMapIdentifier>();

        frontNameIdentifierMap.put(MOSCOW_MAP.getMapName(), MOSCOW_MAP);
        frontNameIdentifierMap.put(STALINGRAD_MAP.getMapName(), STALINGRAD_MAP);
        frontNameIdentifierMap.put(KUBAN_MAP.getMapName(), KUBAN_MAP);
        frontNameIdentifierMap.put(EAST1944_MAP.getMapName(), EAST1944_MAP);
        frontNameIdentifierMap.put(EAST1945_MAP.getMapName(), EAST1945_MAP);
        frontNameIdentifierMap.put(NORMANDY_MAP.getMapName(), NORMANDY_MAP);
        frontNameIdentifierMap.put(BODENPLATTE_MAP.getMapName(), BODENPLATTE_MAP);

        frontNameIdentifierMap.put(WESTERN_FRONT_MAP.getMapName(), WESTERN_FRONT_MAP);

        return frontNameIdentifierMap.get(name);
    }
    

    public static PWCGProduct getProductForMap(FrontMapIdentifier map)
    {
        if (map == WESTERN_FRONT_MAP)
        {
            return PWCGProduct.FC;
        }
        else
        {
            return PWCGProduct.BOS;
        }
    }

}
