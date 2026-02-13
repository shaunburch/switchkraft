package dev.switchkraft

import androidx.compose.runtime.Composable

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
