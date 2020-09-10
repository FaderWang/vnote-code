package com.fader.vnote.concurrent.task.work;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author FaderW
 * 2019/11/12
 */

public class Shops {

    private static List<Shop> shops = Arrays.asList(new Shop("BestPrice"), new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("BuyItAll"),
            new Shop("ShopEasy"));

    private final ExecutorService executors = new ThreadPoolExecutor(Math.min(shops.size(), 100), Math.min(shops.size(), 100), 0L,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(1024), r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
    }, new ThreadPoolExecutor.AbortPolicy());

    public List<String> findPrices(String product) {
//        return shops.parallelStream()
//                .map(shop -> String.format("%s price is %.2f",
//                        shop.getName(), shop.getPrice(product)))
//                .collect(Collectors.toList());

//        List<CompletableFuture<String>> priceFutures = shops.stream()
//                .map(shop -> CompletableFuture.supplyAsync(
//                        () -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)), executors))
//                .collect(Collectors.toList());
//
//        return priceFutures.stream()
//                .map(CompletableFuture::join)
//                .collect(Collectors.toList());

//        return shops.stream()
//                .map(shop -> shop.getPrice(product))
//                .map(Quote::parse)
//                .map(Discount::applyDiscount)
//                .collect(Collectors.toList());

        List<CompletableFuture<String>> priceFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product), executors))
                .map(future -> future.thenApply(Quote::parse))
//                .map(future -> future.thenCompose(quote ->
//                        CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executors)))
                .map(future -> future.thenApplyAsync(Discount::applyDiscount, executors))
                .collect(Collectors.toList());

        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public Stream<CompletableFuture<String>> findPricesStream(String product) {
        return shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product), executors))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote ->
                    CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executors)));
    }



    public static void main(String[] args) {
        Shops shops = new Shops();
        long start = System.nanoTime();
        CompletableFuture<String>[] completableFutures = shops.findPricesStream("myPhone27s")
                .map(future -> future.thenAccept(
                        s -> System.out.println(s + " done in " + ((System.nanoTime() - start) / 1_000_000) + "msecs")))
                .toArray(size -> new CompletableFuture[size]);

        CompletableFuture.allOf(completableFutures).join();

//        System.out.println(shops.findPrices("myPhone27s"));
//        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("All shops have now responded in "
                + ((System.nanoTime() - start) / 1_000_000) + " msecs");
    }


}
