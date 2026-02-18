package dev.switchkraft

import androidx.compose.runtime.Composable

/**
 * Generic switchable field — the backbone for building custom view/edit components.
 *
 * Reads the current [FormMode] from [LocalFormState] and delegates to either
 * [viewContent] or [editContent]. If no [FormState] is present in the
 * composition (i.e. used outside a [Form]), the field defaults to view mode.
 *
 * ```
 * FormField(
 *     state = myFieldState,
 *     viewContent = { value -> Text(value) },
 *     editContent = { fieldState ->
 *         TextField(
 *             value = fieldState.currentValue,
 *             onValueChange = { fieldState.currentValue = it }
 *         )
 *     }
 * )
 * ```
 *
 * @param T the value type of the field.
 * @param state the [FieldState] driving this field.
 * @param viewContent rendered in [FormMode.View] — receives the current value.
 * @param editContent rendered in [FormMode.Edit] — receives the full [FieldState] for mutation.
 */
@Composable
fun <T> FormField(
    state: FieldState<T>,
    viewContent: @Composable (T) -> Unit,
    editContent: @Composable (FieldState<T>) -> Unit
) {
    val formState = LocalFormState.current
    if (formState?.mode == FormMode.Edit) {
        editContent(state)
    } else {
        viewContent(state.currentValue)
    }
}
