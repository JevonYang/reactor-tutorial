package com.yang.tutorial.contrast;

import com.yang.tutorial.model.Scope;
import com.yang.tutorial.service.PersistentScopeService;
import com.yang.tutorial.service.ThreadPoolService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class Contrast {

    private static Scope dataScope;

    private static Scope inputScope;

    private static PersistentScopeService service;

    private ExecutorService executorService = ThreadPoolService.getInstance("es-pool-%d");

    static {

        String[] listData = new String[]{"dataSubId1", "dataSubId2", "dataSubId3", "dataSubId4", "dataSubId5", "dataSubId6", "dataSubId7", "dataSubId8", "dataSubId9", "dataSubId10"};
        dataScope = new Scope("newId", Arrays.asList(listData));

        String[] list1 = new String[]{"newSubId1", "dataSubId2", "newSubId3", "dataSubId4", "newSubId5", "newSubId6", "newSubId7", "newSubId8", "newSubId9", "newSubId10", "newSubId11"};
        inputScope = new Scope("inputId", Arrays.asList(list1));

        service = new PersistentScopeService(dataScope);
    }


    @Test
    public void origin() throws InterruptedException {
        long t = System.nanoTime();

        // 输入的内容
        Set<String> newRelation = inputScope.getSubId().stream()
                .map(s -> s + "")
                .collect(Collectors.toSet());

        // 模拟数据库操作，耗时操作
        // 原来的内容
        Set<String> oldRelation = service.queryScope().getSubId().stream()
                .map(s -> s + "")
                .collect(Collectors.toSet());

        // 输入没有，而原来有的部分需要删掉
        Set<String> needDeleted = new HashSet<>();
        needDeleted.addAll(newRelation);
        needDeleted.addAll(oldRelation);
        needDeleted.removeAll(newRelation);

        // 原来有，而输入没有的部分需要更新
        Set<String> needUpdate = new HashSet<>();
        needUpdate.addAll(newRelation);
        needUpdate.addAll(oldRelation);
        needUpdate.removeAll(oldRelation);

        // 更新差异
        if (!needDeleted.isEmpty()) {
            needDeleted.forEach(s -> {
                try {
                    service.delete(s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        // 更新差异
        if (!needUpdate.isEmpty()) {
            needUpdate.forEach(s -> {
                try {
                    service.update(s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        t = (System.nanoTime() - t) / 1000000;
        log.info("origin操作时间：{}ms", t);
    }

    @Test
    public void streamStyle() {
        long t = System.nanoTime();

        t = (System.nanoTime() - t) / 1000000;
        log.info("streamStyle操作时间：{}ms", t);
    }

    @Test
    public void completableFutureStyle() throws InterruptedException, ExecutionException {
        long t = System.nanoTime();
        CompletableFuture<Set<String>> oldRelationFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return service.queryScope().getSubId().stream()
                        .map(s -> s + "")
                        .collect(Collectors.toSet());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }, executorService);

        CompletableFuture<Set<String>> newRelationFuture = CompletableFuture.supplyAsync(() -> inputScope.getSubId().stream()
                .map(s -> s + "")
                .collect(Collectors.toSet()), executorService);

        Set<String> oldRelation = oldRelationFuture.get();
        Set<String> newRelation = newRelationFuture.get();

        Set<String> needDeleted = new HashSet<>();
        needDeleted.addAll(newRelation);
        needDeleted.addAll(oldRelation);
        needDeleted.removeAll(newRelation);

        Stream<CompletableFuture<String>> needDeletedSet = needDeleted.stream()
                .map(CompletableFuture::completedFuture);

        Set<String> needUpdate = new HashSet<>();
        needUpdate.addAll(newRelation);
        needUpdate.addAll(oldRelation);
        needUpdate.removeAll(oldRelation);

        Stream<CompletableFuture<String>> needUpdateSet = needUpdate.stream()
                .map(CompletableFuture::completedFuture);


//        Set<Boolean> set0 =
                needDeletedSet.map(delItem -> delItem.thenApplyAsync(item -> {
                    try {
                        service.delete(item);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }))
                .map(CompletableFuture::join)
                .collect(Collectors.toSet());


//        Set<Boolean> set1 =
                needUpdateSet.map(updateItem -> updateItem.thenApplyAsync(item -> {
                    try {
                        service.update(item);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }))
                .map(CompletableFuture::join)
                .collect(Collectors.toSet());

//        System.out.println(set0);
//        System.out.println(set1);

        t = (System.nanoTime() - t) / 1000000;
        log.info("completableFutureStyle操作时间：{}ms", t);
    }

    @Test
    public void reactorStyle() {
        long t = System.nanoTime();


        t = (System.nanoTime() - t) / 1000000;
        log.info("reactorStyle操作时间：{}ms", t);
    }

}
