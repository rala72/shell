package at.rala.shell.utils;

import at.rala.shell.Context;
import at.rala.shell.command.Command;
import at.rala.shell.utils.io.CacheOutputStream;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;

@SuppressWarnings("unused")
public class TestContext extends Context {
    private final CacheOutputStream cacheOutputStream;

    public TestContext() {
        this(null);
    }

    public TestContext(Map<String, Command> commands) {
        this(new CacheOutputStream(), commands);
    }

    private TestContext(CacheOutputStream cacheOutputStream, Map<String, Command> commands) {
        super(
            new PrintWriter(cacheOutputStream, true),
            commands != null ? commands : Collections.emptyMap()
        );
        this.cacheOutputStream = cacheOutputStream;
    }

    public CacheOutputStream getCacheOutputStream() {
        return cacheOutputStream;
    }

    @Override
    public String toString() {
        return super.toString() + "\t" + cacheOutputStream;
    }
}
