package pwcg.campaign.ww2.ground.vehicle;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.api.IGroundUnitNames;


public class BoSGroundUnitNames implements IGroundUnitNames
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

    // Tanks
    public static final String TANK_PZ38T = "pz38t";
    public static final String TANK_PZKW_IIIH = "pziii-h";
    public static final String TANK_PZKW_IIIL = "pziii-l";
    public static final String TANK_PZKW_IV_F1 = "pziv-f1";
    public static final String TANK_PZKW_IV_G = "pziv-g";
    public static final String TANK_STUG_III_E = "stug37l24";
    public static final String TANK_STUG_III_L = "stug40l43";
    public static final String TANK_MARDER_III_H = "marderiii-h";

    public static final String AC_BA10M = "ba10m";
    public static final String AC_BA64 = "ba64";
    public static final String TANK_BT7M = "bt7m";
    public static final String TANK_KVI_41 = "kv1-41";
    public static final String TANK_KVI_42 = "kv1-42";
    public static final String TANK_T34_76_41 = "t34-76stz-41";
    public static final String TANK_T34_76 = "t34-76stz";
    public static final String TANK_T70 = "t70";


    // Trucks
    public static final String TRUCK_OPEL = "opel-blitz";
    public static final String CAR_HORSCH = "horch830";
    public static final String HT_SDKFZ10_AA = "sdkfz10-flak38";
    public static final String HT_SDKFZ251_1C = "sdkfz251-1c";
    public static final String HT_SDKFZ251_SZF = "sdkfz251-szf";

    public static final String TRUCK_FORD = "ford-g917";
    public static final String TRUCK_GAZ_M4_AA = "gaz-aa-m4-aa";
    public static final String TRUCK_GAZ_AA = "gaz-aa";
    public static final String TRUCK_GAZ = "gaz-m";
    public static final String TRUCK_ZIS = "zis5";

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

        // Tanks
        typeToDisplayName.put(TANK_PZ38T, "Pz38t");
        typeToDisplayName.put(TANK_PZKW_IIIH, "Pzkw III");
        typeToDisplayName.put(TANK_PZKW_IIIL, "Pzkw III");
        typeToDisplayName.put(TANK_PZKW_IV_F1, "Pzkw IV");
        typeToDisplayName.put(TANK_PZKW_IV_G, "Pzkw IV");
        typeToDisplayName.put(TANK_STUG_III_E, "Stug III");
        typeToDisplayName.put(TANK_STUG_III_L, "Stug III");
        typeToDisplayName.put(TANK_MARDER_III_H, "Marder III");

        typeToDisplayName.put(AC_BA10M, "BA-10m");
        typeToDisplayName.put(AC_BA64, "BA-64");
        typeToDisplayName.put(TANK_BT7M, "BT-7");
        typeToDisplayName.put(TANK_KVI_41, "KV-I");
        typeToDisplayName.put(TANK_KVI_42, "KV-I");
        typeToDisplayName.put(TANK_T34_76_41, "T-34");
        typeToDisplayName.put(TANK_T34_76, "T-34");
        typeToDisplayName.put(TANK_T70, "T-70");

        // Trucks
        typeToDisplayName.put(TRUCK_OPEL, "truck");
        typeToDisplayName.put(CAR_HORSCH, "staff car");
        typeToDisplayName.put(HT_SDKFZ10_AA, "half track");
        typeToDisplayName.put(HT_SDKFZ251_1C, "half track");
        typeToDisplayName.put(HT_SDKFZ251_SZF, "half track");

        typeToDisplayName.put(TRUCK_FORD, "truck");
        typeToDisplayName.put(TRUCK_GAZ_M4_AA, "truck");
        typeToDisplayName.put(TRUCK_GAZ_AA, "truck");
        typeToDisplayName.put(TRUCK_GAZ, "truck");
        typeToDisplayName.put(TRUCK_ZIS, "truck");
        
        // Train
        typeToDisplayName.put(TRAIN_LOCOMOTIVE_G8, "locomotive");
        typeToDisplayName.put(TRAIN_CAR_WAGON, "train car");
        typeToDisplayName.put(TRAIN_CAR_TANKER, "train tanker car");
        typeToDisplayName.put(TRAIN_CAR_AAA, "train AAA car");
    }
}
