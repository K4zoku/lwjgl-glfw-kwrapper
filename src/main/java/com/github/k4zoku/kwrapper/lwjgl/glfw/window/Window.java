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
import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window extends Pointer implements Destroyable {

    private boolean destroyed;

    private Window(long handle, boolean destroyed) {
        super(handle);
        this.destroyed = destroyed;
        if (getPointer() == NULL) {
            throw new GLFWRuntimeException("Failed to create the GLFW window");
        }
    }

    public Window(int windowWidth, int windowHeight, CharSequence windowTitle, long monitor, long share) {
        this(glfwCreateWindow(windowWidth, windowHeight, windowTitle, monitor, share), false);
    }

    public Window(int windowWidth, int windowHeight, CharSequence windowTitle, @Nullable Monitor monitor, long share) {
        this(windowWidth, windowHeight, windowTitle, monitor == null ? NULL : monitor.getPointer(), share);
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
        this(glfwCreateWindow(windowWidth, windowHeight, windowTitle, monitor != null ? monitor.getPointer() : NULL, share), false);
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
        glfwSetWindowIcon(getPointer(), icon);
    }

    public void focus() {
        glfwFocusWindow(getPointer());
    }

    public String getClipboardString() {
        return glfwGetClipboardString(getPointer());
    }

    public void setClipboardString(CharSequence string) {
        glfwSetClipboardString(getPointer(), string);
    }

    public Position<Double> getCursorPosition() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            DoubleBuffer x = stack.mallocDouble(1);
            DoubleBuffer y = stack.mallocDouble(1);
            glfwGetCursorPos(getPointer(), x, y);
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

    public void setCursorPosition(int x, int y) {
        glfwSetCursorPos(getPointer(), x, y);
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
