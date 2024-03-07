from PyQt5.QtWidgets import *
from PyQt5 import uic
from matplotlib.backends.backend_qt5agg import FigureCanvasQTAgg as FigureCanvas
from matplotlib.figure import Figure

from SignalGenerator import SinusSignal


class MyGUI(QMainWindow):
    def __init__(self):
        super(MyGUI, self).__init__()
        uic.loadUi('form.ui', self)
        self.show()

        self.figure = Figure(figsize=(5,3),dpi=100)
        self.canvas = FigureCanvas(self.figure)

        self.gridLayout_3.addWidget(self.canvas)
        self.pushButton.clicked.connect(self.generate_plot)

    def generate_plot(self):
        if self.comboBox.currentText() == "Sinus":
            self.amplitude = float(self.lineEdit.text())
            self.frequency = float(self.lineEdit_2.text())
            self.t1 = float(self.lineEdit_3.text())
            self.duration = float(self.lineEdit_4.text())
            self.sinus = SinusSignal(amplitude=self.amplitude, frequency=self.frequency, phase=0,t1=self.t1, duration=self.duration,sampling_rate=1000)

            t, signal = self.sinus.generate_sinus_signal()

            self.figure.clear()
            ax = self.figure.add_subplot(111)
            ax.plot(t, signal)
            ax.set_title('Sinusoidal Signal')
            ax.set_xlabel('Time (seconds)')
            ax.set_ylabel('Amplitude')
            ax.grid(True)
            self.canvas.draw()


def main():
    app = QApplication([])
    window = MyGUI()
    app.exec_()

if __name__ == '__main__':
    main()
