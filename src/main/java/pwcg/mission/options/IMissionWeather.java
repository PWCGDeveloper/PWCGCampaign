package pwcg.mission.options;

import java.util.Date;

import pwcg.core.exception.PWCGException;

public interface IMissionWeather 
{    
    String getClearSkys() throws PWCGException;
    String getLightSkys() throws PWCGException;
    String getAverageSkys() throws PWCGException;
    String getHeavySkys() throws PWCGException;
    String getOvercastSkys() throws PWCGException;
    int getTemperature(Date date);
}
