package pwcg.dev.weaponsmodanalyzer;

public class  ModLimitationElement
{
    String modificationName;
    boolean isAvailable; 
    
    public void print()
    {
        System.out.println("        " + modificationName + ": " + isAvailable);
    }    
}
