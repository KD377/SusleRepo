import numpy as np
import matplotlib.pyplot as plt


class SinusSignal:

    def __init__(self, amplitude, frequency, phase, t1, d):
        self.amplitude = amplitude
        self.frequnecy = frequency
        self.phase = phase
        self.t1 = t1
        self.duration = d

    @staticmethod
    def generate_sinus_signal(amplitude, frequency, phase, t):
        return amplitude * np.sin(2 * np.pi * frequency * t + phase)

    @staticmethod
    def plot_sinus_signal(amplitude, frequency, phase, t1, d):
        t = np.linspace(t1, t1 + d, 1000)
        signal = SinusSignal.generate_sinus_signal(amplitude, frequency, phase, t)
        plt.plot(t, signal)
        plt.title('Sinusoidal Signal')
        plt.xlabel('Time (seconds)')
        plt.ylabel('Amplitude')
        plt.grid(True)
        plt.show()

    @staticmethod
    def generate_sinus_signal_half(amplitude, frequency, phase, t):
        return amplitude/2 * (np.sin(2 * np.pi * frequency * t + phase) + abs(np.sin(2 * np.pi * frequency * t + phase)))

    @staticmethod
    def plot_sinus_signal_half(amplitude, frequency, phase, t1, d):
        t = np.linspace(t1, t1 + d, 1000)
        signal = SinusSignal.generate_sinus_signal_half(amplitude, frequency, phase, t)
        plt.plot(t, signal)
        plt.title('Sinusoidal Signal')
        plt.xlabel('Time (seconds)')
        plt.ylabel('Amplitude')
        plt.grid(True)
        plt.show()

    @staticmethod
    def generate_sinus_signal_full(amplitude, frequency, phase, t):
        return amplitude * abs(np.sin(2 * np.pi * frequency * t + phase))

    @staticmethod
    def plot_sinus_signal_full(amplitude, frequency, phase, t1, d):
        t = np.linspace(t1, t1 + d, 1000)
        signal = SinusSignal.generate_sinus_signal_full(amplitude, frequency, phase, t)
        plt.plot(t, signal)
        plt.title('Sinusoidal Signal')
        plt.xlabel('Time (seconds)')
        plt.ylabel('Amplitude')
        plt.grid(True)
        plt.show()

class UniformNoise:

    def __init__(self, amplitude, t1, d):
        self.amplitude = amplitude
        self.t1 = t1
        self.duration = d

    @staticmethod
    def plot_uniform_noise(amplitude, t1, d, sampling_rate):

        uniform_noise = amplitude * (2 * np.random.rand(int(d * sampling_rate)) - 1)
        t = np.linspace(t1, t1 + d, len(uniform_noise))

        plt.plot(t, uniform_noise)
        plt.title('Uniform Noise Signal')
        plt.xlabel('Time (seconds)')
        plt.ylabel('Amplitude')
        plt.grid(True)
        plt.show()


class GaussianNoise:

    @staticmethod
    def plot_gaussian_noise(amplitude, mean, dev, t1, d, sampling_rate):

        noise = amplitude * np.random.normal(mean, dev, sampling_rate)
        t = np.linspace(t1, t1 + d, len(noise))

        plt.plot(t, noise)
        plt.title('Gaussian Noise')
        plt.xlabel('Sample Index')
        plt.ylabel('Amplitude')
        plt.grid(True)
        plt.show()

