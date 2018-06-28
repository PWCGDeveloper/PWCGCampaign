package pwcg.campaign.ww2.ground.vehicle;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGIOException;

abstract class AAA extends Vehicle
{
	public void write(BufferedWriter writer) throws PWCGIOException
	{
		super.write(writer);
		
        setName("AAA");     
        setDesc("AAA");
	}
}
