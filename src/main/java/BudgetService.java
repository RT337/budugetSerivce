import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

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
            Budget budgetThisMonth = foundBudgets.get(0);
            long monthDays = myYearMonth.lengthOfMonth();
            long days = DAYS.between(start, end) + 1;
            if (days == monthDays) {
                return new BigDecimal(budgetThisMonth.getAmount());
            }
            long budgetPerDay = budgetThisMonth.getAmount() / monthDays;
            return new BigDecimal(budgetPerDay * days);
        }
        return new BigDecimal(0);
    }
}
