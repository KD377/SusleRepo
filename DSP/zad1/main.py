from SignalGenerator import *

# SinusSignal.plot_sinus_signal(1, 1000, 0, 2, 2)
# UniformNoise.plot_uniform_noise(1, 0, 1, 1000)
# GaussianNoise.plot_gaussian_noise(2, 0, 1, 0, 2, 1000)
# SinusSignal.plot_sinus_signal_full(1, 1000, 0, 2, 2)
SinusSignal.create_sinus_histogram(2,1000,0,5,0,2)
UniformNoise.histogram(2,0,2,1000,10)
GaussianNoise.histogram(2,0,1,1000,20)