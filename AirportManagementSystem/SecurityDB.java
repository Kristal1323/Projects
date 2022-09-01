public class SecurityDB extends SecurityDBBase {
    private Passenger[] buckets;
    private int maxSize;
    private int count;
    /**
     * Creates an empty hashtable and a variable to count non-empty elements.
     *
     * @param numPlanes             number of planes per day
     * @param numPassengersPerPlane number of passengers per plane
     */
    public SecurityDB(int numPlanes, int numPassengersPerPlane) {
        super(numPlanes, numPassengersPerPlane);
        maxSize = getPrimeLargerThan( numPlanes * numPassengersPerPlane);
        buckets = new Passenger[maxSize];

    }

    private int getPrimeLargerThan(int num) {
        for(int i=2; i < 1021; i++) {
            if(num % i != 0 && i > num) {
                return i;
            }
        }
        return 1021;
    }

    @Override
    public int calculateHashCode(String key) {
        char[] charArray = key.toCharArray();
        int[] componentSums = new int[key.length()];

        for(int i=0; i<key.length(); i++) {
            int asciiValue = (int) charArray[i];
            if ( i == 0)
            {
                componentSums[i] = 1 + asciiValue;
            }
            else {
                componentSums[i] = componentSums[ i - 1 ] + asciiValue;
            }
        }

        int cumulativeValue = 0;
        for (int i = 0; i < componentSums.length; i++)
        {
            cumulativeValue += componentSums[i];
        }
        return cumulativeValue;
    }

    @Override
    public int size() {
        return maxSize;
    }

    @Override
    public String get(String passportId) {
        if (count() == 0) {
            return null;
        }

        Passenger p = find(passportId);
        if (p != null) {
            return p.getName();
        }

        return null;
    }

    public Passenger find(String passportId) {
        if (count() == 0) {
            return null;
        }

        int hash = getIndex(passportId);
        for(int i =0; i< maxSize; i++) {
            if (buckets[hash] != null && buckets[hash].getPassportId().equals(passportId)) {
                return buckets[hash];
            }
            if (hash + 1 < maxSize) {
                hash++;
            } else {
                hash = 0;
            }
        }

        return null;
    }

    @Override
    public boolean remove(String passportId) {

        if (count() == 0) {
            return false;
        }

        int hash = getIndex(passportId);

        for(int i =0; i< maxSize; i++) {
            if(buckets[hash] != null && buckets[hash].getPassportId().equals(passportId)) {
                buckets[hash] = null;
                count--;
                return true;
            }
            if(hash + 1 < maxSize){
                hash++;
            } else {
                hash = 0;
            }
        }
        return false;
    }

    private void resizeHashTable() {
        Passenger[] newPassengers = new Passenger[1021];
        for(int i = 0; i < this.buckets.length; i++) {
            newPassengers[i] = buckets[i];
        }

        buckets = newPassengers;
    }

    @Override
    public boolean addPassenger(String name, String passportId) {
        int hash = getIndex(passportId);
        if (count() == maxSize) {
            if (maxSize < 1021) {
                resizeHashTable();
            }
        }

        Passenger p = find(passportId);
        if (p != null) {
            if (!p.getName().equals(name))
            {
                System.err.println("suspicious");
                return false;
            }
            if (p.getEntryCount() > 5)
            {
                System.err.println("suspicious");
                return false;

            }

            p.RecordEntry();
            return true;

        }

        for(int i =0; i< maxSize; i++) {
            if(buckets[hash] == null) {
                buckets[hash] = new Passenger(name, passportId);
                count++;
                return true;
            }

            if(hash + 1 < maxSize){
                hash++;
            } else {
                hash = 0;
            }
        }
        return false;
    }

    @Override
    public int count() {
        return count;
    }

    @Override
    public int getIndex(String passportId) {
        return calculateHashCode(passportId) % maxSize;
    }

//        public static void main(String[] args) {
//        SecurityDB db = new SecurityDB(3, 2);
//        String id = new String("Asb23f");
//        boolean isAdded = db.addPassenger("Rob Bekker", "Asb23f");
//        System.out.println(db.contains(id));
////        // add
////        boolean result = db.addPassenger("Rob Bekker", "Asb23f");
////        assert result;
////        assert db.contains("Asb23f");
////
////        // count
////        assert db.count() == 3;
////
////        // del
////        db.remove("MKSD23");
////        assert !db.contains("MKSD23");
////        assert db.contains("Asb23f");
////
////        // hashcodes
////        assert db.calculateHashCode("Asb23f") == 1717;
////
////        // suspicious
////        db = new SecurityDB(3, 2);
////        db.addPassenger("Rob Bekker", "Asb23f");
////        db.addPassenger("Robert Bekker", "Asb23f");
////        // Should print a warning to stderr
////
////        db = new SecurityDB(3, 2);
////        db.addPassenger("Rob Bekker", "Asb23f");
////        db.addPassenger("Rob Bekker", "Asb23f");
////        db.addPassenger("Rob Bekker", "Asb23f");
////        db.addPassenger("Rob Bekker", "Asb23f");
////        db.addPassenger("Rob Bekker", "Asb23f");
////        db.addPassenger("Rob Bekker", "Asb23f");
//      }
}

/* Add any additional helper classes here */
class Passenger{
    private final String name;
    private final String passportId;
    private int entryCount = 1;

    public Passenger(String name, String passportId) {
        this.name = name;
        this.passportId = passportId;
    }

    public String getName() {
        return name;
    }

    public String getPassportId() {
        return passportId;
    }

    public int getEntryCount() { return entryCount; }

    public void RecordEntry() {
        entryCount++;
    }
}