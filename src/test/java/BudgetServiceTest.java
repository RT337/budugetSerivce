import junit.framework.TestCase;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BudgetServiceTest extends TestCase{

    private BudgetService budgetService;
    private BudgetRepo repo;
    @Override protected void setUp() throws Exception {
        super.setUp();
        repo = mock(BudgetRepo.class);
        budgetService = new BudgetService(repo);
    }

    public void testInvalidQueryRange() {
        LocalDate start = LocalDate.of(2019,3,31);
        LocalDate end = LocalDate.of(2019,3,1);
        budgetOfRangeShouldBe(new BigDecimal(0) , start, end);
    }

    public void testNoBudgetDataWithValidQueryRange() {
        when(repo.getAll()).thenReturn(Collections.emptyList());
        LocalDate start = LocalDate.of(2019,3,1);
        LocalDate end = LocalDate.of(2019,3,31);
        budgetOfRangeShouldBe(new BigDecimal(0) , start, end);
    }

    public void testOneMonthBudgetData(){
        when(repo.getAll()).thenReturn(Arrays.asList(new Budget("201903", 3100)));
        LocalDate start = LocalDate.of(2019,3,1);
        LocalDate end = LocalDate.of(2019,3,31);
        budgetOfRangeShouldBe(new BigDecimal(3100) , start, end);
    }

    public void test_partial_month_budget_data(){
        when(repo.getAll()).thenReturn(Arrays.asList(new Budget("201904", 3000)));
        LocalDate start = LocalDate.of(2019,4,15);
        LocalDate end = LocalDate.of(2019,4,20);
        budgetOfRangeShouldBe(new BigDecimal(600) , start, end);
    }

    private void budgetOfRangeShouldBe(BigDecimal expected, LocalDate start, LocalDate end) {
        assertEquals(expected , budgetService.query(start, end));
    }

}
