import numpy as np
import matplotlib.pyplot as plt


def sphere_function(x):
    return np.sum(x ** 2)

def f2(x):
    suma = 0
    for i in range(len(x)):
        suma += np.square(x[i] - i)
    return suma

def schwefel(x):
    return 418.9829 * len(x) - np.sum(x * np.sin(np.sqrt(np.abs(x))))

def rosenbrock(x):
    suma = 0
    for i in range(len(x) - 1):
        suma += 100 * (x[i + 1] - x[i] ** 2) ** 2 + (x[i] - 1) ** 2
    return suma


def initialize_population(N, D, LB, UB):
    return LB + (UB - LB) * np.random.rand(N, D)


def update_position(X, X_best, LB, UB, t, max_t):
    N, D = X.shape
    a = np.arctanh(-t / max_t + 1)
    vb = np.random.uniform(-a, a, (N, D))
    vc = np.random.uniform(-1, 1, (N, D)) * (1 - t / max_t)
    r = np.random.rand(N, D)

    for i in range(N):
        for j in range(D):
            if r[i, j] < np.tanh(abs(X[i, j] - X_best[j])):
                X[i, j] = X_best[j] + vb[i, j]
            else:
                X[i, j] = X_best[j] + vc[i, j]
            X[i, j] = np.clip(X[i, j], LB, UB)

    return X


def crossover_and_mutation(Pi, G, LB, UB, pm):
    M, D = Pi.shape
    O = np.zeros((M, D))
    for i in range(M):
        for d in range(D):
            rd = np.random.rand()
            kd = np.random.randint(M)
            if sphere_function(Pi[i]) < sphere_function(Pi[kd]):
                O[i, d] = rd * Pi[i, d] + (1 - rd) * G[d]
            else:
                O[i, d] = Pi[kd, d]
            if np.random.rand() < pm:
                O[i, d] = LB + (UB - LB) * np.random.rand()
    return O


def selection(Ei, Oi):
    M = Ei.shape[0]
    for i in range(M):
        if sphere_function(Oi[i]) < sphere_function(Ei[i]):
            Ei[i] = Oi[i].copy()
    return Ei


def migrate(swarms, best_positions, fitness_values, threshold):
    num_swarms = len(swarms)
    for i in range(num_swarms):
        for j in range(i + 1, num_swarms):
            if abs(fitness_values[i] - fitness_values[j]) > threshold:
                if fitness_values[i] < fitness_values[j]:
                    donor, recipient = i, j
                else:
                    donor, recipient = j, i
                num_to_migrate = int(abs(fitness_values[donor] - fitness_values[recipient]) / max(fitness_values[donor], fitness_values[recipient]) * len(swarms[donor]))
                if num_to_migrate > 0:
                    sorted_indices = np.argsort(np.apply_along_axis(sphere_function, 1, swarms[donor]))
                    best_indices = sorted_indices[:num_to_migrate]
                    worst_indices = np.argsort(np.apply_along_axis(sphere_function, 1, swarms[recipient]))[-num_to_migrate:]
                    for k in range(num_to_migrate):
                        swarms[recipient][worst_indices[k]] = swarms[donor][best_indices[k]]


def multi_swarm_sma_with_pattern(num_swarms, swarm_size, D, LB, UB, max_t, migration_threshold, pm, func):
    swarms = [initialize_population(swarm_size, D, LB, UB) for _ in range(num_swarms)]
    best_positions = [None] * num_swarms
    best_fitness_values = [np.inf] * num_swarms
    patterns = [initialize_population(swarm_size, D, LB, UB) for _ in range(num_swarms)]

    for t in range(1, max_t + 1):
        for i in range(num_swarms):
            fitness = np.apply_along_axis(func, 1, swarms[i])
            best_idx = np.argmin(fitness)
            if fitness[best_idx] < best_fitness_values[i]:
                best_positions[i] = swarms[i][best_idx, :].copy()
                best_fitness_values[i] = fitness[best_idx]

            swarms[i] = update_position(swarms[i], best_positions[i], LB, UB, t, max_t)
            Oi = crossover_and_mutation(patterns[i], best_positions[i], LB, UB, pm)
            patterns[i] = selection(patterns[i], Oi)

        migrate(swarms, best_positions, best_fitness_values, migration_threshold)

    global_best_idx = np.argmin(best_fitness_values)
    global_best_position = best_positions[global_best_idx]
    global_best_fitness = best_fitness_values[global_best_idx]

    return global_best_position, global_best_fitness


def initialize_population1(N, D, LB, UB):
    return LB + (UB - LB) * np.random.rand(N, D)


def update_velocity1(velocity, position, personal_best, global_best, w, c1, c2):
    r1 = np.random.rand(*velocity.shape)
    r2 = np.random.rand(*velocity.shape)
    cognitive_component = c1 * r1 * (personal_best - position)
    social_component = c2 * r2 * (global_best - position)
    new_velocity = w * velocity + cognitive_component + social_component
    return new_velocity

def update_position1(position, velocity, LB, UB):
    new_position = position + velocity
    new_position = np.clip(new_position, LB, UB)
    return new_position


def crossover_and_mutation1(Pi, G, LB, UB, pm):
    M, D = Pi.shape
    O = np.zeros((M, D))
    for i in range(M):
        for d in range(D):
            rd = np.random.rand()
            kd = np.random.randint(M)
            if sphere_function(Pi[i]) < sphere_function(Pi[kd]):
                O[i, d] = rd * Pi[i, d] + (1 - rd) * G[d]
            else:
                O[i, d] = Pi[kd, d]
            if np.random.rand() < pm:
                O[i, d] = LB + (UB - LB) * np.random.rand()
    return O


def selection1(Ei, Oi):
    M = Ei.shape[0]
    for i in range(M):
        if sphere_function(Oi[i]) < sphere_function(Ei[i]):
            Ei[i] = Oi[i].copy()
    return Ei


def pso_with_pattern(num_particles, D, LB, UB, max_t, w, c1, c2, pm, func):
    position = initialize_population1(num_particles, D, LB, UB)
    velocity = np.zeros((num_particles, D))
    personal_best = position.copy()
    personal_best_fitness = np.apply_along_axis(func, 1, personal_best)
    global_best_idx = np.argmin(personal_best_fitness)
    global_best = personal_best[global_best_idx, :].copy()
    global_best_fitness = personal_best_fitness[global_best_idx]

    pattern = initialize_population1(num_particles, D, LB, UB)

    for t in range(max_t):
        velocity = update_velocity1(velocity, position, personal_best, global_best, w, c1, c2)
        position = update_position1(position, velocity, LB, UB)

        fitness = np.apply_along_axis(func, 1, position)
        better_fitness_mask = fitness < personal_best_fitness
        personal_best[better_fitness_mask] = position[better_fitness_mask]
        personal_best_fitness[better_fitness_mask] = fitness[better_fitness_mask]

        global_best_idx = np.argmin(personal_best_fitness)
        if personal_best_fitness[global_best_idx] < global_best_fitness:
            global_best = personal_best[global_best_idx, :].copy()
            global_best_fitness = personal_best_fitness[global_best_idx]

        Oi = crossover_and_mutation1(pattern, global_best, LB, UB, pm)
        pattern = selection1(pattern, Oi)

    return global_best, global_best_fitness


functions = [sphere_function, f2, schwefel, rosenbrock]
function_names = ['Sphere', 'f2', 'Schwefel', 'Rosenbrock']
bounds = [(-100, 100), (-100, 100), (-500, 500), (-2.048, 2.048)]


num_swarms = 5
swarm_size = 10
max_t = 200
migration_threshold = 0.3
pm = 0.2

num_particles = 40
w = 0.7
c1 = 1.5
c2 = 1.5

results_sma = np.zeros((len(functions), 5))
results_pso = np.zeros((len(functions), 5))


for func_index, (func, bound) in enumerate(zip(functions, bounds)):
    min_bound, max_bound = bound
    for trial in range(1):
        sma_result = multi_swarm_sma_with_pattern(num_swarms, swarm_size, 20, min_bound, max_bound, max_t, migration_threshold, pm, func)
        results_sma[func_index, trial] = sma_result[1]
        pso_result = pso_with_pattern(num_particles, 20, min_bound, max_bound, max_t, w, c1, c2, pm, func)
        results_pso[func_index, trial] = pso_result[1]


mean_sma = np.mean(results_sma, axis=1)
mean_pso = np.mean(results_pso, axis=1)


x = np.arange(len(functions))
width = 0.35

fig, ax = plt.subplots()
rects1 = ax.bar(x - width/2, mean_sma, width, label='SMA')
rects2 = ax.bar(x + width/2, mean_pso, width, label='PSO')

ax.set_yscale('log')
ax.set_ylabel('Average Result')
ax.set_title('Results by function with specific bounds')
ax.set_xticks(x)
ax.set_xticklabels(function_names)
ax.legend()

plt.show()
