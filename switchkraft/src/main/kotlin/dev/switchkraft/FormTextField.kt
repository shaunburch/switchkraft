package dev.switchkraft

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A ready-made text field that switches between a plain [Text] (view mode) and
 * an [OutlinedTextField] (edit mode).
 *
 * Built on top of [FormField], so it automatically reads the current [FormMode]
 * from [LocalFormState].
 *
 * @param state the [FieldState] backing this field.
 * @param label the label shown on the [OutlinedTextField] in edit mode.
 * @param modifier applied to both the [Text] and the [OutlinedTextField].
 */
@Composable
fun FormTextField(
    state: FieldState<String>,
    label: String,
    modifier: Modifier = Modifier
) {
    FormField(
        state = state,
        viewContent = { value ->
            Text(text = value, modifier = modifier)
        },
        editContent = { fieldState ->
            OutlinedTextField(
                value = fieldState.currentValue,
                onValueChange = { fieldState.currentValue = it },
                label = { Text(label) },
                modifier = modifier
            )
        }
    )
}
