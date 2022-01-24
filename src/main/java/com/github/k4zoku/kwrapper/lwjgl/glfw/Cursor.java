package com.github.k4zoku.kwrapper.lwjgl.glfw;

import com.github.k4zoku.kwrapper.lwjgl.common.Destroyable;
import com.github.k4zoku.kwrapper.lwjgl.glfw.exception.GLFWRuntimeException;
import org.lwjgl.glfw.GLFWImage;

import static org.lwjgl.glfw.GLFW.glfwCreateCursor;
import static org.lwjgl.glfw.GLFW.glfwDestroyCursor;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Cursor extends PointerHandle implements Destroyable {

    private boolean destroyed;

    private Cursor(long handle) {
        super(handle);
        if (getHandle() == NULL) {
            throw new GLFWRuntimeException("Cannot create cursor");
        }
        this.destroyed = false;
    }

    public Cursor(GLFWImage image, int xhot, int yhot) {
        this(glfwCreateCursor(image, xhot, yhot));
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void destroy() {
        if (isDestroyed()) {
            return;
        }

        glfwDestroyCursor(getHandle());
        this.destroyed = true;
    }
}
