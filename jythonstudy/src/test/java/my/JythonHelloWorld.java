package my;

import org.python.util.PythonInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class JythonHelloWorld {
    public static final Logger LOG = LoggerFactory.getLogger(JythonHelloWorld.class);

    public static class RequestContext {
        Reader r;
        Writer w;
        App app;
        long start = System.currentTimeMillis();

        public RequestContext(App app, Reader r, Writer w) {
            this.app = app;
            this.r = r;
            this.w = w;
        }

        public long getStart() {
            return this.start;
        }

        public void log(String msg) {
            LOG.debug("debug:{}", msg);
            LOG.info("INFO:{}", msg);
        }

        public char read() throws IOException {
            return (char) r.read();
        }

        public void write(char c) throws IOException {
            w.write(c);
        }

        public App getApp() {
            return app;
        }
    }

    public static class App {
        private String value = "HI";

        public String get() {
            return value;
        }

        public void set(String value) {
            this.value = value;
        }

        public void process(Reader r, Writer w) throws Exception {

            RequestContext ctx = new RequestContext(this, r, w);
            long time = ctx.getStart();

            System.out.println("beforeNew:" + (System.currentTimeMillis() - time));

            PythonInterpreter pyInterp = new PythonInterpreter();//.threadLocalStateInterpreter(null);
            System.out.println("After New Interpreter:" + (System.currentTimeMillis() - time));

            try {
                pyInterp.cleanup();
                pyInterp.set("request", ctx);
                InputStream is = JythonHelloWorld.class.getClassLoader()
                        .getResourceAsStream("my.py");
                System.out.println("beforeExecuteFile:" + (System.currentTimeMillis() - time));

                pyInterp.execfile(is);
                System.out.println("afterExecuteFile:" + (System.currentTimeMillis() - time));

            } finally {
                System.out.println("beforeCleanUp:" + (System.currentTimeMillis() - time));
                pyInterp.cleanup();

                System.out.println(":" + (System.currentTimeMillis() - time));

            }

        }


    }

    public static void main(String[] args) throws Exception {
        App app = new App();
        for (int i = 0; i < 100; i++) {

            Reader r = new StringReader("hello");
            Writer w = new StringWriter();
            app.process(r, w);
            System.out.println(w.toString());
        }


    }
}