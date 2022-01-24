package com.github.k4zoku.kwrapper.lwjgl.glfw.monitor;

import com.github.k4zoku.kwrapper.lwjgl.common.geometry.Geometry;
import com.github.k4zoku.kwrapper.lwjgl.common.geometry.Position;
import com.github.k4zoku.kwrapper.lwjgl.common.geometry.Size;
import com.github.k4zoku.kwrapper.lwjgl.glfw.common.geometry.ContentScale;
import com.github.k4zoku.kwrapper.lwjgl.glfw.common.pointer.Pointer;
import com.github.k4zoku.kwrapper.lwjgl.glfw.window.Window;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWGammaRamp;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Monitor extends Pointer {

    private Monitor(long handle) {
        super(handle);
    }

    public static Monitor getWindowMonitor(Window window) {
        return new Monitor(glfwGetWindowMonitor(window.getPointer()));
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
        return glfwGetGammaRamp(getPointer());
    }

    public ContentScale getContentScale() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer xscale = stack.mallocFloat(1);
            FloatBuffer yscale = stack.mallocFloat(1);
            glfwGetMonitorContentScale(getPointer(), xscale, yscale);
            return new ContentScale(xscale.get(), yscale.get());
        }
    }

    public String getName() {
        return glfwGetMonitorName(getPointer());
    }

    public Size<Integer> getPhysicalSize() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            glfwGetMonitorPhysicalSize(getPointer(), width, height);
            return new Size<>(width.get(), height.get());
        }
    }

    public Position<Integer> getPosition() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer x = stack.mallocInt(1);
            IntBuffer y = stack.mallocInt(1);
            glfwGetMonitorPos(getPointer(), x, y);
            return new Position<>(x.get(), y.get());
        }
    }

    public Geometry<Integer> getWorkArea() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer x = stack.mallocInt(1);
            IntBuffer y = stack.mallocInt(1);
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            glfwGetMonitorWorkarea(getPointer(), x, y, width, height);
            return new Geometry<>(x.get(), y.get(), width.get(), height.get());
        }
    }

    public GLFWVidMode getVideoMode() {
        return glfwGetVideoMode(getPointer());
    }

    public GLFWVidMode.Buffer getVideoModes() {
        return glfwGetVideoModes(getPointer());
    }

    public void setGamma(float gamma) {
        glfwSetGamma(getPointer(), gamma);
    }

    public void setGammaRamp(GLFWGammaRamp ramp) {
        glfwSetGammaRamp(getPointer(), ramp);
    }

    public void setUserPointer(long pointer) {
        glfwSetMonitorUserPointer(getPointer(), pointer);
    }

}
