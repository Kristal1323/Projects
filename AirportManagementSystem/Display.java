class DisplayRandom extends DisplayRandomBase {

    public DisplayRandom(String[] csvLines) {
        super(csvLines);
    }

    @Override
    public Plane[] sort() {
        Plane[] planes = getData();
        quickSort(planes, 0, planes.length - 1);
        return planes;
    }

    /* Implement all the necessary methods here */
    private int partition(Plane[] planes, int startIndex, int endIndex) {
        int pivotIndex = startIndex;
        Plane pivot = planes[pivotIndex];
        while (startIndex < endIndex) {
            while (startIndex < planes.length
                    && planes[startIndex].compareTo(pivot) <= 0) {
                startIndex++;
            }
            while (planes[endIndex].compareTo(pivot) == 1) {
                endIndex--;
            }
            if (startIndex < endIndex) {
                Plane temp = planes[startIndex];
                planes[startIndex] = planes[endIndex];
                planes[endIndex] = temp;
            }
        }
        planes[pivotIndex] = planes[endIndex];
        planes[endIndex] = pivot;
        return endIndex;
    }

    private void quickSort(Plane[] planes, int startIndex, int endIndex) {
        if (startIndex < endIndex) {
            int p = partition(planes, startIndex, endIndex);
            quickSort(planes, startIndex, p-1);
            quickSort(planes, p+1, endIndex);
        }
    }
}

class DisplayPartiallySorted extends DisplayPartiallySortedBase {

    public DisplayPartiallySorted(String[] scheduleLines, String[] extraLines) {
        super(scheduleLines, extraLines);
    }

    @Override
    Plane[] sort() {
        Plane[] planes = getSchedule();
        Plane[] extraPlanes = getExtraPlanes();
        Plane[] totalPlanes = combineAllPlanes(planes, extraPlanes);
        insertionSort(totalPlanes);
        return totalPlanes;
    }

    /* Implement all the necessary methods here */

    private void insertionSort(Plane[] totalPlanes) {
        int planesLen = totalPlanes.length;
        for (int i = 1; i < planesLen; i++) {
            Plane key = totalPlanes[i];
            int j = i - 1;

            while(j >=0 && totalPlanes[j].compareTo(key) == 1) {
                totalPlanes[j + 1] = totalPlanes[j];
                j = j - 1;
            }
            totalPlanes[j + 1] = key;
        }
    }

    private Plane[] combineAllPlanes(Plane[] planes, Plane[] extraPlanes) {
        Plane[] allPlanes = new Plane[planes.length + extraPlanes.length];
        for (int i = 0; i < planes.length; i++)
        {
            allPlanes[i] = planes[i];
        }

        for (int i = planes.length; i < extraPlanes.length + planes.length; i++)
        {
            allPlanes[i] = extraPlanes[i - planes.length];
        }
        return allPlanes;
    }
}
