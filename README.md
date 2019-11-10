<img src="https://avatars2.githubusercontent.com/u/25752188?v=4" width="50" height="50"> Reactor-Tutorial
===

## Why Reactor?

### Reactor vs. Stream vs. CompletableFuture

## 基础 Reactor-Basic

### Flux/Mono

### lifecycle

## 线程模型 Reactor-Schedulers

## 反压 BackPressure

## 应用 lettuce(reactive-redis)

## WebFlux调用过程
```
TcpServerBind.onStateChange -> ChannelOperationsHandler.channelRead开始
-> HttpServerOperations.onInboundNext -> TcpServerBind.onStateChange
-> HttpServerHandle.onStateChange -> ReactorHttpHandlerAdapter.apply
-> ReactiveWebServerApplicationContext.handle -> HttpWebHandlerAdapter.handle
-> HttpServerHandle.onStateChange.subscribe -> DispatcherHandler.handle -> Mono.subscribe过程
-> DispatcherHandler.invokeHandler -> RequestMappingHandlerAdapter.handle
-> InvocableHandlerMethod.invoke -> 反射 -> Mono.just("world")
-> DispatcherHandler.handleResult -> DispatcherHandler.getResultHandler -> Mono.subscribe过程
-> ChannelOperationsHandler.channelRead结束 -> xxx -> TcpServerBind.onStateChange结束
```
## 实战 EventBus