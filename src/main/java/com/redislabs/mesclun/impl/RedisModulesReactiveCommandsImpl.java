package com.redislabs.mesclun.impl;

import com.redislabs.mesclun.StatefulRedisModulesConnection;
import com.redislabs.mesclun.RedisModulesReactiveCommands;
import com.redislabs.mesclun.gears.*;
import com.redislabs.mesclun.gears.output.ExecutionResults;
import com.redislabs.mesclun.timeseries.Aggregation;
import com.redislabs.mesclun.timeseries.CreateOptions;
import com.redislabs.mesclun.timeseries.Label;
import com.redislabs.mesclun.timeseries.RedisTimeSeriesCommandBuilder;
import io.lettuce.core.RedisReactiveCommandsImpl;
import io.lettuce.core.codec.RedisCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@SuppressWarnings("unchecked")
public class RedisModulesReactiveCommandsImpl<K, V> extends RedisReactiveCommandsImpl<K, V> implements RedisModulesReactiveCommands<K, V> {

    private final StatefulRedisModulesConnection<K, V> connection;
    private final RedisTimeSeriesCommandBuilder<K, V> timeSeriesCommandBuilder;
    private final RedisGearsCommandBuilder<K, V> gearsCommandBuilder;

    public RedisModulesReactiveCommandsImpl(StatefulRedisModulesConnection<K, V> connection, RedisCodec<K, V> codec) {
        super(connection, codec);
        this.connection = connection;
        this.gearsCommandBuilder = new RedisGearsCommandBuilder<>(codec);
        this.timeSeriesCommandBuilder = new RedisTimeSeriesCommandBuilder<>(codec);
    }

    @Override
    public StatefulRedisModulesConnection<K, V> getStatefulConnection() {
        return connection;
    }

    @Override
    public Mono<ExecutionResults> pyExecute(String function, V... requirements) {
        return createMono(() -> gearsCommandBuilder.pyExecute(function, requirements));
    }

    @Override
    public Mono<String> pyExecuteUnblocking(String function, V... requirements) {
        return createMono(() -> gearsCommandBuilder.pyExecuteUnblocking(function, requirements));
    }

    @Override
    public Flux<Object> trigger(String trigger, V... args) {
        return createDissolvingFlux(() -> gearsCommandBuilder.trigger(trigger, args));
    }

    @Override
    public Mono<String> unregister(String id) {
        return createMono(() -> gearsCommandBuilder.unregister(id));
    }

    @Override
    public Mono<String> abortExecution(String id) {
        return createMono(() -> gearsCommandBuilder.abortExecution(id));
    }

    @Override
    public Flux<V> configGet(K... keys) {
        return createDissolvingFlux(() -> gearsCommandBuilder.configGet(keys));
    }

    @Override
    public Flux<V> configSet(Map<K, V> map) {
        return createDissolvingFlux(() -> gearsCommandBuilder.configSet(map));
    }

    @Override
    public Mono<String> dropExecution(String id) {
        return createMono(() -> gearsCommandBuilder.dropExecution(id));
    }

    @Override
    public Flux<Execution> dumpExecutions() {
        return createDissolvingFlux(gearsCommandBuilder::dumpExecutions);
    }

    @Override
    public Flux<Registration> dumpRegistrations() {
        return createDissolvingFlux(gearsCommandBuilder::dumpRegistrations);
    }

    @Override
    public Mono<ExecutionDetails> getExecution(String id) {
        return createMono(() -> gearsCommandBuilder.getExecution(id));
    }

    @Override
    public Mono<ExecutionDetails> getExecution(String id, ExecutionMode mode) {
        return createMono(() -> gearsCommandBuilder.getExecution(id, mode));
    }

    @Override
    public Mono<ExecutionResults> getResults(String id) {
        return createMono(() -> gearsCommandBuilder.getResults(id));
    }

    @Override
    public Mono<ExecutionResults> getResultsBlocking(String id) {
        return createMono(() -> gearsCommandBuilder.getResultsBlocking(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Mono<String> create(K key, CreateOptions options, Label<K, V>... labels) {
        return createMono(() -> timeSeriesCommandBuilder.create(key, options, labels));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Mono<String> create(K key, Label<K, V>... labels) {
        return createMono(() -> timeSeriesCommandBuilder.create(key, null, labels));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Mono<Long> add(K key, long timestamp, double value, CreateOptions options, Label<K, V>... labels) {
        return createMono(() -> timeSeriesCommandBuilder.add(key, timestamp, value, options, labels));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Mono<Long> add(K key, long timestamp, double value, Label<K, V>... labels) {
        return createMono(() -> timeSeriesCommandBuilder.add(key, timestamp, value, null, labels));
    }

    @Override
    public Mono<String> createRule(K sourceKey, K destKey, Aggregation aggregationType, long timeBucket) {
        return createMono(() -> timeSeriesCommandBuilder.createRule(sourceKey, destKey, aggregationType, timeBucket));
    }

    @Override
    public Mono<String> deleteRule(K sourceKey, K destKey) {
        return createMono(() -> timeSeriesCommandBuilder.deleteRule(sourceKey, destKey));
    }

}