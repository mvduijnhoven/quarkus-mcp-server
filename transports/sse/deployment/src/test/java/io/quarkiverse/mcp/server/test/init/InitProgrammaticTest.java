package io.quarkiverse.mcp.server.test.init;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkiverse.mcp.server.ClientCapability;
import io.quarkiverse.mcp.server.Notification.Type;
import io.quarkiverse.mcp.server.NotificationManager;
import io.quarkiverse.mcp.server.test.McpAssured;
import io.quarkiverse.mcp.server.test.McpServerTest;
import io.quarkus.runtime.Startup;
import io.quarkus.test.QuarkusUnitTest;

public class InitProgrammaticTest extends McpServerTest {

    @RegisterExtension
    static final QuarkusUnitTest config = defaultConfig()
            .withApplicationRoot(root -> root.addClass(MyTools.class));

    @Inject
    MyTools myTools;

    @Test
    public void testInitRequest() throws InterruptedException {
        McpAssured.newSseClient()
                .setClientCapabilities(new ClientCapability(ClientCapability.SAMPLING, Map.of()))
                .build()
                .connect();
        assertTrue(MyTools.INIT_LATCH.await(5, TimeUnit.SECONDS));
        assertTrue(myTools.initCalled.get());
    }

    @Singleton
    public static class MyTools {

        static final CountDownLatch INIT_LATCH = new CountDownLatch(1);
        AtomicBoolean initCalled = new AtomicBoolean();

        @Inject
        NotificationManager notificationManager;

        @Startup
        void starup() {
            notificationManager.newNotification("foo")
                    .setType(Type.INITIALIZED)
                    .setHandler(args -> {
                        if (args.connection().initialRequest().supportsSampling()) {
                            initCalled.set(true);
                        }
                        INIT_LATCH.countDown();
                        return null;
                    }).register();
        }

    }

}
