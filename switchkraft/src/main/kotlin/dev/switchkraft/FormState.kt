package dev.switchkraft

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Stable
class FormState(initialMode: FormMode = FormMode.View) {
    var mode: FormMode by mutableStateOf(initialMode)
        private set

    private val _fields = mutableStateListOf<FieldState<*>>()
    val fields: List<FieldState<*>> get() = _fields

    val isModified: Boolean
        get() = _fields.any { it.isModified }

    fun registerField(field: FieldState<*>) {
        if (field !in _fields) _fields.add(field)
    }

    fun unregisterField(field: FieldState<*>) {
        _fields.remove(field)
    }

    fun revert() {
        _fields.forEach { it.revert() }
    }

    fun commit() {
        _fields.forEach { it.commit() }
    }

    fun toggleMode() {
        mode = if (mode == FormMode.View) FormMode.Edit else FormMode.View
    }
}

@Composable
fun rememberFormState(initialMode: FormMode = FormMode.View): FormState {
    return remember { FormState(initialMode) }
}
