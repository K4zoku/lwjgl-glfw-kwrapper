package com.github.k4zoku.kwrapper.lwjgl.glfw.cursor;

import com.github.k4zoku.kwrapper.lwjgl.common.Destroyable;
import com.github.k4zoku.kwrapper.lwjgl.glfw.common.pointer.Pointer;
import com.github.k4zoku.kwrapper.lwjgl.glfw.exception.GLFWRuntimeException;
import com.github.k4zoku.kwrapper.lwjgl.glfw.window.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;

import static org.lwjgl.glfw.GLFW.glfwCreateCursor;
import static org.lwjgl.glfw.GLFW.glfwDestroyCursor;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Cursor extends Pointer implements Destroyable {

    private boolean destroyed;

    /**
     * Private constructor to wrap a pointer to a GLFW Cursor.
     *
     * @param pointer the pointer to a GLFW Cursor
     */
    private Cursor(long pointer) {
        super(pointer);
        if (getPointer() == NULL) {
            throw new GLFWRuntimeException("Cannot create cursor");
        }
        this.destroyed = false;
    }

    /**
     * Creates a new custom cursor image that can be set for a window with {@link Window#setCursor SetCursor}. The cursor can be destroyed with {@link #destroy DestroyCursor}. Any remaining
     * cursors are destroyed by {@link GLFW#glfwTerminate Terminate}.
     *
     * <p>The pixels are 32-bit, little-endian, non-premultiplied RGBA, i.e. eight bits per channel with the red channel first. They are arranged canonically as
     * packed sequential rows, starting from the top-left corner.</p>
     *
     * <p>The cursor hotspot is specified in pixels, relative to the upper-left corner of the cursor image. Like all other coordinate systems in GLFW, the X-axis
     * points to the right and the Y-axis points down.</p>
     *
     * <div style="margin-left: 26px; border-left: 1px solid gray; padding-left: 14px;"><h5>Note</h5>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li>The specified image data is copied before this function returns.</li>
     * </ul></div>
     *
     * @param image the desired cursor image
     * @param xhot  the desired x-coordinate, in pixels, of the cursor hotspot
     * @param yhot  the desired y-coordinate, in pixels, of the cursor hotspot
     *
     * @since version 3.1
     */
    public Cursor(GLFWImage image, int xhot, int yhot) {
        this(glfwCreateCursor(image, xhot, yhot));
    }

    /**
     * Returns whether this cursor is destroyed.
     *
     * @return whether this cursor is destroyed
     */
    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Destroys the cursor and releases the cursor image.
     */
    @Override
    public void destroy() {
        if (isDestroyed()) {
            return;
        }

        glfwDestroyCursor(getPointer());
        this.destroyed = true;
    }
}
