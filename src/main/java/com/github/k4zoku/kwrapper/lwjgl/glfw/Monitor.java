package com.github.k4zoku.kwrapper.lwjgl.glfw;

import com.github.k4zoku.kwrapper.lwjgl.common.Geometry;
import com.github.k4zoku.kwrapper.lwjgl.common.Position;
import com.github.k4zoku.kwrapper.lwjgl.common.Size;
import com.github.k4zoku.kwrapper.lwjgl.glfw.window.Window;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWGammaRamp;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Monitor extends PointerHandle {

    private Monitor(long handle) {
        super(handle);
    }

    public static Monitor getWindowMonitor(Window window) {
        return new Monitor(glfwGetWindowMonitor(window.getHandle()));
    }

    public static Monitor getPrimaryMonitor() {
        return new Monitor(glfwGetPrimaryMonitor());
    }

    public static Monitor[] getMonitors() {
        PointerBuffer glfwMonitors = glfwGetMonitors();
        assert glfwMonitors != null;
        Monitor[] monitors = new Monitor[glfwMonitors.limit()];
        for (int i = 0; i < glfwMonitors.limit(); i++) {
            monitors[i] = new Monitor(glfwMonitors.get(i));
        }
        return monitors;
    }

    public GLFWGammaRamp getGammaRamp() {
        return glfwGetGammaRamp(getHandle());
    }

    public ContentScale getContentScale() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer xscale = stack.mallocFloat(1);
            FloatBuffer yscale = stack.mallocFloat(1);
            glfwGetMonitorContentScale(getHandle(), xscale, yscale);
            return new ContentScale(xscale.get(), yscale.get());
        }
    }

    public String getName() {
        return glfwGetMonitorName(getHandle());
    }

    public Size<Integer> getPhysicalSize() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            glfwGetMonitorPhysicalSize(getHandle(), width, height);
            return new Size<>(width.get(), height.get());
        }
    }

    public Position<Integer> getPosition() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer x = stack.mallocInt(1);
            IntBuffer y = stack.mallocInt(1);
            glfwGetMonitorPos(getHandle(), x, y);
            return new Position<>(x.get(), y.get());
        }
    }

    public Geometry<Integer> getWorkArea() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer x = stack.mallocInt(1);
            IntBuffer y = stack.mallocInt(1);
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            glfwGetMonitorWorkarea(getHandle(), x, y, width, height);
            return new Geometry<>(x.get(), y.get(), width.get(), height.get());
        }
    }

    public GLFWVidMode getVideoMode() {
        return glfwGetVideoMode(getHandle());
    }

    public GLFWVidMode.Buffer getVideoModes() {
        return glfwGetVideoModes(getHandle());
    }

    public void setGamma(float gamma) {
        glfwSetGamma(getHandle(), gamma);
    }

    public void setGammaRamp(GLFWGammaRamp ramp) {
        glfwSetGammaRamp(getHandle(), ramp);
    }

    public void setUserPointer(long pointer) {
        glfwSetMonitorUserPointer(getHandle(), pointer);
    }

}
