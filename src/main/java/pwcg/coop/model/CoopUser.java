package pwcg.coop.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoopUser
{
    private String username;
    private Map<Integer, CoopPersona> userPersonas = new HashMap<>();
    

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public void addPersona(CoopPersona persona)
    {
        userPersonas.put(persona.getSerialNumber(), persona);
    }
    
    public void removePersona(CoopPersona persona)
    {
        userPersonas.remove(persona.getSerialNumber());
    }
    
    public void getPersona(int serialNumber)
    {
        userPersonas.get(serialNumber);
    }

    public List<CoopPersona> getUserPersonas()
    {
        return new ArrayList<CoopPersona>(userPersonas.values());
    }
    
    
}
