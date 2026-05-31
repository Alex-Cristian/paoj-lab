package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

public class Main {
    public static void main(String[] args) {
        List<Transaction> transactions = Arrays.asList(
                new Transaction(1, new BigDecimal("200.00"), LocalDate.parse("2026-05-01"), "RO", "WEB"),
                new Transaction(2, new BigDecimal("700.00"), LocalDate.parse("2026-05-03"), "RO", "APP"),
                new Transaction(3, new BigDecimal("700.00"), LocalDate.parse("2026-05-04"), "DE", "WEB"),
                new Transaction(4, new BigDecimal("120.00"), LocalDate.parse("2026-06-02"), "NL", "ATM"),
                new Transaction(5, new BigDecimal("5000.00"), LocalDate.parse("2026-06-12"), "RO", "CRYPTO"));

        Snapshot snapshot = transactions.stream().collect(TransactionCollectors.toSnapshot(3));

        System.out.println("TOTAL " + snapshot.getTotalAmount());

        System.out.println("TOP_TRANSACTIONS");
        snapshot.getTopTransactions().forEach(System.out::println);

        System.out.println("COUNT_BY_COUNTRY");
        snapshot.getCountByCountry().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(entry -> System.out.println(entry.getKey() + " " + entry.getValue()));

        System.out.println("TOTAL_BY_MONTH");
        snapshot.getTotalByMonth().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.println(entry.getKey() + " " + entry.getValue()));
    }

    private static final class Transaction {
        private final int id;
        private final BigDecimal amount;
        private final LocalDate date;
        private final String country;
        private final String channel;

        private Transaction(int id, BigDecimal amount, LocalDate date, String country, String channel) {
            this.id = id;
            this.amount = amount;
            this.date = date;
            this.country = country;
            this.channel = channel;
        }

        private YearMonth month() {
            return YearMonth.from(date);
        }

        @Override
        public String toString() {
            return "[" + id + "] " + amount + " " + date + " " + country + " " + channel;
        }
    }

    private static final class Snapshot {
        private final Map<String, Long> countByCountry;
        private final Map<String, Long> countByChannel;
        private final Map<YearMonth, BigDecimal> totalByMonth;
        private final BigDecimal totalAmount;
        private final List<Transaction> topTransactions;

        private Snapshot(
                Map<String, Long> countByCountry,
                Map<String, Long> countByChannel,
                Map<YearMonth, BigDecimal> totalByMonth,
                BigDecimal totalAmount,
                List<Transaction> topTransactions) {
            this.countByCountry = Collections.unmodifiableMap(new HashMap<>(countByCountry));
            this.countByChannel = Collections.unmodifiableMap(new HashMap<>(countByChannel));
            this.totalByMonth = Collections.unmodifiableMap(new HashMap<>(totalByMonth));
            this.totalAmount = totalAmount;
            this.topTransactions = Collections.unmodifiableList(new ArrayList<>(topTransactions));
        }

        private Map<String, Long> getCountByCountry() {
            return countByCountry;
        }

        private Map<String, Long> getCountByChannel() {
            return countByChannel;
        }

        private Map<YearMonth, BigDecimal> getTotalByMonth() {
            return totalByMonth;
        }

        private BigDecimal getTotalAmount() {
            return totalAmount;
        }

        private List<Transaction> getTopTransactions() {
            return topTransactions;
        }
    }

    private static final class TransactionCollectors {
        private static Collector<Transaction, ?, Snapshot> toSnapshot(int topN) {
            class Accumulator {
                private final Map<String, Long> countByCountry = new HashMap<>();
                private final Map<String, Long> countByChannel = new HashMap<>();
                private final Map<YearMonth, BigDecimal> totalByMonth = new HashMap<>();
                private final List<Transaction> all = new ArrayList<>();
                private BigDecimal totalAmount = BigDecimal.ZERO;

                private void add(Transaction tx) {
                    countByCountry.merge(tx.country, 1L, Long::sum);
                    countByChannel.merge(tx.channel, 1L, Long::sum);
                    totalByMonth.merge(tx.month(), tx.amount, BigDecimal::add);
                    totalAmount = totalAmount.add(tx.amount);
                    all.add(tx);
                }

                private Accumulator combine(Accumulator other) {
                    other.countByCountry.forEach((key, value) -> countByCountry.merge(key, value, Long::sum));
                    other.countByChannel.forEach((key, value) -> countByChannel.merge(key, value, Long::sum));
                    other.totalByMonth.forEach((key, value) -> totalByMonth.merge(key, value, BigDecimal::add));
                    totalAmount = totalAmount.add(other.totalAmount);
                    all.addAll(other.all);
                    return this;
                }

                private Snapshot finish() {
                    List<Transaction> top = all.stream()
                            .sorted(Comparator
                                    .comparing((Transaction tx) -> tx.amount, Comparator.reverseOrder())
                                    .thenComparingInt(tx -> tx.id))
                            .limit(Math.max(0, topN))
                            .collect(java.util.stream.Collectors.toList());
                    return new Snapshot(countByCountry, countByChannel, totalByMonth, totalAmount, top);
                }
            }

            return Collector.of(Accumulator::new, Accumulator::add, Accumulator::combine, Accumulator::finish);
        }
    }
}
