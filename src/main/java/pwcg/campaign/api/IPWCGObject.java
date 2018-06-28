package pwcg.campaign.api;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public interface IPWCGObject
{

    void initialize(Coordinate unitPosition, String name);

    void write(BufferedWriter writer) throws PWCGException;

    String getName();

    void setName(String name);

    Coordinate getPosition();

    void setPosition(Coordinate unitPosition);
}