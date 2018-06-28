package pwcg.campaign.api;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;

public interface IStaticPlane
{
    public void setPosition(Coordinate position);
    public void setOrientation(Orientation orientation);
    void write(BufferedWriter writer) throws PWCGException;
}