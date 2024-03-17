import random
import numpy as np


def sphere_function(x):
    return np.sum(x ** 2)


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

def exponential_crossover(parent_individual, mutant_individual, CR):
    D = len(parent_individual)
    crossover_individual = np.empty_like(parent_individual)

    for j in range(D):
        if np.random.rand() < CR:
            crossover_individual[j] = mutant_individual[j]
        else:
            crossover_individual[j] = parent_individual[j]

    return crossover_individual


# DESPO current to best
def depso_current_best(num_particles, num_iterations, min_values, max_values, dimensions, function, inertia_weight, cognitive_weight, social_weight,F=0.5, CR = 0.9):
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


# DESPO DE/best-to-mid/2/exp
def depso_best_to_mid(num_particles, num_iterations, min_values, max_values, dimensions, function, inertia_weight, cognitive_weight, social_weight,F=0.5, CR = 0.9):
    global global_best_score, global_best_position

    particles = [Particle(min_values, max_values, dimensions) for _ in range(num_particles)]
    global_best_score = float('inf')

    for _ in range(num_iterations):
        for particle in particles:
            score = function(particle.position)
            if score < particle.best_score:
                particle.best_score = score
                particle.best_position = np.copy(particle.position)

            candidates = np.random.choice(num_particles, 4, replace=False)
            a, b, c,d = [particles[idx].position for idx in candidates]

            lambda_value = np.random.rand()
            best = particles[np.argmin([function(vector.position) for vector in particles])].position
            mid = np.mean([vector.position for vector in particles], axis=0)

            mutant = lambda_value * best + (1 - lambda_value) * mid + F * (a - b) + F * (c - d)

            trial_vector = exponential_crossover(particle.position, mutant, CR)

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

