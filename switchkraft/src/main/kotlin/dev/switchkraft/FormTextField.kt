package dev.switchkraft

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
