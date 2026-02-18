package dev.switchkraft

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * Observable state holder for a single form field.
 *
 * Tracks both the [originalValue] (the last-committed baseline) and the
 * [currentValue] (what the user is actively editing). Comparing the two
 * drives [isModified], and the [revert]/[commit] helpers let callers
 * roll back or accept changes.
 *
 * @param T the type of value this field holds.
 * @param initialValue the starting value for both [originalValue] and [currentValue].
 */
@Stable
class FieldState<T>(initialValue: T) {
    /** The last-committed baseline value. Updated by [commit]. */
    var originalValue: T by mutableStateOf(initialValue)
        private set

    /** The live value being edited. Mutate this directly from input fields. */
    var currentValue: T by mutableStateOf(initialValue)

    /** `true` when [currentValue] differs from [originalValue]. */
    val isModified: Boolean
        get() = currentValue != originalValue

    /** Resets [currentValue] back to [originalValue], discarding edits. */
    fun revert() {
        currentValue = originalValue
    }

    /** Promotes [currentValue] to the new [originalValue], accepting edits. */
    fun commit() {
        originalValue = currentValue
    }
}

/**
 * Creates and remembers a [FieldState] scoped to the current composition.
 *
 * If a [FormState] is provided via [LocalFormState], the field automatically
 * registers itself on enter and unregisters on leave, so that form-level
 * operations like [FormState.revert] and [FormState.commit] include this field.
 *
 * @param T the type of value this field holds.
 * @param initialValue the starting value for the field.
 */
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
