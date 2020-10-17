package pwcg.coop.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoopUserOlde
{
    private String username;
    private Map<Integer, CoopPersonaOlde> userPersonas = new HashMap<>();
    

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public void addPersona(CoopPersonaOlde persona)
    {
        userPersonas.put(persona.getSerialNumber(), persona);
    }
    
    public void removePersona(CoopPersonaOlde persona)
    {
        userPersonas.remove(persona.getSerialNumber());
    }
    
    public void getPersona(int serialNumber)
    {
        userPersonas.get(serialNumber);
    }

    public List<CoopPersonaOlde> getUserPersonas()
    {
        return new ArrayList<CoopPersonaOlde>(userPersonas.values());
    }
    
    
}
