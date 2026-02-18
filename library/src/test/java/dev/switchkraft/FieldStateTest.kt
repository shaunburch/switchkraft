package dev.switchkraft

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FieldStateTest {

    @Test
    fun `initial values match`() {
        val state = FieldState("hello")
        assertEquals("hello", state.originalValue)
        assertEquals("hello", state.currentValue)
    }

    @Test
    fun `isModified is false when unchanged`() {
        val state = FieldState(42)
        assertFalse(state.isModified)
    }

    @Test
    fun `isModified is true after mutation`() {
        val state = FieldState("a")
        state.currentValue = "b"
        assertTrue(state.isModified)
    }

    @Test
    fun `isModified returns to false when set back to original`() {
        val state = FieldState("a")
        state.currentValue = "b"
        state.currentValue = "a"
        assertFalse(state.isModified)
    }

    @Test
    fun `revert restores currentValue to originalValue`() {
        val state = FieldState(10)
        state.currentValue = 99
        state.revert()
        assertEquals(10, state.currentValue)
        assertFalse(state.isModified)
    }

    @Test
    fun `commit promotes currentValue to originalValue`() {
        val state = FieldState("old")
        state.currentValue = "new"
        state.commit()
        assertEquals("new", state.originalValue)
        assertEquals("new", state.currentValue)
        assertFalse(state.isModified)
    }

    @Test
    fun `revert after commit uses new baseline`() {
        val state = FieldState("v1")
        state.currentValue = "v2"
        state.commit()
        state.currentValue = "v3"
        state.revert()
        assertEquals("v2", state.currentValue)
    }

    @Test
    fun `works with nullable types`() {
        val state = FieldState<String?>(null)
        assertFalse(state.isModified)
        state.currentValue = "something"
        assertTrue(state.isModified)
        state.revert()
        assertEquals(null, state.currentValue)
    }
}
