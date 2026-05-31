package com.pao.laboratory11.exercise2;

import com.pao.laboratory11.exercise1.Main.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        try {
            run();
        } catch (IOException e) {
            // Keep deterministic checker output.
        }
    }

    private static void run() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String first = nextNonEmpty(br);
        if (first == null) {
            return;
        }

        int n = Integer.parseInt(first);
        List<AccountTransaction> txs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String line = nextNonEmpty(br);
            if (line == null) {
                return;
            }

            String[] p = line.split("\\s+");
            Transaction tx = new Transaction(
                    Integer.parseInt(p[0]),
                    Double.parseDouble(p[1]),
                    p[2],
                    p[3],
                    p[4]);
            txs.add(new AccountTransaction(tx, p[5]));
        }

        int q = Integer.parseInt(nextNonEmpty(br));
        for (int i = 0; i < q; i++) {
            String line = nextNonEmpty(br);
            if (line == null) {
                return;
            }

            String[] p = line.split("\\s+");
            String op = p[0];

            switch (op) {
                case "REPORT_MONTH": {
                    String month = p[1];
                    List<AccountTransaction> matching = txs.stream()
                            .filter(tx -> tx.transaction.getDate().startsWith(month))
                            .collect(Collectors.toList());
                    double total = matching.stream()
                            .mapToDouble(tx -> tx.transaction.getAmount())
                            .sum();
                    long count = matching.size();
                    System.out.printf(Locale.US, "MONTH %s total=%.2f count=%d%n", month, total, count);
                    break;
                }

                case "REPORT_ACCOUNT": {
                    String account = p[1];
                    List<AccountTransaction> matching = txs.stream()
                            .filter(tx -> tx.accountId.equals(account))
                            .collect(Collectors.toList());
                    double total = matching.stream()
                            .mapToDouble(tx -> tx.transaction.getAmount())
                            .sum();
                    long count = matching.size();
                    System.out.printf(Locale.US, "ACCOUNT %s total=%.2f count=%d%n", account, total, count);
                    break;
                }

                case "TOP_CHANNELS": {
                    int k = Integer.parseInt(p[1]);
                    Map<String, Long> counts = txs.stream()
                            .collect(Collectors.groupingBy(
                                    tx -> tx.transaction.getChannel(),
                                    Collectors.counting()));

                    List<Map.Entry<String, Long>> entries = new ArrayList<>(counts.entrySet());
                    entries.sort(Comparator
                            .comparingLong((Map.Entry<String, Long> e) -> e.getValue()).reversed()
                            .thenComparing(Map.Entry::getKey));

                    if (entries.isEmpty()) {
                        System.out.println("NONE");
                        break;
                    }

                    int limit = Math.min(k, entries.size());
                    for (int idx = 0; idx < limit; idx++) {
                        Map.Entry<String, Long> e = entries.get(idx);
                        System.out.println(e.getKey() + " " + e.getValue());
                    }
                    break;
                }

                default:
                    // Ignore unknown commands.
                    break;
            }
        }
    }

    private static String nextNonEmpty(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                return line.trim();
            }
        }
        return null;
    }

    private static final class AccountTransaction {
        private final Transaction transaction;
        private final String accountId;

        private AccountTransaction(Transaction transaction, String accountId) {
            this.transaction = transaction;
            this.accountId = accountId;
        }
    }
}
