package by.trusevich.house.climate.service

import by.trusevich.house.climate.listener.PortListener
import by.trusevich.house.climate.repository.ClimateRepository
import by.trusevich.house.core.util.lazyLogger
import jssc.SerialPort
import jssc.SerialPort.MASK_RXCHAR
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ComPortService(
    private val climateRepository: ClimateRepository,
    @Value("\${house.default.jssc.baudRate}") private val baudRate: Int,
    @Value("\${house.default.jssc.dataBits}") private val dataBits: Int,
    @Value("\${house.default.jssc.stopBits}") private val stopBits: Int,
    @Value("\${house.default.jssc.parity}") private val parity: Int,
    @Value("\${house.default.jssc.flowControl}") private val flowControl: Int
) {

    private val log by lazyLogger()

    fun connectToPort(portName: String) = SerialPort(portName).apply {
        openPort()
        setParams(baudRate, dataBits, stopBits, parity)
        flowControlMode = flowControl
    }

    fun disconnectFromPort(serialPort: SerialPort) = serialPort.closePort()

    fun addListener(serialPort: SerialPort) = serialPort.apply {
        addEventListener(PortListener(serialPort, this@ComPortService), MASK_RXCHAR)
    }

    fun sendMessage(portName: String, message: String): Unit = connectToPort(portName).let { port ->
        port.writeString(message)
        disconnectFromPort(port)
    }

    fun handleReceivedTemperature(message: String, portName: String) {

        log.info("received $message from $portName")

        val pinNumber = message.substring(0, message.indexOf(" ")).toInt()
        val realTemperature = message.substring(message.indexOf(" ") + 1).toDouble()

        climateRepository.findByThermometerPortNameAndCollectorPinNumber(portName, pinNumber).let { climateZone ->
            climateRepository.save(climateZone.apply { currentTemperature = realTemperature })
        }

    }
}