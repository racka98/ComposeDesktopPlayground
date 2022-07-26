package dwm

import kotlinx.cinterop.objcPtr
import kotlinx.cinterop.ptr
import platform.windows.*

class NativeTest {
}

fun enableBlurBehind(hwnd: HWND): HRESULT {
    val hr: HRESULT
    val bb: DWM_BLURBEHIND = DWM_BLURBEHIND(arrayOf(0).objcPtr())

    // Specify blur-behind and blur region.
    bb.dwFlags = DWM_BB_ENABLE.toUInt()
    bb.fEnable = 1
    bb.hRgnBlur = null

    // Enable Blur Behind
    hr = DwmEnableBlurBehindWindow(hWnd = hwnd, bb.ptr)
    return hr
}

