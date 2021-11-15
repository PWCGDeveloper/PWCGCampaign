package pwcg.core.logfiles.event;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;

public interface IATypeBase
{
    void write(BufferedWriter writer) throws PWCGException;

    int getSequenceNum();
}