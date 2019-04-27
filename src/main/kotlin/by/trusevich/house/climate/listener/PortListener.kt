package by.trusevich.house.climate.listener

import by.trusevich.house.climate.service.ComPortService
import by.trusevich.house.core.util.lazyLogger
import jssc.SerialPort
import jssc.SerialPortEvent
import jssc.SerialPortEventListener
import java.lang.Thread.sleep

class PortListener(private val serialPort: SerialPort, private val comPortService: ComPortService) :
    SerialPortEventListener {

    private val log by lazyLogger()

    private var buffer = ""

    override fun serialEvent(event: SerialPortEvent) {
        if (event.isRXCHAR && event.eventValue > 0) {
            try {
                sleep(100)
                buffer += serialPort.readString(event.eventValue)
                if (buffer.contains(END_MESSAGE_SYMBOL)) {
                    comPortService.handleReceivedTemperature(
                        buffer.substring(0, buffer.indexOf(END_MESSAGE_SYMBOL)),
                        serialPort.portName
                    )
                    buffer = buffer.substring(buffer.indexOf(END_MESSAGE_SYMBOL) + 1)
                }
            } catch (ex: Exception) {
                log.error("Error in receiving message from COM-port", ex)
            }

        }
    }

    companion object {

        private const val END_MESSAGE_SYMBOL = "#"
    }
}
