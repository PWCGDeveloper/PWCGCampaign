package pwcg.campaign.ww1.ground.vehicle;


import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.api.IGroundUnitNames;

public class RoFGroundUnitNames implements IGroundUnitNames
{
    // AAA
    public static final String MM_77_AA = "77mmaaa";
    public static final String PDR_13_AA = "13pdraaa";
    public static final String LMG08_AA = "lmg08aaa";
    public static final String HOTCHKISS_AA = "hotchkissaaa";

    // Artillery
    public static final String FK96_ARTY = "fk96";
    public static final String M13_ARTY = "m13";
    public static final String QF45_ARTY = "45qf";
    public static final String MM_75_ARTY = "75fg1897";

    // MG
    public static final String LMG08_MG = "lmg08";
    public static final String HOTCHKISS_MG = "hotchkiss";

    // Infantry
    public static final String INF_DE = "infantryde";  
    public static final String INF_EN = "infantryen";
    public static final String INF_PLATOON_DE = "platoonde";
    public static final String INF_PLATOON_DE2 = "platoonde02";
    public static final String INF_PLATOON_FR = "platoonfr02";
    public static final String INF_PLATOON_FR2 = "infantryen";
    public static final String INF_PLATOON_EN = "platoonen";
    public static final String INF_PLATOON_EN2 = "platoonen02";

    // Pillboxes
    public static final String PILLBOX_1 = "pillbox01";
    public static final String PILLBOX_2 = "pillbox02";
    public static final String PILLBOX_3 = "pillbox03";
    public static final String PILLBOX_4 = "pillbox04";
    
    // Ships
    public static final String SHIP_CRUISER_DE = "gercruiser";
    public static final String SHIP_SUBMARINE_DE = "gersubmarine";
    public static final String SHIP_CRUISER_EN = "hmscruiser";
    public static final String SHIP_CARGO = "ship_cargo";
    
    public static final String SHIP_DRIFTER_EN = " hmsDrifter";
    public static final String SHIP_DRIFTER_DE = "gerdrifter";
    public static final String SHIP_DRIFTER_EN_6PDR = "hmsdrifter6pdraaa";

    // Spotlight trucks
    public static final String SPOTLIGHT_DE = "benz_p";
    public static final String SPOTLIGHT_EN = "quad_p";
    
    // Tanks
    public static final String TANK_MK4F_DE = "mk4fger";
    public static final String TANK_MK4M_DE = "mk4mger";
    public static final String TANK_A7_DE = "a7v";

    public static final String TANK_FT17C_FR = "ft17c";
    public static final String TANK_FT17M_FR = "ft17m";
    public static final String TANK_WHIPPET = "whippet";
    public static final String TANK_CA1_FR = "ca1";
    public static final String TANK_STCHAMOND_FR = "stchamond";

    public static final String TANK_MK4F_EN = "mk4f";
    public static final String TANK_MK4M_EN = "mk4m";
    public static final String TANK_MK5F_EN = "mk5f";
    public static final String TANK_MK5M_EN = "mk5m";

    // Trucks
    public static final String TRUCK_BENZ_OPEN_DE = "benz_open";
    public static final String TRUCK_BENZ_SOFT_DE = "benz_soft";
    public static final String TRUCK_DIAM_SOFT_DE = "daimlermarienfelde_s";
    public static final String TRUCK_DIAM_OPEN_DE = "daimlermarienfelde";

    public static final String TRUCK_LEYLAND_OPEN_EN = "leyland";
    public static final String TRUCK_LEYLAND_SOFT_EN = "leylands";
    public static final String TRUCK_QUAD_OPEN_EN = "quad";
    public static final String TRUCK_QUADA_OPEN_EN = "quada";

    // Train
    public static final String TRAIN_LOCOMOTIVE_G8 = "g8";
    public static final String TRAIN_COAL_CAR_G8 = "g8t";
    public static final String TRAIN_CAR_PAASSENGER = "pass";
    public static final String TRAIN_CAR_PAASSENGERC = "passc";
    public static final String TRAIN_CAR_PLATFORM = "platformb";
    public static final String TRAIN_CAR_PLATFORM_EMPTY = "platformemptyb";
    public static final String TRAIN_CAR_GONDALA = "gondolab";
    public static final String TRAIN_CAR_GONDALA_EMPTY = "gondolanb";
    public static final String TRAIN_CAR_BOX = "boxb";
    public static final String TRAIN_CAR_BOX_NB = "boxnb";

    // Wagons
    public final String WAGON = "guncarriagede";
    
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
            if (key.equalsIgnoreCase(typeName))
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
        typeToDisplayName.put(MM_77_AA, "77mm AA");
        typeToDisplayName.put(PDR_13_AA, "13 pdr AA");
        typeToDisplayName.put(LMG08_AA, "LMG 08 AA");
        typeToDisplayName.put(HOTCHKISS_AA, "Hotchkiss AA");

        // Artillery
        typeToDisplayName.put(FK96_ARTY, "FK 96 artillery");
        typeToDisplayName.put(M13_ARTY, "M13 artillery");
        typeToDisplayName.put(QF45_ARTY, "4.5 Inch artillery");
        typeToDisplayName.put(MM_75_ARTY, "75mm artillery");

        // MG
        typeToDisplayName.put(LMG08_MG, "LMG 08 MG");
        typeToDisplayName.put(HOTCHKISS_MG, "Hotchkiss MG");

        // Infantry
        typeToDisplayName.put(INF_DE, "infantry");  
        typeToDisplayName.put(INF_EN, "infantry");
        typeToDisplayName.put(INF_PLATOON_DE, "infantry");
        typeToDisplayName.put(INF_PLATOON_DE2, "infantry");
        typeToDisplayName.put(INF_PLATOON_FR, "infantry");
        typeToDisplayName.put(INF_PLATOON_FR2, "infantry");
        typeToDisplayName.put(INF_PLATOON_EN, "infantry");
        typeToDisplayName.put(INF_PLATOON_EN2, "infantry");

        // Pillboxes
        typeToDisplayName.put(PILLBOX_1, "bunker");
        typeToDisplayName.put(PILLBOX_2, "bunker");
        typeToDisplayName.put(PILLBOX_3, "bunker");
        typeToDisplayName.put(PILLBOX_4, "bunker");
        
        // Ships
        typeToDisplayName.put(SHIP_CRUISER_DE, "cruiser");
        typeToDisplayName.put(SHIP_SUBMARINE_DE, "submarine");
        typeToDisplayName.put(SHIP_CRUISER_EN, "cruiser");
        typeToDisplayName.put(SHIP_CARGO, "cargo ship");
        
        typeToDisplayName.put(SHIP_DRIFTER_EN, " drifter");
        typeToDisplayName.put(SHIP_DRIFTER_DE, "drifter");
        typeToDisplayName.put(SHIP_DRIFTER_EN_6PDR, "armed drifter");

        // Spotlight trucks
        typeToDisplayName.put(SPOTLIGHT_DE, "spotlight truck");
        typeToDisplayName.put(SPOTLIGHT_EN, "spotlight truck");
        
        // Tanks
        typeToDisplayName.put(TANK_MK4F_DE, "MK4 female tank");
        typeToDisplayName.put(TANK_MK4M_DE, "MK4 male tank");
        typeToDisplayName.put(TANK_A7_DE, "A7v tank");

        typeToDisplayName.put(TANK_FT17C_FR, "FT17c tank");
        typeToDisplayName.put(TANK_FT17M_FR, "FT17m tank");
        typeToDisplayName.put(TANK_WHIPPET, "Whippet tank");
        typeToDisplayName.put(TANK_CA1_FR, "CA1 tank");
        typeToDisplayName.put(TANK_STCHAMOND_FR, "St Chamond tank");

        typeToDisplayName.put(TANK_MK4F_EN, "MK4 female");
        typeToDisplayName.put(TANK_MK4M_EN, "MK4 male");
        typeToDisplayName.put(TANK_MK5F_EN, "MK5 female");
        typeToDisplayName.put(TANK_MK5M_EN, "MK5 male");

        // Trucks
        typeToDisplayName.put(TRUCK_BENZ_OPEN_DE, "truck");
        typeToDisplayName.put(TRUCK_BENZ_SOFT_DE, "truck");
        typeToDisplayName.put(TRUCK_DIAM_SOFT_DE, "truck");
        typeToDisplayName.put(TRUCK_DIAM_OPEN_DE, "truck");

        typeToDisplayName.put(TRUCK_LEYLAND_OPEN_EN, "truck");
        typeToDisplayName.put(TRUCK_LEYLAND_SOFT_EN, "truck");
        typeToDisplayName.put(TRUCK_QUAD_OPEN_EN, "truck");
        typeToDisplayName.put(TRUCK_QUADA_OPEN_EN, "truck");

        typeToDisplayName.put(WAGON, "Wagon");
        
        // Train
        typeToDisplayName.put(TRAIN_LOCOMOTIVE_G8, "locomotive");
        typeToDisplayName.put(TRAIN_COAL_CAR_G8, "coal car");
        typeToDisplayName.put(TRAIN_CAR_PAASSENGER, "train car");
        typeToDisplayName.put(TRAIN_CAR_PAASSENGERC, "train car");
        typeToDisplayName.put(TRAIN_CAR_PLATFORM, "train car");
        typeToDisplayName.put(TRAIN_CAR_PLATFORM_EMPTY, "train car");
        typeToDisplayName.put(TRAIN_CAR_GONDALA, "train car");
        typeToDisplayName.put(TRAIN_CAR_GONDALA_EMPTY, "train car");
        typeToDisplayName.put(TRAIN_CAR_BOX, "train car");
        typeToDisplayName.put(TRAIN_CAR_BOX_NB, "train car");
    }
}
