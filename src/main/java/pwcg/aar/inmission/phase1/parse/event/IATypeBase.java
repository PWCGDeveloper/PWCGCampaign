package pwcg.aar.inmission.phase1.parse.event;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGIOException;

public interface IATypeBase
{
    void write(BufferedWriter writer) throws PWCGIOException;

    int getSequenceNum();
}