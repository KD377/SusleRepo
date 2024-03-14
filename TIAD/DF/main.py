import numpy as np


def sphere_function(x):
    return np.sum(x ** 2)


def crossover(parent_individual, mutant_individual, CR):

    D = len(parent_individual)

    d = np.random.randint(0, D)

    crossover_individual = np.array(
        [mutant_individual[j] if (np.random.rand() < CR or j == d) else parent_individual[j] for j in range(D)])

    return crossover_individual


def differential_evolution(function, bounds, dimensions, population_size, max_iterations, F=0.5, CR = 0.9, tolerance=1e-4):
    population = np.random.uniform(bounds[0], bounds[1], size=(population_size, dimensions))

    for i in range(max_iterations):
        new_population = np.zeros_like(population)

        for j in range(population_size):
            candidates = np.random.choice(population_size,2,replace=False)
            a, b = population[candidates]

            lambda_value = np.random.rand()

            mutant = lambda_value * population[np.argmin([function(vector) for vector in population])] + (1 - lambda_value) * population[j] + F * (a - b)

            trial_vector = crossover(population[j], mutant, CR)
            if function(trial_vector) < function(population[j]):
                new_population[j] = trial_vector
            else:
                new_population[j] = population[j]

        population = new_population

        best_fitness = function(population[np.argmin([function(vector) for vector in population])])
        if best_fitness < tolerance:
            break

    best_individual = population[np.argmin([function(vector) for vector in population])]

    return best_individual


bounds = (-100, 100)
dimensions = 20
population_size = 50
max_iterations = 1000
F = 0.5
CR = 0.9
tolerance = 1e-4

best_solution = differential_evolution(sphere_function, bounds, dimensions, population_size, max_iterations, F, CR, tolerance)
print("Best solution found:", best_solution)
print("Fitness value of the best solution:", sphere_function(best_solution))

