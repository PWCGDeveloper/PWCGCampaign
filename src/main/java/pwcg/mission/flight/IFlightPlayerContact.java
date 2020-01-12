package pwcg.mission.flight;

import java.util.List;

public interface IFlightPlayerContact
{

    int getFirstContactWithPlayer();

    int getLastContactWithPlayer();

    void setContactWithPlayer(int contact);

    double getClosestContactWithPlayerDistance();

    void setClosestContactWithPlayerDistance(double newClosestDistance);

    List<Integer> getContactWithPlayer();

    void setContactWithPlayer(List<Integer> contactWithPlayer);

}