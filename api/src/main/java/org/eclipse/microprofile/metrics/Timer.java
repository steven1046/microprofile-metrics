/*
 **********************************************************************
 * Copyright (c) 2017, 2022 Contributors to the Eclipse Foundation
 *               2010, 2013 Coda Hale, Yammer.com
 *
 * See the NOTICES file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 **********************************************************************/
package org.eclipse.microprofile.metrics;

import java.io.Closeable;
import java.time.Duration;
import java.util.concurrent.Callable;

/**
 * A timer metric which aggregates timing durations and provides duration statistics
 */
public interface Timer extends Metric, Sampling {
    /**
     * A timing context.
     *
     * @see Timer#time()
     */
    interface Context extends Closeable {

        /**
         * Updates the timer with the difference between current and start time. Call to this method will not reset the
         * start time. Multiple calls result in multiple updates.
         * 
         * @return the elapsed time in nanoseconds
         */
        long stop();

        /** Equivalent to calling {@link #stop()}. */
        @Override
        void close();
    }

    /**
     * Adds a recorded duration.
     *
     * @param duration
     *            the length of the {@link java.time.Duration duration}
     */
    void update(Duration duration);

    /**
     * Times and records the duration of event.
     *
     * @param event
     *            a {@link Callable} whose {@link Callable#call()} method implements a process whose duration should be
     *            timed
     * @param <T>
     *            the type of the value returned by {@code event}
     * @return the value returned by {@code event}
     * @throws Exception
     *             if {@code event} throws an {@link Exception}
     */
    <T> T time(Callable<T> event) throws Exception;

    /**
     * Times and records the duration of event.
     *
     * @param event
     *            a {@link Runnable} whose {@link Runnable#run()} method implements a process whose duration should be
     *            timed
     */
    void time(Runnable event);

    /**
     * Returns a new {@link Context}.
     *
     * @return a new {@link Context}
     * @see Context
     */
    Context time();

    /**
     * Returns the total elapsed timing durations of all completed timing events that are recorded with
     * {@link #update(Duration)}.
     * 
     * @return the elapsed time {@link java.time.Duration duration}
     */
    Duration getElapsedTime();

    long getCount();

    @Override
    Snapshot getSnapshot();
}
