package outputCapturer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class OutputCapturer {
    private PrintStream origOut;

    private ByteArrayOutputStream outputStream;

    public void start()
    {
        this.origOut = System.out;
        this.outputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(this.outputStream);
        System.setOut(ps);
    }

    public String getOutput() {
        System.out.flush();
        return this.outputStream.toString().replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
    }

    public void stop() {
        System.setOut(this.origOut);
    }
}