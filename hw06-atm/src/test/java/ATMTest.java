import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kt.ATMService;
import org.kt.BanknoteType;
import org.kt.Cash;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ATMTest {
    ATMService service = new ATMService();

    @BeforeEach
    void initATM() {
        service.put(BanknoteType.R5000, 2);
        service.put(BanknoteType.R2000, 3);
        service.put(BanknoteType.R1000, 4);
        service.put(BanknoteType.R500, 7);
        service.put(BanknoteType.R100, 1);
        service.put(BanknoteType.R50, 3);
        service.put(BanknoteType.R10, 4);
    }

    @Test
    @DisplayName("Проверяем, что удалось снять деньги")
    void withdrawCashTest() {
        Optional<Cash> result = service.withdrawCash(7230);

        Cash correctResult = new Cash();
        // 7230 = 5000 + 2000 + 100 + 50*2 + 10*3
        correctResult.put(BanknoteType.R5000, 1);
        correctResult.put(BanknoteType.R2000, 1);
        correctResult.put(BanknoteType.R100, 1);
        correctResult.put(BanknoteType.R50, 2);
        correctResult.put(BanknoteType.R10, 3);

        //then
        assertThat(result).isPresent();
        assertThat(result.get().toString()).isEqualTo(correctResult.toString());
    }

    @Test
    @DisplayName("Проверяем остаток, после снятия денег")
    void balanceTest() {
        service.withdrawCash(7230);

        Cash correctBalance = new Cash();
        correctBalance.put(BanknoteType.R5000, 1);
        correctBalance.put(BanknoteType.R2000, 2);
        correctBalance.put(BanknoteType.R1000, 4);
        correctBalance.put(BanknoteType.R500, 7);
        correctBalance.put(BanknoteType.R100, 0);
        correctBalance.put(BanknoteType.R50, 1);
        correctBalance.put(BanknoteType.R10, 1);

        //then
        assertThat(service.getBalanceReport()).isEqualTo(correctBalance.toString());
    }

    @Test
    @DisplayName("Проверяем не успешное снятие")
    void failedWithdrawCashTest() {
        Optional<Cash> result = service.withdrawCash(100000);
        Assertions.assertFalse(result.isPresent());
    }
}
