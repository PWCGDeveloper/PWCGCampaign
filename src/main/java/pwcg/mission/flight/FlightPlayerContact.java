package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.List;

public class FlightPlayerContact implements IFlightPlayerContact
{
    private List<Integer> contactWithPlayer = new ArrayList<Integer>();
    private double closestContactWithPlayerDistance = -1.0;

    @Override
    public int getFirstContactWithPlayer()
    {
        int firstContactWithPlayer = -1;
        
        if (contactWithPlayer.size() > 0)
        {
            firstContactWithPlayer = contactWithPlayer.get(0);
        }
        
        return firstContactWithPlayer;
    }

    @Override
    public int getLastContactWithPlayer()
    {
        int lastContactWithPlayer = -1;
        
        if (contactWithPlayer.size() > 0)
        {
            lastContactWithPlayer = contactWithPlayer.get(contactWithPlayer.size() - 1);
        }
        
        return lastContactWithPlayer;
    }

    @Override
    public void setContactWithPlayer(int contact)
    {
        contactWithPlayer.add(contact);
    }


    @Override
    public double getClosestContactWithPlayerDistance()
    {
        return this.closestContactWithPlayerDistance;
    }

    @Override
    public void setClosestContactWithPlayerDistance(double newClosestDistance)
    {
        if (newClosestDistance > 0.0)
        {
            if (this.closestContactWithPlayerDistance <= 0.0 || newClosestDistance < this.closestContactWithPlayerDistance)
            {
                this.closestContactWithPlayerDistance = newClosestDistance;
            }
        }
    }

    @Override
    public List<Integer> getContactWithPlayer()
    {
        return contactWithPlayer;
    }

    @Override
    public void setContactWithPlayer(List<Integer> contactWithPlayer)
    {
        this.contactWithPlayer = contactWithPlayer;
    }

}
