package com.han.towersofivory.network.businesslayer.threading;

import com.han.towersofivory.network.businesslayer.threading.operation.IOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Manages a thread pool and executes operations on it.
 */
public class ThreadManager {
    private static final Logger LOGGER = LogManager.getLogger(ThreadManager.class);
    private final ScheduledExecutorService executorService;
    private final Map<IOperation, Thread> threads;
    private final Map<IOperation, ScheduledFuture<?>> scheduledTasks;

    /**
     * Creates a new thread manager with the specified number of threads.
     *
     * @param numberOfThreads The number of threads to create.
     */
    public ThreadManager(int numberOfThreads) {
        this.threads = new ConcurrentHashMap<>();
        this.scheduledTasks = new ConcurrentHashMap<>();
        this.executorService = Executors.newScheduledThreadPool(numberOfThreads);
    }

    /**
     * Executes the specified operation on the thread pool.
     *
     * @param operation The operation to execute.
     */
    public void runOperation(IOperation operation) {
        executorService.execute(() -> executeOperation(operation));
    }

    /**
     * Schedules the specified operation to run after a given delay.
     *
     * @param operation The operation to execute.
     */
    public void runScheduledOperation(IOperation operation) {
        ScheduledFuture<?> scheduledTask = executorService.scheduleAtFixedRate(() -> executeOperation(operation),
                0, operation.getTimeout(), TimeUnit.MILLISECONDS);

        scheduledTasks.put(operation, scheduledTask);
    }

    /**
     * Interrupts the specified operation on the thread pool.
     *
     * @param operation The operation to interrupt.
     */
    public void stopOperation(IOperation operation) {
        if (operation != null) {
            operation.interrupt();
        }

        Thread thread = threads.get(operation);
        if (thread != null) {
            thread.interrupt();
            threads.remove(operation);
        }

        ScheduledFuture<?> scheduledTask = scheduledTasks.get(operation);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            scheduledTasks.remove(operation);
        }
    }

    /**
     * Shuts down the thread pool.
     */
    public void shutdown() {
        executorService.shutdown();
        threads.keySet().forEach(this::stopOperation);
        try {
            // Wait a while for existing tasks to terminate.
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                // Wait a while for tasks to respond to being canceled.
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    LOGGER.error("Pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Executes the specified operation in the current thread.
     *
     * @param operation The operation to execute.
     */
    private void executeOperation(IOperation operation) {
        Thread currentThread = Thread.currentThread();
        try {
            threads.put(operation, currentThread);
            operation.execute();
        } catch (Exception e) {
            LOGGER.info("An exception occurred in thread {}", currentThread.getName(), e);
        }
    }
}
