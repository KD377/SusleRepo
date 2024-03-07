import numpy as np
import matplotlib.pyplot as plt
from scipy.signal import square

class SinusSignal:

    def __init__(self, amplitude, frequency, phase, t1, duration,sampling_rate):
        self.amplitude = amplitude
        self.frequency = frequency
        self.phase = phase
        self.t1 = t1
        self.duration = duration
        self.sampling_rate = sampling_rate

    def generate_sinus_signal(self):
        t = np.linspace(self.t1, self.t1 + self.duration, 1000)
        return t, self.amplitude * np.sin(2 * np.pi * self.frequency * t + self.phase)


    @staticmethod
    def create_sinus_histogram(amplitude, frequency, phase, bins, t1, d):
        t = np.linspace(t1, t1 + d, 1000)
        signal = SinusSignal.generate_sinus_signal(amplitude, frequency, phase, t)
        bins_arr = np.linspace(-amplitude, amplitude, bins)

        plt.hist(signal, bins_arr)
        plt.grid()
        plt.show()

    def generate_sinus_signal_half(self):
        t = np.linspace(self.t1, self.t1 + self.duration, self.sampling_rate)
        return t, self.amplitude / 2 * (np.sin(2 * np.pi * self.frequency * t + self.phase)
                                + abs(np.sin(2 * np.pi * self.frequency * t + self.phase)))

    def generate_sinus_signal_full(self):
        t = np.linspace(self.t1, self.t1 + self.duration, self.sampling_rate)
        return t, self.amplitude * abs(np.sin(2 * np.pi * self.frequency * t + self.phase))


class UniformNoise:

    def __init__(self, amplitude, t1, d, sampling_rate):
        self.amplitude = amplitude
        self.t1 = t1
        self.duration = d
        self.sampling_rate = sampling_rate

    def plot_uniform_noise(self):
        uniform_noise = self.amplitude * (2 * np.random.rand(int(self.duration * self.sampling_rate)) - 1)
        t = np.linspace(self.t1, self.t1 + self.duration, len(uniform_noise))
        return t, uniform_noise

    @staticmethod
    def histogram(amplitude, t1, d, sampling_rate, bins):
        uniform_noise = amplitude * (2 * np.random.rand(int(d * sampling_rate)) - 1)
        bins_arr = np.linspace(-amplitude, amplitude, bins)

        plt.hist(uniform_noise, bins_arr, edgecolor='black')
        plt.grid()
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

    @staticmethod
    def histogram(amplitude, mean, dev, sampling_rate, bins):
        noise = amplitude * np.random.normal(mean, dev, sampling_rate)
        bins_arr = np.linspace(-amplitude, amplitude, bins)

        plt.hist(noise, bins_arr, edgecolor='black')
        plt.grid()
        plt.show()


"""
Sygnały susła poniżej
"""


class RectangularSignal:
    def __init__(self, range_start, range_length, amplitude, term, fulfillment):
        self.range_start = range_start
        self.range_length = range_length
        self.amplitude = amplitude
        self.term = term
        self.fulfillment = fulfillment

    def value(self, t):
        if ((t - self.range_start) / self.term) - np.floor((t - self.range_start) / self.term) < self.fulfillment:
            return self.amplitude
        else:
            return 0.0

    def generate_signal(self):
        t = np.linspace(self.range_start, self.range_start + self.range_length, int(1000 * self.range_length))
        signal = np.array([self.value(ti) for ti in t])
        return t, signal

    def plot_signal(self):
        t, signal = self.generate_signal()
        plt.plot(t, signal)
        plt.title('Rectangular Signal')
        plt.xlabel('Time')
        plt.ylabel('Amplitude')
        plt.grid(True)
        plt.show()

    def plot_histogram(self):
        _, signal = self.generate_signal()
        plt.hist(signal, bins='auto', edgecolor='black')
        plt.title('Histogram of Rectangular Signal')
        plt.xlabel('Amplitude')
        plt.ylabel('Frequency')
        plt.grid(True)
        plt.show()

class RectangularSymmetricSignal:
    def __init__(self, range_start, range_length, amplitude, term, fulfillment):
        self.range_start = range_start
        self.range_length = range_length
        self.amplitude = amplitude
        self.term = term
        self.fulfillment = fulfillment

    def value(self, t):
        if ((t - self.range_start) / self.term) - np.floor((t - self.range_start) / self.term) < self.fulfillment:
            return self.amplitude
        else:
            return -self.amplitude

    def generate_signal(self):
        t = np.linspace(self.range_start, self.range_start + self.range_length, int(1000 * self.range_length))
        signal = np.array([self.value(ti) for ti in t])
        return t, signal

    def plot_signal(self):
        t, signal = self.generate_signal()
        plt.plot(t, signal)
        plt.title('Rectangular Symmetric Signal')
        plt.xlabel('Time')
        plt.ylabel('Amplitude')
        plt.grid(True)
        plt.show()

    def plot_histogram(self):
        _, signal = self.generate_signal()
        plt.hist(signal, bins='auto', edgecolor='black')
        plt.title('Histogram of Rectangular Symmetric Signal')
        plt.xlabel('Amplitude')
        plt.ylabel('Frequency')
        plt.grid(True)
        plt.show()