package pwcg.campaign.api;

import java.io.BufferedWriter;

import pwcg.campaign.context.Country;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;

public interface ICountry
{    
    ICountry copy();

    public String getCountryName();

    public String getNationality();

    public Side getSide();

    public Side getSideNoNeutral() throws PWCGException;

    boolean isNeutral();

    boolean isSameSide(ICountry otherCountry);

    boolean isCountryByCode(int testCountryCode);

    boolean isEnemy(ICountry otherCountry);

    boolean equals(ICountry otherCountry);

    void write(BufferedWriter writer) throws PWCGIOException, PWCGException;

    void writeAdjusted(BufferedWriter writer) throws PWCGIOException;

    int getCountryCode();

    Country getCountry();

    boolean isCountry(Country country);

}