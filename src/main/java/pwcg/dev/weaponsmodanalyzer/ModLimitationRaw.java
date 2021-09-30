package pwcg.dev.weaponsmodanalyzer;

public class  ModLimitationRaw
{
    String plane;
    String map;
    String period;
    String modificationRequired; 
    String modificationDenied; 
    
    public void print()
    {
        System.out.println("    " + map);
        System.out.println("    " + period);
        System.out.println("    " + modificationRequired);
        System.out.println("    " + modificationDenied);
    }
}
