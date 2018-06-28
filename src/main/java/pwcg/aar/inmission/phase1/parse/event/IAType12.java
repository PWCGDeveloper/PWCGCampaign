package pwcg.aar.inmission.phase1.parse.event;

import pwcg.campaign.api.ICountry;

public interface IAType12 extends IATypeBase
{
    String getId();
    String getName();
    String getType();
    ICountry getCountry();
    String getPid();
}