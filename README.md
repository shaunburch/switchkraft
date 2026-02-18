# Switchkraft

A Jetpack Compose library for building forms that switch between **View** and **Edit** modes, with built-in revert and commit support.

## Installation

### Stable releases (Maven Central)

No extra repository configuration needed. Add the dependency to your module's `build.gradle.kts`:

```kotlin
implementation("dev.switchkraft:switchkraft:VERSION")
```

Replace `VERSION` with the latest release version.

### Snapshots (GitHub Packages)

Snapshots are published automatically after every push to `main`. They require a GitHub Personal Access Token (PAT) with `read:packages` scope.

1. Create a PAT at GitHub → Settings → Developer settings → Personal access tokens → Fine-grained tokens (or classic with `read:packages`).

2. Add your credentials to `~/.gradle/gradle.properties`:
   ```properties
   gpr.user=YOUR_GITHUB_USERNAME
   gpr.key=YOUR_GITHUB_PAT
   ```

3. In your project's `settings.gradle.kts`, add the GitHub Packages repository:
   ```kotlin
   dependencyResolutionManagement {
       repositories {
           maven {
               url = uri("https://maven.pkg.github.com/shaunburch/switchkraft")
               credentials {
                   username = providers.gradleProperty("gpr.user").get()
                   password = providers.gradleProperty("gpr.key").get()
               }
           }
       }
   }
   ```

4. Add the snapshot dependency to your module's `build.gradle.kts`:
   ```kotlin
   implementation("dev.switchkraft:switchkraft:VERSION-SNAPSHOT")
   ```

---

## Releases

Switchkraft follows a snapshot → pre-release → stable version flow.

| Artifact | How it's published |
|---|---|
| `X.Y.Z-SNAPSHOT` | Automatically on every push to `main` → GitHub Packages |
| `X.Y.Z-alphaNNN` | Create a GitHub Release (marked pre-release) with tag `vX.Y.Z-alphaNNN` → Maven Central |
| `X.Y.Z` | Create a GitHub Release (not pre-release) with tag `vX.Y.Z` → Maven Central |

After a stable release, `gradle.properties` is automatically updated to the next minor snapshot (e.g. `0.1.0` → `0.2.0-SNAPSHOT`) via a bot commit.

---

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
