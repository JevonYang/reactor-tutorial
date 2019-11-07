package com.yang.tutorial.observable;

import reactor.core.publisher.Flux;

public class Main {

    public static void main(String[] args) {

        Flux.just("tom", "jack", "allen")
                // fromArray -> onAssembly -> new FluxArray(array)
                .map(s-> "hello, " + s)
                // onAssembly -> new FluxMap<>(this, mapper) this是传入的FluxArray， mapper则为对应的操作
                //               return onAssembly(new FluxMapFuseable<>(this, mapper));
                .subscribe(System.out::println);
                // Flux.subscribe -> new LambdaSubscriber<>() == System.out::println
                // -> Flux.subscribe


        // public final void subscribe(Subscriber<? super T> actual) {
        //		CorePublisher publisher = Operators.onLastAssembly(this); ---> publisher -> FluxMapFuseable
        //		CoreSubscriber subscriber = Operators.toCoreSubscriber(actual); --> actual -> LambdaSubscriber
        //
        //		if (publisher instanceof OptimizableOperator) {
        //			OptimizableOperator operator = (OptimizableOperator) publisher;
        //			while (true) {
        //				subscriber = operator.subscribeOrReturn(subscriber); --> operator -> FluxMapFuseable  subscriber -> new MapFuseableSubscriber<>(actual, mapper);
        //				if (subscriber == null) {
        //					// null means "I will subscribe myself", returning...
        //					return;
        //				}
        //				OptimizableOperator newSource = operator.nextOptimizableSource();
        //				if (newSource == null) {
        //					publisher = operator.source();  --> FluxMapFuseable中取出source，此时的source就是FluxArray
        //					break;
        //				}
        //				operator = newSource;
        //			}
        //		}
        //
        //		publisher.subscribe(subscriber);  LambdaSubscriber订阅FluxArray
        //	}


        /* 真正开始消费过程 */
        // 1. Flux.subscribe(actual, array);
        //              actual -> FluxMapFuseable.MapFuseableSubscriber
        // 2. s.onSubscribe(new ArraySubscription<>(s, array));
        //              s-> FluxMapFuseable.MapFuseableSubscriber
        // 3. FluxMapFuseable.MapFuseableSubscriber.onSubscribe
        //        this.s = (QueueSubscription<T>)
        //		  actual.onSubscribe(this);
        //              this.s -> ArraySubscription
        //				actual -> LambdaSubscriber  this -> MapFuseableSubscriber
        // 4. LambdaSubscriber.onSubscribe -> this.subscription = s; -> s.request(Long.MAX_VALUE);
        //              s -> MapFuseableSubscriber
        // 5. MapFuseableSubscriber.request -> s.request(n);
        //              在第三步骤可以看到s被赋值为ArraySubscription，故s -> ArraySubscription
        // 6. ArraySubscription.request -> fastPath() -> s.onNext(t);
        //              在第二步可以看到s为FluxMapFuseable.MapFuseableSubscriber
        // 7. FluxMapFuseable.MapFuseableSubscriber.onNext -> v = mapper.apply(t) -> actual.onNext(v)
        //          a) t -> 为单个数据  mapper为传入的lambda表达式
        //          b) actual -> LambdaSubscriber
        // 8. LambdaSubscriber.onNext -> consumer.accept(x);
        //          consumer就是写入的lambda表达式

    }
}
