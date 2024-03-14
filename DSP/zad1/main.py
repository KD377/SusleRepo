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
        ax.set_title(f'Histogram dla {signal_type}')
        self.canvas.draw()

    def generate_plot(self):
        signal_type = self.comboBox.currentText()
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

        ax.set_xlabel('Czas (sekundy)')
        ax.set_ylabel('Amplituda')
        ax.grid(True)
        self.canvas.draw()


def main():
    syg1 = SinusSignal(amplitude=1, frequency=1, phase=0, t1=0, duration=2, sampling_rate=1000)
    syg2 = RectangularSignal(range_start=0, range_length=2, amplitude=1, term=1, fulfillment=0.5)

    operuj_na_sygnalach(syg1, syg2, 'dodawanie')
    operuj_na_sygnalach(syg1, syg2, 'odejmowanie')
    operuj_na_sygnalach(syg1, syg2, 'mnożenie')
    operuj_na_sygnalach(syg1, syg2, 'dzielenie')




    app = QApplication([])
    window = MyGUI()
    app.exec_()

if __name__ == '__main__':
    main()
