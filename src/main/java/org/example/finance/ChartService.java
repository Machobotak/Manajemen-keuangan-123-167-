package org.example.finance;

import java.util.LinkedHashMap;
import java.util.Map;

public class ChartService {

    /**
     * Data grafik:
     * - Pemasukan
     * - Pengeluaran
     */
    public static Map<String, Double> getIncomeExpenseChart() {
        Map<String, Double> data = new LinkedHashMap<>();

        data.put("Pemasukan", DashboardService.getTotalPemasukan());
        data.put("Pengeluaran", DashboardService.getTotalPengeluaran());

        return data;
    }
}
