import org.jnativehook.keyboard.NativeKeyEvent

fun keyEventToString(e: NativeKeyEvent?): String {
    if (e == null) {
        return ""
    }

    return listOf(
        NativeKeyEvent.getModifiersText(e.modifiers),
        NativeKeyEvent.getKeyText(e.keyCode)
    )
        .filter { !it.isBlank() }
        .joinToString("+")
}