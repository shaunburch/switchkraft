package dev.switchkraft

/**
 * The two modes a [Form] can be in.
 *
 * [FormField] reads the current mode from the nearest [FormState] (via [LocalFormState])
 * and renders either its view or edit content accordingly.
 */
enum class FormMode {
    /** Read-only display mode. */
    View,

    /** Interactive editing mode. */
    Edit
}
