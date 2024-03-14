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
            score = function(particle.position)
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


def crossover(parent_individual, mutant_individual, CR):

    D = len(parent_individual)

    d = np.random.randint(0, D)

    crossover_individual = np.array(
        [mutant_individual[j] if (np.random.rand() < CR or j == d) else parent_individual[j] for j in range(D)])

    return crossover_individual


def depso(num_particles, num_iterations, min_values, max_values, dimensions, function, inertia_weight, cognitive_weight, social_weight,F=0.5, CR = 0.9):
    global global_best_score, global_best_position

    particles = [Particle(min_values, max_values, dimensions) for _ in range(num_particles)]
    global_best_score = float('inf')

    for _ in range(num_iterations):
        for particle in particles:
            score = function(particle.position)
            if score < particle.best_score:
                particle.best_score = score
                particle.best_position = np.copy(particle.position)

            candidates = np.random.choice(num_particles, 2, replace=False)
            a, b = [particles[idx].position for idx in candidates]

            lambda_value = np.random.rand()

            mutant = lambda_value * particles[np.argmin([function(vector.position) for vector in particles])].position + (
                        1 - lambda_value) * particle.position + F * (a - b)

            trial_vector = crossover(particle.position, mutant, CR)

            if function(trial_vector) < score:
                particle.position = trial_vector

            if score < global_best_score:
                global_best_score = score
                global_best_position = np.copy(particle.position)

        for particle in particles:
            particle.velocity = (particle.velocity * inertia_weight +
                                 cognitive_weight * random.random() * (particle.best_position - particle.position) +
                                 social_weight * random.random() * (global_best_position - particle.position))
            particle.position += particle.velocity

    return global_best_position, global_best_score

num_particles = 200
num_iterations = 500
dimensions = 20


# for _ in range(3):
#     global_best_position, global_best_score = pso(num_particles, num_iterations, -100, 100, dimensions, sphere_function, 0.7, 1, 1)
#     print("Best position:", global_best_position)
#     print("Best score:", global_best_score)

global_best, global_score = depso(num_particles,num_iterations,-100,100,dimensions,sphere_function,0.8,1,1)
print("Best position:", global_best)
print("Best score:", global_score)