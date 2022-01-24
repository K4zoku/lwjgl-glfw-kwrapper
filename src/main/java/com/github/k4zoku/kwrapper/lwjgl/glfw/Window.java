package com.github.k4zoku.kwrapper.lwjgl.glfw;

import com.github.k4zoku.kwrapper.lwjgl.common.Destroyable;
import com.github.k4zoku.kwrapper.lwjgl.common.Geometry;
import com.github.k4zoku.kwrapper.lwjgl.common.Position;
import com.github.k4zoku.kwrapper.lwjgl.common.Size;
import com.github.k4zoku.kwrapper.lwjgl.glfw.exception.GLFWRuntimeException;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window extends PointerHandle implements Destroyable {

    private boolean destroyed;

    public Window(int windowWidth, int windowHeight, CharSequence windowTitle, long monitor, long share) {
        this(glfwCreateWindow(windowWidth, windowHeight, windowTitle, monitor, share), false);
    }

    private Window(long handle, boolean destroyed) {
        super(handle);
        this.destroyed = destroyed;
        if (getHandle() == NULL) {
            throw new GLFWRuntimeException("Failed to create the GLFW window");
        }
    }

    public void setTitle(CharSequence title) {
        glfwSetWindowTitle(getHandle(), title);
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
        return new Monitor(glfwGetWindowMonitor(getHandle()));
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

    public void setPosition(@NotNull Position<Integer> position) {
        setPosition(position.getX(), position.getY());
    }

    public void setPosition(int x, int y) {
        glfwSetWindowPos(getHandle(), x, y);
    }

    public Size<Integer> getSize() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            glfwGetWindowSize(getHandle(), width, height);
            return new Size<>(width.get(), height.get());
        }
    }

    public void setSize(@NotNull Size<Integer> size) {
        setSize(size.getWidth(), size.getHeight());
    }

    public void setSize(int width, int height) {
        glfwSetWindowSize(getHandle(), width, height);
    }

    public Geometry<Integer> getGeometry() {
        Size<Integer> size = getSize();
        Position<Integer> position = getPosition();
        return new Geometry<>(position, size);
    }

    public void setGeometry(@NotNull Geometry<Integer> geometry) {
        setGeometry(geometry.getX(), geometry.getY(), geometry.getWidth(), geometry.getHeight());
    }

    public void setGeometry(int x, int y, int width, int height) {
        setPosition(x, y);
        setSize(width, height);
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

    public void setMouseButtonCallback(GLFWMouseButtonCallbackI callback) {
        glfwSetMouseButtonCallback(getHandle(), callback);
    }

    public void setScrollCallback(GLFWScrollCallbackI callback) {
        glfwSetScrollCallback(getHandle(), callback);
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
}
