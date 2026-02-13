package dev.switchkraft

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * [CompositionLocal] that carries the nearest [FormState] down the tree.
 *
 * `null` when no [Form] wrapper is present, which lets [FormField] and
 * [rememberFieldState] work standalone (fields simply won't auto-register).
 */
val LocalFormState = staticCompositionLocalOf<FormState?> { null }

/**
 * Provides [state] to all descendant composables via [LocalFormState].
 *
 * Wrap your fields in [Form] so that [rememberFieldState] can auto-register
 * them and [FormField] can read the current [FormMode].
 *
 * ```
 * val formState = rememberFormState()
 * Form(state = formState) {
 *     val name = rememberFieldState("Alice")
 *     FormTextField(state = name, label = "Name")
 * }
 * ```
 *
 * @param state the [FormState] to provide.
 * @param content the composable subtree that will have access to [state].
 */
@Composable
fun Form(
    state: FormState,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalFormState provides state) {
        content()
    }
}
