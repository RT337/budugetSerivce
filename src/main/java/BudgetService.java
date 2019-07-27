import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class BudgetService {
    private BudgetRepo repo ;
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMM");

    public BudgetService(BudgetRepo repo) {
        this.repo = repo;
    }

    public BigDecimal query(LocalDate start, LocalDate end) {
        if (end.isBefore(start)) {
            return new BigDecimal(0);
        }

        List<Budget> budgets = repo.getAll();
        YearMonth myYearMonth = YearMonth.from(start);
        List<Budget> foundBudgets = budgets.stream()
                        .filter(budget -> { return budget.getYearMonth().equals(myYearMonth.format(format));})
                        .collect(Collectors.toList());

        if (foundBudgets.size() == 1) {
            return new BigDecimal(foundBudgets.get(0).getAmount());
        }
        return new BigDecimal(0);
    }
}
