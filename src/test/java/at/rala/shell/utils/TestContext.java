package at.rala.shell.utils;

import at.rala.shell.Context;
import at.rala.shell.command.Command;
import at.rala.shell.utils.io.CacheOutputStream;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;

@SuppressWarnings({"unused", "WeakerAccess"})
public class TestContext extends Context {
    private final CacheOutputStream cacheOutputStream;
    private final CacheOutputStream cacheErrorStream;

    public TestContext() {
        this(new CacheOutputStream());
    }

    public TestContext(Map<String, Command> commands) {
        this(new CacheOutputStream(), commands);
    }

    public TestContext(CacheOutputStream outputStream) {
        this(outputStream, outputStream);
    }

    public TestContext(CacheOutputStream outputStream, Map<String, Command> commands) {
        this(
            outputStream,
            outputStream,
            commands
        );
    }

    public TestContext(CacheOutputStream outputStream, CacheOutputStream errorStream) {
        this(outputStream, errorStream, null);
    }

    public TestContext(CacheOutputStream outputStream, CacheOutputStream errorStream,
                       Map<String, Command> commands) {
        super(
            new PrintWriter(outputStream, true),
            new PrintWriter(errorStream, true),
            commands != null ? commands : Collections.emptyMap()
        );
        this.cacheOutputStream = outputStream;
        this.cacheErrorStream = errorStream;
    }

    public CacheOutputStream getCacheOutputStream() {
        return cacheOutputStream;
    }

    public CacheOutputStream getCacheErrorStream() {
        return cacheErrorStream;
    }
}
