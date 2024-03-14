import random
import math
import numpy as np

def bunkin_function_n6(x):
    return 100 * np.sqrt(np.abs(x[:, 1] - 0.01 * x[:, 0] ** 2)) + 0.01 * np.abs(x[:, 0] + 10)


def sphere_function(x):
    return np.sum(x ** 2)

def booth_function(x):
    return (x[:, 0] + 2 * x[:, 1] - 7) ** 2 + (2 * x[:, 0] + x[:, 1] - 5) ** 2


class Particle:
    def __init__(self, min_values, max_values, dimensions):
        self.position = np.random.uniform(min_values, max_values, dimensions)
        self.velocity = np.zeros(dimensions)
        self.best_position = np.copy(self.position)
        self.best_score = float('inf')


def pso(num_particles, num_iterations, min_values, max_values, dimensions, function, inertia_weight, cognitive_weight, social_weight):
    global global_best_score, global_best_position

    particles = [Particle(min_values, max_values, dimensions) for _ in range(num_particles)]
    global_best_score = float('inf')

    for _ in range(num_iterations):
        for particle in particles:
            score = function(np.expand_dims(particle.position, axis=0))
            if score < particle.best_score:
                particle.best_score = score
                particle.best_position = np.copy(particle.position)

            if score < global_best_score:
                global_best_score = score
                global_best_position = np.copy(particle.position)

        for particle in particles:
            particle.velocity = (particle.velocity * inertia_weight +
                                 cognitive_weight * random.random() * (particle.best_position - particle.position) +
                                 social_weight * random.random() * (global_best_position - particle.position))
            particle.position += particle.velocity

    return global_best_position, global_best_score



num_particles = 75
num_iterations = 1000
dimensions = 20

for _ in range(3):
    global_best_position, global_best_score = pso(num_particles, num_iterations, -100, 100, dimensions, sphere_function, 0.5, 1, 1)
    print("Best position:", global_best_position)
    print("Best score:", global_best_score)
