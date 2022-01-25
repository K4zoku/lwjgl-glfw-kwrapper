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
import org.lwjgl.system.Callback;
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
     * <p>By default, both windowed and full screen mode windows are focused when initially created. Set the {@link GLFW#GLFW_FOCUSED FOCUSED} hint to disable this behavior.</p>
     *
     * <p>Also by default, windowed mode windows are focused when shown with {@link #show  ShowWindow}. Set the {@link GLFW#GLFW_FOCUS_ON_SHOW FOCUS_ON_SHOW} window hint to disable this behavior.</p>
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
     * <li><b>Wayland</b>: It is not possible for an application to set the input focus. This function will emit {@link GLFW#GLFW_FEATURE_UNAVAILABLE FEATURE_UNAVAILABLE}.</li>
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
     * <p>The returned string is allocated and freed by GLFW. You should not free it yourself. It is valid until the next call to {@link #getClipboardString GetClipboardString} or
     * {@link #setClipboardString  SetClipboardString}, or until the library is terminated.</p>
     *
     * <div style="margin-left: 26px; border-left: 1px solid gray; padding-left: 14px;"><h5>Note</h5>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li>The returned string is allocated and freed by GLFW.  You should not free it yourself.</li>
     * <li>The returned string is valid only until the next call to {@link #getClipboardString GetClipboardString} or {@link #getClipboardString SetClipboardString}.</li>
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

    /**
     * Retrieves the size, in pixels, of the framebuffer of the specified window. If you wish to retrieve the size of the window in screen coordinates, see
     * {@link #getSize GetWindowSize}.
     *
     * <p>Any or all of the size arguments may be {@code NULL}. If an error occurs, all non-{@code NULL} size arguments will be set to zero.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @return {@link Size<Integer>} The size, in pixels, of the framebuffer of the specified window.
     *
     * @since version 3.0
     */
    public Size<Integer> getFrameBufferSize() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            glfwGetFramebufferSize(getPointer(), width, height);
            return new Size<>(width.get(), height.get());
        }
    }

    /**
     * Returns the value of an input option for the specified window.
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @param mode   the input mode whose value to return. One of:<br><table><tr><td>{@link GLFW#GLFW_CURSOR CURSOR}</td><td>{@link GLFW#GLFW_STICKY_KEYS STICKY_KEYS}</td><td>{@link GLFW#GLFW_STICKY_MOUSE_BUTTONS STICKY_MOUSE_BUTTONS}</td><td>{@link GLFW#GLFW_LOCK_KEY_MODS LOCK_KEY_MODS}</td><td>{@link GLFW#GLFW_RAW_MOUSE_MOTION RAW_MOUSE_MOTION}</td></tr></table>
     *
     * @return the input mode value
     *
     * @since version 3.0
     */
    public int getInputMode(int mode) {
        return glfwGetInputMode(getPointer(), mode);
    }

    /**
     * Returns the last state reported for the specified key to the specified window. The returned state is one of {@link GLFW#GLFW_PRESS PRESS} or {@link GLFW#GLFW_RELEASE RELEASE}. The higher-level action
     * {@link GLFW#GLFW_REPEAT REPEAT} is only reported to the key callback.
     *
     * <p>If the {@link GLFW#GLFW_STICKY_KEYS STICKY_KEYS} input mode is enabled, this function returns {@link GLFW#GLFW_PRESS PRESS} the first time you call it for a key that was pressed, even if that
     * key has already been released.</p>
     *
     * <p>The key functions deal with physical keys, with key tokens named after their use on the standard US keyboard layout. If you want to input text, use the
     * Unicode character callback instead.</p>
     *
     * <p>The modifier key bit masks are not key tokens and cannot be used with this function.</p>
     *
     * <p><b>Do not use this function</b> to implement <a target="_blank" href="http://www.glfw.org/docs/latest/input.html#input_char">text input</a>.</p>
     *
     * <div style="margin-left: 26px; border-left: 1px solid gray; padding-left: 14px;"><h5>Note</h5>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li>{@link GLFW#GLFW_KEY_UNKNOWN KEY_UNKNOWN} is not a valid key for this function.</li>
     * </ul></div>
     *
     * @param key    the desired keyboard key
     *
     * @return one of {@link GLFW#GLFW_PRESS PRESS} or {@link GLFW#GLFW_RELEASE RELEASE}
     *
     * @since version 1.0
     */
    public int getKey(int key) {
        return glfwGetKey(getPointer(), key);
    }

    /**
     * Returns the last state reported for the specified mouse button to the specified window. The returned state is one of {@link GLFW#GLFW_PRESS PRESS} or {@link GLFW#GLFW_RELEASE RELEASE}. The
     * higher-level action {@link GLFW#GLFW_REPEAT REPEAT} is only reported to the mouse button callback.
     *
     * <p>If the {@link GLFW#GLFW_STICKY_MOUSE_BUTTONS STICKY_MOUSE_BUTTONS} input mode is enabled, this function returns {@link GLFW#GLFW_PRESS PRESS} the first time you call it for a mouse button that was pressed, even
     * if that mouse button has already been released.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @param button the desired mouse button
     *
     * @return one of {@link GLFW#GLFW_PRESS PRESS} or {@link GLFW#GLFW_RELEASE RELEASE}
     *
     * @since version 1.0
     */
    public int getMouseButton(int button) {
        return glfwGetMouseButton(getPointer(), button);
    }

    /**
     * Returns the value of an attribute of the specified window or its OpenGL or OpenGL ES context.
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * <p>Framebuffer related hints are not window attributes.</p>
     *
     * <p>Zero is a valid value for many window and context related attributes so you cannot use a return value of zero as an indication of errors. However, this
     * function should not fail as long as it is passed valid arguments and the library has been initialized.</p>
     *
     * @param attribute the <a href="http://www.glfw.org/docs/latest/window.html#window_attribs">window attribute</a> whose value to return. One of:<br><table><tr><td>{@link GLFW#GLFW_FOCUSED FOCUSED}</td><td>{@link GLFW#GLFW_ICONIFIED ICONIFIED}</td><td>{@link GLFW#GLFW_RESIZABLE RESIZABLE}</td><td>{@link GLFW#GLFW_VISIBLE VISIBLE}</td><td>{@link GLFW#GLFW_DECORATED DECORATED}</td></tr><tr><td>{@link GLFW#GLFW_FLOATING FLOATING}</td><td>{@link GLFW#GLFW_MAXIMIZED MAXIMIZED}</td><td>{@link GLFW#GLFW_CENTER_CURSOR CENTER_CURSOR}</td><td>{@link GLFW#GLFW_TRANSPARENT_FRAMEBUFFER TRANSPARENT_FRAMEBUFFER}</td><td>{@link GLFW#GLFW_HOVERED HOVERED}</td></tr><tr><td>{@link GLFW#GLFW_FOCUS_ON_SHOW FOCUS_ON_SHOW}</td><td>{@link GLFW#GLFW_MOUSE_PASSTHROUGH MOUSE_PASSTHROUGH}</td><td>{@link GLFW#GLFW_CLIENT_API CLIENT_API}</td><td>{@link GLFW#GLFW_CONTEXT_VERSION_MAJOR CONTEXT_VERSION_MAJOR}</td><td>{@link GLFW#GLFW_CONTEXT_VERSION_MINOR CONTEXT_VERSION_MINOR}</td></tr><tr><td>{@link GLFW#GLFW_CONTEXT_REVISION CONTEXT_REVISION}</td><td>{@link GLFW#GLFW_CONTEXT_ROBUSTNESS CONTEXT_ROBUSTNESS}</td><td>{@link GLFW#GLFW_OPENGL_FORWARD_COMPAT OPENGL_FORWARD_COMPAT}</td><td>{@link GLFW#GLFW_CONTEXT_DEBUG CONTEXT_DEBUG}</td><td>{@link GLFW#GLFW_OPENGL_DEBUG_CONTEXT OPENGL_DEBUG_CONTEXT}</td></tr><tr><td>{@link GLFW#GLFW_OPENGL_PROFILE OPENGL_PROFILE}</td><td>{@link GLFW#GLFW_CONTEXT_RELEASE_BEHAVIOR CONTEXT_RELEASE_BEHAVIOR}</td><td>{@link GLFW#GLFW_CONTEXT_NO_ERROR CONTEXT_NO_ERROR}</td><td>{@link GLFW#GLFW_CONTEXT_CREATION_API CONTEXT_CREATION_API}</td><td>{@link GLFW#GLFW_SCALE_TO_MONITOR SCALE_TO_MONITOR}</td></tr></table>
     *
     * @return the value of the attribute, or zero if an error occurred
     *
     * @since version 3.0
     */
    public int getAttribute(int attribute) {
        return glfwGetWindowAttrib(getPointer(), attribute);
    }

    /**
     * Retrieves the content scale for the specified window.
     *
     * <p>This function retrieves the content scale for the specified window. The content scale is the ratio between the current DPI and the platform's default
     * DPI. This is especially important for text and any UI elements. If the pixel dimensions of your UI scaled by this look appropriate on your machine then
     * it should appear at a reasonable size on other machines regardless of their DPI and scaling settings. This relies on the system DPI and scaling
     * settings being somewhat correct.</p>
     *
     * <p>On platforms where each monitor can have its own content scale, the window content scale will depend on which monitor the system considers the window
     * to be on.</p>
     *
     * @return {@link ContentScale} The content scale for the specified window.
     *
     * @since version 3.3
     */
    public ContentScale getContentScale() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer xscale = stack.mallocFloat(1);
            FloatBuffer yscale = stack.mallocFloat(1);
            glfwGetWindowContentScale(getPointer(), xscale, yscale);
            return new ContentScale(xscale.get(), yscale.get());
        }
    }

    /**
     * Retrieves the size, in screen coordinates, of each edge of the frame of the specified window. This size includes the title bar, if the window has one.
     * The size of the frame may vary depending on the <a target="_blank" href="http://www.glfw.org/docs/latest/window.html#window-hints_wnd">window-related hints</a> used to
     * create it.
     *
     * <p>Because this function retrieves the size of each window frame edge and not the offset along a particular coordinate axis, the retrieved values will
     * always be zero or positive.</p>
     *
     * <p>Any or all of the size arguments may be {@code NULL}. If an error occurs, all non-{@code NULL} size arguments will be set to zero.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @return {@link FrameSize} The size, in screen coordinates, of each edge of the frame of the specified window.
     *
     * @since version 3.1
     */
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

    /**
     * Returns the monitor instance that the specified window is in full screen on.
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @return the monitor
     *
     * @throws NullPointerException if no monitor is set
     *
     * @since version 3.0
     */
    public Monitor getMonitor() {
        return Monitor.getWindowMonitor(this);
    }

    /**
     * Returns the opacity of the whole window.
     *
     * <p>This function returns the opacity of the window, including any decorations.</p>
     *
     * <p>The opacity (or alpha) value is a positive finite number between zero and one, where zero is fully transparent and one is fully opaque.  If the system
     * does not support whole window transparency, this function always returns one.</p>
     *
     * <p>The initial opacity value for newly created windows is one.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @return the opacity value of the specified window
     *
     * @since version 3.3
     */
    public float getOpacity() {
        return glfwGetWindowOpacity(getPointer());
    }

    /**
     * Retrieves the position, in screen coordinates, of the upper-left corner of the content area of the specified window.
     *
     * <p>Any or all of the position arguments may be {@code NULL}. If an error occurs, all non-{@code NULL} position arguments will be set to zero.</p>
     *
     * <p>Notes:</p>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li><b>Wayland</b>: There is no way for an application to retrieve the global position of its windows. This function will emit {@link GLFW#GLFW_FEATURE_UNAVAILABLE FEATURE_UNAVAILABLE}.</li>
     * </ul>
     *
     * @return {@link Position} The position, in screen coordinates, of the upper-left corner of the content area of the specified window.
     *
     * @since version 3.0
     */
    public Position<Integer> getPosition() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer x = stack.mallocInt(1);
            IntBuffer y = stack.mallocInt(1);
            glfwGetWindowPos(getPointer(), x, y);
            return new Position<>(x.get(), y.get());
        }
    }

    /**
     * Retrieves the size, in screen coordinates, of the content area of the specified window. If you wish to retrieve the size of the framebuffer of the
     * window in pixels, see {@link #getFrameBufferSize GetFramebufferSize}.
     *
     * <p>Any or all of the size arguments may be {@code NULL}. If an error occurs, all non-{@code NULL} size arguments will be set to zero.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @return {@link Size<Integer>} The size, in screen coordinates, of the content area of the specified window.
     *
     * @since version 1.0
     */
    public Size<Integer> getSize() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            glfwGetWindowSize(getPointer(), width, height);
            return new Size<>(width.get(), height.get());
        }
    }

    /**
     * Returns the current value of the user-defined pointer of the specified window. The initial value is {@code NULL}.
     *
     * <p>This function may be called from any thread. Access is not synchronized.</p>
     *
     * @since version 3.0
     */
    public long getUserPointer() {
        return glfwGetWindowUserPointer(getPointer());
    }

    /**
     * Hides the specified window, if it was previously visible. If the window is already hidden or is in full screen mode, this function does nothing.
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @since version 3.0
     */
    public void hide() {
        glfwHideWindow(getPointer());
    }

    /**
     * Iconifies (minimizes) the specified window if it was previously restored. If the window is already iconified, this function does nothing.
     *
     * <p>If the specified window is a full screen window, the original monitor resolution is restored until the window is restored.</p>
     *
     * <p>Notes:</p>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li><b>Wayland</b>: Once a window is iconified, {@link #restore() RestoreWindow} won’t be able to restore it. This is a design decision of the {@code xdg-shell}.</li>
     * </ul>
     *
     * @since version 2.1
     */
    public void iconify() {
        glfwIconifyWindow(getPointer());
    }

    /**
     * Makes the OpenGL or OpenGL ES context of the specified window current on the calling thread. A context must only be made current on a single thread at
     * a time and each thread can have only a single current context at a time.
     *
     * <p>When moving a context between threads, you must make it non-current on the old thread before making it current on the new one.</p>
     *
     * <p>By default, making a context non-current implicitly forces a pipeline flush. On machines that support
     * <a target="_blank" href="https://www.khronos.org/registry/OpenGL/extensions/KHR/KHR_context_flush_control.txt">GL_KHR_context_flush_control</a>, you can control whether
     * a context performs this flush by setting the {@link GLFW#GLFW_CONTEXT_RELEASE_BEHAVIOR CONTEXT_RELEASE_BEHAVIOR}
     * <a target="_blank" href="http://www.glfw.org/docs/latest/window.html#window_hints_ctx">window hint</a>.</p>
     *
     * <p>The specified window must have an OpenGL or OpenGL ES context. Specifying a window without a context will generate a {@link GLFW#GLFW_NO_WINDOW_CONTEXT NO_WINDOW_CONTEXT} error.</p>
     *
     * <p>This function may be called from any thread.</p>
     *
     * @since version 3.0
     */
    public void makeContextCurrent() {
        glfwMakeContextCurrent(getPointer());
    }

    /**
     * Maximizes the specified window if it was previously not maximized. If the window is already maximized, this function does nothing.
     *
     * <p>If the specified window is a full screen window, this function does nothing.</p>
     *
     * <p>This function may only be called from the main thread.</p>
     *
     * @since version 3.2
     */
    public void maximize() {
        glfwMaximizeWindow(getPointer());
    }

    /**
     * Requests user attention to the specified window.
     *
     * <p>This function requests user attention to the specified window. On platforms where this is not supported, attention is requested to the application as
     * a whole.</p>
     *
     * <p>Once the user has given attention, usually by focusing the window or application, the system will end the request automatically.</p>
     *
     * <div style="margin-left: 26px; border-left: 1px solid gray; padding-left: 14px;"><h5>Note</h5>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li><b>macOS:</b> Attention is requested to the application as a whole, not the specific window.</li>
     * </ul></div>
     *
     * @since version 3.3
     */
    public void requestAttention() {
        glfwRequestWindowAttention(getPointer());
    }

    /**
     * Restores the specified window if it was previously iconified (minimized) or maximized. If the window is already restored, this function does nothing.
     *
     * <p>If the specified window is a full screen window, the resolution chosen for the window is restored on the selected monitor.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @since version 2.1
     */
    public void restore() {
        glfwRestoreWindow(getPointer());
    }

    /**
     * Sets the character callback of the specified window, which is called when a Unicode character is input.
     *
     * <p>The character callback is intended for Unicode text input. As it deals with characters, it is keyboard layout dependent, whereas {@link #setKeyCallback(GLFWKeyCallbackI)  SetKeyCallback} is
     * not. Characters do not map 1:1 to physical keys, as a key may produce zero, one or more characters. If you want to know whether a specific physical key
     * was pressed or released, see the key callback instead.</p>
     *
     * <p>The character callback behaves as system text input normally does and will not be called if modifier keys are held down that would prevent normal text
     * input on that platform, for example a Super (Command) key on macOS or Alt key on Windows.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @param callback  the new callback or {@code NULL} to remove the currently set callback
     *
     * @return the previously set callback, or {@code NULL} if no callback was set
     *
     * @since version 2.4
     */
    @Nullable
    public GLFWCharCallback setCharCallback(GLFWCharCallbackI callback) {
        return glfwSetCharCallback(getPointer(), callback);
    }

    /**
     * Sets the character with modifiers callback of the specified window, which is called when a Unicode character is input regardless of what modifier keys
     * are used.
     *
     * <p>The character with modifiers callback is intended for implementing custom Unicode character input. For regular Unicode text input, see
     * {@link #setCharCallback  SetCharCallback}. Like the character callback, the character with modifiers callback deals with characters and is keyboard layout dependent.
     * Characters do not map 1:1 to physical keys, as a key may produce zero, one or more characters. If you want to know whether a specific physical key was
     * pressed or released, see {@link #setKeyCallback(GLFWKeyCallbackI) SetKeyCallback} instead.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * <p>Deprecated: scheduled for removal in version 4.0.</p>
     *
     * @param callback  the new callback or {@code NULL} to remove the currently set callback
     *
     * @return the previously set callback, or {@code NULL} if no callback was set
     *
     * @since version 3.1
     */
    @Nullable
    public GLFWCharModsCallback setCharModsCallback(GLFWCharModsCallbackI callback) {
        return glfwSetCharModsCallback(getPointer(), callback);
    }

    /**
     * Sets the cursor image to be used when the cursor is over the content area of the specified window. The set cursor will only be visible when the
     * <a target="_blank" href="http://www.glfw.org/docs/latest/input.html#cursor_mode">cursor mode</a> of the window is {@link GLFW#GLFW_CURSOR_NORMAL CURSOR_NORMAL}.
     *
     * <p>On some platforms, the set cursor may not be visible unless the window also has input focus.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @param cursor the cursor to set, or {@code null} to switch back to the default arrow cursor
     *
     * @since version 3.1
     */
    public void setCursor(@Nullable Cursor cursor) {
        glfwSetCursor(getPointer(), cursor == null ? NULL : cursor.getPointer());
    }

    /**
     * Sets the cursor boundary crossing callback of the specified window, which is called when the cursor enters or leaves the content area of the window.
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @param callback  the new callback or {@code NULL} to remove the currently set callback
     *
     * @return the previously set callback, or {@code NULL} if no callback was set
     *
     * @since version 3.0
     */
    @Nullable
    public GLFWCursorEnterCallback setCursorEnterCallback(GLFWCursorEnterCallbackI callback) {
        return glfwSetCursorEnterCallback(getPointer(), callback);
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

    /**
     * Sets the cursor position callback of the specified window, which is called when the cursor is moved. The callback is provided with the position, in
     * screen coordinates, relative to the upper-left corner of the content area of the window.
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @param callback  the new callback or {@code NULL} to remove the currently set callback
     *
     * @return the previously set callback, or {@code NULL} if no callback was set
     *
     * @since version 1.0
     */
    @Nullable
    public GLFWCursorPosCallback setCursorPositionCallback(GLFWCursorPosCallbackI callback) {
        return glfwSetCursorPosCallback(getPointer(), callback);
    }

    /**
     * Sets the file drop callback of the specified window, which is called when one or more dragged files are dropped on the window.
     *
     * <p>Because the path array and its strings may have been generated specifically for that event, they are not guaranteed to be valid after the callback has
     * returned. If you wish to use them after the callback returns, you need to make a deep copy.</p>
     *
     * <p>Notes:</p>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li><b>Wayland</b>: File drop is currently unimplemented.</li>
     * </ul>
     *
     * @param callback  the new callback or {@code NULL} to remove the currently set callback
     *
     * @return the previously set callback, or {@code NULL} if no callback was set
     *
     * @since version 3.1
     */
    @Nullable
    public GLFWDropCallback setDropCallback(GLFWDropCallbackI callback) {
        return glfwSetDropCallback(getPointer(), callback);
    }

    /**
     * Sets the framebuffer resize callback of the specified window, which is called when the framebuffer of the specified window is resized.
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @param callback  the new callback or {@code NULL} to remove the currently set callback
     *
     * @return the previously set callback, or {@code NULL} if no callback was set or the library had not been
     *         <a target="_blank" href="http://www.glfw.org/docs/latest/intro.html#intro_init">initialized</a>
     *
     * @since version 3.0
     */
    @Nullable
    public GLFWFramebufferSizeCallback setFrameBufferSizeCallback(GLFWFramebufferSizeCallbackI callback) {
        return glfwSetFramebufferSizeCallback(getPointer(), callback);
    }

    /**
     * Sets an input option for the specified window.
     *
     * <p>If {@code mode} is {@link GLFW#GLFW_CURSOR CURSOR}, the value must be one of the following cursor modes:</p>
     *
     * <ul>
     * <li>{@link GLFW#GLFW_CURSOR_NORMAL CURSOR_NORMAL} makes the cursor visible and behaving normally.</li>
     * <li>{@link GLFW#GLFW_CURSOR_HIDDEN CURSOR_HIDDEN} makes the cursor invisible when it is over the content area of the window but does not restrict the cursor from leaving.</li>
     * <li>{@link GLFW#GLFW_CURSOR_DISABLED CURSOR_DISABLED} hides and grabs the cursor, providing virtual and unlimited cursor movement. This is useful for implementing for example 3D camera
     * controls.</li>
     * </ul>
     *
     * <p>If the {@code mode} is {@link GLFW#GLFW_STICKY_KEYS STICKY_KEYS}, the value must be either {@link GLFW#GLFW_TRUE TRUE} to enable sticky keys, or {@link GLFW#GLFW_FALSE FALSE} to disable it. If sticky keys are enabled, a key
     * press will ensure that {@link #getKey GetKey} returns {@link GLFW#GLFW_PRESS PRESS} the next time it is called even if the key had been released before the call. This is useful when you
     * are only interested in whether keys have been pressed but not when or in which order.</p>
     *
     * <p>If the {@code mode} is {@link GLFW#GLFW_STICKY_MOUSE_BUTTONS STICKY_MOUSE_BUTTONS}, the value must be either {@link GLFW#GLFW_TRUE TRUE} to enable sticky mouse buttons, or {@link GLFW#GLFW_FALSE FALSE} to disable it. If sticky mouse
     * buttons are enabled, a mouse button press will ensure that {@link #getMouseButton  GetMouseButton} returns {@link GLFW#GLFW_PRESS PRESS} the next time it is called even if the mouse button had
     * been released before the call. This is useful when you are only interested in whether mouse buttons have been pressed but not when or in which order.</p>
     *
     * <p>If the {@code mode} is {@link GLFW#GLFW_LOCK_KEY_MODS LOCK_KEY_MODS}, the value must be either {@link GLFW#GLFW_TRUE TRUE} to enable lock key modifier bits, or {@link GLFW#GLFW_FALSE FALSE} to disable them. If enabled,
     * callbacks that receive modifier bits will also have the {@link GLFW#GLFW_MOD_CAPS_LOCK MOD_CAPS_LOCK} bit set when the event was generated with Caps Lock on, and the {@link GLFW#GLFW_MOD_NUM_LOCK MOD_NUM_LOCK}
     * bit when Num Lock was on.</p>
     *
     * <p>If the mode is {@link GLFW#GLFW_RAW_MOUSE_MOTION RAW_MOUSE_MOTION}, the value must be either {@link GLFW#GLFW_TRUE TRUE} to enable raw (unscaled and unaccelerated) mouse motion when the cursor is disabled,
     * or {@link GLFW#GLFW_FALSE FALSE} to disable it. If raw motion is not supported, attempting to set this will emit {@link GLFW#GLFW_FEATURE_UNAVAILABLE FEATURE_UNAVAILABLE}. Call {@link GLFW#glfwRawMouseMotionSupported RawMouseMotionSupported} to
     * check for support.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @param mode   the input mode to set. One of:<br><table><tr><td>{@link GLFW#GLFW_CURSOR CURSOR}</td><td>{@link GLFW#GLFW_STICKY_KEYS STICKY_KEYS}</td><td>{@link GLFW#GLFW_STICKY_MOUSE_BUTTONS STICKY_MOUSE_BUTTONS}</td></tr></table>
     * @param value  the new value of the specified input mode
     *
     * @since GLFW 3.0
     */
    public void setInputMode(int mode, int value) {
        glfwSetInputMode(getPointer(), mode, value);
    }

    /**
     * Sets the key callback of the specified window, which is called when a key is pressed, repeated or released.
     *
     * <p>The key functions deal with physical keys, with layout independent key tokens named after their values in the standard US keyboard layout. If you want
     * to input text, use {@link #setCharCallback(GLFWCharCallbackI)  SetCharCallback} instead.</p>
     *
     * <p>When a window loses input focus, it will generate synthetic key release events for all pressed keys. You can tell these events from user-generated
     * events by the fact that the synthetic ones are generated after the focus loss event has been processed, i.e. after the window focus callback has been
     * called.</p>
     *
     * <p>The scancode of a key is specific to that platform or sometimes even to that machine. Scancodes are intended to allow users to bind keys that don't have
     * a GLFW key token. Such keys have {@code key} set to {@link GLFW#GLFW_KEY_UNKNOWN KEY_UNKNOWN}, their state is not saved and so it cannot be queried with {@link GLFW#glfwGetKey GetKey}.</p>
     *
     * <p>Sometimes GLFW needs to generate synthetic key events, in which case the scancode may be zero.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @param callback  the new callback or {@code NULL} to remove the currently set callback
     *
     * @return the previously set callback, or {@code NULL} if no callback was set
     *
     * @since version 1.0
     */
    @Nullable
    public GLFWKeyCallback setKeyCallback(GLFWKeyCallbackI callback) {
        return glfwSetKeyCallback(getPointer(), callback);
    }

    /**
     * Wrapper for {@link #setKeyCallback(GLFWKeyCallbackI) SetKeyCallback}.
     *
     * @param callback the new callback or {@code null} to remove the currently set callback
     */
    public void setKeyCallback(KeyCallback callback) {
        setKeyCallback((window, key, scancode, action, mods) -> callback.invoke(key, scancode, action, mods));
    }

    /**
     * Sets the mouse button callback of the specified window, which is called when a mouse button is pressed or released.
     *
     * <p>When a window loses input focus, it will generate synthetic mouse button release events for all pressed mouse buttons. You can tell these events from
     * user-generated events by the fact that the synthetic ones are generated after the focus loss event has been processed, i.e. after the window focus
     * callback has been called.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @param callback  the new callback or {@code NULL} to remove the currently set callback
     *
     * @return the previously set callback, or {@code NULL} if no callback was set
     *
     * @since version 1.0
     */
    @Nullable
    public GLFWMouseButtonCallback setMouseButtonCallback(GLFWMouseButtonCallbackI callback) {
        return glfwSetMouseButtonCallback(getPointer(), callback);
    }

    /**
     * Sets the scroll callback of the specified window, which is called when a scrolling device is used.
     *
     * <p>The scroll callback receives all scrolling input, like that from a mouse wheel or a touchpad scrolling area.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @param callback  the new callback or {@code NULL} to remove the currently set callback
     *
     * @return the previously set callback, or {@code NULL} if no callback was set
     *
     * @since version 2.1
     */
    @Nullable
    public GLFWScrollCallback setScrollCallback(GLFWScrollCallbackI callback) {
        return glfwSetScrollCallback(getPointer(), callback);
    }

    /**
     * Sets the required aspect ratio of the content area of the specified window. If the window is full screen, the aspect ratio only takes effect once it is
     * made windowed. If the window is not resizable, this function does nothing.
     *
     * <p>The aspect ratio is specified as a numerator and a denominator and both values must be greater than zero. For example, the common 16:9 aspect ratio is
     * specified as 16 and 9, respectively.</p>
     *
     * <p>If the numerator and denominator is set to {@link GLFW#GLFW_DONT_CARE DONT_CARE} then the aspect ratio limit is disabled.</p>
     *
     * <p>The aspect ratio is applied immediately to a windowed mode window and may cause it to be resized.</p>
     *
     * <p>Notes:</p>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li><b>Wayland</b>: The aspect ratio will not be applied until the window is actually resized, either by the user or by the compositor.</li>
     * </ul>
     *
     * @param numer  the numerator of the desired aspect ratio, or {@link GLFW#GLFW_DONT_CARE DONT_CARE}
     * @param denom  the denominator of the desired aspect ratio, or {@link GLFW#GLFW_DONT_CARE DONT_CARE}
     *
     * @since version 3.2
     */
    public void setAspectRatio(int numer, int denom) {
        glfwSetWindowAspectRatio(getPointer(), numer, denom);
    }

    /**
     * Sets an attribute of the specified window.
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @param attribute the attribute to set.
     *
     *               <p>Some of these attributes are ignored for full screen windows. The new value will take effect if the window is later made windowed.</p>
     *
     *               <p>Some of these attributes are ignored for windowed mode windows. The new value will take effect if the window is later made full screen.</p>
     *
     *               <p>Calling {@link #getAttribute  GetWindowAttrib} will always return the latest value, even if that value is ignored by the current mode of the window. One of:<br></p><table><tr><td>{@link GLFW#GLFW_DECORATED DECORATED}</td><td>{@link GLFW#GLFW_RESIZABLE RESIZABLE}</td><td>{@link GLFW#GLFW_FLOATING FLOATING}</td><td>{@link GLFW#GLFW_AUTO_ICONIFY AUTO_ICONIFY}</td><td>{@link GLFW#GLFW_FOCUS_ON_SHOW FOCUS_ON_SHOW}</td><td>{@link GLFW#GLFW_MOUSE_PASSTHROUGH MOUSE_PASSTHROUGH}</td></tr></table>
     * @param value  the value to set
     *
     * @since version 3.3
     */
    public void setAttribute(int attribute, int value) {
        glfwSetWindowAttrib(getPointer(), attribute, value);
    }

    /**
     * Sets the close callback of the specified window, which is called when the user attempts to close the window, for example by clicking the close widget in
     * the title bar.
     *
     * <p>The close flag is set before this callback is called, but you can modify it at any time with {@link #setShouldClose SetWindowShouldClose}.</p>
     *
     * <p>The close callback is not triggered by {@link #destroy DestroyWindow}.</p>
     *
     * <div style="margin-left: 26px; border-left: 1px solid gray; padding-left: 14px;"><h5>Note</h5>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li><b>macOS:</b> Selecting Quit from the application menu will trigger the close callback for all windows.</li>
     * </ul></div>
     *
     * @param callback  the new callback or {@code NULL} to remove the currently set callback
     *
     * @return the previously set callback, or {@code NULL} if no callback was set or the library had not been
     *         <a target="_blank" href="http://www.glfw.org/docs/latest/intro.html#intro_init">initialized</a>
     *
     * @since version 2.5
     */
    @Nullable
    public GLFWWindowCloseCallback setCloseCallback(GLFWWindowCloseCallbackI callback) {
        return glfwSetWindowCloseCallback(getPointer(), callback);
    }

    /**
     * Sets the window content scale callback for the specified window, which is called when the content scale of the specified window changes.
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @param callback  the new callback or {@code NULL} to remove the currently set callback
     *
     * @return the previously set callback, or {@code NULL} if no callback was set or the library had not been
     *         <a target="_blank" href="http://www.glfw.org/docs/latest/intro.html#intro_init">initialized</a>
     *
     * @since version 3.3
     */
    @Nullable
    public GLFWWindowContentScaleCallback setContentScaleCallback(GLFWWindowContentScaleCallbackI callback) {
        return glfwSetWindowContentScaleCallback(getPointer(), callback);
    }

    /**
     * Sets the focus callback of the specified window, which is called when the window gains or loses input focus.
     *
     * <p>After the focus callback is called for a window that lost input focus, synthetic key and mouse button release events will be generated for all such
     * that had been pressed. For more information, see {@link #setKeyCallback(GLFWKeyCallbackI) SetKeyCallback} and {@link #setMouseButtonCallback(GLFWMouseButtonCallbackI) SetMouseButtonCallback}.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @param callback  the new callback or {@code NULL} to remove the currently set callback
     *
     * @return the previously set callback, or {@code NULL} if no callback was set or the library had not been
     *         <a target="_blank" href="http://www.glfw.org/docs/latest/intro.html#intro_init">initialized</a>
     *
     * @since version 3.0
     */
    @Nullable
    public GLFWWindowFocusCallback setFocusCallback(GLFWWindowFocusCallbackI callback) {
        return glfwSetWindowFocusCallback(getPointer(), callback);
    }

    /**
     * Sets the iconification callback of the specified window, which is called when the window is iconified or restored.
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @param callback  the new callback or {@code NULL} to remove the currently set callback
     *
     * @return the previously set callback, or {@code NULL} if no callback was set or the library had not been
     *         <a target="_blank" href="http://www.glfw.org/docs/latest/intro.html#intro_init">initialized</a>
     *
     * @since version 3.0
     */
    @Nullable
    public GLFWWindowIconifyCallback setIconifyCallback(GLFWWindowIconifyCallbackI callback) {
        return glfwSetWindowIconifyCallback(getPointer(), callback);
    }

    /**
     * Sets the maximization callback of the specified window, which is called when the window is maximized or restored.
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @param callback  the new callback or {@code NULL} to remove the currently set callback
     *
     * @return the previously set callback, or {@code NULL} if no callback was set or the library had not been
     *         <a target="_blank" href="http://www.glfw.org/docs/latest/intro.html#intro_init">initialized</a>
     *
     * @since version 3.3
     */
    @Nullable
    public GLFWWindowMaximizeCallback setMaximizeCallback(GLFWWindowMaximizeCallbackI callback) {
        return glfwSetWindowMaximizeCallback(getPointer(), callback);
    }

    /**
     * Sets the mode, monitor, video mode and placement of a window.
     *
     * <p>This function sets the monitor that the window uses for full screen mode or, if the monitor is {@code NULL}, makes it windowed mode.</p>
     *
     * <p>When setting a monitor, this function updates the width, height and refresh rate of the desired video mode and switches to the video mode closest to
     * it. The window position is ignored when setting a monitor.</p>
     *
     * <p>When the monitor is {@code NULL}, the position, width and height are used to place the window content area. The refresh rate is ignored when no monitor is
     * specified.</p>
     *
     * <p>If you only wish to update the resolution of a full screen window or the size of a windowed mode window, see {@link #setSize(int, int) SetWindowSize}.</p>
     *
     * <p>When a window transitions from full screen to windowed mode, this function restores any previous window settings such as whether it is decorated,
     * floating, resizable, has size or aspect ratio limits, etc.</p>
     *
     * <p>Notes:</p>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li><b>Wayland</b>: The desired window position is ignored, as there is no way for an application to set this property.</li>
     * <li><b>Wayland</b>: Setting the window to full screen will not attempt to change the mode, no matter what the requested size or refresh rate.</li>
     * </ul>
     *
     * @param monitor     the desired monitor, or {@code NULL} to set windowed mode
     * @param xpos        the desired x-coordinate of the upper-left corner of the content area
     * @param ypos        the desired y-coordinate of the upper-left corner of the content area
     * @param width       the desired with, in screen coordinates, of the content area or video mode
     * @param height      the desired height, in screen coordinates, of the content area or video mode
     * @param refreshRate the desired refresh rate, in Hz, of the video mode, or {@link GLFW#GLFW_DONT_CARE DONT_CARE}
     *
     * @since version 3.2
     */
    public void setMonitor(Monitor monitor, int xpos, int ypos, int width, int height, int refreshRate) {
        glfwSetWindowMonitor(getPointer(), monitor.getPointer(), xpos, ypos, width, height, refreshRate);
    }

    /**
     * Wrapper for {@link #setMonitor(Monitor, int, int, int, int, int) SetWindowMonitor}.
     *
     * @param monitor the desired monitor, or {@code NULL} to set windowed mode
     * @param position the desired position of the upper-left corner of the content area
     * @param size the desired size of the content area
     * @param refreshRate the desired refresh rate, in Hz, of the video mode, or {@link GLFW#GLFW_DONT_CARE DONT_CARE}
     */
    public void setMonitor(Monitor monitor, Position<Integer> position, Size<Integer> size, int refreshRate) {
        setMonitor(monitor, position.getX(), position.getY(), size.getWidth(), size.getHeight(), refreshRate);
    }

    /**
     * Shorter version of {@link #setMonitor(Monitor, Position, Size, int) SetWindowMonitor}.
     *
     * @param monitor the desired monitor, or {@code NULL} to set windowed mode
     * @param geometry the desired position and size of the content area
     * @param refreshRate the desired refresh rate, in Hz, of the video mode, or {@link GLFW#GLFW_DONT_CARE DONT_CARE}
     */
    public void setMonitor(Monitor monitor, Geometry<Integer> geometry, int refreshRate) {
        setMonitor(monitor, geometry.getPosition(), geometry.getSize(), refreshRate);
    }

    /**
     * Sets the opacity of the whole window.
     *
     * <p>This function sets the opacity of the window, including any decorations.</p>
     *
     * <p>The opacity (or alpha) value is a positive finite number between zero and one, where zero is fully transparent and one is fully opaque.</p>
     *
     * <p>The initial opacity value for newly created windows is one.</p>
     *
     * <p>A window created with framebuffer transparency may not use whole window transparency. The results of doing this are undefined.</p>
     *
     * <p>Notes:</p>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li><b>Wayland</b>: There is no way to set an opacity factor for a window. This function will emit {@link GLFW#GLFW_FEATURE_UNAVAILABLE FEATURE_UNAVAILABLE}.</li>
     * </ul>
     *
     * @param opacity the desired opacity of the specified window
     *
     * @since version 3.3
     */
    public void setOpacity(float opacity) {
        glfwSetWindowOpacity(getPointer(), opacity);
    }

    /**
     * Sets the position, in screen coordinates, of the upper-left corner of the content area of the specified windowed mode window. If the window is a full
     * screen window, this function does nothing.
     *
     * <p><b>Do not use this function</b> to move an already visible window unless you have very good reasons for doing so, as it will confuse and annoy the
     * user.</p>
     *
     * <p>The window manager may put limits on what positions are allowed. GLFW cannot and should not override these limits.</p>
     *
     * <p>Notes:</p>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li><b>Wayland</b>: There is no way for an application to set the global position of its windows. This function will emit {@link GLFW#GLFW_FEATURE_UNAVAILABLE FEATURE_UNAVAILABLE}.</li>
     * </ul>
     *
     * @param xpos   the x-coordinate of the upper-left corner of the content area
     * @param ypos   the y-coordinate of the upper-left corner of the content area
     *
     * @since version 1.0
     */
    public void setPosition(int xpos, int ypos) {
        glfwSetWindowPos(getPointer(), xpos, ypos);
    }

    /**
     * Wrapper for {@link #setPosition(int, int) SetWindowPos}.
     *
     * @param position the desired position of the upper-left corner of the content area
     */
    public void setPosition(@NotNull Position<Integer> position) {
        setPosition(position.getX(), position.getY());
    }

    /**
     * Sets the position callback of the specified window, which is called when the window is moved. The callback is provided with the position, in screen
     * coordinates, of the upper-left corner of the content area of the window.
     *
     * <p>Notes:</p>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li><b>Wayland</b>: This callback will never be called, as there is no way for an application to know its global position.</li>
     * </ul>
     *
     * @param callback  the new callback or {@code NULL} to remove the currently set callback
     *
     * @return the previously set callback, or {@code NULL} if no callback was set or the library had not been
     *         <a target="_blank" href="http://www.glfw.org/docs/latest/intro.html#intro_init">initialized</a>
     *
     * @since version 1.0
     */
    @Nullable
    public GLFWWindowPosCallback setPositionCallback(GLFWWindowPosCallbackI callback) {
        return glfwSetWindowPosCallback(getPointer(), callback);
    }

    /**
     * Sets the refresh callback of the specified window, which is called when the content area of the window needs to be redrawn, for example if the window has
     * been exposed after having been covered by another window.
     *
     * <p>On compositing window systems such as Aero, Compiz or Aqua, where the window contents are saved off-screen, this callback may be called only very
     * infrequently or never at all.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @param callback  the new callback or {@code NULL} to remove the currently set callback
     *
     * @return the previously set callback, or {@code NULL} if no callback was set or the library had not been
     *         <a target="_blank" href="http://www.glfw.org/docs/latest/intro.html#intro_init">initialized</a>
     *
     * @since version 2.5
     */
    @Nullable
    public GLFWWindowRefreshCallback setRefreshCallback(GLFWWindowRefreshCallbackI callback) {
        return glfwSetWindowRefreshCallback(getPointer(), callback);
    }

    /**
     * Sets the size, in pixels, of the content area of the specified window.
     *
     * <p>For full screen windows, this function updates the resolution of its desired video mode and switches to the video mode closest to it, without affecting
     * the window's context. As the context is unaffected, the bit depths of the framebuffer remain unchanged.</p>
     *
     * <p>If you wish to update the refresh rate of the desired video mode in addition to its resolution, see {@link #setMonitor(Monitor, int, int, int, int, int) SetWindowMonitor}.</p>
     *
     * <p>The window manager may put limits on what sizes are allowed. GLFW cannot and should not override these limits.</p>
     *
     * <p>Notes:</p>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li><b>Wayland</b>: A full screen window will not attempt to change the mode, no matter what the requested size.</li>
     * </ul>
     *
     * @param width  the desired width, in screen coordinates, of the window content area
     * @param height the desired height, in screen coordinates, of the window content area
     *
     * @since version 1.0
     */
    public void setSize(int width, int height) {
        glfwSetWindowSize(getPointer(), width, height);
    }

    /**
     * Wrapper for {@link #setSize(int, int) SetWindowSize}.
     *
     * @param size the desired size of the content area of the window
     */
    public void setSize(@NotNull Size<Integer> size) {
        setSize(size.getWidth(), size.getHeight());
    }

    /**
     * Sets the size callback of the specified window, which is called when the window is resized. The callback is provided with the size, in screen
     * coordinates, of the content area of the window.
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @param callback  the new callback or {@code NULL} to remove the currently set callback
     *
     * @return the previously set callback, or {@code NULL} if no callback was set or the library had not been
     *         <a target="_blank" href="http://www.glfw.org/docs/latest/intro.html#intro_init">initialized</a>
     *
     * @since version 1.0
     */
    @Nullable
    public GLFWWindowSizeCallback setSizeCallback(GLFWWindowSizeCallbackI callback) {
        return glfwSetWindowSizeCallback(getPointer(), callback);
    }

    /**
     * Sets the size limits of the content area of the specified window. If the window is full screen, the size limits only take effect if once it is made
     * windowed. If the window is not resizable, this function does nothing.
     *
     * <p>The size limits are applied immediately to a windowed mode window and may cause it to be resized.</p>
     *
     * <p>The maximum dimensions must be greater than or equal to the minimum dimensions and all must be greater than or equal to zero.</p>
     *
     * <p>Notes:</p>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li><b>Wayland</b>: The size limits will not be applied until the window is actually resized, either by the user or by the compositor.</li>
     * </ul>
     *
     * @param minWidth  the minimum width, in screen coordinates, of the content area, or {@link GLFW#GLFW_DONT_CARE DONT_CARE}
     * @param minHeight the minimum height, in screen coordinates, of the content area, or {@link GLFW#GLFW_DONT_CARE DONT_CARE}
     * @param maxWidth  the maximum width, in screen coordinates, of the content area, or {@link GLFW#GLFW_DONT_CARE DONT_CARE}
     * @param maxHeight the maximum height, in screen coordinates, of the content area, or {@link GLFW#GLFW_DONT_CARE DONT_CARE}
     *
     * @since version 3.2
     */
    public void setSizeLimits(int minWidth, int minHeight, int maxWidth, int maxHeight) {
        glfwSetWindowSizeLimits(getPointer(), minWidth, minHeight, maxWidth, maxHeight);
    }

    /**
     * Wrapper for {@link #setSizeLimits(int, int, int, int) SetWindowSizeLimits}.
     *
     * @param minSize the minimum size of the content area of the window
     * @param maxSize the maximum size of the content area of the window
     */
    public void setSizeLimits(Size<Integer> minSize, Size<Integer> maxSize) {
        setSizeLimits(minSize.getWidth(), minSize.getHeight(), maxSize.getWidth(), maxSize.getHeight());
    }

    /**
     * Sets the window title, encoded as UTF-8, of the specified window.
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * <p><b>macOS</b>: The window title will not be updated until the next time you process events.</p>
     *
     * @param title  the UTF-8 encoded window title
     *
     * @since version 1.0
     */
    public void setTitle(CharSequence title) {
        glfwSetWindowTitle(getPointer(), title);
    }

    /**
     * Sets the window title, encoded as UTF-8, of the specified window.
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * <p><b>macOS</b>: The window title will not be updated until the next time you process events.</p>
     *
     * @param title  the UTF-8 encoded window title
     *
     * @since version 1.0
     */
    public void setTitle(ByteBuffer title) {
        glfwSetWindowTitle(getPointer(), title);
    }

    /**
     * Sets the user-defined pointer of the specified window. The current value is retained until the window is destroyed. The initial value is {@code NULL}.
     *
     * <p>This function may be called from any thread. Access is not synchronized.</p>
     *
     * @param pointer the new value
     *
     * @since version 3.0
     */
    public void setUserPointer(long pointer) {
        glfwSetWindowUserPointer(getPointer(), pointer);
    }

    /**
     * Makes the specified window visible if it was previously hidden. If the window is already visible or is in full screen mode, this function does nothing.
     *
     * <p>By default, windowed mode windows are focused when shown. Set the {@link GLFW#GLFW_FOCUS_ON_SHOW FOCUS_ON_SHOW} window hint to change this behavior for all newly created windows, or
     * change the behavior for an existing window with {@link #setAttribute(int, int) SetWindowAttrib}.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @since version 3.0
     */
    public void show() {
        glfwShowWindow(getPointer());
    }

    /**
     * Swaps the front and back buffers of the specified window when rendering with OpenGL or OpenGL ES. If the swap interval is greater than zero, the GPU
     * driver waits the specified number of screen updates before swapping the buffers.
     *
     * <p>The specified window must have an OpenGL or OpenGL ES context. Specifying a window without a context will generate a {@link GLFW#GLFW_NO_WINDOW_CONTEXT NO_WINDOW_CONTEXT} error.</p>
     *
     * <p>This function does not apply to Vulkan. If you are rendering with Vulkan, {@code vkQueuePresentKHR} instead.</p>
     *
     * <p><b>EGL</b>: The context of the specified window must be current on the calling thread.</p>
     *
     * <p>This function may be called from any thread.</p>
     *
     * @since version 1.0
     */
    public void swapBuffers() {
        glfwSwapBuffers(getPointer());
    }

    /**
     * Returns the value of the close flag of the specified window.
     *
     * <p>This function may be called from any thread.</p>
     *
     * @return the value of the close flag
     *
     * @since version 3.0
     */
    public boolean shouldClose() {
        return glfwWindowShouldClose(getPointer());
    }

    /**
     * Sets the value of the close flag of the specified window. This can be used to override the user's attempt to close the window, or to signal that it
     * should be closed.
     *
     * <p>This function may be called from any thread. Access is not synchronized.</p>
     *
     * @param shouldClose  the new value
     *
     * @since version 3.0
     */
    public void setShouldClose(boolean shouldClose) {
        glfwSetWindowShouldClose(getPointer(), shouldClose);
    }

    /**
     * Get the size, position of the window.
     *
     * @return the size, position of the window
     */
    public Geometry<Integer> getGeometry() {
        Size<Integer> size = getSize();
        Position<Integer> position = getPosition();
        return new Geometry<>(position, size);
    }

    /**
     * Set the size, position of the window.
     *
     * @param xpos the x position
     * @param ypos the y position
     * @param width the width
     * @param height the height
     */
    public void setGeometry(int xpos, int ypos, int width, int height) {
        setPosition(xpos, ypos);
        setSize(width, height);
    }

    /**
     * Set the size, position of the window.
     *
     * @param geometry the geometry to set
     */
    public void setGeometry(@NotNull Geometry<Integer> geometry) {
        setGeometry(geometry.getX(), geometry.getY(), geometry.getWidth(), geometry.getHeight());
    }

    /**
     * Determine if the window is destroyed.
     *
     * @return true if the window is destroyed
     */
    public final boolean isDestroyed() {
        return this.destroyed;
    }

    /**
     * Destroy the window.
     */
    public void destroy() {
        if (isDestroyed()) {
            return;
        }
        glfwDestroyWindow(getPointer());
        this.destroyed = true;
    }

    /**
     * Resets all callbacks for the specified GLFW window to {@code NULL} and {@link Callback#free frees} all previously set callbacks.
     *
     * <p>This method resets only callbacks registered with a GLFW window. Non-window callbacks (registered with
     * {@link GLFW#glfwSetErrorCallback SetErrorCallback}, {@link GLFW#glfwSetMonitorCallback SetMonitorCallback}, etc.) must be reset and freed
     * separately.</p>
     *
     * <p>This method is not official GLFW API. It exists in LWJGL to simplify window callback cleanup.</p>
     *
     */
    public void freeCallbacks() {
        glfwFreeCallbacks(getPointer());
    }

}
