import numpy as np
import matplotlib.pyplot as plt
from scipy.signal import square
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

def operuj_na_sygnalach(syg1, syg2, operacja):
    # Generowanie sygnałów
    t1, signal1 = syg1.generate_signal()
    t2, signal2 = syg2.generate_signal()

    # Znalezienie wspólnego zakresu czasowego
    t_start = max(t1[0], t2[0])
    t_end = min(t1[-1], t2[-1])
    # Wybór większego zbioru punktów czasowych jako bazy do interpolacji
    if len(t1) > len(t2):
        t_common = np.linspace(t_start, t_end, len(t1))
    else:
        t_common = np.linspace(t_start, t_end, len(t2))

    # Interpolacja sygnałów do wspólnego zbioru punktów czasowych
    signal1_interp = np.interp(t_common, t1, signal1)
    signal2_interp = np.interp(t_common, t2, signal2)

    # Wybór operacji
    if operacja == 'dodawanie':
        wynik = signal1_interp + signal2_interp
    elif operacja == 'odejmowanie':
        wynik = signal1_interp - signal2_interp
    elif operacja == 'mnożenie':
        wynik = signal1_interp * signal2_interp
    elif operacja == 'dzielenie':
        wynik = np.divide(signal1_interp, signal2_interp, out=np.zeros_like(signal1_interp), where=signal2_interp != 0)
    else:
        raise ValueError("Nieznana operacja.")

    # Wyświetlanie wyniku
    plt.plot(t_common, wynik)
    plt.title(f'Wynik operacji: {operacja}')
    plt.xlabel('Czas')
    plt.ylabel('Amplituda')
    plt.grid(True)
    plt.show()

    return t_common, wynik

class SinusSignal:
    def __init__(self, amplitude, frequency, phase, t1, duration,sampling_rate):
        self.amplitude = amplitude
        self.frequency = frequency
        self.phase = phase
        self.t1 = t1
        self.duration = duration
        self.sampling_rate = sampling_rate

    def generate_signal(self):
        t = np.linspace(self.t1, self.t1 + self.duration, 1000)
        return t, self.amplitude * np.sin(2 * np.pi * self.frequency * t + self.phase)


    @staticmethod
    def create_sinus_histogram(amplitude, frequency, phase, bins, t1, d):
        t = np.linspace(t1, t1 + d, 1000)
        signal = SinusSignal.generate_signal(amplitude, frequency, phase, t)
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