import numpy as np
import matplotlib.pyplot as plt
import random
import math
import os

# Function: Sphere Function
def sphere_function(variables_values):
    return np.sum(np.array(variables_values)**2)

def f2(x):
    suma = 0
    for i in range(len(x)):
        suma += np.square(x[i] - i)
    return suma


def shwefel(x):
    x = np.array(x)  # Konwertuj listę na tablicę NumPy, jeśli to konieczne
    suma = np.sum(np.abs(x) ** 2)
    multi = 1
    for i in range(len(x)):
        multi *= np.abs(x[i])
    return suma + multi

def rosenbrock(x):
    suma = 0
    for i in range(len(x) - 1):
        suma += 100 * (x[i + 1] - x[i] ** 2) ** 2 + (x[i] - 1) ** 2

    return suma

# Function: Initialize Variables
def initial_position(swarm_size, min_values, max_values, target_function):
    position = np.zeros((swarm_size, len(min_values) + 1))
    velocity = np.zeros((swarm_size, len(min_values)))
    frequency = np.zeros((swarm_size, 1))
    rate = np.zeros((swarm_size, 1))
    loudness = np.zeros((swarm_size, 1))
    for i in range(swarm_size):
        for j in range(len(min_values)):
            position[i, j] = random.uniform(min_values[j], max_values[j])
        position[i, -1] = target_function(position[i, 0:len(min_values)])
        rate[i, 0] = int.from_bytes(os.urandom(8), byteorder="big") / ((1 << 64) - 1)
        loudness[i, 0] = random.uniform(1, 2)
    return position, velocity, frequency, rate, loudness

# Function: Update Position
def update_position(position, velocity, frequency, rate, loudness, best_ind, alpha, gamma, fmin, fmax, count, min_values, max_values, target_function):
    position_temp = np.zeros_like(position)
    for i in range(position.shape[0]):
        beta = int.from_bytes(os.urandom(8), byteorder="big") / ((1 << 64) - 1)
        frequency[i, 0] = fmin + (fmax - fmin) * beta
        for j in range(len(max_values)):
            velocity[i, j] = velocity[i, j] + (position[i, j] - best_ind[j]) * frequency[i, 0]
        for k in range(len(max_values)):
            position_temp[i, k] = position[i, k] + velocity[i, k]
            if position_temp[i, k] > max_values[k]:
                position_temp[i, k] = max_values[k]
                velocity[i, k] = 0
            elif position_temp[i, k] < min_values[k]:
                position_temp[i, k] = min_values[k]
                velocity[i, k] = 0
        position_temp[i, -1] = target_function(position_temp[i, 0:len(max_values)])
        rand = int.from_bytes(os.urandom(8), byteorder="big") / ((1 << 64) - 1)
        if rand > rate[i, 0]:
            for L in range(len(max_values)):
                position_temp[i, L] = best_ind[L] + random.uniform(-1, 1) * loudness.mean()
                if position_temp[i, L] > max_values[L]:
                    position_temp[i, L] = max_values[L]
                    velocity[i, L] = 0
                elif position_temp[i, L] < min_values[L]:
                    position_temp[i, L] = min_values[L]
                    velocity[i, L] = 0
            position_temp[i, -1] = target_function(position_temp[i, 0:len(max_values)])
        rand = int.from_bytes(os.urandom(8), byteorder="big") / ((1 << 64) - 1)
        if rand < position[i, -1] and position_temp[i, -1] <= position[i, -1]:
            for m in range(len(max_values)):
                position[i, m] = position_temp[i, m]
            position[i, -1] = target_function(position[i, 0:len(max_values)])
            rate[i, 0] = rate[i, 0] * (1 - math.exp(-gamma * count))
            loudness[i, 0] = alpha * loudness[i, -1]
        value = np.copy(position[position[:, -1].argsort()][0, :])
        if best_ind[-1] > value[-1]:
            best_ind = np.copy(value)
    return position, velocity, frequency, rate, loudness, best_ind

# Bat Algorithm
def bat_algorithm(swarm_size, min_values, max_values, iterations, alpha, gamma, fmin, fmax, target_function):
    count = 0
    position, velocity, frequency, rate, loudness = initial_position(swarm_size, min_values, max_values, target_function)
    best_ind = np.copy(position[position[:, -1].argsort()][0, :])
    while count <= iterations:
        position, velocity, frequency, rate, loudness, best_ind = update_position(position, velocity, frequency, rate, loudness, best_ind, alpha, gamma, fmin, fmax, count, min_values, max_values, target_function)
        count += 1
    return best_ind[-1]


def initialize_butterflies(n_butterflies, min_values, max_values,dimensions):
    butterflies = []
    for _ in range(n_butterflies):
        butterfly = [random.uniform(min_values, max_values) for i in range(dimensions)]
        butterflies.append(butterfly)
    return butterflies


def levy_flight(dim, scale=1.5):
    beta = 1.5
    sigma = (np.pi * beta) ** 0.5
    u = np.random.normal(0, sigma, dim)
    v = np.random.normal(0, 1, dim)
    step = u / np.abs(v) ** (1 / beta)
    step[np.isnan(step)] = 0
    step[np.isinf(step)] = np.sign(step[np.isinf(step)]) * 1e-15
    return scale * step


def move_to_best(butterfly, best, f):
    new_butterfly = []
    for i in range(len(butterfly)):
        r = random.random()
        new = butterfly[i] + (r**2 * best[i] - butterfly[i]) * f
        new_butterfly.append(new)
    return new_butterfly


def move_randomly(butterfly, best, f):
    new_butterfly = []
    for i in range(len(butterfly)):
        indices = list(range(len(butterfly)))
        indices.remove(i)
        j = random.choice(indices)
        indices.remove(j)
        k = random.choice(indices)
        r = random.random()
        new = butterfly[i] + (r**2 * butterfly[j]-butterfly[k]) * f
        new_butterfly.append(new)
    return new_butterfly



def run_BOA(n_butterflies,n_iterations,min_bound,max_bound,dim,target,c,a=0.1,p=0.8):
    butterflies = initialize_butterflies(n_butterflies, min_bound, max_bound, dim)
    fragrances = np.zeros(len(butterflies))
    for index,_ in enumerate(range(n_iterations)):
        for i in range(len(butterflies)):
            fragrances[i] = c * target(butterflies[i]) ** a
        best_butterfly = min(butterflies, key=target)

        for index1, butterfly in enumerate(butterflies):
            if random.random() < p:
                butterflies[index1] = move_to_best(butterfly,best_butterfly,fragrances[index1])
            else:
                butterflies[index1] = move_randomly(butterfly,best_butterfly,fragrances[index1])
        a = 0.1 + 0.2 * (index/n_iterations)
    return target(best_butterfly)

functions = [sphere_function, f2, shwefel, rosenbrock]
function_names = ['Sphere', 'f2', 'Schwefel', 'Rosenbrock']
bounds = [(-100, 100), (-100, 100), (-10, 10), (-2.048, 2.048)]

# Parametry algorytmów
n_butterflies = 20
n_iterations = 100
dim = 20
swarm_size = 10
iterations = 100
alpha = 0.5
gamma = 0.9
fmin = 0.5
fmax = 0.8

# Struktury do przechowywania wyników
results_boa = np.zeros((len(functions), 5))
results_bat = np.zeros((len(functions), 5))

# Uruchomienie algorytmów dla każdej funkcji z odpowiednimi przedziałami
for func_index, (func, bound) in enumerate(zip(functions, bounds)):
    min_bound, max_bound = bound
    for trial in range(5):
        boa_result = run_BOA(n_butterflies, n_iterations, min_bound, max_bound, dim, func, c=0.1)
        results_boa[func_index, trial] = boa_result
        bat_result = bat_algorithm(swarm_size, [min_bound]*dim, [max_bound]*dim, iterations, alpha, gamma, fmin, fmax, func)
        results_bat[func_index, trial] = bat_result

# Obliczenie średnich
mean_boa = np.mean(results_boa, axis=1)
mean_bat = np.mean(results_bat, axis=1)

# Rysowanie wykresów
x = np.arange(len(functions))  # Lokalizacje etykiet
width = 0.35  # Szerokość słupków

fig, ax = plt.subplots()
rects1 = ax.bar(x - width/2, mean_boa, width, label='BOA')
rects2 = ax.bar(x + width/2, mean_bat, width, label='Bat')

ax.set_yscale('log')
ax.set_ylabel('Average Result')
ax.set_title('Results by function with specific bounds')
ax.set_xticks(x)
ax.set_xticklabels(function_names)
ax.legend()

plt.show()