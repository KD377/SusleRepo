from SignalGenerator import *

# SinusSignal.plot_sinus_signal(1, 1000, 0, 2, 2)
# UniformNoise.plot_uniform_noise(1, 0, 1, 1000)
# GaussianNoise.plot_gaussian_noise(2, 0, 1, 0, 2, 1000)
# SinusSignal.plot_sinus_signal_full(1, 1000, 0, 2, 2)
# SinusSignal.create_sinus_histogram(2, 1000, 0, 5, 0, 2)
# UniformNoise.histogram(2, 0, 2, 1000, 10)
# GaussianNoise.histogram(2, 0, 1, 1000, 20)


t1 = 0  # Początek zakresu
d = 4  # Długość zakresu
a = -1  # Amplituda
t = 1  # Okres
k = 0.25  # Wypełnienie

# PROSTOKATNY
signal = RectangularSignal(t1, d, a, t, k)
signal.plot_signal()
signal.plot_histogram()

# PROSTOKATNY SYMETRYCZNY
symmetric_signal = RectangularSymmetricSignal(t1, d, a, t, k)
symmetric_signal.plot_signal()
symmetric_signal.plot_histogram()
