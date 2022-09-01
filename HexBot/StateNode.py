import constants
#import math
class StateNode:
    # Some parts of this code were adapted from COMP3702 Tutorial 3
    # solution code ("tutorial3.py" by njc, available on COMP3702
    # Blackboard page, retrieved 19 Aug 2022)

    def __init__(self, environment, state, parent, prev_action, path_steps, path_cost):
        self.environment = environment
        self.state = state
        self.parent = parent
        self.prev_action = prev_action
        self.path_steps = path_steps
        self.path_cost = path_cost

    def __lt__(self, other):
        return self.path_cost < other.path_cost

    def get_path(self):
        path = []
        current_state_node = self
        while current_state_node.prev_action is not None:
            path.append(current_state_node.prev_action)
            current_state_node = current_state_node.parent
        path.reverse()
        return path

    def get_successors(self):
        successors = []
        for a in constants.ROBOT_ACTIONS:
            success, cost, next_state = self.environment.perform_action(self.state, a)
            if success:
                successors.append(StateNode(self.environment, next_state, self, a, self.path_steps + 1, self.path_cost + cost))
        return successors

    def get_heuristics(self):
        # manhattan distance
        distances = []
        for target in self.environment.target_list:
            for widget in self.state.widget_centres:
                widget_tgt = abs(widget[0] - target[0]) + abs(widget[1] - target[1])
                distances.append(widget_tgt)
        return min(distances)

    # def get_heuristics_v(self):
    #     # vancouver distance
    #
    #     # the formula for this code were adapted from a paper by Peter Yap
    #     # link: https://svn.sable.mcgill.ca/sable/courses/COMP763/oldpapers/yap-02-grid-based.pdf
    #     # retrieved 22 Aug 2022
    #
    #     for target in self.environment.target_list:
    #         for widget in self.state.widget_centres:
    #             dx = widget[0] - target[0]
    #             dy = widget[1] - target[1]
    #
    #             if widget[1] < target[1] and (dx % 2) != 0:
    #                 correction = widget[0] % 2
    #             elif widget[1] > target[1] and (dx % 2) != 0:
    #                 correction = target[0] % 2
    #             else:
    #                 correction = 0
    #
    #     return max(0, dy - math.floor(dx / 2)) + dx - correction

    # def get_heuristics_e(self):
    #     # euclidean distance
    #
    #     distances = []
    #     for target in self.environment.target_list:
    #         for widget in self.state.widget_centres:
    #             widget_tgt = math.sqrt(abs(widget[0] - target[0])**2 + abs(widget[1] - target[1])**2)
    #             distances.append(widget_tgt)
    #     return min(distances)


