public class Plane extends PlaneBase {

    public Plane(String planeNumber, String time) {
        super(planeNumber, time);
    }

    @Override
    public int compareTo(PlaneBase o) {
        var myTime = getTime().trim().split(":");
        var theirTime = o.getTime().trim().split(":");

        if (Integer.parseInt(myTime[0]) == Integer.parseInt(theirTime[0]))
        {
            if((Integer.parseInt(myTime[1]) == Integer.parseInt(theirTime[1])))
            {
                // hours and minutes same. compare plane number
                return getPlaneNumber().compareToIgnoreCase(o.getPlaneNumber());
            }
            else if (Integer.parseInt(myTime[1]) < Integer.parseInt(theirTime[1]))
            {
                // hours same. my minutes are less
                return -1;
            }

            // hours same but my minutes are greater.
            return 1;
        }
        else if (Integer.parseInt(myTime[0]) < Integer.parseInt(theirTime[0]))
        {
            return -1;
        }

        return 1;
    }

    /* Implement all the necessary methods of src.Plane here */
}
