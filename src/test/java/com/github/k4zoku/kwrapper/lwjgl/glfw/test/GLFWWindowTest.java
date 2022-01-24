package com.github.k4zoku.kwrapper.lwjgl.glfw.test;

import com.github.k4zoku.kwrapper.lwjgl.glfw.exception.GLFWRuntimeException;
import com.github.k4zoku.kwrapper.lwjgl.glfw.window.Window;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.system.MemoryUtil.NULL;

class GLFWWindowTest {

    @Test
    void windowTest() {
        // Init GLFW
        assertTrue(glfwInit());
        // Create new window
        Window window;
        try {
            window = new Window(640, 480, "Test");
        } catch (GLFWRuntimeException e) {
            window = null;
        }
        assertNotNull(window);
        assertNotEquals(NULL, window.getPointer());
    }


}
