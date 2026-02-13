package dev.switchkraft

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FormScreen() {
    val formState = rememberFormState()

    MaterialTheme {
        Form(state = formState) {
            val firstName = rememberFieldState("John")
            val lastName = rememberFieldState("Doe")
            val email = rememberFieldState("john.doe@example.com")

            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Button(onClick = {
                    if (formState.mode == FormMode.Edit) formState.commit()
                    formState.toggleMode()
                }) {
                    Text(if (formState.mode == FormMode.View) "Edit" else "Done")
                }

                Spacer(modifier = Modifier.height(16.dp))

                FormTextField(
                    state = firstName,
                    label = "First Name",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                FormTextField(
                    state = lastName,
                    label = "Last Name",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                FormTextField(
                    state = email,
                    label = "Email",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (firstName.isModified) {
                    TextButton(onClick = { firstName.revert() }) {
                        Text("Revert First Name")
                    }
                }

                Button(
                    onClick = { formState.revert() },
                    enabled = formState.isModified
                ) {
                    Text("Revert All")
                }
            }
        }
    }
}
