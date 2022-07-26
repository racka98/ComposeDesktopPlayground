package jna

import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.win32.StdCallLibrary

fun main() {
    val state = getBatteryState()
    println(state)
}

fun getBatteryState(): SYSTEM_BATTERY_STATE? {
    val batteryState = SYSTEM_BATTERY_STATE()
    val retrieveValue = PowrProf.CallNtPowerInformation(
        5,
        Pointer.NULL,
        0,
        batteryState,
        batteryState.size().toLong()
    )

    return if (retrieveValue == 0) batteryState else null
}

interface PowrProf : StdCallLibrary {
    @Suppress("FunctionName")
    fun CallNtPowerInformation(
        informationLevel: Int,
        inBuffer: Pointer?,
        inBufferLen: Long,
        outBuffer: SYSTEM_BATTERY_STATE?,
        outBufferLen: Long
    ): Int

    companion object : PowrProf by Native.load("PowrProf", PowrProf::class.java)!!
}

class SYSTEM_BATTERY_STATE : Structure(ALIGN_MSVC), Structure.ByReference {
    @JvmField
    var AcOnLine: Byte = 0

    @JvmField
    var BatteryPresent: Byte = 0

    @JvmField
    var Charging: Byte = 0

    @JvmField
    var Discharging: Byte = 0

    @JvmField
    var Spare1_0: Byte = 0

    @JvmField
    var Spare1_1: Byte = 0

    @JvmField
    var Spare1_2: Byte = 0

    @JvmField
    var Spare1_3: Byte = 0

    @JvmField
    var MaxCapacity = 0

    @JvmField
    var RemainingCapacity = 0

    @JvmField
    var Rate = 0

    @JvmField
    var EstimatedTime = 0

    @JvmField
    var DefaultAlert1 = 0

    @JvmField
    var DefaultAlert2 = 0

    override fun getFieldOrder(): List<String> {
        return listOf(
            "AcOnLine", "BatteryPresent", "Charging", "Discharging",
            "Spare1_0", "Spare1_1", "Spare1_2", "Spare1_3",
            "MaxCapacity", "RemainingCapacity", "Rate",
            "EstimatedTime", "DefaultAlert1", "DefaultAlert2"
        )
    }
}
