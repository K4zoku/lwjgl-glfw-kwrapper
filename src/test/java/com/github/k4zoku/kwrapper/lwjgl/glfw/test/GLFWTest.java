package com.github.k4zoku.kwrapper.lwjgl.glfw.test;

import com.github.k4zoku.kwrapper.lwjgl.common.Size;
import com.github.k4zoku.kwrapper.lwjgl.glfw.Window;
import com.github.k4zoku.kwrapper.lwjgl.glfw.exception.GLFWRuntimeException;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GLFWTest {

    private Window window;

    public static void main(String[] args) {
        new GLFWTest().run();
    }

    public void init() {
        System.out.println("Initializing...");
        // Set up an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create window
        try {
            window = new Window(640, 480, "GLFW Test", NULL, NULL);
        } catch (GLFWRuntimeException e) {
            throw new IllegalStateException(e);
        }

        window.setKeyCallback((window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the resolution of the primary monitor
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        assert vidMode != null;
        Size<Integer> windowSize = window.getSize();

        // Center the window
        window.setPosition((vidMode.width() - windowSize.getWidth()) / 2, (vidMode.height() - windowSize.getHeight()) / 2);

        // Make the OpenGL context current
        window.makeContextCurrent();

        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        window.show();
    }

    public void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!window.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            window.swapBuffers(); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

    public void run() {
        init();
        loop();
        window.freeCallbacks();
        window.destroy();
    }
}
