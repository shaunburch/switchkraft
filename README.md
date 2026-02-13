# Switchkraft

A Jetpack Compose library for building forms that switch between **View** and **Edit** modes, with built-in revert and commit support.

## Core Concepts

| Type | Role |
|---|---|
| `FormMode` | Enum — `View` or `Edit` |
| `FieldState<T>` | Per-field state: tracks original vs. current value |
| `FormState` | Form-level aggregator: holds the mode and all registered fields |
| `Form` | Composable wrapper that provides `FormState` via `CompositionLocal` |
| `FormField` | Generic composable that renders view or edit content based on mode |
| `FormTextField` | Ready-made text field built on `FormField` |

## Quick Start

```kotlin
@Composable
fun ProfileForm() {
    val formState = rememberFormState()

    Form(state = formState) {
        val name = rememberFieldState("Alice")
        val email = rememberFieldState("alice@example.com")

        Column {
            FormTextField(state = name, label = "Name")
            FormTextField(state = email, label = "Email")

            Button(onClick = { formState.toggleMode() }) {
                Text(if (formState.mode == FormMode.View) "Edit" else "Done")
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
```

## Field State

`rememberFieldState(initialValue)` creates a `FieldState<T>` and auto-registers it with the nearest `FormState`. Each field tracks:

- **`originalValue`** — the last-committed baseline
- **`currentValue`** — the live value (mutate directly from inputs)
- **`isModified`** — `true` when `currentValue != originalValue`

Per-field operations:

```kotlin
val name = rememberFieldState("Alice")

name.currentValue = "Bob"   // edit
name.isModified             // true
name.revert()               // back to "Alice"
name.commit()               // "Bob" becomes the new baseline
```

## Form State

`rememberFormState()` creates a `FormState` that holds the current `FormMode` and all registered fields. Aggregate operations:

```kotlin
formState.isModified   // true if any field is modified
formState.revert()     // revert all fields
formState.commit()     // commit all fields
formState.toggleMode() // flip between View and Edit
```

## Custom Fields with FormField

`FormField` is the building block for creating your own switchable components:

```kotlin
@Composable
fun FormDatePicker(state: FieldState<LocalDate>, modifier: Modifier = Modifier) {
    FormField(
        state = state,
        viewContent = { date ->
            Text(date.toString(), modifier = modifier)
        },
        editContent = { fieldState ->
            DatePicker(
                value = fieldState.currentValue,
                onValueChange = { fieldState.currentValue = it },
                modifier = modifier
            )
        }
    )
}
```

## Standalone Usage

Fields and `FormField` work without a `Form` wrapper — they just default to view mode. This can be useful for read-only displays that share the same component definitions.
