package pwcg.dev.weaponsmodanalyzer;

import java.util.ArrayList;
import java.util.List;

public class  ModLimitationPeriod
{
    String period;
    List<ModLimitationElement> modLimitationElements = new ArrayList<>();
    
    public void print()
    {
        System.out.println("    " + period);
        for (ModLimitationElement element: modLimitationElements)
        {
            element.print();
        }
    }
}
