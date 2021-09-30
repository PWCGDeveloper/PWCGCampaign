package pwcg.dev.weaponsmodanalyzer;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class WeaponsModAnalyzer
{    
    ModLimitations modLimitations = new ModLimitations();
    private Map <String, Map<String, ModLimitationRaw>> rawModLimitations =  new TreeMap<>();
    private Map <String, List<PlaneMod>> planeMods =  new TreeMap<>();
    
    public static void main(String[] args)
    {
        try
        {
            WeaponsModAnalyzer  weaponsModAnalyzer = new WeaponsModAnalyzer();
            weaponsModAnalyzer.analyze();
            
            System.out.println("****** DONE ************");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void analyze() throws Exception
    {
        ModAvailabilityReader availabilityReader = new ModAvailabilityReader();
        rawModLimitations  = availabilityReader.readMapModificationAvailabilityFiles();

        PlaneModSetReader planeModSetReader = new PlaneModSetReader();
        planeMods = planeModSetReader.readPlaneModFiles();
        
        ModAvailabilityConsolidator consolidator = new ModAvailabilityConsolidator();
        modLimitations = consolidator.consolidate(planeMods, rawModLimitations);
        
        modLimitations.print();
    }
}
