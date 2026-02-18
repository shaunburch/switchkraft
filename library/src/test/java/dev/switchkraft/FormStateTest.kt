package dev.switchkraft

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FormStateTest {

    @Test
    fun `default mode is View`() {
        val form = FormState()
        assertEquals(FormMode.View, form.mode)
    }

    @Test
    fun `respects initial mode`() {
        val form = FormState(FormMode.Edit)
        assertEquals(FormMode.Edit, form.mode)
    }

    @Test
    fun `toggleMode switches View to Edit`() {
        val form = FormState(FormMode.View)
        form.toggleMode()
        assertEquals(FormMode.Edit, form.mode)
    }

    @Test
    fun `toggleMode switches Edit to View`() {
        val form = FormState(FormMode.Edit)
        form.toggleMode()
        assertEquals(FormMode.View, form.mode)
    }

    @Test
    fun `fields list is initially empty`() {
        val form = FormState()
        assertTrue(form.fields.isEmpty())
    }

    @Test
    fun `registerField adds field`() {
        val form = FormState()
        val field = FieldState("x")
        form.registerField(field)
        assertEquals(1, form.fields.size)
        assertTrue(field in form.fields)
    }

    @Test
    fun `registerField is idempotent`() {
        val form = FormState()
        val field = FieldState("x")
        form.registerField(field)
        form.registerField(field)
        assertEquals(1, form.fields.size)
    }

    @Test
    fun `unregisterField removes field`() {
        val form = FormState()
        val field = FieldState("x")
        form.registerField(field)
        form.unregisterField(field)
        assertTrue(form.fields.isEmpty())
    }

    @Test
    fun `unregisterField on unknown field is no-op`() {
        val form = FormState()
        val field = FieldState("x")
        form.unregisterField(field) // should not throw
        assertTrue(form.fields.isEmpty())
    }

    @Test
    fun `isModified is false when no fields registered`() {
        val form = FormState()
        assertFalse(form.isModified)
    }

    @Test
    fun `isModified is false when all fields are unmodified`() {
        val form = FormState()
        form.registerField(FieldState("a"))
        form.registerField(FieldState("b"))
        assertFalse(form.isModified)
    }

    @Test
    fun `isModified is true when any field is modified`() {
        val form = FormState()
        val field1 = FieldState("a")
        val field2 = FieldState("b")
        form.registerField(field1)
        form.registerField(field2)
        field2.currentValue = "changed"
        assertTrue(form.isModified)
    }

    @Test
    fun `revert reverts all registered fields`() {
        val form = FormState()
        val field1 = FieldState("a")
        val field2 = FieldState("b")
        form.registerField(field1)
        form.registerField(field2)
        field1.currentValue = "x"
        field2.currentValue = "y"
        form.revert()
        assertEquals("a", field1.currentValue)
        assertEquals("b", field2.currentValue)
        assertFalse(form.isModified)
    }

    @Test
    fun `commit commits all registered fields`() {
        val form = FormState()
        val field1 = FieldState("a")
        val field2 = FieldState("b")
        form.registerField(field1)
        form.registerField(field2)
        field1.currentValue = "x"
        field2.currentValue = "y"
        form.commit()
        assertEquals("x", field1.originalValue)
        assertEquals("y", field2.originalValue)
        assertFalse(form.isModified)
    }

    @Test
    fun `revert only affects registered fields`() {
        val form = FormState()
        val registered = FieldState("a")
        val unregistered = FieldState("b")
        form.registerField(registered)
        registered.currentValue = "x"
        unregistered.currentValue = "y"
        form.revert()
        assertEquals("a", registered.currentValue)
        assertEquals("y", unregistered.currentValue) // unchanged
    }

    @Test
    fun `multiple register and unregister cycles`() {
        val form = FormState()
        val field = FieldState("a")
        form.registerField(field)
        form.unregisterField(field)
        form.registerField(field)
        assertEquals(1, form.fields.size)
    }
}
