import numpy as np


def differential_evolution(fitness_func, bounds, pop_size=50, max_iter=1000, F=0.5, CR=0.9, tol=1e-5, dimension=20):

    dim = len(bounds)
    lower_bound, upper_bound = np.array(bounds).T
    pop = np.random.rand(pop_size, dim) * (upper_bound - lower_bound) + lower_bound
    fitness = np.array([fitness_func(ind) for ind in pop])
    best_idx = np.argmin(fitness)
    best_solution, best_fitness = pop[best_idx], fitness[best_idx]

    for i in range(max_iter):
        for j in range(pop_size):
            idxs = [idx for idx in range(pop_size) if idx != j]
            a, b, c = pop[np.random.choice(idxs, 3, replace=False)]
            mutant = pop[j] + F * (best_solution - pop[j]) + F * (a - b)
            mutant = np.clip(mutant, lower_bound, upper_bound)
            crossover = np.random.rand(dim) < CR
            if not np.any(crossover):
                crossover[np.random.randint(0, dim)] = True
            trial = np.where(crossover, mutant, pop[j])
            trial_fitness = fitness_func(trial)
            if trial_fitness < fitness[j]:
                pop[j], fitness[j] = trial, trial_fitness
                if trial_fitness < best_fitness:
                    best_solution, best_fitness = trial, trial_fitness
        if np.std(fitness) < tol:
            break

    return best_solution, best_fitness

# Example usage:
def sphere_function(x):
    return np.sum(x**2)

bounds = [(-100, 100)] * 20
best_solution, best_fitness = differential_evolution(sphere_function, bounds)
print("Best solution:", best_solution)
print("Best fitness:", best_fitness)
