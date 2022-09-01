from StateNode import StateNode
"""
solution.py

This file is a template you should use to implement your solution.

You should implement 

COMP3702 2022 Assignment 1 Support Code

Last updated by njc 01/08/22
"""
import heapq


class Solver:

    def __init__(self, environment, loop_counter):
        self.environment = environment
        self.loop_counter = loop_counter
        #
        # TODO: Define any class instance variables you require here.
        #
        self.init_state = environment.get_init_state()

    def solve_ucs(self):
        """
        Find a path which solves the environment using Uniform Cost Search (UCS).
        :return: path (list of actions, where each action is an element of ROBOT_ACTIONS)
        """
        #
        #
        # TODO: Implement your UCS code here
        #
        # === Important ================================================================================================
        # To ensure your code works correctly with tester, you should include the following line of code in your main
        # search loop:
        #
        # self.loop_counter.inc()
        #
        # e.g.
        # while loop_condition():
        #   self.loop_counter.inc()
        #   ...
        #
        # ==============================================================================================================
        #
        #

        # Some parts of this code were adapted from COMP3702 Tutorial 3
        # solution code ("tutorial3.py" by njc, available on COMP3702
        # Blackboard page, retrieved 20 Aug 2022)
        visited = {self.init_state: 0}
        pq = [StateNode(self.environment, self.init_state, None, None, 0, 0)]
        heapq.heapify(pq)
        expanded_nodes = 0
        while pq:
            self.loop_counter.inc()
            expanded_nodes += 1
            node = heapq.heappop(pq)

            if self.environment.is_solved(node.state):
                # print("no. of nodes in frontier:" + str(len(pq)))
                # print("no. of nodes in visited:" + str(len(visited)))
                return node.get_path()

            successors = node.get_successors()
            for succ in successors:
                if succ.state not in visited.keys() or succ.path_cost < visited[succ.state]:
                    visited[succ.state] = succ.path_cost
                    heapq.heappush(pq, succ)

        return None

    def solve_a_star(self):
        """
        Find a path which solves the environment using A* search.
        :return: path (list of actions, where each action is an element of ROBOT_ACTIONS)
        """

        #
        #
        # TODO: Implement your A* search code here
        #
        # === Important ================================================================================================
        # To ensure your code works correctly with tester, you should include the following line of code in your main
        # search loop:
        #
        # self.loop_counter.inc()
        #
        # e.g.
        # while loop_condition():
        #   self.loop_counter.inc()
        #   ...
        #
        # ==============================================================================================================
        #
        #

        # Some parts of this code were adapted from COMP3702 Tutorial 3
        # solution code ("tutorial3.py" by njc, available on COMP3702
        # Blackboard page, retrieved 20 Aug 2022)
        visited = {self.init_state: 0}
        initial_node = StateNode(self.environment, self.init_state, None, None, 0, 0)
        tst = initial_node.get_heuristics()
        pq = [(initial_node.get_heuristics(), initial_node)]
        heapq.heapify(pq)
        expanded_nodes = 0
        while pq:
            self.loop_counter.inc()
            expanded_nodes += 1
            node = heapq.heappop(pq)[1]

            if self.environment.is_solved(node.state):
                # print("no. of nodes in frontier:" + str(len(pq)))
                # print("no. of nodes in visited:" + str(len(visited)))
                return node.get_path()

            successors = node.get_successors()
            for succ in successors:
                if succ.state not in visited.keys() or succ.path_cost < visited[succ.state]:
                    visited[succ.state] = succ.path_cost
                    heapq.heappush(pq, (succ.get_heuristics(), succ))

        return None

    #
    #
    # TODO: Add any additional methods here
    #
    #
