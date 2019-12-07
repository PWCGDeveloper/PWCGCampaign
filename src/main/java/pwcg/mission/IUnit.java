package pwcg.mission;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;

public interface IUnit
{
    int getIndex();
    ICountry getCountry() throws PWCGException;
    List<IUnit> getLinkedUnits() ;
    void addLinkedUnit (IUnit unit) throws PWCGException;
    void write(BufferedWriter writer) throws PWCGException;
}
