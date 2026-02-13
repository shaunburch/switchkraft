package dev.switchkraft

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Stable
class FieldState<T>(initialValue: T) {
    var originalValue: T by mutableStateOf(initialValue)
        private set
    var currentValue: T by mutableStateOf(initialValue)

    val isModified: Boolean
        get() = currentValue != originalValue

    fun revert() {
        currentValue = originalValue
    }

    fun commit() {
        originalValue = currentValue
    }
}

@Composable
fun <T> rememberFieldState(initialValue: T): FieldState<T> {
    val state = remember { FieldState(initialValue) }
    val formState = LocalFormState.current
    DisposableEffect(state) {
        formState?.registerField(state)
        onDispose {
            formState?.unregisterField(state)
        }
    }
    return state
}
