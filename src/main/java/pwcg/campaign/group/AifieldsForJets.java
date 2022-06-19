package pwcg.campaign.group;

import java.util.ArrayList;
import java.util.List;

public class AifieldsForJets
{
    private List<String> airfieldsForJets = new ArrayList<>();
    
    private static AifieldsForJets instance = new AifieldsForJets();
    
    private AifieldsForJets ()     
    {
        airfieldsForJets.add("Essen-Mulheim");
        airfieldsForJets.add("Hopsten");
        airfieldsForJets.add("Hesepe");
        airfieldsForJets.add("Giebelstadt");
        airfieldsForJets.add("Rheine");
        airfieldsForJets.add("Achmer");
        airfieldsForJets.add("Munster-Handorf");
    }
    
    public static AifieldsForJets getInstance()
    {
        return instance;
    }
    
    public boolean isJetAirfield(String airfieldName)
    {
        return airfieldsForJets.contains(airfieldName);
    }
}
