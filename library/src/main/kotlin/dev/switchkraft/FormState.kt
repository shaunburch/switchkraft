package dev.switchkraft

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * Form-level state aggregator.
 *
 * Holds the current [FormMode] and a live list of registered [FieldState]
 * instances. Provides aggregate operations ([isModified], [revert], [commit])
 * that fan out to every registered field.
 *
 * Fields register themselves automatically when created with [rememberFieldState]
 * inside a [Form] composable.
 *
 * @param initialMode the mode the form starts in (defaults to [FormMode.View]).
 */
@Stable
class FormState(initialMode: FormMode = FormMode.View) {
    /** The current view/edit mode. Use [toggleMode] to switch. */
    var mode: FormMode by mutableStateOf(initialMode)
        private set

    private val _fields = mutableStateListOf<FieldState<*>>()

    /** Snapshot-backed read-only view of all registered fields. */
    val fields: List<FieldState<*>> get() = _fields

    /** `true` when any registered field has been modified. */
    val isModified: Boolean
        get() = _fields.any { it.isModified }

    /** Adds [field] to the tracked set (no-op if already registered). */
    fun registerField(field: FieldState<*>) {
        if (field !in _fields) _fields.add(field)
    }

    /** Removes [field] from the tracked set. */
    fun unregisterField(field: FieldState<*>) {
        _fields.remove(field)
    }

    /** Reverts every registered field to its [FieldState.originalValue]. */
    fun revert() {
        _fields.forEach { it.revert() }
    }

    /** Commits every registered field, promoting current values to originals. */
    fun commit() {
        _fields.forEach { it.commit() }
    }

    /** Flips between [FormMode.View] and [FormMode.Edit]. */
    fun toggleMode() {
        mode = if (mode == FormMode.View) FormMode.Edit else FormMode.View
    }
}

/**
 * Creates and remembers a [FormState] scoped to the current composition.
 *
 * @param initialMode the mode the form starts in (defaults to [FormMode.View]).
 */
@Composable
fun rememberFormState(initialMode: FormMode = FormMode.View): FormState {
    return remember { FormState(initialMode) }
}
