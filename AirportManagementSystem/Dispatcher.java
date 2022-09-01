

public class Dispatcher extends DispatcherBase {
    // TODO: replace with hash structure
    private PlaneHash planes;
    private int waitIndex = 0000;

    @Override
    public int size() {
        return this.planes.length;
    }

    @Override
    public void addPlane(String planeNumber, String time) {
        planes.add(planeNumber, time);
    }

    @Override
    public String allocateLandingSlot(String currentTime) {
        int searchIndex = Integer.parseInt(currentTime.replace(":", ""));
        Plane[] waitingPlane = this.planes.getAtIndex(waitIndex);
        if(waitingPlane != null) {
//           if(searchIndex >= waitIndex)
//            {
//                //then check the next 5 minutes
//            }
           return waitingPlane[0].getPlaneNumber();
       }

        return null;
    }

    @Override
    public String emergencyLanding(String planeNumber) {
        Plane searchResult = this.planes.getPlane(planeNumber);
        if (searchResult == null)
        {
            return null;
        }

        return allocateLandingSlot(searchResult.getTime());
    }

    @Override
    public boolean isPresent(String planeNumber) {
       return planes.isPresent(planeNumber);
    }

    /* Implement all the necessary methods of src.Dispatcher here */
}

/* Add any additional helper classes here */
//TODO: New hash structure
class PlaneHash
{
    private Plane[][] planes = new Plane[1440][0];
    public int length;

    public void add(String planeNumber, String time)
    {
        int index = Integer.parseInt(time.replace(":", ""));
        Plane plane = new Plane(planeNumber, time);
        planes[index] = addPlane(plane, index);
    }

    public boolean isPresent(String planeNumber)
    {
        Plane searchResult = getPlane(planeNumber);

        return searchResult == null;
    }

    public Plane getPlane(String planeNumber)
    {
        for (int i = 0; i < planes.length; i++)
        {
            for (int j = 0; j < planes[i].length; j++)
            {
                if (planes[i][j].getPlaneNumber() == planeNumber)
                {
                    return planes[i][j];
                }
            }
        }

        return null;
    }

    public Plane[] getAtIndex(int index)
    {
        return planes[index];
    }

    private Plane[] addPlane(Plane plane, int hour)
    {
        //consider doubling
        int i;
        Plane[] newPlanes = new Plane[planes[hour].length + 1];

        for(i = 0; i < planes[hour].length; i++) {
            newPlanes[i] = planes[hour][i];
        }
        newPlanes[planes[hour].length] = plane;

        return newPlanes;
    }
}