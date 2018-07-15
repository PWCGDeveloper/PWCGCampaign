package pwcg.campaign.api;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;

public interface IPWCGObject
{
    void write(BufferedWriter writer) throws PWCGException;
}