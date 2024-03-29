from PyQt5.QtWidgets import *
from PyQt5 import uic
from matplotlib.backends.backend_qt5agg import FigureCanvasQTAgg as FigureCanvas
from matplotlib.figure import Figure

from SignalGenerator import *


class MyGUI(QMainWindow):
    def __init__(self):
        super(MyGUI, self).__init__()
        uic.loadUi('form.ui', self)
        self.show()

        self.figure = Figure(figsize=(5,3),dpi=100)
        self.canvas = FigureCanvas(self.figure)

        self.gridLayout_3.addWidget(self.canvas)
        self.pushButton.clicked.connect(self.generate_plot)

        self.histogramButton.clicked.connect(self.generate_histogram)
        self.histogramBinsComboBox.addItems(["5", "10", "15", "20"])

        self.saveButton.clicked.connect(self.save_signal)
        self.selectFile1Button.clicked.connect(lambda: self.select_file(1))
        self.selectFile2Button.clicked.connect(lambda: self.select_file(2))
        self.operationComboBox.addItems(["Dodawanie", "Odejmowanie", "Mnożenie", "Dzielenie"])
        self.performOperationButton.clicked.connect(lambda: self.perform_operation(display_type='plot'))
        self.performOperationButton_2.clicked.connect(lambda: self.perform_operation(display_type='histogram'))
        self.saveToTxt.clicked.connect(self.save_signal_to_txt)


    def perform_operation(self, display_type='plot'):
        if hasattr(self, 'file1') and hasattr(self, 'file2'):
            try:
                _, _, signal1, _ = odczyt_z_pliku(self.file1)
                _, _, signal2, _ = odczyt_z_pliku(self.file2)

                operation = self.operationComboBox.currentText()
                if operation == "Dodawanie":
                    self.resultSignal = signal1 + signal2
                elif operation == "Odejmowanie":
                    self.resultSignal = signal1 - signal2
                elif operation == "Mnożenie":
                    self.resultSignal = signal1 * signal2
                elif operation == "Dzielenie":
                    self.resultSignal = np.divide(signal1, signal2, out=np.zeros_like(signal1), where=signal2 != 0)

                if display_type == 'plot':
                    self.display_result(self.resultSignal)
                elif display_type == 'histogram':
                    self.display_result2(self.resultSignal)
            except Exception as e:
                QMessageBox.warning(self, "Błąd", str(e))
        else:
            QMessageBox.warning(self, "Błąd", "Nie wybrano plików sygnału.")

    def display_result(self, result):
        self.figure.clear()
        ax = self.figure.add_subplot(111)
        ax.plot(result)
        ax.set_title('Wynik operacji')
        ax.set_xlabel('Próbki')
        ax.set_ylabel('Amplituda')
        ax.grid(True)
        self.canvas.draw()

    def display_result2(self, result):
        self.figure.clear()
        ax = self.figure.add_subplot(111)
        ax.hist(self.resultSignal, bins=50)
        ax.set_title("Histogram sygnału wynikowego")
        ax.set_xlabel("Wartość próbki")
        ax.set_ylabel("Liczba próbek")
        self.canvas.draw()

    def select_file(self, file_number):
        file_name, _ = QFileDialog.getOpenFileName(self, f"Wybierz plik sygnału {file_number}")
        if file_name:
            setattr(self, f'file{file_number}', file_name)


    def save_signal(self):
        file_name = self.fileName.text()
        if hasattr(self, 'current_signal'):
            t, signal = self.current_signal
            zapisz_do_pliku(file_name, 0, 1000, signal)
            QMessageBox.information(self, "Zapis sygnału", f"Sygnał został zapisany do {file_name}.")
        else:
            QMessageBox.warning(self, "Błąd", "Brak sygnału do zapisu.")

    def save_signal_to_txt(self):
        fileName = self.fileName.text()
        if fileName:
            if hasattr(self, 'current_signal'):
                t, signal = self.current_signal
                with open(fileName, 'w') as file:
                    for t, value in zip(t, signal):
                        file.write(f"{t}\t{value}\n")
                QMessageBox.information(self, "Zapis do pliku", "Pomyślnie zapisano sygnał do pliku.")
            else:
                QMessageBox.warning(self, "Błąd", "Najpierw wygeneruj sygnał.")

    def generate_histogram(self):
        signal_type = self.comboBox.currentText()
        bins = int(self.histogramBinsComboBox.currentText())  # Pobranie liczby przedziałów

        self.figure.clear()
        ax = self.figure.add_subplot(111)
        if signal_type == "Sinus":
            amplitude = float(self.lineEdit.text())
            frequency = float(self.lineEdit_2.text())
            t1 = float(self.lineEdit_3.text())
            duration = float(self.lineEdit_4.text())
            sinus = SinusSignal(amplitude=amplitude, frequency=frequency, phase=0, t1=t1, duration=duration,
                                sampling_rate=1000)
            t, signal = sinus.generate_signal()
            ax.hist(signal, bins=bins)
        elif signal_type == "Jednostajny":
            amplitude = float(self.lineEdit.text())
            t1 = float(self.lineEdit_3.text())
            duration = float(self.lineEdit_4.text())
            uniform = UniformNoise(amplitude=amplitude, t1=t1, duration=duration, sampling_rate=1000)
            t, signal = uniform.generate_signal()
            ax.hist(signal, bins=bins)
        elif signal_type == "Gaussowski":
            amplitude = float(self.lineEdit.text())
            t1 = float(self.lineEdit_3.text())
            duration = float(self.lineEdit_4.text())
            gaussian = GaussianNoise(amplitude=amplitude, mean=0, std_dev=1, t1=t1, duration=duration, sampling_rate=1000)
            t, signal = gaussian.generate_signal()
            ax.hist(signal, bins=bins)
        elif signal_type == "Trojkatny":
            amplitude = float(self.lineEdit.text())
            term = float(self.lineEdit_2.text())
            range_start = float(self.lineEdit_3.text())
            range_length = float(self.lineEdit_4.text())
            fulfillment = float(self.lineEdit_5.text())
            triangular = TriangleSignal(A=amplitude, T=term, t1=range_start, d=range_length, fulfillment=fulfillment)
            t, signal = triangular.generate_signal()
            ax.hist(signal, bins=bins)
        elif signal_type == "Skok_jednostkowy":
            amplitude = float(self.lineEdit.text())
            t1 = float(self.lineEdit_3.text())
            duration = float(self.lineEdit_4.text())
            step_time = float(self.lineEdit_5.text())
            step = StepSignal(amplitude=amplitude, t1=t1, duration=duration, step_time=step_time)
            t, signal = step.generate_signal()
            ax.hist(signal, bins=bins)
        elif signal_type == "Prostokatny":
            amplitude = float(self.lineEdit.text())
            term = float(self.lineEdit_2.text())
            range_start = float(self.lineEdit_3.text())
            range_length = float(self.lineEdit_4.text())
            fulfillment = float(self.lineEdit_5.text())
            rectangular_signal = RectangularSignal(range_start=range_start, range_length=range_length,
                                                   amplitude=amplitude, term=term, fulfillment=fulfillment)
            t, signal = rectangular_signal.generate_signal()
            ax.hist(signal, bins=bins)
        elif signal_type == "Prostokatny_symetryczny":
            amplitude = float(self.lineEdit.text())
            term = float(self.lineEdit_2.text())
            range_start = float(self.lineEdit_3.text())
            range_length = float(self.lineEdit_4.text())
            fulfillment = float(self.lineEdit_5.text())
            rectangular_sym_signal = RectangularSymmetricSignal(range_start=range_start, range_length=range_length,
                                                                amplitude=amplitude, term=term, fulfillment=fulfillment)
            t, signal = rectangular_sym_signal.generate_signal()
            ax.hist(signal, bins=bins)
        elif signal_type == "Sinus_pol":
            amplitude = float(self.lineEdit.text())
            frequency = float(self.lineEdit_2.text())
            t1 = float(self.lineEdit_3.text())
            duration = float(self.lineEdit_4.text())
            sinus_pol = SinusSignal(amplitude=amplitude, frequency=frequency, phase=0, t1=t1, duration=duration,
                                    sampling_rate=1000)
            t, signal = sinus_pol.generate_half_wave_rectified_signal()
            ax.plot(t, signal)
            ax.set_title('Sinus pół')
            ax.hist(signal, bins=bins)
        elif signal_type == "Sinus_caly":
            amplitude = float(self.lineEdit.text())
            frequency = float(self.lineEdit_2.text())
            t1 = float(self.lineEdit_3.text())
            duration = float(self.lineEdit_4.text())
            sinus_pol = SinusSignal(amplitude=amplitude, frequency=frequency, phase=0, t1=t1, duration=duration,
                                    sampling_rate=1000)
            t, signal = sinus_pol.generate_full_wave_rectified_signal()
            ax.plot(t, signal)
            ax.set_title('Sinus cały')
            ax.hist(signal, bins=bins)
        elif signal_type == "Impuls_jednostkowy":
            amplitude = float(self.lineEdit.text())
            t1 = float(self.lineEdit_3.text())
            duration = float(self.lineEdit_4.text())
            sampling_rate = int(self.lineEdit_7.text())
            impulse_sample = int(self.lineEdit_8.text())
            unit_impulse = UnitImpulseSignal(amplitude=amplitude, t1=t1, duration=duration, sampling_rate=sampling_rate,
                                             impulse_sample=impulse_sample)
            t, signal = unit_impulse.generate_signal()
            ax.hist(signal, bins=bins)
            ax.set_title('Impuls Jednostkowy')
        elif signal_type == "Szum_impulsowy":
            amplitude = float(self.lineEdit.text())
            t1 = float(self.lineEdit_3.text())
            duration = float(self.lineEdit_4.text())
            sampling_rate = int(self.lineEdit_7.text())
            probability = float(self.lineEdit_9.text())
            impulsive_noise = ImpulsiveNoiseSignal(amplitude=amplitude, t1=t1, duration=duration,
                                                   sampling_rate=sampling_rate, probability=probability)
            t, signal = impulsive_noise.generate_signal()
            ax.hist(signal, bins=bins)
            ax.set_title('Szum Impulsowy')
        ax.set_title(f'Histogram dla {signal_type}')
        self.canvas.draw()

    def generate_plot(self):
        signal_type = self.comboBox.currentText()
        self.figure.clear()
        ax = self.figure.add_subplot(111)
        t = None
        signal = None
        if signal_type == "Sinus":
            amplitude = float(self.lineEdit.text())
            frequency = float(self.lineEdit_2.text())
            t1 = float(self.lineEdit_3.text())
            duration = float(self.lineEdit_4.text())
            sinus = SinusSignal(amplitude=amplitude, frequency=frequency, phase=0, t1=t1, duration=duration,
                                sampling_rate=1000)
            t, signal = sinus.generate_signal()
            ax.plot(t, signal)
            ax.set_title('Sygnał Sinusoidalny')

        elif signal_type == "Prostokatny":
            amplitude = float(self.lineEdit.text())
            term = float(self.lineEdit_2.text())
            range_start = float(self.lineEdit_3.text())
            range_length = float(self.lineEdit_4.text())
            fulfillment = float(self.lineEdit_5.text())
            rectangular_signal = RectangularSignal(range_start=range_start, range_length=range_length,
                                                   amplitude=amplitude, term=term, fulfillment=fulfillment)
            t, signal = rectangular_signal.generate_signal()
            ax.plot(t, signal)
            ax.set_title('Sygnał Prostokątny')

        elif signal_type == "Prostokatny_symetryczny":
            amplitude = float(self.lineEdit.text())
            term = float(self.lineEdit_2.text())
            range_start = float(self.lineEdit_3.text())
            range_length = float(self.lineEdit_4.text())
            fulfillment = float(self.lineEdit_5.text())
            rectangular_sym_signal = RectangularSymmetricSignal(range_start=range_start, range_length=range_length,
                                                                amplitude=amplitude, term=term, fulfillment=fulfillment)
            t, signal = rectangular_sym_signal.generate_signal()
            ax.plot(t, signal)
            ax.set_title('Sygnał Prostokątny Symetryczny')
        elif signal_type == "Jednostajny":
            amplitude = float(self.lineEdit.text())
            t1 = float(self.lineEdit_3.text())
            duration = float(self.lineEdit_4.text())
            uniform = UniformNoise(amplitude=amplitude, t1=t1, duration=duration,
                                sampling_rate=1000)
            t, signal = uniform.generate_signal()
            ax.plot(t, signal)
            ax.set_title('Szum o rozkładzie jednostajnym')
        elif signal_type == "Gaussowski":
            amplitude = float(self.lineEdit.text())
            t1 = float(self.lineEdit_3.text())
            duration = float(self.lineEdit_4.text())
            gaussian = GaussianNoise(amplitude=amplitude, mean=0, std_dev=1, t1=t1, duration=duration,
                                sampling_rate=1000)
            t, signal = gaussian.generate_signal()
            ax.plot(t, signal)
            ax.set_title('Szum gaussowski')
        elif signal_type == "Trojkatny":
            amplitude = float(self.lineEdit.text())
            term = float(self.lineEdit_2.text())
            range_start = float(self.lineEdit_3.text())
            range_length = float(self.lineEdit_4.text())
            fulfillment = float(self.lineEdit_5.text())
            triangular = TriangleSignal(A=amplitude, T=term, t1=range_start, d=range_length, fulfillment=fulfillment)
            t, signal = triangular.generate_signal()
            ax.plot(t, signal)
            ax.set_title('Sygnał trójkątny')
        elif signal_type == "Skok_jednostkowy":
            amplitude = float(self.lineEdit.text())
            t1 = float(self.lineEdit_3.text())
            duration = float(self.lineEdit_4.text())
            step_time = float(self.lineEdit_6.text())
            step = StepSignal(amplitude=amplitude, t1=t1, duration=duration, step_time=step_time)
            t, signal = step.generate_signal()
            ax.plot(t, signal)
            ax.set_title('Skok jednostkowy')
        elif signal_type == "Sinus_pol":
            amplitude = float(self.lineEdit.text())
            frequency = float(self.lineEdit_2.text())
            t1 = float(self.lineEdit_3.text())
            duration = float(self.lineEdit_4.text())
            sinus_pol = SinusSignal(amplitude=amplitude, frequency=frequency, phase=0, t1=t1, duration=duration,
                                    sampling_rate=1000)
            t, signal = sinus_pol.generate_half_wave_rectified_signal()
            ax.plot(t, signal)
            ax.set_title('Sygnał sinusoidalny wyprostowany jednopołówkowo')
        elif signal_type == "Sinus_caly":
            amplitude = float(self.lineEdit.text())
            frequency = float(self.lineEdit_2.text())
            t1 = float(self.lineEdit_3.text())
            duration = float(self.lineEdit_4.text())
            sinus_pol = SinusSignal(amplitude=amplitude, frequency=frequency, phase=0, t1=t1, duration=duration,
                                    sampling_rate=1000)
            t, signal = sinus_pol.generate_full_wave_rectified_signal()
            ax.plot(t, signal)
            ax.set_title('Sygnał sinusoidalny wyprostowany dwupołówkowo')
        elif signal_type == "Impuls_jednostkowy":
            amplitude = float(self.lineEdit.text())
            t1 = float(self.lineEdit_3.text())
            duration = float(self.lineEdit_4.text())
            sampling_rate = int(self.lineEdit_7.text())
            impulse_sample = int(self.lineEdit_8.text())
            unit_impulse = UnitImpulseSignal(amplitude=amplitude, t1=t1, duration=duration, sampling_rate=sampling_rate,
                                             impulse_sample=impulse_sample)
            t, signal = unit_impulse.generate_signal()
            ax.stem(t, signal, linefmt='b-', markerfmt='bo',
                    basefmt='r-')
            ax.set_title('Impuls Jednostkowy')
        elif signal_type == "Szum_impulsowy":
            amplitude = float(self.lineEdit.text())
            t1 = float(self.lineEdit_3.text())
            duration = float(self.lineEdit_4.text())
            sampling_rate = int(self.lineEdit_7.text())
            probability = float(self.lineEdit_9.text())
            impulsive_noise = ImpulsiveNoiseSignal(amplitude=amplitude, t1=t1, duration=duration,
                                                   sampling_rate=sampling_rate, probability=probability)
            t, signal = impulsive_noise.generate_signal()
            ax.stem(t, signal, linefmt='g-', markerfmt='go', basefmt='r-')
            ax.set_title('Szum Impulsowy')

        if t is not None and signal is not None:
            self.current_signal = (t, signal)
            srednia, srednia_bezwzgledna, rms, wariancja, moc_srednia = oblicz_parametry_sygnalu(signal)
        self.labelSrednia.setText(f"Wartość średnia: {srednia:.4f}")
        self.labelSredniaBezwzgledna.setText(f"Wartość średnia bezwzględna: {srednia_bezwzgledna:.4f}")
        self.labelRMS.setText(f"Wartość skuteczna: {rms:.4f}")
        self.labelWariancja.setText(f"Wariancja: {wariancja:.4f}")
        self.labelMocSrednia.setText(f"Moc średnia: {moc_srednia:.4f}")
        ax.set_xlabel('Czas (sekundy)')
        ax.set_ylabel('Amplituda')
        ax.grid(True)
        self.canvas.draw()


def oblicz_parametry_sygnalu(signal):
    srednia = np.mean(signal)

    srednia_bezwzgledna = np.mean(np.abs(signal))

    rms = np.sqrt(np.mean(signal ** 2))

    wariancja = np.var(signal)

    moc_srednia = np.mean(signal ** 2)

    return srednia, srednia_bezwzgledna, rms, wariancja, moc_srednia



def main():
    app = QApplication([])
    window = MyGUI()
    app.exec_()

if __name__ == '__main__':
    main()
