package com.github.k4zoku.kwrapper.lwjgl.glfw.window;

import com.github.k4zoku.kwrapper.lwjgl.common.Destroyable;
import com.github.k4zoku.kwrapper.lwjgl.common.geometry.Geometry;
import com.github.k4zoku.kwrapper.lwjgl.common.geometry.Position;
import com.github.k4zoku.kwrapper.lwjgl.common.geometry.Size;
import com.github.k4zoku.kwrapper.lwjgl.glfw.common.geometry.ContentScale;
import com.github.k4zoku.kwrapper.lwjgl.glfw.common.pointer.Pointer;
import com.github.k4zoku.kwrapper.lwjgl.glfw.cursor.Cursor;
import com.github.k4zoku.kwrapper.lwjgl.glfw.exception.GLFWRuntimeException;
import com.github.k4zoku.kwrapper.lwjgl.glfw.monitor.Monitor;
import com.github.k4zoku.kwrapper.lwjgl.glfw.window.callback.KeyCallback;
import com.github.k4zoku.kwrapper.lwjgl.glfw.window.geometry.FrameSize;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Wrapper for GLFW window.
 *
 * @author k4zoku
 */
public class Window extends Pointer implements Destroyable {

    /**
     * Determine if the window is destroyed.
     */
    private boolean destroyed;

    /**
     * Private constructor, accepts pointer to GLFW window.
     *
     * @param pointer Pointer to GLFW window.
     */
    private Window(long pointer) {
        super(pointer);
        this.destroyed = false;
        if (getPointer() == NULL) {
            throw new GLFWRuntimeException("Failed to create the GLFW window");
        }
    }

    /**
     * Constructor that look like the raw "constructor"
     *
     * @param windowWidth Width of the window.
     * @param windowHeight Height of the window.
     * @param windowTitle Title of the window.
     * @param monitor Pointer to the monitor to use. (NULL for default monitor)
     * @param share Pointer to the window to share OpenGL objects with. (NULL for no sharing)
     */
    public Window(int windowWidth, int windowHeight, CharSequence windowTitle, long monitor, long share) {
        this(glfwCreateWindow(windowWidth, windowHeight, windowTitle, monitor, share));
    }

    /**
     * Constructor that look like the raw "constructor" but with a {@link Monitor} object.
     *
     * @param windowWidth Width of the window.
     * @param windowHeight Height of the window.
     * @param windowTitle Title of the window.
     * @param monitor {@link Monitor} object to use. (null for default monitor)
     * @param share Pointer to the window to share OpenGL objects with. (NULL for no sharing)
     */
    public Window(int windowWidth, int windowHeight, CharSequence windowTitle, @Nullable Monitor monitor, long share) {
        this(windowWidth, windowHeight, windowTitle, monitor == null ? NULL : monitor.getPointer(), share);
    }

    /**
     * Constructor with more oop approach.
     *
     * @param windowSize {@link Size<Integer>} object that represents the size of the window.
     * @param windowTitle Title of the window.
     * @param monitor {@link Monitor} object to use. (null for default monitor)
     * @param share Pointer to the window to share OpenGL objects with. (NULL for no sharing)
     */
    public Window(@NotNull Size<Integer> windowSize, CharSequence windowTitle, Monitor monitor, long share) {
        this(windowSize.getWidth(), windowSize.getHeight(), windowTitle, monitor, share);
    }

    /**
     * Constructor but with simple parameters.
     *
     * @param windowWidth Width of the window.
     * @param windowHeight Height of the window.
     * @param windowTitle Title of the window.
     */
    public Window(int windowWidth, int windowHeight, CharSequence windowTitle) {
        this(glfwCreateWindow(windowWidth, windowHeight, windowTitle, NULL, NULL));
    }

    /**
     * Constructor but with simple parameters and more oop approach.
     *
     * @param windowSize {@link Size<Integer>} object that represents the size of the window.
     * @param windowTitle Title of the window.
     */
    public Window(Size<Integer> windowSize, CharSequence windowTitle) {
        this(windowSize.getWidth(), windowSize.getHeight(), windowTitle);
    }

    /**
     * Constructor that look like the raw "constructor".
     *
     * @param windowWidth Width of the window.
     * @param windowHeight Height of the window.
     * @param windowTitle Title of the window.
     * @param monitor Pointer to the monitor to use. (NULL for default monitor)
     * @param share Pointer to the window to share OpenGL objects with. (NULL for no sharing)
     */
    public Window(int windowWidth, int windowHeight, ByteBuffer windowTitle, Monitor monitor, long share) {
        this(glfwCreateWindow(windowWidth, windowHeight, windowTitle, monitor != null ? monitor.getPointer() : NULL, share));
    }

    /**
     * Constructor but with more oop approach.
     *
     * @param windowSize {@link Size<Integer>} object that represents the size of the window.
     * @param windowTitle Title of the window.
     * @param monitor {@link Monitor} object to use. (null for default monitor)
     * @param share Pointer to the window to share OpenGL objects with. (NULL for no sharing)
     */
    public Window(Size<Integer> windowSize, ByteBuffer windowTitle, Monitor monitor, long share) {
        this(windowSize.getWidth(), windowSize.getHeight(), windowTitle, monitor, share);
    }

    /**
     * Constructor with simple parameters.
     *
     * @param windowWidth Width of the window.
     * @param windowHeight Height of the window.
     * @param windowTitle Title of the window.
     */
    public Window(int windowWidth, int windowHeight, ByteBuffer windowTitle) {
        this(glfwCreateWindow(windowWidth, windowHeight, windowTitle, NULL, NULL));
    }

    /**
     * Constructor with simple parameters and more oop approach.
     *
     * @param windowSize {@link Size<Integer>} object that represents the size of the window.
     * @param windowTitle Title of the window.
     */
    public Window(Size<Integer> windowSize, ByteBuffer windowTitle) {
        this(windowSize.getWidth(), windowSize.getHeight(), windowTitle);
    }

    /**
     * Sets the icon for the specified window.
     *
     * <p>This function sets the icon of the specified window. If passed an array of candidate images, those of or closest to the sizes desired by the system are
     * selected. If no images are specified, the window reverts to its default icon.</p>
     *
     * <p>The pixels are 32-bit, little-endian, non-premultiplied RGBA, i.e. eight bits per channel with the red channel first. They are arranged canonically as
     * packed sequential rows, starting from the top-left corner.</p>
     *
     * <p>The desired image sizes varies depending on platform and system settings. The selected images will be rescaled as needed. Good sizes include 16x16,
     * 32x32 and 48x48.</p>
     *
     * <p>Notes:</p>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li>The specified image data is copied before this function returns.</li>
     * <li><b>macOS</b>: Regular windows do not have icons on macOS. This function will emit {@link GLFW#GLFW_FEATURE_UNAVAILABLE FEATURE_UNAVAILABLE}. The dock icon will be the same as the
     * application bundle's icon. For more information on bundles, see the <a target="_blank" href="https://developer.apple.com/library/content/documentation/CoreFoundation/Conceptual/CFBundles/">Bundle Programming Guide</a> in the Mac Developer Library.</li>
     * <li><b>Wayland</b>: There is no existing protocol to change an icon, the window will thus inherit the one defined in the application's desktop file.
     * This function will emit {@link GLFW#GLFW_FEATURE_UNAVAILABLE FEATURE_UNAVAILABLE}.</li>
     * </ul>
     *
     * @param images the images to create the icon from. This is ignored if count is zero.
     *
     * @since version 3.2
     */
    public void setIcon(GLFWImage.Buffer images) {
        glfwSetWindowIcon(getPointer(), images);
    }

    /**
     * Brings the specified window to front and sets input focus. The window should already be visible and not iconified.
     *
     * <p>By default, both windowed and full screen mode windows are focused when initially created. Set the {@link #GLFW_FOCUSED FOCUSED} hint to disable this behavior.</p>
     *
     * <p>Also by default, windowed mode windows are focused when shown with {@link #show  ShowWindow}. Set the {@link #GLFW_FOCUS_ON_SHOW FOCUS_ON_SHOW} window hint to disable this behavior.</p>
     *
     * <p><b>Do not use this function</b> to steal focus from other applications unless you are certain that is what the user wants. Focus stealing can be
     * extremely disruptive.</p>
     *
     * <p>For a less disruptive way of getting the user's attention, see {@link #requestAttention  RequestWindowAttention}.</p>
     *
     * <p>Notes:</p>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li><b>Wayland</b>: It is not possible for an application to set the input focus. This function will emit {@link #GLFW_FEATURE_UNAVAILABLE FEATURE_UNAVAILABLE}.</li>
     * </ul>
     *
     *
     * @since version 3.2
     */
    public void focus() {
        glfwFocusWindow(getPointer());
    }

    /**
     * Returns the contents of the system clipboard, if it contains or is convertible to a UTF-8 encoded string. If the clipboard is empty or if its contents
     * cannot be converted, {@code NULL} is returned and a {@link GLFW#GLFW_FORMAT_UNAVAILABLE FORMAT_UNAVAILABLE} error is generated.
     *
     * <p>The returned string is allocated and freed by GLFW. You should not free it yourself. It is valid until the next call to {@link #glfwGetClipboardString GetClipboardString} or
     * {@link #setClipboardString  SetClipboardString}, or until the library is terminated.</p>
     *
     * <div style="margin-left: 26px; border-left: 1px solid gray; padding-left: 14px;"><h5>Note</h5>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li>The returned string is allocated and freed by GLFW.  You should not free it yourself.</li>
     * <li>The returned string is valid only until the next call to {@link #getClipboardString GetClipboardString} or {@link #glfwSetClipboardString SetClipboardString}.</li>
     * </ul></div>
     *
     *
     * @return the contents of the clipboard as a UTF-8 encoded string, or {@code NULL} if an error occurred
     *
     * @since version 3.0
     */
    @Nullable
    public String getClipboardString() {
        return glfwGetClipboardString(getPointer());
    }

    /**
     * Sets the system clipboard to the specified, UTF-8 encoded string.
     *
     * <p>The specified string is copied before this function returns.</p>
     *
     * <p>Notes:</p>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * </ul>
     *
     * @param string a UTF-8 encoded string
     *
     * @since version 3.0
     */
    public void setClipboardString(CharSequence string) {
        glfwSetClipboardString(getPointer(), string);
    }

    /**
     * Returns the position of the cursor, in screen coordinates, relative to the upper-left corner of the content area of the specified window.
     *
     * <p>If the cursor is disabled (with {@link GLFW#GLFW_CURSOR_DISABLED CURSOR_DISABLED}) then the cursor position is unbounded and limited only by the minimum and maximum values of a
     * <b>double</b>.</p>
     *
     * <p>The coordinates can be converted to their integer equivalents with the {@link Math#floor} function. Casting directly to an integer type works for positive
     * coordinates, but fails for negative ones.</p>
     *
     * <p>Any or all of the position arguments may be {@code NULL}. If an error occurs, all non-{@code NULL} position arguments will be set to zero.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     *
     * @return The position of the cursor.
     *
     * @since version 1.0
     */
    public Position<Double> getCursorPosition() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            DoubleBuffer x = stack.mallocDouble(1);
            DoubleBuffer y = stack.mallocDouble(1);
            glfwGetCursorPos(getPointer(), x, y);
            return new Position<>(x.get(), y.get());
        }
    }

    /**
     * Sets the position, in screen coordinates, of the cursor relative to the upper-left corner of the content area of the specified window. The window must
     * have input focus. If the window does not have input focus when this function is called, it fails silently.
     *
     * <p><b>Do not use this function</b> to implement things like camera controls. GLFW already provides the {@link GLFW#GLFW_CURSOR_DISABLED CURSOR_DISABLED} cursor mode that hides the cursor,
     * transparently re-centers it and provides unconstrained cursor motion. See {@link #setInputMode SetInputMode} for more information.</p>
     *
     * <p>If the cursor mode is {@link GLFW#GLFW_CURSOR_DISABLED CURSOR_DISABLED} then the cursor position is unconstrained and limited only by the minimum and maximum values of <b>double</b>.</p>
     *
     * <p>Notes:</p>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li><b>Wayland</b>: This function will only work when the cursor mode is {@link GLFW#GLFW_CURSOR_DISABLED CURSOR_DISABLED}, otherwise it will do nothing.</li>
     * </ul>
     *
     *
     * @param position The position of the cursor to set.
     *
     * @since version 1.0
     */
    public void setCursorPosition(Position<Integer> position) {
        setCursorPosition(position.getX(), position.getY());
    }

    public Size<Integer> getFrameBufferSize() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            glfwGetFramebufferSize(getPointer(), width, height);
            return new Size<>(width.get(), height.get());
        }
    }

    public int getInputMode(int mode) {
        return glfwGetInputMode(getPointer(), mode);
    }

    public int getKey(int key) {
        return glfwGetKey(getPointer(), key);
    }

    public int getMouseButton(int button) {
        return glfwGetMouseButton(getPointer(), button);
    }

    public int getAttribute(int attribute) {
        return glfwGetWindowAttrib(getPointer(), attribute);
    }

    public ContentScale getContentScale() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer xscale = stack.mallocFloat(1);
            FloatBuffer yscale = stack.mallocFloat(1);
            glfwGetMonitorContentScale(getPointer(), xscale, yscale);
            return new ContentScale(xscale.get(), yscale.get());
        }
    }

    public FrameSize getFrameSize() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer left = stack.mallocInt(1);
            IntBuffer top = stack.mallocInt(1);
            IntBuffer right = stack.mallocInt(1);
            IntBuffer bottom = stack.mallocInt(1);
            glfwGetWindowFrameSize(getPointer(), left, top, right, bottom);
            return new FrameSize(left.get(), top.get(), right.get(), bottom.get());
        }
    }

    public Monitor getMonitor() {
        return Monitor.getWindowMonitor(this);
    }

    public float getOpacity() {
        return glfwGetWindowOpacity(getPointer());
    }

    public Position<Integer> getPosition() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer x = stack.mallocInt(1);
            IntBuffer y = stack.mallocInt(1);
            glfwGetWindowPos(getPointer(), x, y);
            return new Position<>(x.get(), y.get());
        }
    }

    public Size<Integer> getSize() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            glfwGetWindowSize(getPointer(), width, height);
            return new Size<>(width.get(), height.get());
        }
    }

    public long getUserPointer() {
        return glfwGetWindowUserPointer(getPointer());
    }

    public void hide() {
        glfwHideWindow(getPointer());
    }

    public void iconify() {
        glfwIconifyWindow(getPointer());
    }

    public void makeContextCurrent() {
        glfwMakeContextCurrent(getPointer());
    }

    public void maximize() {
        glfwMaximizeWindow(getPointer());
    }

    public void requestAttention() {
        glfwRequestWindowAttention(getPointer());
    }

    public void restore() {
        glfwRestoreWindow(getPointer());
    }

    public void setCharCallback(GLFWCharCallbackI callback) {
        glfwSetCharCallback(getPointer(), callback);
    }

    public void setCharModsCallback(GLFWCharModsCallbackI callback) {
        glfwSetCharModsCallback(getPointer(), callback);
    }

    public void setCursor(Cursor cursor) {
        glfwSetCursor(getPointer(), cursor.getPointer());
    }

    public void setCursorEnterCallback(GLFWCursorEnterCallbackI callback) {
        glfwSetCursorEnterCallback(getPointer(), callback);
    }

    /**
     * Sets the position, in screen coordinates, of the cursor relative to the upper-left corner of the content area of the specified window. The window must
     * have input focus. If the window does not have input focus when this function is called, it fails silently.
     *
     * <p><b>Do not use this function</b> to implement things like camera controls. GLFW already provides the {@link GLFW#GLFW_CURSOR_DISABLED CURSOR_DISABLED} cursor mode that hides the cursor,
     * transparently re-centers it and provides unconstrained cursor motion. See {@link #setInputMode SetInputMode} for more information.</p>
     *
     * <p>If the cursor mode is {@link GLFW#GLFW_CURSOR_DISABLED CURSOR_DISABLED} then the cursor position is unconstrained and limited only by the minimum and maximum values of <b>double</b>.</p>
     *
     * <p>Notes:</p>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li><b>Wayland</b>: This function will only work when the cursor mode is {@link GLFW#GLFW_CURSOR_DISABLED CURSOR_DISABLED}, otherwise it will do nothing.</li>
     * </ul>
     *
     *
     * @param xpos   the desired x-coordinate, relative to the left edge of the content area
     * @param ypos   the desired y-coordinate, relative to the top edge of the content area
     *
     * @since version 1.0
     */
    public void setCursorPosition(int xpos, int ypos) {
        glfwSetCursorPos(getPointer(), xpos, ypos);
    }

    public void setCursorPositionCallback(GLFWCursorPosCallbackI callback) {
        glfwSetCursorPosCallback(getPointer(), callback);
    }

    public void setDropCallback(GLFWDropCallbackI callback) {
        glfwSetDropCallback(getPointer(), callback);
    }

    public void setFrameBufferSizeCallback(GLFWFramebufferSizeCallbackI callback) {
        glfwSetFramebufferSizeCallback(getPointer(), callback);
    }

    public void setInputMode(int mode, int value) {
        glfwSetInputMode(getPointer(), mode, value);
    }

    public void setKeyCallback(GLFWKeyCallbackI callback) {
        glfwSetKeyCallback(getPointer(), callback);
    }

    public void setKeyCallback(KeyCallback callback) {
        setKeyCallback((window, key, scancode, action, mods) -> callback.invoke(key, scancode, action, mods));
    }

    public void setMouseButtonCallback(GLFWMouseButtonCallbackI callback) {
        glfwSetMouseButtonCallback(getPointer(), callback);
    }

    public void setScrollCallback(GLFWScrollCallbackI callback) {
        glfwSetScrollCallback(getPointer(), callback);
    }

    public void setAspectRatio(int numer, int denom) {
        glfwSetWindowAspectRatio(getPointer(), numer, denom);
    }

    public void setAttribute(int attribute, int value) {
        glfwSetWindowAttrib(getPointer(), attribute, value);
    }

    public void setCloseCallback(GLFWWindowCloseCallbackI callback) {
        glfwSetWindowCloseCallback(getPointer(), callback);
    }

    public void setContentScaleCallback(GLFWWindowContentScaleCallbackI callback) {
        glfwSetWindowContentScaleCallback(getPointer(), callback);
    }

    public void setFocusCallback(GLFWWindowFocusCallbackI callback) {
        glfwSetWindowFocusCallback(getPointer(), callback);
    }

    public void setIconifyCallback(GLFWWindowIconifyCallbackI callback) {
        glfwSetWindowIconifyCallback(getPointer(), callback);
    }

    public void setMaximizeCallback(GLFWWindowMaximizeCallbackI callback) {
        glfwSetWindowMaximizeCallback(getPointer(), callback);
    }

    public void setMonitor(Monitor monitor, int xpos, int ypos, int width, int height, int refreshRate) {
        glfwSetWindowMonitor(getPointer(), monitor.getPointer(), xpos, ypos, width, height, refreshRate);
    }

    public void setMonitor(Monitor monitor, Position<Integer> position, Size<Integer> size, int refreshRate) {
        setMonitor(monitor, position.getX(), position.getY(), size.getWidth(), size.getHeight(), refreshRate);
    }

    public void setMonitor(Monitor monitor, Geometry<Integer> geometry, int refreshRate) {
        setMonitor(monitor, geometry.getPosition(), geometry.getSize(), refreshRate);
    }

    public void setOpacity(float opacity) {
        glfwSetWindowOpacity(getPointer(), opacity);
    }

    public void setPosition(int x, int y) {
        glfwSetWindowPos(getPointer(), x, y);
    }

    public void setPosition(@NotNull Position<Integer> position) {
        setPosition(position.getX(), position.getY());
    }

    public void setPositionCallback(GLFWWindowPosCallbackI callback) {
        glfwSetWindowPosCallback(getPointer(), callback);
    }

    public void setRefreshCallback(GLFWWindowRefreshCallbackI callback) {
        glfwSetWindowRefreshCallback(getPointer(), callback);
    }

    public void setSize(int width, int height) {
        glfwSetWindowSize(getPointer(), width, height);
    }

    public void setSize(@NotNull Size<Integer> size) {
        setSize(size.getWidth(), size.getHeight());
    }

    public void setSizeCallback(GLFWWindowSizeCallbackI callback) {
        glfwSetWindowSizeCallback(getPointer(), callback);
    }

    public void setSizeLimits(int minWidth, int minHeight, int maxWidth, int maxHeight) {
        glfwSetWindowSizeLimits(getPointer(), maxWidth, maxHeight, maxWidth, maxHeight);
    }

    public void setSizeLimits(Size<Integer> minSize, Size<Integer> maxSize) {
        setSizeLimits(minSize.getWidth(), minSize.getHeight(), maxSize.getWidth(), maxSize.getHeight());
    }

    public void setTitle(CharSequence title) {
        glfwSetWindowTitle(getPointer(), title);
    }

    public void setTitle(ByteBuffer title) {
        glfwSetWindowTitle(getPointer(), title);
    }

    public void setUserPointer(long pointer) {
        glfwSetWindowUserPointer(getPointer(), pointer);
    }

    public void show() {
        glfwShowWindow(getPointer());
    }

    public void swapBuffers() {
        glfwSwapBuffers(getPointer());
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(getPointer());
    }

    public void setShouldClose(boolean shouldClose) {
        glfwSetWindowShouldClose(getPointer(), shouldClose);
    }

    public Geometry<Integer> getGeometry() {
        Size<Integer> size = getSize();
        Position<Integer> position = getPosition();
        return new Geometry<>(position, size);
    }

    public void setGeometry(int x, int y, int width, int height) {
        setPosition(x, y);
        setSize(width, height);
    }

    public void setGeometry(@NotNull Geometry<Integer> geometry) {
        setGeometry(geometry.getX(), geometry.getY(), geometry.getWidth(), geometry.getHeight());
    }

    public final boolean isDestroyed() {
        return this.destroyed;
    }

    public void destroy() {
        if (isDestroyed()) {
            return;
        }
        glfwDestroyWindow(getPointer());
        this.destroyed = true;
    }

    public void freeCallbacks() {
        glfwFreeCallbacks(getPointer());
    }

}
