import numpy as np
import pso
from matplotlib import pyplot as plt


def sphere_function(x):
    return np.sum(x ** 2)


def f2(x):
    suma = 0
    for i in range(len(x)):
        suma += np.square(x[i] - i)
    return suma


def shwefel(x):
    suma = np.sum(abs(x) ** 2)
    multi = 1
    for i in range(len(x)):
        multi *= abs(x[i])
    return suma + multi


def rosenbrock(x):
    suma = 0
    for i in range(len(x) - 1):
        suma += 100 * (x[i + 1] - x[i] ** 2) ** 2 + (x[i] - 1) ** 2

    return suma


def griewank(x):
    suma = np.sum(x ** 2)
    suma /= 4000
    multi = 1
    for i in range(len(x)):
        multi *= np.cos(x[i]/np.sqrt(i+1))
    return suma - multi + 1


def rastrigin(x):
    suma = 0
    for i in range(len(x)):
        suma += x[i] ** 2 - 10 * np.cos(2 * np.pi * x[i]) + 10

    return suma

def crossover(parent_individual, mutant_individual, CR):

    D = len(parent_individual)

    d = np.random.randint(0, D)

    crossover_individual = np.array(
        [mutant_individual[j] if (np.random.rand() < CR or j == d) else parent_individual[j] for j in range(D)])

    return crossover_individual


def differential_evolution(function, bounds, dimensions, population_size, max_iterations, F=0.8, CR = 0.7):
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

        # best_fitness = function(population[np.argmin([function(vector) for vector in population])])
        # if best_fitness < tolerance:
        #     break

    best_individual = population[np.argmin([function(vector) for vector in population])]

    return best_individual, function(best_individual)


def run_DE(num_of_runs,function, bounds, dimensions, population_size, max_iterations):
    results = []
    for _ in range(num_of_runs):
        _, result = differential_evolution(function, bounds, dimensions, population_size, max_iterations)
        results.append(result)

    results_sorted = sorted(results)
    results_to_mean = results_sorted
    results_avg = np.mean(results_to_mean)

    return results_avg


def run_curent_best(num_of_runs, num_particles, num_iterations, min_values, max_values, dimensions, function, inertia_weight, cognitive_weight, social_weight):
    results = []
    for _ in range(num_of_runs):
        _, result = pso.depso_current_best(num_particles, num_iterations, min_values, max_values, dimensions, function, inertia_weight, cognitive_weight, social_weight)
        results.append(result)

    results_sorted = sorted(results)
    results_to_mean = results_sorted
    results_avg = np.mean(results_to_mean)

    return results_avg


def run_best_mid(num_of_runs, num_particles, num_iterations, min_values, max_values, dimensions, function, inertia_weight, cognitive_weight, social_weight):
    results = []
    for _ in range(num_of_runs):
        _, result = pso.depso_best_to_mid(num_particles, num_iterations, min_values, max_values, dimensions, function, inertia_weight, cognitive_weight, social_weight)
        results.append(result)

    results_sorted = sorted(results)
    results_to_mean = results_sorted
    results_avg = np.mean(results_to_mean)

    return results_avg


functions = [sphere_function, f2, shwefel, rosenbrock, griewank, rastrigin]
bounds = [(-100, 100), (-100, 100), (-10, 10), (-2.048, 2.048), (-600, 600), (-5.12,5.12)]

parameters = {"populatio_size": [30, 100, 50, 100, 50],
              "max_iterations": [1500, 1000, 1000, 1000, 500],
              "interia_weight": [0.5, 0.5, 0.7, 0.8, 0.8],
              "cognitive_weight": [0.8, 1, 1, 1, 1],
              "social_weight": [1, 1, 0.8, 1, 1]
              }


for i in range(len(functions)):
    if functions[i] != griewank:
        continue
    de_results_arr = []
    depso_current_best_arr = []
    depso_best_mid_arr = []

    barWidth = 0.25
    fig = plt.subplots(figsize=(12, 8))

    for j in range(5):
        de_results = run_DE(1, functions[i], bounds[i],20,parameters["populatio_size"][j], parameters["max_iterations"][j])
        depso_current_best = run_curent_best(1,parameters["populatio_size"][j], parameters["max_iterations"][j], bounds[i][0],
                                             bounds[i][1],20,
                                             functions[i],parameters["interia_weight"][j], parameters["cognitive_weight"][j],
                                             parameters["social_weight"][j])
        depso_best_mid = run_best_mid(1, parameters["populatio_size"][j], parameters["max_iterations"][j],
                                             bounds[i][0], bounds[i][1], 20, functions[i],
                                             parameters["interia_weight"][j], parameters["cognitive_weight"][j],
                                             parameters["social_weight"][j])

        de_results_arr.append(de_results)
        depso_current_best_arr.append(depso_current_best)
        depso_best_mid_arr.append(depso_best_mid)

    br1 = np.arange(len(de_results_arr))
    br2 = [x + barWidth for x in br1]
    br3 = [x + barWidth for x in br2]

    plt.bar(br1, de_results_arr, color='r', width=barWidth,
            edgecolor='grey', label='DE')
    plt.bar(br2, depso_current_best_arr, color='g', width=barWidth,
            edgecolor='grey', label='DEPSO')
    plt.bar(br3, depso_best_mid_arr, color='b', width=barWidth,
            edgecolor='grey', label='DEPSO/mid')

    # Adding Xticks
    plt.xlabel('Data set', fontweight='bold', fontsize=15)
    plt.ylabel('Values', fontweight='bold', fontsize=15)
    plt.yscale('log')
    plt.xticks([r + barWidth for r in range(len(de_results_arr))],
               ['set 1', 'set 2', 'set 3', 'set 4', 'set 5'])
    plt.title(str(functions[i].__name__), fontweight='bold')

    plt.legend()
    plt.show()





