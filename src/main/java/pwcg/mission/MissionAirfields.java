package pwcg.mission;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;

public class MissionAirfields
{
    private Map<String, Airfield> fieldSet = new TreeMap<>();

    public MissionAirfields (Map<String, Airfield> fieldSet)
    {
        this.fieldSet = fieldSet;
    }

    public List<Airfield> getFieldsForPatrol() throws PWCGException 
    {
        return new ArrayList<>(fieldSet.values());
    }
}
