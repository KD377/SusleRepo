import numpy as np
import matplotlib.pyplot as plt
import struct

def zapisz_do_pliku(nazwa_pliku, czas_poczatkowy, czestotliwosc_probkowania, sygnal, zespolony=False):
    try:
        with open(nazwa_pliku, 'wb') as f:
            f.write(struct.pack('d', czas_poczatkowy))
            f.write(struct.pack('d', czestotliwosc_probkowania))
            f.write(struct.pack('?', zespolony))
            f.write(struct.pack('i', len(sygnal)))
            for probka in sygnal:
                if zespolony:
                    f.write(struct.pack('dd', probka.real, probka.imag))
                else:
                    f.write(struct.pack('d', probka))
    except IOError as e:
        print(f"Błąd przy zapisie do pliku: {e}")


def odczyt_z_pliku(nazwa_pliku):
    try:
        with open(nazwa_pliku, 'rb') as f:
            czas_poczatkowy = struct.unpack('d', f.read(8))[0]
            czestotliwosc_probkowania = struct.unpack('d', f.read(8))[0]
            zespolony = struct.unpack('?', f.read(1))[0]
            liczba_probek = struct.unpack('i', f.read(4))[0]
            sygnal = np.zeros(liczba_probek, dtype=np.complex if zespolony else np.float64)

            for i in range(liczba_probek):
                if zespolony:
                    re, im = struct.unpack('dd', f.read(16))
                    sygnal[i] = complex(re, im)
                else:
                    sygnal[i] = struct.unpack('d', f.read(8))[0]
            return czas_poczatkowy, czestotliwosc_probkowania, sygnal, zespolony
    except IOError as e:
        print(f"Błąd przy odczycie z pliku: {e}")
        return None

def prezentuj_dane_tekstowo(czas_poczatkowy, czestotliwosc_probkowania, sygnal, zespolony):
    print(f"Czas początkowy: {czas_poczatkowy}s, Częstotliwość próbkowania: {czestotliwosc_probkowania}Hz")
    print(f"Rodzaj wartości: {'zespolone' if zespolony else 'rzeczywiste'}, Liczba próbek: {len(sygnal)}")
    print("Pierwsze 10 próbek sygnału:")
    for probka in sygnal[:10]:
        print(probka)


def operuj_na_sygnalach(signal1, signal2, operacja):
    if operacja == 'dodawanie':
        wynik = signal1 + signal2
    elif operacja == 'odejmowanie':
        wynik = signal1 - signal2
    elif operacja == 'mnożenie':
        wynik = signal1 * signal2
    elif operacja == 'dzielenie':
        wynik = np.divide(signal1, signal2, out=np.zeros_like(signal1), where=signal2 != 0)
    else:
        raise ValueError("Nieznana operacja.")

    t0 = 0
    t = np.linspace(t0, t0 + len(signal1) / 1000, len(signal1))

    plt.plot(t, wynik)
    plt.title(f'Wynik operacji: {operacja}')
    plt.xlabel('Czas')
    plt.ylabel('Amplituda')
    plt.grid(True)
    plt.show()

    return wynik

class SinusSignal:
    def __init__(self, amplitude, frequency, t1, duration, sampling_rate):
        self.amplitude = amplitude
        self.frequency = frequency
        self.t1 = t1
        self.duration = duration
        self.sampling_rate = sampling_rate

    def generate_signal(self):
        t = np.linspace(self.t1, self.t1 + self.duration, int(self.duration * self.sampling_rate), endpoint=False)
        return t, self.amplitude * np.sin(2 * np.pi * self.frequency * t)

    def generate_half_wave_rectified_signal(self):
        t, signal = self.generate_signal()
        signal_rectified = np.maximum(signal, 0)
        return t, signal_rectified

    def generate_full_wave_rectified_signal(self):
        t, signal = self.generate_signal()
        signal_rectified = np.abs(signal)
        return t, signal_rectified


class UniformNoise:
    def __init__(self, amplitude, t1, duration, sampling_rate):
        self.amplitude = amplitude
        self.t1 = t1
        self.duration = duration
        self.sampling_rate = sampling_rate

    def generate_signal(self):
        t = np.linspace(self.t1, self.t1 + self.duration, int(self.duration * self.sampling_rate))
        signal = self.amplitude * (2 * np.random.rand(len(t)) - 1)
        return t, signal


class GaussianNoise:
    def __init__(self, amplitude, mean, std_dev, t1, duration, sampling_rate):
        self.amplitude = amplitude
        self.mean = mean
        self.std_dev = std_dev
        self.t1 = t1
        self.duration = duration
        self.sampling_rate = sampling_rate

    def generate_signal(self):
        t = np.linspace(self.t1, self.t1 + self.duration, int(self.duration * self.sampling_rate))
        noise = self.amplitude * np.random.normal(self.mean, self.std_dev, len(t))
        return t, noise


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

class TriangleSignal:
    def __init__(self, A, T, t1, d, fulfillment):
        self.A = A
        self.T = T
        self.t1 = t1
        self.d = d
        self.fulfillment = fulfillment

    def generate_signal(self):
        sampling_rate = 1000
        t = np.linspace(self.t1, self.t1 + self.d, int(self.d * sampling_rate))
        signal = np.zeros_like(t)

        for i in range(len(t)):
            time_from_period_start = (t[i] - self.t1) % self.T
            if time_from_period_start < self.T * self.fulfillment:
                signal[i] = (time_from_period_start / (self.T * self.fulfillment)) * self.A * 2 - self.A
            else:
                signal[i] = -((time_from_period_start - self.T * self.fulfillment) / (self.T * (1 - self.fulfillment))) * self.A * 2 + self.A

        return t, signal

class StepSignal:
    def __init__(self, amplitude, t1, duration, step_time):
        self.amplitude = amplitude
        self.t1 = t1
        self.duration = duration
        self.step_time = step_time

    def generate_signal(self):
        sampling_rate = 1000
        t = np.linspace(self.t1, self.t1 + self.duration, int(self.duration * sampling_rate))
        signal = np.zeros_like(t)

        signal[t >= self.step_time] = self.amplitude

        return t, signal


class UnitImpulseSignal:
    def __init__(self, amplitude, t1, duration, sampling_rate, impulse_sample):
        self.amplitude = amplitude
        self.t1 = t1
        self.duration = duration
        self.sampling_rate = sampling_rate
        self.impulse_sample = impulse_sample

    def generate_signal(self):
        total_samples = int(self.duration * self.sampling_rate)
        signal = np.zeros(total_samples)
        if self.impulse_sample < total_samples:
            signal[self.impulse_sample] = self.amplitude
        t = np.linspace(self.t1, self.t1 + self.duration, total_samples)
        return t, signal

class ImpulsiveNoiseSignal:
    def __init__(self, amplitude, t1, duration, sampling_rate, probability):
        self.amplitude = amplitude
        self.t1 = t1
        self.duration = duration
        self.sampling_rate = sampling_rate
        self.probability = probability

    def generate_signal(self):
        total_samples = int(self.duration * self.sampling_rate)
        signal = np.zeros(total_samples)
        for i in range(total_samples):
            if np.random.rand() < self.probability:
                signal[i] = self.amplitude
        t = np.linspace(self.t1, self.t1 + self.duration, total_samples)
        return t, signal