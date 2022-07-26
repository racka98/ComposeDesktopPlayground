package skia

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Structure
import com.sun.jna.platform.win32.WinDef.HWND
import com.sun.jna.ptr.IntByReference
import com.sun.jna.win32.StdCallLibrary.StdCallCallback
import com.sun.jna.win32.W32APIOptions
import javax.swing.JFrame


interface DwmApi : StdCallCallback, Library {
    //val DWM_BB_ENABLE = 0x00000001

    @Suppress("functionName")
    fun DwmEnableBlurBehindWindow(hWnd: HWND, pBlurBehind: DWM_BLURBEHIND): Boolean

    companion object : DwmApi by Native.load("DwmApi", DwmApi::class.java, W32APIOptions.UNICODE_OPTIONS)

    @Suppress("className")
    class DWM_BLURBEHIND : Structure() {

        @JvmField
        var dwFlags = 0

        @JvmField
        var fEnable = false

        @JvmField
        var hRgnBlur: IntByReference? = null

        @JvmField
        var fTransitionOnMaximized = false

        override fun getFieldOrder() =
            listOf("dwFlags", "fEnable", "hRgnBlur", "fTransitionOnMaximized")
    }
}

fun JFrame.blurWindow() {
    val hWnd = HWND(Native.getWindowPointer(this))
    val DWM_BB_ENABLE = 0x00000001
    val instance = DwmApi
    val pBlurBehind = DwmApi.DWM_BLURBEHIND()
    pBlurBehind.dwFlags = DWM_BB_ENABLE
    pBlurBehind.fEnable = true
    pBlurBehind.fTransitionOnMaximized = true
    val blurred = instance.DwmEnableBlurBehindWindow(hWnd = hWnd, pBlurBehind = pBlurBehind)
    println("Is Blurred: $blurred. Instance: $instance")
}