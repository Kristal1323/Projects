import java.util.*;
import java.util.stream.Collectors;

public class Airport extends AirportBase {

    private Graph terminalGraph;
    private Map<ShuttleBase, Integer> shuttleCapacity;
    /**
     * Creates a new src.AirportBase instance with the given capacity.
     *
     * @param capacity capacity of the airport shuttles
     *                 (same for all shuttles)
     */
    public Airport(int capacity) {
        super(capacity);
        terminalGraph = new Graph();
        shuttleCapacity = new HashMap<>();
    }

    @Override
    public TerminalBase opposite(ShuttleBase shuttle, TerminalBase terminal) {
        if (shuttle.getOrigin().getId().equals(terminal.getId())) {
            return shuttle.getDestination();
        }

        return shuttle.getOrigin();
    }

    @Override
    public TerminalBase insertTerminal(TerminalBase terminal) {
        return terminalGraph.addVertex(terminal);
    }

    @Override
    public ShuttleBase insertShuttle(TerminalBase origin, TerminalBase destination, int time) {
        ShuttleBase shuttle = new Shuttle(origin, destination, time);
        if (!shuttleCapacity.containsKey(shuttle)) {
            shuttleCapacity.put(shuttle, this.getCapacity());
        }
        return terminalGraph.addEdge(shuttle);
    }

    @Override
    public boolean removeTerminal(TerminalBase terminal) {
        return terminalGraph.removeVertex(terminal);
    }

    @Override
    public boolean removeShuttle(ShuttleBase shuttle) {
        if (shuttleCapacity.containsKey(shuttle)) {
            shuttleCapacity.remove(shuttle);
        }
        return terminalGraph.removeEdge(shuttle);
    }

    @Override
    public List<ShuttleBase> outgoingShuttles(TerminalBase terminal) {
        return terminalGraph.getEdges(terminal);
    }

    @Override
    public Path findShortestPath(TerminalBase origin, TerminalBase destination) {
        DijkstraAlgorithm pathCalculator = new DijkstraAlgorithm(terminalGraph);
        return pathCalculator.getShortestPath(origin, destination);
    }

    @Override
    public Path findFastestPath(TerminalBase origin, TerminalBase destination) {
        DijkstraAlgorithm pathCalculator = new DijkstraAlgorithm(terminalGraph);
        return pathCalculator.getFastestPath(origin, destination);
    }

    /* Implement all the necessary methods of the src.Airport here */

    static class Terminal extends TerminalBase {
        /**
         * Creates a new TerminalBase instance with the given terminal ID
         * and waiting time.
         *
         * @param id          terminal ID
         * @param waitingTime waiting time for the terminal, in minutes
         */
        public Terminal(String id, int waitingTime) {
            super(id, waitingTime);
        }

        /* Implement all the necessary methods of the Terminal here */
    }

    static class Shuttle extends ShuttleBase {
        /**
         * Creates a new ShuttleBase instance, travelling from origin to
         * destination and requiring 'time' minutes to travel.
         *
         * @param origin      origin terminal
         * @param destination destination terminal
         * @param time        time required to travel, in minutes
         */
        public Shuttle(TerminalBase origin, TerminalBase destination, int time) {
            super(origin, destination, time);
        }

        /* Implement all the necessary methods of the Shuttle here */
    }

    private class Graph {
        private Map<TerminalBase, List<ShuttleBase>> map;

        public Graph() {
            this.map = new HashMap<>();
        }

        public TerminalBase addVertex(TerminalBase vertex) {
            this.map.put(vertex, new LinkedList<>());
            return vertex;
        }

        public ShuttleBase addEdge(ShuttleBase shuttle) {
            if(!map.containsKey(shuttle.getOrigin())) {
                addVertex(shuttle.getOrigin());
            }
            if(!map.containsKey(shuttle.getDestination())) {
                addVertex(shuttle.getDestination());
            }

            map.get(shuttle.getOrigin()).add(shuttle);
            // map.get(destination).add(new Shuttle(destination, origin, time));

            return shuttle;
        }

        public List<ShuttleBase> getEdges(TerminalBase terminal) {
            if (map.containsKey(terminal)) {
                List<ShuttleBase> shuttles = map.get(terminal);
                return shuttles;
            }
            return null;
        }

        public List<ShuttleBase> getShuttles() {
            return map.values().stream().flatMap(List::stream).collect(Collectors.toList());
        }

        public List<TerminalBase> getTerminals() {
            return new ArrayList<>(map.keySet());
        }

        public boolean removeVertex(TerminalBase terminal) {
            List<ShuttleBase> itemRemoved = map.remove(terminal);
            if (itemRemoved != null) {
                return true;
            }

            return false;
        }

        public boolean removeEdge(ShuttleBase shuttle) {
            return map.get(shuttle.getOrigin()).remove(shuttle);
        }
    }

    private class DijkstraAlgorithm {
        private final List<TerminalBase> terminals;
        private final List<ShuttleBase> shuttles;
        private Set<TerminalBase> settledNodes;
        private Set<TerminalBase> unSettledNodes;
        private Map<TerminalBase, TerminalBase> predecessors;
        private Map<TerminalBase, Integer> distance;
        private Map<TerminalBase, Integer> shuttleDistance;

        public DijkstraAlgorithm(Graph graph) {
            // create a copy of the array so that we can operate on this array
            this.terminals = new ArrayList<TerminalBase>(graph.getTerminals());
            this.shuttles = new ArrayList<ShuttleBase>(graph.getShuttles());
        }

        public Path getShortestPath(TerminalBase origin, TerminalBase destination)
        {
            this.evaluate(origin, true);
            LinkedList<TerminalBase> steps = this.getPath(destination);
            int totalTime = 0;
            for(int i = 1; i < steps.size(); i++) {
                totalTime += this.getDistance(steps.get(i - 1), steps.get(i));
            }

            return new Path(steps, totalTime);
        }

        public Path getFastestPath(TerminalBase origin, TerminalBase destination)
        {
            this.evaluate(origin, false);
            LinkedList<TerminalBase> steps = this.getPath(destination);
            return new Path(steps, distance.get(destination));
        }

        private void evaluate(TerminalBase origin, boolean shortestPath) {
            settledNodes = new HashSet<TerminalBase>();
            unSettledNodes = new HashSet<TerminalBase>();
            distance = new HashMap<TerminalBase, Integer>();
            predecessors = new HashMap<TerminalBase, TerminalBase>();
            distance.put(origin, 0);
            unSettledNodes.add(origin);
            while (unSettledNodes.size() > 0) {
                TerminalBase node = getMinimum(unSettledNodes);
                settledNodes.add(node);
                unSettledNodes.remove(node);
                if (shortestPath) {
                    findMinimalShuttleCountDistance(node);
                }
                else {
                    findMinimalDistances(node);
                }

            }
        }

        private void findMinimalDistances(TerminalBase origin) {
            List<TerminalBase> adjacentNodes = getNeighbors(origin);
            for (TerminalBase destination : adjacentNodes) {
                if (getShortestDistance(destination) > getShortestDistance(origin)
                        + getDistance(origin, destination)) {
                    distance.put(destination, getShortestDistance(origin)
                            + getDistance(origin, destination));
                    predecessors.put(destination, origin);
                    unSettledNodes.add(destination);
                }
            }
        }

        private void findMinimalShuttleCountDistance(TerminalBase node)
        {
            List<TerminalBase> adjacentNodes = getNeighbors(node);
            for (TerminalBase target : adjacentNodes) {
                if (getShortestDistance(target) > getShortestDistance(node)
                        + getShuttleCount(node, target)) {
                    distance.put(target, getShortestDistance(node)
                            + getShuttleCount(node, target));
                    predecessors.put(target, node);
                    unSettledNodes.add(target);
                }
            }
        }

        private TerminalBase getMinimum(Set<TerminalBase> terminals) {
            TerminalBase minimum = null;
            for (TerminalBase terminal : terminals) {
                if (minimum == null) {
                    minimum = terminal;
                } else {
                    if (getShortestDistance(terminal) < getShortestDistance(minimum)) {
                        minimum = terminal;
                    }
                }
            }
            return minimum;
        }

        public int getDistance(TerminalBase origin, TerminalBase destination) {
            for (ShuttleBase shuttle : shuttles) {
                if (shuttle.getOrigin().equals(origin)
                        && shuttle.getDestination().equals(destination)) {
                    return shuttle.getTime() + origin.getWaitingTime();
                }
            }
            throw new RuntimeException("Should not happen");
        }

        private int getShuttleCount(TerminalBase origin, TerminalBase destination) {
            int count = 0;
            for (ShuttleBase shuttle : shuttles) {
                if (shuttle.getOrigin().equals(origin)
                        && shuttle.getDestination().equals(destination)) {
                    count++;
                }
            }
            return count;
        }


        private List<TerminalBase> getNeighbors(TerminalBase terminal) {
            List<TerminalBase> neighbors = new ArrayList<TerminalBase>();
            for (ShuttleBase shuttle : shuttles) {
                if (shuttle.getOrigin().equals(terminal)
                        && !isSettled(shuttle.getDestination())) {
                    neighbors.add(shuttle.getDestination());
                }
            }
            return neighbors;
        }

        private boolean isSettled(TerminalBase terminal) {
            return settledNodes.contains(terminal);
        }

        private int getShortestDistance(TerminalBase destination) {
            Integer d = distance.get(destination);
            if (d == null) {
                return Integer.MAX_VALUE;
            } else {
                return d;
            }
        }

        private LinkedList<TerminalBase> getPath(TerminalBase target) {
            LinkedList<TerminalBase> path = new LinkedList<TerminalBase>();
            TerminalBase step = target;
            // check if a path exists
            if (predecessors.get(step) == null) {
                return null;
            }
            path.add(step);
            while (predecessors.get(step) != null) {
                step = predecessors.get(step);
                path.add(step);
            }
            // Put it into the correct order
            Collections.reverse(path);
            return path;
        }
    }


}
