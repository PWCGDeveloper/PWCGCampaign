package pwcg.mission.ground.org;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.mission.mcu.BaseFlightMcu;

public interface IGroundElement
{
    void createGroundUnitElement() throws PWCGException;

    void linkToNextElement(int targetIndex);

    int getEntryPoint();

    BaseFlightMcu getEntryPointMcu();

    void write(BufferedWriter writer) throws PWCGIOException;
    
    void validate() throws PWCGException;

}
