package com.github.k4zoku.kwrapper.lwjgl.glfw.window;

import com.github.k4zoku.kwrapper.lwjgl.common.Destroyable;
import com.github.k4zoku.kwrapper.lwjgl.common.Geometry;
import com.github.k4zoku.kwrapper.lwjgl.common.Position;
import com.github.k4zoku.kwrapper.lwjgl.common.Size;
import com.github.k4zoku.kwrapper.lwjgl.glfw.ContentScale;
import com.github.k4zoku.kwrapper.lwjgl.glfw.Cursor;
import com.github.k4zoku.kwrapper.lwjgl.glfw.Monitor;
import com.github.k4zoku.kwrapper.lwjgl.glfw.PointerHandle;
import com.github.k4zoku.kwrapper.lwjgl.glfw.exception.GLFWRuntimeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window extends PointerHandle implements Destroyable {

    private boolean destroyed;

    private Window(long handle, boolean destroyed) {
        super(handle);
        this.destroyed = destroyed;
        if (getHandle() == NULL) {
            throw new GLFWRuntimeException("Failed to create the GLFW window");
        }
    }

    public Window(int windowWidth, int windowHeight, CharSequence windowTitle, long monitor, long share) {
        this(glfwCreateWindow(windowWidth, windowHeight, windowTitle, monitor, share), false);
    }

    public Window(int windowWidth, int windowHeight, CharSequence windowTitle, @Nullable Monitor monitor, long share) {
        this(windowWidth, windowHeight, windowTitle, monitor == null ? NULL : monitor.getHandle(), share);
    }

    public Window(@NotNull Size<Integer> windowSize, CharSequence windowTitle, Monitor monitor, long share) {
        this(windowSize.getWidth(), windowSize.getHeight(), windowTitle, monitor, share);
    }

    public Window(int windowWidth, int windowHeight, CharSequence windowTitle) {
        this(glfwCreateWindow(windowWidth, windowHeight, windowTitle, NULL, NULL), false);
    }

    public Window(Size<Integer> windowSize, CharSequence windowTitle) {
        this(windowSize.getWidth(), windowSize.getHeight(), windowTitle);
    }

    public Window(int windowWidth, int windowHeight, ByteBuffer windowTitle, Monitor monitor, long share) {
        this(glfwCreateWindow(windowWidth, windowHeight, windowTitle, monitor != null ? monitor.getHandle() : NULL, share), false);
    }

    public Window(Size<Integer> windowSize, ByteBuffer windowTitle, Monitor monitor, long share) {
        this(windowSize.getWidth(), windowSize.getHeight(), windowTitle, monitor, share);
    }

    public Window(int windowWidth, int windowHeight, ByteBuffer windowTitle) {
        this(glfwCreateWindow(windowWidth, windowHeight, windowTitle, NULL, NULL), false);
    }

    public Window(Size<Integer> windowSize, ByteBuffer windowTitle) {
        this(windowSize.getWidth(), windowSize.getHeight(), windowTitle);
    }

    public void setIcon(GLFWImage.Buffer icon) {
        glfwSetWindowIcon(getHandle(), icon);
    }

    public void focus() {
        glfwFocusWindow(getHandle());
    }

    public String getClipboardString() {
        return glfwGetClipboardString(getHandle());
    }

    public void setClipboardString(CharSequence string) {
        glfwSetClipboardString(getHandle(), string);
    }

    public Position<Double> getCursorPosition() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            DoubleBuffer x = stack.mallocDouble(1);
            DoubleBuffer y = stack.mallocDouble(1);
            glfwGetCursorPos(getHandle(), x, y);
            return new Position<>(x.get(), y.get());
        }
    }

    public void setCursorPosition(Position<Integer> position) {
        setCursorPosition(position.getX(), position.getY());
    }

    public Size<Integer> getFrameBufferSize() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            glfwGetFramebufferSize(getHandle(), width, height);
            return new Size<>(width.get(), height.get());
        }
    }

    public int getInputMode(int mode) {
        return glfwGetInputMode(getHandle(), mode);
    }

    public int getKey(int key) {
        return glfwGetKey(getHandle(), key);
    }

    public int getMouseButton(int button) {
        return glfwGetMouseButton(getHandle(), button);
    }

    public int getAttribute(int attribute) {
        return glfwGetWindowAttrib(getHandle(), attribute);
    }

    public ContentScale getContentScale() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer xscale = stack.mallocFloat(1);
            FloatBuffer yscale = stack.mallocFloat(1);
            glfwGetMonitorContentScale(getHandle(), xscale, yscale);
            return new ContentScale(xscale.get(), yscale.get());
        }
    }

    public FrameSize getFrameSize() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer left = stack.mallocInt(1);
            IntBuffer top = stack.mallocInt(1);
            IntBuffer right = stack.mallocInt(1);
            IntBuffer bottom = stack.mallocInt(1);
            glfwGetWindowFrameSize(getHandle(), left, top, right, bottom);
            return new FrameSize(left.get(), top.get(), right.get(), bottom.get());
        }
    }

    public Monitor getMonitor() {
        return Monitor.getWindowMonitor(this);
    }

    public float getOpacity() {
        return glfwGetWindowOpacity(getHandle());
    }

    public Position<Integer> getPosition() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer x = stack.mallocInt(1);
            IntBuffer y = stack.mallocInt(1);
            glfwGetWindowPos(getHandle(), x, y);
            return new Position<>(x.get(), y.get());
        }
    }

    public Size<Integer> getSize() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            glfwGetWindowSize(getHandle(), width, height);
            return new Size<>(width.get(), height.get());
        }
    }

    public long getUserPointer() {
        return glfwGetWindowUserPointer(getHandle());
    }

    public void hide() {
        glfwHideWindow(getHandle());
    }

    public void iconify() {
        glfwIconifyWindow(getHandle());
    }

    public void makeContextCurrent() {
        glfwMakeContextCurrent(getHandle());
    }

    public void maximize() {
        glfwMaximizeWindow(getHandle());
    }

    public void requestAttention() {
        glfwRequestWindowAttention(getHandle());
    }

    public void restore() {
        glfwRestoreWindow(getHandle());
    }

    public void setCharCallback(GLFWCharCallbackI callback) {
        glfwSetCharCallback(getHandle(), callback);
    }

    public void setCharModsCallback(GLFWCharModsCallbackI callback) {
        glfwSetCharModsCallback(getHandle(), callback);
    }

    public void setCursor(Cursor cursor) {
        glfwSetCursor(getHandle(), cursor.getHandle());
    }

    public void setCursorEnterCallback(GLFWCursorEnterCallbackI callback) {
        glfwSetCursorEnterCallback(getHandle(), callback);
    }

    public void setCursorPosition(int x, int y) {
        glfwSetCursorPos(getHandle(), x, y);
    }

    public void setCursorPositionCallback(GLFWCursorPosCallbackI callback) {
        glfwSetCursorPosCallback(getHandle(), callback);
    }

    public void setDropCallback(GLFWDropCallbackI callback) {
        glfwSetDropCallback(getHandle(), callback);
    }

    public void setFrameBufferSizeCallback(GLFWFramebufferSizeCallbackI callback) {
        glfwSetFramebufferSizeCallback(getHandle(), callback);
    }

    public void setInputMode(int mode, int value) {
        glfwSetInputMode(getHandle(), mode, value);
    }

    public void setKeyCallback(GLFWKeyCallbackI callback) {
        glfwSetKeyCallback(getHandle(), callback);
    }

    public void setKeyCallback(KeyCallback callback) {
        setKeyCallback((window, key, scancode, action, mods) -> callback.invoke(key, scancode, action, mods));
    }

    public void setMouseButtonCallback(GLFWMouseButtonCallbackI callback) {
        glfwSetMouseButtonCallback(getHandle(), callback);
    }

    public void setScrollCallback(GLFWScrollCallbackI callback) {
        glfwSetScrollCallback(getHandle(), callback);
    }

    public void setAspectRatio(int numer, int denom) {
        glfwSetWindowAspectRatio(getHandle(), numer, denom);
    }

    public void setAttribute(int attribute, int value) {
        glfwSetWindowAttrib(getHandle(), attribute, value);
    }

    public void setCloseCallback(GLFWWindowCloseCallbackI callback) {
        glfwSetWindowCloseCallback(getHandle(), callback);
    }

    public void setContentScaleCallback(GLFWWindowContentScaleCallbackI callback) {
        glfwSetWindowContentScaleCallback(getHandle(), callback);
    }

    public void setFocusCallback(GLFWWindowFocusCallbackI callback) {
        glfwSetWindowFocusCallback(getHandle(), callback);
    }

    public void setIconifyCallback(GLFWWindowIconifyCallbackI callback) {
        glfwSetWindowIconifyCallback(getHandle(), callback);
    }

    public void setMaximizeCallback(GLFWWindowMaximizeCallbackI callback) {
        glfwSetWindowMaximizeCallback(getHandle(), callback);
    }

    public void setMonitor(Monitor monitor, int xpos, int ypos, int width, int height, int refreshRate) {
        glfwSetWindowMonitor(getHandle(), monitor.getHandle(), xpos, ypos, width, height, refreshRate);
    }

    public void setMonitor(Monitor monitor, Position<Integer> position, Size<Integer> size, int refreshRate) {
        setMonitor(monitor, position.getX(), position.getY(), size.getWidth(), size.getHeight(), refreshRate);
    }

    public void setMonitor(Monitor monitor, Geometry<Integer> geometry, int refreshRate) {
        setMonitor(monitor, geometry.getPosition(), geometry.getSize(), refreshRate);
    }

    public void setOpacity(float opacity) {
        glfwSetWindowOpacity(getHandle(), opacity);
    }

    public void setPosition(int x, int y) {
        glfwSetWindowPos(getHandle(), x, y);
    }

    public void setPosition(@NotNull Position<Integer> position) {
        setPosition(position.getX(), position.getY());
    }

    public void setPositionCallback(GLFWWindowPosCallbackI callback) {
        glfwSetWindowPosCallback(getHandle(), callback);
    }

    public void setRefreshCallback(GLFWWindowRefreshCallbackI callback) {
        glfwSetWindowRefreshCallback(getHandle(), callback);
    }

    public void setSize(int width, int height) {
        glfwSetWindowSize(getHandle(), width, height);
    }

    public void setSize(@NotNull Size<Integer> size) {
        setSize(size.getWidth(), size.getHeight());
    }

    public void setSizeCallback(GLFWWindowSizeCallbackI callback) {
        glfwSetWindowSizeCallback(getHandle(), callback);
    }

    public void setSizeLimits(int minWidth, int minHeight, int maxWidth, int maxHeight) {
        glfwSetWindowSizeLimits(getHandle(), maxWidth, maxHeight, maxWidth, maxHeight);
    }

    public void setSizeLimits(Size<Integer> minSize, Size<Integer> maxSize) {
        setSizeLimits(minSize.getWidth(), minSize.getHeight(), maxSize.getWidth(), maxSize.getHeight());
    }

    public void setTitle(CharSequence title) {
        glfwSetWindowTitle(getHandle(), title);
    }

    public void setTitle(ByteBuffer title) {
        glfwSetWindowTitle(getHandle(), title);
    }

    public void setUserPointer(long pointer) {
        glfwSetWindowUserPointer(getHandle(), pointer);
    }

    public void show() {
        glfwShowWindow(getHandle());
    }

    public void swapBuffers() {
        glfwSwapBuffers(getHandle());
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(getHandle());
    }

    public void setShouldClose(boolean shouldClose) {
        glfwSetWindowShouldClose(getHandle(), shouldClose);
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
        glfwDestroyWindow(getHandle());
        this.destroyed = true;
    }

    public void freeCallbacks() {
        glfwFreeCallbacks(getHandle());
    }

}
