package dev.switchkraft

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalFormState = staticCompositionLocalOf<FormState?> { null }

@Composable
fun Form(
    state: FormState,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalFormState provides state) {
        content()
    }
}
