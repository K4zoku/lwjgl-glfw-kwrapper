package com.github.k4zoku.kwrapper.lwjgl.glfw.monitor;

import com.github.k4zoku.kwrapper.lwjgl.common.geometry.Geometry;
import com.github.k4zoku.kwrapper.lwjgl.common.geometry.Position;
import com.github.k4zoku.kwrapper.lwjgl.common.geometry.Size;
import com.github.k4zoku.kwrapper.lwjgl.glfw.common.geometry.ContentScale;
import com.github.k4zoku.kwrapper.lwjgl.glfw.common.pointer.Pointer;
import com.github.k4zoku.kwrapper.lwjgl.glfw.window.Window;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGammaRamp;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Monitor extends Pointer {

    /**
     * Private constructor that initializes the monitor pointer.
     *
     * @param pointer the monitor pointer
     */
    private Monitor(long pointer) {
        super(pointer);
    }

    /**
     * Returns the monitor that the specified window is in full screen on.
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @param window the window to query
     *
     * @return the monitor, or {@code NULL} if the window is in windowed mode or an error occurred
     *
     * @since version 3.0
     */
    public static Monitor getWindowMonitor(Window window) {
        return new Monitor(glfwGetWindowMonitor(window.getPointer()));
    }

    /**
     * Returns the primary monitor. This is usually the monitor where elements like the task bar or global menu bar are located.
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * <p>The primary monitor is always first in the array returned by {@link #getMonitors GetMonitors}.</p>
     *
     * @return the primary monitor, or {@code NULL} if no monitors were found or if an error occurred
     *
     * @since version 3.0
     */
    @Nullable
    public static Monitor getPrimaryMonitor() {
        long monitor = glfwGetPrimaryMonitor();
        return monitor == NULL ? null : new Monitor(monitor);
    }

    /**
     * Returns an array of handles for all currently connected monitors. The primary monitor is always first in the returned array. If no monitors were found,
     * this function returns {@code NULL}.
     *
     * <p>The returned array is allocated and freed by GLFW. You should not free it yourself. It is guaranteed to be valid only until the monitor configuration
     * changes or the library is terminated.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @return an array of monitor handlers, or an empty array if no monitors were found or if an error occurred
     *
     * @since version 3.0
     */
    public static Monitor[] getMonitors() {
        PointerBuffer glfwMonitors = glfwGetMonitors();
        int size = glfwMonitors == null ? 0 : glfwMonitors.limit();
        Monitor[] monitors = new Monitor[size];
        for (int i = 0; i < size; i++) {
            monitors[i] = new Monitor(glfwMonitors.get(i));
        }
        return monitors;
    }

    /**
     * Returns the current gamma ramp of the specified monitor.
     *
     * <p>The returned structure and its arrays are allocated and freed by GLFW. You should not free them yourself. They are valid until the specified monitor is
     * disconnected, this function is called again for that monitor or the library is terminated.</p>
     *
     * <p>Notes:</p>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li><b>Wayland</b>: Gamma handling is a privileged protocol, this function will thus never be implemented and emits {@link GLFW#GLFW_PLATFORM_ERROR PLATFORM_ERROR} while returning
     * {@code NULL}.</li>
     * </ul>
     *
     * @return the current gamma ramp, or {@code NULL} if an error occurred
     *
     * @since version 3.0
     */
    @Nullable
    public GLFWGammaRamp getGammaRamp() {
        return glfwGetGammaRamp(getPointer());
    }

    /**
     * Retrieves the content scale for the specified monitor.
     *
     * <p>This function retrieves the content scale for the specified monitor. The content scale is the ratio between the current DPI and the platform's default
     * DPI. This is especially important for text and any UI elements. If the pixel dimensions of your UI scaled by this look appropriate on your machine then
     * it should appear at a reasonable size on other machines regardless of their DPI and scaling settings. This relies on the system DPI and scaling
     * settings being somewhat correct.</p>
     *
     * <p>The content scale may depend on both the monitor resolution and pixel density and on user settings. It may be very different from the raw DPI
     * calculated from the physical size and current resolution.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @return {@link ContentScale} object containing the content scale for the specified monitor
     *
     * @since version 3.3
     */
    public ContentScale getContentScale() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer xscale = stack.mallocFloat(1);
            FloatBuffer yscale = stack.mallocFloat(1);
            glfwGetMonitorContentScale(getPointer(), xscale, yscale);
            return new ContentScale(xscale.get(), yscale.get());
        }
    }

    /**
     * Returns a human-readable name, encoded as UTF-8, of the specified monitor. The name typically reflects the make and model of the monitor and is not
     * guaranteed to be unique among the connected monitors.
     *
     * <p>The returned string is allocated and freed by GLFW. You should not free it yourself. It is valid until the specified monitor is disconnected or the
     * library is terminated.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @return the UTF-8 encoded name of the monitor, or {@code NULL} if an error occurred
     *
     * @since version 3.0
     */
    @Nullable
    public String getName() {
        return glfwGetMonitorName(getPointer());
    }

    /**
     * Returns the size, in millimetres, of the display area of the specified monitor.
     *
     * <p>Some platforms do not provide accurate monitor size information, either because the monitor
     * <a target="_blank" href="https://en.wikipedia.org/wiki/Extended_display_identification_data">EDID</a> data is incorrect or because the driver does not report it
     * accurately.</p>
     *
     * <p>Any or all of the size arguments may be {@code NULL}. If an error occurs, all non-{@code NULL} size arguments will be set to zero.</p>
     *
     * <div style="margin-left: 26px; border-left: 1px solid gray; padding-left: 14px;"><h5>Note</h5>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li><b>Windows</b>: On Windows 8 and earlier the physical size is calculated from the current resolution and system DPI instead of querying the monitor
     * EDID data.</li>
     * </ul></div>
     *
     * @return {@link Size<Integer>} object containing the size, in millimetres, of the display area of the specified monitor
     *
     * @since version 3.0
     */
    public Size<Integer> getPhysicalSize() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            glfwGetMonitorPhysicalSize(getPointer(), width, height);
            return new Size<>(width.get(), height.get());
        }
    }

    /**
     * Returns the position, in screen coordinates, of the upper-left corner of the specified monitor.
     *
     * <p>Any or all of the position arguments may be {@code NULL}. If an error occurs, all non-{@code NULL} position arguments will be set to zero.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @return {@link Position<Integer>} object containing the position, in screen coordinates, of the upper-left corner of the specified monitor
     *
     * @since version 3.0
     */
    public Position<Integer> getPosition() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer x = stack.mallocInt(1);
            IntBuffer y = stack.mallocInt(1);
            glfwGetMonitorPos(getPointer(), x, y);
            return new Position<>(x.get(), y.get());
        }
    }

    /**
     * Retrieves the work area of the monitor.
     *
     * <p>This function returns the position, in screen coordinates, of the upper-left corner of the work area of the specified monitor along with the work area
     * size in screen coordinates. The work area is defined as the area of the monitor not occluded by the window system task bar where present. If no task
     * bar exists then the work area is the monitor resolution in screen coordinates.</p>
     *
     * <p>Any or all of the position and size arguments may be {@code NULL}.  If an error occurs, all non-{@code NULL} position and size arguments will be set to zero.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @return {@link Geometry<Integer>} object containing the working area of the specified monitor
     *
     * @since version 3.3
     */
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

    /**
     * Returns the current video mode of the specified monitor. If you have created a full screen window for that monitor, the return value will depend on
     * whether that window is iconified.
     *
     * <p>The returned array is allocated and freed by GLFW. You should not free it yourself. It is valid until the specified monitor is disconnected or the
     * library is terminated.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @return the current mode of the monitor, or {@code NULL} if an error occurred
     *
     * @since version 3.0
     */
    @Nullable
    public GLFWVidMode getVideoMode() {
        return glfwGetVideoMode(getPointer());
    }

    /**
     * Returns an array of all video modes supported by the specified monitor.
     *
     * <p>The returned array is sorted in ascending order, first by color bit depth (the sum of all channel depths), then by resolution area (the product of
     * width and height), then resolution width and finally by refresh rate.</p>
     *
     * <p>The returned array is allocated and freed by GLFW. You should not free it yourself. It is valid until the specified monitor is disconnected, this
     * function is called again for that monitor or the library is terminated.</p>
     *
     * <p>This function must only be called from the main thread.</p>
     *
     * @return an array of video modes, or {@code NULL} if an error occurred
     *
     * @since version 1.0
     */
    @Nullable
    public GLFWVidMode.Buffer getVideoModes() {
        return glfwGetVideoModes(getPointer());
    }

    /**
     * Generates a gamma ramp and sets it for the specified monitor.
     *
     * <p>This function generates an appropriately sized gamma ramp from the specified exponent and then calls {@link #setGammaRamp  SetGammaRamp} with it. The value must be a
     * finite number greater than zero.</p>
     *
     * <p>The software controlled gamma ramp is applied <em>in addition</em> to the hardware gamma correction, which today is usually an approximation of sRGB
     * gamma. This means that setting a perfectly linear ramp, or gamma 1.0, will produce the default (usually sRGB-like) behavior.</p>
     *
     * <p>For gamma correct rendering with OpenGL or OpenGL ES, see the {@link GLFW#GLFW_SRGB_CAPABLE SRGB_CAPABLE} hint.</p>
     *
     * <p>Notes:</p>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li><b>Wayland</b>: Gamma handling is a privileged protocol, this function will thus never be implemented and emits {@link GLFW#GLFW_PLATFORM_ERROR PLATFORM_ERROR}.</li>
     * </ul>
     *
     * @param gamma   the desired exponent
     *
     * @since version 3.0
     */
    public void setGamma(float gamma) {
        glfwSetGamma(getPointer(), gamma);
    }

    /**
     * Sets the current gamma ramp for the specified monitor.
     *
     * <p>This function sets the current gamma ramp for the specified monitor. The original gamma ramp for that monitor is saved by GLFW the first time this
     * function is called and is restored by {@link GLFW#glfwTerminate Terminate}.</p>
     *
     * <p>The software controlled gamma ramp is applied <em>in addition</em> to the hardware gamma correction, which today is usually an approximation of sRGB
     * gamma. This means that setting a perfectly linear ramp, or gamma 1.0, will produce the default (usually sRGB-like) behavior.</p>
     *
     * <p>For gamma correct rendering with OpenGL or OpenGL ES, see the {@link GLFW#GLFW_SRGB_CAPABLE SRGB_CAPABLE} hint.</p>
     *
     * <div style="margin-left: 26px; border-left: 1px solid gray; padding-left: 14px;"><h5>Note</h5>
     *
     * <ul>
     * <li>This function must only be called from the main thread.</li>
     * <li>The size of the specified gamma ramp should match the size of the current ramp for that monitor.</li>
     * <li><b>Windows</b>: The gamma ramp size must be 256.</li>
     * <li><b>Wayland</b>: Gamma handling is a privileged protocol, this function will thus never be implemented and emits {@link GLFW#GLFW_PLATFORM_ERROR PLATFORM_ERROR}.</li>
     * <li>The specified gamma ramp is copied before this function returns.</li>
     * </ul></div>
     *
     * @param ramp    the gamma ramp to use
     *
     * @since version 3.0
     */
    public void setGammaRamp(GLFWGammaRamp ramp) {
        glfwSetGammaRamp(getPointer(), ramp);
    }

    /**
     * Sets the user pointer of the specified monitor.
     *
     * <p>This function sets the user-defined pointer of the specified monitor. The current value is retained until the monitor is disconnected. The initial
     * value is {@code NULL}.</p>
     *
     * <p>This function may be called from the monitor callback, even for a monitor that is being disconnected.</p>
     *
     * <p>This function may be called from any thread. Access is not synchronized.</p>
     *
     * @param pointer the new value
     *
     * @since version 3.3
     */
    public void setUserPointer(long pointer) {
        glfwSetMonitorUserPointer(getPointer(), pointer);
    }

}
