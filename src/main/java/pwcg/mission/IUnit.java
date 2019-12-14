package pwcg.mission;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;

public interface IUnit
{
    int getIndex();
    List<IUnit> getLinkedUnits() ;
    void addLinkedUnit (IUnit unit) throws PWCGException;
    void write(BufferedWriter writer) throws PWCGException;
}
