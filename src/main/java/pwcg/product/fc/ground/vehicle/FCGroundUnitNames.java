package pwcg.product.fc.ground.vehicle;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.api.IGroundUnitNames;


public class FCGroundUnitNames implements IGroundUnitNames
{
    
    // AAA
    public static final String FLAK_37 = "flak37";
    public static final String FLAK_38 = "flak38";
    public static final String AAA_52K = "52k";
    public static final String AAA_61K = "61k";
    public static final String MG34_AAA = "mg34-aa";
    public static final String MAKSIM_AAA = "maksim4-aa";

    // Artillery
    public static final String LEFH18_ARTY = "LEFH18_ARTY";
    public static final String ML20_ARTY = "ml20";
    public static final String PAK38_AT = "pak38";
    public static final String PAK40_AT = "pak40";
    public static final String ZIS_2_AT = "zis2gun";
    public static final String ZIS_3_AT = "zis3gun";

    // Spotlight trucks
    public static final String SPOTLIGHT_DE = "searchlightger";
    public static final String SPOTLIGHT_RU = "searchlightsu";


    public static final String AC_BA10M = "ba10m";
    public static final String AC_BA64 = "ba64";


    // Trucks
    public static final String TRUCK_OPEL = "opel-blitz";

    public static final String TRUCK_FORD = "ford-g917";

    // Train
    public static final String TRAIN_LOCOMOTIVE_G8 = "g8";
    public static final String TRAIN_CAR_WAGON = "Wagon_";
    public static final String TRAIN_CAR_TANKER = "Wagon_Tank";
    public static final String TRAIN_CAR_AAA = "Wagon_PlatformAA";

    private Map<String, String> typeToDisplayName = new HashMap<String, String>();

    /**
     * 
     */
    public String getGroundUnitDisplayName(String typeName)
    {
        String displayName = "";
        
        if (typeToDisplayName.size() == 0)
        {
            loadMap();
        }
        
        if (typeToDisplayName.containsKey(typeName))
        {
            displayName = typeToDisplayName.get(typeName);
        }
        
        for (String key : typeToDisplayName.keySet())
        {
            if (typeName.toLowerCase().startsWith(key.toLowerCase()))
            {
                displayName = typeToDisplayName.get(key);
            }
        }
        
        return displayName;
    }
    
    /**
     * 
     */
    private void loadMap()
    {
        // AAA
        typeToDisplayName.put(FLAK_37, "Flak 37");
        typeToDisplayName.put(FLAK_38, "Flak 38");
        typeToDisplayName.put(AAA_52K, "Type 52-K AA");
        typeToDisplayName.put(AAA_61K, "Type 61-K AA");
        typeToDisplayName.put(MG34_AAA, "MG-34 AA");
        typeToDisplayName.put(MAKSIM_AAA, "Maksim 4 AA");


        // Artillery
        typeToDisplayName.put(LEFH18_ARTY, "artillery piece");
        typeToDisplayName.put(ML20_ARTY, "artillery piece");
        typeToDisplayName.put(PAK38_AT, "anti tank gun");
        typeToDisplayName.put(PAK40_AT, "anti tank gun");
        typeToDisplayName.put(ZIS_2_AT, "anti tank gun");
        typeToDisplayName.put(ZIS_3_AT, "anti tank gun");

        // Spotlights
        typeToDisplayName.put(SPOTLIGHT_DE, "spotlight");
        typeToDisplayName.put(SPOTLIGHT_RU, "spotlight");

        typeToDisplayName.put(AC_BA10M, "BA-10m");
        typeToDisplayName.put(AC_BA64, "BA-64");

        // Trucks
        typeToDisplayName.put(TRUCK_OPEL, "truck");

        typeToDisplayName.put(TRUCK_FORD, "truck");
        
        // Train
        typeToDisplayName.put(TRAIN_LOCOMOTIVE_G8, "locomotive");
        typeToDisplayName.put(TRAIN_CAR_WAGON, "train car");
        typeToDisplayName.put(TRAIN_CAR_TANKER, "train tanker car");
        typeToDisplayName.put(TRAIN_CAR_AAA, "train AAA car");
    }
}
